package swing;

import util.ShadowRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JLayeredPane;

public class PanelBorder extends JLayeredPane {

    private int shadowSize = 10;
    private float shadowOpacity = 0.5f;
    private Color shadowColor = new Color(0, 0, 0, 80);
    private BufferedImage shadowImage;

    public PanelBorder() {
        setOpaque(false); // Ensure transparency
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;

        // Enable anti-aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Generate shadow image if needed
        if (shadowImage == null) {
            createShadowImage();
        }

        // Draw shadow
        if (shadowImage != null) {
            g2.drawImage(shadowImage, 0, 0, null);
        }

        // Draw panel content
        g2.setColor(getBackground());
        g2.fillRect(shadowSize, shadowSize, getWidth() - shadowSize * 2, getHeight() - shadowSize * 2);
    }

    private void createShadowImage() {
        int width = getWidth();
        int height = getHeight();

        if (width > 0 && height > 0) {
            BufferedImage panelImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = panelImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLACK);
            g2.fillRect(shadowSize, shadowSize, width - shadowSize * 2, height - shadowSize * 2);
            g2.dispose();

            // Create shadow using ShadowRenderer
            ShadowRenderer shadowRenderer = new ShadowRenderer(shadowSize, shadowOpacity, shadowColor);
            shadowImage = shadowRenderer.createShadow(panelImage);
        }
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        shadowImage = null; // Regenerate shadow when resized
        repaint();
    }

    // Getter and setter for shadow properties
    public int getShadowSize() {
        return shadowSize;
    }

    public void setShadowSize(int shadowSize) {
        this.shadowSize = shadowSize;
        shadowImage = null;
        repaint();
    }

    public float getShadowOpacity() {
        return shadowOpacity;
    }

    public void setShadowOpacity(float shadowOpacity) {
        this.shadowOpacity = shadowOpacity;
        shadowImage = null;
        repaint();
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        shadowImage = null;
        repaint();
    }
}
