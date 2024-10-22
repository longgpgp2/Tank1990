package tank1990.main;

import javax.swing.*;

import tank1990.common.constants.GameConstants;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameObject extends JFrame {
    JPanel panel;
    private JLabel enemyLabel, livesLabel, instructionsLabel;
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

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.GRAY);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(200,GameConstants.MAP_HEIGHT));


        enemyLabel = new JLabel("Enemies: 10");
        livesLabel = new JLabel("Lives: 3");
        instructionsLabel = new JLabel("Instructions:");

        enemyLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        livesLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        enemyLabel.setForeground(Color.WHITE);
        livesLabel.setForeground(Color.WHITE);
        instructionsLabel.setForeground(Color.WHITE);

        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(enemyLabel);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(livesLabel);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(instructionsLabel);

        this.add(infoPanel, BorderLayout.EAST);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenu optionsMenu = new JMenu("Options");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem soundItem = new JMenuItem("Sound");
        JMenuItem fullScreenItem = new JMenuItem("Full Screen");
        JMenuItem instructionsItem = new JMenuItem("Instructions");
        JMenuItem aboutItem = new JMenuItem("About");

        gameMenu.add(newGameItem);
        gameMenu.add(exitItem);

        optionsMenu.add(soundItem);
        optionsMenu.add(fullScreenItem);

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
    }
//    private void resetGame() {
//        System.out.println("Game reset!");
//    }


    //    public void updateEnemies(int numEnemies) {
//        enemyLabel.setText("Enemies: " + numEnemies);
//    }
//
//    public void updateLives(int lives) {
//        livesLabel.setText("Lives: " + lives);
//    }
    public void startGame() {
        setVisible(true);
    }
}
//        panel = new GamePanel();
//        setSize(new Dimension(GameConstants.FRAME_WIDTH, GameConstants.FRAME_HEIGHT));
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.add(panel);
//        this.pack();
//        this.setLocationRelativeTo(null);
//        this.setVisible(true);


