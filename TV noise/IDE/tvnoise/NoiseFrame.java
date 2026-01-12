package tvnoise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class NoiseFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private boolean fullscreen = true;
    private final GraphicsDevice device;
    private final WhiteNoiseAudio audio;

    public NoiseFrame() {
        setTitle("TV noise");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        device = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        NoisePanel panel = new NoisePanel();
        add(panel);

        audio = new WhiteNoiseAudio();
        audio.start();

        setupKeyBindings(panel);

        setFullscreen();
        setVisible(true);
    }

    private void setupKeyBindings(JComponent component) {
        InputMap im = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = component.getActionMap();

        // ESC – wyjście
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
        am.put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                audio.stop();
                System.exit(0);
            }
        });

        // F11 – fullscreen toggle
        im.put(KeyStroke.getKeyStroke("F"), "toggleFullscreen");
        am.put("toggleFullscreen", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleFullscreen();
            }
        });
    }

    private void setFullscreen() {
        dispose();
        setUndecorated(true);
        device.setFullScreenWindow(this);
        setVisible(true);
        fullscreen = true;
    }

    private void setWindowed() {
        device.setFullScreenWindow(null);
        dispose();
        setUndecorated(false);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        fullscreen = false;
    }

    private void toggleFullscreen() {
        if (fullscreen) {
            setWindowed();
        } else {
            setFullscreen();
        }
    }
}
