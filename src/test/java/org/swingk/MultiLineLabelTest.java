package org.swingk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;

class MultiLineLabelTest {
    @Test
    void illegal_text() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultiLineLabel label = new MultiLineLabel();
            Assertions.assertThrows(IllegalArgumentException.class, () -> label.setText("a  b"));
            Assertions.assertThrows(IllegalArgumentException.class, () -> label.setText("abc "));
            Assertions.assertThrows(IllegalArgumentException.class, () -> label.setText(" abc"));
            Assertions.assertThrows(IllegalArgumentException.class, () -> label.setText("a\nbc"));
        });
    }

    @Test
    void basic_test_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultiLineLabel label = new MultiLineLabel();
            Assertions.assertTrue(label.isOpaque());
            Assertions.assertEquals("", label.getText());
            Assertions.assertEquals(MultiLineLabel.DEFAULT_WIDTH_LIMIT, label.getPreferredWidthLimit());
            Assertions.assertEquals(new Dimension(0, 0), label.getPreferredSize());
            Assertions.assertEquals(new JLabel().getFont(), label.getFont());
            Assertions.assertEquals(new JLabel().getForeground(), label.getForeground());
            Assertions.assertEquals(new JLabel().getBackground(), label.getBackground());
        });
    }

    @Test
    void setText() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultiLineLabel label = new MultiLineLabel();
            label.setText(Demo.LOREM_IPSUM);
            Assertions.assertEquals(Demo.LOREM_IPSUM, label.getText());
        });
    }

    @Test
    void getPreferredSize_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultiLineLabel label = new MultiLineLabel(Demo.LOREM_IPSUM);
            Assertions.assertEquals(new Dimension(481, 48), label.getPreferredSize());

            label.setPreferredWidthLimit(1000);
            Assertions.assertEquals(new Dimension(980, 32), label.getPreferredSize());

            label.setPreferredWidthLimit(10);
            Assertions.assertEquals(new Dimension(69, 528), label.getPreferredSize());

            label.setPreferredWidthLimit(MultiLineLabel.DEFAULT_WIDTH_LIMIT);
            Assertions.assertEquals(new Dimension(481, 48), label.getPreferredSize());

            label.setSize(new Dimension(300, 20));
            Assertions.assertEquals(new Dimension(288, 80), label.getPreferredSize());

            label.setSize(new Dimension(3000, 200));
            Assertions.assertEquals(new Dimension(1252, 16), label.getPreferredSize());
        });
    }

    @Test
    void getPreferredSize_2() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultiLineLabel label = new MultiLineLabel();
            label.setBorder(new EmptyBorder(10, 20, 30, 40));
            Assertions.assertEquals(new Dimension(60, 40), label.getPreferredSize());
        });
    }

    @Test
    void getPreferredSize_3() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultiLineLabel label = new MultiLineLabel(Demo.LOREM_IPSUM);
            label.setBorder(new EmptyBorder(10, 20, 30, 40));
            Assertions.assertEquals(new Dimension(495, 88), label.getPreferredSize());
            label.setBorder(new EmptyBorder(0, 0, 0, 0));
            Assertions.assertEquals(new Dimension(481, 48), label.getPreferredSize());
            label.setBorder(new EmptyBorder(0, 1, 0, 0));
            Assertions.assertEquals(new Dimension(482, 48), label.getPreferredSize());
            label.setBorder(new EmptyBorder(0, 0, 0, 1));
            Assertions.assertEquals(new Dimension(482, 48), label.getPreferredSize());
            label.setBorder(new EmptyBorder(5, 0, 0, 0));
            Assertions.assertEquals(new Dimension(481, 53), label.getPreferredSize());
            label.setBorder(new EmptyBorder(5, 0, 3, 0));
            Assertions.assertEquals(new Dimension(481, 56), label.getPreferredSize());
        });
    }
}
