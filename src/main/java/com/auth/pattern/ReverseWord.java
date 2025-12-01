package com.auth.pattern;

import java.util.Scanner;

public class ReverseWord {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        System.out.println(reverse(s));
    }

    public static String reverse(String s) {
        String[] words = s.split(" ");
            String rev = "";
            for (int i = words.length - 1; i >= 0; i--) {
                rev = rev + words[i]+ " ";
            }

        return rev;
    }
}
