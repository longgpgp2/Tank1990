package tank1990.main;

import tank1990.manager.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class OptionsMenu extends JPanel implements KeyListener {
    private int currentSelection =0;
    private Image logoImage;
    private String[] optionsItems = {"Fullscreen", "Sound", "Exit"};
    private SoundManager backgroundMusic;

    private JFrame parentFrame ;
    private GameState gameState;
    private boolean isFullScreen ;
    private boolean isSoundOn ;

    public OptionsMenu(JFrame fr, SoundManager backgroundMusic){
        this.parentFrame = fr;
        this.backgroundMusic = backgroundMusic;
        this.gameState=GameState.getInstance();
        this.isSoundOn= gameState.isSoundOn();
        this.isFullScreen=gameState.isFullScreen();
        setFocusable(true);
        addKeyListener(this);
        ImageIcon icon = new ImageIcon(".\\src\\main\\resources\\images\\battle_city.png");
        logoImage = icon.getImage();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (logoImage != null){
            g.drawImage(logoImage, getWidth()/ 2 - logoImage.getWidth(null) / 2, 100, null);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Options", getWidth() / 2 - 90, 300);

        g.setFont(new Font("Arial", Font.PLAIN, 28));
        for (int i = 0; i < optionsItems.length; i++) {
            if (i == currentSelection) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.WHITE);
            }
            g.drawString(optionsItems[i], getWidth() / 2 - 100, 350 + i * 50);
        }

        g.setColor(Color.WHITE);
        g.drawString( (isFullScreen ? "On" : "Off"), getWidth() / 2 + 100, 350);
        g.drawString( (isSoundOn ? "On" : "Off"), getWidth() / 2 +100, 400);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            currentSelection--;
            if (currentSelection < 0) {
                currentSelection = optionsItems.length - 1;
            }
        } else if (key == KeyEvent.VK_S) {
            currentSelection++;
            if (currentSelection >= optionsItems.length) {
                currentSelection = 0;
            }
        } else if (key == KeyEvent.VK_ENTER) {
            selectOption();
        }
        repaint();
    }

    private void selectOption(){
        if (currentSelection ==0){
            isFullScreen =!isFullScreen;
            gameState.setFullScreen(isFullScreen);
        }else if (currentSelection == 1){
            isSoundOn = !isSoundOn;
            gameState.setSoundOn(isSoundOn);
            backgroundMusic.setSoundOn(isSoundOn);
        }else if (currentSelection == 2){
            System.out.println("Returning to main menu");
            parentFrame.dispose();
            openMainMenu();
        }

    }
    private void openMainMenu() {
        JFrame menuFrame = new JFrame("Tank 1990");
        Menu menu = new Menu(menuFrame);
        menuFrame.add(menu);
        menuFrame.setSize(800, 600);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
