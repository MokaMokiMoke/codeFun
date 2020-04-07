package oth;

import java.util.ArrayList;
import java.util.Calendar;

public class ResultPrinter {

	private ArrayList<Integer> myList;
	private int min, max;
	private PrimeChecker pc;
	private long startTime, endTime;

	public ResultPrinter(ArrayList<Integer> primeList, int min, double max, PrimeChecker pc) {

		myList = primeList;
		this.min = min;
		this.max = (int) max;
		this.pc = pc;
		startTime = System.currentTimeMillis();

	}

	private void printSingleValue(double value, String name) {
		System.out.printf("The function \"%s\" \nwas used \t%d times\n\n", name, (int) value);
	}

	private void printTimeNeeded() {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(startTime);
		endTime = System.currentTimeMillis();

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int msecond = calendar.get(Calendar.MILLISECOND);

		System.out.printf("Start Time: %2d:%2d:%2d and %3dms\n", hour, minute, second, msecond);

		calendar.setTimeInMillis(endTime);

		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);
		second = calendar.get(Calendar.SECOND);
		msecond = calendar.get(Calendar.MILLISECOND);
		System.out.printf("Start Time: %2d:%2d:%2d and %3dms\n", hour, minute, second, msecond);
		System.out.printf("Execution time: %dms", endTime - startTime);

	}

	public void printResults() {

		// print all found primes
		System.out.println("======== Full primes found ========");
		printList();
		System.out.println("");

		// print Statistic head
		System.out.println("============ Statistic ============");
		System.out.printf("The input range was %d to %d\n", min, max);
		System.out.printf("There were %d full primes\n\n", myList.size());

		// print function call values
		System.out.println("========= Function Calls =========");
		printSingleValue(pc.getCheckRecursivelyUsed(), "checkRecursivelyUsed");
		printSingleValue(pc.getChecksumIsPrimeUsed(), "checksumIsPrime");
		printSingleValue(pc.getChecksumUsed(), "checksum");
		printSingleValue(pc.getEveryDigitIsPrimeUsed(), "everyDigitIsPrime");
		printSingleValue(pc.getIntToArrayUsed(), "intToArray");
		printSingleValue(pc.getInverseChecksumUsed(), "inverseChecksum");
		printSingleValue(pc.getInverseChecksumIsPrimeUsed(), "inverseChecksumIsPrime");
		printSingleValue(pc.getIsPrimeUsed(), "isPrime");
		printSingleValue(pc.getIsSingleDigitUsed(), "isSingleDigit");

		// print Time needed
		System.out.println("========= Execution Time =========");
		printTimeNeeded();

	}

	private void printList() {
		for (Integer i : myList)
			System.out.printf("%20d\n", i);
	}

}
