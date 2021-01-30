package org.swingk.multiline;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;

class MultilineUtils {
    private MultilineUtils() {
    }

    static Dimension toDimension(int width, int height, Insets insets) {
        return new Dimension(width + insets.right + insets.left, height + insets.top + insets.bottom);
    }

    static int getHeightIncrement(FontMetrics fm, float lineSpacing) {
        return Math.round(fm.getHeight() * lineSpacing);
    }
}
