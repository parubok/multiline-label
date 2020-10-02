package org.swingk;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    public static final String LINE_SEPARATOR_UNIX = "\n";
    public static final String LINE_SEPARATOR_WIN = "\r\n";

    private static boolean applySystemAA = true;

    /**
     * @return True if the text drawing is performed by calling
     * {@link BasicGraphicsUtils#drawString(JComponent, Graphics2D, String, float, float)},
     * which applies the system anti-aliasing, otherwise the text is drawn via
     * {@link Graphics#drawString(String, int, int)}.
     */
    public static boolean isApplySystemAA() {
        return applySystemAA;
    }

    /**
     * @param applySystemAA If true, the text drawing is performed by calling
     *                      {@link BasicGraphicsUtils#drawString(JComponent, Graphics2D, String, float, float)},
     *                      which applies the system anti-aliasing, otherwise the text is drawn via
     *                      {@link Graphics#drawString(String, int, int)}.
     */
    public static void setApplySystemAA(boolean applySystemAA) {
        MultilineLabel.applySystemAA = applySystemAA;
    }

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
    public static void paintText(JComponent c, Graphics g, String text, Insets insets, int wLimit, boolean enabled, Color background) {
        if (hasLineSeparators(text)) {
            ProvidedTextLayout.paintText(c, g, text, insets, enabled, background);
        } else {
            WidthTextLayout.paintText(c, g, text, insets, wLimit, enabled, background);
        }
    }

    private String text = "";
    private TextLayout textLayout; // not null
    private int prefWidthLimit = DEFAULT_WIDTH_LIMIT;
    private boolean useCurrentWidthForPreferredSize = true;

    public MultilineLabel() {
        this("");
    }

    public MultilineLabel(String text) {
        super();
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(true);
        updateUI();
        setText(text);
    }

    @Override
    public void updateUI() {
        setUI(new BasicMultilineLabelUI());
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

    @Override
    public Dimension getMinimumSize() {
        if (isMinimumSizeSet()) {
            return super.getMinimumSize();
        }
        Insets insets = getInsets();
        return new Dimension(insets.left + insets.right, insets.top + insets.bottom);
    }

    @Override
    public Dimension getMaximumSize() {
        if (isMaximumSizeSet()) {
            return super.getMaximumSize();
        }
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
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
     * See https://stackoverflow.com/questions/39455573/how-to-set-fixed-width-but-dynamic-height-on-jtextpane/39466255#39466255
     *
     * @param useCurrentWidthForPreferredSize If true, the label will try to layout its text according to the current width,
     *                                        if false - the current width will be ignored by the text layout.
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
