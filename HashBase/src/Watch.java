package crypto;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Watch {

	private long initTime, startTime, stopTime;
	Calendar calendar = Calendar.getInstance();

	public Watch() {
		initTime = System.currentTimeMillis();
	}

	public void start() {
		this.startTime = getCurrentTime();
	}

	public Watch(long startTimeInMs) {
		this();
		setStartTime(startTimeInMs);
		this.startTime = startTimeInMs;
	}

	public void stop() {
		this.stopTime = getCurrentTime();
	}

	public long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public long getRuntime() {
		return getCurrentTime() - this.startTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getInitTime() {
		return initTime;
	}

	public long getStopTime() {
		return stopTime;
	}

	public void setInitTime(long initTime) {
		if (initTime < 0) {
			this.initTime = 0;
			return;
		}
		this.initTime = initTime;
	}

	public void setStartTime(long startTimeInMs) {
		if (startTimeInMs < 0) {
			startTimeInMs = 0;
			return;
		}
		this.startTime = startTimeInMs;
	}

	public void setStopTime(long stopTime) {
		if (stopTime < 0) {
			this.stopTime = getCurrentTime();
			return;
		}
		this.stopTime = stopTime;
	}

	private void setCalendar(long time) {

		calendar.setTimeInMillis(time);
	}

	private String calendar2String() {

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		int sec = calendar.get(Calendar.SECOND);
		int ms = calendar.get(Calendar.MILLISECOND);

		return String.format("%02d.%02d.%04d - %02d:%02d:%02d and %4dms", day, month, year, hour, min, sec, ms);
	}

	public String getStartTimePretty() {
		setCalendar(getStartTime());
		return calendar2String();
	}

	public String getInitTimePretty() {
		setCalendar(getInitTime());
		return calendar2String();
	}

	public String getStopTimePretty() {
		setCalendar(getStopTime());
		return calendar2String();
	}

	public String getRuntimePretty() {

		long runtime = getRuntime();
		// hh:mm:ss and #### ms
		String nice = String.format("%02d:%02d:%02d and %4d ms", TimeUnit.MILLISECONDS.toHours(runtime),
				TimeUnit.MILLISECONDS.toMinutes(runtime)
						- TimeUnit.MINUTES.toMinutes(TimeUnit.MILLISECONDS.toHours(runtime)),
				TimeUnit.MILLISECONDS.toSeconds(runtime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(runtime)),
				TimeUnit.MILLISECONDS.toMillis(runtime)
						- TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(runtime)));

		return nice;
	}

}
