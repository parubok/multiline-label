package io.github.parubok.text.multiline;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;

import java.util.List;

public class ProvidedTextLayoutTest {
    @Test
    public void getLines_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("\n\nabc \n 12 3\n \n", true);
            var textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals(List.of("abc", "12 3"), textLayout.getLines());
        });
    }

    @Test
    public void getLines_2() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("line1\n\nline2\n", true);
            var textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals(List.of("line1", "", "line2"), textLayout.getLines());
        });
    }

    @Test
    public void getLines_3() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("\r\n\nabc \r\n 12 3\r\n \r\n", true);
            var textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals(List.of("abc", "12 3"), textLayout.getLines());
        });
    }

    @Test
    public void getLines_4() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("line1\r\n\r\nline2\r\n", true);
            var textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals(List.of("line1", "", "line2"), textLayout.getLines());
        });
    }

    @Test
    public void getLines_5() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var label = new MultilineLabel("ab c\r\n12 3", true);
            var textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals(List.of("ab c", "12 3"), textLayout.getLines());
        });
    }
}
