package org.swingk.multiline;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.Insets;

class MultilineLabelTest {

    public static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed " +
            "bibendum, lacus vel convallis consectetur, erat dui pharetra lectus, ac venenatis nulla nisi eget erat. " +
            "Donec ornare volutpat augue, a venenatis magna rutrum non.";

    @Test
    void basic_test_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel();
            Assertions.assertTrue(label.isOpaque());
            Assertions.assertEquals("", label.getText());
            Assertions.assertEquals(MultilineLabel.DEFAULT_WIDTH_LIMIT, label.getPreferredWidthLimit());
            Assertions.assertEquals(new Dimension(0, 0), label.getPreferredSize());
            Assertions.assertEquals(new Dimension(0, 0), MultilineLabel.calculatePreferredSize(new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), "", label.getPreferredWidthLimit()));
            Assertions.assertEquals(new JLabel().getFont(), label.getFont());
            Assertions.assertEquals(new JLabel().getForeground(), label.getForeground());
            Assertions.assertEquals(new JLabel().getBackground(), label.getBackground());
        });
    }

    @Test
    void isWidthBasedLayout() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel("text1");
            Assertions.assertTrue(label.isWidthBasedLayout());
            label.setText("line1\r\nline2");
            Assertions.assertFalse(label.isWidthBasedLayout());
        });
    }

    @Test
    void setGetText() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel();
            label.setText(LOREM_IPSUM);
            Assertions.assertEquals(LOREM_IPSUM, label.getText());
            label.setText(" ");
            Assertions.assertEquals(" ", label.getText());
        });
    }

    @Test
    void getPreferredSize_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel(LOREM_IPSUM);
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
    void calculatePreferredSize_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel(LOREM_IPSUM);
            Assertions.assertEquals(new Dimension(488, 48), MultilineLabel.calculatePreferredSize(new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, label.getPreferredWidthLimit()));

            Assertions.assertEquals(new Dimension(987, 32), MultilineLabel.calculatePreferredSize(new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, 1_000));

            Assertions.assertEquals(new Dimension(72, 528), MultilineLabel.calculatePreferredSize(new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, 10));

            Assertions.assertEquals(new Dimension(488, 48), MultilineLabel.calculatePreferredSize(new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, MultilineLabel.DEFAULT_WIDTH_LIMIT));
        });
    }

    @Test
    void getPreferredSize_2() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel();
            label.setBorder(new EmptyBorder(10, 20, 30, 40));
            Assertions.assertEquals(new Dimension(60, 40), label.getPreferredSize());
        });
    }

    @Test
    void getPreferredSize_3() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel(LOREM_IPSUM);
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
    void calculatePreferredSize_2() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel();
            Assertions.assertEquals(new Dimension(499, 88), MultilineLabel.calculatePreferredSize(new Insets(10, 20, 30, 40),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, MultilineLabel.DEFAULT_WIDTH_LIMIT));
            Assertions.assertEquals(new Dimension(489, 48), MultilineLabel.calculatePreferredSize(new Insets(0, 1, 0, 0),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, MultilineLabel.DEFAULT_WIDTH_LIMIT));
            Assertions.assertEquals(new Dimension(489, 48), MultilineLabel.calculatePreferredSize(new Insets(0, 0, 0, 1),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, MultilineLabel.DEFAULT_WIDTH_LIMIT));
            Assertions.assertEquals(new Dimension(488, 53), MultilineLabel.calculatePreferredSize(new Insets(5, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, MultilineLabel.DEFAULT_WIDTH_LIMIT));
            Assertions.assertEquals(new Dimension(488, 56), MultilineLabel.calculatePreferredSize(new Insets(5, 0, 3, 0),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, MultilineLabel.DEFAULT_WIDTH_LIMIT));
        });
    }

    @Test
    void getPreferredSize_4() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("line1\nline2");
            Assertions.assertEquals(new Dimension(27, 32), label.getPreferredSize());

            label.setText("line1\nline2\nline3");
            Assertions.assertEquals(new Dimension(27, 48), label.getPreferredSize());

            label.setText("line1\nline2\nline3\nline4");
            Assertions.assertEquals(new Dimension(27, 64), label.getPreferredSize());

            label.setText("  line1 \n line2\nline3\r\nline4   ");
            Assertions.assertEquals(new Dimension(27, 64), label.getPreferredSize());

            label.setBorder(new EmptyBorder(1, 2, 3, 4));
            Assertions.assertEquals(new Dimension(33, 68), label.getPreferredSize());
        });
    }

    @Test
    void calculatePreferredSize_3() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel();
            Assertions.assertEquals(new Dimension(27, 32), MultilineLabel.calculatePreferredSize(new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), "line1\nline2", MultilineLabel.DEFAULT_WIDTH_LIMIT));
            Assertions.assertEquals(new Dimension(27, 48), MultilineLabel.calculatePreferredSize(new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), "line1\nline2\nline3", MultilineLabel.DEFAULT_WIDTH_LIMIT));
            Assertions.assertEquals(new Dimension(27, 64), MultilineLabel.calculatePreferredSize(new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), "line1\nline2\nline3\nline4", MultilineLabel.DEFAULT_WIDTH_LIMIT));
            Assertions.assertEquals(new Dimension(33, 68), MultilineLabel.calculatePreferredSize(new Insets(1, 2, 3, 4),
                    label.getFontMetrics(label.getFont()), "line1\nline2\nline3\nline4", MultilineLabel.DEFAULT_WIDTH_LIMIT));
        });
    }

    @Test
    void getPreferredSize_5() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("line1\n\n\nline2");
            Assertions.assertEquals(new Dimension(27, 64), label.getPreferredSize());
        });
    }

    @Test
    void setCurrentWidthIgnoredForPreferredSizeCalculation_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel(LOREM_IPSUM);
            Assertions.assertTrue(label.isUseCurrentWidthForPreferredSize());
            Assertions.assertEquals(new Dimension(488, 48), label.getPreferredSize());

            label.setBounds(0, 0, 200, 20);
            Assertions.assertEquals(new Dimension(195, 112), label.getPreferredSize());

            label.setUseCurrentWidthForPreferredSize(false);
            Assertions.assertFalse(label.isUseCurrentWidthForPreferredSize());
            Assertions.assertEquals(new Dimension(488, 48), label.getPreferredSize());

            label.setUseCurrentWidthForPreferredSize(true);
            Assertions.assertTrue(label.isUseCurrentWidthForPreferredSize());
            Assertions.assertEquals(new Dimension(195, 112), label.getPreferredSize());
        });
    }

    @Test
    void getTextLayout() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel(LOREM_IPSUM);
            Assertions.assertTrue(label.getTextLayout() instanceof WidthTextLayout);
            label.setText("line1\nline2");
            Assertions.assertTrue(label.getTextLayout() instanceof ProvidedTextLayout);
        });
    }

    @Test
    void setPreferredSize_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel(LOREM_IPSUM);
            var d = new Dimension(13, 15);
            label.setPreferredSize(d);
            Assertions.assertEquals(d, label.getPreferredSize());

            label.setPreferredSize(null);
            Assertions.assertEquals(new Dimension(488, 48), label.getPreferredSize());
        });
    }
}
