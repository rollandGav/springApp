package com.springApp.jpa;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ExceptionExamples {
    public static void main(String[] args) {
//        scannerExample();
        printCharAt("Hello",5  );
//        printCharAt("hello", 3);
    }

    public static void printCharAt(String text, int index){
        try {
            System.out.println("char at: " + index + " is: " + text.charAt(index));
        }catch (IndexOutOfBoundsException e){
            System.out.println("index out of Bounds");
        }
    }

    public static void scannerExample(){
        try {
        Scanner scanner = new Scanner(System.in);
        System.out.println("insert 2 numbers");
        int nr1 = scanner.nextInt();
        int nr2 = scanner.nextInt();
        int result = nr1 / nr2;
        System.out.println("result: " + result);

        } catch (ArithmeticException e){
            System.out.println("error, you cannot divide numbers by 0" + e.getMessage());
        }catch (InputMismatchException ex){
            System.out.println("inputMismatchException");
        }finally {
            System.out.println("all exception are catched.");
        }

    }

}
