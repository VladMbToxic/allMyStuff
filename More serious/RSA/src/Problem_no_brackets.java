import java.util.ArrayList;
import java.util.Scanner;

public class Problem_no_brackets {

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

    public static int[] invert(int[] num) {
        for (int i = 0; i < num.length; i++) {
            num[i] *= -1;
        }
        return num;
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
            return num;
        }
        int[] result = new int[num.length + 1];
        System.arraycopy(num, 0, result, 0, num.length);
        result[num.length - 1] = p;
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

    public static int[] multDigit(int[] num, int digit, int shift) {
        int[] result = new int[num.length + shift];
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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        sc.close();

        ArrayList<int[]> numbers = new ArrayList<>();
        ArrayList<Integer> sign = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {

                while (str.charAt(i) != ' ') {
                    sb.append(str.charAt(i));
                    i++;
                }

                // добавили чсило

                numbers.add(toNum(sb.toString()));
                sb = new StringBuilder();

                // если есть два числа, смотрим на след. опреацию
                if (numbers.size() >= 2) {
                    int j = i;
                    while (j < str.length() - 1 && str.charAt(j) == ' ') {
                        j++;
                    }
                    int temp = 0;
                    if (str.charAt(j) == '-') {
                        temp = 1;
                    } else if (str.charAt(j) == '+') {
                        temp = 1;
                    } else if (str.charAt(j) == '*') {
                        temp = 3;
                    }
                    while (numbers.size() >= 2 && sign.get(sign.size() - 1) >= temp) {
                        if (sign.get(sign.size() - 1) == 3) {
                            numbers.add(mult(numbers.get(numbers.size() - 1), numbers.get(numbers.size() - 2)));
                            numbers.remove(numbers.size() - 2);
                            numbers.remove(numbers.size() - 2);
                            sign.remove(sign.size() - 1);
                        } else if (sign.get(sign.size() - 1) == 2) {
                            numbers.add(add(numbers.get(numbers.size() - 1), numbers.get(numbers.size() - 2)));
                            numbers.remove(numbers.size() - 2);
                            numbers.remove(numbers.size() - 2);
                            sign.remove(sign.size() - 1);
                        } else if (sign.get(sign.size() - 1) == 1) {
                            numbers.add(substract(numbers.get(numbers.size() - 2), numbers.get(numbers.size() - 1)));
                            numbers.remove(numbers.size() - 2);
                            numbers.remove(numbers.size() - 2);
                            sign.remove(sign.size() - 1);
                        }
                    }
                }
            } else if (str.charAt(i) != ' ') {
                if (str.charAt(i) == '-') {
                    sign.add(1);
                } else if (str.charAt(i) == '+') {
                    sign.add(2);
                } else if (str.charAt(i) == '*') {
                    sign.add(3);
                }
            }
        }
        System.out.println(toString(numbers.get(0)));
    }
}
