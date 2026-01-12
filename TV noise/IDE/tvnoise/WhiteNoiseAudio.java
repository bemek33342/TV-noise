package tvnoise;

import javax.sound.sampled.*;
import java.util.Random;

public class WhiteNoiseAudio implements Runnable {

    private volatile boolean running = true;
    private Thread thread;

    public void start() {
        thread = new Thread(this, "WhiteNoiseAudio");
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try {
            AudioFormat format = new AudioFormat(
                    44100, 16, 1, true, false
            );

            SourceDataLine line = AudioSystem.getSourceDataLine(format);
            line.open(format);
            line.start();

            Random random = new Random();
            byte[] buffer = new byte[4096];

            while (running) {
                for (int i = 0; i < buffer.length; i += 2) {
                    // Gaussian = bardziej analogowy szum TV
                    short sample = (short) (random.nextGaussian() * 0.4 * Short.MAX_VALUE);
                    buffer[i] = (byte) (sample & 0xff);
                    buffer[i + 1] = (byte) ((sample >> 8) & 0xff);
                }
                line.write(buffer, 0, buffer.length);
            }

            line.drain();
            line.stop();
            line.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
