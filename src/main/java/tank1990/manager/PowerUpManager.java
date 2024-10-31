package tank1990.manager;

import tank1990.main.GamePanel;
import tank1990.objects.environments.Environment;
import tank1990.objects.powerups.PowerUp;
import tank1990.objects.tanks.Tank;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PowerUpManager manages how power-ups are added and removed from the current game
 *
 */
public class PowerUpManager {
    private static final int AUTO_SPAWN_DELAY = 10000;
    private static final int AUTO_REMOVE_DELAY = 5000;

    private static ArrayList<PowerUp> powerUps = new ArrayList<>();
    private static Timer autoSpawnTimer = new Timer(AUTO_SPAWN_DELAY, null);

    public static ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }

    /**
     * Create and add a new PowerUp
     *
     * @param environments A List of Environment components
     * @param tanks A List of Tank components
     */
    public static boolean addPowerUp(List<Environment> environments, List<Tank> tanks) {
        if (powerUps.size() < 5) { // Limit the number of power-up can be on the field
            PowerUp powerUp = MapManager.createPowerUp(environments, tanks);
            powerUps.add(powerUp);
            System.out.println("Add a power-up");
            Timer autoRemoveTimer = new Timer(AUTO_REMOVE_DELAY, e -> {
                System.out.println("A power-up is removed after " + AUTO_REMOVE_DELAY + "ms");
                removePowerUp(powerUp);
            });
            autoRemoveTimer.setRepeats(false);
            autoRemoveTimer.start();
            System.out.println(powerUps.size());
            return true;
        }
        return false;
    }

    /**
     * Remove a PowerUp
     *
     * @param powerUp The PowerUp that needs to be removed
     */
    private static void removePowerUp(PowerUp powerUp) {
        // disable collision
        powerUp.getCollision().setEnabled(false);
        // remove power-up from the field
        GameEntityManager.remove(powerUp);
        GamePanel.removeEntity(powerUp);
    }

    /**
     * Trigger the effect(s) of the given PowerUp
     *
     * @param powerUp
     */
    public static void triggerPowerUp(PowerUp powerUp) {
        // remove power-up
        removePowerUp(powerUp);
        // trigger the effect of power-up
        powerUp.activate();
    }

    /**
     * Occasionally trigger addPowerUp() after some time has passed
     *
     * @param environments A List of Environment components
     * @param tanks A List of Tank components
     */
    public static void startPowerUpTimer(List<Environment> environments, List<Tank> tanks) {
        autoSpawnTimer.addActionListener(e -> {
            if (addPowerUp(environments, tanks)) {
                System.out.println("A new power-up is added after " + AUTO_SPAWN_DELAY + "ms");
            } else {
                System.out.println("Power-up list is full. Nothing happened.");
            }
        });
        autoSpawnTimer.start();
    }
}
