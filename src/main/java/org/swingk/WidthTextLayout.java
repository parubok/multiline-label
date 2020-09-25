package org.swingk;

import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Objects;

import static org.swingk.MultilineLabelUtils.paintTextInDisabledStyle;

/**
 * Dynamically calculates line breaks based on value of {@link MultilineLabel#getPreferredScrollableViewportSize()}
 * or the current label width. Ignores line breaks in text by replacing them with spaces.
 */
public class WidthTextLayout implements TextLayout {

    protected static String toRenderedText(String text) {
        StringBuilder sb = new StringBuilder(text.replace('\n', ' ').trim());
        int doubleSpaceIndex;
        while ((doubleSpaceIndex = sb.indexOf("  ")) > -1) {
            sb.delete(doubleSpaceIndex + 1, doubleSpaceIndex + 2); // delete second space
        }
        return sb.toString();
    }

    protected final MultilineLabel label;
    protected final String textToRender;

    public WidthTextLayout(MultilineLabel label) {
        this.label = Objects.requireNonNull(label);
        this.textToRender = toRenderedText(label.getText());
    }

    @Override
    public Dimension calculatePreferredSize() {
        return calcPreferredSize(0);
    }

    protected void requestLayout() {
        SwingUtilities.invokeLater(() -> {
            label.revalidate();
            label.repaint();
        });
    }

    @Override
    public void preSetBounds(int x, int y, int width, int height) {
        if (label.isUseCurrentWidthForPreferredSize()
                && !textToRender.isEmpty()
                && width > 0 && height > 0
                && width != label.getWidth()
                && calcPreferredSize(width).height != height) {
            requestLayout();
        }
    }

    protected Dimension calcPreferredSize(int expectedLabelWidth) {
        final int wLimit;
        if (expectedLabelWidth > 0) {
            wLimit = expectedLabelWidth;
        } else if (label.isUseCurrentWidthForPreferredSize() && label.getWidth() > 0) {
            // https://stackoverflow.com/questions/39455573/how-to-set-fixed-width-but-dynamic-height-on-jtextpane/39466255#39466255
            wLimit = label.getWidth();
        } else {
            wLimit = label.getPreferredWidthLimit();
        }
        final FontMetrics fm = label.getFontMetrics(label.getFont());
        final Insets insets = label.getInsets();
        return MultilineLabelUtils.calcComponentPreferredSizeForWidthLimit(insets, fm, textToRender, wLimit);
    }

    @Override
    public void paintText(Graphics g) {
        if (textToRender.isEmpty()) {
            return;
        }
        final Insets insets = label.getInsets();
        final int widthLimit = label.getWidth() - insets.right - insets.left;
        if (widthLimit < 1) {
            return;
        }
        final FontMetrics fm = g.getFontMetrics();
        final int x = insets.left;
        int y = insets.top + fm.getAscent();
        final boolean enabled = label.isEnabled();
        MultilineLabelUtils.NextLine nextLine;
        int index = 0;
        do {
            nextLine = MultilineLabelUtils.getNextLine(textToRender, index, fm, widthLimit);
            String lineStr = textToRender.substring(nextLine.lineStartIndex, nextLine.lineEndIndex + 1);
            if (enabled) {
                g.drawString(lineStr, x, y);
            } else {
                paintTextInDisabledStyle(lineStr, g, label.getBackground(), x, y);
            }
            y += fm.getHeight();
            index = nextLine.nextLineStartIndex;
        } while (!nextLine.lastLine);
    }
}
