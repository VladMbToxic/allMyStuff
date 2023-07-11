package Archivator;


import java.io.*;
import java.nio.ByteBuffer;

public class Main {
    public static final int N = 256;

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
    public static int[] getCounts(InputStream is) throws IOException {
        int[] counts = new int[N + 1];
        int data = is.read();
        while (data != -1) {
            counts[data]++;
            data = is.read();
        }
        counts[N] = 1;
        return counts;
    }

    public static int[] getCounts_old(String str) {
        int[] counts = new int[N];
        for (int i = 0; i < str.length(); i++) {
            counts[str.charAt(i) % 256]++;
        }
        return counts;
    }

    public static int getM(int[] counts) {
        int m = 0;
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
        for (int i = 0; i < N + 1; i++) {
            if (counts[i] > 0) {
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
        return tree;
    }

    public static String[] getCodes(int[] counts, Node[] tree) {
        String[] codes = new String[N + 1];
        int k = 0;
        for (int i = 0; i < N + 1; i++) {
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

    public static void writeEncoded(InputStream is, OutputStream os, String[] codes) throws IOException {
        String result = "";
        int data = is.read();
        while (data != -1) {
            result = result + codes[data];
            while (result.length() >= 8) {
                String tmp = result.substring(0, 8);
                int res = Integer.parseInt(tmp, 2);
                os.write(res);
                result = result.substring(8);
            }
            data = is.read();
        }
        if (result.length() > 0) {
            int count = 0;
            // questionable part
            while (result.length() < 8) {
                result = result + codes[codes.length - 1].charAt(count);
                count++;
                if (count >= codes[codes.length - 1].length()) {
                    count = 0;
                }
            }
            os.write(Integer.parseInt(result, 2));
        }
    }

    public static void readEncoded(InputStream is, OutputStream os, Node[] tree) throws IOException {
        int total = 0;
        for (Node a : tree) {
            if (a.symbol != -1 && a.value > 0) {
                total += a.value;
            }
        }
        total--;

        int pos = tree.length - 1;
        int data = is.read();
        int count = 0;
        while (data != -1) {
            for (boolean bit : byteToBit((byte) data)) {
                if (bit) {
                    pos = tree[pos].right;
                } else if (!bit) {
                    pos = tree[pos].left;
                }
                if (tree[pos].symbol != -1) {
                    if (count < total) {
                        os.write((char) tree[pos].symbol);
                        pos = tree.length - 1;
                        count++;
                    } else {
                        break;
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
        //    public static int[] counts = getCounts(inpPath);
        InputStream is = new FileInputStream(input);
        int[] counts = getCounts(is);
        is.close();

        int M = getM(counts);
        Node[] tree = buildTree(counts);
        String[] codes = getCodes(counts, tree);

        printCodesCounts(tree, M);
        System.out.println("______________");
        printTree(tree);

        is = new FileInputStream(input);
        OutputStream os = new FileOutputStream(output);
        writeTable(os, M, counts);
        writeEncoded(is, os, codes);
        is.close();
        os.close();

    }

    public static void dearchivate(String input, String output) throws IOException {
        InputStream is = new FileInputStream(input);
        int[] counts = readTable(is);

        int M = getM(counts);

        OutputStream os = new FileOutputStream(output);
        Node[] tree = buildTree(counts);

        readEncoded(is, os, tree);

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