import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static int MATCH;
    private static int MISMATCH;
    private static int GAP;
    private static String sX;
    private static String sY;


    public static void main(String[] args) {

        // input of the match, mismatch and gap penalties
        inputPenalties();

        // input of the two sequences
        inputSequences();

        // scoring the matrix
        int[][] matrix = scoreMatrix(sX, sY);

        // finding the highest score in the matrix
        int x = 0;
        int y = 0;
        int max = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] > max) {
                    max = matrix[i][j];
                    x = i;
                    y = j;
                }
            }
        }

        // traceback to find the alignment
        List<Character> xList = new ArrayList<>();
        List<Character> yList = new ArrayList<>();
        while (matrix[x][y] != 0) {
            if (matrix[x][y] == matrix[x - 1][y] + GAP) {
                xList.add(sX.charAt(x - 1));
                yList.add('-');
                x--;
            } else if (matrix[x][y] == matrix[x][y - 1] + GAP) {
                xList.add('-');
                yList.add(sY.charAt(y - 1));
                y--;
            }
            if (matrix[x][y] == matrix[x - 1][y - 1] + MATCH) {
                xList.add(sX.charAt(x - 1));
                yList.add(sY.charAt(y - 1));
                x--;
                y--;
            } else if (matrix[x][y] == matrix[x - 1][y - 1] + MISMATCH) {
                xList.add(sX.charAt(x - 1));
                yList.add(sY.charAt(y - 1));
                x--;
                y--;

            }
        }

        // reverse the lists
        reverseArrayList(xList);
        reverseArrayList(yList);

        // print the sequence alignment
        int row = 0;
        int matchCount = 0;
        outerloop:
        while (true) {
            for (int i = 0; i < 100; i++) {
                if (row + i > xList.size() - 1) {
                    break;
                }
                System.out.print(xList.get(i + row));
            }
            System.out.println("");
            for (int i = 0; i < 100; i++) {
                if (row + i > yList.size() - 1) {
                    break;
                }
                if (xList.get(i + row) == yList.get(i + row)) {
                    System.out.print("|");
                    matchCount++;
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println("");
            for (int i = 0; i < 100; i++) {
                if (row + i > yList.size() - 1) {
                    break outerloop;
                }
                System.out.print(yList.get(i + row));
            }
            row = row + 100;
            System.out.println("\n\n");
        }
        double percent = (double) matchCount / (double) Math.max(sX.length(), sY.length()) * 100;
        System.out.printf("\n\nmatch: %.2f%%", percent);
    }

    private static boolean isNOTValidSequence(String s1) {
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != 'A' && s1.charAt(i) != 'C' && s1.charAt(i) != 'G' && s1.charAt(i) != 'T') {
                System.out.println("Invalid sequence. Please enter a valid sequence.");
                return true;
            }
        }
        return false;
    }

    private static int[][] scoreMatrix(String first, String second) {
        int[][] matrix = new int[first.length() + 1][second.length() + 1];

        for (int i = 1; i < first.length() + 1; i++) {
            for (int j = 1; j < second.length() + 1; j++) {
                int match = matrix[i - 1][j - 1] + (first.charAt(i - 1) == second.charAt(j - 1) ? MATCH : MISMATCH);
                int left = matrix[i][j - 1] + GAP;
                int up = matrix[i - 1][j] + GAP;
                matrix[i][j] = Math.max(0, Math.max(match, Math.max(left, up)));
            }
        }
        return matrix;
    }

    public static void reverseArrayList(List<Character> alist) {
        for (int i = 0; i < alist.size() / 2; i++) {
            Character temp = alist.get(i);
            alist.set(i, alist.get(alist.size() - i - 1));
            alist.set(alist.size() - i - 1, temp);
        }
    }

    private static void inputPenalties() {
        System.out.println("Enter the match score:");
        MATCH = sc.nextInt();
        System.out.println("Enter the mismatch penalty:");
        MISMATCH = sc.nextInt();
        System.out.println("Enter the gap penalty:");
        GAP = sc.nextInt();
    }

    private static void inputSequences() {
        sX = sc.nextLine();
        do {
            System.out.println("Enter the first sequence:");
            sX = sc.nextLine();
        } while (isNOTValidSequence(sX));

        do {
            System.out.println("Enter the second sequence:");
            sY = sc.nextLine();
        } while (isNOTValidSequence(sY));
    }
}