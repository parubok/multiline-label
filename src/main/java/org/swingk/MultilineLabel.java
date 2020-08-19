package org.swingk;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Objects;

import static javax.swing.SwingUtilities.computeStringWidth;

/**
 * Text label capable of word wrapping.
 */
public class MultilineLabel extends JComponent implements Scrollable {
    private String text = "";

    /**
     * Default label width limit in pixels.
     */
    public static final int DEFAULT_WIDTH_LIMIT = 500;

    private int prefWidthLimit = DEFAULT_WIDTH_LIMIT;

    public MultilineLabel() {
        super();
        setOpaque(true);
        LookAndFeel.installColorsAndFont(this, "Label.background", "Label.foreground", "Label.font");
    }

    public MultilineLabel(String text) {
        this();
        setText(text);
    }

    /**
     * Draws {@code text} in a style of disabled component text at {@link Graphics} context from the point (x,y). Uses
     * {@code color} as a base.
     */
    private static void paintTextInDisabledStyle(String text, Graphics g, Color color, int x, int y) {
        g.setColor(color.brighter());
        g.drawString(text, x + 1, y + 1); // draw title text
        g.setColor(color.darker());
        g.drawString(text, x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        if (!text.isEmpty()) {
            Insets insets = getInsets();
            int textWidth = (getWidth() - insets.left - insets.right);
            if (textWidth > 1) {
                g.setFont(getFont());
                FontMetrics fm = g.getFontMetrics();
                g.setColor(getForeground());
                int x = insets.left;
                int y = insets.top + fm.getAscent();
                boolean enabled = isEnabled();
                MultilineLabelUtils.NextLine nextLine;
                int index = 0;
                int widthLimit = getWidth() - insets.right - insets.left;
                do {
                    nextLine = MultilineLabelUtils.getNextLine(text, index, fm, widthLimit);
                    String lineStr = text.substring(nextLine.lineStartIndex, nextLine.lineEndIndex + 1);
                    if (enabled) {
                        g.drawString(lineStr, x, y);
                    } else {
                        paintTextInDisabledStyle(lineStr, g, getBackground(), x, y);
                    }
                    y += fm.getHeight();
                    index = nextLine.nextLineStartIndex;
                } while (!nextLine.lastLine);
            }
        }
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        if (width > 0 && height > 0 && width != getWidth() && calcPreferredSize(width).height > height) {
            // the component bounds are insufficient to display the entire text - request another layout attempt.
            SwingUtilities.invokeLater(() -> {
                revalidate();
                repaint();
            });
        }
        super.setBounds(x, y, width, height);
    }

    @Override
    public Dimension getPreferredSize() {
        return calcPreferredSize(0);
    }

    /**
     * @param expectedLabelWidth If 0 - use fallback resolution.
     */
    private Dimension calcPreferredSize(int expectedLabelWidth) {
        Insets insets = getInsets();
        final int horInsets = insets.right + insets.left;
        int textPrefWidth;
        int textPrefHeight;
        if (!text.isEmpty()) {
            final FontMetrics fm = getFontMetrics(getFont());
            assert fm != null;
            MultilineLabelUtils.NextLine nextLine;
            int startIndex = 0;
            final int wLimit;
            if (expectedLabelWidth > 0) {
                wLimit = expectedLabelWidth;
            } else if (getWidth() > 0) {
                // https://stackoverflow.com/questions/39455573/how-to-set-fixed-width-but-dynamic-height-on-jtextpane/39466255#39466255
                wLimit = getWidth();
            } else {
                wLimit = prefWidthLimit;
            }
            final int textWidthLimit = Math.max(1, wLimit - horInsets);
            int lineCount = 0;
            int maxLineWidth = 0; // pixels
            do {
                nextLine = MultilineLabelUtils.getNextLine(text, startIndex, fm, textWidthLimit);
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
        Dimension ps = new Dimension(textPrefWidth + horInsets, textPrefHeight + insets.top + insets.bottom);
        return ps;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        Objects.requireNonNull(text);
        if (text.contains("\n")) {
            throw new IllegalArgumentException("Text contains EOL.");
        }
        if (text.startsWith(" ") || text.endsWith(" ")) {
            throw new IllegalArgumentException("Text starts or ends with space.");
        }
        if (text.contains("  ")) {
            throw new IllegalArgumentException("Text contains substrings of 2 or more spaces.");
        }
        this.text = text;
        revalidate();
        repaint();
    }

    public int getPreferredWidthLimit() {
        return prefWidthLimit;
    }

    public void setPreferredWidthLimit(int prefWidthLimit) {
        if (prefWidthLimit < 1) {
            throw new IllegalArgumentException();
        }
        this.prefWidthLimit = prefWidthLimit;
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 20;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 20;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}