package de.tud.codeGolf;

public class AnnoNamesBasic {

    public static void main(String[] args) {

        int counter = 0;
        for (int i = 1000; i <= 9999; i = i + 1) {
            if (quersumme(i) != 9) {
                continue;
            }

            if (containsZero(i) == false) {
                continue;
            }
            
            System.out.println(i);
            counter = counter + 1;

        }

        System.out.print("Anno Games: ");
        System.out.printf(counter);
    }

    public static int quersumme(int zahl) {
        
        int summe = 0;
        while (0 != zahl) {
            summe = summe + (zahl % 10);
            zahl = zahl / 10;
        }
        
        return summe;
    }

    private static boolean containsZero(int zahl) {
        
        boolean val = false;
        char[] charArray = String.valueOf(zahl).toCharArray();
        for (int i = 0; i < charArray.length; i = i + 1) {
            if (charArray[i] == '0') {
                val = true;
            }
        }

        return val;
    }

}
