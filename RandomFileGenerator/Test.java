import java.util.Random;
import java.util.UUID;

class Test {

	public static void main(String[] args) {

		// How Much Files do you want?
		final int numberOfFiles = 250;

		// Location of files
		final String directory = "D:\\_test\\java\\";
		final String extension = ".sample";

		// Do you want B, KB, MB or GB?
		Dimension dim = Dimension.KB;

		// Min & Max Size according to your dimension
		final double minFileSize = 20;
		final double maxFileSize = 250; // should be: max >= (min + 3)

		// Internal Stuff and statistics
		Random random = new Random();
		Watch programWatch = new Watch();
		programWatch.start();

		System.out.printf("The Start time is: %s\n", programWatch.getStartTimePretty());

		for (int i = 0; i != numberOfFiles; i++) {

			double randFileSize = minFileSize + (maxFileSize - minFileSize) * random.nextDouble();
			String path = directory + UUID.randomUUID().toString() + extension;
			RandomFileGenerator.generate(path, dim, randFileSize);

		}

		programWatch.stop();
		System.out.printf("The Stop time is : %s\n", programWatch.getStopTimePretty());
		System.out.printf("Time processed   : %s\n", programWatch.getRuntimePretty());
		System.out.printf("Miliseconds needed: %,d", programWatch.getRuntime());
	}
}
