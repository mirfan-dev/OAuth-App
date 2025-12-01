package com.auth.pattern;

import java.util.Scanner;

/*
        1
        2 3
        4 5 6
        7 8 9 10
        11 12 13 14 15
 */

public class SolidPattern5 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter a number");
        int n=sc.nextInt();
        pattern(n);
    }
    public static void pattern(int n){
        int number=1;
        for (int i=1;i<=n;i++){
            for (int j=1;j<=i;j++){
                System.out.print(number+" ");
                number++;
            }
            System.out.println();
        }
    }
}