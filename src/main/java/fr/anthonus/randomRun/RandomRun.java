package fr.anthonus.randomRun;

import fr.anthonus.Main;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class RandomRun {
    protected Pane root;
    protected static final double screenWidth = Screen.getPrimary().getBounds().getWidth();
    protected static final double screenHeight = Screen.getPrimary().getBounds().getHeight();
    protected static final ThreadLocalRandom rand = ThreadLocalRandom.current();

    protected final double maxVolume = 0.5;

    protected double imageX;
    protected double imageY;
    protected double imageWitdh;
    protected double imageHeight;
    protected double imageOpacity;

    protected final List<ImageView> imageViews = new ArrayList<>();

    protected final List<MediaPlayer> mediaPlayers = new ArrayList<>();

    protected final List<Timeline> timelines = new ArrayList<>();
    protected final List<AnimationTimer> timers = new ArrayList<>();

    protected boolean finished = false;

    public RandomRun(Pane root, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) {
        this.root = root;
        this.imageX = imageX;
        this.imageY = imageY;
        this.imageWitdh = imageWitdh;
        this.imageHeight = imageHeight;
        this.imageOpacity = imageOpacity;
    }

    protected void addStopListener(ImageView imageView){
        imageView.setOnMousePressed(_ -> stop());
    }

    public abstract void run();

    public void stop(){
        imageViews.forEach(imageView -> {
            imageView.setImage(null);
            imageView.setOnMousePressed(null);
        });
        root.getChildren().removeAll(imageViews);
        imageViews.clear();

        mediaPlayers.forEach(MediaPlayer::stop);
        mediaPlayers.forEach(MediaPlayer::dispose);
        mediaPlayers.clear();

        timelines.forEach(Timeline::stop);
        timelines.clear();
        timers.forEach(AnimationTimer::stop);
        timers.clear();

        Main.activeRuns.remove(this);
    }
}
