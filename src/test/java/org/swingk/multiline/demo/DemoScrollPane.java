package org.swingk.multiline.demo;

import org.swingk.multiline.MultilineLabel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class DemoScrollPane {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DemoScrollPane::buildUI);
    }

    private static void buildUI() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        MultilineLabel label = new MultilineLabel(Demo.TEXT);

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
