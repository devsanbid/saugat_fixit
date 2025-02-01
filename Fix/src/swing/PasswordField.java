package swing;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicPasswordFieldUI;
import util.ShadowRenderer;

public class PasswordField extends JPasswordField {
    private int round = 50; // Corner roundness
    private static final Color DEFAULT_SHADOW_COLOR = new Color(255, 192, 203); // Pink shadow
    private static final Insets DEFAULT_SHADOW_SIZE = new Insets(2, 5, 8, 5);

    private final Color shadowColor = DEFAULT_SHADOW_COLOR;
    private BufferedImage imageShadow;

    private boolean hide = true; // For toggling password visibility
    private final Image eye; // Eye icon for show
    private final Image eyeHide; // Eye icon for hide
    private String placeholder = "Enter your password"; // Default placeholder

    public PasswordField() {
        setUI(new CustomPasswordFieldUI());
        setOpaque(false);
        setForeground(new Color(80, 80, 80));
        setSelectedTextColor(Color.WHITE);
        setSelectionColor(new Color(133, 209, 255));
        setBorder(new EmptyBorder(10, 12, 15, 12));
        setBackground(Color.WHITE);

        eye = new ImageIcon(getClass().getResource("/Image/eye.png")).getImage(); // Path to the eye icon
        eyeHide = new ImageIcon(getClass().getResource("/Image/eye_hide.png")).getImage(); // Path to the hidden eye icon

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                int x = getWidth() - 50; // Adjusted for left positioning
                if (new java.awt.Rectangle(x, 0, 30, 30).contains(me.getPoint())) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand when over the icon
                    setToolTipText(hide ? "Show Password" : "Hide Password"); // Set tooltip based on state
                } else {
                    setCursor(new Cursor(Cursor.TEXT_CURSOR)); // Reset cursor
                    setToolTipText(null); // Remove tooltip when not hovering over the icon
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                int x = getWidth() - 40; // Adjusted for left positioning
                if (new java.awt.Rectangle(x, 0, 30, 30).contains(me.getPoint())) {
                    hide = !hide; // Toggle visibility
                    repaint();
                }
            }
        });

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint(); // Repaint to reflect the updated placeholder
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double width = getWidth() - (DEFAULT_SHADOW_SIZE.left + DEFAULT_SHADOW_SIZE.right);
        double height = getHeight() - (DEFAULT_SHADOW_SIZE.top + DEFAULT_SHADOW_SIZE.bottom);
        double x = DEFAULT_SHADOW_SIZE.left;
        double y = DEFAULT_SHADOW_SIZE.top;

        // Draw shadow
        g2.drawImage(imageShadow, 0, 0, null);

        // Create Background with Rounded Corners
        g2.setColor(getBackground());
        Area area = new Area(new RoundRectangle2D.Double(x, y, width, height, round, round));
        g2.fill(area);

        // Draw Border
        g2.setColor(Color.GRAY);
        g2.draw(new RoundRectangle2D.Double(x, y, width, height, round, round));

        // Draw placeholder text if the field is empty and unfocused
        if (getPassword().length == 0 && !isFocusOwner()) {
            g2.setColor(Color.GRAY);
            g2.drawString(placeholder, getInsets().left, getHeight() / 2 + getFont().getSize() / 2 - 2);
        }

        // Draw Password or Plain Text
        g2.setFont(getFont());
        g2.setColor(getForeground());
        String text = new String(getPassword());
        if (!hide) {
            g2.drawString(text, getInsets().left, getHeight() / 2 + getFont().getSize() / 2 - 2);
        } else {
            String echoText = "●".repeat(text.length());
            g2.drawString(echoText, getInsets().left, getHeight() / 2 + getFont().getSize() / 2 - 2);
        }

        // Draw Show/Hide Eye Icon
        int eyeX = getWidth() - 40; // Shifted 10px to the left
        int eyeY = (getHeight() - 20) / 2; // Centered vertically
        g2.drawImage(hide ? eyeHide : eye, eyeX, eyeY, null); // Draw appropriate eye icon

        g2.dispose();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        createImageShadow();
    }

    private void createImageShadow() {
        int height = getHeight();
        int width = getWidth();

        if (width > 0 && height > 0) {
            imageShadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = imageShadow.createGraphics();
            BufferedImage shadow = createShadow();
            if (shadow != null) {
                g2.drawImage(shadow, 0, 0, null);
            }
            g2.dispose();
        }
    }

    private BufferedImage createShadow() {
        int width = getWidth() - (DEFAULT_SHADOW_SIZE.left + DEFAULT_SHADOW_SIZE.right);
        int height = getHeight() - (DEFAULT_SHADOW_SIZE.top + DEFAULT_SHADOW_SIZE.bottom);

        if (width > 0 && height > 0) {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fill(new RoundRectangle2D.Double(0, 0, width, height, round, round));
            g2.dispose();
            return new ShadowRenderer(5, 0.3f, shadowColor).createShadow(img);
        }

        return null;
    }

    private class CustomPasswordFieldUI extends BasicPasswordFieldUI {
        @Override
        protected void paintBackground(Graphics grphcs) {
            // Do not paint default background
        }
    }
}
