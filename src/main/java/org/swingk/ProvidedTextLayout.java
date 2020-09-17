package org.swingk;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Objects;

/**
 * Text layout where line breaks are provided in the text by EOL ('\n') characters.
 */
public class ProvidedTextLayout implements TextLayout {
    protected final MultilineLabel label;

    public ProvidedTextLayout(MultilineLabel label) {
        this.label = Objects.requireNonNull(label);
    }

    @Override
    public void paintText(Graphics g) {
    }

    @Override
    public Dimension calculatePreferredSize() {
        return null;
    }

    @Override
    public void preSetBounds(int x, int y, int width, int height) {
        // do nothing
    }
}
