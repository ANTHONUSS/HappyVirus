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

public class GokuRun extends RandomRun {
    private Thread appearThread;
    private int changeCount = 0;
    private int maxChangeCount;

    public GokuRun() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        super(Main.class.getResource("/images/goku.png"), Main.class.getResource("/audio/goku/goku.wav"));

        // init l'image taille et position random, opacité faible, apparaît petit à petit et son aussi
        Random rand = new Random();
        int size = rand.nextInt(100, 200);
        int x = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().width - size);
        int y = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().height - size);
        init(size, x, y);

        // set du maximum de changements de fenêtre
        maxChangeCount = rand.nextInt(5, 10);

        setVisible(true);

        System.out.println("Goku started");
    }

    @Override
    protected void init(int size, int x, int y) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // set de la taille et de la position
        setSize(size, size);
        setLocation(x, y);

        // set de l'image
        ImageIcon imageIcon = new ImageIcon(imageURL);
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));

        imageLabel = new JLabel(imageIcon);
        setOpacity(0.0f);
        add(imageLabel);

        // Lancement du son
        playSound();

        // apparition de la fenêtre
        appearThread();

        // ajout du listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                stop();
                dispose();
            }

            //Changement position au survol de la souris
            @Override
            public void mouseEntered(MouseEvent e) {
                if (changeCount < maxChangeCount) {
                    try {
                        AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource("/audio/goku/gokuTP.wav"));
                        Clip secondClip = AudioSystem.getClip();
                        secondClip.open(audioIn);
                        secondClip.start();

                        FloatControl volumeControl = (FloatControl) secondClip.getControl(FloatControl.Type.MASTER_GAIN);
                        volumeControl.setValue(-35.0f); // volume à -35 dB
                    } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
                        ex.printStackTrace();
                    }

                    Random rand = new Random();
                    int x = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().width - size);
                    int y = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().height - size);
                    setLocation(x, y);
                    changeCount++;
                }
            }
        });
    }

    @Override
    protected void playSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // set du son
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
        clip.open(audioIn);
        clip.setMicrosecondPosition(53_000_000L); // set de la position de début à 53 secondes
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);


        // set du volume
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(volumeControl.getMinimum()); // volume à 0 (-80.0f)
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

    private void appearThread() {
        // thread pour faire apparaître l'image petit à petit (avec son)
        appearThread = new Thread(() -> {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float opacity = 0.0f;
            float volume = volumeControl.getMinimum();

            float opacityStep = (1.0f - 0.0f) / 100;
            float volumeStep = (-15.0f - (-80.0f)) / 100;



            while (opacity < 1.0f || volume < -15.0f) {
                opacity += opacityStep;
                setOpacity(opacity);

                volume += volumeStep;
                volumeControl.setValue(volume);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        appearThread.start();
    }
}
