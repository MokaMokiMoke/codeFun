package de.oth.fsim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;

public class algo {

	static boolean parallel = true;
	static final int numberOfThreads = 10;
	final static long timeStart = System.currentTimeMillis();
	static int[] maximum;

	static void readInput(File file, int[][] values) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line;

		for (int i = 0; i < values.length && ((line = br.readLine()) != null); i++) {
			// System.out.println(line + "Step " + i);
			values[i][0] = Integer.parseInt(line.substring(0, line.indexOf(",")));
			values[i][1] = Integer.parseInt(line.substring(line.indexOf(",") + 1, line.length()));
		}
	}

	@SuppressWarnings("unused")
	static void calculate(int[][] values) {

		BigInteger a = new BigInteger("0"), max = new BigInteger("0"), big;
		int i, b, maxA = 0, maxB = 0, brk = 20;

		for (i = 0; i < values.length; i++) {
			a = BigInteger.valueOf(values[i][0]);
			b = values[i][1];
			big = a.pow(b);

			if (big.compareTo(max) == 1) {
				max = big;
				maxB = b;
				maxA = Integer.parseInt(a.toString());
			}
			int[] tmp = { maxA, maxB };
			maximum = tmp;
		}
	}

	@SuppressWarnings("resource")
	static int getNumberOfLines(File file) {
		int numberOfLines = 0;

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			while (in.readLine() != null)
				numberOfLines++;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return numberOfLines;
	}

	public static void main(String[] args) throws IOException {

		// Check if Input Argument is there
		try {
			if (args[0] == null)
				; // NULL Statement
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("Bitte Inputfile als Start Agument angeben!");
			return;
		}

		String inputFile = args[0].toString();
		ArrayList<int[]> solutionList = new ArrayList<int[]>();

		// Input File die per args[0] übergeben wird z.B. "./input/full.txt"
		File file = new File(args[0]);
		int[][] values = new int[getNumberOfLines(file)][2];

		readInput(file, values);

		if (parallel)
			calculateParallel(values, numberOfThreads, solutionList);
		else
			calculate(values);

		printStatistik(inputFile, values, timeStart);
	}

	private static void calculateParallel(int[][] values, int numberOfThreads, ArrayList<int[]> solutionList) {

		ArrayList<calcThread> threadList = new ArrayList<>();

		for (int i = 0; i < numberOfThreads; i++)
			threadList.add(new calcThread(values, numberOfThreads, solutionList));

		for (Thread t : threadList)
			t.start();

		for (Thread t : threadList) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		maximum = selectMax(solutionList);
		return;
	}

	private static int[] selectMax(ArrayList<int[]> solutionList) {

		BigInteger a = new BigInteger("0"), max = new BigInteger("0"), big;
		int b, maxA = 0, maxB = 0;

		for (int[] values : solutionList) {
			a = BigInteger.valueOf(values[0]);
			b = values[1];
			big = a.pow(b);

			if (big.compareTo(max) == 1) {
				max = big;
				maxB = b;
				maxA = Integer.parseInt(a.toString());
			}
		}
		int maximum[] = { maxA, maxB };
		return maximum;
	}

	private static void printStatistik(String inputFile, int[][] values, long timeStart) {
		final long timeEnd = System.currentTimeMillis();
		System.out.println("Ausabe der Ergebniswerte!");
		System.out.println("Groesstes Paerchen = " + maximum[0] + " hoch " + maximum[0]);
		System.out.println("Input File: " + inputFile);
		System.out.println("Anzahl der Paare: " + values.length);
		System.out.printf("Programm Laufzeit: %f Sekunden", (double) (timeEnd - timeStart) / 1000);
	}
}
