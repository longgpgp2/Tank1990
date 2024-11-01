package tank1990.common.classes;

public class AABB {
  public Vector2D center;
  public Vector2D extents;

  public Vector2D min;
  public Vector2D max;
  public Vector2D size;

  public Vector2D getMin() {
    return new Vector2D(center.x - extents.x, center.y - extents.y);
  }

  public Vector2D getMax() {
    return new Vector2D(center.x + extents.x, center.y + extents.y);
  }

  public Vector2D getSize() {
    return new Vector2D(extents.x * 2, extents.y * 2);
  }

  public String toString() {
    return "AABB(x=" + min.x + ", y=" + min.y + ", width=" + size.x + ", height=" + size.y + ", min=" + min + ", max="
        + max + ")";
  }

  public AABB(Vector2D center, Vector2D extents) {
    this.center = center;
    this.extents = extents;

    this.min = getMin();
    this.max = getMax();
    this.size = getSize();
  }

  public AABB(CollisionBox collisionBox) {
    this.center = collisionBox.center;
    this.extents = collisionBox.extents;

    this.min = getMin();
    this.max = getMax();
    this.size = new Vector2D(collisionBox.width, collisionBox.height);
  }

  public void setCenter(Vector2D position) {
    this.center = position;
    this.min = getMin();
    this.max = getMax();
  }

  public AABB minkowskiDifference(AABB other) {
    Vector2D topLeft = min.minus(other.max);
    Vector2D fullSize = size.add(other.size);
    return new AABB(topLeft.add(fullSize.multiply(0.5)), fullSize.multiply(0.5));
  }

  private Double getRayIntersectionFractionOfFirstRay(Vector2D originA, Vector2D endA, Vector2D originB,
      Vector2D endB) {
    Vector2D r = endA.minus(originA);
    Vector2D s = endB.minus(originB);

    Double numerator = (originB.minus(originA)).crossProduct(r);
    Double denominator = r.crossProduct(s);

    if (numerator == 0 && denominator == 0) {
      return Double.POSITIVE_INFINITY;
    }
    if (denominator == 0) {
      return Double.POSITIVE_INFINITY;
    }

    Double u = numerator / denominator;
    Double t = ((originB.minus(originA)).crossProduct(s)) / denominator;

    if ((t >= 0) && (t <= 1) && (u >= 0) && (u <= 1)) {
      return t;
    }
    return Double.POSITIVE_INFINITY;
  }

  public Double getRayIntersectionFraction(Vector2D origin, Vector2D direction) {
    Vector2D end = origin.add(direction);
    Double minT = getRayIntersectionFractionOfFirstRay(origin, end, new Vector2D(min.x, min.y),
        new Vector2D(min.x, max.y));
    Double x;
    x = getRayIntersectionFractionOfFirstRay(origin, end, new Vector2D(min.x, max.y), new Vector2D(max.x, max.y));
    if (x < minT)
      minT = x;
    x = getRayIntersectionFractionOfFirstRay(origin, end, new Vector2D(max.x, max.y), new Vector2D(max.x, min.y));
    if (x < minT)
      minT = x;
    x = getRayIntersectionFractionOfFirstRay(origin, end, new Vector2D(max.x, min.y), new Vector2D(min.x, min.y));
    if (x < minT)
      minT = x;
    return minT;
  }

  public Vector2D getOverlappingOffsetDirection(AABB other, double offsetX, double offsetY) {
    Vector2D direction = this.center.minus(other.center).normalized();
    double newOffsetX = offsetX;
    double newOffsetY = offsetY;

    if (offsetX > offsetY) {
      newOffsetX = 0;
      newOffsetY = 1;
    }

    if (offsetY > offsetX) {
      newOffsetX = 1;
      newOffsetY = 0;
    }

    return new Vector2D(direction.x * newOffsetX, direction.y * newOffsetY);
  }

}
