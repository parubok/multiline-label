package org.swingk;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Objects;

/**
 * Text label capable of word wrapping.
 */
public class MultiLineLabel extends JComponent {
    private String text = "";

    public static final int DEFAULT_WIDTH_LIMIT = 500; //in pixels; relevant when wrapping text without "\n";

    private int widthLimit = DEFAULT_WIDTH_LIMIT;

    public MultiLineLabel() {
        super();
        LookAndFeel.installColorsAndFont(this, "Label.background", "Label.foreground", "Label.font");
    }

    public MultiLineLabel(String text) {
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

    /**
     * Adds the specified {@link Insets} to the {@link Dimension} object and returns the new {@link Dimension}.
     */
    private static Dimension addInsets(Dimension size, Insets insets) {
        return new Dimension(size.width + insets.left + insets.right, size.height + insets.top + insets.bottom);
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
                MultiLineLabelUtils.NextLine nextLine;
                int index = 0;
                int widthLimit = getWidth() - insets.right - insets.left;
                do {
                    nextLine = MultiLineLabelUtils.getNextLine(text, index, fm, widthLimit);
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
    public Dimension getPreferredSize() {
        // https://stackoverflow.com/questions/39455573/how-to-set-fixed-width-but-dynamic-height-on-jtextpane/39466255#39466255
        FontMetrics fm = getFontMetrics(getFont());
        Insets insets = getInsets();
        Dimension textPrefSize;
        if (!text.isEmpty()) {
            MultiLineLabelUtils.NextLine nextLine;
            int index = 0;
            int widthLimit = this.widthLimit - insets.right - insets.left;
            int lineCount = 0;
            int maxWidth = 0;
            do {
                nextLine = MultiLineLabelUtils.getNextLine(text, index, fm, widthLimit);
                int width = SwingUtilities.computeStringWidth(fm, text.substring(nextLine.lineStartIndex, nextLine.lineEndIndex));
                maxWidth = Math.max(maxWidth, width);
                lineCount++;
                index = nextLine.nextLineStartIndex;
            } while (!nextLine.lastLine);
            textPrefSize = new Dimension(maxWidth, (fm.getAscent() + fm.getDescent()) * lineCount + fm.getLeading() * (lineCount - 1));
        } else {
            textPrefSize = new Dimension(0, 0);
        }
        return addInsets(textPrefSize, insets);
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

    public int getWidthLimit() {
        return widthLimit;
    }

    public void setWidthLimit(int widthLimit) {
        if (widthLimit < 1) {
            throw new IllegalArgumentException();
        }
        this.widthLimit = widthLimit;
        revalidate();
        repaint();
    }
}
