package org.swingk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;

import static java.util.Arrays.asList;

class ProvidedTextLayoutTest {
    @Test
    void test_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel("\n\nabc \n 12 3\n \n");
            ProvidedTextLayout textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals("\n", textLayout.getLineSeparator());
            Assertions.assertEquals(asList("abc", "12 3"), textLayout.getLines());
        });
    }

    @Test
    void test_2() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel("line1\n\nline2\n");
            ProvidedTextLayout textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals("\n", textLayout.getLineSeparator());
            Assertions.assertEquals(asList("line1", "", "line2"), textLayout.getLines());
        });
    }

    @Test
    void test_3() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel("\r\n\nabc \r\n 12 3\r\n \r\n");
            ProvidedTextLayout textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals("\r\n", textLayout.getLineSeparator());
            Assertions.assertEquals(asList("abc", "12 3"), textLayout.getLines());
        });
    }

    @Test
    void test_4() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel("line1\r\n\r\nline2\r\n");
            ProvidedTextLayout textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals("\r\n", textLayout.getLineSeparator());
            Assertions.assertEquals(asList("line1", "", "line2"), textLayout.getLines());
        });
    }
}
