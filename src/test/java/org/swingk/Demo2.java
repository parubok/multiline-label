package org.swingk;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

public class Demo2 {
    public static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed " +
            "bibendum, lacus vel convallis consectetur, erat dui pharetra lectus, ac venenatis nulla nisi eget erat. " +
            "Donec ornare volutpat augue, a venenatis magna rutrum non.";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Demo2::buildUI);
    }

    private static void buildUI() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        MultiLineLabel label = new MultiLineLabel();
        label.setBorder(new EmptyBorder(3, 3, 3, 3));
        label.setText(LOREM_IPSUM + " " + LOREM_IPSUM + " " + LOREM_IPSUM);

        JScrollPane sp = new JScrollPane();
        sp.setViewportView(label);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        contentPanel.add(sp, BorderLayout.CENTER);

        JFrame frame = new JFrame("Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(contentPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
