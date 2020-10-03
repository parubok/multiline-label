package org.swingk.multiline;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.Scrollable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Objects;

import static org.swingk.multiline.MultilineUtils.toDimension;

/**
 * Component to display plain text. Supports multiline text.
 * <p>
 * Operates in 2 modes:
 * <ol>
 *     <li>When the label text doesn't contain line separators, then the label dynamically decides how to break it
 *     into multiple lines according to available space.</li>
 *     <li>When the label text contains line separators, it is broken into lines according to them.</li>
 * </ol>
 * </p>
 */
public class MultilineLabel extends JComponent implements Scrollable {
    /**
     * Default label width limit in pixels.
     */
    public static final int DEFAULT_WIDTH_LIMIT = 500;

    /**
     * @param insets Insets to include in the calculation. Not null.
     * @param fm {@link FontMetrics} to calculate text size. Not null.
     * @param text Text to calculate preferred size for. Not null.
     * @param wLimit Width limit in pixels (incl. insets). Greater than 0. Applicable only if the text doesn't contain EOL.
     * @return Preferred size of text bounds.
     */
    public static Dimension calculatePreferredSize(Insets insets, FontMetrics fm, String text, int wLimit) {
        return ProvidedTextLayout.hasLines(text) ? ProvidedTextLayout.calcPreferredSize(null, text, fm, insets) :
                WidthTextLayout.calcPreferredSize(null, insets, fm, text, wLimit);
    }

    /**
     * @param g Graphics to paint text on. Must be preconfigured with font, color and AA hints. Not null.
     * @param text Text to paint. Not null.
     * @param insets Insets. Not null.
     * @param wLimit Width limit in pixels (incl. insets). Greater than 0. Applicable only if the text doesn't contain EOL.
     * @param enabled If false - paint disabled text.
     * @param background Background color of the target component. Used to paint disabled text. Not null.
     */
    public static void paintText(Graphics g, String text, Insets insets, int wLimit, boolean enabled, Color background) {
        if (ProvidedTextLayout.hasLines(text)) {
            ProvidedTextLayout.paintText(null, g, text, insets, enabled, background);
        } else {
            WidthTextLayout.paintText(null, g, text, insets, wLimit, enabled, background);
        }
    }

    private String text = "";
    private TextLayout textLayout; // not null after constructor
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
        setUI(new MultilineLabelUI());
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
        return toDimension(0, 0, getInsets());
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
     * adjacent spaces may collapsed into one space, text may trimmed, EOL may be inserted, etc.
     * Fires change event for property "text".
     */
    public void setText(String text) {
        String oldValue = this.text;
        this.text = Objects.requireNonNull(text);
        this.textLayout = createTextLayout();
        firePropertyChange("text", oldValue, this.text);
        revalidate();
        repaint();
    }

    /**
     * @return True is the label displays its text according to the preferred/current width, false if the line breaks
     * are predefined by line separators in the text and the label width is ignored.
     * @see #setPreferredWidthLimit(int)
     */
    public boolean isWidthBasedLayout() {
        return textLayout instanceof WidthTextLayout;
    }

    protected TextLayout createTextLayout() {
        return ProvidedTextLayout.hasLines(getText()) ? new ProvidedTextLayout(this) : new WidthTextLayout(this);
    }

    protected TextLayout getTextLayout() {
        return textLayout;
    }

    /**
     * Note: This property is ignored if the text contains line separators.
     *
     * @see #DEFAULT_WIDTH_LIMIT
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
     * @param useCurrentWidthForPreferredSize If true, the label will try to layout its text according to the current
     * width, if false - the current width will be ignored by the text layout.
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
