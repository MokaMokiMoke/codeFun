package oth;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {

		PrimeChecker pc = new PrimeChecker();
		ArrayList<Integer> primeList = new ArrayList<>();
		final int min = 0;
		final double max = Integer.MAX_VALUE;
		ResultPrinter rp = new ResultPrinter(primeList, min, max, pc);

		for (int i = min; i <= max; i++)
			if (pc.checkRecursively(i))
				primeList.add(i);

		rp.printResults();

	}
}
