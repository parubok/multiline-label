package org.swingk;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;

public class MultilineLabelUtils {

    private MultilineLabelUtils() {
    }

    public static final String LINE_SEPARATOR_UNIX = "\n";
    public static final String LINE_SEPARATOR_WIN = "\r\n";

    public static boolean hasLineSeparators(String text) {
        return text.contains(LINE_SEPARATOR_UNIX) || text.contains(LINE_SEPARATOR_WIN);
    }

    public static Dimension calculatePreferredSize(Insets insets, FontMetrics fm, String text, int wLimit) {
        return hasLineSeparators(text) ? ProvidedTextLayout.calcPreferredSize(text, fm, insets) :
                WidthTextLayout.calcPreferredSize(insets, fm, text, wLimit);
    }

    /**
         * Draws {@code text} in a style of disabled component text at {@link Graphics} context from the point (x,y). Uses
         * {@code color} as a base.
         */
    public static void paintTextInDisabledStyle(String text, Graphics g, Color color, int x, int y) {
        g.setColor(color.brighter());
        g.drawString(text, x + 1, y + 1);
        g.setColor(color.darker());
        g.drawString(text, x, y);
    }
}
