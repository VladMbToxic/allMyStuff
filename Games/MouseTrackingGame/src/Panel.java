import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Panel extends JPanel {

    final int Width = Main.frame.getWidth();
    final int Height = Main.frame.getHeight();
    final double C = 75;
    final double K = 0.01;
    final int TriggerDistance = 10;
    final int DT = 1;

    Random rand = new Random();

    public double x;
    public double y;
    public static double vx;
    public static double vy;
    public static double x_c;
    public static double y_c;
    int[] xSquare;
    int[] ySquare;
    int[] squareWidth;
    int[] squareHeight;
    int r = 5;
    int score = 0;

    public Panel() {
        squareHeight = new int[]{40};
        squareWidth = new int[]{40};
        xSquare = new int[]{rand.nextInt(Width - squareWidth[0])};
        ySquare = new int[]{rand.nextInt(Height - squareHeight[0])};
        x_c = rand.nextInt(Width - 2 * r);
        y_c = rand.nextInt(Height - 2 * r);
    }

    public void move() {
        double ax = 0;
        double ay = 0;
        if (getMousePosition() != null) {
            double xMouse = getMousePosition().getX();
            double yMouse = getMousePosition().getY();

            double sqDist = Math.pow(xMouse - x_c, 2) + Math.pow(yMouse - y_c, 2);

            if (Math.sqrt(sqDist) > TriggerDistance) {
                ax += C * (xMouse - x_c) / sqDist;
                ay += C * (yMouse - y_c) / sqDist;
            }
        }

        ax += -K * vx;
        ay += -K * vy;

        vx += ax * DT;
        vy += ay * DT;

        Main.T -= Main.timerDelay / 1000.0;
        if (Main.T <= 0) {
            Main.t.stop();
        }

        x_c += vx;
        y_c += vy;

        if (x_c + r + 10 >= Width) {
            vx *= -1;
            x_c = Width - r - 10;
        }
        if (x_c < r) {
            vx *= -1;
            x_c = r;
        }
        if (y_c + r + 30 >= Height) {
            vy *= -1;
            y_c = Height - r - 30;
        }
        if (y_c < r) {
            vy *= -1;
            y_c = r;
        }
    }


    public void collisionTest() {
        if ((x_c + r >= xSquare[0] && x_c + r <= xSquare[0] + squareWidth[0] && y_c >= ySquare[0] && y_c <= ySquare[0] + squareHeight[0]) ||
                (x_c - r >= xSquare[0] && x_c - r <= xSquare[0] + squareWidth[0] && y_c >= ySquare[0] && y_c <= ySquare[0] + squareHeight[0]) ||
                (x_c >= xSquare[0] && x_c <= xSquare[0] + squareWidth[0] && y_c + r >= ySquare[0] && y_c + r <= ySquare[0] + squareHeight[0]) ||
                (x_c >= xSquare[0] && x_c <= xSquare[0] + squareWidth[0] && y_c - r >= ySquare[0] && y_c - r <= ySquare[0] + squareHeight[0])) {
            xSquare[0] = rand.nextInt(Width - squareWidth[0]);
            ySquare[0] = rand.nextInt(Height - squareHeight[0]);
            score++;
        }
    }

    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        if (Main.T > 0) {
            g2d.setColor(Color.blue);
            g2d.fillRect(xSquare[0], ySquare[0], squareWidth[0], squareHeight[0]);
            g2d.setColor(Color.red);
            g2d.fillOval((int) x_c - r, (int) y_c - r, 2 * r, 2 * r);
            g2d.setColor(Color.black);
            g2d.drawString("T = " + ((int) (Main.T * 1000)) / 1000.0, 5, 25);
        } else {
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g2d.setColor(Color.green);
            g2d.drawString("Congrats! Your score is " + score + " points!", Width / 2, Height / 2);
        }
    }
}
