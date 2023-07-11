// Butkovskiy Vlad 2023a
// 20.05.2022
// Game with mouse traction

import javax.swing.*;

public class Main {

    /*
    1000 463 или 1280 720
    */

    public static JFrame frame = new JFrame("no title");

    public static Timer t;
    public static double T = 30.0;
    public static double timerDelay = 20.0;


    public static void main(String[] args) {

        frame.setSize(1280, 720);

        Panel panel = new Panel();

        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        t = new Timer((int) timerDelay, (a) -> {
            frame.repaint();
            panel.move();
            panel.collisionTest();
        });
        t.start();
    }
}