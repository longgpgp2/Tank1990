package tank1990.main;

import tank1990.common.constants.GameConstants;
import tank1990.manager.MapManager;
import tank1990.objects.tanks.PlayerTank;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameInfoPanel extends JPanel {
    private static GameInfoPanel instance;
    JPanel enemyPanel, livePanel, levelPanel, leveloutSide, pointPanel;
    private JLabel livesLabel, levelLabel, levelIconLabel, pointLabel, pointsTextLabel;
    private List<JLabel> enemyLabels;
    private int points = 0;
    private int lives =1;
    public int level = 1;
    public GameInfoPanel(){
        this.setBackground(Color.GRAY);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(100, GameConstants.MAP_HEIGHT + 100));
        this.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        initEnemyPanel();
        initLivePanel();
        initLevelPanel();
        initPointPanel();
    }
    public static synchronized GameInfoPanel getInstance() {
        if (instance == null) {
            instance = new GameInfoPanel();
        }
        return instance;
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
            JLabel label1 = new JLabel(enemyIcon);
            JLabel label2 = new JLabel(enemyIcon);
            enemyLabels.add(label1);
            enemyLabels.add(label2);
            column1.add(label1);
            column2.add(label2);
        }

        enemyPanel.add(column1);
        enemyPanel.add(column2);
        add(enemyPanel);
    }
    private void initPointPanel() {
        pointPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pointPanel.setBackground(Color.GRAY);
        // Tạo JLabel cho "Points:"
        JLabel pointsTextLabel = new JLabel("Points: ", SwingConstants.CENTER);
        pointsTextLabel.setForeground(Color.BLACK);
        pointsTextLabel.setFont(new Font("Arial", Font.BOLD, 12));

        // Tạo JLabel cho giá trị của points
        pointLabel = new JLabel(String.valueOf(points), SwingConstants.CENTER);
        pointLabel.setForeground(Color.BLACK);
        pointLabel.setFont(new Font("Arial", Font.BOLD, 12));

        // Thêm cả hai JLabel vào pointPanel
        pointPanel.add(pointsTextLabel);
        pointPanel.add(pointLabel);

        // Thêm panel vào layout chính
        add(pointPanel);
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
        livesLabel = new JLabel(String.valueOf(lives), SwingConstants.CENTER);
        livesLabel.setForeground(Color.BLACK);
        columnlive1.add(iLabel);
        columnlive1.add(livesIconLabel);
        columnlive2.add(pLabel);
        columnlive2.add(livesLabel);

        livePanel.add(columnlive1);
        livePanel.add(columnlive2);
        add(livePanel);
    }

    private void initLevelPanel() {
        leveloutSide = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        leveloutSide.setBackground(Color.GRAY);
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
        add(leveloutSide);
    }
    public void removeEnemyIcon() {
        if (!enemyLabels.isEmpty()) {
            // Xóa tất cả các JLabel hiện có từ enemyPanel
            enemyPanel.removeAll();

            // Giảm số lượng enemy và cập nhật danh sách JLabel
            enemyLabels.remove(enemyLabels.size() - 1);

            // Chia lại các enemy thành 2 cột với số hàng bằng một nửa danh sách enemyLabels
            JPanel column1 = new JPanel(new GridLayout(5, 1, 0, 5));
            JPanel column2 = new JPanel(new GridLayout(5, 1, 0, 5));
            column1.setBackground(Color.GRAY);
            column2.setBackground(Color.GRAY);

            // Thêm icon vào từng cột, xen kẽ giữa các cột
            for (int i = 0; i < enemyLabels.size(); i++) {
                if (i < 5) {
                    column1.add(enemyLabels.get(i));
                } else {
                    column2.add(enemyLabels.get(i));
                }
            }
            // Thêm hai cột vào enemyPanel
            enemyPanel.add(column1);
            enemyPanel.add(column2);
            // Cập nhật lại UI
            enemyPanel.revalidate();
            enemyPanel.repaint();
            // Cập nhật lại GameInfoPanel nếu cần
            this.revalidate();
            this.repaint();
            System.out.println("Removed an enemy icon. Remaining icons: " + enemyLabels.size());
        }
    }

    public void resetEnemyPanel() {
        enemyPanel.removeAll();
        enemyLabels.clear();
        int numberOfEnemies = 10;
        ImageIcon enemyIcon = new ImageIcon(".\\src\\main\\resources\\images\\enemy.png");
        enemyLabels = new ArrayList<>();

        for (int i = 0; i < numberOfEnemies; i++) {
            JLabel enemyLabel = new JLabel(enemyIcon);
            enemyLabels.add(enemyLabel);
        }
        JPanel column1 = new JPanel(new GridLayout(5, 1, 0, 5));
        JPanel column2 = new JPanel(new GridLayout(5, 1, 0, 5));
        column1.setBackground(Color.GRAY);
        column2.setBackground(Color.GRAY);
        for (int i = 0; i < enemyLabels.size(); i++) {
            if (i < 5) {
                column1.add(enemyLabels.get(i));
            } else {
                column2.add(enemyLabels.get(i));
            }
        }
        enemyPanel.add(column1);
        enemyPanel.add(column2);

        enemyPanel.revalidate();
        enemyPanel.repaint();
        this.revalidate();
        this.repaint();
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

    public void updateLivesLabel() {
        livesLabel.setText(String.valueOf(lives));
        levelLabel.repaint(); // Vẽ lại label
        levelPanel.revalidate(); // Xác nhận layout lại nếu cần
        levelPanel.repaint(); // Vẽ lại panel chứa label
    }
    public void updatePoint(){
        PlayerTank playerTank = MapManager.getPlayerTank();
        pointLabel.setText(String.valueOf(playerTank.getPoint()));
        pointLabel.repaint(); // Vẽ lại label
        pointPanel.revalidate(); // Xác nhận layout lại nếu cần
        pointPanel.repaint(); // Vẽ lại panel chứa label
    }
    public void resetPoint() {
        points = 0;  // Đặt lại điểm số về 0
        pointLabel.setText(String.valueOf(points));  // Cập nhật label hiển thị điểm số
        pointLabel.repaint();  // Vẽ lại label
        pointPanel.revalidate();  // Xác nhận layout lại nếu cần
        pointPanel.repaint();  // Vẽ lại panel chứa label
        System.out.println("Points have been reset to 0");
    }
    public void updateLevelLabel() {
        int currentLevel = GameObject.getInstance().getCurrentLevel();
        levelLabel.setText(String.valueOf(currentLevel));
        System.out.println("Updating level label to: " + currentLevel);
        levelLabel.repaint(); // Vẽ lại label
        levelPanel.revalidate(); // Xác nhận layout lại nếu cần
        levelPanel.repaint(); // Vẽ lại panel chứa label
    }

}
