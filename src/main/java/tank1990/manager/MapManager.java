package tank1990.manager;

import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;
import tank1990.common.utils.CollisionUtil;
import tank1990.main.GamePanel;
import tank1990.manager.spawner.PerkSpawner;
import tank1990.objects.environments.*;
import tank1990.objects.powerups.PowerUp;
import tank1990.objects.tanks.PlayerTank;
import tank1990.objects.tanks.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MapManager {

    // truyền 1 set các unoccupied indices vào để check xem index và 3 indices xung
    // quanh có trống hay không
    public static boolean checkIndexAvailability(int index) {
        Set<Integer> unoccupiedIndices = getUnoccupiedIndex();
        Set<Integer> requiredIndices = new HashSet<>();
        requiredIndices.add(index);
        requiredIndices.add(index + 1);
        requiredIndices.add(index + 33);
        requiredIndices.add(index + 34);
        if (!unoccupiedIndices.containsAll(requiredIndices)) {
            System.out.println("Index " + index + " is occupied!");
            return false;
        }
        return true;
    }

    public static Set<Integer> getUnoccupiedIndex() {
        List<Environment> environments = GamePanel.getEnvironments();
        List<Tank> tanks = Tank.getTanks();
        Set<Integer> occupiedIndices = new HashSet<>();
        if (tanks != null)
            for (Tank tank : tanks) {
                int index = CollisionUtil.getTileIndex(tank.getPosition());
                occupiedIndices.add(index);
            }
        if (environments != null)
            for (Environment env : environments) {
                int index = CollisionUtil.getTileIndex(env.getPosition());
                occupiedIndices.add(index);
            }
        for (PowerUp pow : PowerUpManager.getPowerUps()) {
            int index = CollisionUtil.getTileIndex(pow.getPosition());
            occupiedIndices.add(index);
            occupiedIndices.add(index + 1);
            occupiedIndices.add(index + 33);
            occupiedIndices.add(index + 34);
        }
        Set unoccupiedIndices = new HashSet();
        for (int i = 0; i < 33 * 33; i++) {
            unoccupiedIndices.add(i);
        }
        unoccupiedIndices.removeAll(occupiedIndices);
        return unoccupiedIndices;
    }

    public static PowerUp createPowerUp() {
        Set<Integer> unoccupiedIndices = getUnoccupiedIndex();
        Integer[] unoccupiedIndicesAsArray = unoccupiedIndices.toArray(new Integer[0]);
        Random random = new Random();
        int randomArrayIndex;
        Integer randomIndex;
        // the power-up will be within a specific boundary
        do {
            randomArrayIndex = random.nextInt(unoccupiedIndicesAsArray.length);
            randomIndex = unoccupiedIndicesAsArray[randomArrayIndex];
        } while (!checkIndexAvailability(randomIndex) || ((randomIndex < 99 || randomIndex >= 990) || // vertical
                                                                                                      // boundary [3 *
                                                                                                      // 33, 30 * 33]
                (randomIndex % 33 < 3 || randomIndex % 33 >= 30) // horizontal boundary [3, 30]
        ));
        Vector2D powerupPosition = CollisionUtil.getPositionByIndex(randomIndex, GameConstants.ENTITY_WIDTH,
                GameConstants.ENTITY_HEIGHT);

        randomArrayIndex = random.nextInt(GameConstants.PERK_LIST.length);
        String perkName = GameConstants.PERK_LIST[randomArrayIndex];

        PowerUp powerUp = PerkSpawner.createPowerUp(perkName, powerupPosition);
        // System.out.println(powerUp);
        return powerUp;

    }

    public static void drawPowerUp(PowerUp powerUp, Graphics g, ImageObserver observer) {
        g.drawImage(powerUp.image, (int) (powerUp.getPosition().x), (int) (powerUp.getPosition().y), powerUp.width,
                powerUp.height, observer);
    }

    public static void drawTanks(Graphics g, ImageObserver observer) {
        List<Tank> tanks = Tank.getTanks();
        for (Tank tank : tanks) {
            g.drawImage(tank.image, (int) (tank.getPosition().x), (int) (tank.getPosition().y), tank.width, tank.height,
                    observer);
            ImageIcon currentShieldFrame;
            if (tank.isShielded() && (currentShieldFrame = tank.getShield().getCurrentFrame()) != null) {
                g.drawImage(currentShieldFrame.getImage(), (int) tank.getPosition().x, (int) tank.getPosition().y,
                        null);
            }
        }
    }

    public static PlayerTank getPlayerTank() {
        List<Tank> tanks = Tank.getTanks();
        for (Tank tank : tanks) {
            if (tank.getType() == EntityType.PLAYER) {
                return (PlayerTank) tank;
            }
        }
        return null;
    }
    public static Base getBase() {
        Base base = null;
        if (GameEntityManager.getGameEntity(EntityType.BASE).length > 0) {
            base = (Base) GameEntityManager.getGameEntity(EntityType.BASE)[0];
        }


        return base;
    }


    // Đọc từ level và tạo ra map tương ứng
    public static List<Environment> generateEnvironments(int level) {
        List<Environment> envs = new ArrayList();
        List<Integer> map = readLevel(level);
        for (int i = 0; i < map.size(); i++) {
            int envX = (i % 33) * (GameConstants.ENTITY_WIDTH);
            int envY = (i / 33) * (GameConstants.ENTITY_HEIGHT);
            if (map.get(i) != 0) {
                Environment env = null;
                switch (map.get(i)) {
                    case 1: {
                        env = new BrickWall(envX, envY);
                        break;
                    }
                    case 2: {
                        env = new SteelWall(envX, envY);
                        break;
                    }
                    case 3: {
                        env = new Base(envX, envY);
                        break;
                    }
                    case 4: {
                        env = new Water(envX, envY);
                        break;
                    }
                    case 5: {
                        env = new Trees(envX, envY);
                        break;
                    }
                    case 6: {
                        env = new Border(envX, envY);
                        break;
                    }
                    case 9: {
                        env = new BaseWall(envX, envY);
                        break;
                    }
                    default: {
                        break;
                    }
                }
                if (env != null) {
                    env.setPosition(new Vector2D(envX, envY));
                    if (!env.crossable && env.getType().equals(EntityType.BASE))
                        env.setCollision(new CollisionBox(env, new Vector2D(0, 0), env.width * 2, env.height * 2));
                    if (!env.crossable)
                        env.setCollision(new CollisionBox(env, new Vector2D(0, 0), env.width, env.height));
                    envs.add(env);
                }
            }
        }
        return envs;
    }

    // Vẽ environment bằng Graphics
    public static void drawEnvironments(Graphics g, ImageObserver observer) {
        List<Environment> envs = GamePanel.getEnvironments();
        for (Environment env : envs) {
            g.setColor(Color.GRAY);
            int width = GameConstants.ENTITY_WIDTH;
            int height = GameConstants.ENTITY_HEIGHT;
            if (env.getType().equals(EntityType.BASE)) {
                width = width * 2;
                height = height * 2;
            }
            g.drawImage(env.image, (int) env.getPosition().x, (int) env.getPosition().y,
                    width, height, observer);
        }
    }

    // Đọc file battlefield.map của level truyền vào để tạo ra 1 list các integer
    // tương ứng với từng environment object
    public static List<Integer> readLevel(int level) {
        List map = new ArrayList<Integer>();
        File file = new File(".\\src\\main\\resources\\levels\\" + level + "\\battlefield.map");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = (br.readLine())) != null) {
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (Character.isDigit(c)) {
                        map.add(Integer.parseInt(String.valueOf(c)));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
