package Name;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import static Name.Utils.*;

public class Main {

    public static double[] a1, a2, a3, a4;

    public static void main(String[] args) {
        Input.input(700);

        for (int k = 1; k <= 4; k++) {
            double[][] avgMatrix = new double[k][k];
            double[] avgVect = new double[k];
            Equation.fillMatrix(avgMatrix, avgVect, k);
            switch (k) {
                case 1 -> {
                    a1 = Equation.solution(avgMatrix, avgVect, k);
                    System.out.println("( " + avgVect[0] + " ) [ " + avgMatrix[0][0] + " ] and " + a1[0]);
//                    System.out.println(a1[0] + " " + a1[1]);
                    System.out.println("____");
                }
                case 2 -> {
                    a2 = Equation.solution(avgMatrix, avgVect, k);
                    for (int i = 0; i < k; i++) {
                        System.out.println("( " + avgVect[i] + " ) [ " + avgMatrix[i][0] + " " + avgMatrix[i][1] + " ] and " + a2[0] + ", " + a2[1]);
                    }
                    //                    System.out.println(a2[0] + " " + a2[1] + " " + a2[2]);
                    System.out.println("____");
                }
                case 3 -> {
                    a3 = Equation.solution(avgMatrix, avgVect, k);
                    for (int i = 0; i < k; i++) {
                        System.out.println("( " + avgVect[i] + " ) [ " + avgMatrix[i][0] + " " + avgMatrix[i][1] + " " + avgMatrix[i][2] + " ] and " +
                                a3[0] + ", " + a3[1] + ", " + a3[2]);
                    }
//                    System.out.println(a3[0] + " " + a3[1] + " " + a3[2] + " " + a3[3]);
                    System.out.println("____");
                }
                case 4 -> {
                    a4 = Equation.solution(avgMatrix, avgVect, k);
                    for (int i = 0; i < k; i++) {
                        System.out.println("( " + avgVect[i] + " ) [ " + avgMatrix[i][0] + " " + avgMatrix[i][1] + " " + avgMatrix[i][2] + " " + avgMatrix[i][3] + " ] and " +
                                a4[0] + ", " + a4[1] + ", " + a4[2] + ", " + a4[3]);
                    }

//                    System.out.println(a4[0] + " " + a4[1] + " " + a4[2] + " " + a4[3] + " " + a4[4]);
                    System.out.println("____");
                }
            }
        }

        System.out.println("-------------------");

        double[] a = Forecasts.best_K(a1, a2, a3, a4);

//        a = a1;

        k = a.length;

        System.out.println("k = " + k + "; coefficients are:");
        for (double v : a) {
            System.out.print(v + " ");
        }
        System.out.println();
        System.out.println("-------------------");

        double[] y = Forecasts.forecastInit(a, normData, totalLines - k);

        /*for (int i = 0; i < totalLines-k; i++) {
            System.out.println("i: " + (i+k) + "; " + df.format(data[i+2][csvColumn]) + " | " + "i: " + (i+k) + "; " + df.format(y[i]));
        }
        System.out.println("-------------------");*/

        Epsilon.getCoef(y);

        int amount = 5;
        double[][] prediction = new double[amount][testPart];

        for (int i = 0; i < amount; i++) {
            prediction[i] = Forecasts.forecast(a, normDataTrain, testPart);
        }

        /*for (int i = 0; i < totalLines - k; i++) {
            System.out.println(epsilon[i]);
        }
        System.out.println("-------------------");*/

        XYChart chart2 = new XYChartBuilder().width(1000).height(800).title("Train data").xAxisTitle("time").yAxisTitle("Price").build();
        chart2.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart2.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart2.getStyler().setYAxisDecimalPattern("$ #,###.##");
        chart2.getStyler().setPlotMargin(0);
        chart2.getStyler().setPlotContentSize(.95);
        double[] x0 = new double[trainPart];
        for (int i = 1; i <= trainPart; i++) {
            x0[i - 1] = i;
        }
        double[] truth0 = new double[trainPart];
        for (int i = 0; i < trainPart; i++) {
            truth0[i] = (double) dataTrain[i][csvColumn];
        }
        chart2.addSeries("Train data", x0, truth0);
        new SwingWrapper<XYChart>(chart2).displayChart();

        XYChart chart1 = new XYChartBuilder().width(1000).height(800).title("Predictions").xAxisTitle("num of predict").yAxisTitle("predict").build();
        chart1.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart1.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart1.getStyler().setYAxisDecimalPattern("$ #,###.##");
        chart1.getStyler().setPlotMargin(0);
        chart1.getStyler().setPlotContentSize(.95);

        double[] x = new double[testPart];
        for (int i = 1; i <= testPart; i++) {
            x[i - 1] = i;
        }

        double[] truth = new double[testPart];
        for (int i = 0; i < testPart; i++) {
            truth[i] = (double) dataTest[i][csvColumn];
        }

//        chart1.addSeries("Pred 1", x, y);
        chart1.addSeries("truth", x, truth);
        chart1.addSeries("Pred 1", x, prediction[0]);
//        chart1.addSeries("Pred 2", x, prediction[1]);
//        chart1.addSeries("Pred 3", x, prediction[2]);
//        chart1.addSeries("Pred 4", x, prediction[3]);
//        chart1.addSeries("Pred 5", x, prediction[4]);

        for (int i = 0; i < testPart; i+=10) {
            System.out.print("(" + i + "," + truth[i] + ")");
        }
        System.out.println();
        for (int i = 0; i < testPart; i+=5) {
            System.out.print("(" + i + "," + prediction[0][i] + ")");
        }

        new SwingWrapper<>(chart1).displayChart();
    }
}
