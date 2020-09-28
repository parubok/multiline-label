package org.swingk;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.Scrollable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Objects;

/**
 * Component to display plain text. Supports multiline text.
 * <p>
 * Operates in 2 modes:
 * <ol>
 *     <li>When the label text doesn't contain line separators, then the label dynamically decides how to break it into multiple lines.</li>
 *     <li>When the label text contains line separators, it is broken into lines according to them.</li>
 * </ol>
 * </p>
 */
public class MultilineLabel extends JComponent implements Scrollable {
    /**
     * Default label width limit in pixels.
     */
    public static final int DEFAULT_WIDTH_LIMIT = 500;

    // EOL strings.
    static final String LINE_SEPARATOR_UNIX = "\n";
    static final String LINE_SEPARATOR_WIN = "\r\n";

    public static boolean hasLineSeparators(String text) {
        return text.contains(LINE_SEPARATOR_UNIX) || text.contains(LINE_SEPARATOR_WIN);
    }

    /**
     * @param insets Insets to include in the calculation. Not null.
     * @param fm     {@link FontMetrics} to calculate text size. Not null.
     * @param text   Text to calculate preferred size for. Not null.
     * @param wLimit Width limit in pixels (incl. insets). Greater than 0. Applicable only if the text doesn't contain EOL.
     * @return Preferred size of text bounds.
     */
    public static Dimension calculatePreferredSize(Insets insets, FontMetrics fm, String text, int wLimit) {
        return hasLineSeparators(text) ? ProvidedTextLayout.calcPreferredSize(text, fm, insets) :
                WidthTextLayout.calcPreferredSize(insets, fm, text, wLimit);
    }

    /**
     * @param g          Graphics to paint text on. Not null.
     * @param text       Text to paint. Not null.
     * @param insets     Insets. Not null.
     * @param wLimit     Width limit in pixels (incl. insets). Greater than 0. Applicable only if the text doesn't contain EOL.
     * @param enabled    If false - paint disabled text.
     * @param background Background color of the target component. Not null.
     */
    public static void paintText(Graphics g, String text, Insets insets, int wLimit, boolean enabled, Color background) {
        if (hasLineSeparators(text)) {
            ProvidedTextLayout.paintText(g, text, insets, enabled, background);
        } else {
            WidthTextLayout.paintText(g, text, insets, wLimit, enabled, background);
        }
    }

    /**
     * Draws {@code text} in a style of disabled component text at {@link Graphics} context from the point (x,y). Uses
     * {@code color} as a base.
     */
    static void paintTextInDisabledStyle(String text, Graphics g, Color color, int x, int y) {
        g.setColor(color.brighter());
        g.drawString(text, x + 1, y + 1);
        g.setColor(color.darker());
        g.drawString(text, x, y);
    }

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

    /**
     * @return Text of this label. The actually displayed text may differ from this value - multiple adjacent spaces
     * may collapsed into one space, text may trimmed, EOL may be inserted, etc.
     */
    public String getText() {
        return text;
    }

    /**
     * @param text Text of this label. Not null. The actually displayed text may differ from this value - multiple
     *             adjacent spaces may collapsed into one space, text may trimmed, EOL may be inserted, etc.
     *             Fires change event for property "text".
     */
    public void setText(String text) {
        String oldValue = this.text;
        this.text = Objects.requireNonNull(text);
        this.textLayout = createTextLayout(text);
        revalidate();
        repaint();
        firePropertyChange("text", oldValue, this.text);
    }

    /**
     * @return True is the label displays its text according to the preferred/current width, false if the line breaks
     * are predefined by line separators in the text and the label width is ignored.
     * @see #setPreferredWidthLimit(int)
     * @see #LINE_SEPARATOR_UNIX
     * @see #LINE_SEPARATOR_WIN
     */
    public boolean isWidthBasedLayout() {
        return textLayout instanceof WidthTextLayout;
    }

    protected TextLayout createTextLayout(String text) {
        return hasLineSeparators(text) ? new ProvidedTextLayout(this) : new WidthTextLayout(this);
    }

    protected TextLayout getTextLayout() {
        return textLayout;
    }

    /**
     * Note: This property is ignored if the text contains line separators.
     *
     * @see #DEFAULT_WIDTH_LIMIT
     * @see #LINE_SEPARATOR_UNIX
     * @see #LINE_SEPARATOR_WIN
     */
    public int getPreferredWidthLimit() {
        return prefWidthLimit;
    }

    /**
     * Note: This property is ignored if the text contains line separators.
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

    @Override
    protected String paramString() {
        return super.paramString()
                + ",prefWidthLimit=" + prefWidthLimit
                + ",useCurrentWidthForPreferredSize=" + useCurrentWidthForPreferredSize
                + ",textLayout=" + textLayout
                + ",text=" + text;
    }
}
