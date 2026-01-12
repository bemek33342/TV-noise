package tvnoise;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class NoisePanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
    private final Thread thread;
    private final Random random = new Random();

    public NoisePanel() {
        setDoubleBuffered(true);
        thread = new Thread(this, "NoiseRenderer");
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            generateNoise();
            repaint();

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException ignored) {}
        }
    }

    private void generateNoise() {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) return;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = random.nextInt(256);
                int rgb = (gray << 16) | (gray << 8) | gray;
                image.setRGB(x, y, rgb);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }
}
