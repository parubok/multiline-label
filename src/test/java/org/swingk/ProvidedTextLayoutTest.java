package org.swingk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.util.Arrays;

class ProvidedTextLayoutTest {
    @Test
    void toLines_1() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Assertions.assertEquals(Arrays.asList("abc", "12 3"), ProvidedTextLayout.toLines("\n\nabc \n 12 3\n \n"));
            Assertions.assertEquals(Arrays.asList("line1", "", "line2"), ProvidedTextLayout.toLines("line1\n\nline2\n"));
        });
    }
}
