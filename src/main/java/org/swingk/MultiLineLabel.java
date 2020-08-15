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

    private int prefWidthLimit = DEFAULT_WIDTH_LIMIT;

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
        Insets insets = getInsets();
        final int horInsets = insets.right + insets.left;
        Dimension textPrefSize;
        int textPrefWidth;
        int textPrefHeight;
        if (!text.isEmpty()) {
            FontMetrics fm = getFontMetrics(getFont());
            final int labelWidth = getWidth();
            // https://stackoverflow.com/questions/39455573/how-to-set-fixed-width-but-dynamic-height-on-jtextpane/39466255#39466255
            MultiLineLabelUtils.NextLine nextLine;
            int index = 0;
            final int textWidthLimit = Math.max((labelWidth > 0 ? labelWidth : prefWidthLimit) - horInsets, 1);
            int lineCount = 0;
            int maxWidth = 0;
            do {
                nextLine = MultiLineLabelUtils.getNextLine(text, index, fm, textWidthLimit);
                String nextLineStr = text.substring(nextLine.lineStartIndex, nextLine.lineEndIndex);
                int nextLineWidth = SwingUtilities.computeStringWidth(fm, nextLineStr);
                maxWidth = Math.max(maxWidth, nextLineWidth);
                lineCount++;
                index = nextLine.nextLineStartIndex;
            } while (!nextLine.lastLine);
            textPrefWidth = maxWidth;
            textPrefHeight = (fm.getAscent() + fm.getDescent()) * lineCount + fm.getLeading() * (lineCount - 1);
        } else {
            textPrefWidth = textPrefHeight = 0;
        }
        return new Dimension(textPrefWidth + horInsets, textPrefHeight + insets.top + insets.bottom);
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
}
