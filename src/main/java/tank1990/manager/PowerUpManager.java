package tank1990.manager;

import tank1990.common.enums.EntityType;
import tank1990.main.GameState;
import tank1990.objects.powerups.PowerUp;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * PowerUpManager manages how power-ups are added and removed from the current game
 *
 */
public class PowerUpManager {
    private static int autoSpawnDelay = 10000;
    private static int autoRemoveDelay = 30000;

    private static ArrayList<PowerUp> powerUps = new ArrayList<>();
    private static ActionListener autoSpawnAction = null;
    private static Timer autoSpawnTimer = new Timer(autoSpawnDelay, autoSpawnAction);

    public static ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }
    private static GameState gameState = GameState.getInstance();

    /**
     * Create and add a new PowerUp
     *
     */
    public static boolean addPowerUp() {
        if (powerUps.size() < 5) { // Limit the number of power-up can be on the field
            PowerUp powerUp = MapManager.createPowerUp();
            powerUps.add(powerUp);
            GameEntityManager.addCollisionEntity(EntityType.PLAYER, powerUp);;
            
            // tạo sound mỗi khi 1 powerup được spawn
            SoundManager spawnSound = new SoundManager();
            spawnSound.soundLoader(".\\src\\main\\resources\\sounds\\spawn-sound-powerup.wav");
            if (gameState.isSoundOn()){
                spawnSound.playSound();
            }else {
                spawnSound.stopSound();
            }

            Timer autoRemoveTimer = new Timer(autoRemoveDelay, e -> {
                System.out.println("A power-up is removed after " + autoRemoveDelay + "ms");
                removePowerUp(powerUp);
            });
            autoRemoveTimer.setRepeats(false);
            autoRemoveTimer.start();
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
        GameEntityManager.removeCollisionEntity(EntityType.PLAYER, powerUp);
        powerUps.remove(powerUp);
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
     */
    public static void startAutoSpawn() {
        if (autoSpawnAction == null) {
            autoSpawnAction = e -> {
                if (addPowerUp()) {
                    System.out.println("A new power-up is added after " + autoSpawnDelay + "ms");
                } else {
                    System.out.println("Power-up list is full. Nothing happened.");
                }
            };
            autoSpawnTimer.addActionListener(autoSpawnAction);
            autoSpawnTimer.start();
        }
    }

    /**
     * Turn off auto spawn and clear the list of power-ups
     *
     */
    public static void resetPowerUps() {
        // stop auto spawn
        autoSpawnTimer.stop();
        if (autoSpawnAction != null) {
            autoSpawnTimer.removeActionListener(autoSpawnAction);
            autoSpawnTimer = null;
        }
        // remove all power-ups
        for (PowerUp powerUp : powerUps) {
            GameEntityManager.remove(powerUp);
            GameEntityManager.removeCollisionEntity(EntityType.PLAYER, powerUp);
        }
        powerUps.clear();
    }

    public static int getAutoSpawnDelay() {
        return autoSpawnDelay;
    }

    public static void setAutoSpawnDelay(int autoSpawnDelay) {
        PowerUpManager.autoSpawnDelay = autoSpawnDelay;
    }

    public static int getAutoRemoveDelay() {
        return autoRemoveDelay;
    }

    public static void setAutoRemoveDelay(int autoRemoveDelay) {
        PowerUpManager.autoRemoveDelay = autoRemoveDelay;
    }
}
