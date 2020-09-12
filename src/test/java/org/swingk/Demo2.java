package org.swingk;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class Demo2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Demo2::buildUI);
    }

    private static void buildUI() {
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));

        MultilineLabel label = new MultilineLabel();
        label.setText(Demo.LOREM_IPSUM);

        JPanel labelPanel = new JPanel();
        labelPanel.add(label);
        contentPanel.add(labelPanel, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField widthTextField = new JTextField("400");
        widthTextField.setColumns(10);
        JTextField heightTextField = new JTextField("200");
        heightTextField.setColumns(10);
        JTextField borderSizeTextField = new JTextField("1");
        borderSizeTextField.setColumns(5);
        JButton setSizeButton = new JButton("Set");
        Runnable updater = () -> {
            int w = Integer.parseInt(widthTextField.getText());
            int h = Integer.parseInt(heightTextField.getText());
            label.setPreferredSize(new Dimension(w, h));
            int b = Integer.parseInt(borderSizeTextField.getText());
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK, b));
            label.revalidate();
            label.repaint();
        };
        setSizeButton.addActionListener(e -> updater.run());
        controlsPanel.add(new JLabel("Width:"));
        controlsPanel.add(widthTextField);
        controlsPanel.add(new JLabel("Height:"));
        controlsPanel.add(heightTextField);
        controlsPanel.add(new JLabel("Border:"));
        controlsPanel.add(borderSizeTextField);
        controlsPanel.add(setSizeButton);
        contentPanel.add(controlsPanel, BorderLayout.NORTH);

        JFrame frame = new JFrame("Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(contentPanel);
        frame.setSize(1500, 800);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        updater.run();
    }
}
