package io.github.parubok.text.multiline.demo;

import io.github.parubok.text.multiline.MultilineLabel;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.List;

public class Demo {

    private static final String DEFAULT_FONT_FAMILY = "<default>";

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

    private final JFrame frame;

    private final JTextField preferredWidthLimitTextField;
    private final JTextField preferredViewportLineCountTextField;
    private final JCheckBox prefSizeCheckBox;
    private final JTextField widthTextField;
    private final JTextField heightTextField;
    private final JTextField borderSizeTextField;
    private final JComboBox<String> fontFamilyComboBox;
    private final JTextField fontSizeTextField;
    private final JTextField lineSpacingTextField;
    private final JTextField maxLinesTextField;
    private final JCheckBox enabledCheckBox;
    private final JPanel labelPanel;
    private MultilineLabel label;
    private final JLabel infoLabel;

    private Demo() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        labelPanel = new JPanel();
        contentPanel.add(labelPanel, BorderLayout.CENTER);

        preferredWidthLimitTextField = new JTextField(Integer.toString(MultilineLabel.DEFAULT_WIDTH_LIMIT));

        preferredViewportLineCountTextField = new JTextField(Integer.toString(MultilineLabel.DEFAULT_PREFERRED_VIEWPORT_LINE_COUNT));

        prefSizeCheckBox = new JCheckBox("Preferred Size:");
        prefSizeCheckBox.setSelected(false);
        widthTextField = new JTextField("400");
        widthTextField.setColumns(5);
        heightTextField = new JTextField("200");
        heightTextField.setColumns(5);
        borderSizeTextField = new JTextField("1");
        borderSizeTextField.setColumns(5);

        DefaultComboBoxModel<String> fontFamilyComboBoxModel = new DefaultComboBoxModel<>();
        fontFamilyComboBoxModel.addAll(List.of(DEFAULT_FONT_FAMILY, Font.DIALOG, Font.DIALOG_INPUT, Font.MONOSPACED,
                Font.SERIF, Font.SANS_SERIF));
        fontFamilyComboBoxModel.setSelectedItem(DEFAULT_FONT_FAMILY);
        fontFamilyComboBox = new JComboBox<>(fontFamilyComboBoxModel);

        fontSizeTextField = new JTextField("12.0");
        fontSizeTextField.setColumns(5);
        lineSpacingTextField = new JTextField(Float.toString(MultilineLabel.DEFAULT_LINE_SPACING));
        lineSpacingTextField.setColumns(5);

        maxLinesTextField = new JTextField("1000");
        maxLinesTextField.setColumns(5);

