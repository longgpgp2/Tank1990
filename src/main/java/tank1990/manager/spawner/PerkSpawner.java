package tank1990.manager.spawner;

import tank1990.common.classes.Vector2D;
import tank1990.objects.powerups.PowerUp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PerkSpawner {
    public static PowerUp createPowerUp(String className, Vector2D position){
        try {
            int x = (int) position.x;
            int y = (int) position.y;
            String classPath = "tank1990.objects.powerups."+className;
            Class<?> clazz = Class.forName(classPath);
            System.out.println(clazz.getName());
            Class<?>[] paramTypes = { int.class, int.class };
            Constructor<?> constructor = clazz.getConstructor(paramTypes);
//            System.out.println(constructor.getName());
            return (PowerUp) constructor.newInstance(x, y);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        PowerUp powerUp = createPowerUp("Star", new Vector2D(5 ,5));
        System.out.println(powerUp);
    }

}
