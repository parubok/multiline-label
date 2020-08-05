package org.swingk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.awt.Dimension;

class MultiLineLabelTest {
    @Test
    void illegal_text() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultiLineLabel label = new MultiLineLabel();
            Assertions.assertThrows(IllegalArgumentException.class, () -> label.setText("a  b"));
            Assertions.assertThrows(IllegalArgumentException.class, () -> label.setText("abc "));
            Assertions.assertThrows(IllegalArgumentException.class, () -> label.setText(" abc"));
            Assertions.assertThrows(IllegalArgumentException.class, () -> label.setText("a\nbc"));
        });
    }

    @Test
    void basic_test() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MultiLineLabel label = new MultiLineLabel();
            Assertions.assertEquals(new Dimension(0, 0), label.getPreferredSize());
        });
    }
}
