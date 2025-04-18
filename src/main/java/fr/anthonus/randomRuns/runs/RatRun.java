package fr.anthonus.randomRuns.runs;

import fr.anthonus.Main;
import fr.anthonus.randomRuns.RandomRun;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RatRun extends RandomRun {
    private Thread changeThread; // thread pour changer la position de l'image toutes les 1 secondes

    private final List<BufferedImage> frames;
    private int currentFrame = 0;

    public RatRun() {
        super(Main.class.getResource("/images/rat-dance/rat-dance.gif"), Main.class.getResource("/audio/rat-dance/rat-dance.wav"), 310, 478);
        gifFps = 16;

        try {
            frames = loadGifFrames(imageURL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // init l'image taille et position random
        float ratio = (float) imageHeight / imageWitdh;

        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int width = rand.nextInt(100, 200);
        int height = Math.round(width * ratio);

        int x = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().width - width);
        int y = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().height - height);
        init(width, height, x, y);

        setFocusableWindowState(false);
        setVisible(true);
        setFocusableWindowState(true);

        System.out.println("Rat-dance started");
    }

    @Override
    protected void init(int width, int height, int x, int y) {
        // set de la taille et de la position
        setSize(width, height);
        setLocation(x, y);

        // set de l'image
        imageLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (!frames.isEmpty()) {
                    BufferedImage frame = frames.get(currentFrame);

                    // Calcul du ratio d'aspect
                    int originalWidth = frame.getWidth();
                    int originalHeight = frame.getHeight();
                    float aspectRatio = (float) originalWidth / originalHeight;

                    // Calcul des dimensions redimensionnées
                    int newWidth = width;
                    int newHeight = height;

                    if (width / (float) height > aspectRatio) {
                        newWidth = Math.round(height * aspectRatio);
                    } else {
                        newHeight = Math.round(width / aspectRatio);
                    }

                    // Centrage de l'image
                    int x = (width - newWidth) / 2;
                    int y = (height - newHeight) / 2;

                    // Dessin de l'image redimensionnée
                    g.drawImage(frame, x, y, newWidth, newHeight, this);
                }
            }
        };
        add(imageLabel);

        int delayFPS = 1000 / gifFps;
        Timer timer = new Timer(delayFPS, e -> {
            currentFrame = (currentFrame + 1) % frames.size();
            repaint();
        });
        timer.start();

        // Lancement du son
        playSound();

        changeThread();

        // ajout du listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                stop();
                dispose();
            }
        });
    }

    @Override
    protected void playSound() {
        // set du son
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            clip.open(audioIn);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }


        // set du volume
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(maxVolume);
    }

    @Override
    protected void stop() {
        // stop le son
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }

        // stop les Threads
        if (changeThread != null && changeThread.isAlive()) {
            changeThread.interrupt();
        }
    }

    private void changeThread() {
        // thread pour faire apparaître l'image petit à petit (avec son)
        changeThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2150);
                } catch (InterruptedException _) {}
                // change la position de l'image
                ThreadLocalRandom rand = ThreadLocalRandom.current();
                int x = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().width - getWidth());
                int y = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().height - getHeight());
                setLocation(x, y);
            }
        });

        changeThread.start();
    }
}
