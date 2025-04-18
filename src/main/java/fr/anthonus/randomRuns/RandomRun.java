package fr.anthonus.randomRuns;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public abstract class RandomRun extends JFrame {
    protected final Clip clip;
    protected JLabel imageLabel;

    protected final URL imageURL;
    protected final URL soundURL;

    protected final float maxVolume = -20.0f;


    protected RandomRun(URL imageURL, URL soundURL) {
        // set des paths
        this.imageURL = imageURL;
        this.soundURL = soundURL;

        // set des parametres de la fenetre par défauts
        setUndecorated(true);
        setAlwaysOnTop(true);
        setType(Window.Type.UTILITY);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // background transparent
        setBackground(new Color(0, 0, 0, 0));

        // création du clip
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void init(int width, int heigth, int x, int y);

    protected abstract void playSound();

    protected abstract void stop();

}
