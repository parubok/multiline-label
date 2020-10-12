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

/**
 * TODO: static methods (separate demo)
 * TODO: display metrics
 */
public class Demo {

    static final String TEXT = "Reference types are the class types, the interface types, and the array " +
            "types. The reference types are implemented by dynamically created objects that are either instances of " +
            "classes or arrays. Many references to each object can exist. All objects (including arrays) support " +
            "the methods of the class Object, which is the (single) root of the class hierarchy. A predefined " +
            "String class supports Unicode character strings. Classes exist for wrapping primitive values inside " +
            "of objects. In many cases, wrapping and unwrapping is performed automatically by the compiler (in " +
            "which case, wrapping is called boxing, and unwrapping is called unboxing). Class and interface " +
            "declarations may be generic, that is, they may be parameterized by other reference types. Such " +
            "declarations may then be invoked with specific type arguments.";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Demo::new);
    }

    private String labelText = TEXT;

    private final JTextField preferredWidthLimitTextField;
    private final JCheckBox prefSizeCheckBox;
    private final JTextField widthTextField;
    private final JTextField heightTextField;
    private final JTextField borderSizeTextField;
    private final JTextField fontSizeTextField;
    private final JCheckBox enabledCheckBox;
    private final JPanel labelPanel;

    private Demo() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        labelPanel = new JPanel();
        contentPanel.add(labelPanel, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        preferredWidthLimitTextField = new JTextField(Integer.toString(MultilineLabel.DEFAULT_WIDTH_LIMIT));
        prefSizeCheckBox = new JCheckBox("Preferred Size:");
        prefSizeCheckBox.setSelected(false);
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
                        String clipboardText = (String) clipboard.getData(DataFlavor.stringFlavor);
                        if (clipboardText != null) {
                            labelText = clipboardText;
                            updateLabel();
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    } catch (IOException | UnsupportedFlavorException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        controlsPanel.add(new JLabel("Preferred Width Limit:"));
        controlsPanel.add(preferredWidthLimitTextField);
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
        frame.setSize(1000, 500);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        updateLabel();
    }

    private void updateLabel() {
        MultilineLabel label = new MultilineLabel(labelText);

        label.setPreferredWidthLimit(Integer.parseInt(preferredWidthLimitTextField.getText()));

        if (prefSizeCheckBox.isSelected()) {
            int w = Integer.parseInt(widthTextField.getText());
            int h = Integer.parseInt(heightTextField.getText());
            label.setPreferredSize(new Dimension(w, h));
        } else {
            label.setPreferredSize(null);
        }

        int b = Integer.parseInt(borderSizeTextField.getText());
        if (b > 0) {
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK, b));
        }

        float f = Float.parseFloat(fontSizeTextField.getText());
        label.setFont(label.getFont().deriveFont(f));

        label.setEnabled(enabledCheckBox.isSelected());

        labelPanel.removeAll();
        labelPanel.add(label);
        labelPanel.revalidate();
        labelPanel.repaint();
    }
}
