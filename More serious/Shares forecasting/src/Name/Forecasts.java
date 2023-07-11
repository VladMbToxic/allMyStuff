package Name;

import static Name.Utils.*;

public interface Forecasts {

    static double sqrtMeanSquare(double[] nums) {
        double sum = 0.0;
        for (int i = 0; i < nums.length; i++)
            sum += (nums[i] - (double) normDataTest[i][csvColumn]) * (nums[i] - (double) normDataTest[i][csvColumn]);
        return Math.sqrt(sum / nums.length);
    }

    static int indexOfSmallest(double[] array) {
        if (array.length == 0)
            return -1;

        int index = 0;
        double min = array[index];
        for (int i = 1; i < array.length; i++) {
            if (array[i] <= min) {
                min = array[i];
                index = i;
            }
        }
        return index;
    }

    // initial forecasting of test part
    static double[] forecastInit(double[] a, Object[][] dataBase, int amount) {
        double[] y = new double[amount];
        int k = a.length;
        /*int start = dataBase.length;
        if (startPos.length != 0) {
            start = startPos[0];
        }*/
        int start = k;
        // filling of y from x
        for (int i = 0; i < k; i++) {
            for (int j = 1; j < k - i + 1; j++) {
                y[i] += a[j + i - 1] * (double) dataBase[start - j][csvColumn];
            }
            for (int j = k - i; j < k; j++) {
                y[i] += a[j - k + i] * y[k - j - 1];
            }
        }
        // filling of y from y
        for (int i = k; i < amount; i++) {
            for (int j = 1; j < k + 1; j++) {
                y[i] += a[j - 1] * y[i - j];
            }
        }
        return y;
    }

    // choosing the best approximation
    static double[] best_K(double[]... a) {
        double[][] y = new double[a.length][testPart];
        int len = a.length;
        for (int i = 0; i < len; i++) {
            y[i] = forecastInit(a[i], normDataTrain, testPart);
        }
        double[] avr = new double[len];
        for (int i = 0; i < len; i++) {
            avr[i] = sqrtMeanSquare(y[i]);
        }

        for (int i = 0; i < avr.length; i++) {
            System.out.println("Average mistake of k = " + (i + 1) + " is " + avr[i]);
        }
        System.out.println("------------------"); // uncomment for output of average mistake

        int index = indexOfSmallest(avr);
        return a[index];
    }

    // forecasting
    static double[] forecast(double[] a, Object[][] dataBase, int amount, int... startPos) {
        double[] y = new double[amount];
        int k = a.length;
        int start = dataBase.length;
        if (startPos.length != 0) {
            start = startPos[0];
        }
        // filling of y from x
        for (int i = 0; i < k; i++) {
            for (int j = 1; j < k - i + 1; j++) {
                y[i] += a[j + i - 1] * (double) dataBase[start - j][csvColumn];
            }
            for (int j = k - i; j < k; j++) {
                y[i] += a[j - k + i] * y[k - j - 1];
            }
        }
        // filling of y from y
        for (int i = k; i < amount; i++) {
            for (int j = 1; j < k + 1; j++) {
                y[i] += a[j - 1] * y[i - j];
            }
        }
        for (int i = 0; i < amount; i++) {
            double e = Epsilon.epsilon();
            while (y[i] + e <= 0) {
                e = Epsilon.epsilon();
            }
//            y[i] += e;
            y[i] += avg[csvColumn - 1];
        }
        return y;
    }
}
