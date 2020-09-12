package org.swingk;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
        SwingUtilities.invokeLater(() -> {
            new Demo2();
        });
    }

    private final MultilineLabel label;
    private final JTextField widthTextField;
    private final JTextField heightTextField;
    private final JTextField borderSizeTextField;
    private final JTextField fontSizeTextField;
    private final JCheckBox enabledCheckBox;

    private Demo2() {
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));

        label = new MultilineLabel();
        label.setText(Demo.LOREM_IPSUM);

        JPanel labelPanel = new JPanel();
        labelPanel.add(label);
        contentPanel.add(labelPanel, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        widthTextField = new JTextField("400");
        widthTextField.setColumns(5);
        heightTextField = new JTextField("200");
        heightTextField.setColumns(5);
        borderSizeTextField = new JTextField("1");
        borderSizeTextField.setColumns(5);
        fontSizeTextField = new JTextField("12.0");
        fontSizeTextField.setColumns(5);
        enabledCheckBox = new JCheckBox("Enabled");
        enabledCheckBox.setSelected(true);
        JButton setSizeButton = new JButton("Set");
        setSizeButton.addActionListener(e -> updateLabel());
        controlsPanel.add(new JLabel("Width:"));
        controlsPanel.add(widthTextField);
        controlsPanel.add(new JLabel("Height:"));
        controlsPanel.add(heightTextField);
        controlsPanel.add(new JLabel("Border:"));
        controlsPanel.add(borderSizeTextField);
        controlsPanel.add(new JLabel("Font:"));
        controlsPanel.add(fontSizeTextField);
        controlsPanel.add(enabledCheckBox);
        controlsPanel.add(setSizeButton);
        contentPanel.add(controlsPanel, BorderLayout.NORTH);

        JFrame frame = new JFrame("Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(contentPanel);
        frame.setSize(1200, 600);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        updateLabel();
    }

    private void updateLabel() {
        int w = Integer.parseInt(widthTextField.getText());
        int h = Integer.parseInt(heightTextField.getText());
        label.setPreferredSize(new Dimension(w, h));
        int b = Integer.parseInt(borderSizeTextField.getText());
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, b));
        float f = Float.parseFloat(fontSizeTextField.getText());
        label.setFont(label.getFont().deriveFont(f));
        label.setEnabled(enabledCheckBox.isSelected());
        label.revalidate();
        label.repaint();
    }
}
