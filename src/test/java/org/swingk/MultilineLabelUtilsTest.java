package org.swingk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.FontMetrics;

class MultilineLabelUtilsTest {
    
    static final Font font = new Font("Dialog", Font.PLAIN, 11);

    @Test
    void getNextLine_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            String text = "text";
            MultilineLabel label = new MultilineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            MultilineLabelUtils.NextLine nextLine = MultilineLabelUtils.getNextLine(text, 0, fm, 10_000);
            Assertions.assertTrue(nextLine.lastLine);
            Assertions.assertEquals(0, nextLine.lineStartIndex);
            Assertions.assertEquals(3, nextLine.lineEndIndex);
            Assertions.assertEquals(-1, nextLine.nextLineStartIndex);
        });
    }

    @Test
    void getNextLine_2() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            String text = "text1 text2";
            MultilineLabel label = new MultilineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            MultilineLabelUtils.NextLine nextLine = MultilineLabelUtils.getNextLine(text, 0, fm, 10_000);
            Assertions.assertTrue(nextLine.lastLine);
            Assertions.assertEquals(0, nextLine.lineStartIndex);
            Assertions.assertEquals(10, nextLine.lineEndIndex);
            Assertions.assertEquals(-1, nextLine.nextLineStartIndex);
        });
    }

    @Test
    void getNextLine_3() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            String text = "text";
            MultilineLabel label = new MultilineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            MultilineLabelUtils.NextLine nextLine = MultilineLabelUtils.getNextLine(text, 0, fm, 1);
            Assertions.assertTrue(nextLine.lastLine);
            Assertions.assertEquals(0, nextLine.lineStartIndex);
            Assertions.assertEquals(3, nextLine.lineEndIndex);
            Assertions.assertEquals(-1, nextLine.nextLineStartIndex);
        });
    }

    @Test
    void getNextLine_4() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            String text = "text1 text2";
            MultilineLabel label = new MultilineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            MultilineLabelUtils.NextLine nextLine1 = MultilineLabelUtils.getNextLine(text, 0, fm, 1);
            Assertions.assertFalse(nextLine1.lastLine);
            Assertions.assertEquals(0, nextLine1.lineStartIndex);
            Assertions.assertEquals(4, nextLine1.lineEndIndex);
            Assertions.assertEquals(6, nextLine1.nextLineStartIndex);
            MultilineLabelUtils.NextLine nextLine2 = MultilineLabelUtils.getNextLine(text, nextLine1.nextLineStartIndex, fm, 1);
            Assertions.assertTrue(nextLine2.lastLine);
            Assertions.assertEquals(6, nextLine2.lineStartIndex);
            Assertions.assertEquals(10, nextLine2.lineEndIndex);
            Assertions.assertEquals(-1, nextLine2.nextLineStartIndex);
        });
    }

    @Test
    void getNextLine_5() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            String text = "t";
            MultilineLabel label = new MultilineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            MultilineLabelUtils.NextLine nextLine = MultilineLabelUtils.getNextLine(text, 0, fm, 1);
            Assertions.assertTrue(nextLine.lastLine);
            Assertions.assertEquals(0, nextLine.lineStartIndex);
            Assertions.assertEquals(0, nextLine.lineEndIndex);
            Assertions.assertEquals(-1, nextLine.nextLineStartIndex);
        });
    }

    @Test
    void getNextLine_6() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            String text = "t abcdvfsl2808kdfkfkdjk94893483dkjdkfjkfjkdjfkdfjdkjdkd";
            MultilineLabel label = new MultilineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            MultilineLabelUtils.NextLine nextLine1 = MultilineLabelUtils.getNextLine(text, 0, fm, 20);
            Assertions.assertFalse(nextLine1.lastLine);
            Assertions.assertEquals(0, nextLine1.lineStartIndex);
            Assertions.assertEquals(0, nextLine1.lineEndIndex);
            Assertions.assertEquals(2, nextLine1.nextLineStartIndex);
            MultilineLabelUtils.NextLine nextLine2 = MultilineLabelUtils.getNextLine(text, nextLine1.nextLineStartIndex, fm, 1);
            Assertions.assertTrue(nextLine2.lastLine);
            Assertions.assertEquals(2, nextLine2.lineStartIndex);
            Assertions.assertEquals(54, nextLine2.lineEndIndex);
            Assertions.assertEquals(-1, nextLine2.nextLineStartIndex);
        });
    }

    @Test
    void getNextLine_7() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            String text = "a abcdvfsl2808kdfkfkdjk94893483dkjdkfjkfjkdjfkdfjdkjdkd abcdvfsl2808kdfkfkdjk94893483dkjdkfjkfjkdjfkdfjdkjdkd";
            MultilineLabel label = new MultilineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            final int limit = 30;
            MultilineLabelUtils.NextLine nextLine1 = MultilineLabelUtils.getNextLine(text, 0, fm, limit);
            Assertions.assertFalse(nextLine1.lastLine);
            Assertions.assertEquals(0, nextLine1.lineStartIndex);
            Assertions.assertEquals(0, nextLine1.lineEndIndex);
            Assertions.assertEquals(2, nextLine1.nextLineStartIndex);
            MultilineLabelUtils.NextLine nextLine2 = MultilineLabelUtils.getNextLine(text, nextLine1.nextLineStartIndex, fm, limit);
            Assertions.assertFalse(nextLine2.lastLine);
            Assertions.assertEquals(2, nextLine2.lineStartIndex);
            Assertions.assertEquals(54, nextLine2.lineEndIndex);
            Assertions.assertEquals(56, nextLine2.nextLineStartIndex);
            MultilineLabelUtils.NextLine nextLine3 = MultilineLabelUtils.getNextLine(text, nextLine2.nextLineStartIndex, fm, limit);
            Assertions.assertTrue(nextLine3.lastLine);
            Assertions.assertEquals(56, nextLine3.lineStartIndex);
            Assertions.assertEquals(108, nextLine3.lineEndIndex);
            Assertions.assertEquals(-1, nextLine3.nextLineStartIndex);
        });
    }

    @Test
    void getNextLine_8() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            String text = "a abcdvfsl2808kdfkfkdjk94893483dkjdkfjkfjkdjfkdfjdkjdkd b";
            MultilineLabel label = new MultilineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            final int limit = 30;
            MultilineLabelUtils.NextLine nextLine1 = MultilineLabelUtils.getNextLine(text, 0, fm, limit);
            Assertions.assertFalse(nextLine1.lastLine);
            Assertions.assertEquals(0, nextLine1.lineStartIndex);
            Assertions.assertEquals(0, nextLine1.lineEndIndex);
            Assertions.assertEquals(2, nextLine1.nextLineStartIndex);
            MultilineLabelUtils.NextLine nextLine2 = MultilineLabelUtils.getNextLine(text, nextLine1.nextLineStartIndex, fm, limit);
            Assertions.assertFalse(nextLine2.lastLine);
            Assertions.assertEquals(2, nextLine2.lineStartIndex);
            Assertions.assertEquals(54, nextLine2.lineEndIndex);
            Assertions.assertEquals(56, nextLine2.nextLineStartIndex);
            MultilineLabelUtils.NextLine nextLine3 = MultilineLabelUtils.getNextLine(text, nextLine2.nextLineStartIndex, fm, limit);
            Assertions.assertTrue(nextLine3.lastLine);
            Assertions.assertEquals(56, nextLine3.lineStartIndex);
            Assertions.assertEquals(56, nextLine3.lineEndIndex);
            Assertions.assertEquals(-1, nextLine3.nextLineStartIndex);
        });
    }
}