package org.swingk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.FontMetrics;

class MultiLineLabelUtilsTest {
    
    static final Font font = new Font("Dialog", Font.PLAIN, 11);

    @Test
    void getNextLine_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            String text = "text";
            MultiLineLabel label = new MultiLineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            MultiLineLabelUtils.NextLine nextLine = MultiLineLabelUtils.getNextLine(text, 0, fm, 10_000);
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
            MultiLineLabel label = new MultiLineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            MultiLineLabelUtils.NextLine nextLine = MultiLineLabelUtils.getNextLine(text, 0, fm, 10_000);
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
            MultiLineLabel label = new MultiLineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            MultiLineLabelUtils.NextLine nextLine = MultiLineLabelUtils.getNextLine(text, 0, fm, 1);
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
            MultiLineLabel label = new MultiLineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            MultiLineLabelUtils.NextLine nextLine1 = MultiLineLabelUtils.getNextLine(text, 0, fm, 1);
            Assertions.assertFalse(nextLine1.lastLine);
            Assertions.assertEquals(0, nextLine1.lineStartIndex);
            Assertions.assertEquals(4, nextLine1.lineEndIndex);
            Assertions.assertEquals(6, nextLine1.nextLineStartIndex);
            MultiLineLabelUtils.NextLine nextLine2 = MultiLineLabelUtils.getNextLine(text, nextLine1.nextLineStartIndex, fm, 1);
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
            MultiLineLabel label = new MultiLineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            MultiLineLabelUtils.NextLine nextLine = MultiLineLabelUtils.getNextLine(text, 0, fm, 1);
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
            MultiLineLabel label = new MultiLineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            MultiLineLabelUtils.NextLine nextLine1 = MultiLineLabelUtils.getNextLine(text, 0, fm, 20);
            Assertions.assertFalse(nextLine1.lastLine);
            Assertions.assertEquals(0, nextLine1.lineStartIndex);
            Assertions.assertEquals(0, nextLine1.lineEndIndex);
            Assertions.assertEquals(2, nextLine1.nextLineStartIndex);
            MultiLineLabelUtils.NextLine nextLine2 = MultiLineLabelUtils.getNextLine(text, nextLine1.nextLineStartIndex, fm, 1);
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
            MultiLineLabel label = new MultiLineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            final int limit = 30;
            MultiLineLabelUtils.NextLine nextLine1 = MultiLineLabelUtils.getNextLine(text, 0, fm, limit);
            Assertions.assertFalse(nextLine1.lastLine);
            Assertions.assertEquals(0, nextLine1.lineStartIndex);
            Assertions.assertEquals(0, nextLine1.lineEndIndex);
            Assertions.assertEquals(2, nextLine1.nextLineStartIndex);
            MultiLineLabelUtils.NextLine nextLine2 = MultiLineLabelUtils.getNextLine(text, nextLine1.nextLineStartIndex, fm, limit);
            Assertions.assertFalse(nextLine2.lastLine);
            Assertions.assertEquals(2, nextLine2.lineStartIndex);
            Assertions.assertEquals(54, nextLine2.lineEndIndex);
            Assertions.assertEquals(56, nextLine2.nextLineStartIndex);
            MultiLineLabelUtils.NextLine nextLine3 = MultiLineLabelUtils.getNextLine(text, nextLine2.nextLineStartIndex, fm, limit);
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
            MultiLineLabel label = new MultiLineLabel();
            FontMetrics fm = label.getFontMetrics(font);
            final int limit = 30;
            MultiLineLabelUtils.NextLine nextLine1 = MultiLineLabelUtils.getNextLine(text, 0, fm, limit);
            Assertions.assertFalse(nextLine1.lastLine);
            Assertions.assertEquals(0, nextLine1.lineStartIndex);
            Assertions.assertEquals(0, nextLine1.lineEndIndex);
            Assertions.assertEquals(2, nextLine1.nextLineStartIndex);
            MultiLineLabelUtils.NextLine nextLine2 = MultiLineLabelUtils.getNextLine(text, nextLine1.nextLineStartIndex, fm, limit);
            Assertions.assertFalse(nextLine2.lastLine);
            Assertions.assertEquals(2, nextLine2.lineStartIndex);
            Assertions.assertEquals(54, nextLine2.lineEndIndex);
            Assertions.assertEquals(56, nextLine2.nextLineStartIndex);
            MultiLineLabelUtils.NextLine nextLine3 = MultiLineLabelUtils.getNextLine(text, nextLine2.nextLineStartIndex, fm, limit);
            Assertions.assertTrue(nextLine3.lastLine);
            Assertions.assertEquals(56, nextLine3.lineStartIndex);
            Assertions.assertEquals(56, nextLine3.lineEndIndex);
            Assertions.assertEquals(-1, nextLine3.nextLineStartIndex);
        });
    }
}
