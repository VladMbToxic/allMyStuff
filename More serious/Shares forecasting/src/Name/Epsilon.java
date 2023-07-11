package Name;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import java.util.ArrayList;
import java.util.List;

import static Name.Utils.*;
import static java.lang.Math.*;

public class Epsilon {

    static double c;
    static double freq;

    static double getMostFreq(double[] arr, double max, double min, int numBin) {
        double diff = abs(max - min) / numBin;
        double[] edges = new double[numBin];
        int[] count = new int[numBin];
        for (int i = 0; i < edges.length; i++) {
            edges[i] = min + diff * i;
        }
        for (double v : arr) {
            for (int j = 0; j < edges.length - 1; j++) {
                if (v < edges[j + 1]) {
                    count[j]++;
                    break;
                }
            }
        }
        int maxAt = 0;
        for (int i = 0; i < count.length; i++) {
            maxAt = count[i] > count[maxAt] ? i : maxAt;
        }
        //System.out.println("diff " + diff);
        return count[maxAt];
    }

    static void getCoef(double[] predict) {
        int len = predict.length;
        double[] res = new double[len];
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            list.add(res[i] = (double) normData[i + k][csvColumn] - predict[i]);
        }

        Histogram histogram1 = new Histogram(list, 20);

        double a = getMostFreq(res, histogram1.getMax(), histogram1.getMin(), histogram1.getNumBins());
        c = (min(abs(histogram1.getMin()), histogram1.getMax())) / sqrt(2 * log(a));
//        c += c * 0.33;
        freq = c * sqrt(2 * log(1 / 0.6)); // change 0.6 between 0.5 and 0.7 to make histogram wider by "separating" two halves

        /*System.out.println(a);
        System.out.println(c);
        System.out.println(freq);
        System.out.println(histogram1.getMax());
        System.out.println("______________________");*/                                         // uncomment for output of coefficients

        /*List<Double> list2 = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list2.add(epsilon());
        }

        Histogram histogram2 = new Histogram(list2, 20);

        System.out.println("Minimum of epsilon from test: " + histogram2.getMin() +
                ", maximum of epsilon from test: " + histogram2.getMax()); // array of epsilon

        *//*System.out.println("------------");
        for (double num : list2) {
            System.out.println(num);
        }*//* // output of epsilon array

        // charts of epsilon
        CategoryChart chart1 = new CategoryChartBuilder().width(800).height(600).title("test").xAxisTitle("Mean").yAxisTitle("Count").build();
        chart1.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart1.getStyler().setAvailableSpaceFill(.96);
        chart1.getStyler().setDecimalPattern("#0.0"); // chart creation
        chart1.addSeries("Histogram 1", histogram1.getxAxisData(), histogram1.getyAxisData());
        new SwingWrapper(chart1).displayChart(); // chart of epsilon which comes from mistake

        CategoryChart chart2 = new CategoryChartBuilder().width(800).height(600).title("Function").xAxisTitle("Mean").yAxisTitle("Count").build();
        chart2.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart2.getStyler().setAvailableSpaceFill(.96);
        chart2.getStyler().setDecimalPattern("#0.0"); // chart creation
        chart2.addSeries("Histogram 2", histogram2.getxAxisData(), histogram2.getyAxisData());
        new SwingWrapper(chart2).displayChart(); // chart of epsilon made from function*/

        // uncomment stuff above to see histogram of function
    }

    static double epsilon() {
        if (random() < 0.5) return c * sqrt(2 * log(1 / random())) - freq;
        else return -(c * sqrt(2 * log(1 / random())) - freq);
    }
}
