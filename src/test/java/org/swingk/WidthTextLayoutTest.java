package org.swingk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;

class WidthTextLayoutTest {
    @Test
    void toRenderedText_1() throws Exception {
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
    void toRenderedText_2() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText("a\nb"));
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText("a \n b"));
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText("\na \n b\n"));
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText(" \n a \n b \n"));
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText(" \n a\n\nb \n"));
            Assertions.assertEquals("a b", WidthTextLayout.toRenderedText("a  \n  b"));
        });
    }
}
