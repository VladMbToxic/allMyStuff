import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Platf implements Paintable, Hittable {
    final int maxW = 200;
    final int maxH = 25;
    final int minW = 100;
    final int minH = 5;
    final int edge = 5;
    double x, y;
    int width, height, hWidth, hHeight;
    BufferedImage image;
    Random rand = new Random(232);

    /* КОНСТРУКТОР */

    // Координатами и размерами
    public Platf(int x, int y, int width, int height, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        hWidth = width / 2;
        hHeight = height / 2;
        Cfg.paints.add(this);
    }

    // Размерами (коорд. рандом)
    public Platf(int width, int height, BufferedImage image) {
        this.x = rand.nextInt(Cfg.width);
        this.y = rand.nextInt(Cfg.height);
        this.width = width;
        this.height = height;
        this.image = image;
        hWidth = width / 2;
        hHeight = height / 2;
        Cfg.paints.add(this);
    }

    // Координатами (размер рандом или фиксирован)
    public Platf(int x, int y, boolean sizeNotConst, BufferedImage image) {
        this.x = x;
        this.y = y;
        if (sizeNotConst) {
            this.width = rand.nextInt(maxW - minW) + minW;
            this.height = rand.nextInt(maxH - minH) + minH;
        } else {
            this.width = 200;
            this.height = 10;
        }
        this.image = image;
        hWidth = width / 2;
        hHeight = height / 2;
        Cfg.paints.add(this);
        Cfg.hits.add(this);
    }

    // все случайное
    public Platf(BufferedImage image) {
        this.x = rand.nextInt(Cfg.width);
        this.y = rand.nextInt(Cfg.height);
        this.width = rand.nextInt(maxW - minW) + minW;
        this.height = rand.nextInt(maxH - minH) + minH;
        this.image = image;
        hWidth = width / 2;
        hHeight = height / 2;
        Cfg.paints.add(this);
    }

    public void paint(Graphics2D g2d) {
        g2d.drawImage(image, (int) (x - hWidth), (int) (y - hHeight), (int) (x + hWidth), (int) (y + hHeight), 0, 0, 300, 30, null);
    }

    public int hitTest(Bun b) {
        // кроль справа от платформы
        if (b.y - b.hHeight < y + hHeight && b.y + b.hHeight > y - hHeight
                && b.x + b.vx - b.hWidth < x + hWidth - edge && b.x + b.vx + b.hWidth > x + hWidth - edge) {
            return 2;
        }
        // кроль слева от платформы
        else if (b.y - b.hHeight < y + hHeight && b.y + b.hHeight > y - hHeight
                && b.x + b.vx + b.hWidth > x - hWidth + edge && b.x + b.vx - b.hWidth < x - hWidth + edge) {
            return 4;
        }
        //кроль снизу от платформы
        else if (b.y - b.hHeight > y + hHeight && b.y - b.hHeight + b.vy < y + hHeight
                && b.x + b.vx + b.hWidth > x - hWidth + edge && b.x + b.vx - b.hWidth < x + hWidth - edge) {
            return 3;
        }
        //кроль сверху от прлатформы
        else if (b.y + b.hHeight <= y - hHeight && b.y + b.hHeight + b.vy > y - hHeight
                && b.x + b.vx + b.hWidth > x - hWidth + edge && b.x + b.vx - b.hWidth < x + hWidth - edge) {
            return 1;
        }
        return 0;
    }
}
