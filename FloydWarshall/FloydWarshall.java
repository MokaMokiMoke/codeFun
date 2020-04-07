
/*
 * THIS PROGRAM NEEDS A FRIENDLY ATMOSPHERE ;-)
 */
public class FloydWarshall {

	private static int inf = Integer.MAX_VALUE;

	public static int[][] weightMatrix;
	private static int[][][] distanceMatrixes;
	private static int arrayDimension = 12;

	static {
		// weightMatrix = new int[][] { { 0, inf, inf, inf, -1, inf }, { 1, 0,
		// inf,
		// 2, inf, inf },
		// { inf, 2, 0, inf, inf, -8 }, { -4, inf, inf, 0, 3, inf }, { inf, 7,
		// inf,
		// inf, 0, inf },
		// { inf, 5, 10, inf, inf, 0 } };

		// weightMatrix = new int[][] { { 0, 10, inf, 8, inf }, { inf, 0, -8,
		// -5, -10 }, { inf, inf, 0, 2, inf },
		// { inf, inf, inf, 0, inf }, { inf, inf, 2, 11, 0 } };

		// weightMatrix = new int[][] { { 0, 2, 4, inf }, { inf, 0, 9, -2 }, {
		// -3, inf, 0, 1 },
		// { inf, 5, inf, 0 } };

		weightMatrix = new int[][] { { inf, 10, inf, 8, inf, inf }, { -6, inf, inf, 2, 2, inf },
				{ -10, -8, inf, -5, inf, 5 }, { inf, inf, inf, inf, inf, inf }, { inf, inf, 10, 8, inf, -2 },
				{ -7, inf, inf, inf, inf, inf } };

		distanceMatrixes = new int[weightMatrix.length][weightMatrix.length][weightMatrix.length];
	}

	public static void run() {
		int k, row, col, oldMinPath, newPart1, newPart2;
		int[][] prevMatrix;
		for (k = 0; k < weightMatrix.length; k++) { // Starting with d(1) since
													// weightMatrix equals d(0)
			prevMatrix = (k == 0) ? weightMatrix : distanceMatrixes[k - 1];
			for (row = 0; row < weightMatrix.length; row++) {
				for (col = 0; col < weightMatrix.length; col++) {
					oldMinPath = prevMatrix[row][col];
					newPart1 = prevMatrix[row][k];
					newPart2 = prevMatrix[k][col];
					distanceMatrixes[k][row][col] = fWMin(oldMinPath, newPart1, newPart2);
				}
			}
		}
		printResult();
	}

	private static int fWMin(int oldPathLen, int newPart1, int newPart2) {
		if (newPart1 == inf || newPart2 == inf) {
			return oldPathLen;
		} else if (oldPathLen == inf) {
			return newPart1 + newPart2;
		}
		return Math.min(oldPathLen, newPart1 + newPart2);
	}

	private static void printResult() {
		Integer value;
		String print;
		for (int k = 0; k < distanceMatrixes.length; k++) {
			System.out.println("D(" + (k + 1) + "):");
			for (int i = 0; i < distanceMatrixes.length; i++) {
				for (int j = 0; j < distanceMatrixes.length; j++) {
					value = distanceMatrixes[k][i][j];
					print = (value == inf) ? "∞" : value.toString();
					System.out.print(String.format("%7s", print));
				}
				System.out.println();
			}
			for (int i = 0; i < distanceMatrixes.length; i++)
				System.out.print("------- ");
			System.out.println("");

		}
	}

	public static void main(String[] args) {
		// FloydWarshall.weightMatrix = ...;
		FloydWarshall.run();
	}

}
