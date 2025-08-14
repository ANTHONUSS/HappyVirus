package fr.anthonus.randomRun;

import fr.anthonus.Main;
import fr.anthonus.utils.Utils;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class RandomRun {
    protected Pane root;

    protected final double maxVolume = 0.5;

    protected double imageX;
    protected double imageY;
    protected double imageWitdh;
    protected double imageHeight;
    protected double imageOpacity;

    protected ImageView imageView;

    protected final List<MediaPlayer> players = new ArrayList<>();

    protected boolean finished = false;

    public RandomRun(Pane root, String imagePath, String soundPath, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) {
        this.root = root;
        this.imageX = imageX;
        this.imageY = imageY;
        this.imageWitdh = imageWitdh;
        this.imageHeight = imageHeight;
        this.imageOpacity = imageOpacity;

        imageView = Utils.createImage(imagePath, imageX, imageY, imageWitdh, imageHeight, imageOpacity);

        MediaPlayer player = Utils.createMediaPlayer(soundPath);
        players.add(player);
    }

    public abstract void run();

    protected void addDeleteListener(RandomRun child) {
        imageView.setOnMousePressed(_ -> {
            players.forEach(MediaPlayer::stop);
            root.getChildren().remove(imageView);
            finished = true;
            Main.activeRuns.remove(child);
        });
    }
}
