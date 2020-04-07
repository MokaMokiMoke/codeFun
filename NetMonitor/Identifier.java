package MaxAndMelonSoft;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Identifier {

	private String name;
	private String ip;
	private int timeout;
	final int defaultTimeout = 250;

	public Identifier(String name, String ip, int timeout) {
		setName(name);
		setIp(ip);
		setTimeout(timeout);
	}

	private void setTimeout(int timeout) {
		if (timeout <= 0 || timeout > 2500)
			this.timeout = defaultTimeout;
		else
			this.timeout = timeout;

	}

	public int getTimeout() {
		return timeout;
	}

	private void setIp(String ip) {
		if (isIpValid(ip))
			this.ip = ip.trim();
		else
			System.out.println("IP is invalid!");
	}

	private void setName(String name) {
		if (name.isEmpty())
			System.out.println("Hostname is empty!");
		else
			this.name = name.trim();
	}

	public String getName() {
		return name;
	}

	public String getIp() {
		return ip;
	}

	private boolean isIpValid(String ip) {

		ip = ip.trim();
		if (ip == null || ip.isEmpty() || ip.length() < 6 || ip.length() > 15)
			return false;

		Pattern pattern = Pattern.compile(
				"^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

}
