package org.swingk;

import java.awt.FontMetrics;

import static javax.swing.SwingUtilities.computeStringWidth;

class MultiLineLabelUtils {

    private MultiLineLabelUtils() {
    }

    /**
     * @param text       Text to display in {@link MultiLineLabel}.
     * @param startIndex Index of 1st character in the new line.
     * @param fm         Current {@link FontMetrics}.
     * @param widthLimit Limit on the width of the line.
     * @return Object with details of the next line.
     */
    static NextLine getNextLine(String text, int startIndex, FontMetrics fm, int widthLimit) {
        assert text != null;
        assert text.length() > 0;
        assert startIndex > -1;
        assert fm != null;
        assert widthLimit > 0;
        int spaceIndex = startIndex;
        while (true) {
            int newSpaceIndex = text.indexOf(' ', spaceIndex + 1);
            if (newSpaceIndex == -1) {
                if (spaceIndex > startIndex && computeStringWidth(fm, text.substring(startIndex)) > widthLimit) {
                    return new NextLine(false, startIndex, spaceIndex - 1, spaceIndex + 1);
                } else {
                    return new NextLine(true, startIndex, text.length() - 1, -1);
                }
            } else {
                if (computeStringWidth(fm, text.substring(startIndex, newSpaceIndex - 1)) > widthLimit) {
                    if (spaceIndex > startIndex) {
                        return new NextLine(false, startIndex, spaceIndex - 1, spaceIndex + 1);
                    } else {
                        return new NextLine(false, startIndex, newSpaceIndex - 1, newSpaceIndex + 1);
                    }
                } else {
                    spaceIndex = newSpaceIndex; // continue
                }
            }
        }
    }

    static class NextLine {
        /**
         * True if this is the last line of the text (end of text).
         */
        public final boolean lastLine;

        /**
         * Index of first character in the line. Inclusive.
         */
        public final int lineStartIndex;

        /**
         * Index of last character in the line. Inclusive.
         */
        public final int lineEndIndex;

        /**
         * Index of first character in the line after that line. Inclusive.
         */
        public final int nextLineStartIndex;

        NextLine(boolean lastLine,
                 int lineStartIndex,
                 int lineEndIndex,
                 int nextLineStartIndex) {
            this.lastLine = lastLine;
            this.lineStartIndex = lineStartIndex;
            this.lineEndIndex = lineEndIndex;
            this.nextLineStartIndex = nextLineStartIndex;
        }

        @Override
        public String toString() {
            return "NextLine{" +
                    "lastLine=" + lastLine +
                    ", lineStartIndex=" + lineStartIndex +
                    ", lineEndIndex=" + lineEndIndex +
                    ", nextLineStartIndex=" + nextLineStartIndex +
                    '}';
        }
    }
}
