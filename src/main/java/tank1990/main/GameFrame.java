package tank1990.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;
import tank1990.manager.GameEntityManager;
import tank1990.manager.SoundManager;
import tank1990.objects.tanks.PlayerTank;


public class GameFrame extends JFrame {

  private SoundManager backgroundMusic;
  private GameState gameState;
  JPanel panel, infoPanel;



  public GameFrame() {
    gameState = GameState.getInstance();
    this.backgroundMusic = new SoundManager();
    this.backgroundMusic.soundLoader(".\\src\\main\\resources\\sounds\\gameMusicTheme.wav");
    if (gameState.isSoundOn()) {
      backgroundMusic.playLoop();
    } else {
      backgroundMusic.stopSound();
    }
    setTitle("Tank 1990");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(new Dimension(GameConstants.MAP_WIDTH + 114, GameConstants.MAP_HEIGHT + 60));
    setLayout(new BorderLayout());
    add(GameInfoPanel.getInstance(), BorderLayout.EAST);
    setLocationRelativeTo(null);
    setResizable(false);
    setBackground(Color.GRAY);
    setVisible(true);

    // khởi tạo giao diện
    initUI();
  }
  private void initUI() {
    panel = new GamePanel();
    panel.setBackground(Color.BLACK);
    panel.setSize(GameConstants.MAP_WIDTH, GameConstants.MAP_HEIGHT);
    this.add(panel, BorderLayout.CENTER);


    // Khởi tạo các panel

    infoPanel = new GameInfoPanel();
    this.add(infoPanel, BorderLayout.EAST);
    createMenuBar();
  }


  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JMenu gameMenu = new JMenu("Game");
    JMenu optionsMenu = new JMenu("Options");
    JMenu helpMenu = new JMenu("Help");

    JMenuItem newGameItem = new JMenuItem("New Game");
    JMenuItem exitItem = new JMenuItem("Exit");
    JMenuItem soundItem = new JMenuItem("Sound");
    JMenuItem instructionsItem = new JMenuItem("Instructions");
    JMenuItem aboutItem = new JMenuItem("About");

    gameMenu.add(newGameItem);
    gameMenu.add(exitItem);
    optionsMenu.add(soundItem);
    helpMenu.add(instructionsItem);
    helpMenu.add(aboutItem);

    menuBar.add(gameMenu);
    menuBar.add(optionsMenu);
    menuBar.add(helpMenu);
    this.setJMenuBar(menuBar);

    newGameItem.addActionListener(e ->resetGame());
    exitItem.addActionListener(e -> System.exit(0));
    soundItem.addActionListener(e -> toggleSound());
    instructionsItem.addActionListener(e -> showInstructions());
    aboutItem.addActionListener(e -> showAbout());
  }

  private void toggleSound() {
    gameState.setSoundOn(!gameState.isSoundOn());
    if (gameState.isSoundOn()) {
      backgroundMusic.playLoop();
    } else {
      backgroundMusic.stopSound();
    }
  }

  private void showInstructions() {
    String instructions = "<html>"
            + "<h2>Instructions: </h2>"
            + "<p>Movement buttons:</p>"
            + "<ul>"
            + "<li><strong>W</strong>: UP</li>"
            + "<li><strong>A</strong>: LEFT</li>"
            + "<li><strong>S</strong>: DOWN</li>"
            + "<li><strong>D</strong>: RIGHT</li>"
            + "</ul>"
            + "<p>Click <strong>Space</strong> to shoot!</p>"
            + "</html>";
    JOptionPane.showMessageDialog(this, instructions, "Instruction", JOptionPane.INFORMATION_MESSAGE);
  }

  private void showAbout() {
    String message = "<html><h2>Group 8</h2>"
            + "2101040009: Trần Đình Khánh An<br>"
            + "2101040088: Phạm Đức Hiếu<br>"
            + "2101040115: Nguyễn Thành Long<br>"
            + "2101040166: Bùi Trọng Thành<br>"
            + "2101040192: Nguyễn Nhật Trang</html>";
    JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
  }



  private void resetGame() {
    System.out.println("Game reset!");
    GameObject.getInstance().eraseGame();
    GameObject.getInstance().newGame();
  }
  public void draw() {
    repaint();
  }

}
