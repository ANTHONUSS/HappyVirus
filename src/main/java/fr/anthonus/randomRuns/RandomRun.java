package fr.anthonus.randomRuns;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class RandomRun extends JFrame {
    protected final Clip clip;
    protected JLabel imageLabel;

    protected URL imageURL;
    protected URL soundURL;

    protected final float maxVolume = -20.0f;

    protected int imageWitdh;
    protected int imageHeight;
    protected int gifFps;


    protected RandomRun(URL imageURL, URL soundURL, int imageWitdh, int imageHeight) {
        // set des paths
        this.imageURL = imageURL;
        this.soundURL = soundURL;
        this.imageWitdh = imageWitdh;
        this.imageHeight = imageHeight;

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

    protected List<BufferedImage> loadGifFrames(URL gifURL) throws IOException {
        List<BufferedImage> frames = new ArrayList<>();

        // Ouverture du flux d'entrée à partir de l'URL
        try (ImageInputStream stream = ImageIO.createImageInputStream(gifURL.openStream())) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
            if (!readers.hasNext()) {
                throw new IOException("Aucun lecteur d'image trouvé pour le GIF");
            }
            ImageReader reader = readers.next();
            reader.setInput(stream);
            int count = reader.getNumImages(true);
            for (int i = 0; i < count; i++) {
                BufferedImage frame = reader.read(i);
                frames.add(frame);
            }
        }

        return frames;
    }

}
