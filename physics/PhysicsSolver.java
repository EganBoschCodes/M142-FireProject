package physics;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSolver {

    public static List<Particle> PARTICLES = new ArrayList<>();
    public static final float DT = 0.01f;

    public static void addParticle(float x, float y, float r) {
        PARTICLES.add(new Particle(x, y, r));
    }

    public static void solve() {
        for (int i = 0; i < PARTICLES.size(); i++) {
            Particle particle = PARTICLES.get(i);
            particle.accelerate(0, 300f);
            particle.move();
            particle.constrain();

            for (int j = 0; j < PARTICLES.size(); j++) {
                if (i == j) continue;
                particle.bounce(PARTICLES.get(j));
            }
        }
    }
}
