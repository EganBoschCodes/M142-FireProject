package main;

import physics.PhysicsThread;

import javax.swing.*;
import java.awt.*;

public class Main {

    private static Frame frame;


    private static PhysicsThread physicsThread = new PhysicsThread();

    public static final Dimension SCREEN_SIZE = new Dimension(800, 600);


    public static void initialize() {
        frame = new Frame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(SCREEN_SIZE.width + 15, SCREEN_SIZE.height + 40);
        frame.setTitle("M142 Final Project");
        frame.setVisible(true);

        physicsThread.start();
    }

    public static void main(String[] args) throws InterruptedException {
        initialize();

        long frameCount = 0;

        while(true) {
            frame.repaint();
            Thread.sleep(16);
        }

    }
}
