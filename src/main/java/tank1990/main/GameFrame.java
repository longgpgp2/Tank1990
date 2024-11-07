package tank1990.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import tank1990.common.constants.GameConstants;
import tank1990.manager.SoundManager;


public class GameFrame extends JFrame {
  public GamePanel gamePanel;
  private SoundManager backgroundMusic;
  private GameState gameState;
  JPanel panel, infoPanel, enemyPanel, livePanel, levelPanel, leveloutSide, pointPanel;
  private JLabel livesLabel, levelLabel, levelIconLabel, pointLabel;
  private List<JLabel> enemyLabels;
  private int points =0;
  private int lives;
  private int level;


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

    infoPanel = new JPanel();
    infoPanel.setBackground(Color.GRAY);
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    infoPanel.setPreferredSize(new Dimension(100, GameConstants.MAP_HEIGHT + 100));
    infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

    // Khởi tạo các panel
    initEnemyPanel();
    initLivePanel();
    initLevelPanel();
    initPointPanel();

    this.add(infoPanel, BorderLayout.EAST);
    createMenuBar();
  }

  private void initEnemyPanel() {
    enemyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    enemyPanel.setBackground(Color.GRAY);
    JPanel column1 = new JPanel(new GridLayout(5, 1, 0, 0));
    JPanel column2 = new JPanel(new GridLayout(5, 1, 0, 0));
    column1.setBackground(Color.GRAY);
    column2.setBackground(Color.GRAY);
    ImageIcon enemyIcon = new ImageIcon(".\\src\\main\\resources\\images\\enemy.png");

    enemyLabels = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      column1.add(new JLabel(enemyIcon));
      column2.add(new JLabel(enemyIcon));
    }
    enemyPanel.add(column1);
    enemyPanel.add(column2);
    infoPanel.add(enemyPanel);
  }
  private void initPointPanel() {
    pointPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    pointPanel.setBackground(Color.GRAY);
    points = 0;
    pointLabel = new JLabel("Points: " + points, SwingConstants.CENTER);
    pointLabel.setForeground(Color.BLACK);
    pointLabel.setFont(new Font("Arial", Font.BOLD, 14));
    pointPanel.add(pointLabel);
    infoPanel.add(pointPanel);
  }

  private void initLivePanel() {
    livePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    livePanel.setBackground(Color.GRAY);
    JPanel columnlive1 = new JPanel(new GridLayout(2, 1, 0, 0));
    JPanel columnlive2 = new JPanel(new GridLayout(2, 1, 0, 0));
    columnlive1.setBackground(Color.GRAY);
    columnlive2.setBackground(Color.GRAY);

    JLabel iLabel = new JLabel("I", SwingConstants.CENTER);
    JLabel pLabel = new JLabel("P", SwingConstants.CENTER);
    ImageIcon livesIcon = new ImageIcon(".\\src\\main\\resources\\images\\lives.png");
    JLabel livesIconLabel = new JLabel(livesIcon);
    lives = 3;
    livesLabel = new JLabel(String.valueOf(lives), SwingConstants.CENTER);
    livesLabel.setForeground(Color.BLACK);
    columnlive1.add(iLabel);
    columnlive1.add(livesIconLabel);
    columnlive2.add(pLabel);
    columnlive2.add(livesLabel);

    livePanel.add(columnlive1);
    livePanel.add(columnlive2);
    infoPanel.add(livePanel);
  }

  private void initLevelPanel() {
    leveloutSide = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    leveloutSide.setBackground(Color.GRAY);
    level = 1;
    levelPanel = new JPanel(new GridLayout(2, 1, 0, 0));
    levelPanel.setBackground(Color.GRAY);

    ImageIcon levelIcon = new ImageIcon(".\\src\\main\\resources\\images\\flag.png");
    levelIconLabel = new JLabel(levelIcon);
    levelLabel = new JLabel(String.valueOf(level), SwingConstants.CENTER);
    levelLabel.setForeground(Color.BLACK);
    levelLabel.setFont(new Font("Arial", Font.BOLD, 15));
    levelPanel.add(levelIconLabel);
    levelPanel.add(levelLabel);
    leveloutSide.add(levelPanel);
    infoPanel.add(leveloutSide);
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

    newGameItem.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                      resetGame();
                                    }
                                  }
    );
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

  public void removeEnemyIcon() {
    if (!enemyLabels.isEmpty()) {
      JLabel enemyLabel = enemyLabels.remove(enemyLabels.size() - 1);
      enemyPanel.remove(enemyLabel);
      enemyPanel.revalidate();
      enemyPanel.repaint();
    }
  }

  public void increaseLives() {
    if (lives < 10) {
      lives++;
      updateLivesLabel();
    }
  }

  public void decreaseLives() {
    if (lives > 0) {
      lives--;
      updateLivesLabel();
    }
  }

  private void updateLivesLabel() {
    livesLabel.setText(String.valueOf(lives));
  }

  public void increaseLevel() {
    level++;
    updateLevelLabel();
  }

  private void updateLevelLabel() {
    levelLabel.setText(String.valueOf(level));
  }

  private void resetGame() {
    System.out.println("Game reset!");

  }
  public void draw() {
    repaint();
  }

}
