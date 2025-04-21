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

public class AreYouSureRun extends RandomRun {
    private final List<BufferedImage> frames;
    private int currentFrame = 0;

    public AreYouSureRun() {
        super(Main.class.getResource("/images/are-you-sure/are-you-sure.gif"), Main.class.getResource("/audio/are-you-sure/are-you-sure.wav"), 476, 541);
        gifFps = 24;

        try {
            frames = loadGifFrames(imageURL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // init l'image taille et position random
        float ratio = (float) imageHeight / imageWitdh;

        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int width = rand.nextInt(200, 400);
        int height = Math.round(width * ratio);

        int x = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().width - width);
        int y = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().height - height);
        init(width, height, x, y);

        setFocusableWindowState(false);
        setVisible(true);
        setFocusableWindowState(true);

        playSound();

        System.out.println("are-you-sure started");
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
        //playSound(); <-- déplacement dans le constructeur pouur syncro avec le gif

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

            long audioLength = clip.getMicrosecondLength() / 1000;

            Timer closeTimer = new Timer((int) audioLength, e -> {
                stop();
                dispose();
            });
            closeTimer.setRepeats(false);
            closeTimer.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }


        // set du volume
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(volumeControl.getMaximum()); // volume max car le son de base est assez faible
    }

    @Override
    protected void stop() {
        // stop le son
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
