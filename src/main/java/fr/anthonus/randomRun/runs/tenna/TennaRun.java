package fr.anthonus.randomRun.runs.tenna;

import fr.anthonus.Main;
import fr.anthonus.randomRun.RandomRun;
import fr.anthonus.utils.Utils;
import javafx.animation.*;
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

public class TennaRun extends RandomRun {
    private static final Image tennaRunningImage = Utils.createImage("/runAssets/tenna/tennaRun.gif");
    private static final Image correctTennaImage = Utils.createImage("/runAssets/tenna/tennaCleanDance.gif");
    private static final Image[] tennaImages = {
        Utils.createImage("/runAssets/tenna/tennaBend.gif"),
        Utils.createImage("/runAssets/tenna/tennaCrazy.gif"),
        Utils.createImage("/runAssets/tenna/tennaDance.gif"),
        Utils.createImage("/runAssets/tenna/tennaKick.gif"),
        Utils.createImage("/runAssets/tenna/tennaSpin.gif")
    };
    private static final Media runningSound = Utils.createMedia("/runAssets/tenna/running.mp3");
    private static final Media tvTimeSong = Utils.createMedia("/runAssets/tenna/tvTime.mp3");

    private final List<Tenna> tennas = new ArrayList<>();

    public TennaRun(Pane root, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) {
        super(
                root,
                imageX,
                imageY,
                imageWitdh,
                imageHeight,
                imageOpacity
        );

        Main.blocked = true;
    }

    @Override
    public void run() {
        ImageView tennaRunningImageView = Utils.createImageView(tennaRunningImage, imageX, imageY, imageWitdh, imageHeight, imageOpacity);
        imageViews.add(tennaRunningImageView);
        addStopListener(tennaRunningImageView);

        MediaPlayer runningMediaPlayer = Utils.createMediaPlayer(runningSound, maxVolume, false);
        mediaPlayers.add(runningMediaPlayer);

        Timeline appear = new Timeline(
                new KeyFrame(Duration.seconds(0)),
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(tennaRunningImageView.fitHeightProperty(), screenHeight * 2, Interpolator.EASE_IN),
                        new KeyValue(tennaRunningImageView.layoutXProperty(), screenWidth / 2 - 500, Interpolator.EASE_IN),
                        new KeyValue(tennaRunningImageView.layoutYProperty(), -screenHeight / 2, Interpolator.EASE_IN)
                )
        );
        timelines.add(appear);

        appear.setOnFinished(_ -> {
            if (!finished) {
                runningMediaPlayer.stop();
                runningMediaPlayer.dispose();
                mediaPlayers.remove(runningMediaPlayer);

                root.getChildren().remove(tennaRunningImageView);
                tennaRunningImageView.setImage(null);
                imageViews.remove(tennaRunningImageView);

                tennaArmy();
            } else {
                Main.blocked = false;
            }
        });

        root.getChildren().add(tennaRunningImageView);
        appear.play();
        runningMediaPlayer.play();
    }

    private void tennaArmy() {
        MediaPlayer tvTimeMediaPlayer = Utils.createMediaPlayer(tvTimeSong, maxVolume, true);
        mediaPlayers.add(tvTimeMediaPlayer);

        Timeline game = new Timeline(
                new KeyFrame(Duration.seconds(0), _ -> {
                    addTennas();
                }),
                new KeyFrame(Duration.seconds(6.5), _ -> {
                    moveTennas();
                })
        );
        timelines.add(game);

        game.play();
        tvTimeMediaPlayer.play();
    }

    private void addTennas() {
        tennas.clear();

        tennas.add(new Tenna(correctTennaImage));
        tennas.getFirst().setOnMousePressed(_ -> this.stop());

        for (int i = 0; i < tennaImages.length * 10; i++) {
            int tennaIndex = i % tennaImages.length;
            Tenna tenna = new Tenna(tennaImages[tennaIndex]);
            tennas.add(tenna);
        }

        root.getChildren().addAll(tennas);
    }

    private void moveTennas() {
        AnimationTimer moveTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Tenna tenna : tennas) {
                    double x = tenna.getLayoutX() + tenna.speed;
                    double y = tenna.baseY + tenna.amplitude * Math.sin(tenna.frequency * x + tenna.phase);
                    if (tenna.getLayoutX() > screenWidth) {
                        x = -Tenna.size;
                        y = rand.nextDouble(0, screenHeight - Tenna.size);
                        tenna.baseY = y;
                    }
                    tenna.setLayoutX(x);
                    tenna.setLayoutY(y);
                }
            }
        };
        timers.add(moveTimer);

        moveTimer.start();
    }

    @Override
    public void stop() {
        root.getChildren().removeAll(tennas);
        tennas.clear();

        Main.blocked = false;
        super.stop();
    }
}
