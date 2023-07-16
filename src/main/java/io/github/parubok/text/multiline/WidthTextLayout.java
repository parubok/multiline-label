package io.github.parubok.text.multiline;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.List;
import java.util.Set;

import static javax.swing.plaf.basic.BasicGraphicsUtils.getStringWidth;

/**
 * Dynamically calculates line breaks based on the current label width.
 * <p>
 * This is the default text layout for {@link MultilineLabel}.
 */
final class WidthTextLayout extends AbstractTextLayout {

    // order is important
    private static final List<String> LINE_SEPARATORS = List.of("\r\n", "\n", "\r");

    static void paintText(JComponent c, Graphics g, String text, Insets insets, int wLimit, boolean enabled,
                          Color background, float lineSpacing, Set<Character> separators) {
        paintText2(c, g, toRenderedText(text), insets, wLimit, enabled, background, lineSpacing,
                maxLinesForComponent(c), separators);
    }

    private static void paintText2(JComponent c, Graphics g, String text, Insets insets, int wLimit, boolean enabled,
                                   Color background, float lineSpacing, int maxLines, Set<Character> separators) {
        if (text.isEmpty()) {
            return;
        }
        final int wLimitText = wLimit - insets.right - insets.left;
        if (wLimitText < 1) {
            return;
        }
        final var fm = g.getFontMetrics();
        final int x = insets.left;
        int y = insets.top + fm.getAscent();
        final int yIncrement = MultilineUtils.getHeightIncrement(fm, lineSpacing);
        NextLine nextLine;
        int index = 0;
        int lineCount = 0;
        do {
            nextLine = getNextLine(c, text, index, fm, wLimitText, separators);
            String lineStr = nextLine.stringToPaint(text, ++lineCount, maxLines);
            if (enabled) {
                drawString(c, g, lineStr, x, y);
            } else {
                drawStringInDisabledStyle(c, lineStr, g, background, x, y);
            }
            y += yIncrement;
            index = nextLine.nextLineStartIndex;
        } while (nextLine.hasMoreLines(lineCount, maxLines));
    }

    static Dimension calcPreferredSize(JComponent c, Insets insets, FontMetrics fm, String text, int wLimit,
                                       float lineSpacing, Set<Character> separators) {
        return calcPreferredSize2(c, insets, fm, toRenderedText(text), wLimit, lineSpacing, maxLinesForComponent(c),
                separators);
    }

    private static int maxLinesForComponent(JComponent c) {
        if (c instanceof MultilineLabel) {
            return ((MultilineLabel) c).getMaxLines();
        }
        return MultilineLabel.DEFAULT_MAX_LINES;
    }

    private static Dimension calcPreferredSize2(JComponent c, Insets insets, FontMetrics fm, String text, int wLimit,
                                                float lineSpacing, int maxLines, Set<Character> separators) {
        assert insets != null;
        assert fm != null;
        assert text != null;
        assert wLimit > 0;

        int textPrefWidth;
        int textPrefHeight;
        if (!text.isEmpty()) {
            NextLine nextLine;
            int startIndex = 0;
            final int textWidthLimit = Math.max(1, wLimit - insets.right - insets.left);
            int lineCount = 0;
            int maxLineWidth = 0; // pixels
            do {
                nextLine = getNextLine(c, text, startIndex, fm, textWidthLimit, separators);
                String nextLineStr = nextLine.stringToPaint(text, ++lineCount, maxLines);
                int nextLineWidth = Math.round(getStringWidth(c, fm, nextLineStr));
                maxLineWidth = Math.max(maxLineWidth, nextLineWidth);
                startIndex = nextLine.nextLineStartIndex;
            } while (nextLine.hasMoreLines(lineCount, maxLines));
            textPrefWidth = maxLineWidth;
            textPrefHeight = getTextPreferredHeight(lineCount, fm, lineSpacing);
        } else {
            textPrefWidth = textPrefHeight = 0;
        }
        return MultilineUtils.toDimension(textPrefWidth, textPrefHeight, insets);
    }

    static int getSeparatorIndex(String text, int fromIndex, Set<Character> separators) {
        return separators.stream()
                .mapToInt(s -> text.indexOf(Character.toString(s), fromIndex))
                .filter(index -> index > -1)
                .min()
                .orElse(-1);
    }

    private static boolean isWhitespace(String text, int index) {
        return Character.isWhitespace(text.charAt(index));
    }

