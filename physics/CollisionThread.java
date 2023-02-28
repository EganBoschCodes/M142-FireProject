package physics;

import java.util.ArrayList;

public class CollisionThread extends Thread {

    private ArrayList<Particle>[][] Partitions;

    private int minX, minY, maxX, maxY;

    public CollisionThread(ArrayList<Particle>[][] Partitions, int minimumX, int minimumY, int maximumX, int maximumY) {
        this.Partitions = Partitions;
        this.minX = minimumX;
        this.maxX = maximumX;
        this.minY = minimumY;
        this.maxY = maximumY;
    }

    @Override
    public void run() {

        for (int bx = minX; bx < maxX; bx++) {
            for (int by = minY; by < maxY; by++) {
                ArrayList<Particle> blockParticles = Partitions[by][bx];

                for (int i = 0; i < blockParticles.size(); i++) {
                    Particle particle = blockParticles.get(i);

                    for (int offsetX = -1; offsetX <= 1; offsetX++) {
                        for (int offsetY = -1; offsetY <= 1; offsetY++) {
                            bounceParticleBlock(particle, Partitions, bx + offsetX, by + offsetY);
                        }
                    }
                }
            }
        }
    }

    private static void bounceParticleBlock(Particle particle, ArrayList<Particle>[][] Partitions, int indexX, int indexY) {
        if (indexX < 0 || indexX >= Partitions[0].length || indexY < 0 || indexY >= Partitions.length) return;

        ArrayList<Particle> block = Partitions[indexY][indexX];
        for (Particle other : block) {
            if (particle != other) particle.bounce(other);
        }
    }
}
