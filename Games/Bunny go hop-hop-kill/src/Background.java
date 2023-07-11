import java.awt.*;
import java.awt.image.BufferedImage;

public class Background implements Paintable {
    BufferedImage img = Cfg.curBack;

    public void paint(Graphics2D g2d) {
        g2d.drawImage(img, 0, 0, Cfg.width, Cfg.height, 0, 0, img.getWidth(), img.getHeight(), null);
    }
}
