package org.example.utils;

import org.example.entity.User;

import java.util.Scanner;

public class Utils {
    static Scanner numberScanner = new Scanner(System.in);
    static Scanner textScanner = new Scanner(System.in);
    public static User currentUser;

    public static int getNumber(String text) {
        System.out.print(text + ": ");
        return numberScanner.nextInt();
    }

    public static double getFloat(String text) {
        System.out.print(text + ": ");
        return numberScanner.nextDouble();
    }

    public static String getText(String text) {
        System.out.print(text + ": ");
        return textScanner.nextLine();
    }

}
