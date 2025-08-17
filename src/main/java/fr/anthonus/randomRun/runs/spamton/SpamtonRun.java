package fr.anthonus.randomRun.runs.spamton;

import fr.anthonus.randomRun.RandomRun;
import fr.anthonus.utils.Utils;
import javafx.animation.AnimationTimer;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SpamtonRun extends RandomRun {
    private static final Image spamtonImage = Utils.createImage("/runAssets/spamton/spamton_angel.gif");
    private static final Image pipisImage = Utils.createImage("/runAssets/spamton/pipis.png");
    private static final Image msPipisImage = Utils.createImage("/runAssets/spamton/msPipis.png");
    private static final Media spamtonSound = Utils.createMedia("/runAssets/spamton/spamton.mp3");

    private static final List<Pipis> pipisList = new ArrayList<>();

    public SpamtonRun(Pane root, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) {
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
        ImageView spamtonImageView = Utils.createImageView(spamtonImage, imageX, imageY, imageWitdh, imageHeight, imageOpacity);
        imageViews.add(spamtonImageView);
        addStopListener(spamtonImageView);

        MediaPlayer spamtonMediaPlayer = Utils.createMediaPlayer(spamtonSound, 0, false);
        mediaPlayers.add(spamtonMediaPlayer);

        spamtonMediaPlayer.setOnReady(() -> {
            spamtonMediaPlayer.seek(Duration.millis(rand.nextInt(0, 30_000)));


            root.getChildren().add(spamtonImageView);
            moveSpamton(spamtonImageView, spamtonMediaPlayer);
            spamtonMediaPlayer.play();
        });
    }

    private void moveSpamton(ImageView spamtonImageView, MediaPlayer spamtonMediaPlayer) {
        double speed = rand.nextDouble(20, 25);
        double amplitude = rand.nextDouble(15, 20);
        double frequency = rand.nextDouble(0.001, 0.01);

        double distance = screenWidth + 250;
        double fps = 60.0;
        double speedPerSecond = speed * fps;
        double timeSeconds = distance / speedPerSecond;
        long timeMillis = (long)(timeSeconds * 1000);

        long pipisTime = rand.nextLong(50, timeMillis-50);
        long startTime = System.currentTimeMillis();
        final boolean[] hasTriggered = {false};

        AnimationTimer moveTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsed = System.currentTimeMillis() - startTime;
                if (!hasTriggered[0] && elapsed >= pipisTime) {
                    hasTriggered[0] = true;

                    Image imageToUse = rand.nextDouble(100) <= 0.5 ? msPipisImage : pipisImage;
                    Pipis newPipis = new Pipis(imageToUse, spamtonImageView.getLayoutX()+30, spamtonImageView.getLayoutY()+70, pipisList);
                    pipisList.add(newPipis);
                    root.getChildren().add(newPipis);
                    newPipis.toBack();
                }
                double x = spamtonImageView.getLayoutX() - speed;
                double y = spamtonImageView.getLayoutY() + amplitude * Math.sin(frequency * x);

                if (spamtonImageView.getLayoutX() < -250) {
                    spamStop();
                }

                spamtonImageView.setLayoutX(x);
                spamtonImageView.setLayoutY(y);
            }
        };
        timers.add(moveTimer);

        long fadeMillis = timeMillis/3;
        Timeline soundFade = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(spamtonMediaPlayer.volumeProperty(), 0.0)
                ),
                new KeyFrame(Duration.millis(fadeMillis),
                        new KeyValue(spamtonMediaPlayer.volumeProperty(), maxVolume)
                ),
                new KeyFrame(Duration.millis(timeMillis-fadeMillis),
                        new KeyValue(spamtonMediaPlayer.volumeProperty(), maxVolume)
                ),
                new KeyFrame(Duration.millis(timeMillis),
                        new KeyValue(spamtonMediaPlayer.volumeProperty(), 0.0)
                )
        );
        timelines.add(soundFade);

        soundFade.play();
        moveTimer.start();
    }

    @Override
    public void stop() {
        super.stop();
        root.getChildren().removeAll(pipisList);
        pipisList.clear();
    }

    private void spamStop() {
        super.stop();
    }
}
