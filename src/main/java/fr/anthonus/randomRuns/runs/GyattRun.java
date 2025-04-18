package fr.anthonus.randomRuns.runs;

import fr.anthonus.Main;
import fr.anthonus.randomRuns.RandomRun;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GyattRun extends RandomRun {

    public GyattRun() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        super(Main.class.getResource("/images/gyatt.png"), Main.class.getResource("/audio/gyatt.wav"));

        // init l'image taille et position random, opacité très faible et son normal
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int size = rand.nextInt(100, 200);
        int x = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().width-size);
        int y = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().height-size);
        init(size, x, y);

        setFocusableWindowState(false);
        setVisible(true);
        setFocusableWindowState(true);

        System.out.println("GyattRun started");
    }

    @Override
    protected void init(int size, int x, int y) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        // set de la taille et de la position
        setSize(size, size);
        setLocation(x, y);

        // set de l'image
        ImageIcon imageIcon = new ImageIcon(imageURL);
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));

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
    protected void playSound() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        // set du son
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
        clip.open(audioIn);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);

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
