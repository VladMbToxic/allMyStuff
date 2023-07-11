import javax.swing.*;
import java.awt.*;


public class Panel extends JPanel {

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (Paintable p: Cfg.paints){
            p.paint(g2d);
        }
    }


}
