package tank1990.objects.tanks;

import tank1990.common.enums.Direction;

public abstract class EnemyTank extends Tank {
    private String name;
    private int point;
    private String specialTraits;

    public EnemyTank(String name, int health, int point, int bulletSpeed, int movementSpeed, String specialTraits) {
        super(health, bulletSpeed, movementSpeed, Direction.DOWN);
        this.name = name;
        this.point = point;
        this.specialTraits = specialTraits;
    }

    @Override
    public Bullet shoot() {
        System.out.println(name + " is shooting!");

        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialTraits() {
        return specialTraits;
    }

    public void setSpecialTraits(String specialTraits) {
        this.specialTraits = specialTraits;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "EnemyTank{" +
                "name='" + name + '\'' +
                ", health=" + this.getHealth() +
                ", bulletSpeed=" + this.getBulletSpeed() +
                ", movementSpeed=" + this.getMovementSpeed() +
                ", point=" + point +
                ", specialTraits=" + specialTraits +
                '}';
    }
}
