package fr.anthonus.randomRun.runs.uncannyCat;

import fr.anthonus.Main;
import fr.anthonus.randomRun.RandomRun;
import fr.anthonus.utils.Utils;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class UncannyCatRun extends RandomRun {
    private static final Image uncannyCatImage = Utils.createImage("/runAssets/uncanny-cat/uncanny-cat.png");
    private static final Image cannyCatImage = Utils.createImage("/runAssets/uncanny-cat/canny-cat.png");
    private ImageView uncannyCatImageView;

    private static final Media uncannyCatGolfSound = Utils.createMedia("/runAssets/uncanny-cat/uncanny-cat-golf.mp3");
    private static MediaPlayer uncannyCatGolfMedia;

    private static final List<UncannyCatRun> allCats = new ArrayList<>();

    private static Timeline uncannyCatEndTimeline;
    private static final Duration UNCANNY_CAT_END_TIMELINE_DURATION = Duration.seconds(30);

    public UncannyCatRun(Pane root, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) {
        super(
                root,
                imageX,
                imageY,
                imageWitdh,
                imageHeight,
                imageOpacity
        );

        Main.blocked = true;
        allCats.add(this);
    }

    @Override
    public void run() {
        uncannyCatImageView = Utils.createImageView(uncannyCatImage, imageX, imageY, imageWitdh, imageHeight, imageOpacity);
        imageViews.add(uncannyCatImageView);

        uncannyCatImageView.setOnMouseEntered(_ -> {
            double randomSize = rand.nextDouble(150, 250);
            double randomX = rand.nextDouble(0, screenWidth - randomSize);
            UncannyCatRun uncannyCat = new UncannyCatRun(
                    root,
                    randomX,
                    -randomSize*2,
                    randomSize,
                    randomSize,
                    1
            );
            uncannyCat.run();
            uncannyCatEndTimeline.playFromStart();
        });

        AnimationTimer moveTimer = new AnimationTimer() {
            double targetX = rand.nextDouble(0, screenWidth - imageWitdh);
            double targetY = rand.nextDouble(0, screenHeight - imageHeight);

            double velocityX = 0;
            double velocityY = 0;

            final double MAX_VELOCITY = 15;
            final double SPEED = 0.1;
            final double TOLERANCE = 200.0;

            final Rectangle toleranceBox = new Rectangle(
                    targetX - TOLERANCE / 2,
                    targetY - TOLERANCE / 2,
                    TOLERANCE,
                    TOLERANCE
            );

            boolean isEntered = false;

            {
                toleranceBox.setFill(Color.TRANSPARENT);
                root.getChildren().add(toleranceBox);
            }

            @Override
            public void handle(long now) {
                if (uncannyCatImageView.getBoundsInParent().intersects(toleranceBox.getBoundsInParent())) {
                    targetX = rand.nextDouble(0, screenWidth - imageWitdh);
                    targetY = rand.nextDouble(0, screenHeight - imageHeight);
                    toleranceBox.setX(targetX - TOLERANCE / 2);
                    toleranceBox.setY(targetY - TOLERANCE / 2);
                }

                if(!isEntered && imageY > 0) isEntered = true;

                doPhysics();
                if (isEntered) doCollision();
            }

            private void doPhysics(){
                if (imageX < targetX) velocityX += SPEED;
                if (imageX > targetX) velocityX -= SPEED;
                if (imageY < targetY) velocityY += SPEED;
                if (imageY > targetY) velocityY -= SPEED;

                if (Math.abs(velocityX) > MAX_VELOCITY) {
                    velocityX = Math.signum(velocityX) * MAX_VELOCITY;
                }
                if (Math.abs(velocityY) > MAX_VELOCITY) {
                    velocityY = Math.signum(velocityY) * MAX_VELOCITY;
                }

                imageX = imageX + velocityX;
                imageY = imageY + velocityY;
                uncannyCatImageView.setLayoutX(imageX);
                uncannyCatImageView.setLayoutY(imageY);
            }

            private void doCollision() {
                if (imageX < 0) {
                    uncannyCatImageView.setLayoutX(0);
                    velocityX = -velocityX;
                }
                if (imageX + imageWitdh > screenWidth) {
                    uncannyCatImageView.setLayoutX(screenWidth - imageWitdh);
                    velocityX = -velocityX;
                }
                if (imageY < 0) {
                    uncannyCatImageView.setLayoutY(0);
                    velocityY = -velocityY;
                }
                if (imageY + imageHeight > screenHeight) {
                    uncannyCatImageView.setLayoutY(screenHeight - imageHeight);
                    velocityY = -velocityY;
                }
            }
        };
        timers.add(moveTimer);

        if (uncannyCatEndTimeline == null) {
            uncannyCatEndTimeline = new Timeline(
                    new KeyFrame(UNCANNY_CAT_END_TIMELINE_DURATION, _ -> {
                        allCats.forEach(cat -> {
                            cat.uncannyCatImageView.setOnMousePressed(_ -> initialStop());
                            cat.uncannyCatImageView.setImage(cannyCatImage);
                            cat.uncannyCatImageView.setOnMouseEntered(null);
                        });
                    }
                    ));
            timelines.add(uncannyCatEndTimeline);
            uncannyCatEndTimeline.play();
        }

        if (uncannyCatGolfMedia == null) {
            uncannyCatGolfMedia = Utils.createMediaPlayer(uncannyCatGolfSound, maxVolume, true);
            mediaPlayers.add(uncannyCatGolfMedia);
            uncannyCatGolfMedia.play();
        }

        root.getChildren().add(uncannyCatImageView);
        moveTimer.start();
    }

    private void initialStop(){
        allCats.forEach(UncannyCatRun::stop);
        allCats.clear();
        uncannyCatGolfMedia = null;
        uncannyCatEndTimeline = null;

        Main.blocked = false;
    }

    @Override
    public void stop() {
        super.stop();
    }
}
