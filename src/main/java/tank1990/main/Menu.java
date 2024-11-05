package tank1990.main;

import tank1990.manager.FontManager;
import tank1990.manager.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Menu extends JPanel implements KeyListener, Runnable {
    private int currentSelection = 0;
    private String[] menuItems = {"Start", "Options", "Exit"};
    private Image logoImage;
    private JFrame parentFrame;
    private Font customFont;
    private SoundManager soundManager, backgroundMusic;
    private GameState gameState ;
    public Menu(Frame frame){
        this.parentFrame = (JFrame) frame;
        FontManager font = new FontManager("/fonts/6809-chargen.regular.ttf", 36f);
        customFont = font.getCustomFont();
        setFocusable(true);
        addKeyListener(this);
        this.gameState = GameState.getInstance();
        this.backgroundMusic = new SoundManager();
        this.backgroundMusic.soundLoader(".\\src\\main\\resources\\sounds\\menuMusicTheme.wav");
        this.soundManager = new SoundManager();
        this.soundManager.soundLoader(".\\src\\main\\resources\\sounds\\changeOption.wav");
        ImageIcon icon = new ImageIcon(".\\src\\main\\resources\\images\\battle_city.png");
        logoImage = icon.getImage();
        if (gameState.isSoundOn()) {
            backgroundMusic.playLoop();
        } else {
            backgroundMusic.stopSound();
        }
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        g. setColor(Color.BLACK);
        g.fillRect(0,0,getWidth(),getHeight());

        if (logoImage != null){
            g.drawImage(logoImage, getWidth()/ 2 - logoImage.getWidth(null) / 2, 100, null);
        }

        g.setFont(customFont != null ? customFont : new Font("Arial", Font.PLAIN, 36));
        for (int i =0; i< menuItems.length; i++){
            if ( i == currentSelection){
                g.setColor(Color.YELLOW);
            }else{
                g.setColor(Color.WHITE);
            }
            g.drawString(menuItems[i], getWidth() / 2 - 100, 300 + i * 50);
        }
    }
    @Override
    public void run() {


        while (true) {
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W){
            currentSelection --;
            if (currentSelection <0){
                currentSelection = menuItems.length-1;
            }
            selectSoundManager();
        }else if(key == KeyEvent.VK_S){
            currentSelection ++;
            if (currentSelection >= menuItems.length){
                currentSelection =0;
            }
            selectSoundManager();
        }else if(key == KeyEvent.VK_ENTER){
            selectSoundManager();
            selectMenuItem();
        }
        repaint();
    }
    public void selectMenuItem(){
        switch (currentSelection){
            case 0:
                System.out.println("Start game selected!");
                backgroundMusic.stopSound();
                parentFrame.dispose();
                new GameObject().startGame();
                break;
            case 1:
                System.out.println("Options selected");
                parentFrame.dispose();
                openOptionsMenu();
                break;
            case 2:
                System.out.println("Exit selected");
                System.exit(0);
                break;
        }
    }

    private void openOptionsMenu(){
        JFrame optionsFrame = new JFrame("Options Menu");
        OptionsMenu optionsMenu = new OptionsMenu(optionsFrame, backgroundMusic, gameState);
        optionsFrame.add(optionsMenu);
        optionsFrame.setSize(800, 600);
        optionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        optionsFrame.setLocationRelativeTo(this);
        optionsFrame.setVisible(true);
        new Thread(optionsMenu).start();
    }
    public void refreshBackgroundMusic() {
        if (gameState.isSoundOn()) {
            backgroundMusic.resetSound(); // Chơi nhạc nếu isSoundOn là true
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    public void selectSoundManager(){
        if (gameState.isSoundOn()){
            soundManager.resetSound();
            soundManager.playSound();
        }else {
            soundManager.stopSound();
        }
    }
}