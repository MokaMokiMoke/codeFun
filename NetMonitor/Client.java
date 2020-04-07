package MaxAndMelonSoft;

import java.io.IOException;
import java.net.InetAddress;

public class Client {

	private Identifier id;

	public Client(Identifier id) {
		this.id = id;
	}

	public Identifier getId() {
		return id;
	}

	public boolean ping() {
		try {
			System.out.println("IP: " + id.getIp() + "\tTimeout: " + id.getTimeout());
			return InetAddress.getByName(id.getIp()).isReachable(id.getTimeout());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
