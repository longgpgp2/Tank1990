package tank1990.main;

import javax.swing.*;
import tank1990.common.constants.GameConstants;
import tank1990.manager.SoundManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import tank1990.common.classes.GameEntity;
import tank1990.common.classes.GameLoop;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;
import tank1990.manager.GameEntityManager;
public class GameObject extends GameLoop implements Runnable {
    private GameFrame gameFrame;

    public GameObject() {
        gameFrame = new GameFrame();
    }
    public void startGame() {
        GameEntityManager.setCollisionEntities(EntityType.ENEMY, GameConstants.IMPASSABLE_ENTITIES);
        GameEntityManager.setCollisionEntities(EntityType.PLAYER, GameConstants.PLAYER_IMPASSABLE_ENTITIES);
        GameEntityManager.setCollisionEntities(EntityType.BULLET, new EntityType[] {
                EntityType.BRICK,
                EntityType.STEEL,
                EntityType.ENEMY,
                EntityType.PLAYER

        });
        run();
    }
    @Override
    protected void processGameLoop() {
        long initalFrameTime = System.nanoTime();
        double lastFrameTime = 0.0;

        while (isGameRunning()) {
            double currentFrameTime = (System.nanoTime() - initalFrameTime) / 1E9;
            double deltaTime = currentFrameTime - lastFrameTime;

            update(deltaTime);
            render();

            lastFrameTime = currentFrameTime;
        }
    }
    protected void render() {
        gameFrame.draw();
    }

    protected void update(double deltaTime) {
        try {
            for (GameEntity gameEntity : GameEntityManager.getGameEntities()) {
                gameEntity.update(deltaTime);
            }
        } catch (Exception e) {
            // System.out.println(e);
        }
    }



}