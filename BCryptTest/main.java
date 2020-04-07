package oth;

import java.io.UnsupportedEncodingException;

public class foo {

	public static void main(String[] args) throws UnsupportedEncodingException {

		String secret = "thisIsMySecret";
		BCrypt bc = new BCrypt();
		final int loops = 50;
		final int minLogRounds = 5;
		final int maxLogRounds = 10; // shall be between 5 and 29
		String hash = bc.hashpw(secret, bc.gensalt(minLogRounds)); // WarmUpHasher

		Watch programmWatch = new Watch();
		Watch loopWatch = new Watch();

		programmWatch.start();
		System.out.printf("The Start time is: %s\n\n", programmWatch.getStartTimePretty());

		for (int logRounds = minLogRounds; logRounds != maxLogRounds; logRounds++) {

			loopWatch.start();
			for (int i = 0; i < loops; i++)
				hash = bc.hashpw(hash, bc.gensalt(logRounds));

			loopWatch.stop();
			System.out.printf("Loops: %2d, Rounds: %2d, Time: %s\n", loops, logRounds, loopWatch.getRuntimePretty());

		}

		programmWatch.stop();
		System.out.printf("\n");

		System.out.printf("The Stop time is : %s\n", programmWatch.getStopTimePretty());
		System.out.printf("Time processed   : %s\n", programmWatch.getRuntimePretty());
		System.out.printf("Miliseconds needed: %,d", programmWatch.getRuntime());

		System.out.print("\n\nGenerated hash: " + hash);

	}

}
