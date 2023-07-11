package Archivator_but_better;


import java.io.*;
import java.nio.ByteBuffer;

public class Main1 {
    public static final int N = 256;
    public static final int esc = 257;

    // функции поддержки
    public static int[] getIndexOfTwoMin(Node[] tree) {
        int[] min = new int[2];
        min[0] = tree.length - 1;
        min[1] = tree.length - 1;
        for (int i = 0; i < tree.length; i++) {
            if (tree[i].value < tree[min[0]].value && tree[i].isFree) {
                min[0] = i;
            }
        }
        for (int i = 0; i < tree.length; i++) {
            if (tree[i].value < tree[min[1]].value && i != min[0] && tree[i].isFree) {
                min[1] = i;
            }
        }
        return min;
    }

    public static byte[] intToBytes(int a) {
        return ByteBuffer.allocate(Integer.BYTES).putInt(a).array();
    }

    public static int bytesToInt(byte[] bytes) {
        return ByteBuffer.allocate(Integer.BYTES).put(bytes).rewind().getInt();
    }

    public static boolean[] byteToBit(byte b) {
        boolean[] result = new boolean[Byte.SIZE];
        for (int j = 0; j < Byte.SIZE; j++) {
            result[j] = (0 != (b & (1 << (Byte.SIZE - j - 1))));
        }
        return result;
    }

    public static void writeAsBytes(int a, OutputStream os) throws IOException {
        for (byte b : intToBytes(a)) {
            os.write(b);
        }
    }


    // рабочие функции

    public static int getM(int[] counts) {
        int m = 1;
        for (int num : counts) {
            if (num > 0) {
                m++;
            }
        }
        return m;
    }

    public static Node[] buildTree(int[] counts) {
        int M = getM(counts);
        Node[] tree = new Node[2 * M - 1];
        for (int i = 0; i < 2 * M - 1; i++) {
            tree[i] = new Node();
        }

        // setTreeCodes

        // first layer (leaves)
        int k = 0;
        for (int i = N + 1; i >= 0; i--) {
            if (counts[i] > 0 || i == N + 1) {
                tree[k].symbol = i;
                tree[k].value = counts[i];
                tree[k].isFree = true;
                k++;
            }
        }

        // the rest of the tree
        for (int i = 0; i < M - 1; i++) {
            int[] min = getIndexOfTwoMin(tree);
            tree[k].value = tree[min[0]].value + tree[min[1]].value;
            tree[k].left = min[0];
            tree[k].right = min[1];
            tree[k].isFree = true;
            tree[min[0]].isFree = false;
            tree[min[1]].isFree = false;
            k++;
        }

        // Assigning codes

        int root = 2 * M - 2;
        for (int i = root; i >= M; i--) {
            tree[tree[i].left].code = tree[i].code + "0";
            tree[tree[i].right].code = tree[i].code + "1";
        }
        tree[root].code = null;

        printCodesCounts(tree, getM(counts));
        System.out.println("______________");
//        printTree(tree);

        return tree;
    }

    public static String[] getCodes(int[] counts, Node[] tree) {
        String[] codes = new String[N + 2];
        codes[esc] = tree[0].code;
        codes[N] = tree[1].code;
        int k = 2;
        for (int i = 255; i >= 0; i--) {
            if (counts[i] == 0) {
                codes[i] = "";
            } else {
                codes[i] = tree[k].code;
                k++;
            }
        }
        return codes;
    }

////////////////////////////////

    public static void writeTable(OutputStream os, int M, int[] counts) throws IOException {
        os.write(M - 2);
        for (int i = 0; i < counts.length - 1; i++) {
            if (counts[i] > 0) {
                os.write(i);
                writeAsBytes(counts[i], os);
            }
        }
    }

    public static int[] readTable(InputStream is) throws IOException {
        int[] counts = new int[N + 1];
        int m = is.read();
        assert m != -1 : "End of stream is reached";
        m++;
        for (int i = 0; i < m; i++) {
            int symbol = is.read();
            byte[] tmp = new byte[Integer.BYTES];
            for (int j = 0; j < Integer.BYTES; j++) {
                tmp[j] = (byte) is.read();
            }
            counts[symbol] = bytesToInt(tmp);
        }
        counts[N] = 1;
        return counts;
    }

////////////////////////////////

