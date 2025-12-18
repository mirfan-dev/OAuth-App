package com.auth.pattern;

import java.util.Scanner;

public class FibnocciSeriesPattern {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of rows:");
        int n = sc.nextInt();
        pattern(n);
    }

    public static void pattern(int n) {

        int a = 0, b = 1;

        for (int i = 1; i <=n; i++) {   // rows
            for (int j = 1; j <= i; j++) {  // print i numbers
                System.out.print(a + " ");
                int c = a + b;
                a = b;
                b = c;
            }
            System.out.println();
        }
    }
}
