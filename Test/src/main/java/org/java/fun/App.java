/**
 * Created by krzys on 2/28/2017.
 */
package org.java.fun;

public class App
{
    public static void main(String[] args)
    {
        System.out.println("Hello world");
        System.out.println("Hi Travis!");

        System.out.println(isFive(5) ? "Yup" : "Nope");
        System.out.println(isFive(6) ? "Yup" : "Nope");

        System.out.println("Jesteś januszem.");
    }

    protected static boolean isFive(int x) {
        return x == 5;
    }

    public static boolean isTrue()
    {
        return true;
    }

    public static void functionJanek() { System.out.println("To moja funkcja"); }
}