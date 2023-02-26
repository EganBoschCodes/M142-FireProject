package physics;

public class PhysicsThread extends Thread {
    @Override
    public void run() {
        long frameCount = 0;
        while(true) {
            long startTime = System.nanoTime();

            PhysicsSolver.solve();

            long endTime = System.nanoTime();
            long sleepTime = 5 - (endTime - startTime) / 1000000;

            if (frameCount % 5 == 0) {
                PhysicsSolver.addParticle((float)Math.random() * 800, 100, 3);
                System.out.println("FPS: " + 240.0f / ((float)(endTime - startTime) / 100000.0f));
                System.out.println("Objects: " + PhysicsSolver.PARTICLES.size());
            }

            frameCount++;
            if (sleepTime > 0) {
                try { Thread.sleep(sleepTime); } catch (InterruptedException e) {}
            }
        }
    }
}
