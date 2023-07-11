package Name;

import Jama.Matrix;

import static Name.Utils.*;

public class Equation {

    static void fillMatrix(double[][] matrix, double[] vect, int k) {
        double avr = 0;

        // avr of x_{i}^2 (x_{i-1}^2, x_{i-2}^2, x_{i-3}^2)
        for (int i = 0; i < trainPart; i++) {
            avr += Math.pow((Double) normDataTrain[i][csvColumn], 2);
        }
        /*for (int i = 1; i < k + 1; i++) {
            double res = avr - Math.pow((double) dataTrain[i][csvColumn], 2);
            res /= (trainPart - 1);
            matrix[i][i] = res;
        }*/
        avr /= trainPart;
        for (int i = 0; i < k; i++) {
            matrix[i][i] = avr;
        }

        // avr of x_{i} * x_{i-j} (and other multiplications of 2 consecutive 'x')
        for (int j = 1; j < k + 1; j++) {
            avr = 0;

            for (int i = j; i < trainPart; i++) {
                avr += (double) normDataTrain[i][csvColumn] * (double) normDataTrain[i - j][csvColumn];
            }
            avr /= trainPart - j;

            vect[j - 1] = avr;
            for (int i = 1; i + j < k + 1; i++) {
                matrix[i - 1][i + j - 1] = avr;
                matrix[i + j - 1][i - 1] = avr;
            }
        }
    }

    // solving matrix using ext. library Jama
    static double[] solution(double[][] matrix, double[] vect, int k) {
        Matrix a = new Matrix(matrix);
        Matrix b = new Matrix(vect, k);
        Matrix x = a.solve(b);

        double[] res = new double[k];

        double[][] tmp = x.getArrayCopy();
        for (int i = 0; i < tmp.length; i++) {
            res[i] = tmp[i][0];
        }

        return res;
    }
}