import java.util.ArrayList;
import java.util.Scanner;

public class Big_Decimals {

    public static final int k = 3;
    public static final int m = (int) Math.pow(10, k);

    public static int[] toNum(String str) {
        int[] result = new int[(int) Math.ceil(1.0 * (str.length()) / k)];
        int num = 0;
        for (int i = str.length(); i > 0; i -= k) {
            int x = Integer.parseInt(str.substring(Math.max(0, i - k), i));
            result[num++] += x;
        }
        return result;
    }

    public static String toString(int[] num) {
        StringBuilder sb = new StringBuilder();
        int[] normNum = normalise(num);
        boolean isPositive = normNum[normNum.length - 1] >= 0;
        if (!isPositive) {
            normNum = normalise(invert(normNum));
            sb.append("-");
        }
        int start = normNum.length - 1;
        while (start > 0 && normNum[start] == 0) {
            start--;
        }
        for (int i = start; i >= 0; i--) {
            String part = Integer.toString(normNum[i]);
            if (i != start && part.length() < k) {
                sb.append("0".repeat(k - part.length()));
            }
            sb.append(part);
        }
        return sb.toString();
    }

    public static int getDigit(int[] num, int i) {
        if (i >= num.length) {
            return 0;
        }
        return num[i];
    }

    public static int[] getPart(int[] num, int start, int length) {
        int[] result = new int[length];
        System.arraycopy(num, start, result, 0, length);
        return result;
    }

    public static int[] invert(int[] num) {
        for (int i = 0; i < num.length; i++) {
            num[i] *= -1;
        }
        return num;
    }

    public static int[] concatDigit(int[] num, int digit) {
        int[] result = new int[num.length + 1];
        System.arraycopy(num, 0, result, 1, num.length);
        result[0] = digit;
        return result;
    }

    public static int[] GetRidOfZero(int[] num) {
        int[] result = num;
        while (result.length >= 2 && result[result.length - 1] == 0) {
            result = new int[result.length - 1];
            System.arraycopy(num, 0, result, 0, result.length);
        }
        return result;
    }

    public static boolean comparison(int[] num1, int[] num2) {
        if (num1.length > num2.length) {
            return true;
        }
        if (num1.length < num2.length) {
            return false;
        }
        for (int i = num1.length - 1; i >= 0; i--) {
            if (num1[i] > num2[i]) {
                return true;
            }
            if (num1[i] < num2[i]) {
                return false;
            }
        }
        return true;
    }

    //oneDigit normalisation
    public static int[] normalise(int[] num) {
        int p = 0;
        for (int i = 0; i < num.length - 1; i++) {
            num[i] += p;
            p = 0;
            if (num[i] >= m) {
                p = num[i] / m;
                num[i] %= m;
            }
            if (num[i] < 0) {
                p = (int) Math.floor(1.0 * num[i] / m);
                num[i] -= p * m;
            }
        }
        if (p == 0) {
            return GetRidOfZero(num);
        }
        if (p < 0) {
            int[] result = new int[num.length + 1];
            System.arraycopy(num, 0, result, 0, num.length);
            result = GetRidOfZero(result);
            result[result.length - 1] += p;
            return (result);
        }
        int[] result = new int[num.length + 1];
        System.arraycopy(num, 0, result, 0, num.length);
        result[num.length - 1] = p;
        return GetRidOfZero(result);
    }

    //multiDigit normalise
    /*public static int[] normalisation(int[] num) {
        int p = 0;
        for (int i = 0; i < num.length - 1; i++) {
            num[i] += p;
            p = 0;
            if (num[i] >= m) {
                p = num[i] / m;
                num[i] %= m;
            }
            if (num[i] < 0) {
                p = (int) Math.floor(1.0 * num[i] / m);
                num[i] -= Math.abs(p * m);
            }
        }
        if (p == 0) {
            return num;
        }
        int[] result = new int[num.length];
        if (p > 0) {
            result = new int[num.length + p / m + 1];
            for (int i = 0; i < num.length; i++) {
                result[i] = num[i];
            }
            for (int i = num.length; i < result.length; i++) {
                result[i] = p % m;
                p /= m;
            }
            return result;
        }
        for (int i = 0; i < num.length - 1; i++) {
            result[i] = num[i];
        }
        result[result.length - 1] = num[result.length - 1] + p;
        return result;
    }*/

    public static int[] add(int[] num1, int[] num2) {
        int[] result = new int[Math.max(num1.length, num2.length)];
        for (int i = 0; i < result.length; i++) {
            int sum = getDigit(num1, i) + getDigit(num2, i);
            result[i] = sum;
        }
        return normalise(result);
    }

