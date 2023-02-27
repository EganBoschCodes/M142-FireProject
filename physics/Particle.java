package physics;

import main.Main;

import java.awt.*;

public class Particle {
    public Vec2D position;
    private Vec2D lastPosition;
    public float r;
    private float baselineR;
    public float mass = 1.0f;
    public float temp = 0.0f;




    private Vec2D acceleration = new Vec2D(0, 0);

    public Particle(float x, float y, float r) {
        this.position = new Vec2D(x, y);
        this.lastPosition = new Vec2D(x, y);
        this.r = r;
        this.baselineR = r;

        this.mass = r * r;

    }

    public void accelerate(float x, float y) {
        acceleration = acceleration.add(x, y);
    }

    public void move() {
        Vec2D velocity = position.sub(lastPosition);

        lastPosition.copy(position);
        position = position.add(velocity.mult(0.995f)).add(acceleration.mult(PhysicsSolver.DT * PhysicsSolver.DT));

        acceleration = new Vec2D(0, 0);
    }

    public void constrain() {
        position.x = Math.max(r, Math.min(Main.SCREEN_SIZE.width - r, position.x));

        if (position.y < r * 5) temp -= 0.05;
        if (position.y > Main.SCREEN_SIZE.height - 30 && position.x > 100 && position.x < Main.SCREEN_SIZE.width - 100) temp += 0.03 + Math.random() / 10;
        position.y = Math.max(r, Math.min(Main.SCREEN_SIZE.height - r, position.y));

        //r = baselineR + temp / 5;
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

        float tempDiff = other.temp - temp;

        temp += tempDiff * PhysicsSolver.HEAT_TRANSFER * other.mass / mass;
        other.temp -= tempDiff * PhysicsSolver.HEAT_TRANSFER * mass / other.mass;
    }

    public Vec2D getVelocity() {
        return position.sub(lastPosition);
    }

    public void render(Graphics g) {
        g.setColor(new Color(Math.max(0, Math.min(1.0f, temp)), Math.max(0, Math.min(1.0f, temp * 0.3f)), 0, 0.5f));
        g.fillOval((int)(position.x - r), (int)(position.y - r), (int)(r*2), (int)(r*2));
    }


}
