package physics;

public class Vec2D {
    public float x, y;

    public Vec2D() {
        x = 0.0f;
        y = 0.0f;
    }

    public Vec2D (float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2D (Vec2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vec2D add (float x, float y) {
        return new Vec2D(this.x + x, this.y + y);
    }

    public Vec2D add (Vec2D v) {
        return new Vec2D(this.x + v.x, this.y + v.y);
    }

    public Vec2D sub (float x, float y) {
        return new Vec2D(this.x - x, this.y - y);
    }

    public Vec2D sub (Vec2D v) {
        return new Vec2D(this.x - v.x, this.y - v.y);
    }

    public Vec2D mult (float s) {
        return new Vec2D(x * s, y * s);
    }

    public Vec2D div (float s) {
        return new Vec2D(x / s, y / s);
    }

    public float mag2 () {
        return x*x + y*y;
    }

    public float mag () {
        return (float)Math.sqrt(mag2());
    }

    public Vec2D normalize () {
        return div(mag());
    }

    public void copy(Vec2D vec) {
        x = vec.x;
        y = vec.y;
    }

    public boolean isNaN() {
        return Float.isNaN(x) || Float.isNaN(y);
    }

    public String toString() {
        return "{ x: " + x + ", y: " + y + " }";
    }

}
