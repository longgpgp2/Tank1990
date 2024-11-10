package tank1990.manager.spawner;

import java.lang.reflect.Constructor;

import tank1990.common.classes.Vector2D;
import tank1990.objects.powerups.PowerUp;

public class PerkSpawner {
    public static PowerUp createPowerUp(String className, Vector2D position) {
        try {
            int x = (int) position.x;
            int y = (int) position.y;
            String classPath = "tank1990.objects.powerups." + className;
            Class<?> clazz = Class.forName(classPath);
            Class<?>[] paramTypes = { int.class, int.class };
            Constructor<?> constructor = clazz.getConstructor(paramTypes);
            return (PowerUp) constructor.newInstance(x, y);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
