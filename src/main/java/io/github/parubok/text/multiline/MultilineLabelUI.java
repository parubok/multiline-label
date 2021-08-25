package io.github.parubok.text.multiline;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import java.awt.Graphics;

/**
 * UI delegate of {@link MultilineLabel}.
 */
public class MultilineLabelUI extends ComponentUI {
    @Override
    public void installUI(JComponent c) {
        LookAndFeel.installColorsAndFont(c, "Label.background", "Label.foreground", "Label.font");
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        var label = (MultilineLabel) c;
        g.setColor(label.getForeground());
        g.setFont(label.getFont());
        label.getTextLayout().paintText(g);
    }
}
