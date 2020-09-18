package org.swingk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.util.Arrays;

class ProvidedTextLayoutTest {
    @Test
    void getPreferredSize_2() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultilineLabel label = new MultilineLabel();
            label.setText("\n\nabc \n 12 3\n \n");
            ProvidedTextLayout textLayout = (ProvidedTextLayout) label.getTextLayout();
            Assertions.assertEquals(Arrays.asList("abc", "12 3"), textLayout.getLines());
        });
    }
}