    /**
     * @param c Component to paint the text on it. Not necessarily {@link MultilineLabel}. May be null.
     * @param text Text to display in {@link MultilineLabel}.
     * @param startIndex Index of 1st character in the new line.
     * @param fm Current {@link FontMetrics}.
     * @param widthLimit Limit on the width of the line (in pixels).
     * @return Object with details of the next line.
     */
    static NextLine getNextLine(JComponent c, String text, int startIndex, FontMetrics fm, int widthLimit,
                                Set<Character> separators) {
        assert text != null;
        assert text.length() > 0;
        assert startIndex > -1;
        assert fm != null;
        assert widthLimit > 0;

        // if there is a line separator before the width limit - return line before the separator
        // (we assume that, for a string, all line separators are identical)
        for (String lineSep : LINE_SEPARATORS) {
            int lineSepIndex = text.indexOf(lineSep, startIndex);
            if (lineSepIndex > -1) {
                String sub = text.substring(startIndex, lineSepIndex);
                if (getSeparatorIndex(sub, 0, separators) == -1 || getStringWidth(c, fm, sub) <= widthLimit) {
                    return new NextLine(false, startIndex, lineSepIndex - 1, lineSepIndex + lineSep.length());
                } else {
                    break;
                }
            }
        }

        // note: if separator is a whitespace - exclude it from the text, o/w keep it as part of the line
        int sepIndex = startIndex;
        while (true) {
            int nextSepIndex = getSeparatorIndex(text, sepIndex + 1, separators);
            if (nextSepIndex == -1) { // there is no next separator after sepIndex
                if (sepIndex > startIndex && getStringWidth(c, fm, text.substring(startIndex)) > widthLimit) {
                    // next line will be single word last line
                    return new NextLine(false, startIndex, sepIndex - (isWhitespace(text, sepIndex) ? 1 : 0), sepIndex + 1);
                } else {
                    // last line
                    return new NextLine(true, startIndex, text.length() - 1, -1);
                }
            } else { // there is next separator after sepIndex
                String sub = text.substring(startIndex, nextSepIndex + (isWhitespace(text, nextSepIndex) ? 0 : 1));
                if (getStringWidth(c, fm, sub) > widthLimit) {
                    int sIndex;
                    if (sepIndex > startIndex) {
                        // regular next line
                        sIndex = sepIndex;
                    } else {
                        // single word line (dishonors width limit!)
                        assert sepIndex == startIndex;
                        sIndex = nextSepIndex;
                    }
                    return new NextLine(false, startIndex, sIndex - (isWhitespace(text, sIndex) ? 1 : 0), sIndex + 1);
                } else {
                    sepIndex = nextSepIndex; // continue with current line
                }
            }
        }
    }

    static String toRenderedText(String text) {
        var sb = new StringBuilder(text.strip());
        int doubleSpaceIndex;
        while ((doubleSpaceIndex = sb.indexOf("  ")) > -1) {
            sb.delete(doubleSpaceIndex + 1, doubleSpaceIndex + 2); // delete second space
        }
        return sb.toString();
    }

    private final String textToRender;

    WidthTextLayout(MultilineLabel label) {
        super(label);
        this.textToRender = toRenderedText(label.getText());
    }

    @Override
    public Dimension calculatePreferredSize() {
        return calcPreferredSize(0);
    }

    private void requestLayout() {
        SwingUtilities.invokeLater(() -> {
            label.revalidate();
            label.repaint();
        });
    }

    @Override
    public void preSetBounds(int x, int y, int width, int height) {
        if (label.isUseCurrentWidthForPreferredSize()
                && !textToRender.isEmpty()
                && width > 0 && height > 0
                && width != label.getWidth()
                && calcPreferredSize(width).height != height) {
            requestLayout();
        }
    }

    /**
     * @param expectedLabelWidth Expected label width. Ignored if less than 1.
     */
    private Dimension calcPreferredSize(int expectedLabelWidth) {
        final int wLimit;
        if (expectedLabelWidth > 0) {
            wLimit = expectedLabelWidth;
        } else if (label.isUseCurrentWidthForPreferredSize() && label.getWidth() > 0) {
            // https://stackoverflow.com/questions/39455573/how-to-set-fixed-width-but-dynamic-height-on-jtextpane/39466255#39466255
            wLimit = label.getWidth();
        } else {
            wLimit = label.getPreferredWidthLimit();
        }
        final var fm = label.getFontMetrics(label.getFont());
        final var insets = label.getInsets();
        return calcPreferredSize2(label, insets, fm, textToRender, wLimit, label.getLineSpacing(), label.getMaxLines(),
                label.getSeparators());
    }

    @Override
    public void paintText(Graphics g) {
        paintText2(label, g, textToRender, label.getInsets(), label.getWidth(), label.isEnabled(),
                label.getBackground(), label.getLineSpacing(), label.getMaxLines(), label.getSeparators());
    }
}
