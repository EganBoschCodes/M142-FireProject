package main;

import physics.PhysicsSolver;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private Panel panel;

    public Frame() {
        panel = new Panel();
        add(panel);
    }

    private class Panel extends JPanel {

        public Panel() {
            super.setPreferredSize(new Dimension(300, 300));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.BLACK);

            Graphics2D g2d = (Graphics2D)g.create();
            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2d.setRenderingHints(hints);


            for (int i = 0; i < PhysicsSolver.PARTICLES.size(); i++) {
                PhysicsSolver.PARTICLES.get(i).render(g2d);
            }

            g2d.dispose();
        }
    }
}
