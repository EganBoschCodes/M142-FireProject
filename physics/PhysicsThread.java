package physics;

import main.Main;

import java.util.ArrayList;

public class PhysicsThread extends Thread {

    @Override
    public void run() {

        long frameCount = 0;

        for (float x = PhysicsSolver.PARTICLE_RADIUS * 2; x < Main.SCREEN_SIZE.width; x += PhysicsSolver.PARTICLE_RADIUS * 3) {
            for(float y = -300; y < Main.SCREEN_SIZE.height; y += PhysicsSolver.PARTICLE_RADIUS * 3)
                PhysicsSolver.addParticle(x + (float)Math.random() * 5, y, PhysicsSolver.PARTICLE_RADIUS);
        }

        while(true) {
            long startTime = System.nanoTime();

            PhysicsSolver.solve();

            long endTime = System.nanoTime();
            long sleepTime = 5 - (endTime - startTime) / 1000000;

            // Log Performance Data, and add new Particles
            if (frameCount % 10 == 0) {
                System.out.println("FPS: " + 1000.0f / ((float)(endTime - startTime) / 1000000.0f));
                System.out.println("Objects: " + PhysicsSolver.PARTICLES.size());
            }

            if (frameCount % 1000 == 0) {
                PhysicsSolver.wind = ((float)Math.random() - 0.5f) / 100;
            }

            frameCount++;

            ArrayList<Float[]> frameBuffer = new ArrayList<>();

            for (Particle particle : PhysicsSolver.PARTICLES) {
                frameBuffer.add(new Float[] {particle.position.x, particle.position.y, particle.temp} );
            }

            Main.frame.frameBuffers.add(frameBuffer);

            if (sleepTime > 0) {
                try { Thread.sleep(sleepTime); } catch (InterruptedException e) {}
            }
        }
    }
}
