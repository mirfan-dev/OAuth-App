package com.auth.pattern;

import java.util.Scanner;

/*
       Enter the Kth rotation : 3
        output: 5 6 7 1 2 3 4
 */

public class RotatedArray {


    public static void main(String[] args) {
        Scanner sc =new Scanner(System.in);
        System.out.println("Enter the Kth rotation");
        int k=sc.nextInt();
        int[] arr={1,2,3,4,5,6,7};
        rotatedArray(arr,k);

        // Print rotated array
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
    public static void rotatedArray(int[] nums,int k){
        reverse(nums,0,nums.length-1);
        reverse(nums,0,k-1);
        reverse(nums,k,nums.length-1);
    }


    public static void reverse(int[] arr,int i,int j){

        while (i<j){
            int temp=arr[i];
            arr[i]=arr[j];
            arr[j]=temp;

            i++;
            j--;
        }
    }
}
