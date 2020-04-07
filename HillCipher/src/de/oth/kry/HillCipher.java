package de.oth.kry;

public class HillCipher {

    public static Matrix2x1 multiply(Matrix2x2 k, Matrix2x1 p) {

        Matrix2x1 result = new Matrix2x1(p.modulo);
        result.setP1(k.getA11() * p.getP1() + k.getA12() * p.getP2());
        result.setP2(k.getA21() * p.getP1() + k.getA22() * p.getP2());

        return result;
    }

    public static Matrix3x1 multiply(Matrix3x3 k, Matrix3x1 p) {

        Matrix3x1 result = new Matrix3x1(p.modulo);
        result.setP1(k.getA11() * p.getP1() + k.getA12() + p.getP2()
                + k.getA13() * p.getP3());

        result.setP2(k.getA21() * p.getP1() + k.getA22() * p.getP2()
                + k.getA23() * p.getP3());

        result.setP3(k.getA31() * p.getP1() + k.getA32() * p.getP2()
                + k.getA33() * p.getP3());

        return result;

    }

    public static void main(String[] args) {

        final int mod = 26;

        // 2x2 Matrix mit Schlüssel
        Matrix2x2 key = new Matrix2x2(11, 8, 3, 7, mod);
        // TODO: Schlüsselmatrix bestimmen für Beispiel
        Matrix3x3 key3x3 = new Matrix3x3(1, 2, 3, 1, 2, 3, 1, 2, 3, mod);

        // Zu verschlüsselnder Plaintext
        Matrix2x1 plainA = new Matrix2x1(9, 20, mod);
        Matrix2x1 plainB = new Matrix2x1(11, 8, mod);

        // TODO: Guten Plaintext wählen (invertierbar)
        Matrix3x1 plainC = new Matrix3x1(17, 3, 5, mod);
        Matrix3x1 plainD = new Matrix3x1(11, 3, 7, mod);

        // Verschlüsselung
        Matrix2x1 resultA = multiply(key, plainA);
        Matrix2x1 resultB = multiply(key, plainB);

        Matrix3x1 resultC = multiply(key3x3, plainC);

        // Prüf-Matrix mit korrekten Ergebnis
        Matrix2x1 checkA = new Matrix2x1(25, 11, mod);
        Matrix2x1 checkB = new Matrix2x1(3, 11, mod);

        // TODO Berechnete Werte eintragen
        Matrix3x1 checkC = new Matrix3x1(1, 2, 3, mod);

        // Uhr zum Messen der Laufzeit
        Watch programWatch = new Watch();
        startWatch(programWatch);

        System.out.println("### Start ###");
        System.out.printf("Eingabe Plaintext A: \t %s\n", plainA);
        System.out.printf("Eingabe Plaintext B: \t %s\n", plainB);
        System.out.printf("Ciphertext A: \t\t %s\n", checkA);
        System.out.printf("Ciphertext B: \t\t %s\n", checkB);
        System.out.print("\n");

        // Schleifen, die alle möglichen Werte im Key probieren
        int keyCounter = 0;
        int foundKeys = 0;
        int countDetInveriterbar = 0;

        for (int a = 0; a < mod; a++) {
            for (int b = 0; b < mod; b++) {
                for (int c = 0; c < mod; c++) {
                    for (int d = 0; d < mod; d++) {
                        for (int e = 0; e < mod; e++) {
                            for (int f = 0; f < mod; f++) {
                                for (int g = 0; g < mod; g++) {
                                    for (int h = 0; h < mod; h++) {
                                        for (int i = 0; i < mod; i++) {

                                            // Status Ausgabe
                                            if (keyCounter % 250e6 == 0 && keyCounter > 0) {
                                                // DEBUG
                                                System.out.printf("\nFortschritt: i: %2d, j: %2d, k: %2d, l: %2d, keyCounter: %,6d\n", a, b, c, d, keyCounter);

                                                // Neues Schlüsselpaar erzeugen und testen
                                                Matrix3x3 newKey = new Matrix3x3(a, b, c, d, e, f, g, h, i, mod);

                                                if (checkA.equals(multiply(newKey, plainC)) && checkB.equals(multiply(newKey, plainD))) {
                                                    System.out.printf("Schlüssel #%3d gefunden: %s, Determinante: %4d = %3d (mod %2d), Invertierbar: %5s\n",
                                                            ++foundKeys, newKey, newKey.computeDeterminante(), newKey.computeDeterminanteModus(), newKey.modulo, newKey.determinanteInvertierbar());

                                                    if (newKey.determinanteInvertierbar()) {
                                                        countDetInveriterbar++;
                                                    }
                                                }

                                            }

                                            keyCounter++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
//
//            for (int i = 0; i < mod; i++) {
//
//                for (int j = 0; j < mod; j++) {
//
//                    for (int k = 0; k < mod; k++) {
//
//                        for (int l = 0; l < mod; l++) {
//
//                            // Status Ausgabe
//                            if (keyCounter % 75e3 == 0 && keyCounter > 0) {
//                                // DEBUG
//                                // System.out.printf("\nFortschritt: i: %2d, j: %2d, k: %2d, l: %2d, keyCounter: %,6d\n", i, j, k, l, keyCounter);
//                            }
//
//                            // Neues Schlüsselpaar erzeugen und testen
//                            Matrix2x2 newKey = new Matrix2x2(i, j, k, l, mod);
//
//                            if (checkA.equals(multiply(newKey, plainA)) && checkB.equals(multiply(newKey, plainB))) {
//                                System.out.printf("Schlüssel #%3d gefunden: %s, Determinante: %4d = %3d (mod %2d), Invertierbar: %5s\n",
//                                        ++foundKeys, newKey, newKey.computeDeterminante(), newKey.computeDeterminanteModus(), newKey.modulo, newKey.determinanteInvertierbar());
//
//                                if (newKey.determinanteInvertierbar()) {
//                                    countDetInveriterbar++;
//                                }
//
//                            }
//
//                            keyCounter++;
//                        }
//                    }
//                }
//
//            }

            System.out.printf("\n#### Bericht #####\n");
            System.out.printf("Anzahl der versuchten Schlüssel: \t%,6d\n", keyCounter);
            System.out.printf("Anzahl der gefunden Schlüssel: \t%,6d \t%f Prozenzt\n", foundKeys, (float) foundKeys / keyCounter * 100);
            System.out.printf("Anzahl der invertierbaren Schlüssel: \t%,6d \t%f Prozent\n", countDetInveriterbar, (float) countDetInveriterbar / keyCounter * 100);

            // Stop Watch
            stopWatch(programWatch);

            System.out.printf("Result: %,d", 26 ^ 9);

        }

    }

    private static void startWatch(Watch watch) {
        watch.start();
        System.out.printf("The Start time is: %s\n", watch.getStartTimePretty());
    }

    private static void stopWatch(Watch programWatch) {
        programWatch.stop();
        System.out.printf("The Stop time is : %s\n", programWatch.getStopTimePretty());
        System.out.printf("Time processed   : %s\n", programWatch.getRuntimePretty());
        System.out.printf("Miliseconds needed: %,d\n", programWatch.getRuntime());
    }

}
