package io.github.parubok.text.multiline;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Set;

public class MultilineLabelTest {

    public static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed " +
            "bibendum, lacus vel convallis consectetur, erat dui pharetra lectus, ac venenatis nulla nisi eget erat. " +
            "Donec ornare volutpat augue, a venenatis magna rutrum non.";

    @Test
    public void basic_test_1() throws Exception {
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
    public void lineSpacingProp() throws Exception {
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
    public void isWidthBasedLayout() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel("text1");
            Assertions.assertTrue(label.isWidthBasedLayout());
            label.setText("line1\r\nline2");
            Assertions.assertTrue(label.isWidthBasedLayout());
        });
    }

    @Test
    public void setGetText() throws Exception {
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
    public void getPreferredSize_1() throws Exception {
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
    public void getPreferredSize_disabled() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel(LOREM_IPSUM);
            label.setLineSpacing(1.0f);
            label.setEnabled(false);
            Assertions.assertEquals(new Dimension(488, 48), label.getPreferredSize());
        });
    }

    @Test
    public void getPreferredSize_singleLetter() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Assertions.assertEquals(new Dimension(10, 16), new MultilineLabel("w").getPreferredSize());
            Assertions.assertEquals(new Dimension(16, 16), new MultilineLabel(" w ").getPreferredSize());
        });
    }

    @Test
    public void calculatePreferredSize_1() throws Exception {
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
    public void getPreferredSize_emptyText_border_diffLineSpacing() throws Exception {
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
    public void getPreferredSize_emptyText_noBorder() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Assertions.assertEquals(new Dimension(3, 16), new MultilineLabel(" ").getPreferredSize());
            Assertions.assertEquals(new Dimension(0, 0), new MultilineLabel("").getPreferredSize());
        });
    }

    @Test
    public void getPreferredSize_diffBorders() throws Exception {
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
    public void calculatePreferredSize_2() throws Exception {
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
    public void getPreferredSize_4() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("line1\nline2");
            label.setLineSpacing(1.0f);
            Assertions.assertEquals(new Dimension(27, 32), label.getPreferredSize());

            label.setText("line1\nline2\nline3");
            Assertions.assertEquals(new Dimension(27, 48), label.getPreferredSize());

            label.setText("line1\nline2\nline3\nline4");
            Assertions.assertEquals(new Dimension(27, 64), label.getPreferredSize());

            label.setText("  line1 \n line2\nline3\nline4   ");
            Assertions.assertEquals(new Dimension(36, 64), label.getPreferredSize());

            label.setBorder(new EmptyBorder(1, 2, 3, 4));
            Assertions.assertEquals(new Dimension(42, 68), label.getPreferredSize());
        });
    }

    @Test
    public void getPreferredSize_5() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("line1\n\n\nline2");
            label.setLineSpacing(1.0f);
            Assertions.assertEquals(new Dimension(27, 64), label.getPreferredSize());
        });
    }

    @Test
    public void getPreferredSize_maxLines() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("line\nline\nline\nlast line");
            label.setLineSpacing(1.0f);
            Assertions.assertEquals(new Dimension(44, 64), label.getPreferredSize());

            label.setMaxLines(1);
            Assertions.assertEquals(new Dimension(9, 16), label.getPreferredSize());

            label.setMaxLines(2);
            Assertions.assertEquals(new Dimension(20, 32), label.getPreferredSize());

            label.setMaxLines(3);
            Assertions.assertEquals(new Dimension(20, 48), label.getPreferredSize());

            label.setMaxLines(4);
            Assertions.assertEquals(new Dimension(44, 64), label.getPreferredSize());

            label.setMaxLines(5);
            Assertions.assertEquals(new Dimension(44, 64), label.getPreferredSize());
        });
    }

    @Test
    public void maxLines_0() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("");
            Assertions.assertThrows(IllegalArgumentException.class, () -> label.setMaxLines(0));
        });
    }

    @Test
    public void calculatePreferredSize_3() throws Exception {
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
    public void calculatePreferredSize_lineSpacing() throws Exception {
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
    public void getPreferredSize_lineSpacing_providedLayout() throws Exception {
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
    public void getPreferredSize_lineSpacing_widthLayout() throws Exception {
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
    public void setCurrentWidthIgnoredForPreferredSizeCalculation_1() throws Exception {
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
    public void preferredViewportLineCount() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel(LOREM_IPSUM);
            Assertions.assertEquals(new Dimension(488, 52), label.getPreferredScrollableViewportSize());
            label.setPreferredViewportLineCount(1);
            Assertions.assertEquals(new Dimension(488, 16), label.getPreferredScrollableViewportSize());
            label.setPreferredViewportLineCount(2);
            Assertions.assertEquals(new Dimension(488, 34), label.getPreferredScrollableViewportSize());
            label.setPreferredViewportLineCount(1_000);
            Assertions.assertEquals(new Dimension(488, 52), label.getPreferredScrollableViewportSize());

            label.setText("");
            Assertions.assertEquals(new Dimension(0, 0), label.getPreferredScrollableViewportSize());

            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> label.setPreferredViewportLineCount(0));
        });
    }

    @Test
    public void preferredViewportLineCount_withBorder() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel(LOREM_IPSUM);
            label.setBorder(new EmptyBorder(1, 2, 3, 4));
            Assertions.assertEquals(new Dimension(494, 56), label.getPreferredScrollableViewportSize());
            label.setPreferredViewportLineCount(1);
            Assertions.assertEquals(new Dimension(494, 20), label.getPreferredScrollableViewportSize());
            label.setPreferredViewportLineCount(2);
            Assertions.assertEquals(new Dimension(494, 38), label.getPreferredScrollableViewportSize());
            label.setPreferredViewportLineCount(1_000);
            Assertions.assertEquals(new Dimension(494, 56), label.getPreferredScrollableViewportSize());

            label.setText("");
            Assertions.assertEquals(new Dimension(6, 4), label.getPreferredScrollableViewportSize());
        });
    }

    @Test
    public void setPreferredSize_1() throws Exception {
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

    @Test
    public void copy() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var clipboard = new Clipboard("test");
            var label = new MultilineLabel(LOREM_IPSUM) {
                @Override
                protected Clipboard getClipboard() {
                    return clipboard;
                }
            };
            label.copy();
            Transferable t = clipboard.getContents(null);
            try {
                Assertions.assertEquals(LOREM_IPSUM, t.getTransferData(DataFlavor.stringFlavor));
            } catch (Exception e) {
                Assertions.fail(e);
            }
        });
    }

    @Test
    public void setSeparators() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("text123456789atext123456789");
            label.setPreferredWidthLimit(20);

            label.setSeparators(Set.of('a'));
            Assertions.assertEquals(new Dimension(92, 34), label.getPreferredSize()); // 2 lines

            label.setSeparators(Set.of('t'));
            Assertions.assertEquals(new Dimension(74, 70), label.getPreferredSize()); // 4 lines

            label.setSeparators(Set.of(' '));
            Assertions.assertEquals(new Dimension(177, 16), label.getPreferredSize()); // 1 line

            label.setSeparators(Set.of('a', 't', 'e', 'x', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
            label.setPreferredWidthLimit(1_000);
            Assertions.assertEquals(new Dimension(177, 16), label.getPreferredSize()); // 1 line

            label.setText("text123456789\nt");
            Assertions.assertEquals(new Dimension(85, 34), label.getPreferredSize()); // 2 lines

            label.setPreferredWidthLimit(20);
            Assertions.assertEquals(new Dimension(18, 124), label.getPreferredSize()); // 7 lines
        });
    }

    /**
     * In theory, should produce 3 lines. Actually 2 lines because of the limitation of the current algo.
     */
    @Test
    public void setSeparators_www() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel();
            label.setPreferredWidthLimit(3);
            label.setSeparators(Set.of('w'));
            label.setText("www");
            Assertions.assertEquals(new Dimension(20, 34), label.getPreferredSize()); // 2 lines: "ww" and "w"
        });
    }

    @Test
    public void variousLineBreaksBetween2Lines() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel();

            label.setText("\n\n\nline1\n\n\n\nline2\n\n\n");
            Assertions.assertEquals(new Dimension(27, 196), label.getPreferredSize());

            label.setText("\r\r\rline1\r\r\r\rline2\r\r\r");
            Assertions.assertEquals(new Dimension(27, 196), label.getPreferredSize());

            label.setText("line1\r\n\r\n\r\n\r\nline2");
            Assertions.assertEquals(new Dimension(27, 88), label.getPreferredSize()); // 5 lines
        });
    }
}
