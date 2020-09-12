package org.swingk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;

class MultilineLabelTest {
    @Test
    void illegal_text() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel();
            Assertions.assertThrows(IllegalArgumentException.class, () -> label.setText("a\nbc"));
        });
    }

    @Test
    void basic_test_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel();
            Assertions.assertTrue(label.isOpaque());
            Assertions.assertEquals("", label.getText());
            Assertions.assertEquals(MultilineLabel.DEFAULT_WIDTH_LIMIT, label.getPreferredWidthLimit());
            Assertions.assertEquals(new Dimension(0, 0), label.getPreferredSize());
            Assertions.assertEquals(new JLabel().getFont(), label.getFont());
            Assertions.assertEquals(new JLabel().getForeground(), label.getForeground());
            Assertions.assertEquals(new JLabel().getBackground(), label.getBackground());
        });
    }

    @Test
    void setText() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel();
            label.setText(Demo.LOREM_IPSUM);
            Assertions.assertEquals(Demo.LOREM_IPSUM, label.getText());
        });
    }

    @Test
    void getPreferredSize_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel(Demo.LOREM_IPSUM);
            Assertions.assertEquals(new Dimension(488, 48), label.getPreferredSize());

            label.setPreferredWidthLimit(1000);
            Assertions.assertEquals(new Dimension(987, 32), label.getPreferredSize());

            label.setPreferredWidthLimit(10);
            Assertions.assertEquals(new Dimension(72, 528), label.getPreferredSize());

            label.setPreferredWidthLimit(MultilineLabel.DEFAULT_WIDTH_LIMIT);
            Assertions.assertEquals(new Dimension(488, 48), label.getPreferredSize());

            label.setSize(new Dimension(300, 20));
            Assertions.assertEquals(new Dimension(292, 80), label.getPreferredSize());

            label.setSize(new Dimension(3000, 200));
            Assertions.assertEquals(new Dimension(1255, 16), label.getPreferredSize());
        });
    }

    @Test
    void getPreferredSize_2() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel();
            label.setBorder(new EmptyBorder(10, 20, 30, 40));
            Assertions.assertEquals(new Dimension(60, 40), label.getPreferredSize());
        });
    }

    @Test
    void getPreferredSize_3() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel(Demo.LOREM_IPSUM);
            label.setBorder(new EmptyBorder(10, 20, 30, 40));
            Assertions.assertEquals(new Dimension(499, 88), label.getPreferredSize());
            label.setBorder(new EmptyBorder(0, 0, 0, 0));
            Assertions.assertEquals(new Dimension(488, 48), label.getPreferredSize());
            label.setBorder(new EmptyBorder(0, 1, 0, 0));
            Assertions.assertEquals(new Dimension(489, 48), label.getPreferredSize());
            label.setBorder(new EmptyBorder(0, 0, 0, 1));
            Assertions.assertEquals(new Dimension(489, 48), label.getPreferredSize());
            label.setBorder(new EmptyBorder(5, 0, 0, 0));
            Assertions.assertEquals(new Dimension(488, 53), label.getPreferredSize());
            label.setBorder(new EmptyBorder(5, 0, 3, 0));
            Assertions.assertEquals(new Dimension(488, 56), label.getPreferredSize());
        });
    }

    @Test
    void setPreferredSize_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel(Demo.LOREM_IPSUM);
            Dimension d = new Dimension(13, 15);
            label.setPreferredSize(d);
            Assertions.assertEquals(d, label.getPreferredSize());

            label.setPreferredSize(null);
            Assertions.assertEquals(new Dimension(488, 48), label.getPreferredSize());
        });
    }

    @Test
    void toRenderedText_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Assertions.assertEquals("", MultilineLabel.toRenderedText(""));
            Assertions.assertEquals("", MultilineLabel.toRenderedText(" "));
            Assertions.assertEquals("", MultilineLabel.toRenderedText("  "));
            Assertions.assertEquals("a b", MultilineLabel.toRenderedText("a b"));
            Assertions.assertEquals("a b", MultilineLabel.toRenderedText("a  b"));
            Assertions.assertEquals("a b", MultilineLabel.toRenderedText("a   b"));
            Assertions.assertEquals("a b", MultilineLabel.toRenderedText("a    b"));
            Assertions.assertEquals("a b c d", MultilineLabel.toRenderedText(" a  b c  d "));
            Assertions.assertEquals("abcd", MultilineLabel.toRenderedText("abcd"));
            Assertions.assertEquals("abcd 1", MultilineLabel.toRenderedText("abcd 1"));
            Assertions.assertEquals("abcd 12", MultilineLabel.toRenderedText("abcd 12"));
        });
    }
}
