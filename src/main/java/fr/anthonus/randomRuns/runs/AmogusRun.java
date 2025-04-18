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

public class AmogusRun extends RandomRun {
    private Thread appearThread;
    private int position = 0;

    public AmogusRun() {
        super(Main.class.getResource("/images/amogus/amogus.png"), Main.class.getResource("/audio/amogus/amogus.wav"), 1525, 2049);

        // init l'image taille et position random, opacité faible, apparaît petit à petit et son aussi
        float ratio = (float) imageHeight / imageWitdh;

        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int width = rand.nextInt(100, 150);
        int height = Math.round(width * ratio);

        int x = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().width - width);
        int y = Toolkit.getDefaultToolkit().getScreenSize().height;
        init(width, height, x, y);

        setFocusableWindowState(false);
        setVisible(true);
        setFocusableWindowState(true);

        System.out.println("Amogus started");
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
        add(imageLabel);

        // Lancement du son
        playSound();

        // apparition de la fenêtre
        appearThread(heigth);

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
        if (appearThread != null && appearThread.isAlive()) {
            appearThread.interrupt();
        }
    }

    private void appearThread(int heigth) {
        // thread pour faire apparaître l'image petit à petit (avec son)
        appearThread = new Thread(() -> {

            while (position < heigth-20) {

                position += 1;
                setLocation(getX(), getY() - 1);

                try {
                    Thread.sleep(200);
                } catch (InterruptedException _) {}
            }
        });

        appearThread.start();
    }
}
