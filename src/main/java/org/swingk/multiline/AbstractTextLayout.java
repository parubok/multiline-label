package org.swingk.multiline;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Objects;

public abstract class AbstractTextLayout implements TextLayout {

    protected final MultilineLabel label;

    protected AbstractTextLayout(MultilineLabel label) {
        this.label = Objects.requireNonNull(label);
    }

    protected static int getTextPreferredHeight(int lineCount, FontMetrics fm) {
        return (fm.getAscent() + fm.getDescent()) * lineCount + fm.getLeading() * (lineCount - 1);
    }

    protected static void drawString(JComponent c, Graphics g, String str, int x, int y) {
        BasicGraphicsUtils.drawString(c, (Graphics2D) g, str, x, y);
    }

    /**
     * Draws {@code text} in a style of disabled component text at {@link Graphics} context from the point (x,y). Uses
     * {@code color} as a base.
     */
    protected static void drawStringInDisabledStyle(JComponent c, String str, Graphics g, Color color, int x, int y) {
        g.setColor(color.brighter());
        drawString(c, g, str, x + 1, y + 1);
        g.setColor(color.darker());
        drawString(c, g, str, x, y);
    }
}
