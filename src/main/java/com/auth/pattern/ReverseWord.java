package com.auth.pattern;

import java.util.Scanner;
/*
                    input : My name 934u938; kldfjd @#$ ram
                    output: ram  kldfjd u name My
 */
public class ReverseWord {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        System.out.println(reverse(s));
    }

    public static String reverse(String s) {

        // Remove all numbers and special characters, keep only letters and spaces
        s = s.replaceAll("[^A-Za-z ]", "");

        String[] words = s.trim().split(" ");

        String rev = "";
        for (int i = words.length - 1; i >= 0; i--) {
            rev = rev + words[i] + " ";
        }

        return rev.trim();
    }
}