    public static int[] substract(int[] num1, int[] num2) {
        int[] result = new int[Math.max(num1.length, num2.length)];
        for (int i = 0; i < result.length; i++) {
            int sum = getDigit(num1, i) - getDigit(num2, i);
            result[i] = sum;
        }
        return normalise(result);
    }

    public static int[] multDigit(int[] num, int digit, int shift) {
        int[] result = new int[num.length + shift + 1];
        for (int i = 0; i < num.length; i++) {
            result[i + shift] = digit * num[i];
        }
        return normalise(result);
    }

    public static int[] mult(int[] num1, int[] num2) {
        int[] result = new int[num1.length + num2.length];
        for (int i = 0; i < num2.length; i++) {
            int[] temp = multDigit(num1, num2[i], i);
            for (int j = 0; j < temp.length; j++) {
                result[j] += temp[j];
            }
        }
        return result;
    }

    public static int[] multKaracuba(int[] num1, int[] num2) {
        if (num1.length < k + 1 && num2.length < k + 1) {
            return mult(num1, num2);
        }

        // длины
        int num1Length = num1.length;
        int num2Length = num2.length;

        // максимум длины
        int maxNumLength = Math.max(num1Length, num2Length);

        // половина длины с округлением вверх
        int halfMaxNumLength = (maxNumLength / 2) + (maxNumLength % 2);

        // множитель 10 в степени
        int[] multTen = new int[]{1};

        for (int i = 0; i < halfMaxNumLength; i++) {
            multTen = mult(multTen, new int[]{10});
        }

        // 4 куска
        int[] a = new int[num1Length - halfMaxNumLength];
        System.arraycopy(num1, num1Length - halfMaxNumLength - 1, a, 0, num1Length - halfMaxNumLength);
        int[] b = new int[halfMaxNumLength];
        System.arraycopy(num1, 0, b, 0, halfMaxNumLength);
        int[] c = new int[num2Length - halfMaxNumLength];
        System.arraycopy(num2, num2Length - halfMaxNumLength - 1, c, 0, num1Length - halfMaxNumLength);
        int[] d = new int[halfMaxNumLength];
        System.arraycopy(num2, 0, d, 0, halfMaxNumLength);


        // 3 произведения
        int[] z0 = mult(a, c);
        int[] z1 = mult(add(a, b), add(c, d));
        int[] z2 = mult(b, d);

        int[] result = add(mult(z0, mult(multTen, multTen)), add(z2, mult(multTen, substract(z1, add(z0, z2)))));

        return result;
    }

    public static int[] divDigit(int[] num, int digit) {
        ArrayList<Integer> res = new ArrayList<>();
        int r = 0;
        for (int i = num.length - 1; i >= 0; i--) {
            int temp = num[i] + r * m;
            res.add(0, temp / digit);
            r = temp % digit;
        }
        int[] result = new int[res.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = res.get(i);
        }
        return normalise(result);
    }

    public static int divSameLength(int[] num1, int[] num2) {
        int result;
        int a = 0;
        int b = m;
        while (b - a > 1) {
            int d = (a + b) / 2;
            int[] mult = multDigit(num2, d, 0);
            if (mult == num1) {
                return d;
            } else if (comparison(mult, num1)) {
                b = d;
            } else {
                a = d;
            }
        }
        result = a;
        return result;
    }

    public static int[] divDiffLength(int[] num1, int[] num2) {
        if (!comparison(num1, num2)) {
            return new int[]{0};
        }
        int length = num2.length;
        int[] result = new int[num1.length - length + 1];
        int[] part = getPart(num1, num1.length - length, length);

        for (int i = num1.length - length; i >= 0; i--) {
            result[i] = divSameLength(part, num2);
            part = substract(part, multDigit(num2, result[i], 0));
            part = GetRidOfZero(part);
            if (i - 1 >= 0) {
                part = concatDigit(part, num1[i - 1]);
            }
        }

//        result[num1.length - num2.length] = divSameLength(part, num2);
//        for(int i = num1.length - num2.length - 1; i >= 0; i--){
//            part = concatDigit(substract(part, multDigit(num2, result[i + 1], 0)), getDigit(num1, i));
//            result[i] = divSameLength(part, num2);
//        }

        return normalise(result);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input1 = sc.nextLine();
        String input2 = sc.nextLine();
        int[] interp1 = toNum(input1);
        int[] interp2 = toNum(input2);

        int[] num = divDiffLength(interp1, interp2);
        System.out.println(toString(num));
    }
}
