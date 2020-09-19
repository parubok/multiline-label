package org.swingk;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.swing.SwingUtilities.computeStringWidth;
import static org.swingk.MultilineLabelUtils.paintTextInDisabledStyle;

/**
 * Text layout where line breaks are provided in the text by EOL ('\n') characters.
 */
public class ProvidedTextLayout implements TextLayout {
    private final MultilineLabel label;
    private final List<String> lines;

    public ProvidedTextLayout(MultilineLabel label) {
        this.label = Objects.requireNonNull(label);
        this.lines = toLines(label.getText());
    }

    static List<String> toLines(String text) {
        List<String> lines = new ArrayList<>();
        String t = text.trim();
        StringBuilder sb = new StringBuilder();
        final int len = t.length();
        for (int i = 0; i < len; i++) {
            char c = t.charAt(i);
            if (c == '\n') {
                addLine(lines, sb);
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
            if (i == (len - 1)) {
                addLine(lines, sb);
            }
        }
        return lines;
    }

    private static void addLine(List<String> lines, StringBuilder sb) {
        lines.add(sb.toString().trim());
    }

    @Override
    public void paintText(Graphics g) {
        final Insets insets = label.getInsets();
        final FontMetrics fm = g.getFontMetrics();
        final int x = insets.left;
        int y = insets.top + fm.getAscent();
        final boolean enabled = label.isEnabled();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (enabled) {
                g.drawString(line, x, y);
            } else {
                paintTextInDisabledStyle(line, g, label.getBackground(), x, y);
            }
            y += fm.getHeight();
        }
    }

    @Override
    public Dimension calculatePreferredSize() {
        Insets insets = label.getInsets();
        final int horInsets = insets.right + insets.left;
        final int textPrefWidth;
        final int textPrefHeight;
        if (!lines.isEmpty()) {
            final FontMetrics fm = label.getFontMetrics(label.getFont());
            assert fm != null;
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
        return new Dimension(textPrefWidth + horInsets, textPrefHeight + insets.top + insets.bottom);
    }

    @Override
    public void preSetBounds(int x, int y, int width, int height) {
        // do nothing since this layout doesn't depend on the current bounds
    }

    List<String> getLines() {
        return lines;
    }
}
