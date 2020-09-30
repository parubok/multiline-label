package org.swingk;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Objects;

public abstract class AbstractTextLayout implements TextLayout {

    protected final MultilineLabel label;

    protected AbstractTextLayout(MultilineLabel label) {
        this.label = Objects.requireNonNull(label);
    }

    protected static int getTextPreferredHeight(int lineCount, FontMetrics fm) {
        return (fm.getAscent() + fm.getDescent()) * lineCount + fm.getLeading() * (lineCount - 1);
    }

    /**
     * Draws {@code text} in a style of disabled component text at {@link Graphics} context from the point (x,y). Uses
     * {@code color} as a base.
     */
    protected static void paintTextInDisabledStyle(String text, Graphics g, Color color, int x, int y) {
        g.setColor(color.brighter());
        g.drawString(text, x + 1, y + 1);
        g.setColor(color.darker());
        g.drawString(text, x, y);
    }
}
