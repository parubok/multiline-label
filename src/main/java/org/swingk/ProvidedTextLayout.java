package org.swingk;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Text layout where line breaks are provided in the text by EOL ('\n') characters.
 */
final class ProvidedTextLayout implements TextLayout {
    @Override
    public void paintText(Graphics g) {

    }

    @Override
    public Dimension calculatePreferredSize() {
        return null;
    }

    @Override
    public void preSetBounds(int x, int y, int width, int height) {

    }
}
