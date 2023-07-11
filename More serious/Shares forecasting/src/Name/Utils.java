package Name;

public class Utils {
    public static final double trainFrac = 0.80;
    public static final String sourcePath = "src/TSLA.csv";
    public static int csvColumn = 3;
    public static int totalLines;
    public static int strLength;
    public static int trainPart;
    public static int testPart;
    public static double[] avg;
    public static Object[][] data;
    public static Object[][] dataTrain;
    public static Object[][] dataTest;
    public static Object[][] normData;
    public static Object[][] normDataTrain;
    public static Object[][] normDataTest;
    public static int k;
}
