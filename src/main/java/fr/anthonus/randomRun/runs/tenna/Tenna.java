package fr.anthonus.randomRun.runs.tenna;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

import java.util.concurrent.ThreadLocalRandom;

public class Tenna extends ImageView {
    public static final double size = 450;

    public double baseX;
    public double baseY;
    public double speed;
    public double amplitude;
    public double frequency;
    public double phase;

    public Tenna(Image image) {
        super(image);
        setFitHeight(size);
        setPreserveRatio(true);

        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double screenWidth = Screen.getPrimary().getBounds().getWidth();

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        this.baseX = rand.nextDouble(-screenWidth, -size);
        this.baseY = rand.nextDouble(0, screenHeight - size);
        this.setLayoutX(baseX);
        this.setLayoutY(baseY);

        this.speed = rand.nextDouble(10, 20);
        this.amplitude = rand.nextDouble(50, 100);
        this.frequency = rand.nextDouble(0.001, 0.005);
        this.phase = rand.nextDouble(0, 5 * Math.PI);
    }
}
