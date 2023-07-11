import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class Panel extends JPanel {

    final int width = Main.frame.getWidth();
    final int height = Main.frame.getHeight();
    final int maxVx = 20;
    final int maxVy = 20;

    Random rand = new Random();

    final int amount;

    public static double[] x;
    public static double[] y;
    public static double[] vx;
    public static double[] vy;
    int r = 5;
    int m = 15;

    public Panel(double[] x, double[] y, double[] vx, double[] vy) {
        amount = x.length;
        Panel.x = x;
        Panel.y = y;
        Panel.vx = vx;
        Panel.vy = vy;
    }

    public Panel(int amount) {
        this.amount = amount;

        x = new double[amount];
        y = new double[amount];
        vx = new double[amount];
        vy = new double[amount];
        for (int i = 0; i < amount; i++) {
            x[i] = rand.nextInt(width - r * 2);
            y[i] = rand.nextInt(height - r * 2);
            vx[i] = rand.nextInt(maxVx) - maxVx / 2;
            vy[i] = rand.nextInt(maxVy) - maxVy / 2;
        }
    }

    public Panel(int amount, boolean SameSpeed) {
        this.amount = amount;

        x = new double[amount];
        y = new double[amount];
        vx = new double[amount];
        vy = new double[amount];
        for (int i = 0; i < amount; i++) {
            x[i] = rand.nextInt(1280 - r * 2);
            y[i] = rand.nextInt(720 - r * 2);
        }
        Arrays.fill(vx, rand.nextInt(20) - 10);
        Arrays.fill(vy, rand.nextInt(20) - 10);
    }

    public void move() {
        for (int i = 0; i < amount; i++) {
            x[i] += vx[i];
            y[i] += vy[i];
            if (x[i] + 2 * r >= getWidth()) {
                vx[i] *= -1;
                x[i] = getWidth() - 2 * r;
            }
            if (x[i] < 0) {
                vx[i] *= -1;
                x[i] = 0;
            }
            if (y[i] + 2 * r >= getHeight()) {
                vy[i] *= -1;
                y[i] = getHeight() - 2 * r;
            }
            if (y[i] < 0) {
                vy[i] *= -1;
                y[i] = 0;
            }

        }
    }

    public void collisionTest() {
        for (int i = 0; i < amount; i++) {
            for (int j = i + 1; j < amount; j++) {

                double prevEnergy = Math.pow(Panel.vx[i] * Panel.vx[i] + Panel.vy[i] * Panel.vy[i], 2);

                double dist = Math.sqrt(Math.pow(x[i] - x[j], 2) + Math.pow(y[i] - y[j], 2));
                if (dist * dist >= Math.pow(2 * r, 2)) {
                    continue;
                }
                double crossPointx = (x[i] + x[j]) / 2;
                double crossPointy = (y[i] + y[j]) / 2;
                if (dist == 0) {
                    x[i] = (int) (crossPointx + r * Math.sqrt(2) / 2);
                    y[i] = (int) (crossPointy + r * Math.sqrt(2) / 2);
                    x[j] = (int) (crossPointx - r * Math.sqrt(2) / 2);
                    y[j] = (int) (crossPointy - r * Math.sqrt(2) / 2);
                    continue;
                }
                double cos = (x[i] - x[j]) / dist;
                double sin = (y[i] - y[j]) / dist;
                x[i] = (int) (crossPointx + r * cos);
                y[i] = (int) (crossPointy + r * sin);
                x[j] = (int) (crossPointx - r * cos);
                y[j] = (int) (crossPointy - r * sin);

                // проблема в косинусе и синусе - я расчитываю из момента перекрывания,
                // а нужно как-то раздвинуть их и получить момент того, как только они коснулись друг друга

                double delta = (vx[i] * cos - vx[j] * cos + vy[i] * sin - vy[j] * sin);
                vx[i] -= delta * cos;
                vy[i] -= delta * sin;
                vx[j] += delta * cos;
                vy[j] += delta * sin;

                double newEnergy = Math.pow(Panel.vx[i] * Panel.vx[i] + Panel.vy[i] * Panel.vy[i], 2);

                /*if (newEnergy != prevEnergy) {
                    System.out.println("BRUH ENERGY LOSS");
                    System.out.println("Difference: " + (prevEnergy - newEnergy));
                    System.exit(1321312);
                }*/
            }
        }
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < amount; i++) {
            g2d.fillOval((int) x[i], (int) y[i], 2 * r, 2 * r);
        }
        Main.TimePassed += 10;
        if (Main.TimePassed > 60000) {
            Main.t.stop();
            for (int i = 0; i < vx.length; i++) {
                System.out.println(Math.sqrt(Panel.vx[i] * Panel.vx[i] + Panel.vy[i] * Panel.vy[i]));
            }
        }
    }
}
