package fr.anthonus.randomRun.runs.rat;

import fr.anthonus.randomRun.RandomRun;
import fr.anthonus.utils.Utils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class RatRun extends RandomRun {
    private static final Image ratImage = Utils.createImage("/runAssets/rat-dance/rat-dance.gif");
    private static final Media ratSound = Utils.createMedia("/runAssets/rat-dance/rat-dance.mp3");

    public RatRun(Pane root, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) {
        super(
                root,
                imageX,
                imageY,
                imageWitdh,
                imageHeight,
                imageOpacity
        );
    }

    @Override
    public void run() {
        ImageView ratImageView = Utils.createImageView(ratImage, imageX, imageY, imageWitdh, imageHeight, imageOpacity);
        imageViews.add(ratImageView);
        addStopListener(ratImageView);

        double bpm = 113/4.0;
        double intervalSeconds = 60.0 / bpm;
        double offsetSeconds = 0.2;

        Timeline bpmTimer = new Timeline(
                new KeyFrame(Duration.seconds(intervalSeconds), _ -> {
                    double randomX = rand.nextDouble(0, screenWidth-imageWitdh);
                    double randomY = rand.nextDouble(0, screenHeight-imageHeight);

                    ratImageView.setLayoutX(randomX);
                    ratImageView.setLayoutY(randomY);
                    imageX=randomX;
                    imageY=randomY;
                })
        );
        timelines.add(bpmTimer);
        bpmTimer.setCycleCount(Timeline.INDEFINITE);
        bpmTimer.setDelay(Duration.seconds(offsetSeconds));

        MediaPlayer ratMediaPlayer = Utils.createMediaPlayer(ratSound, maxVolume, true);
        mediaPlayers.add(ratMediaPlayer);

        root.getChildren().add(ratImageView);
        bpmTimer.play();
        ratMediaPlayer.play();
    }
}
