import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class Panel extends JPanel {

    final int Width = Main.frame.getWidth();
    final int Height = Main.frame.getHeight();
    final double C = 75;
    final double K = 0.01;
    final int DT = 1;

    static Random rand = new Random();

    public static double x_c = 0;
    public static double y_c = 0;
    public static double angle = rand.nextInt((int) (PI / 2 * 1000000)) / 1000000.0;
    public static double vx;
    public static double vy;
    int squareWidth = 40;
    int squareHeight = 40;
    int[] xSquare = new int[]{rand.nextInt(Width - squareWidth), rand.nextInt(Width - squareWidth)};
    int[] ySquare = new int[]{rand.nextInt(Height - squareHeight), rand.nextInt(Height - squareHeight)};
    boolean[] score = new boolean[xSquare.length];
    public static ArrayList<Integer> px = new ArrayList<>();
    public static ArrayList<Integer> py = new ArrayList<>();
    public static ArrayList<Integer> nx = new ArrayList<>();
    public static ArrayList<Integer> ny = new ArrayList<>();
    int rp = 10;
    public static int ra = 7;
    public static boolean angleVisible = true;


    public void move() {
        double ax = 0;
        double ay = 0;

        for (int i = 0; i < px.size(); i++) {
            double sqDist = Math.pow(px.get(i) - x_c, 2) + Math.pow(py.get(i) - y_c, 2);

            ax += C * (px.get(i) - x_c) / sqDist;
            ay += C * (py.get(i) - y_c) / sqDist;
        }
        for (int i = 0; i < nx.size(); i++) {
            double sqDist = Math.pow(nx.get(i) - x_c, 2) + Math.pow(ny.get(i) - y_c, 2);

            ax += -C * (nx.get(i) - x_c) / sqDist;
            ay += -C * (ny.get(i) - y_c) / sqDist;
        }

        ax += -K * vx;
        ay += -K * vy;

        vx += ax * DT;
        vy += ay * DT;

        x_c += vx;
        y_c += vy;
    }


    public void collisionTest() {
        for (int i = 0; i < score.length; i++) {
            if ((x_c + rp >= xSquare[i] && x_c + rp <= xSquare[i] + squareWidth && y_c >= ySquare[i] && y_c <= ySquare[i] + squareHeight) ||
                    (x_c - rp >= xSquare[i] && x_c - rp <= xSquare[i] + squareWidth && y_c >= ySquare[i] && y_c <= ySquare[i] + squareHeight) ||
                    (x_c >= xSquare[i] && x_c <= xSquare[i] + squareWidth && y_c + rp >= ySquare[i] && y_c + rp <= ySquare[i] + squareHeight) ||
                    (x_c >= xSquare[i] && x_c <= xSquare[i] + squareWidth && y_c - rp >= ySquare[i] && y_c - rp <= ySquare[i] + squareHeight)) {
                score[i] = true;
            }
        }
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.fillOval((int) x_c - rp, (int) y_c - rp, 2 * rp, 2 * rp);
        if (angleVisible) {
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.blue);
            g2d.drawLine(0, 0, (int) (50*cos(angle)), (int) (50*sin(angle)));
            g2d.setColor(Color.black);
        }
        g2d.setColor(Color.red);
        for (int i = 0; i < px.size(); i++) {
            g2d.fillOval(px.get(i), py.get(i), 2 * ra, 2 * ra);
        }
        g2d.setColor(Color.blue);
        for (int i = 0; i < nx.size(); i++) {
            g2d.fillOval(nx.get(i), ny.get(i), 2 * ra, 2 * ra);
        }
        int count = 0;
        for (int i = 0; i < score.length; i++) {
            if (score[i]) {
                g2d.setColor(Color.green);
                g2d.fillRect(xSquare[i], ySquare[i], squareWidth, squareHeight);
                count++;
            } else {
                g2d.setColor(Color.pink);
                g2d.fillRect(xSquare[i], ySquare[i], squareWidth, squareHeight);
            }
        }
        if (count == score.length) {
            Main.t.stop();
        }
    }
}
