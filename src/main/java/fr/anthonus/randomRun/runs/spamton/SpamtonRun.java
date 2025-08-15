package fr.anthonus.randomRun.runs.spamton;

import fr.anthonus.randomRun.RandomRun;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SpamtonRun extends RandomRun {
    private static final List<Pipis> pipis = new ArrayList<>();
    private static final Image pipisImage = new Image(SpamtonRun.class.getResource("/runAssets/spamton/pipis.png").toExternalForm());
    private static final Image msPipisImage = new Image(SpamtonRun.class.getResource("/runAssets/spamton/msPipis.png").toExternalForm());

    public SpamtonRun(Pane root, String imagePath, String soundPath, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) {
        super(
                root,
                imagePath,
                soundPath,
                imageX,
                imageY,
                imageWitdh,
                imageHeight,
                imageOpacity
        );
    }

    @Override
    public void run() {
        addDeleteListener();
        ThreadLocalRandom rand = ThreadLocalRandom.current();


        MediaPlayer player = players.getFirst();
        player.setVolume(0);

        player.setOnReady(() -> {
            player.seek(Duration.millis(rand.nextInt(0, 30_000)));


            root.getChildren().add(imageView);
            moveSpamton();
            player.play();
        });
    }

    private void moveSpamton() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        ThreadLocalRandom rand = ThreadLocalRandom.current();

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
                    Pipis newPipis = new Pipis(imageToUse, imageView.getLayoutX()+30, imageView.getLayoutY()+70);
                    pipis.add(newPipis);
                    root.getChildren().add(newPipis);
                    newPipis.toBack();
                }
                double x = imageView.getLayoutX() - speed;
                double y = imageView.getLayoutY() + amplitude * Math.sin(frequency * x);

                if (imageView.getLayoutX() < -250) {
                    this.stop();
                }

                imageView.setLayoutX(x);
                imageView.setLayoutY(y);
            }
        };
        timers.add(moveTimer);

        MediaPlayer player = players.getFirst();
        long fadeMillis = timeMillis/3;
        Timeline soundFade = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(player.volumeProperty(), 0.0)
                ),
                new KeyFrame(Duration.millis(fadeMillis),
                        new KeyValue(player.volumeProperty(), maxVolume)
                ),
                new KeyFrame(Duration.millis(timeMillis-fadeMillis),
                        new KeyValue(player.volumeProperty(), maxVolume)
                ),
                new KeyFrame(Duration.millis(timeMillis),
                        new KeyValue(player.volumeProperty(), 0.0)
                )
        );
        timelines.add(soundFade);

        soundFade.play();
        moveTimer.start();
    }

    @Override
    protected void stop() {
        super.stop();
        root.getChildren().removeAll(pipis);
        pipis.clear();
    }
}
