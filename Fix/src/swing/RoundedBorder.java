package swing;

import java.awt.*;
import javax.swing.border.Border;

public class RoundedBorder implements Border {
    private final int radius; // Radius for rounded corners
    private final Color borderColor; // Border color
    private final int borderThickness; // Thickness of the border

    public RoundedBorder(int radius, Color borderColor, int borderThickness) {
        this.radius = radius;
        this.borderColor = borderColor;
        this.borderThickness = borderThickness;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        // Ensure consistent padding for all sides
        return new Insets(radius + borderThickness, radius + borderThickness, radius + borderThickness, radius + borderThickness);
    }

    @Override
    public boolean isBorderOpaque() {
        return false; // Allows the border to blend seamlessly with the background
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Adjust for border thickness
        int adjustedX = x + borderThickness / 2;
        int adjustedY = y + borderThickness / 2;
        int adjustedWidth = width - borderThickness;
        int adjustedHeight = height - borderThickness;

        // Set stroke with rounded caps and joins
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(borderThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Draw the rounded rectangle
        g2d.drawRoundRect(adjustedX, adjustedY, adjustedWidth - 1, adjustedHeight - 1, radius, radius);

        g2d.dispose(); // Free resources
    }
}