package org.swingk;

import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Objects;

import static javax.swing.SwingUtilities.computeStringWidth;
import static org.swingk.MultilineLabelUtils.paintTextInDisabledStyle;

/**
 * Dynamically calculates line breaks based on value of {@link MultilineLabel#getPreferredScrollableViewportSize()}
 * or the current label width. Ignores line breaks in text by replacing them with spaces.
 */
final class WidthTextLayout implements TextLayout {

    static String toRenderedText(String text) {
        StringBuilder sb = new StringBuilder(text.replace('\n', ' ').trim());
        int doubleSpaceIndex;
        while ((doubleSpaceIndex = sb.indexOf("  ")) > -1) {
            sb.delete(doubleSpaceIndex + 1, doubleSpaceIndex + 2); // delete second space
        }
        return sb.toString();
    }

    private final MultilineLabel label;
    private final String textToRender;

    public WidthTextLayout(MultilineLabel label) {
        this.label = Objects.requireNonNull(label);
        this.textToRender = toRenderedText(label.getText());
    }

    @Override
    public Dimension calculatePreferredSize() {
        return calcPreferredSize(0);
    }

    @Override
    public void preSetBounds(int x, int y, int width, int height) {
        if (width > 0 && height > 0 && width != label.getWidth() && calcPreferredSize(width).height != height) {
            SwingUtilities.invokeLater(() -> {
                label.revalidate();
                label.repaint();
            });
        }
    }

    /**
     * @param expectedLabelWidth If 0 - use fallback resolution.
     */
    protected Dimension calcPreferredSize(int expectedLabelWidth) {
        Insets insets = label.getInsets();
        final int horInsets = insets.right + insets.left;
        int textPrefWidth;
        int textPrefHeight;
        if (!textToRender.isEmpty()) {
            final FontMetrics fm = label.getFontMetrics(label.getFont());
            assert fm != null;
            MultilineLabelUtils.NextLine nextLine;
            int startIndex = 0;
            final int wLimit;
            if (expectedLabelWidth > 0) {
                wLimit = expectedLabelWidth;
            } else if (label.getWidth() > 0) {
                // https://stackoverflow.com/questions/39455573/how-to-set-fixed-width-but-dynamic-height-on-jtextpane/39466255#39466255
                wLimit = label.getWidth();
            } else {
                wLimit = label.getPreferredWidthLimit();
            }
            final int textWidthLimit = Math.max(1, wLimit - horInsets);
            int lineCount = 0;
            int maxLineWidth = 0; // pixels
            do {
                nextLine = MultilineLabelUtils.getNextLine(textToRender, startIndex, fm, textWidthLimit);
                String nextLineStr = textToRender.substring(nextLine.lineStartIndex, nextLine.lineEndIndex + 1);
                int nextLineWidth = computeStringWidth(fm, nextLineStr);
                maxLineWidth = Math.max(maxLineWidth, nextLineWidth);
                lineCount++;
                startIndex = nextLine.nextLineStartIndex;
            } while (!nextLine.lastLine);
            textPrefWidth = maxLineWidth;
            textPrefHeight = (fm.getAscent() + fm.getDescent()) * lineCount + fm.getLeading() * (lineCount - 1);
        } else {
            textPrefWidth = textPrefHeight = 0;
        }
        return new Dimension(textPrefWidth + horInsets, textPrefHeight + insets.top + insets.bottom);
    }

    @Override
    public void paintText(Graphics g) {
        if (textToRender.isEmpty()) {
            return;
        }
        Insets insets = label.getInsets();
        int textWidth = (label.getWidth() - insets.left - insets.right);
        if (textWidth > 1) {
            g.setFont(label.getFont());
            FontMetrics fm = g.getFontMetrics();
            g.setColor(label.getForeground());
            int x = insets.left;
            int y = insets.top + fm.getAscent();
            boolean enabled = label.isEnabled();
            MultilineLabelUtils.NextLine nextLine;
            int index = 0;
            int widthLimit = label.getWidth() - insets.right - insets.left;
            do {
                nextLine = MultilineLabelUtils.getNextLine(textToRender, index, fm, widthLimit);
                String lineStr = textToRender.substring(nextLine.lineStartIndex, nextLine.lineEndIndex + 1);
                if (enabled) {
                    g.drawString(lineStr, x, y);
                } else {
                    paintTextInDisabledStyle(lineStr, g, label.getBackground(), x, y);
                }
                y += fm.getHeight();
                index = nextLine.nextLineStartIndex;
            } while (!nextLine.lastLine);
        }
    }
}