        enabledCheckBox = new JCheckBox("Enabled");
        enabledCheckBox.setSelected(true);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridBagLayout());
        controlsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        int gridY = 0;
        int bottomInset = 10;

        controlsPanel.add(new JLabel("Preferred Width Limit (pixels):"), gb(0, gridY++));
        preferredWidthLimitTextField.setColumns(10);
        controlsPanel.add(preferredWidthLimitTextField, gb(0, gridY++, 0, bottomInset));
        controlsPanel.add(new JLabel("Preferred Viewport Line Count:"), gb(0, gridY++));
        preferredViewportLineCountTextField.setColumns(10);
        controlsPanel.add(preferredViewportLineCountTextField, gb(0, gridY++, 0, bottomInset));
        controlsPanel.add(prefSizeCheckBox, gb(0, gridY++));
        controlsPanel.add(new JLabel("Width:"), gb(0, gridY++, 20, 0));
        controlsPanel.add(widthTextField, gb(0, gridY++, 20, 0));
        controlsPanel.add(new JLabel("Height:"), gb(0, gridY++, 20, 0));
        controlsPanel.add(heightTextField, gb(0, gridY++, 20, bottomInset));
        controlsPanel.add(new JLabel("Border (pixels):"), gb(0, gridY++));
        controlsPanel.add(borderSizeTextField, gb(0, gridY++, 0, bottomInset));
        controlsPanel.add(new JLabel("Font Family (logical):"), gb(0, gridY++, 0, 3));
        controlsPanel.add(fontFamilyComboBox, gb(0, gridY++, 0, bottomInset));
        controlsPanel.add(new JLabel("Font Size:"), gb(0, gridY++));
        controlsPanel.add(fontSizeTextField, gb(0, gridY++, 0, bottomInset));
        controlsPanel.add(new JLabel("Line Spacing:"), gb(0, gridY++));
        controlsPanel.add(lineSpacingTextField, gb(0, gridY++, 0, bottomInset));
        controlsPanel.add(new JLabel("Max lines:"), gb(0, gridY++));
        controlsPanel.add(maxLinesTextField, gb(0, gridY++, 0, bottomInset));
        controlsPanel.add(enabledCheckBox, gb(0, gridY++, 0, bottomInset));

        var setButton = new JButton("Set");
        setButton.addActionListener(e -> updateLabel());
        controlsPanel.add(setButton, gb(0, gridY++, 0, bottomInset));

        var pasteButton = new JButton("Paste Text");
        pasteButton.addActionListener(e -> pasteText());
        controlsPanel.add(pasteButton, gb(0, gridY++, 0, bottomInset));

        var scrollPaneButton = new JButton("Scroll Pane");
        scrollPaneButton.addActionListener(e -> showLabelInScrollPane());
        controlsPanel.add(scrollPaneButton, gb(0, gridY++));

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.add(controlsPanel, BorderLayout.NORTH);
        contentPanel.add(westPanel, BorderLayout.WEST);

        infoLabel = new JLabel();
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        contentPanel.add(infoLabel, BorderLayout.SOUTH);

        frame = new JFrame("Demo: multiline-label");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(contentPanel);
        frame.setSize(1200, 700);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        updateLabel();
    }

    private void updateInfoLabel() {
        infoLabel.setText("Bounds: " + label.getBounds());
    }

    private static GridBagConstraints gb(int gridX, int gridY) {
        return gb(gridX, gridY, 0, 0);
    }

    private static GridBagConstraints gb(int gridX, int gridY, int leftInset, int bottomInset) {
        return new GridBagConstraints(gridX, gridY, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START,
                GridBagConstraints.NONE, new Insets(0, leftInset, bottomInset, 0), 0, 0);
    }

    private void showLabelInScrollPane() {
        var dialog = new JDialog(frame, "Scroll Pane");
        var contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        var scrollPane = new JScrollPane();
        scrollPane.setViewportView(createLabel());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        dialog.setContentPane(contentPanel);
        dialog.setModal(true);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void pasteText() {
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
            JOptionPane.showMessageDialog(frame, ex.getMessage());
        }
    }

    private void updateLabel() {
        label = createLabel();
        label.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateInfoLabel();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                updateInfoLabel();
            }
        });

        labelPanel.removeAll();
        labelPanel.add(label);
        labelPanel.revalidate();
        labelPanel.repaint();

        updateInfoLabel();
    }

    private MultilineLabel createLabel() {
        var newLabel = new MultilineLabel(labelText);
        newLabel.setPreferredWidthLimit(Integer.parseInt(preferredWidthLimitTextField.getText()));
        newLabel.setPreferredViewportLineCount(Integer.parseInt(preferredViewportLineCountTextField.getText()));
        if (prefSizeCheckBox.isSelected()) {
            int w = Integer.parseInt(widthTextField.getText());
            int h = Integer.parseInt(heightTextField.getText());
            newLabel.setPreferredSize(new Dimension(w, h));
        } else {
            newLabel.setPreferredSize(null);
        }
        int b = Integer.parseInt(borderSizeTextField.getText());
        if (b > 0) {
            newLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, b));
        }
        float f = Float.parseFloat(fontSizeTextField.getText());
        String fontFamily = (String) fontFamilyComboBox.getSelectedItem();
        var newLabelFont = new MultilineLabel().getFont();
        Font font = fontFamily.equals(DEFAULT_FONT_FAMILY) ?
                newLabelFont : new Font(fontFamily, newLabelFont.getStyle(), newLabelFont.getSize());
        newLabel.setFont(font.deriveFont(f));
        newLabel.setLineSpacing(Float.parseFloat(lineSpacingTextField.getText()));
        newLabel.setEnabled(enabledCheckBox.isSelected());
        newLabel.setMaxLines(Integer.parseInt(maxLinesTextField.getText()));
        return newLabel;
    }
}
