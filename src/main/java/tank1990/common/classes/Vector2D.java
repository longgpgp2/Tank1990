package tank1990.common.classes;

public class Vector2D {
  public double x;
  public double y;

  public Vector2D(double x, double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "Vector2D(x=" + x + ", y=" + y + ")";
  }

  public double dotProduct(Vector2D vector) {
    return this.x * vector.x + this.y * vector.y;
  }

  public Vector2D normalized() {
    double magnitude = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    return new Vector2D(this.x / magnitude, this.y / magnitude);
  }

  public Vector2D add(Vector2D vector) {
    return new Vector2D(this.x + vector.x, this.y + vector.y);
  }

  public Vector2D minus(Vector2D vector) {
    return new Vector2D(this.x - vector.x, this.y - vector.y);
  }

  public Vector2D multiply(double scalar) {
    return new Vector2D(x * scalar, y * scalar);
  }

  public Double crossProduct(Vector2D vector) {
    return this.x * vector.y - this.y * vector.x;
  }

  public Vector2D getTangent() {
    return new Vector2D(-this.y, this.x); // dot product = 0 => perpendicular
  }

  public boolean isZero() {
    return this.x == 0 && this.y == 0;
  }

  public boolean isNaN() {
    return Double.isNaN(x) || Double.isNaN(y);
  }

  public boolean isEqual(Vector2D vector) {
    return this.x == vector.x && this.y == vector.y;
  }

  public static double dotProduct(Vector2D vectorA, Vector2D vectorB) {
    return vectorA.x * vectorB.x + vectorA.y * vectorB.y;
  }

  public static Vector2D zero() {
    return new Vector2D(0, 0);
  }
}
