package org.swingk.text.multiline;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.awt.Font;

public class WidthTextLayoutTest {

    static final Font font = new Font("Dialog", Font.PLAIN, 11);

    @Test
    public void toRenderedText_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Assertions.assertEquals("", WidthTextLayout.toRenderedText(""));
            Assertions.assertEquals("", WidthTextLayout.toRenderedText(" "));
            Assertions.assertEquals("", WidthTextLayout.toRenderedText("  "));
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText("a b"));
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText("a  b"));
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText("a   b"));
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText("a    b"));
            Assertions.assertEquals("a b c d", WidthTextLayout.toRenderedText(" a  b c  d "));
            Assertions.assertEquals("abcd", WidthTextLayout.toRenderedText("abcd"));
            Assertions.assertEquals("abcd 1", WidthTextLayout.toRenderedText("abcd 1"));
            Assertions.assertEquals("abcd 12", WidthTextLayout.toRenderedText("abcd 12"));
        });
    }

    @Test
    public void toRenderedText_2() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText("a\nb"));
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText("a \n b"));
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText("\na \n b\n"));
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText(" \n a \n b \n"));
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText(" \n a\n\nb \n"));
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText("a  \n  b"));
        });
    }

    @Test
    public void getNextLine_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var text = "text";
            var label = new MultilineLabel();
            var fm = label.getFontMetrics(font);
            var nextLine = WidthTextLayout.getNextLine(label, text, 0, fm, 10_000);
            Assertions.assertTrue(nextLine.lastLine);
            Assertions.assertEquals(0, nextLine.lineStartIndex);
            Assertions.assertEquals(3, nextLine.lineEndIndex);
            Assertions.assertEquals(-1, nextLine.nextLineStartIndex);
        });
    }

    @Test
    public void getNextLine_2() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var text = "text1 text2";
            var label = new MultilineLabel();
            var fm = label.getFontMetrics(font);
            var nextLine = WidthTextLayout.getNextLine(label, text, 0, fm, 10_000);
            Assertions.assertTrue(nextLine.lastLine);
            Assertions.assertEquals(0, nextLine.lineStartIndex);
            Assertions.assertEquals(10, nextLine.lineEndIndex);
            Assertions.assertEquals(-1, nextLine.nextLineStartIndex);
        });
    }

    @Test
    public void getNextLine_3() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var text = "text";
            var label = new MultilineLabel();
            var fm = label.getFontMetrics(font);
            var nextLine = WidthTextLayout.getNextLine(label, text, 0, fm, 1);
            Assertions.assertTrue(nextLine.lastLine);
            Assertions.assertEquals(0, nextLine.lineStartIndex);
            Assertions.assertEquals(3, nextLine.lineEndIndex);
            Assertions.assertEquals(-1, nextLine.nextLineStartIndex);
        });
    }

    @Test
    public void getNextLine_4() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var text = "text1 text2";
            var label = new MultilineLabel();
            var fm = label.getFontMetrics(font);
            var nextLine1 = WidthTextLayout.getNextLine(label, text, 0, fm, 1);
            Assertions.assertFalse(nextLine1.lastLine);
            Assertions.assertEquals(0, nextLine1.lineStartIndex);
            Assertions.assertEquals(4, nextLine1.lineEndIndex);
            Assertions.assertEquals(6, nextLine1.nextLineStartIndex);
            var nextLine2 = WidthTextLayout.getNextLine(label, text, nextLine1.nextLineStartIndex, fm, 1);
            Assertions.assertTrue(nextLine2.lastLine);
            Assertions.assertEquals(6, nextLine2.lineStartIndex);
            Assertions.assertEquals(10, nextLine2.lineEndIndex);
            Assertions.assertEquals(-1, nextLine2.nextLineStartIndex);
        });
    }

    @Test
    public void getNextLine_5() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var text = "t";
            var label = new MultilineLabel();
            var fm = label.getFontMetrics(font);
            var nextLine = WidthTextLayout.getNextLine(label, text, 0, fm, 1);
            Assertions.assertTrue(nextLine.lastLine);
            Assertions.assertEquals(0, nextLine.lineStartIndex);
            Assertions.assertEquals(0, nextLine.lineEndIndex);
            Assertions.assertEquals(-1, nextLine.nextLineStartIndex);
        });
    }

    @Test
    public void getNextLine_6() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var text = "t abcdvfsl2808kdfkfkdjk94893483dkjdkfjkfjkdjfkdfjdkjdkd";
            var label = new MultilineLabel();
            var fm = label.getFontMetrics(font);
            var nextLine1 = WidthTextLayout.getNextLine(label, text, 0, fm, 20);
            Assertions.assertFalse(nextLine1.lastLine);
            Assertions.assertEquals(0, nextLine1.lineStartIndex);
            Assertions.assertEquals(0, nextLine1.lineEndIndex);
            Assertions.assertEquals(2, nextLine1.nextLineStartIndex);
            var nextLine2 = WidthTextLayout.getNextLine(label, text, nextLine1.nextLineStartIndex, fm, 1);
            Assertions.assertTrue(nextLine2.lastLine);
            Assertions.assertEquals(2, nextLine2.lineStartIndex);
            Assertions.assertEquals(54, nextLine2.lineEndIndex);
            Assertions.assertEquals(-1, nextLine2.nextLineStartIndex);
        });
    }

    @Test
    public void getNextLine_7() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var text = "a abcdvfsl2808kdfkfkdjk94893483dkjdkfjkfjkdjfkdfjdkjdkd abcdvfsl2808kdfkfkdjk94893483dkjdkfjkfjkdjfkdfjdkjdkd";
            var label = new MultilineLabel();
            var fm = label.getFontMetrics(font);
            final int limit = 30;
            var nextLine1 = WidthTextLayout.getNextLine(label, text, 0, fm, limit);
            Assertions.assertFalse(nextLine1.lastLine);
            Assertions.assertEquals(0, nextLine1.lineStartIndex);
            Assertions.assertEquals(0, nextLine1.lineEndIndex);
            Assertions.assertEquals(2, nextLine1.nextLineStartIndex);
            var nextLine2 = WidthTextLayout.getNextLine(label, text, nextLine1.nextLineStartIndex, fm, limit);
            Assertions.assertFalse(nextLine2.lastLine);
            Assertions.assertEquals(2, nextLine2.lineStartIndex);
            Assertions.assertEquals(54, nextLine2.lineEndIndex);
            Assertions.assertEquals(56, nextLine2.nextLineStartIndex);
            var nextLine3 = WidthTextLayout.getNextLine(label, text, nextLine2.nextLineStartIndex, fm, limit);
            Assertions.assertTrue(nextLine3.lastLine);
            Assertions.assertEquals(56, nextLine3.lineStartIndex);
            Assertions.assertEquals(108, nextLine3.lineEndIndex);
            Assertions.assertEquals(-1, nextLine3.nextLineStartIndex);
        });
    }

    @Test
    public void getNextLine_8() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var text = "a abcdvfsl2808kdfkfkdjk94893483dkjdkfjkfjkdjfkdfjdkjdkd b";
            var label = new MultilineLabel();
            var fm = label.getFontMetrics(font);
            final int limit = 30;
            var nextLine1 = WidthTextLayout.getNextLine(label, text, 0, fm, limit);
            Assertions.assertFalse(nextLine1.lastLine);
            Assertions.assertEquals(0, nextLine1.lineStartIndex);
            Assertions.assertEquals(0, nextLine1.lineEndIndex);
            Assertions.assertEquals(2, nextLine1.nextLineStartIndex);
            var nextLine2 = WidthTextLayout.getNextLine(label, text, nextLine1.nextLineStartIndex, fm, limit);
            Assertions.assertFalse(nextLine2.lastLine);
            Assertions.assertEquals(2, nextLine2.lineStartIndex);
            Assertions.assertEquals(54, nextLine2.lineEndIndex);
            Assertions.assertEquals(56, nextLine2.nextLineStartIndex);
            var nextLine3 = WidthTextLayout.getNextLine(label, text, nextLine2.nextLineStartIndex, fm, limit);
            Assertions.assertTrue(nextLine3.lastLine);
            Assertions.assertEquals(56, nextLine3.lineStartIndex);
            Assertions.assertEquals(56, nextLine3.lineEndIndex);
            Assertions.assertEquals(-1, nextLine3.nextLineStartIndex);
        });
    }
}
