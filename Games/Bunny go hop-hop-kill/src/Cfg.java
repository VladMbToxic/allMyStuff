import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Cfg {
    /*
    1000 463 или 1280 720
    */
    public static final int width = 1280;
    public static final int height = 720;
    public static final int dErr = 38;
    public static final int rErr = 13;

    public static int T = 15000;
    public static final double timerDelay = 15.0;
    public static final double dt = 1;

    public static final int jumpSpeed = -10;
    public static final double g = 0.2;

    // public static int[][] keys = {{KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W}, {KeyEvent.VK_F, KeyEvent.VK_H, KeyEvent.VK_T},
    //    {KeyEvent.VK_K, KeyEvent.VK_SEMICOLON, KeyEvent.VK_O}, {KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP}};

    public static ArrayList<Paintable> paints = new ArrayList<>();
    public static ArrayList<Hittable> hits = new ArrayList<>();

    static Random r = new Random();
    static BufferedImage[] backs;

    static {
        try {
            backs = new BufferedImage[]{ImageIO.read(new File("sprites/back_1.jpg")), ImageIO.read(new File("sprites/back_2.jpg")),
                    ImageIO.read(new File("sprites/back_3.jpg")), ImageIO.read(new File("sprites/back_4.jpg")),
                    ImageIO.read(new File("sprites/back_5.jpg")), ImageIO.read(new File("sprites/back_6.jpg"))};
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage curBack = backs[r.nextInt(6)];
    static Background actBack = new Background();

    //для ввода ↓

    public static int bunNum; // не более 4
    public static Bun[] buns;
    public static int[][] spawns;
    public static int[] score;

    public static int platfNum; // сколько угодно
    public static Platf[] platfs;
}
