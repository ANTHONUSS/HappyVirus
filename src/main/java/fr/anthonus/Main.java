package fr.anthonus;

import fr.anthonus.randomRuns.runs.*;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static boolean activated = true;
    private static Thread whileTrueThread;

    public static void main(String[] args) throws AWTException {


        PopupMenu popup = new PopupMenu();
        addTrayItems(popup);

        Image icon = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/images/omniMan/omniMan.png"));
        TrayIcon trayIcon = new TrayIcon(icon, "HappyVirus", popup);
        trayIcon.setImageAutoSize(true);


        SystemTray tray = SystemTray.getSystemTray();
        tray.add(trayIcon);

        whileTrue();
    }

    private static void whileTrue() {
        whileTrueThread = new Thread(() -> {
            ThreadLocalRandom rand = ThreadLocalRandom.current();

            while (true) {
                if (!activated) {
                    return;
                }
                int randomTime = rand.nextInt(30_000, 180_000); // temps aléatoire entre 30 secondes et 3 minutes
                int totalSeconds = randomTime / 1000;
                int minutes = totalSeconds / 60;
                int seconds = totalSeconds % 60;
                System.out.println("Next run in " + minutes + " minutes and " + seconds + " seconds");
                try {
                    Thread.sleep(randomTime);
                } catch (InterruptedException _) {
                    return;
                }

                int randomRunType = rand.nextInt(6);
                switch (randomRunType) {
                    case 0 -> new GokuRun();
                    case 1 -> new OmniManRun();
                    case 2 -> new RatRun();
                    case 3 -> new AmogusRun();
                    case 4 -> new MikuRun();
                    case 5 -> new MikuRun();
                }
            }
        });

        whileTrueThread.start();
    }

    private static void addTrayItems(PopupMenu popup) {
        // Ajout du menu pour forcer l'apparition des items
        Menu runMenu = new Menu("Lancer un run");

        // Ajout de l'item pour le GokuRun
        MenuItem gokuRunItem = new MenuItem("GokuRun");
        gokuRunItem.addActionListener(e ->  new GokuRun());
        runMenu.add(gokuRunItem);

        // Ajout de l'item pour le OmniManRun
        MenuItem gyattRunItem = new MenuItem("GyattRun");
        gyattRunItem.addActionListener(e -> new OmniManRun());
        runMenu.add(gyattRunItem);

        // Ajout de l'item pour le RatRun
        MenuItem ratRunItem = new MenuItem("RatRun");
        ratRunItem.addActionListener(e -> new RatRun());
        runMenu.add(ratRunItem);

        // Ajout de l'item pour le AmogusRun
        MenuItem amogusRunItem = new MenuItem("AmogusRun");
        amogusRunItem.addActionListener(e -> new AmogusRun());
        runMenu.add(amogusRunItem);

        // Ajout de l'item pour le MikuRun
        MenuItem mikuRunItem = new MenuItem("MikuRun");
        mikuRunItem.addActionListener(e -> new MikuRun());
        runMenu.add(mikuRunItem);

        // Ajout de l'item pour le AreYouSureRun
        MenuItem areYouSureRunItem = new MenuItem("AreYouSureRun");
        areYouSureRunItem.addActionListener(e -> new AreYouSureRun());
        runMenu.add(areYouSureRunItem);

        // Ajout du menu au popup
        popup.add(runMenu);

        // Ajout de l'état d'activation
        CheckboxMenuItem checkbox = new CheckboxMenuItem("Activé");
        checkbox.setState(true);
        checkbox.addItemListener(e -> {
            if (checkbox.getState()) {
                activated = true;
                whileTrue();
                System.out.println("Activated");
            } else {
                activated = false;
                if (whileTrueThread != null && whileTrueThread.isAlive()) {
                    whileTrueThread.interrupt();
                }
                System.out.println("Deactivated");
            }
        });
        popup.add(checkbox);

        popup.addSeparator();

        // Ajout de l'exit
        MenuItem exitItem = new MenuItem("Quitter");
        exitItem.addActionListener(e -> System.exit(0));
        popup.add(exitItem);
    }
}