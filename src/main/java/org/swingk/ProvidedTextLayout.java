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
import static org.swingk.MultilineLabelUtils.LINE_SEPARATOR_WIN;
import static org.swingk.MultilineLabelUtils.LINE_SEPARATOR_UNIX;

/**
 * Text layout where line breaks are provided in the text by line separator characters.
 */
public class ProvidedTextLayout implements TextLayout {

    private final MultilineLabel label;
    private final List<String> lines;
    private final String lineSeparator;

    public ProvidedTextLayout(MultilineLabel label) {
        this.label = Objects.requireNonNull(label);
        this.lineSeparator = guessLineSeparator();
        this.lines = new ArrayList<>();
        breakToLines();
    }

    protected String getLineSeparator() {
        return lineSeparator;
    }

    protected List<String> getLines() {
        return lines;
    }

    private String guessLineSeparator() {
        String text = label.getText();
        return text.contains(LINE_SEPARATOR_WIN) ? LINE_SEPARATOR_WIN : LINE_SEPARATOR_UNIX;
    }

    private void breakToLines() {
        String t = label.getText().trim();
        StringBuilder sb = new StringBuilder();
        final int len = t.length();
        for (int i = 0; i < len; i++) {
            if (t.startsWith(lineSeparator, i)) {
                addLine(sb);
                sb = new StringBuilder();
                i += (lineSeparator.length() - 1);
            } else {
                sb.append(t.charAt(i));
                if (i == (len - 1)) {
                    addLine(sb);
                }
            }
        }
    }

    private void addLine(StringBuilder sb) {
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
        Insets insets = label.getInsets();
        return new Dimension(textPrefWidth + insets.right + insets.left, textPrefHeight + insets.top + insets.bottom);
    }

    @Override
    public void preSetBounds(int x, int y, int width, int height) {
        // do nothing since this layout doesn't depend on the current bounds
    }
}
