package org.swingk;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class Demo {
    public static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed " +
            "bibendum, lacus vel convallis consectetur, erat dui pharetra lectus, ac venenatis nulla nisi eget erat. " +
            "Donec ornare volutpat augue, a venenatis magna rutrum non.";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Demo::buildUI);
    }

    private static void buildUI() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        MultilineLabel label = new MultilineLabel();
        label.setText(LOREM_IPSUM);
        contentPanel.add(label, BorderLayout.CENTER);

        JFrame frame = new JFrame("Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(contentPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
