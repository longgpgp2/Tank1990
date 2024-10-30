package tank1990.main;

import javax.swing.*;

import tank1990.common.constants.GameConstants;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GameObject extends JFrame {
    JPanel panel, infoPanel, enemyPanel, livePanel, levelPanel, leveloutSide;
    private JLabel  livesLabel, levelLabel, levelIconLabel;
    private List<JLabel> enemyLabels;
    private int lives;
    private int level;
    GameObject() {

        setTitle("Tank 1990");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(GameConstants.MAP_WIDTH +200, GameConstants.MAP_HEIGHT));
        setLayout(new BorderLayout());
        setBackground(Color.GRAY);


        panel = new GamePanel();
        panel.setBackground(Color.BLACK);
        panel.setSize(GameConstants.MAP_WIDTH,GameConstants.MAP_HEIGHT);
        this.add(panel, BorderLayout.CENTER);

        infoPanel = new JPanel();
        infoPanel.setBackground(Color.GRAY);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(200,GameConstants.MAP_HEIGHT));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // in 10 enemies ra màn hình
        enemyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        enemyPanel.setBackground(Color.GRAY);
        JPanel column1 = new JPanel(new GridLayout(5, 1, 0, 0));
        JPanel column2 = new JPanel(new GridLayout(5, 1, 0, 0));
        column1.setBackground(Color.GRAY);
        column2.setBackground(Color.GRAY);
        ImageIcon enemyIcon = new ImageIcon(".\\src\\main\\resources\\images\\enemy.png");

        enemyLabels = new ArrayList<JLabel>();
        for (int i = 0; i < 5; i++) {
            column1.add(new JLabel(enemyIcon));
            column2.add(new JLabel(enemyIcon));
        }
        enemyPanel.add(column1);
        enemyPanel.add(column2);

        livePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
        livePanel.setBackground(Color.GRAY);
        JPanel columnlive1 = new JPanel(new GridLayout(2, 1, 0, 0));
        columnlive1.setBackground(Color.GRAY);;
        JPanel columnlive2 = new JPanel(new GridLayout(2, 1, 0, 0));
        columnlive2.setBackground(Color.GRAY);

        JLabel iLabel = new JLabel("I", SwingConstants.CENTER);
        JLabel pLabel = new JLabel("P", SwingConstants.CENTER);
        ImageIcon livesIcon = new ImageIcon(".\\src\\main\\resources\\images\\lives.png");
        JLabel livesIconLabel = new JLabel(livesIcon);
        lives =3;
        livesLabel = new JLabel(String.valueOf(lives), SwingConstants.CENTER);
        livesLabel.setForeground(Color.BLACK);
        columnlive1.add(iLabel);
        columnlive1.add(livesIconLabel);
        columnlive2.add(pLabel);
        columnlive2.add(livesLabel);

        livePanel.add(columnlive1);
        livePanel.add(columnlive2);

        leveloutSide = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
        leveloutSide.setBackground(Color.GRAY);
        level = 1;
        levelPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        levelPanel.setBackground(Color.GRAY);

        ImageIcon levelIcon = new ImageIcon(".\\src\\main\\resources\\images\\flag.png");
        levelIconLabel = new JLabel(levelIcon);
        Image img = levelIcon.getImage();
        Image scaledImg = img.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        levelIconLabel = new JLabel(new ImageIcon(scaledImg));
        levelLabel = new JLabel(String.valueOf(level), SwingConstants.CENTER);
        levelLabel.setForeground(Color.BLACK);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 15));
        levelPanel.add(levelIconLabel);
        levelPanel.add(levelLabel);
        leveloutSide.add(levelPanel);

        infoPanel.add(enemyPanel);
        infoPanel.add(livePanel);
        infoPanel.add(leveloutSide);

        this.add(infoPanel, BorderLayout.EAST);

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

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        soundItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // turn off backgroundMusic
            }
        });
    }
    // xóa bỏ 1 icon khi 1 enemy bị giết
    public void removeEnemyIcon() {
        if (!enemyLabels.isEmpty()) {
            JLabel enemyLabel = enemyLabels.remove(enemyLabels.size() - 1); // Lấy enemy cuối cùng
            enemyPanel.remove(enemyLabel);
            enemyPanel.revalidate(); // Cập nhật bố cục
            enemyPanel.repaint(); // Vẽ lại giao diện
        }
    }
    public JPanel getEnemyPanel() {
        return enemyPanel;
    }

    // tăng giảm số mạng
    public void increaseLives() {
        if (lives < 10) { // Giới hạn số mạng tối đa là 10
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
        livesLabel.setText(String.valueOf(lives)); // Cập nhật số mạng hiển thị
    }
    public JPanel getLivesPanel() {
        return livePanel;
    }

    // tăng cấp
    public void increaseLevel() {
        level++;
        updateLevelLabel();
    }
    private void updateLevelLabel() {
        levelLabel.setText(String.valueOf(level)); // Cập nhật cấp độ hiển thị
    }



//    private void resetGame() {
//        System.out.println("Game reset!");
//    }

    public void startGame() {
        setVisible(true);
    }
}


