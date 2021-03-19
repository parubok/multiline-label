package org.swingk.text.multiline;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javax.swing.plaf.basic.BasicGraphicsUtils.getStringWidth;

/**
 * Text layout where line breaks are provided in the text by line separators.
 * @implNote {@link String#lines()} is used to separate text into lines.
 */
final class ProvidedTextLayout extends AbstractTextLayout {

    private final List<String> lines;

    ProvidedTextLayout(MultilineLabel label) {
        super(label);
        this.lines = breakToLines(label.getText());
    }

    List<String> getLines() { return lines; }

    private static Stream<String> lineStream(String text) {
        return text.strip().lines();
    }

    private static List<String> breakToLines(String text) {
        return lineStream(text).map(String::strip).collect(Collectors.toUnmodifiableList());
    }

    static boolean hasLines(String text) {
        return lineStream(text).skip(1).findFirst().isPresent();
    }

    /**
     * @param c Component. If not null, its AA settings will be used for the painting.
     * @param g Not null.
     * @param text Text to paint. Not null.
     * @param insets Insets to honor when painting. Not null.
     * @param enabled Paint text as enabled or disabled.
     * @param backgroundColor Needed to paint disabled text. Not null.
     */
    static void paintText(JComponent c, Graphics g, String text, Insets insets, boolean enabled, Color backgroundColor,
                          float lineSpacing) {
        paintText2(c, g, breakToLines(text), insets, enabled, backgroundColor, lineSpacing);
    }

    private static void paintText2(JComponent c, Graphics g, List<String> lines, Insets insets, boolean enabled,
                                   Color background, float lineSpacing) {
        final var fm = g.getFontMetrics();
        final int x = insets.left;
        int y = insets.top + fm.getAscent();
        final int yIncrement = MultilineUtils.getHeightIncrement(fm, lineSpacing);
        for (String line : lines) {
            if (enabled) {
                drawString(c, g, line, x, y);
            } else {
                drawStringInDisabledStyle(c, line, g, background, x, y);
            }
            y += yIncrement;
        }
    }

    @Override
    public void paintText(Graphics g) {
        paintText2(label, g, lines, label.getInsets(), label.isEnabled(), label.getBackground(),
                label.getLineSpacing());
    }

    static Dimension calcPreferredSize(JComponent c, String text, FontMetrics fm, Insets insets, float lineSpacing) {
        return calcPreferredSize(c, breakToLines(text), fm, insets, lineSpacing);
    }

    static Dimension calcPreferredSize(JComponent c, List<String> lines, FontMetrics fm, Insets insets,
                                       float lineSpacing) {
        assert lines != null;
        assert fm != null;
        assert insets != null;

        final int textPrefWidth;
        final int textPrefHeight;
        if (!lines.isEmpty()) {
            int maxLineWidth = 0;
            for (String line : lines) {
                maxLineWidth = Math.max(maxLineWidth, Math.round(getStringWidth(c, fm, line)));
            }
            textPrefWidth = maxLineWidth;
            textPrefHeight = getTextPreferredHeight(lines.size(), fm, lineSpacing);
        } else {
            textPrefWidth = textPrefHeight = 0;
        }
        return MultilineUtils.toDimension(textPrefWidth, textPrefHeight, insets);
    }

    @Override
    public Dimension calculatePreferredSize() {
        return calcPreferredSize(label, lines, label.getFontMetrics(label.getFont()), label.getInsets(),
                label.getLineSpacing());
    }

    @Override
    public void preSetBounds(int x, int y, int width, int height) {
        // do nothing since this layout doesn't depend on the current bounds
    }
}
