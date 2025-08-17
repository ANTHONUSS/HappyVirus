package fr.anthonus.randomRun.runs.amogus;

import fr.anthonus.randomRun.RandomRun;
import fr.anthonus.utils.Utils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AmogusRun extends RandomRun {
    private static final Image amogusImage = Utils.createImage("/runAssets/amogus/amogus.png");
    private static final Media amogusSound = Utils.createMedia("/runAssets/amogus/amogus.mp3");

    public AmogusRun(Pane root, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) {
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
        ImageView amogusImageView = Utils.createImageView(amogusImage, imageX, imageY, imageWitdh, imageHeight, imageOpacity);
        imageViews.add(amogusImageView);
        addStopListener(amogusImageView);

        Timeline showUp = new Timeline(
                new KeyFrame(Duration.seconds(0)),
                new KeyFrame(Duration.seconds(50),
                        new KeyValue(amogusImageView.layoutYProperty(), screenHeight - imageHeight - 10)
                )
        );
        timelines.add(showUp);

        MediaPlayer amogusMediaPlayer = Utils.createMediaPlayer(amogusSound, maxVolume, true);
        mediaPlayers.add(amogusMediaPlayer);

        root.getChildren().add(amogusImageView);
        showUp.play();
        amogusMediaPlayer.play();
    }
}
