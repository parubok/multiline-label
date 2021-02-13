package org.swingk.multiline;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

class MultilineLabelTest {

    static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed " +
            "bibendum, lacus vel convallis consectetur, erat dui pharetra lectus, ac venenatis nulla nisi eget erat. " +
            "Donec ornare volutpat augue, a venenatis magna rutrum non.";

    @Test
    void basic_test_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel();
            Assertions.assertTrue(label.isOpaque());
            Assertions.assertEquals("", label.getText());
            Assertions.assertEquals(MultilineLabel.DEFAULT_LINE_SPACING, label.getLineSpacing());
            Assertions.assertEquals(MultilineLabel.DEFAULT_WIDTH_LIMIT, label.getPreferredWidthLimit());
            Assertions.assertEquals(new Dimension(0, 0), label.getPreferredSize());
            Assertions.assertEquals(new Dimension(0, 0), MultilineLabel.calculatePreferredSize(label,
                    new Insets(0, 0, 0, 0), label.getFontMetrics(label.getFont()), "", label.getPreferredWidthLimit(),
                    1.0f));
            Assertions.assertEquals(new JLabel().getFont(), label.getFont());
            Assertions.assertEquals(new JLabel().getForeground(), label.getForeground());
            Assertions.assertEquals(new JLabel().getBackground(), label.getBackground());
        });
    }

    @Test
    void lineSpacingProp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel();
            label.setLineSpacing(1.0f);
            Assertions.assertEquals(1.0f, label.getLineSpacing());
            label.setLineSpacing(0.5f);
            Assertions.assertEquals(0.5f, label.getLineSpacing());
            Assertions.assertThrows(IllegalArgumentException.class, () -> label.setLineSpacing(0.0f));
            Assertions.assertThrows(IllegalArgumentException.class, () -> label.setLineSpacing(-0.1f));
        });
    }

    @Test
    void lineSpacingPropChangeEvent() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel();
            label.setLineSpacing(2.0f);
            List<PropertyChangeEvent> events = new ArrayList<>();
            label.addPropertyChangeListener("lineSpacing", events::add);
            label.setLineSpacing(10.0f);
            Assertions.assertEquals(1, events.size());
            Assertions.assertEquals(2.0f, events.get(0).getOldValue());
            Assertions.assertEquals(10.0f, events.get(0).getNewValue());
            Assertions.assertEquals("lineSpacing", events.get(0).getPropertyName());
            Assertions.assertEquals(label, events.get(0).getSource());
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
            label.setText("  ");
            Assertions.assertEquals("  ", label.getText());
            label.setText(" \n ");
            Assertions.assertEquals(" \n ", label.getText());
        });
    }

    @Test
    void getPreferredSize_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel(LOREM_IPSUM);
            label.setLineSpacing(1.0f);
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
    void getPreferredSize_disabled() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel(LOREM_IPSUM);
            label.setLineSpacing(1.0f);
            label.setEnabled(false);
            Assertions.assertEquals(new Dimension(488, 48), label.getPreferredSize());
        });
    }

    @Test
    void getPreferredSize_singleLetter() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Assertions.assertEquals(new Dimension(10, 16), new MultilineLabel("w").getPreferredSize());
            Assertions.assertEquals(new Dimension(10, 16), new MultilineLabel(" w ").getPreferredSize());
        });
    }

    @Test
    void calculatePreferredSize_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel(LOREM_IPSUM);
            Assertions.assertEquals(new Dimension(488, 48), MultilineLabel.calculatePreferredSize(label, new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, label.getPreferredWidthLimit(), 1.0f));

            Assertions.assertEquals(new Dimension(987, 32), MultilineLabel.calculatePreferredSize(label, new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, 1_000, 1.0f));

            Assertions.assertEquals(new Dimension(72, 528), MultilineLabel.calculatePreferredSize(label, new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, 10, 1.0f));

            Assertions.assertEquals(new Dimension(488, 48), MultilineLabel.calculatePreferredSize(label, new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, MultilineLabel.DEFAULT_WIDTH_LIMIT, 1.0f));
        });
    }

    @Test
    void getPreferredSize_emptyText_border_diffLineSpacing() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            for (int i = 0; i < 10; i++) {
                var label = new MultilineLabel();
                label.setLineSpacing(1.0f + i);
                label.setBorder(new EmptyBorder(10, 20, 30, 40));
                Assertions.assertEquals(new Dimension(60, 40), label.getPreferredSize());
            }
        });
    }

    @Test
    void getPreferredSize_emptyText_noBorder() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Assertions.assertEquals(new Dimension(0, 0), new MultilineLabel(" ").getPreferredSize());
            Assertions.assertEquals(new Dimension(0, 0), new MultilineLabel("").getPreferredSize());
        });
    }

    @Test
    void getPreferredSize_diffBorders() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel(LOREM_IPSUM);
            label.setLineSpacing(1.0f);
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
            Assertions.assertEquals(new Dimension(499, 88), MultilineLabel.calculatePreferredSize(label, new Insets(10, 20, 30, 40),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, MultilineLabel.DEFAULT_WIDTH_LIMIT, 1.0f));
            Assertions.assertEquals(new Dimension(489, 48), MultilineLabel.calculatePreferredSize(label, new Insets(0, 1, 0, 0),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, MultilineLabel.DEFAULT_WIDTH_LIMIT, 1.0f));
            Assertions.assertEquals(new Dimension(489, 48), MultilineLabel.calculatePreferredSize(label, new Insets(0, 0, 0, 1),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, MultilineLabel.DEFAULT_WIDTH_LIMIT, 1.0f));
            Assertions.assertEquals(new Dimension(488, 53), MultilineLabel.calculatePreferredSize(label, new Insets(5, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, MultilineLabel.DEFAULT_WIDTH_LIMIT, 1.0f));
            Assertions.assertEquals(new Dimension(488, 56), MultilineLabel.calculatePreferredSize(label, new Insets(5, 0, 3, 0),
                    label.getFontMetrics(label.getFont()), LOREM_IPSUM, MultilineLabel.DEFAULT_WIDTH_LIMIT, 1.0f));
        });
    }

    @Test
    void getPreferredSize_4() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("line1\nline2");
            label.setLineSpacing(1.0f);
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
    void getPreferredSize_5() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("line1\n\n\nline2");
            label.setLineSpacing(1.0f);
            Assertions.assertEquals(new Dimension(27, 64), label.getPreferredSize());
        });
    }

    @Test
    void calculatePreferredSize_3() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel();
            Assertions.assertEquals(new Dimension(27, 16), MultilineLabel.calculatePreferredSize(label, new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), "line1", MultilineLabel.DEFAULT_WIDTH_LIMIT, 1.0f));
            Assertions.assertEquals(new Dimension(27, 32), MultilineLabel.calculatePreferredSize(label, new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), "line1\nline2", MultilineLabel.DEFAULT_WIDTH_LIMIT, 1.0f));
            Assertions.assertEquals(new Dimension(27, 48), MultilineLabel.calculatePreferredSize(label, new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), "line1\nline2\nline3", MultilineLabel.DEFAULT_WIDTH_LIMIT, 1.0f));
            Assertions.assertEquals(new Dimension(27, 64), MultilineLabel.calculatePreferredSize(label, new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), "line1\nline2\nline3\nline4", MultilineLabel.DEFAULT_WIDTH_LIMIT, 1.0f));
            Assertions.assertEquals(new Dimension(33, 68), MultilineLabel.calculatePreferredSize(label, new Insets(1, 2, 3, 4),
                    label.getFontMetrics(label.getFont()), "line1\nline2\nline3\nline4", MultilineLabel.DEFAULT_WIDTH_LIMIT, 1.0f));
        });
    }

    @Test
    void calculatePreferredSize_lineSpacing() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel();
            final float lineSpacing = 2.0f;
            Assertions.assertEquals(new Dimension(27, 16), MultilineLabel.calculatePreferredSize(label, new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), "line1", MultilineLabel.DEFAULT_WIDTH_LIMIT, lineSpacing));
            Assertions.assertEquals(new Dimension(27, 48), MultilineLabel.calculatePreferredSize(label, new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), "line1\nline2", MultilineLabel.DEFAULT_WIDTH_LIMIT, lineSpacing));
            Assertions.assertEquals(new Dimension(27, 80), MultilineLabel.calculatePreferredSize(label, new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), "line1\nline2\nline3", MultilineLabel.DEFAULT_WIDTH_LIMIT, lineSpacing));
            Assertions.assertEquals(new Dimension(27, 112), MultilineLabel.calculatePreferredSize(label, new Insets(0, 0, 0, 0),
                    label.getFontMetrics(label.getFont()), "line1\nline2\nline3\nline4", MultilineLabel.DEFAULT_WIDTH_LIMIT, lineSpacing));
            Assertions.assertEquals(new Dimension(33, 116), MultilineLabel.calculatePreferredSize(label, new Insets(1, 2, 3, 4),
                    label.getFontMetrics(label.getFont()), "line1\nline2\nline3\nline4", MultilineLabel.DEFAULT_WIDTH_LIMIT, lineSpacing));
        });
    }

    @Test
    void getPreferredSize_lineSpacing_providedLayout() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("line1\nline2");

            label.setLineSpacing(0.5f);
            Assertions.assertEquals(new Dimension(27, 24), label.getPreferredSize());

            label.setLineSpacing(1.0f);
            Assertions.assertEquals(new Dimension(27, 32), label.getPreferredSize());

            label.setLineSpacing(2.0f);
            Assertions.assertEquals(new Dimension(27, 48), label.getPreferredSize());

            label.setLineSpacing(2.5f);
            Assertions.assertEquals(new Dimension(27, 56), label.getPreferredSize());
        });
    }

    @Test
    void getPreferredSize_lineSpacing_widthLayout() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("line1 line2");
            label.setPreferredWidthLimit(10); // to ensure 2 lines

            label.setLineSpacing(0.5f);
            Assertions.assertEquals(new Dimension(27, 24), label.getPreferredSize());

            label.setLineSpacing(1.0f);
            Assertions.assertEquals(new Dimension(27, 32), label.getPreferredSize());

            label.setLineSpacing(2.0f);
            Assertions.assertEquals(new Dimension(27, 48), label.getPreferredSize());

            label.setLineSpacing(2.5f);
            Assertions.assertEquals(new Dimension(27, 56), label.getPreferredSize());
        });
    }

    @Test
    void setCurrentWidthIgnoredForPreferredSizeCalculation_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel(LOREM_IPSUM);
            label.setLineSpacing(1.0f);
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
            label.setLineSpacing(1.0f);
            var d = new Dimension(13, 15);
            label.setPreferredSize(d);
            Assertions.assertEquals(d, label.getPreferredSize());

            label.setPreferredSize(null);
            Assertions.assertEquals(new Dimension(488, 48), label.getPreferredSize());
        });
    }
}
