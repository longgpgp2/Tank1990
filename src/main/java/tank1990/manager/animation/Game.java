//package tank1990.manager.animation;
//
//import java.awt.Dimension;
//import java.awt.Graphics;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.Timer;
//
//import tank1990.common.enums.Direction;
//import tank1990.objects.tanks.Bullet;
//
//public class Game extends JPanel implements ActionListener {
//    private List<Bullet> bullets = new ArrayList<>();
//    private Timer timer;
//
//    public Game() {
//        setPreferredSize(new Dimension(500, 500));
//        timer = new Timer(16, this); // Khoảng thời gian mỗi khung hình (60 FPS)
//        timer.start();
//    }
//
//    public void addBullet(Bullet bullet) {
//        bullets.add(bullet);
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        for (Bullet bullet : bullets) {
//            bullet.draw(g);
//        }
//        // Vẽ thêm các đối tượng khác nếu cần
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        for (int i = 0; i < bullets.size(); i++) {
//            Bullet bullet = bullets.get(i);
//            bullet.update(0);
//
//            if (bullet.isCollided() || bullet.checkBulletOutOfBound()) {
//                bullet.update();
//                if (bullet.isExplosionFinished()) {
//                    bullets.remove(i);
//                    i--;
//                }
//            }
//        }
//        repaint();
//    }
//
//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Tank Game");
//        Game gamePanel = new Game();
//        frame.add(gamePanel);
//        frame.pack();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//
//        Bullet bullet = new Bullet(100, 100, Direction.RIGHT, 5);
//        gamePanel.addBullet(bullet);
//    }
//}
