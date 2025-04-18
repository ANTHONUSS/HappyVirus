package fr.anthonus;

import fr.anthonus.runs.RandomImageSound;

import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Random rand = new Random();
        while (true) {
            int randInt = rand.nextInt(60000, 120000);
            System.out.println("Waiting for " + randInt/1000 + "s");
            Thread.sleep(randInt);
            System.out.println("Creating new image and sound");
            switch (rand.nextInt(2)){
                case 0 -> new RandomImageSound("/fr/anthonus/images/goku.png", "/fr/anthonus/audio/goku.wav");
                case 1 -> new RandomImageSound("/fr/anthonus/images/gyatt.png", "/fr/anthonus/audio/gyatt.wav");
            }
        }
    }
}