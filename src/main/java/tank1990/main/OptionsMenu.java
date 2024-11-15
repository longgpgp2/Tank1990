package tank1990.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import tank1990.manager.FontManager;
import tank1990.manager.SoundManager;

public class OptionsMenu extends JPanel implements KeyListener, Runnable {
    private int currentSelection = 0;
    private Image logoImage;
    private String[] optionsItems = { "Sound", "Exit" };
    private SoundManager soundManager, backgroundMusic;
    private Font customFont;
    private JFrame parentFrame;
    private GameState gameState;

    public OptionsMenu(JFrame fr, SoundManager backgroundMusic, GameState gameState) {
        this.parentFrame = fr;
        this.backgroundMusic = backgroundMusic;
        this.gameState = GameState.getInstance();
        FontManager font = new FontManager("/fonts/6809-chargen.regular.ttf", 36f);
        customFont = font.getCustomFont();
        this.soundManager = new SoundManager();
        this.soundManager.soundLoader(".\\src\\main\\resources\\sounds\\changeOption.wav");
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
        if (logoImage != null) {
            g.drawImage(logoImage, getWidth() / 2 - logoImage.getWidth(null) / 2, 100, null);
        }

        g.setColor(Color.WHITE);
        g.setFont(customFont != null ? customFont : new Font("Arial", Font.BOLD, 36));
        g.drawString("Options", getWidth() / 2 - 90, 300);

        g.setFont(customFont != null ? customFont : new Font("Arial", Font.PLAIN, 36));
        for (int i = 0; i < optionsItems.length; i++) {
            if (i == currentSelection) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.WHITE);
            }
            g.drawString(optionsItems[i], getWidth() / 2 - 100, 350 + i * 50);
        }

        g.setColor(Color.WHITE);
        g.drawString((gameState.isSoundOn() ? "On" : "Off"), getWidth() / 2 + 100, 350);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            currentSelection--;
            if (currentSelection < 0) {
                currentSelection = optionsItems.length - 1;
            }
            selectSoundManager();
        } else if (key == KeyEvent.VK_S) {
            currentSelection++;
            if (currentSelection >= optionsItems.length) {
                currentSelection = 0;
                selectSoundManager();
            }
        } else if (key == KeyEvent.VK_ENTER) {
            selectSoundManager();
            selectOption();
        }
        repaint();
    }

    private void selectOption() {
        if (currentSelection == 0) {
            gameState.setSoundOn(!gameState.isSoundOn());
            if (!gameState.isSoundOn()) {
                backgroundMusic.stopSound();
            }
        } else if (currentSelection == 1) {
            parentFrame.dispose();
            openMainMenu();
        }

    }

    private void openMainMenu() {
        JFrame menuFrame = new JFrame("Tank 1990");
        Menu menu = new Menu(menuFrame);
        menu.refreshBackgroundMusic();
        menuFrame.add(menu);
        menuFrame.setSize(800, 600);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void run() {
        setVisible(true);
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void selectSoundManager() {
        if (gameState.isSoundOn()) {
            soundManager.resetSound();
            soundManager.playSound();
        } else {
            soundManager.stopSound();
        }
    }
}
