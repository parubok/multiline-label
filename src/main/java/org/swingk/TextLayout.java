package org.swingk;

import java.awt.Dimension;
import java.awt.Graphics;

interface TextLayout {
    /**
     * Called from {@link MultilineLabel#paintComponent(Graphics)} to paint label's text.
     * The {@link Graphics} object is preconfigured with the label's font and color.
     */
    void paintText(Graphics g);

    /**
     * @return Calculated preferred size of the label (incl. insets).
     */
    Dimension calculatePreferredSize();

    /**
     * Called before {@link MultilineLabel#setBounds(int, int, int, int)}.
     */
    void preSetBounds(int x, int y, int width, int height);
}
