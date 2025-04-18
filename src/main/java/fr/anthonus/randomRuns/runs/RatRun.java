package fr.anthonus.randomRuns.runs;

import fr.anthonus.Main;
import fr.anthonus.randomRuns.RandomRun;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class RatRun extends RandomRun {
    private Thread changeThread; // thread pour changer la position de l'image toutes les 1 secondes

    public RatRun() {
        super(Main.class.getResource("/images/rat-dance.gif"), Main.class.getResource("/audio/rat-dance.wav"));

        // init l'image taille et position random
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int baseWidth = 310;
        int baseHeight = 478;
        float ratio = (float) baseHeight / baseWidth;

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
        ImageIcon imageIcon = new ImageIcon(imageURL);
        Image scaledImage = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);

        imageLabel = new JLabel(resizedIcon);
        add(imageLabel);


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
