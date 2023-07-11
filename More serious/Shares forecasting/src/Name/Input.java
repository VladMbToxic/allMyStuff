package Name;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import static Name.Utils.*;

public class Input {
    public static void input(int... lines) {
        String line;
        String splitBy = ",";

        try {
            BufferedReader br = new BufferedReader(new FileReader(sourcePath));
            String[] inp = br.readLine().split(splitBy);
            strLength = inp.length;
            while (br.readLine() != null) {
                totalLines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } //counting the number of data lines in the file

        if (lines.length != 0) {
            totalLines = lines[0];
        }

        data = new Object[totalLines][strLength];
        trainPart = (int) Math.round(((double) totalLines) * trainFrac);
        testPart = totalLines - trainPart;
        dataTrain = new Object[trainPart][strLength];
        dataTest = new Object[testPart][strLength];

        normData = new Object[totalLines][strLength];
        normDataTrain = new Object[trainPart][strLength];
        normDataTest = new Object[testPart][strLength];


        try {
            BufferedReader br = new BufferedReader(new FileReader(sourcePath));
            br.readLine();
            int z = 0;

            while ((line = br.readLine()) != null && z < totalLines) {
                String[] inp = line.split(splitBy);
                data[z][0] = inp[0];
                for (int j = 1; j < strLength; j++) {
                    data[z][j] = Double.parseDouble(inp[j]);
                    normData[z][j] = Double.parseDouble(inp[j]);
                }
                z++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } // filling data array (all lines)

        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(sourcePath));
            br.readLine();
            int i = 0;

            // filling dataTrain array (trainFrac of lines)
            while (i < trainPart && (line = br.readLine()) != null) {
                String[] inp = line.split(splitBy);
                dataTrain[i][0] = inp[0];
                for (int j = 1; j < strLength; j++) {
                    dataTrain[i][j] = Double.parseDouble(inp[j]);
                    normDataTrain[i][j] = Double.parseDouble(inp[j]);
                }
//                System.out.println("i: " + i + " | Date: " + dataTrain[i][0] + ", Value 1: " + dataTrain[i][1] + ", Value 2:" + dataTrain[i][2] +
//                        ", Value 3: " + dataTrain[i][3] + ", Value 4: " + dataTrain[i][4] + ", Value 5: " + dataTrain[i][5] +
//                        ", Value 6: " + dataTrain[i][6]);
                i++;
            }

            System.out.println("-----");
            i = 0;

            // filling dataTest array (lines that are left after training)
            while ((line = br.readLine()) != null && trainPart + i < totalLines) {
                String[] inp = line.split(splitBy);
                dataTest[i][0] = inp[0];
                for (int j = 1; j < strLength; j++) {
                    dataTest[i][j] = Double.parseDouble(inp[j]);
                    normDataTest[i][j] = Double.parseDouble(inp[j]);
                }
//                System.out.println("i: " + i + " | Date: " + dataTest[i][0] + ", Value 1: " + dataTest[i][1] + ", Value 2: " + dataTest[i][2] +
//                        ", Value 3: " + dataTest[i][3] + ", Value 4: " + dataTest[i][4] + ", Value 5: " + dataTest[i][5] +
//                        ", Value 6: " + dataTest[i][6]);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("-----");

        // uncomment for output of number of lines
        //System.out.println("Amount of train lines: " + trainPart + ", amount of test lines: " + testPart + ", it totals with " + totalLines + " lines");

        int columns = 4;

        avg = new double[columns];
        for (int i = 1; i < 1 + columns; i++) {
            // avr of x_i (x_i-1, x_i-2, x_i-3)
            for (int j = 0; j < totalLines; j++) {
                avg[i-1] += (double) data[j][i];
            }
            avg[i-1] /= totalLines;
            for (int j = 0; j < totalLines; j++) {
                normData[j][i] = (double) normData[j][i] - avg[i-1];
                if (j < trainPart) {
                    normDataTrain[j][i] = (double) normDataTrain[j][i] - avg[i-1];
                } else {
                    normDataTest[j - trainPart][i] = (double) normDataTest[j - trainPart][i] - avg[i-1];
                }
//                if (j == 0 || j == 1) System.out.println(data[j][i] + " - " + avg[i-1] + " = " + normData[j][i]);
            }
        }
        for (double avr : avg){
            System.out.print(avr + ", ");
        }
        System.out.println();
        System.out.println("___________");
    }
}