package fr.anthonus.randomRuns;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public abstract class RandomRun extends JFrame {
    protected final Clip clip;
    protected JLabel imageLabel;

    protected final URL imageURL;
    protected final URL soundURL;

    protected final float maxVolume = -20.0f;


    protected RandomRun(URL imageURL, URL soundURL) throws LineUnavailableException {
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
        clip = AudioSystem.getClip();
    }

    protected abstract void init(int size, int x, int y) throws UnsupportedAudioFileException, IOException, LineUnavailableException;

    protected abstract void playSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException;

    protected abstract void stop();

}
