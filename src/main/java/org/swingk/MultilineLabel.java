package org.swingk;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.Scrollable;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Objects;

/**
 * Text label capable of word wrapping.
 *
 * TODO: interline distance coefficient
 * TODO: add spaces to 1st line
 * TODO: support text with "\n"
 * TODO: "text" property
 * TODO: JavaDoc
 * TODO: README
 * TODO: specify width limit in characters
 * TODO: refactor to improve performance
 * TODO: fix AA to be as in L&F (UI delegate?)
 * TODO: javax.swing.text.Utilities.getBreakLocation ?
 */
public class MultilineLabel extends JComponent implements Scrollable {
    private String text = "";
    private TextLayout textLayout;

    /**
     * Default label width limit in pixels.
     */
    public static final int DEFAULT_WIDTH_LIMIT = 500;

    private int prefWidthLimit = DEFAULT_WIDTH_LIMIT;

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

    public void setText(String text) {
        String oldValue = this.text;
        this.text = Objects.requireNonNull(text);
        this.textLayout = new WidthTextLayout(this);
        firePropertyChange("text", oldValue, this.text);
    }

    public int getPreferredWidthLimit() {
        return prefWidthLimit;
    }

    public void setPreferredWidthLimit(int prefWidthLimit) {
        if (prefWidthLimit < 1) {
            throw new IllegalArgumentException();
        }
        this.prefWidthLimit = prefWidthLimit;
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
