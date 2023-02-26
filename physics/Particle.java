package physics;

import java.awt.*;

public class Particle {
    public Vec2D position;
    public float r;

    public float mass = 1.0f;

    private Vec2D lastPosition;

    private Vec2D acceleration = new Vec2D(0, 0);

    public Particle(float x, float y, float r) {
        this.position = new Vec2D(x, y);
        this.lastPosition = new Vec2D(x, y);
        this.r = r;

        this.mass = r * r;

    }

    public void accelerate(float x, float y) {
        acceleration = acceleration.add(x, y);
    }

    public void move() {
        Vec2D velocity = position.sub(lastPosition);

        lastPosition.copy(position);
        position = position.add(velocity.mult(0.999f)).add(acceleration.mult(PhysicsSolver.DT * PhysicsSolver.DT));

        acceleration = new Vec2D(0, 0);
    }

    public void constrain() {
        position.x = Math.max(r, Math.min(800 - r, position.x));
        position.y = Math.max(r, Math.min(600 - r, position.y));
    }

    public void bounce(Particle other) {
        float distSquared = position.sub(other.position).mag2();

        if (distSquared > r * r + 2 * r * other.r + other.r * other.r) return;

        float dist = (float)Math.sqrt(distSquared);
        Vec2D diff = position.sub(other.position).div(dist);

        float lerpFactor = other.mass / (mass + other.mass);
        float shift = r + other.r - dist;

        position = position.add(diff.mult(lerpFactor * shift));
        other.position = other.position.sub(diff.mult((1 - lerpFactor) * shift));
    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval((int)(position.x - r), (int)(position.y - r), (int)(r*2), (int)(r*2));
    }


}
