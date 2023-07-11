//Butkovskiy Vlad
//18.03.2022
// Division of the numbers of the same length

import java.util.Scanner;

public class division {

    public static final int k = 1;
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

    public static int[] invert(int[] num) {
        for (int i = 0; i < num.length; i++) {
            num[i] *= -1;
        }
        return num;
    }

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
        int[] result = new int[num.length + 1];
        System.arraycopy(num, 0, result, 0, num.length);
        result = GetRidOfZero(result);
        result[result.length - 1] += p;
        return (result);

//        ArrayList<Integer> res = new ArrayList<>();
//        for (int j : num) {
//            res.add(j);
//        }
//        while (p != 0) {
//            res.add(p % m);
//            p /= m;
//        }
//        int[] num1 = new int[res.size()];
//        for (int i = 0; i < res.size(); i++) {
//            num1[i] = res.get(i);
//        }
//        return num1;
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
        return false;
    }

    public static int[] concatDigit(int[] num, int digit) {
        int[] result = new int[num.length + 1];
        System.arraycopy(num, 0, result, 1, num.length);
        result[0] = digit;
        return result;
    }

    public static int[] GetRidOfZero(int[] num) {
        int[] result = num;
        while (result.length >= 2 && result[result.length - 1] == 0){
            result = new int[result.length - 1];
            System.arraycopy(num, 0, result, 0, result.length);
        }
        return result;
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

    public static int[] multDigit(int[] num, int digit) {
        int[] result = new int[num.length];
        for (int i = 0; i < num.length; i++) {
            result[i] = digit * num[i];
        }
        return normalise(result);
    }

    public static void test() {
        for (int i = 1000; i < 10000; i++) {
            for (int j = 1000; j < 10000; j++) {
                if (divSameLength(toNum(Integer.toString(i)), toNum(Integer.toString(j))) != i / j) {
                    System.out.println("error");
                    System.out.println("i: " + i + " j: " + j);
                    System.exit(1029238);
                }
                if (divSameLength(toNum(Integer.toString(i)), toNum(Integer.toString(j))) / 10 != 0) {
                    System.out.println("error");
                    System.out.println("i: " + i + " j: " + j);
                    System.exit(1029238);
                }
            }
        }
        System.out.println("OK");
    }

    public static int divSameLength(int[] num1, int[] num2) {
        int result;
        int a = 0;
        int b = m;
        while (b - a > 1) {
            int d = (a + b) / 2;
            int[] mult = multDigit(num2, d);
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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input1 = sc.nextLine();
        String input2 = sc.nextLine();
        int[] interp1 = toNum(input1);
        int[] interp2 = toNum(input2);

        int[] num = add(interp1, interp2);
        System.out.println(toString(num));
    }
}
