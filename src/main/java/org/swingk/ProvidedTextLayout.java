package org.swingk;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.swing.SwingUtilities.computeStringWidth;

/**
 * Text layout where line breaks are provided in the text by EOL ('\n') characters.
 */
public class ProvidedTextLayout implements TextLayout {
    protected final MultilineLabel label;

    protected final List<String> lines = new ArrayList<>();

    public ProvidedTextLayout(MultilineLabel label) {
        this.label = Objects.requireNonNull(label);
        String t = label.getText().trim();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (c == '\n') {
                lines.add(sb.toString().trim());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }
    }

    @Override
    public void paintText(Graphics g) {
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
}