    public static void writeEncoded(InputStream is, OutputStream os, int[] counts, Node[] tree) throws IOException {
        int data = is.read();
        String result = "";
        os.write(data);
        counts[data]++;
        tree = buildTree(counts);
        String[] codes = getCodes(counts, tree);

        data = is.read();
        while (data != -1) {

            // если символ уже был в файле
            if (counts[data] > 0) {
                // запишем символ
                result = result + codes[data];

                while (result.length() >= 8) {
                    String tmp = result.substring(0, 8);
                    int res = Integer.parseInt(tmp, 2);
                    os.write(res);
                    result = result.substring(8);
                }

                counts[data]++;
                tree = buildTree(counts);
                codes = getCodes(counts, tree);
            } else {
                // если символ в файле не встречался

                // запишем символ нового символа
                result = result + codes[esc];

                while (result.length() >= 8) {
                    String tmp = result.substring(0, 8);
                    int res = Integer.parseInt(tmp, 2);
                    os.write(res);
                    result = result.substring(8);
                }

                if (result.length() > 0) {
                    int count = 0;
                    while (result.length() < 8) {
                        result = result + codes[N].charAt(count);
                        count++;
                        if (count >= codes[N].length()) {
                            count = 0;
                        }
                    }
                    os.write(Integer.parseInt(result, 2));
                    result = "";
                }

                // запишеем сам символ
                os.write(data);

                counts[data]++;
                tree = buildTree(counts);
                codes = getCodes(counts, tree);
            }

            data = is.read();
        }
        // конец файла
        if (result.length() > 0) {
            int count = 0;
            while (result.length() < 8) {
                result = result + codes[N].charAt(count);
                count++;
                if (count >= codes[N].length()) {
                    count = 0;
                }
            }
            os.write(Integer.parseInt(result, 2));
        }
    }

    public static void readEncoded(InputStream is, OutputStream os, int[] counts, Node[] tree) throws IOException {
        int data = is.read();
        os.write((char) data);
        counts[data]++;
        tree = buildTree(counts);

        int pos = tree.length - 1;
        boolean newChar = false;
        data = is.read();
        while (data != -1) {
            if (newChar) {
                counts[data]++;
                tree = buildTree(counts);
                os.write((char) data);
                pos = tree.length - 1;
                newChar = false;
            } else {

                for (boolean bit : byteToBit((byte) data)) {
                    if (bit) {
                        pos = tree[pos].right;
                    } else if (!bit) {
                        pos = tree[pos].left;
                    }
                    if (pos == 0) {
                        newChar = true;
                        break;
                    } else if (pos == 1) {
                        pos = tree.length - 1;
                        break;
                    } else if (tree[pos].left == -1) {
                        os.write((char) tree[pos].symbol);
                        counts[tree[pos].symbol]++;
                        tree = buildTree(counts);
                        pos = tree.length - 1;
                    }
                }
            }
            data = is.read();
        }


    }

////////////////////////////////

    public static void printCodesCounts(Node[] tree, int M) {
        System.out.println("M = " + M);
        for (int i = 0; i < M; i++) {
            System.out.println((char) tree[i].symbol + " " + tree[i].value + " " + tree[i].code);
        }
    }

    public static void printTreeChar(Node[] tree) {
        for (Node elem : tree) {
            System.out.println((char) elem.symbol + ": " + elem.value + ", " + elem.isFree + ", " + elem.left + ", " + elem.right + ", " + elem.code);
        }
    }

    public static void printTree(Node[] tree) {
        for (Node elem : tree) {
            System.out.println((char) elem.symbol + " -> " + elem.symbol + ": " + elem.value + ", " + elem.isFree + ", " + elem.left + ", " + elem.right + ", " + elem.code);
        }
        System.out.println("__________");
    }

////////////////////////////////

    public static void archivate(String input, String output) throws IOException {
        int[] counts = new int[N + 2];
        counts[N] = 1;
        Node[] tree = buildTree(counts);

        InputStream is = new FileInputStream(input);
        OutputStream os = new FileOutputStream(output);

        writeEncoded(is, os, counts, tree);

        is.close();
        os.close();
        System.out.println("ARCHIVATION COMPLETE");
    }

    public static void dearchivate(String input, String output) throws IOException {
        int[] counts = new int[N + 2];
        counts[N] = 1;
        Node[] tree = buildTree(counts);

        InputStream is = new FileInputStream(input);
        OutputStream os = new FileOutputStream(output);

        readEncoded(is, os, counts, tree);

        is.close();
        os.close();
    }

////////////////////////////////

    public static void main(String[] args) throws IOException {
        String inpPath = "src/input.txt";
        String archPath = "src/archive.txt";
        String outPath = "src/output.txt";
        archivate(inpPath, archPath);
        dearchivate(archPath, outPath);
    }
}