// Butkovskiy Vlad 2023a
// 08.05.2022
// Circle Collision Simulation

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    /*
    1000 463 или 1280 720
    */

    public static JFrame frame = new JFrame("no title");

    public static int TimePassed = 0;

    public static Timer t;

    public static void main(String[] args) {

        frame.setSize(1280, 720);

        /*Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        int[] vx = new int[n];
        int[] vy = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = sc.nextInt();
            y[i] = sc.nextInt();
            vx[i] = sc.nextInt();
            vy[i] = sc.nextInt();
        }
        Panel panel = new Panel(x, y, vx, vy);*/

        Panel panel = new Panel(300);

        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

        t = new Timer(10, (a) -> {

            panel.move();
            panel.collisionTest();
            frame.repaint();

        });
        t.start();

    }
}