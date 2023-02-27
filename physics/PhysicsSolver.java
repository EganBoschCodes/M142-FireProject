package physics;

import main.Main;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSolver {
    public static final float HEAT_TRANSFER = 0.02f;
    public static final float HEAT_DISSIPATION = 0.997f;
    public static final float GRAVITY = 0.03f;
    public static final float HEAT_RISING_STRENGTH = 0.07f;
    public static final int NUM_PARTICLES = 6000;

    public static final float PARTICLE_RADIUS = 3f;








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

        // Run each particle's movement
        for (int i = 0; i < PARTICLES.size(); i++) {
            Particle particle = PARTICLES.get(i);
            particle.accelerate(0, GRAVITY - particle.temp * HEAT_RISING_STRENGTH);
            particle.constrain();
            particle.move();

            particle.temp *= HEAT_DISSIPATION;
        }

        //Positional Hashing to Improve Performance
        ArrayList[][] Partitions = new ArrayList[(int)(Main.SCREEN_SIZE.height / (6))][(int)(Main.SCREEN_SIZE.width / (6))];
        for (int r = 0; r < Partitions.length; r++) {
            for (int c = 0; c < Partitions[r].length; c++) {
                Partitions[r][c] = new ArrayList<Particle>();
            }
        }

        float pw = 1.0f / ((float)Main.SCREEN_SIZE.width / Partitions[0].length);
        float ph = 1.0f / ((float)Main.SCREEN_SIZE.height / Partitions.length);

        for (int i = 0; i < PARTICLES.size(); i++) {
            Particle particle = PARTICLES.get(i);

            if (particle.getVelocity().mag2() / DT > 1000) {
                PARTICLES.remove(i);
                i--;
                continue;
            }

            int indexX = Math.max(0, Math.min(Partitions[0].length - 1, (int)Math.floor(particle.position.x * pw)));
            int indexY = Math.max(0, Math.min(Partitions.length - 1, (int)Math.floor(particle.position.y * ph)));

            Partitions[indexY][indexX].add(particle);
        }


        // Run all particle collisions
        for (int i = 0; i < PARTICLES.size(); i++) {
            Particle particle = PARTICLES.get(i);

            int indexX = Math.max(0, Math.min(Partitions[0].length - 1, (int)Math.floor(particle.position.x * pw)));
            int indexY = Math.max(0, Math.min(Partitions.length - 1, (int)Math.floor(particle.position.y * ph)));

            for (int offsetX = -1; offsetX <= 1; offsetX++) {
                for (int offsetY = -1; offsetY <= 1; offsetY++) {
                    bounceParticleBlock(particle, Partitions, indexX + offsetX, indexY + offsetY);
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
