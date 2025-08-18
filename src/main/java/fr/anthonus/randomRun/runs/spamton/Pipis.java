package fr.anthonus.randomRun.runs.spamton;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Pipis extends ImageView {
    private static final double SIZE = 50;

    private double velocityX = 0;
    private double velocityY = 0;
    private boolean grabbed = false;

    private double lastMouseX, lastMouseY;

    private static List<Pipis> allPipis;

    private static final double FRICTION = 0.98;
    private static final double BOUNCE = 0.5;
    private static final double THROW_MULTIPLIER = 2.0;
    private static final double MAX_THROW_SPEED = 120.0;

    public Pipis(Image image, double x, double y, List<Pipis> allPipis) {
        super(image);
        setFitHeight(SIZE);
        setPreserveRatio(true);

        setLayoutX(x);
        setLayoutY(y);

        if (Pipis.allPipis == null) Pipis.allPipis = allPipis;

        setOnMousePressed(event -> {
            grabbed = true;

            velocityX = 0;
            velocityY = 0;

            lastMouseX = event.getSceneX();
            lastMouseY = event.getSceneY();
        });
        setOnMouseDragged(event -> {
            double halfW = getBoundsInLocal().getWidth() / 2.0;
            double halfH = getBoundsInLocal().getHeight() / 2.0;
            double parentSceneX = getParent().localToScene(0, 0).getX();
            double parentSceneY = getParent().localToScene(0, 0).getY();

            setLayoutX(event.getSceneX() - parentSceneX - halfW);
            setLayoutY(event.getSceneY() - parentSceneY - halfH);

            velocityX = event.getSceneX() - lastMouseX;
            velocityY = event.getSceneY() - lastMouseY;

            lastMouseX = event.getSceneX();
            lastMouseY = event.getSceneY();
        });

        setOnMouseReleased(_ -> {
            grabbed = false;

            velocityX *= THROW_MULTIPLIER;
            velocityY *= THROW_MULTIPLIER;

            double speed = Math.hypot(velocityX, velocityY);
            if (speed > MAX_THROW_SPEED) {
                double k = MAX_THROW_SPEED / speed;
                velocityX *= k;
                velocityY *= k;
            }
        });

        AnimationTimer physicsTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!updateSleep()) {
                    doPhysics();
                    doCollision();
                }
            }
        };
        physicsTimer.start();

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        velocityX += rand.nextDouble(-10, 10);
        velocityY += rand.nextDouble(-10, 10);

    }

    private boolean updateSleep() {
        double speed = Math.hypot(velocityX, velocityY);
        double threshold = 0.1;

        if (speed < threshold) {
            velocityX = 0;
            velocityY = 0;
            return true;
        } else {
            return false;
        }
    }

    private void doPhysics() {
        if (grabbed) return;

        setLayoutX(getLayoutX() + velocityX);
        setLayoutY(getLayoutY() + velocityY);

        velocityX = velocityX * FRICTION;
        velocityY = velocityY * FRICTION;
    }

    private void doCollision() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();


        double x = getLayoutX();
        double y = getLayoutY();
        double size = Pipis.SIZE;

        if (x < 0) {
            setLayoutX(0);
            velocityX = -velocityX * BOUNCE;
        }
        if (x + size > screenWidth) {
            setLayoutX(screenWidth - size);
            velocityX = -velocityX * BOUNCE;
        }
        if (y < 0) {
            setLayoutY(0);
            velocityY = -velocityY * BOUNCE;
        }
        if (y + size > screenHeight) {
            setLayoutY(screenHeight - size);
            velocityY = -velocityY * BOUNCE;
        }

        for (Pipis other : allPipis) {
            if (this == other) continue;
            double dx = getLayoutX() - other.getLayoutX();
            double dy = getLayoutY() - other.getLayoutY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < size && distance > 0) {
                double overlap = size - distance;
                double nx = dx / distance;
                double ny = dy / distance;

                setLayoutX(getLayoutX() + nx * overlap / 2);
                setLayoutY(getLayoutY() + ny * overlap / 2);
                other.setLayoutX(other.getLayoutX() - nx * overlap / 2);
                other.setLayoutY(other.getLayoutY() - ny * overlap / 2);

                double tx = -ny;
                double ty = nx;

                double v1n = velocityX * nx + velocityY * ny;
                double v1t = velocityX * tx + velocityY * ty;
                double v2n = other.velocityX * nx + other.velocityY * ny;
                double v2t = other.velocityX * tx + other.velocityY * ty;

                double v1nAfter = v2n;
                double v2nAfter = v1n;

                velocityX = v1nAfter * nx + v1t * tx;
                velocityY = v1nAfter * ny + v1t * ty;
                other.velocityX = v2nAfter * nx + v2t * tx;
                other.velocityY = v2nAfter * ny + v2t * ty;
            }
        }

    }
}
