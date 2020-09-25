package org.swingk;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;

import static javax.swing.SwingUtilities.computeStringWidth;

public class MultilineLabelUtils {

    private MultilineLabelUtils() {
    }

    /**
     * Draws {@code text} in a style of disabled component text at {@link Graphics} context from the point (x,y). Uses
     * {@code color} as a base.
     */
    public static void paintTextInDisabledStyle(String text, Graphics g, Color color, int x, int y) {
        g.setColor(color.brighter());
        g.drawString(text, x + 1, y + 1);
        g.setColor(color.darker());
        g.drawString(text, x, y);
    }

    /**
     * @param insets Component insets. Not null.
     * @param fm Component {@link FontMetrics}. Not null.
     * @param text Text to paint. Not null.
     * @param wLimit Requested component width limit in pixels. Greater than 0.
     * @return Component preferred size.
     */
    public static Dimension calcComponentPreferredSizeForWidthLimit(Insets insets, FontMetrics fm, String text,
                                                                    int wLimit) {
        assert insets != null;
        assert fm != null;
        assert text != null;
        assert wLimit > 0;

        final int horInsets = insets.right + insets.left;
        int textPrefWidth;
        int textPrefHeight;
        if (!text.isEmpty()) {
            assert fm != null;
            NextLine nextLine;
            int startIndex = 0;
            final int textWidthLimit = Math.max(1, wLimit - horInsets);
            int lineCount = 0;
            int maxLineWidth = 0; // pixels
            do {
                nextLine = getNextLine(text, startIndex, fm, textWidthLimit);
                String nextLineStr = text.substring(nextLine.lineStartIndex, nextLine.lineEndIndex + 1);
                int nextLineWidth = computeStringWidth(fm, nextLineStr);
                maxLineWidth = Math.max(maxLineWidth, nextLineWidth);
                lineCount++;
                startIndex = nextLine.nextLineStartIndex;
            } while (!nextLine.lastLine);
            textPrefWidth = maxLineWidth;
            textPrefHeight = (fm.getAscent() + fm.getDescent()) * lineCount + fm.getLeading() * (lineCount - 1);
        } else {
            textPrefWidth = textPrefHeight = 0;
        }
        return new Dimension(textPrefWidth + horInsets, textPrefHeight + insets.top + insets.bottom);
    }

    /**
     * @param text       Text to display in {@link MultilineLabel}.
     * @param startIndex Index of 1st character in the new line.
     * @param fm         Current {@link FontMetrics}.
     * @param widthLimit Limit on the width of the line.
     * @return Object with details of the next line.
     */
    static NextLine getNextLine(final String text, final int startIndex, final FontMetrics fm, final int widthLimit) {
        assert text != null;
        assert text.length() > 0;
        assert startIndex > -1;
        assert fm != null;
        assert widthLimit > 0;
        int spaceIndex = startIndex;
        while (true) {
            int nextSpaceIndex = text.indexOf(' ', spaceIndex + 1);
            if (nextSpaceIndex == -1) { // there is no next space after spaceIndex
                if (spaceIndex > startIndex && computeStringWidth(fm, text.substring(startIndex)) > widthLimit) {
                    // next line will be single word last line
                    return new NextLine(false, startIndex, spaceIndex - 1, spaceIndex + 1);
                } else {
                    // last line
                    return new NextLine(true, startIndex, text.length() - 1, -1);
                }
            } else { // there is next space after spaceIndex
                if (computeStringWidth(fm, text.substring(startIndex, nextSpaceIndex)) > widthLimit) {
                    if (spaceIndex > startIndex) {
                        // regular next line
                        return new NextLine(false, startIndex, spaceIndex - 1, spaceIndex + 1);
                    } else {
                        // single word line
                        return new NextLine(false, startIndex, nextSpaceIndex - 1, nextSpaceIndex + 1);
                    }
                } else {
                    spaceIndex = nextSpaceIndex; // continue with current line
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
