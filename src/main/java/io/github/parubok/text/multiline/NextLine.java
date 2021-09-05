package io.github.parubok.text.multiline;

final class NextLine {

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

    /**
     * @param text The label text.
     * @param lineCount Current line count (including this line).
     * @param maxLines Maximum number of lines to paint on the label.
     * @return String to paint for this line.
     */
    String stringToPaint(String text, int lineCount, int maxLines) {
        boolean paintAsEllipsis = !lastLine && lineCount == maxLines;
        return paintAsEllipsis ? "..." : text.substring(lineStartIndex, lineEndIndex + 1);
    }

    boolean hasMoreLines(int lineCount, int maxLines) {
        return !lastLine && lineCount < maxLines;
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
