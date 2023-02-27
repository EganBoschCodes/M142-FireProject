package physics;

import main.Main;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSolver {

    public static List<Particle> PARTICLES = new ArrayList<>();
    public static float DT = 0.01f;
    private static long nanoTime;

    public static void addParticle(float x, float y, float r) {
        PARTICLES.add(new Particle(x, y, r));
    }

    public static void solve() {
        long newTime = System.nanoTime();
        DT = (newTime - nanoTime) / 10000000.0f;
        nanoTime = newTime;

        //PARTITIONING

        ArrayList[][] Partitions = new ArrayList[10][10];
        for (int r = 0; r < Partitions.length; r++) {
            for (int c = 0; c < Partitions[r].length; c++) {
                Partitions[r][c] = new ArrayList<Particle>();
            }
        }

        float pw = 1 / (Main.SCREEN_SIZE.width / Partitions[0].length);
        float ph = 1 / (Main.SCREEN_SIZE.height / Partitions.length);

        for (int i = 0; i < PARTICLES.size(); i++) {
            Particle particle = PARTICLES.get(i);
            int indexX = (int)Math.floor(particle.position.x * pw);
            int indexY = (int)Math.floor(particle.position.y * ph);

            Partitions[indexY][indexX].add(particle);
        }

        for (int i = 0; i < PARTICLES.size(); i++) {
            Particle particle = PARTICLES.get(i);
            particle.accelerate(0, 0.03f);
            particle.move();
            particle.constrain();


            int indexX = (int)Math.floor(particle.position.x * pw);
            int indexY = (int)Math.floor(particle.position.y * ph);

            for (int offsetX = -1; offsetX <= 1; offsetX++) {
                for (int offsetY = -1; offsetY <= 1; offsetY++) {
                    bounceParticleBlock(particle, Partitions, indexX + offsetX, indexY + offsetY);
                }
            }
        }
    }

    private static void bounceParticleBlock(Particle particle, ArrayList<Particle>[][] Partitions, int indexX, int indexY) {
        if (indexX < 0 || indexX >= Partitions[0].length || indexY < 0 || indexY >= Partitions.length) return;
        for (int i = 0; i < Partitions[indexX][indexY].size();i++) {
            Particle other = Partitions[indexX][indexY].get(i);
            if (particle != other) particle.bounce(other);
        }
    }
}
