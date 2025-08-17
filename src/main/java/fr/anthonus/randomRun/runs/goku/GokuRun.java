package fr.anthonus.randomRun.runs.goku;

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
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.concurrent.ThreadLocalRandom;

public class GokuRun extends RandomRun {
    private static final Image gokuImage = Utils.createImage("/runAssets/goku/goku.png");
    private static final Media gokuSound = Utils.createMedia("/runAssets/goku/goku.mp3");
    private static final Media tpSound = Utils.createMedia("/runAssets/goku/gokuTP.mp3");

    public GokuRun(Pane root, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) {
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
        ImageView gokuImageView = Utils.createImageView(gokuImage, imageX, imageY, imageWitdh, imageHeight, imageOpacity);
        imageViews.add(gokuImageView);
        addStopListener(gokuImageView);

        final int[] tpLeft = {rand.nextInt(10, 20)};
        gokuImageView.setOnMouseEntered(_ -> {
            if (tpLeft[0] < 0) return;

            double randomX = rand.nextDouble(0, screenWidth-imageWitdh);
            double randomY = rand.nextDouble(0, screenHeight-imageHeight);

            gokuImageView.setLayoutX(randomX);
            gokuImageView.setLayoutY(randomY);
            imageX=randomX;
            imageY=randomY;

            MediaPlayer tpPlayer = Utils.createMediaPlayer(tpSound, 0.2, false);
            mediaPlayers.add(tpPlayer);
            tpPlayer.play();

            tpLeft[0]--;
        });

        MediaPlayer gokuPlayer = Utils.createMediaPlayer(gokuSound, 0, true);
        mediaPlayers.add(gokuPlayer);

        gokuPlayer.setOnReady(() -> { //besoin pour la setDuration
            gokuPlayer.seek(Duration.seconds(50));

            Timeline fadeIn = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(gokuImageView.opacityProperty(), 0.0),
                            new KeyValue(gokuPlayer.volumeProperty(), 0.0)
                    ),
                    new KeyFrame(Duration.seconds(40),
                            new KeyValue(gokuImageView.opacityProperty(), 1.0),
                            new KeyValue(gokuPlayer.volumeProperty(), maxVolume)
                    )
            );
            timelines.add(fadeIn);

            root.getChildren().add(gokuImageView);
            fadeIn.play();
            gokuPlayer.play();
        });
    }
}
