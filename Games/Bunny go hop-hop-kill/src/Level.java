import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Level {
    public static void load(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));

        br.readLine();

        Cfg.paints.add(Cfg.actBack);

        Cfg.bunNum = Integer.parseInt(br.readLine());
        Cfg.buns = new Bun[Cfg.bunNum];
        Cfg.spawns = new int[Cfg.bunNum][2];
        Cfg.score = new int[Cfg.bunNum];
        for (int i = 0; i < Cfg.bunNum; i++) {
            Object[] inp = Arrays.stream(br.readLine().split(" ")).toArray();
            Cfg.buns[i] = new Bun(Cfg.spawns[i][0] = Integer.parseInt(inp[0].toString()), Cfg.spawns[i][1] = Integer.parseInt(inp[1].toString()),
                    Integer.parseInt(inp[2].toString()), ImageIO.read(new File(inp[3].toString())));
        }

        br.readLine();
        br.readLine();

        Cfg.platfNum = Integer.parseInt(br.readLine()) + 1 + Cfg.bunNum;
        Cfg.platfs = new Platf[Cfg.platfNum];
        Cfg.platfs[0] = new Platf(Cfg.width / 2, Cfg.height, Cfg.width, 80, ImageIO.read(new File("sprites/platf_bricks.jpg")));
        for (int i = 1; i < Cfg.bunNum + 1; i++) {
            Cfg.platfs[i] = new Platf((int) Cfg.buns[i - 1].x, (int) (Cfg.buns[i - 1].y + 50 + Cfg.buns[i - 1].hHeight),
                    false, ImageIO.read(new File("sprites/platf_wood.jpg")));
        }
        for (int i = 1 + Cfg.bunNum; i < Cfg.platfNum; i++) {
            Object[] inp = Arrays.stream(br.readLine().split(" ")).toArray();
            Cfg.platfs[i] = new Platf(Integer.parseInt(inp[0].toString()), Integer.parseInt(inp[1].toString()),
                    Boolean.parseBoolean(inp[2].toString()), ImageIO.read(new File((String) inp[3])));
        }
    }
}
