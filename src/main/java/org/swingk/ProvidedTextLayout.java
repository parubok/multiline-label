package org.swingk;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.swing.SwingUtilities.computeStringWidth;
import static org.swingk.MultilineLabel.LINE_SEPARATOR_UNIX;
import static org.swingk.MultilineLabel.LINE_SEPARATOR_WIN;
import static org.swingk.MultilineLabel.paintTextInDisabledStyle;

/**
 * Text layout where line breaks are provided in the text by line separator characters.
 */
public class ProvidedTextLayout implements TextLayout {

    private final MultilineLabel label;
    private final List<String> lines;
    private final String lineSeparator;

    public ProvidedTextLayout(MultilineLabel label) {
        this.label = Objects.requireNonNull(label);
        this.lineSeparator = guessLineSeparator(label.getText());
        this.lines = breakToLines(label.getText(), this.lineSeparator);
    }

    String getLineSeparator() {
        return lineSeparator;
    }

    List<String> getLines() {
        return lines;
    }

    private static String guessLineSeparator(String text) {
        return text.contains(LINE_SEPARATOR_WIN) ? LINE_SEPARATOR_WIN : LINE_SEPARATOR_UNIX;
    }

    private static List<String> breakToLines(String text, String lineSeparator) {
        String t = text.trim();
        StringBuilder sb = new StringBuilder();
        List<String> lines = new ArrayList<>();
        final int len = t.length();
        for (int i = 0; i < len; i++) {
            if (t.startsWith(lineSeparator, i)) {
                addLine(sb, lines);
                sb = new StringBuilder();
                i += (lineSeparator.length() - 1);
            } else {
                sb.append(t.charAt(i));
                if (i == (len - 1)) {
                    addLine(sb, lines);
                }
            }
        }
        return lines;
    }

    private static void addLine(StringBuilder sb, List<String> lines) {
        lines.add(sb.toString().trim());
    }

    static void paintText(Graphics g, String text, Insets insets, boolean enabled, Color backgroundColor) {
        paintText2(g, breakToLines(text, guessLineSeparator(text)), insets, enabled, backgroundColor);
    }

    private static void paintText2(Graphics g, List<String> lines, Insets insets, boolean enabled, Color backgroundColor) {
        final FontMetrics fm = g.getFontMetrics();
        final int x = insets.left;
        int y = insets.top + fm.getAscent();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (enabled) {
                g.drawString(line, x, y);
            } else {
                paintTextInDisabledStyle(line, g, backgroundColor, x, y);
            }
            y += fm.getHeight();
        }
    }

    @Override
    public void paintText(Graphics g) {
        paintText2(g, lines, label.getInsets(), label.isEnabled(), label.getBackground());
    }

    static Dimension calcPreferredSize(String text, FontMetrics fm, Insets insets) {
        return calcPreferredSize(breakToLines(text, guessLineSeparator(text)), fm, insets);
    }

    static Dimension calcPreferredSize(List<String> lines, FontMetrics fm, Insets insets) {
        assert fm != null;
        final int textPrefWidth;
        final int textPrefHeight;
        if (!lines.isEmpty()) {
            final int lineCount = lines.size();
            int maxLineWidth = 0;
            for (int i = 0; i < lineCount; i++) {
                maxLineWidth = Math.max(maxLineWidth, computeStringWidth(fm, lines.get(i)));
            }
            textPrefWidth = maxLineWidth;
            textPrefHeight = (fm.getAscent() + fm.getDescent()) * lineCount + fm.getLeading() * (lineCount - 1);
        } else {
            textPrefWidth = textPrefHeight = 0;
        }
        return new Dimension(textPrefWidth + insets.right + insets.left, textPrefHeight + insets.top + insets.bottom);
    }

    @Override
    public Dimension calculatePreferredSize() {
        return calcPreferredSize(lines, label.getFontMetrics(label.getFont()), label.getInsets());
    }

    @Override
    public void preSetBounds(int x, int y, int width, int height) {
        // do nothing since this layout doesn't depend on the current bounds
    }
}
