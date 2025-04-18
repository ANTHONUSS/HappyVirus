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

public class GyattRun extends RandomRun {

    public GyattRun() {
        super(Main.class.getResource("/images/gyatt.png"), Main.class.getResource("/audio/gyatt.wav"), 318, 360);

        // init l'image taille et position random, opacité très faible et son normal
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

        System.out.println("GyattRun started");
    }

    @Override
    protected void init(int width, int heigth, int x, int y) {
        // set de la taille et de la position
        setSize(width, heigth);
        setLocation(x, y);

        // set de l'image
        ImageIcon imageIcon = new ImageIcon(imageURL);
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(width, heigth, Image.SCALE_DEFAULT));

        imageLabel = new JLabel(imageIcon);
        setOpacity(0.05f);
        add(imageLabel);

        // Lancement du son
        playSound();

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
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
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
    }
}
