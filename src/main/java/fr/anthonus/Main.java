package fr.anthonus;

import fr.anthonus.randomRuns.runs.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        PopupMenu popup = new PopupMenu();
        MenuItem exitItem = new MenuItem("Quitter");
        exitItem.addActionListener(e -> System.exit(0));
        popup.add(exitItem);

        Image icon = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/images/goku.png"));
        TrayIcon trayIcon = new TrayIcon(icon, "HappyVirus", popup);
        trayIcon.setImageAutoSize(true);

        try {
            SystemTray tray = SystemTray.getSystemTray();
            tray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        ThreadLocalRandom rand = ThreadLocalRandom.current();
        while (true) {
            int randomTime = rand.nextInt(30_000, 180_000); // temps aléatoire entre 30 secondes et 3 minutes
            int totalSeconds = randomTime / 1000;
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            System.out.println("Next run in " + minutes + " minutes and " + seconds + " seconds");
            Thread.sleep(randomTime);

            int randomRunType = rand.nextInt(2);
            switch (randomRunType) {
                case 0 -> new GokuRun();
                case 1 -> new GyattRun();
            }
        }
    }
}