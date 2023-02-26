import physics.PhysicsThread;

import javax.swing.*;

public class Main {

    private static Frame frame;


    private static PhysicsThread physicsThread = new PhysicsThread();


    public static void initialize() {
        frame = new Frame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(815, 640);
        frame.setTitle("M142 Final Project");
        frame.setVisible(true);

        physicsThread.start();
    }

    public static void main(String[] args) throws InterruptedException {
        initialize();

        long frameCount = 0;

        while(true) {
            frame.repaint();
            Thread.sleep(5);
        }

    }
}
