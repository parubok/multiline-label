package org.swingk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;

import java.util.List;

class ProvidedTextLayoutTest {
    @Test
    void getLines_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel("\n\nabc \n 12 3\n \n");
            ProvidedTextLayout textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals(List.of("abc", "12 3"), textLayout.getLines());
        });
    }

    @Test
    void getLines_2() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel("line1\n\nline2\n");
            ProvidedTextLayout textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals(List.of("line1", "", "line2"), textLayout.getLines());
        });
    }

    @Test
    void getLines_3() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel("\r\n\nabc \r\n 12 3\r\n \r\n");
            ProvidedTextLayout textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals(List.of("abc", "12 3"), textLayout.getLines());
        });
    }

    @Test
    void getLines_4() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel("line1\r\n\r\nline2\r\n");
            ProvidedTextLayout textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals(List.of("line1", "", "line2"), textLayout.getLines());
        });
    }

    @Test
    void getLines_5() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel("ab c\r\n12 3");
            ProvidedTextLayout textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals(List.of("ab c", "12 3"), textLayout.getLines());
        });
    }

    @Test
    void hasLines() {
        Assertions.assertTrue(ProvidedTextLayout.hasLines("abc\r\n123"));
        Assertions.assertTrue(ProvidedTextLayout.hasLines("abc\n123"));
        Assertions.assertFalse(ProvidedTextLayout.hasLines("abc 123"));
        Assertions.assertFalse(ProvidedTextLayout.hasLines("\r\nabc 123\n"));
        Assertions.assertFalse(ProvidedTextLayout.hasLines("\r\n\n"));
        Assertions.assertFalse(ProvidedTextLayout.hasLines(""));
        Assertions.assertFalse(ProvidedTextLayout.hasLines(" "));
    }
}
