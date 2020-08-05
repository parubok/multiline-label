package org.swingk;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

/**
 * GUI to demo component property binding.
 */
public class Demo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Demo::buildUI);
    }

    private static void buildUI() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        MultiLineLabel label = new MultiLineLabel();
        label.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed bibendum, lacus vel convallis consectetur, erat dui pharetra lectus, ac venenatis nulla nisi eget erat. Donec ornare volutpat augue, a venenatis magna rutrum non.");
        contentPanel.add(label, BorderLayout.CENTER);

        JFrame frame = new JFrame("Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(contentPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
