// Butkovskiy Vlad 2023a
// 14.09.2022
// Mini-arcanoid

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Main {

    /*
    1000 463 или 1280 720
    */

    static Random rand = new Random();

    public static JFrame frame = new JFrame("no title");
    public static final int width = 1280;
    public static final int height = 720;

    //Ball creation
    public static double bX = width / 2;
    public static double bY = height / 2;
    public static final int maxVel = 6;
    public static double bVx = rand.nextInt(2 * maxVel) - maxVel;
    public static double bVy = rand.nextInt(2 * maxVel) - maxVel;
    public static double bR = 10;

    //Platform creation
    public static double pWidth = 100;
    public static double pHalfWidth = pWidth / 2;
    public static double pHeight = 10;
    public static double pX = width / 2;
    public static int pY = 600;
    public static double pV = 0;

    public static Timer t;
    public static double timerDelay = 15.0;
    //public static final int fps = 100;
    //public static double timerDelay = fps/Math.sqrt(bVx*bVx+bVy*bVy)/200;

    public static void move() {
        bX += bVx;
        bY += bVy;
        pX += pV;

        if (bX + bR + 13 >= width) {
            bVx *= -1;
        }
        if (bX < bR) {
            bVx *= -1;
        }
        if (bY - bR < 0) {
            bVy *= -1;
        }
        if (bY + bR + 38 > height) {
            t.stop();
        }
    }

    public static void collisionTest() {
        if (pX - pHalfWidth <= bX && bX <= pX + pHalfWidth &&
                bY + bR > pY && bY < pY) {
            bVy *= -1;
            boolean way = rand.nextBoolean();
            if (way) {
                bVx *= -1;
            } else {
                bVx *= -1;
                bVx += 4;
            }
        }
        if (pX + pHalfWidth > width) {
            pX = width - pHalfWidth;
        }
        if (pX - pHalfWidth < 0) {
            pX = pHalfWidth;
        }
    }

    public static void main(String[] args) {

        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Panel panel = new Panel();
        frame.add(panel);
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        frame.getContentPane().add(box, BorderLayout.EAST);
        /*JTextField tf = new JTextField();
        box.add(tf);
        JButton jb = new JButton("Start");
        box.add(jb);

        tf.addActionListener((te) -> {
            double value = Double.parseDouble(tf.getText());
            bVx = value;
            bVy = value;
        });

        jb.addActionListener((e) -> t.start());*/


        panel.setFocusable(true);
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
                    pV = -10;
                }
                if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    pV = 10;
                }
                if ((e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) &&
                        (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT)) {
                    pX -= 500;
                }
                if ((e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) &&
                        (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)) {
                    pX += 500;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
                    pV = 0;
                }
                if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    pV = 0;
                }
            }
        });

        frame.setVisible(true);

        t = new Timer((int) timerDelay, (h) -> {
            frame.repaint();
            move();
            collisionTest();
        });
        t.start();
    }
}