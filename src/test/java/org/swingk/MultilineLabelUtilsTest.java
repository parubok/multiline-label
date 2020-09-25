package org.swingk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MultilineLabelUtilsTest {
    @Test
    void hasLineSeparators() {
        Assertions.assertTrue(MultilineLabelUtils.hasLineSeparators("abc\r\n123"));
        Assertions.assertTrue(MultilineLabelUtils.hasLineSeparators("abc\n123"));
        Assertions.assertFalse(MultilineLabelUtils.hasLineSeparators("abc 123"));
    }
}
