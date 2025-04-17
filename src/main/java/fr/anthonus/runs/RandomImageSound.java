package fr.anthonus.runs;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RandomImageSound extends JFrame {
    private Clip clip;
    private Thread volumeThread;
    private Thread opacityThread;

    private final String imagePath;
    private final String soundPath;

    private JLabel imageLabel;

    public RandomImageSound(String imagePath, String soundPath) {
        this.imagePath = imagePath;
        this.soundPath = soundPath;

        setUndecorated(true);
        setAlwaysOnTop(false);
        setType(Window.Type.UTILITY);

        setBackground(new Color(0, 0, 0, 0));


        Random rand = new Random();
        int size = rand.nextInt(100, 200);
        setSize(size, size);

        int x = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().width - 200);
        int y = rand.nextInt(Toolkit.getDefaultToolkit().getScreenSize().height - 200);
        setLocation(x, y);

        init(size);
        playSound();

        setVisible(true);
    }

    private void init(int size) {
        ImageIcon image = new ImageIcon(getClass().getResource(imagePath));
        image.setImage(image.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));

        imageLabel = new JLabel(image);
        setOpacity(0.0f);
        add(imageLabel);

        opacityThread = new Thread(() -> {
            float opacity = 0.0f;
            while (opacity < 1.0f) {
                try {
                    opacity += 0.0005f;
                    if (opacity > 1.0f) opacity = 1.0f;
                    setOpacity(opacity);
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        opacityThread.start();

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                stopSound();
                dispose();
            }
        });
    }

    private void playSound() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(soundPath));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volumeControl.getMinimum());

            volumeThread = new Thread(() -> {
                try {
                    float volume = volumeControl.getMinimum();
                    while (volume < volumeControl.getMaximum()) {
                        volume += 0.05f;
                        volumeControl.setValue(volume);
                        Thread.sleep(50);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            volumeThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopSound() {
        if (volumeThread != null && volumeThread.isAlive())
            volumeThread.interrupt();

        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

}
