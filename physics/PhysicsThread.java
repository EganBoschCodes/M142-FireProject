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

            if (frameCount % 10 == 0) {

                if (PhysicsSolver.PARTICLES.size() < 6000) {
                    for (float x = 100; x < 700; x += 10) {
                        PhysicsSolver.addParticle(x + (float)Math.random(), 100, 2.5f + (float)Math.random());
                    }
                }


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
