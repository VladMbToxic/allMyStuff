import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class Panel extends JPanel {

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(0, 0, 0));
        g2d.fillRect(0,0,Main.frame.getWidth(), Main.frame.getHeight());
        g2d.setColor(new Color(200, 200, 200));

        g2d.fillOval((int) (Main.bX - Main.bR), (int) (Main.bY - Main.bR), (int) (2 * Main.bR), (int) (2 * Main.bR));
        g2d.fillRect((int) (Main.pX-Main.pWidth/2), Main.pY, (int) Main.pWidth, (int) Main.pHeight);
    }
}
