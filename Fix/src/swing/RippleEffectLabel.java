package swing;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class RippleEffectLabel extends JLabel {
    private boolean isRippleVisible = false;
    private int rippleRadius = 0;

    public RippleEffectLabel(String text) {
        super(text);
        setOpaque(false);  // Make label transparent for effect visibility.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isRippleVisible = true;
                rippleRadius = 0;
                new Timer(5, (ActionEvent e1) -> {
                    if (isRippleVisible) {
                        rippleRadius += 10;
                        repaint();
                        if (rippleRadius > Math.max(getWidth(), getHeight())) {
                            isRippleVisible = false;
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isRippleVisible) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(0, 0, 0, 50)); // Ripple color with transparency
            g2d.fillOval(getWidth() / 2 - rippleRadius / 2, getHeight() / 2 - rippleRadius / 2, rippleRadius, rippleRadius);
        }
    }
}
