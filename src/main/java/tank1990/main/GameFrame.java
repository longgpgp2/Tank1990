package tank1990.main;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import tank1990.common.constants.GameConstants;

public class GameFrame extends JFrame {
  public GamePanel gamePanel;

  public GameFrame() {
    gamePanel = new GamePanel();

    setSize(new Dimension(GameConstants.FRAME_WIDTH, GameConstants.FRAME_HEIGHT));
    this.setTitle("Battle City");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.add(gamePanel);
    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }
}
