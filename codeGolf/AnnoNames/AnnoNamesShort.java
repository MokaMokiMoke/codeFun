package de.tud.codeGolf;

public class AnnoNamesShort {

    public static void main(String[] args) {
        int ctr = 0;
        for (int i = 1000; i <= 1e4; i++) {
            if (qsum(i) != 9 || !String.valueOf(i).contains("0"))
                continue;
            System.out.printf("%d ", i + ctr++);
        }
        System.out.println("\nAnno Games: " + ctr);
    }

    public static int qsum(int num) {
        int sum = 0;
        while (0 != num) {
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }
}
