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

public class MikuRun extends RandomRun {
    private Thread moveThread;

    private final List<BufferedImage> frames;
    private int currentFrame = 0;

    public MikuRun() {
        super(Main.class.getResource("/images/miku/miku1.gif"), Main.class.getResource("/audio/miku/miku1.wav"), 275, 397);
        gifFps = 16;

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        // set de l'image random
        switch (rand.nextInt(2)) {
            case 0 -> {
                imageURL = Main.class.getResource("/images/miku/miku1.gif");
                imageWitdh = 275;
                imageHeight = 397;
            }
            case 1 -> {
                imageURL = Main.class.getResource("/images/miku/miku2.gif");
                imageWitdh = 219;
                imageHeight = 375;
            }
        }

        //Chargement des frames du gif
        try {
            frames = loadGifFrames(imageURL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // set du son random
        switch (rand.nextInt(3)) {
            case 0 -> soundURL = Main.class.getResource("/audio/miku/miku1.wav");
            case 1 -> soundURL = Main.class.getResource("/audio/miku/miku2.wav");
            case 2 -> soundURL = Main.class.getResource("/audio/miku/miku3.wav");
        }


        // init l'image taille et position random, opacité faible, apparaît petit à petit et son aussi
        float ratio = (float) imageHeight / imageWitdh;

        int width = rand.nextInt(100, 150);
        int height = Math.round(width * ratio);

        int x = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().width - width);
        int y = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().height - height);
        init(width, height, x, y);

        setFocusableWindowState(false);
        setVisible(true);
        setFocusableWindowState(true);

        moveThread();

        System.out.println("Miku started");
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

        // update du gif
        int delayFPS = 1000 / gifFps;
        Timer timer = new Timer(delayFPS, e -> {
            currentFrame = (currentFrame + 1) % frames.size();
            repaint();
        });
        timer.start();

        // Lancement du son
        playSound();

        // apparition de la fenêtre
        //moveThread(); <-- Décalé dans le constructeur pour éviter le getLocation qui ne fonctionne pas

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
        if (moveThread != null && moveThread.isAlive()) {
            moveThread.interrupt();
        }
    }

    private void moveThread() {
        // thread pour faire avancer l'image vers le pointeur de la souris
        moveThread = new Thread(() -> {
            double posX = getLocationOnScreen().x;
            double posY = getLocationOnScreen().y;

            while (true) {
                Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                Point windowLocation = getLocationOnScreen();

                int centerX = windowLocation.x + getWidth() / 2;
                int centerY = windowLocation.y + getHeight() / 2;

                int deltaX = mouseLocation.x - centerX;
                int deltaY = mouseLocation.y - centerY;

                double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                if (length > 10) {
                    double dirX = deltaX / length;
                    double dirY = deltaY / length;

                    double speed = 2.0;
                    posX += dirX * speed;
                    posY += dirY * speed;

                    setLocation((int) posX, (int) posY);
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
            }

        });

        moveThread.start();
    }
}
