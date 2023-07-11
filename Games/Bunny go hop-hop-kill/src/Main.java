// Butkovskiy Vlad 2023a
// 14.09.2022
// Buuny game (AWFUL)

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class Main {

    public static JFrame frame = new JFrame("no title");


    static Timer t;

    public static void main(String[] args) throws IOException {

        Level.load("src/InputCfg");

        frame.setSize(Cfg.width, Cfg.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Panel panel = new Panel();
        frame.add(panel);

        panel.setFocusable(true);
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            public void keyPressed(KeyEvent e) {
                Bun.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Bun.keyReleased(e);
            }
        });

        frame.setVisible(true);

        t = new Timer((int) Cfg.timerDelay, (h) -> {
            for (Bun bun : Cfg.buns) {
                bun.update();
            }
            panel.repaint();

            Cfg.T -= Cfg.timerDelay;
            if (Cfg.T < 0) {
                t.stop();
                for (int i = 0; i < Cfg.bunNum; i++) {
                    System.out.println("Rabbit №" + (i+1) + " your score is " + Cfg.score[i] + " points")t;
                }
            }
        });
        t.start();
    }
}






















