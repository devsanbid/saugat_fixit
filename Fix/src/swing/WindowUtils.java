package swing;

import javax.swing.*;
import java.awt.*;

public class WindowUtils {

    public static void setTransparentTitleBar(JFrame frame, ImageIcon icon) {
        // Set undecorated to true for custom title bar
        frame.setUndecorated(true);

        // Create a JPanel for the custom title bar
        JPanel titleBar = new JPanel();
        titleBar.setLayout(new BorderLayout());
        titleBar.setBackground(new Color(0, 0, 0, 0)); // Transparent background

        // Create a label for the icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Create a label for the title
        JLabel titleLabel = new JLabel("Application Title");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);

        // Add icon and title to the left side of the title bar
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        leftPanel.setOpaque(false);
        leftPanel.add(iconLabel);
        leftPanel.add(titleLabel);

        // Create minimize, maximize, and close buttons
        JButton minimizeButton = createTitleBarButton("-");
        JButton closeButton = createTitleBarButton("x");

        // Add functionality to the buttons
        minimizeButton.addActionListener(e -> frame.setState(Frame.ICONIFIED));
        closeButton.addActionListener(e -> System.exit(0));

        // Add buttons to the right side of the title bar
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        rightPanel.setOpaque(false);
        rightPanel.add(minimizeButton);
        rightPanel.add(closeButton);

        // Add left and right panels to the title bar
        titleBar.add(leftPanel, BorderLayout.WEST);
        titleBar.add(rightPanel, BorderLayout.EAST);

        // Add mouse listeners for dragging functionality
        final Point dragPoint = new Point();
        titleBar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dragPoint.x = evt.getX();
                dragPoint.y = evt.getY();
            }
        });
        titleBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                Point frameLocation = frame.getLocation();
                frame.setLocation(frameLocation.x + evt.getX() - dragPoint.x, frameLocation.y + evt.getY() - dragPoint.y);
            }
        });

        // Set the custom title bar to the frame
        frame.getContentPane().add(titleBar, BorderLayout.NORTH);
    }

    private static JButton createTitleBarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(60, 63, 65));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }
}
