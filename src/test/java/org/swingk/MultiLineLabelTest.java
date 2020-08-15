package org.swingk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
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
            Assertions.assertEquals("", label.getText());
            Assertions.assertEquals(MultiLineLabel.DEFAULT_WIDTH_LIMIT, label.getWidthLimit());
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
            label.setText("abc");
            Assertions.assertEquals("abc", label.getText());
        });
    }

    @Test
    void getPreferredSize_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultiLineLabel label = new MultiLineLabel(Demo.LOREM_IPSUM);
            Assertions.assertEquals(new Dimension(481, 48), label.getPreferredSize());

            label.setWidthLimit(1000);
            Assertions.assertEquals(new Dimension(980, 32), label.getPreferredSize());

            label.setWidthLimit(10);
            Assertions.assertEquals(new Dimension(69, 528), label.getPreferredSize());

            label.setWidthLimit(MultiLineLabel.DEFAULT_WIDTH_LIMIT);
            Assertions.assertEquals(new Dimension(481, 48), label.getPreferredSize());

            label.setSize(new Dimension(300, 20));
            Assertions.assertEquals(new Dimension(298, 80), label.getPreferredSize());

            label.setSize(new Dimension(3000, 200));
            Assertions.assertEquals(new Dimension(1252, 16), label.getPreferredSize());
        });
    }
}
