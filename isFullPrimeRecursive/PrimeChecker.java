package oth;

public class PrimeChecker {

	// Variables for staticstic
	private double checkRecursivelyUsed, checksumUsed, checksumIsPrimeUsed, everyDigitIsPrimeUsed, intoToIntArray,
			inverseChecksumUsed, inverseChecksumIsPrimeUsed, isPrimeUsed, isSingleDigitUsed;

	// Getter for statistic values
	public double getInverseChecksumIsPrimeUsed() {
		return inverseChecksumIsPrimeUsed;
	}

	public double getInverseChecksumUsed() 		{ return inverseChecksumUsed; 	}
	public double getIsSingleDigitUsed() 		{ return isSingleDigitUsed; 	}
	public double getIsPrimeUsed() 			{ return isPrimeUsed; 		}
	public double getIntToArrayUsed() 		{ return intoToIntArray; 	}
	public double getChecksumIsPrimeUsed() 		{ return checksumIsPrimeUsed; 	}
	public double getCheckRecursivelyUsed() 	{ return checkRecursivelyUsed; 	}
	public double getChecksumUsed() 		{ return checksumUsed; 		}
	public double getEveryDigitIsPrimeUsed() 	{ return everyDigitIsPrimeUsed; }

	private boolean isSingleDigit(int n) {
		isSingleDigitUsed++;
		if (n < 0 || n > 9)
			return false;

		return true;
	}

	private int[] intToIntArray(int number) {
		intoToIntArray++;
		String intString = Integer.toString(number);
		int[] intArray = new int[intString.length()];

		for (int i = 0; i < intString.length(); i++)
			intArray[i] = intString.charAt(i) - '0';

		return intArray;
	}

	private boolean everyDigitIsPrime(int number) {
		everyDigitIsPrimeUsed++;
		int[] intArray = intToIntArray(number);

		for (int digit : intArray)
			if (!isPrime(digit))
				return false;
		return true;
	}

	private boolean checksumIsPrime(int number) {
		checksumIsPrimeUsed++;
		if (isPrime(checksum(number)))
			return true;
		
		return false;
	}

	private int checksum(int number) {
		checksumUsed++;
		int[] intArray = intToIntArray(number);
		int checksum = 0;

		for (int digit : intArray)
			checksum += digit;
		
		return checksum;
	}

	private int inverseChecksum(int number) {
		inverseChecksumUsed++;
		int checksum = checksum(number);

		while (!isSingleDigit(checksum))
			checksum = checksum(checksum);

		return checksum;
	}

	private boolean inverseChecksumIsPrime(int number) {
		inverseChecksumIsPrimeUsed++;
		if (isPrime(inverseChecksum(number)))
			return true;
		
		return false;
	}

	private boolean isPrime(int number) {
		
		isPrimeUsed++;
		if (number == 2)
			return true;

		if (number < 2 || number % 2 == 0)
			return false;

		for (int i = 3; i * i <= number; i += 2)
			if (number % i == 0)
				return false;

		return true;
	}

	protected boolean checkRecursively(int number) {

		checkRecursivelyUsed++;
		// Base case: i is not prime && checksum is not prime && invers checksum
		// is not prime && a single digit is not prime
		if (!isPrime(number) || !checksumIsPrime(number) || !inverseChecksumIsPrime(number)
				|| !everyDigitIsPrime(number))
			return false;

		// Base case: i is prime && checksum is prime
		// && inves checksum is prime
		if (isPrime(number) && checksumIsPrime(number) && inverseChecksumIsPrime(number))
			return true;

		// if is not a base case -> start recursion
		if (!isSingleDigit(number))
			checkRecursively(checksum(number));

		// Dummy return to shut up eclipse
		return false;
	}
}
