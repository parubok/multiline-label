package org.swingk.multiline;

import javax.swing.ToolTipManager;
import java.awt.event.MouseEvent;

public class DemoMultilineLabel extends MultilineLabel {
    DemoMultilineLabel(String text) {
        super(text);
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        return "Size: " + getSize();
    }
}
