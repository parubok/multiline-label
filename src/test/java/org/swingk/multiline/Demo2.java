package org.swingk.multiline;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Demo2 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Demo2();
        });
    }

    private final MultilineLabel label;
    private final JCheckBox prefSizeCheckBox;
    private final JTextField widthTextField;
    private final JTextField heightTextField;
    private final JTextField borderSizeTextField;
    private final JTextField fontSizeTextField;
    private final JCheckBox enabledCheckBox;

    private Demo2() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        label = new MultilineLabel();
        label.setText(Demo.LOREM_IPSUM);

        JPanel labelPanel = new JPanel();
        labelPanel.add(label);
        contentPanel.add(labelPanel, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        prefSizeCheckBox = new JCheckBox("Preferred Size:");
        prefSizeCheckBox.setSelected(true);
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
        JButton setButton = new JButton("Set");
        setButton.addActionListener(e -> updateLabel());
        JButton pasteButton = new JButton("Paste");
        pasteButton.addActionListener(e -> {
                    try {
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        label.setText((String) clipboard.getData(DataFlavor.stringFlavor));
                    } catch (IOException | UnsupportedFlavorException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        controlsPanel.add(prefSizeCheckBox);
        controlsPanel.add(new JLabel("Width:"));
        controlsPanel.add(widthTextField);
        controlsPanel.add(new JLabel("Height:"));
        controlsPanel.add(heightTextField);
        controlsPanel.add(new JLabel("Border:"));
        controlsPanel.add(borderSizeTextField);
        controlsPanel.add(new JLabel("Font:"));
        controlsPanel.add(fontSizeTextField);
        controlsPanel.add(enabledCheckBox);
        controlsPanel.add(setButton);
        controlsPanel.add(pasteButton);

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.add(controlsPanel, BorderLayout.NORTH);
        contentPanel.add(westPanel, BorderLayout.WEST);

        JFrame frame = new JFrame("Demo2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(contentPanel);
        frame.setSize(1200, 600);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        updateLabel();
    }

    private void updateLabel() {
        if (prefSizeCheckBox.isSelected()) {
            int w = Integer.parseInt(widthTextField.getText());
            int h = Integer.parseInt(heightTextField.getText());
            label.setPreferredSize(new Dimension(w, h));
        } else {
            label.setPreferredSize(null);
        }
        int b = Integer.parseInt(borderSizeTextField.getText());
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, b));
        float f = Float.parseFloat(fontSizeTextField.getText());
        label.setFont(label.getFont().deriveFont(f));
        label.setEnabled(enabledCheckBox.isSelected());
        label.revalidate();
        label.repaint();
    }
}
