package com.auth.pattern;

import java.util.Scanner;
/*
        Enter upper loop number
        5
        Enter lower loop number
        6
        * * * * * *
        * * * * * *
        * * * * * *
        * * * * * *
        * * * * * *
 */
public class SolidPattern2 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter upper loop number");
        int n=sc.nextInt();
        System.out.println("Enter lower loop number");
        int m=sc.nextInt();
        pattern(n,m);
    }
    public static void pattern(int n,int m){
        for (int i=1;i<=n;i++){
            for (int j=1;j<=m;j++){
                System.out.print("* ");
            }
            System.out.println();
        }
    }
}