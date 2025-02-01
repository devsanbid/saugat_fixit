package util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class ImageUtils {
    public static BufferedImage blurAndScaleImage(BufferedImage source, int radius, double scale) {
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }
        if (scale <= 1.0) {
            throw new IllegalArgumentException("Scale must be > 1.0 for a pop-up effect");
        }

        // Create Gaussian blur
        float[] kernel = createGaussianKernel(radius);
        int kernelSize = 2 * radius + 1;
        BufferedImageOp blurOp = new ConvolveOp(new Kernel(kernelSize, kernelSize, kernel), ConvolveOp.EDGE_NO_OP, null);

        // Blur the image
        BufferedImage blurred = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        blurOp.filter(source, blurred);

        // Scale the blurred image
        int newWidth = (int) (source.getWidth() * scale);
        int newHeight = (int) (source.getHeight() * scale);
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, source.getType());
        Graphics2D g2 = scaledImage.createGraphics();

        // Enable high-quality rendering
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw scaled image
        g2.drawImage(blurred, 0, 0, newWidth, newHeight, null);
        g2.dispose();

        return scaledImage;
    }

    private static float[] createGaussianKernel(int radius) {
        int size = 2 * radius + 1;
        float[] data = new float[size * size];
        float sigma = radius / 3f; // Standard deviation
        float mean = radius; // Center of the kernel
        float sum = 0;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                float value = (float) (Math.exp(
                        -0.5 * (Math.pow((x - mean) / sigma, 2.0) + Math.pow((y - mean) / sigma, 2.0))
                ) / (2 * Math.PI * sigma * sigma));
                data[y * size + x] = value;
                sum += value;
            }
        }

        // Normalize the kernel to ensure the sum equals 1
        for (int i = 0; i < data.length; i++) {
            data[i] /= sum;
        }

        return data;
    }
}