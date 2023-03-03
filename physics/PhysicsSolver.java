package physics;

import main.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PhysicsSolver {
    public static final float HEAT_TRANSFER = 0.03f;
    public static final float HEAT_DISSIPATION = 0.2f;
    public static final float GRAVITY = 0.03f;
    public static final float HEAT_RISING_STRENGTH = 0.03f;
    public static final float PARTICLE_RADIUS = 3f;
    public static final short COLLISION_SUBSTEPS = 3;
    public static final int NUM_PARTICLES = 10000;
    public static float DT = 0.6f;





    public static final Vec2D HEATER_POSITION = new Vec2D(Main.SCREEN_SIZE.width/2, Main.SCREEN_SIZE.height);
    public static final float HEATER_RADIUS = 100.0f;
    public static final float HEATER_TEMPERATURE = 0.3f;


    public static float wind = 0.0f;


    public static List<Particle> PARTICLES = new ArrayList<>();

    public static void addParticle(float x, float y, float r) {
        PARTICLES.add(new Particle(x, y, r));
    }

    public static void solve() {

        // Run each particle's movement
        for (int i = 0; i < PARTICLES.size(); i++) {
            Particle particle = PARTICLES.get(i);

            particle.heat();
            particle.accelerate(wind, GRAVITY - particle.temp * HEAT_RISING_STRENGTH);
            particle.move();
            particle.constrain();
        }

        // Run collisions multiple times to ensure best realism.
        for (int substep = 0; substep < COLLISION_SUBSTEPS; substep++) {

            //Positional Hashing to Improve Performance - sort the particles into cells of a set size, and only bother to check for collisions with surrounding cells.
            ArrayList<Particle>[][] Partitions = new ArrayList[(int)((Main.SCREEN_SIZE.height + 300) / (2 * PARTICLE_RADIUS))][(int)(Main.SCREEN_SIZE.width / (2 * PARTICLE_RADIUS))];
            for (int r = 0; r < Partitions.length; r++) {
                for (int c = 0; c < Partitions[r].length; c++) {
                    Partitions[r][c] = new ArrayList<Particle>();
                }
            }

            float pw = 1.0f / ((float)Main.SCREEN_SIZE.width / Partitions[0].length);
            float ph = 1.0f / ((float)Main.SCREEN_SIZE.height / Partitions.length);

            for (int i = 0; i < PARTICLES.size(); i++) {
                Particle particle = PARTICLES.get(i);

                // Kill clearly bugged particles
                if (particle.getVelocity().mag2() / DT > 1000 || Float.isNaN(particle.position.x) || Float.isNaN(particle.position.y)) {
                    PARTICLES.remove(i);
                    i--;
                    continue;
                }

                int indexX = Math.max(0, Math.min(Partitions[0].length -1, (int)Math.floor(particle.position.x * pw)));
                int indexY = Math.max(0, Math.min(Partitions.length -1, (int)Math.floor((particle.position.y + 300.0f) * ph)));

                // Don't do collisions on off-screen particles to save on computational power
                //if (indexX < 0 || indexX >= Partitions[0].length || indexY < 0 || indexY >= Partitions.length) continue;

                Partitions[indexY][indexX].add(particle);
            }

            final int PROCESSORS = 4;
            final int GRID_SIZE = (int)Math.sqrt(PROCESSORS);

            // Multithreading collision detections to increase performance.
            ThreadPoolExecutor threadExecutor = new ThreadPoolExecutor(PROCESSORS, PROCESSORS, Long.MAX_VALUE, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>());

            for (int bx = 0; bx < GRID_SIZE; bx++) {
                for (int by = 0; by < GRID_SIZE; by++) {
                    Thread collisionThread = new CollisionThread(Partitions, bx * Partitions[0].length / GRID_SIZE, by * Partitions.length / GRID_SIZE, (bx + 1) * Partitions[0].length / GRID_SIZE, (by + 1) * Partitions.length / GRID_SIZE);
                    threadExecutor.execute(collisionThread);
                }
            }

            threadExecutor.shutdown();
            try { threadExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); } catch (InterruptedException e) {}
        }

    }




}
