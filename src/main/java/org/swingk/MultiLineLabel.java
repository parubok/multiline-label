package org.swingk;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * Text label capable of word wrapping.
 * Multiple spaces are displayed as one space character.
 * Warning: empty lines are currently skipped - should be fixed.
 */
public class MultiLineLabel extends JComponent {
    private String text = "";

    private int wrappingPreferredWidth = 500; //in pixels; 500 is just a default value; relevant when wrapping text without "\n";

    public MultiLineLabel() {
        super();
    }

    public MultiLineLabel(String text) {
        this();
        setText(text);
    }

    private static int rowHeight(FontMetrics fm) {
        return fm.getHeight() + fm.getLeading();
    }

    /**
     * Draws {@code text} in a style of disabled component text at {@link Graphics} context from the point (x,y). Uses
     * {@code color} as a base.
     *
     * @param vertical true - if {@link Graphics} rotated vertically to -90 degree.
     */
    public static void paintTextInDisabledStyle(String text, Graphics g, Color color, int x, int y, boolean vertical) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(color.brighter());
        int pixelShift = vertical ? -1 : 1;
        g2d.drawString(text, x + pixelShift, y + pixelShift); // draw title text
        g2d.setColor(color.darker());
        g2d.drawString(text, x, y);
        g2d.dispose();
    }

    /**
     * Adds the specified {@link Insets} to the {@link Dimension} object and returns the new {@link Dimension}.
     */
    private static Dimension addInsets(Dimension size, Insets insets) {
        return new Dimension(size.width + insets.left + insets.right, size.height + insets.top + insets.bottom);
    }

    /**
     * @return @[0] number of pixels left between last word of the 1st row and right border
     * if exists, these pixels make label look not nice and should be minimized.
     * @[1] number of pixels to increase each space in the 1st row
     */
    private int[] firstRowEmptyWidth(String text, int textWidth, int spaceWidth, FontMetrics fm) {
        StringTokenizer tokenizer = new StringTokenizer(text);
        int words = 0;
        int x = 0;
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            int wordWidth = SwingUtilities.computeStringWidth(fm, word);
            if ((x > 0) && ((x + wordWidth) > textWidth)) {
                int emptyWidth = (textWidth - x + spaceWidth);
                int spaceIncr = Math.min(3, Math.max(1, emptyWidth / words));
                return new int[]{emptyWidth, spaceIncr};
            }
            x = x + wordWidth + spaceWidth;
            words++;
        }
        return new int[]{0, 0};
    }

    private boolean textContainsEOL() {
        int endOfLineIndex = text.indexOf("\n");
        return ((endOfLineIndex > -1) && (endOfLineIndex < (text.length() - 1)));
    }

    private void paintText(int rowHeight, Graphics g, JComponent c, Insets customInsets) {
        Insets insets = (customInsets != null) ? customInsets : c.getInsets();
        int textWidth = (c.getWidth() - insets.left - insets.right);
        if (textWidth > 1) {
            FontMetrics fm = c.getFontMetrics(c.getFont());
            g.setColor(c.getForeground());
            int x = insets.left;
            int y = insets.top + fm.getAscent();

            if (!textContainsEOL()) {
                int spaceWidth = SwingUtilities.computeStringWidth(fm, " ");
                StringTokenizer tokenizer = new StringTokenizer(text);
                boolean firstRow = true;
                int[] firstRowEmptyWidth = firstRowEmptyWidth(text, textWidth, spaceWidth, fm);
                while (tokenizer.hasMoreTokens()) {
                    String word = tokenizer.nextToken();
                    int wordWidth = SwingUtilities.computeStringWidth(fm, word);
                    if ((x > insets.left) && ((x + wordWidth) > (textWidth + insets.left))) {
                        x = insets.left;
                        firstRow = false;
                        y = y + rowHeight;
                    }

                    if (c.isEnabled()) {
                        g.drawString(word, x, y);
                    } else {
                        paintTextInDisabledStyle(word, g, c.getBackground(), x, y, false);
                    }

                    int spaceIncr = 0;
                    if (firstRow && (firstRowEmptyWidth[0] > 0)) {
                        spaceIncr = firstRowEmptyWidth[1]; //add pixels to make 1st row a bit longer (it looks nicer)
                        firstRowEmptyWidth[0] = (firstRowEmptyWidth[0] - firstRowEmptyWidth[1]);
                    }
                    x = x + wordWidth + spaceWidth + spaceIncr;
                }
            } else {
                /**
                 * ignore wrappingPreferredWidth in this case - just use '\n' as row separator
                 */
                StringTokenizer tokenizer = new StringTokenizer(text, "\n");
                while (tokenizer.hasMoreTokens()) {
                    String line = tokenizer.nextToken();
                    g.drawString(line, x, y);
                    y = y + rowHeight;
                }
            }
        }
    }

    private Dimension getPreferredTextSize(FontMetrics fm, int textWrappingWidthThreshold, int rowHeight) {
        int lines = 0;
        int maxWidth = 0;
        if (!textContainsEOL()) {
            List<String> lineList = new ArrayList<>();
            int lineStartIndex = 0;
            while (true) {
                int spaceIndex = text.indexOf(' ', lineStartIndex);
                if (spaceIndex == -1) {
                    lineList.add(text.substring(lineStartIndex)); // last line
                    break;
                }
                String substring;
                int wordsInLine = 0;
                while (true) {
                    substring = text.substring(lineStartIndex, spaceIndex - 1);

                    // try to advance a word:
                    int nextSpaceIndex = text.indexOf(' ', spaceIndex + 1);
                    if (nextSpaceIndex == -1) {
                        if (SwingUtilities.computeStringWidth(fm, text.substring(lineStartIndex)) > textWrappingWidthThreshold) {
                            break;
                        } else {
                            substring = text.substring(lineStartIndex);
                        }
                        break;
                    }
                    if (SwingUtilities.computeStringWidth(fm, text.substring(lineStartIndex, nextSpaceIndex - 1)) > textWrappingWidthThreshold) {
                        break;
                    } else {
                        wordsInLine++;
                        spaceIndex = nextSpaceIndex;
                    }
                }
                lineList.add(substring);
                if (spaceIndex == -1) {
                    break;
                }
                lineStartIndex = spaceIndex + 1;
            }
        } else {
            /**
             * ignore wrappingPreferredWidth in this case
             */
            StringTokenizer tokenizer = new StringTokenizer(text, "\n");
            while (tokenizer.hasMoreTokens()) {
                lines++;
                String line = tokenizer.nextToken();
                int w = SwingUtilities.computeStringWidth(fm, line);
                if (w > maxWidth) {
                    maxWidth = w;
                }
            }
        }
        return new Dimension(maxWidth, lines * rowHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        if (!text.isEmpty()) {
            g.setColor(getForeground());
            g.setFont(getFont());
            paintText(rowHeight(g.getFontMetrics()), g, this, null);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        if (fm != null && text.length() > 0) {
            Insets insets = getInsets();
            Dimension textPS = getPreferredTextSize(fm, wrappingPreferredWidth - insets.right - insets.left,
                    rowHeight(fm));
            return addInsets(textPS, insets);
        }
        return super.getPreferredSize();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = Objects.requireNonNull(text);
        revalidate();
        repaint();
    }

    public int getWrappingPreferredWidth() {
        return wrappingPreferredWidth;
    }

    public void setWrappingPreferredWidth(int wrappingPreferredWidth) {
        if (wrappingPreferredWidth < 1) {
            throw new IllegalArgumentException();
        }
        this.wrappingPreferredWidth = wrappingPreferredWidth;
        revalidate();
        repaint();
    }

    public void setPreferredLines(int lines) {
    }

    public void setPreferredWidth(int width) {
    }

    public void setPreferredHeight(int height) {
    }
}
