package io.github.parubok.text.multiline;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Instances provide implementation of preferred size calculation and text painting.
 */
public interface TextLayout {

    /**
     * Called from {@link MultilineLabelUI#paint(Graphics, JComponent)} to paint label's text.
     * The {@link Graphics} object is preconfigured with the label's font and foreground color.
     */
    void paintText(Graphics g);

    /**
     * @return Calculated preferred size of the label (incl. insets).
     */
    Dimension calculatePreferredSize();

    /**
     * Called from {@link MultilineLabel#setBounds(int, int, int, int)} before applying the new bounds.
     */
    void preSetBounds(int x, int y, int width, int height);
}
