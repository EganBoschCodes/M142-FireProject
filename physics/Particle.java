package physics;

import main.Main;

import java.awt.*;

public class Particle {
    public Vec2D position;
    private Vec2D lastPosition;
    public float r;
    public float mass = 1.0f;
    public float temp = 0.0f;


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
    public void accelerate(Vec2D a) {
        acceleration = acceleration.add(a);
    }

    public void move() {
        Vec2D velocity = position.sub(lastPosition).mult(0.9995f);
        lastPosition.copy(position);

        position = position.add(velocity.add(acceleration.mult(PhysicsSolver.DT * PhysicsSolver.DT)));

        acceleration = new Vec2D(0, 0);
    }

    public void constrain() {
        position.x = Math.max(r, Math.min(Main.SCREEN_SIZE.width - r, position.x));
        position.y = Math.min(Main.SCREEN_SIZE.height - r, position.y);
    }

    public void heat() {
        if (position.y < r * 5) temp *= 0.99;

        if (position.y > Main.SCREEN_SIZE.height - r * 4 && Math.abs(position.x - Main.SCREEN_SIZE.width/2) < Main.SCREEN_SIZE.width/3) temp += PhysicsSolver.HEATER_TEMPERATURE * (Math.random() * 10);

        //float dist2 = position.sub(PhysicsSolver.HEATER_POSITION).mag2();
        //if (dist2 < PhysicsSolver.HEATER_RADIUS * PhysicsSolver.HEATER_RADIUS)
            //temp += (float)Math.sqrt(PhysicsSolver.HEATER_RADIUS * PhysicsSolver.HEATER_RADIUS - dist2) / PhysicsSolver.HEATER_RADIUS * PhysicsSolver.HEATER_TEMPERATURE;


        temp *= 1 - Math.min(1, PhysicsSolver.HEAT_DISSIPATION / (Math.max(position.y / 100, 0) + 10) * PhysicsSolver.DT);
    }

    public boolean bounce(Particle other) {
        if (position.isNaN() || other.position.isNaN()) return false;

        float distSquared = position.sub(other.position).mag2();

        if (distSquared > r * r + 2 * r * other.r + other.r * other.r) return false;

        float dist = (float)Math.sqrt(distSquared);
        Vec2D diff = position.sub(other.position).div(dist);

        float lerpFactor = other.mass / (mass + other.mass);
        float shift = r + other.r - dist;

        position = position.add(diff.mult(lerpFactor * shift));
        other.position = other.position.sub(diff.mult((1 - lerpFactor) * shift));

        float tempDiff = other.temp - temp;

        temp += tempDiff * PhysicsSolver.HEAT_TRANSFER / PhysicsSolver.COLLISION_SUBSTEPS * other.mass / mass;
        other.temp -= tempDiff * PhysicsSolver.HEAT_TRANSFER / PhysicsSolver.COLLISION_SUBSTEPS * mass / other.mass;
        return true;
    }

    public Vec2D getVelocity() {
        return position.sub(lastPosition);
    }

    public void render(Graphics g) {
        g.setColor(new Color(Math.max(0, Math.min(1.0f, temp * 0.5f)), Math.max(0, Math.min(1.0f, temp * 0.15f)), 0));
        g.fillOval((int)(position.x - r), (int)(position.y - r), (int)(r*2), (int)(r*2));
    }


}
