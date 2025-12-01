package com.auth.pattern;

import java.util.Scanner;

public class SolidPattern6 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a number");
        int n = sc.nextInt();
        pattern(n);
    }

    public static void pattern(int n) {

        int a = 0, b = 1;

        for (int i = 1; i <= n; i++) {

            int[] row = new int[i];

            for (int j = 1; j <= i; j++) {

                if (j == 1) {
                    row[j] = a;
                } else if (j == 1 || j==2) {
                    row[j] = b;
                } else if (j>3){
                    int c = a + b;
                    row[j] = c;
                    a = b;
                    b = c;
                }
            }

            // LAST ROW REVERSE
            if (i == n) {
                for (int k = i - 1; k >= 0; k--) {
                    System.out.print(row[k] + " ");
                }
            } else {
                for (int k = 0; k < i; k++) {
                    System.out.print(row[k] + " ");
                }
            }

            System.out.println();
        }
    }
}
