package org.swingk;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import java.awt.Graphics;

public class BasicMultilineLabelUI extends ComponentUI {
    @Override
    public void installUI(JComponent c) {
        LookAndFeel.installColorsAndFont(c, "Label.background", "Label.foreground", "Label.font");
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        MultilineLabel label = (MultilineLabel) c;
        g.setColor(label.getForeground());
        g.setFont(label.getFont());
        label.getTextLayout().paintText(g);
    }
}
