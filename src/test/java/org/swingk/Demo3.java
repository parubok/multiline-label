package org.swingk;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;

public class Demo3 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Demo3::buildUI);
    }

    private static MultilineLabel newLabel() {
        MultilineLabel label = new MultilineLabel(Demo.LOREM_IPSUM);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return label;
    }

    private static void buildUI() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        contentPanel.add(newLabel(), BorderLayout.CENTER);
        contentPanel.add(newLabel(), BorderLayout.WEST);
        contentPanel.add(newLabel(), BorderLayout.EAST);
        contentPanel.add(newLabel(), BorderLayout.NORTH);
        contentPanel.add(newLabel(), BorderLayout.SOUTH);

        JFrame frame = new JFrame("Demo3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(contentPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
