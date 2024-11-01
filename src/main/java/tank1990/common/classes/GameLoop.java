package tank1990.common.classes;

import tank1990.common.enums.GameStatus;

public abstract class GameLoop {
  protected volatile GameStatus status;
  private Thread gameThread;

  public GameLoop() {
    status = GameStatus.STOPPED;
  }

  public void run() {
    status = GameStatus.RUNNING;
    gameThread = new Thread(this::processGameLoop);
    gameThread.start();
  }

  public void stop() {
    status = GameStatus.STOPPED;
  }

  public boolean isGameRunning() {
    return status == GameStatus.RUNNING;
  }

  protected abstract void processGameLoop();
}

/*
 * References:
 * https://java-design-patterns.com/patterns/game-loop/#programmatic-example-of-
 * game-loop-pattern-in-java
 * https://gafferongames.com/post/integration_basics/
 * https://dewitters.com/dewitters-gameloop/
 */