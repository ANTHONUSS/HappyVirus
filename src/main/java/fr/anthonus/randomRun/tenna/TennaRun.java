package fr.anthonus.randomRun.tenna;

import fr.anthonus.Main;
import fr.anthonus.randomRun.RandomRun;
import fr.anthonus.utils.Utils;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TennaRun extends RandomRun {
    private final Image[] tennaImages;
    private final Image correctTennaImage;

    private final List<Tenna> tennas = new ArrayList<>();

    private static final String tvTimeSong = "/runAssets/tenna/tvTime.mp3";


    public TennaRun(Pane root, String imagePath, String soundPath, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) throws URISyntaxException {
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

        tennaImages = new Image[]{
                new Image(getClass().getResource("/runAssets/tenna/tennaBend.gif").toURI().toString()),
                new Image(getClass().getResource("/runAssets/tenna/tennaKick.gif").toURI().toString()),
                new Image(getClass().getResource("/runAssets/tenna/tennaDance.gif").toURI().toString()),
                new Image(getClass().getResource("/runAssets/tenna/tennaSpin.gif").toURI().toString()),
                new Image(getClass().getResource("/runAssets/tenna/tennaCrazy.gif").toURI().toString())
        };

        correctTennaImage = new Image(getClass().getResource("/runAssets/tenna/tennaCleanDance.gif").toURI().toString());
    }

    @Override
    public void run() {
        addDeleteListener();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double screenWidth = Screen.getPrimary().getBounds().getWidth();


        Timeline appear = new Timeline(
                new KeyFrame(Duration.seconds(0)),
                new KeyFrame(Duration.seconds(2.5),
                        new KeyValue(imageView.fitHeightProperty(), screenHeight * 2, Interpolator.EASE_IN),
                        new KeyValue(imageView.layoutXProperty(), screenWidth / 2 - 500, Interpolator.EASE_IN),
                        new KeyValue(imageView.layoutYProperty(), -screenHeight / 2, Interpolator.EASE_IN)
                )
        );

        appear.setOnFinished(_ -> {
            if (!finished) {
                players.getFirst().stop();
                players.removeFirst();

                root.getChildren().remove(imageView);

                Main.activated = false;
                if (Main.whileTrueThread != null && Main.whileTrueThread.isAlive()) {
                    Main.whileTrueThread.interrupt();
                }
                tennaArmy();
            }
        });

        MediaPlayer player = players.getFirst();

        root.getChildren().add(imageView);
        appear.play();
        player.play();
    }

    private void tennaArmy() {
        MediaPlayer player = Utils.createMediaPlayer(tvTimeSong);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setVolume(maxVolume);
        players.add(player);

        Timeline game = new Timeline(
                new KeyFrame(Duration.seconds(0), _ -> {
                    addTennas();
                }),
                new KeyFrame(Duration.seconds(6.5), _ -> {
                    moveTennas();
                })
        );

        game.play();
        player.play();
    }

    private void addTennas() {
        tennas.clear();

        tennas.add(new Tenna(correctTennaImage));

        tennas.getFirst().setOnMousePressed(_ -> {
            players.getFirst().stop();
            players.removeFirst();

            Main.activated = true;
            Main.whileTrue();
            root.getChildren().removeAll(tennas);
        });

        for (int i = 0; i < tennaImages.length * 10; i++) {
            int tennaIndex = i % tennaImages.length;
            Tenna tenna = new Tenna(tennaImages[tennaIndex]);
            tennas.add(tenna);
        }

        root.getChildren().addAll(tennas);
    }

    private void moveTennas() {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        Timeline moveTimeline = new Timeline(
                new KeyFrame(Duration.millis(16), e -> {
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
                }));
        moveTimeline.setCycleCount(Timeline.INDEFINITE);
        moveTimeline.play();
    }
}
