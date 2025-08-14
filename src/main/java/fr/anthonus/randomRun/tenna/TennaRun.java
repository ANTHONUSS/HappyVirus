package fr.anthonus.randomRun.tenna;

import fr.anthonus.Main;
import fr.anthonus.randomRun.RandomRun;
import fr.anthonus.utils.Utils;
import javafx.animation.Interpolator;
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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TennaRun extends RandomRun {
    private final Image[] tennaImages;
    private final Image correctTenna;
    private final String tvTimeSong = "/runAssets/tenna/tvTime.mp3";

    private final List<Tenna> tennas = new ArrayList<>();


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

        correctTenna = new Image(getClass().getResource("/runAssets/tenna/tennaCleanDance.gif").toURI().toString());
    }

    @Override
    public void run() {
        addDeleteListener();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double screenWidth = Screen.getPrimary().getBounds().getWidth();

        Timeline appear = new Timeline(
                new KeyFrame(Duration.seconds(0)),
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(imageView.fitHeightProperty(), screenHeight*2, Interpolator.EASE_IN),
                        new KeyValue(imageView.layoutXProperty(), screenWidth / 2 - 500, Interpolator.EASE_IN),
                        new KeyValue(imageView.layoutYProperty(), -screenHeight/2, Interpolator.EASE_IN)
                )
        );
        appear.play();

        MediaPlayer player = players.getFirst();
        player.play();
        root.getChildren().add(imageView);

        appear.setOnFinished(_ -> {
            player.stop();
            players.remove(player);
            root.getChildren().remove(imageView);

            if (!finished) {
                Main.activated = false;
                if (Main.whileTrueThread != null && Main.whileTrueThread.isAlive()) {
                    Main.whileTrueThread.interrupt();
                }
                tennaArmy();
            }
        });
    }

    private void tennaArmy() {
        Media media;
        try {
            media = new Media(getClass().getResource(tvTimeSong).toURI().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        MediaPlayer player = new MediaPlayer(media);
        player.setVolume(maxVolume);
        players.add(player);

        Timeline game = new Timeline(
                new KeyFrame(Duration.seconds(0), _ -> {
                    addTennas();
                    root.getChildren().addAll(tennas);
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

        tennas.add(new Tenna(correctTenna));

        for (int i = 0; i < 50; i++) {
            int tennaIndex = i % tennaImages.length;
            Tenna tenna = new Tenna(tennaImages[tennaIndex]);
            tennas.add(tenna);
        }

        tennas.getFirst().setOnMousePressed(_ -> {
            Main.activated = true;
            Main.whileTrue();

            players.forEach(MediaPlayer::stop);
            root.getChildren().removeAll(tennas);
        });
    }

    private void moveTennas() {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        Timeline moveTimeline = new Timeline(
                new KeyFrame(Duration.millis(16), e -> {
                    for (int i = 0; i < tennas.size(); i++) {
                        Tenna tenna = tennas.get(i);

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
