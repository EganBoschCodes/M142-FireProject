package main;

import physics.PhysicsSolver;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Frame extends JFrame {

    private Panel panel;

    public ArrayList<ArrayList<Float[]>> frameBuffers;

    public Frame() {
        panel = new Panel();
        add(panel);

        frameBuffers = new ArrayList<>();
    }

    private class Panel extends JPanel {

        public Panel() {
            super.setPreferredSize(new Dimension(300, 300));
        }

        @Override
        public void paintComponent(Graphics g) {

            if (frameBuffers.isEmpty()) return;

            super.paintComponent(g);
            setBackground(Color.BLACK);

            ArrayList<Float[]> buffer = frameBuffers.remove(0);

            if (frameBuffers.size() > 20) {
                frameBuffers.subList(0, 5).clear();
            }

            Graphics2D g2d = (Graphics2D)g.create();
            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2d.setRenderingHints(hints);

            for (int i = 0; i < PhysicsSolver.PARTICLES.size(); i++) {
                PhysicsSolver.PARTICLES.get(i).render(g2d);
            }

            //for (Float[] particle : buffer) {
                //g2d.setColor(new Color(Math.max(0, Math.min(1.0f, particle[2])), Math.max(0, Math.min(1.0f, particle[2] * 0.3f)), 0));
                //g2d.fillOval((int)(particle[0] - PhysicsSolver.PARTICLE_RADIUS), (int)(particle[1] - PhysicsSolver.PARTICLE_RADIUS), (int)(PhysicsSolver.PARTICLE_RADIUS * 2), (int)(PhysicsSolver.PARTICLE_RADIUS * 2));
            //}

            g2d.dispose();
        }
    }
}
