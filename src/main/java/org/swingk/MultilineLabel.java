package org.swingk;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.Scrollable;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Objects;

/**
 * Text label capable of presenting multiline text.
 *
 * TODO: interline distance coefficient
 * TODO: extend 1st line
 * TODO: JavaDoc
 * TODO: README with examples and screenshots
 * TODO: specify width limit in characters
 * TODO: refactor to improve performance
 * TODO: fix AA to be as in L&F (UI delegate?)
 * TODO: javax.swing.text.Utilities.getBreakLocation ?
 * TODO: lines limit, "..." to display very long messages
 * TODO: 'flexible' pref. width (e.g. +/-100 pixels) to achieve the best distribution.
 */
public class MultilineLabel extends JComponent implements Scrollable {
    /**
     * Default label width limit in pixels.
     */
    public static final int DEFAULT_WIDTH_LIMIT = 500;

    private String text = "";
    private TextLayout textLayout;
    private int prefWidthLimit = DEFAULT_WIDTH_LIMIT;
    private boolean useCurrentWidthForPreferredSize = true;

    public MultilineLabel() {
        super();
        setOpaque(true);
        LookAndFeel.installColorsAndFont(this, "Label.background", "Label.foreground", "Label.font");
        setText("");
    }

    public MultilineLabel(String text) {
        this();
        setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // ???
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        g.setColor(getForeground());
        g.setFont(getFont());
        textLayout.paintText(g);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        textLayout.preSetBounds(x, y, width, height);
        super.setBounds(x, y, width, height);
    }

    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }
        return textLayout.calculatePreferredSize();
    }

    public String getText() {
        return text;
    }

    protected TextLayout createTextLayout(String text) {
        return ProvidedTextLayout.hasLineSeparators(text) ? new ProvidedTextLayout(this) : new WidthTextLayout(this);
    }

    public void setText(String text) {
        String oldValue = this.text;
        this.text = Objects.requireNonNull(text);
        this.textLayout = createTextLayout(text);
        revalidate();
        repaint();
        firePropertyChange("text", oldValue, this.text);
    }

    protected TextLayout getTextLayout() {
        return textLayout;
    }

    /**
     * Note: Ignored if the text already contains line separators.
     *
     * @see #DEFAULT_WIDTH_LIMIT
     */
    public int getPreferredWidthLimit() {
        return prefWidthLimit;
    }

    /**
     * Note: Ignored if the text already contains line separators.
     */
    public void setPreferredWidthLimit(int prefWidthLimit) {
        if (prefWidthLimit < 1) {
            throw new IllegalArgumentException();
        }
        this.prefWidthLimit = prefWidthLimit;
        revalidate();
        repaint();
    }

    public boolean isUseCurrentWidthForPreferredSize() {
        return useCurrentWidthForPreferredSize;
    }

    /**
     * In some cases using current width to calculate the preferred size may produce undesired results.
     * This parameter allows to disable this behavior.
     *
     * @param useCurrentWidthForPreferredSize
     */
    public void setUseCurrentWidthForPreferredSize(boolean useCurrentWidthForPreferredSize) {
        this.useCurrentWidthForPreferredSize = useCurrentWidthForPreferredSize;
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
