package crypto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;

public class Manager extends Thread {

	// Stuff for Threads (SQL default max. conn = 151)
	private static int NUM_OF_THREADS = 20;
	static int nextId = 1;
	int myId;

	// Path and name of input file
	static String pathOfInputFile = "H:\\OTH\\Eclipse\\HashBase\\src\\";
	static String inputFile = "10m";

	// Global variables
	static int errorInSqlStatement = 0;
	static String[] plainArray = null;
	final String dbTable = "hashing";

	synchronized static int getNextId() {
		return nextId++;
	}

	synchronized static void increaseErrorCounter() {
		errorInSqlStatement++;
	}

	public static void main(String[] args) throws Exception {

		// Start watch
		Watch programWatch = new Watch();
		startWatch(programWatch);

		Thread[] threadList = new Thread[NUM_OF_THREADS];

		// Count lines of input file
		buildPlainArray();

		// Spawn threads
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			threadList[i] = new Manager();
			threadList[i].start();
		}

		// Wait for all threads to end
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			threadList[i].join();
		}

		// Stop Watch
		stopWatch(programWatch);
	}

	public synchronized void run() {

		Statement queryStatement = null;
		Connection dbConnection = ConnectionGenerator.generate();

		String plain = "";

		MultiHasher multiHasher = new MultiHasher();

		// Calculate input file start and stop lines
		int firstLine = (myId - 1) * (plainArray.length / NUM_OF_THREADS);
		int lastLine = myId * (plainArray.length / NUM_OF_THREADS) - 1;
		if (myId == NUM_OF_THREADS)
			lastLine = plainArray.length - 1;
		
		// Let´s build our SQL Statements
		StringBuffer insertStatement = new StringBuffer();

		// BIG loop (StringBuffer is synchronized, StringBuilder is not)
		for (int currentLineNumber = firstLine; currentLineNumber <= lastLine; currentLineNumber++) {

			plain = plainArray[currentLineNumber];

			if (plain.contains("'") && !plain.contains("\\'"))
				plain = plain.replace("'", "''");

			
			insertStatement.append("INSERT into " + dbTable);
			insertStatement.append(" (plain, sha1, sha256, sha384, sha512, md2, md5) VALUES (");
			insertStatement.append("'" + plain + "', '");
			insertStatement.append(multiHasher.sha1(plain) + "', '");
			insertStatement.append(multiHasher.sha256(plain) + "', '");
			insertStatement.append(multiHasher.sha384(plain) + "', '");
			insertStatement.append(multiHasher.sha512(plain) + "', '");
			insertStatement.append(multiHasher.md2(plain) + "', '");
			insertStatement.append(multiHasher.md5(plain) + "'");
			insertStatement.append(")");

			try {
				queryStatement = dbConnection.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				queryStatement.executeUpdate(insertStatement.toString());
			} catch (SQLException ex) {
				System.out.println(myId + " had an error!\n" + ex.toString());
				System.out.println(insertStatement.toString());
				System.out.println("Line No.: " + currentLineNumber);
				// ex.printStackTrace();
				errorInSqlStatement++;
			}
			
			// Reset our StringBuffer
			insertStatement.setLength(0);
			
		}

		// cleanup
		multiHasher = null;

		// Commit & close connection
		try {
			dbConnection.commit();
			dbConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void buildPlainArray() {
		try (Stream<String> stream = Files.lines(Paths.get(pathOfInputFile + inputFile))) {
			plainArray = stream.toArray(size -> new String[size]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Manager() {
		super();
		// Assign an ID to the thread
		myId = getNextId();
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
		System.out.printf("Ende mit %d Fehlern beim SQL-Statement\n", errorInSqlStatement);
	}

}
