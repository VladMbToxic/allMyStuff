import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Bun implements Paintable, Hittable {
    final int width = 40;
    final int height = 40;
    final int hWidth = width / 2;
    final int hHeight = height / 2;
    final int edge = 5;
    double x, y;
    double vx, vy;
    double ax, ay;
    BufferedImage image;
    boolean jumpBut, grounded;
    int ID;

    Random r = new Random();

    public Bun(double x, double y, int ID, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.ay = Cfg.g;
        this.jumpBut = false;
        this.grounded = false;
        this.ID = ID - 1;
        this.image = image;
        Cfg.paints.add(this);
        Cfg.hits.add(this);
    }

    public static void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> {
                Cfg.buns[0].vx = -5;
                System.out.println("LEFT 1");
            }
            case KeyEvent.VK_D -> {
                Cfg.buns[0].vx = 5;
                System.out.println("RIGHT 1");
            }
            case KeyEvent.VK_W -> {
                Cfg.buns[0].jumpBut = true;
                System.out.println("UP 1");
            }
            case KeyEvent.VK_F -> {
                Cfg.buns[1].vx = -5;
                System.out.println("LEFT 2");
            }
            case KeyEvent.VK_H -> {
                Cfg.buns[1].vx = 5;
                System.out.println("RIGHT 2");
            }
            case KeyEvent.VK_T -> {
                Cfg.buns[1].jumpBut = true;
                System.out.println("UP 2");
            }
            case KeyEvent.VK_K -> {
                Cfg.buns[2].vx = -5;
                System.out.println("LEFT 3");
            }
            case KeyEvent.VK_SEMICOLON -> {
                Cfg.buns[2].vx = 5;
                System.out.println("RIGHT 3");
            }
            case KeyEvent.VK_O -> {
                Cfg.buns[2].jumpBut = true;
                System.out.println("UP 3");
            }
            case KeyEvent.VK_LEFT -> {
                Cfg.buns[3].vx = -5;
                System.out.println("LEFT 4");
            }
            case KeyEvent.VK_RIGHT -> {
                Cfg.buns[3].vx = 5;
                System.out.println("RIGHT 4");
            }
            case KeyEvent.VK_UP -> {
                Cfg.buns[3].jumpBut = true;
                System.out.println("UP 4");
            }
        }
        /*== Cfg.keys[id][0]){
            Cfg.buns[id].vx = -5;
            System.out.println("LEFT");
        }
        if (e.getKeyCode() == Cfg.keys[id][1]) {
            Cfg.buns[id].vx = 5;
            System.out.println("RIGHT");
        }
        if (e.getKeyCode() == Cfg.keys[id][2]) {
            Cfg.buns[id].jump = true;
            System.out.println("UP");
        }*/
    }

    public static void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A, KeyEvent.VK_D -> Cfg.buns[0].vx = 0;
            case KeyEvent.VK_F, KeyEvent.VK_H -> Cfg.buns[1].vx = 0;
            case KeyEvent.VK_K, KeyEvent.VK_SEMICOLON -> Cfg.buns[2].vx = 0;
            case KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT -> Cfg.buns[3].vx = 0;
        }
    }

    public void paint(Graphics2D g2d) {
        g2d.drawImage(image, (int) (x - hWidth), (int) (y - hHeight), (int) (x + hWidth), (int) (y + hHeight), 0, 0, image.getWidth(), image.getHeight(), null);
    }

    public void update() {
        ax = 0;
        ay = Cfg.g;

        vx += ax * Cfg.dt;
        vy += ay * Cfg.dt;

        if (x + hWidth + Cfg.rErr >= Cfg.width) vx *= -1;
        if (x < hWidth) vx *= -1;
        if (y < hHeight) {
            vy *= 0;
            y = hHeight;
        }
        if (y + hHeight + Cfg.dErr > Cfg.height) {
            vy *= 0;
            y = Cfg.height - hHeight - Cfg.dErr;
            grounded = true;
        }

        for (Hittable h : Cfg.hits) {
            int res = h.hitTest(this);
            switch (res) {
                case 1: // сверху
                    if (h instanceof Platf) {
                        vy = 0;
                        y = ((Platf) h).y - ((Platf) h).hHeight - hHeight;
                        grounded = true;
                        break;
                    }
                    if (h instanceof Bun) {
                        Cfg.score[this.ID]++;
                        grounded = true;
                        System.out.println("LOL");
                        ((Bun) h).vx =0;
                        ((Bun) h).vy = 0;
                        ((Bun) h).ay = 0;
                        int spawnID = r.nextInt(Cfg.bunNum);
                        ((Bun) h).x = Cfg.spawns[spawnID][0];
                        ((Bun) h).y = Cfg.spawns[spawnID][1];
                        System.out.println("KEK");
                        break;
                    }
                case 2: // справа
                    if (h instanceof Platf) {
                        vx = 0;
                        x = ((Platf) h).x + ((Platf) h).hWidth + hWidth;
                        break;
                    }
                    if (h instanceof Bun) {
                        vx = 0;
                        x = ((Bun) h).x + ((Bun) h).hWidth + hWidth;
                        break;
                    }
                case 3: // снизу
                    if (h instanceof Platf) {
                        vy = 0;
                        ay = 0;
                        y = ((Platf) h).y + ((Platf) h).hHeight + hHeight;
                        break;
                    }
                    if (h instanceof Bun) {
                        Cfg.score[((Bun) h).ID]++;
                        ((Bun) h).grounded = true;
                        System.out.println("LOL");
                        vx =0;
                        vy = 0;
                        ay = 0;
                        int spawnID = r.nextInt(Cfg.bunNum);
                        x = Cfg.spawns[spawnID][0];
                        y = Cfg.spawns[spawnID][1];
                        System.out.println("KEK");
                        break;                    }
                case 4: // слева
                    if (h instanceof Platf) {
                        vx = 0;
                        x = ((Platf) h).x - ((Platf) h).hWidth - hWidth;
                        break;
                    }
                    if (h instanceof Bun) {
                        vx = 0;
                        x = ((Bun) h).x - ((Bun) h).hWidth - hWidth;
                        break;
                    }
            }
        }

        if (jumpBut && grounded) {
            vy = Cfg.jumpSpeed;
        }

        x += vx;
        y += vy;

        jumpBut = false;
        grounded = false;
    }

    public int hitTest(Bun b) {
        if (ID == b.ID) return 0;
        // кроль справа
        else if (b.y - b.hHeight < y + hHeight && b.y + b.hHeight > y - hHeight
                && b.x + b.vx - b.hWidth < x + hWidth - edge && b.x + b.vx + b.hWidth > x + hWidth - edge) {
            return 2;
        }
        // кроль слева
        else if (b.y - b.hHeight < y + hHeight && b.y + b.hHeight > y - hHeight
                && b.x + b.vx + b.hWidth > x - hWidth + edge && b.x + b.vx - b.hWidth < x - hWidth + edge) {
            return 4;
        }
        // кроль снизу
        else if (b.y - b.hHeight > y + hHeight && b.y - b.hHeight + b.vy < y + hHeight
                && b.x + b.vx + b.hWidth > x - hWidth + edge && b.x + b.vx - b.hWidth < x + hWidth - edge) {
            return 3;
        }
        //кроль сверху
        else if (b.y + b.hHeight <= y - hHeight && b.y + b.hHeight + b.vy > y - hHeight
                && b.x + b.vx + b.hWidth > x - hWidth + edge && b.x + b.vx - b.hWidth < x + hWidth - edge) {
            return 1;
        }
        return 0;
    }
}
