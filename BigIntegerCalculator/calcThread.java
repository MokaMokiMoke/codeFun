package de.oth.fsim;

import java.math.BigInteger;
import java.util.ArrayList;

public class calcThread extends Thread {

	int[][] values;
	int numberOfThreads, entriesPerThread, threadID;
	ArrayList<int[]> solutionList;

	public calcThread(int[][] values, int numberOfThreads, ArrayList<int[]> solutionList) {
		this.values = values;
		this.numberOfThreads = numberOfThreads;
		entriesPerThread = this.values.length / numberOfThreads;
		this.threadID = (int) this.getId() % numberOfThreads;
		this.solutionList = solutionList;
	}

	public synchronized void run() {
		System.out.println("Thread " + threadID + " started with " + entriesPerThread + " Data sets!");

		BigInteger a = new BigInteger("0"), max = new BigInteger("0"), big;
		int i, b, maxA = 0, maxB = 0;

		for (i = 0; i < entriesPerThread; i++) {
			a = BigInteger.valueOf(values[threadID * entriesPerThread + i][0]);
			b = values[threadID * entriesPerThread + i][1];
			big = a.pow(b);

			if (big.compareTo(max) == 1) {
				max = big;
				maxB = b;
				maxA = Integer.parseInt(a.toString());
			}

			int result[] = { maxA, maxB };
			solutionList.add(result);
		}
	}
}
