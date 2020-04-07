package MaxAndMelonSoft;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class Main {

	private static int defaultTimeout = 1500;
	private static GUI gui;
	private static Map<String, String> inputMap = new HashMap<>();
	private static Map<Client, IndicatorPanel> clientMap = new HashMap<>();

	public static void main(String[] args) {
		inputMap = new HashMap<>();
		clientMap = new HashMap<>();
		gui = new GUI();
		gui.setState(new InputState(gui, inputMap, e -> transition()));
		gui.createContent();
		gui.display();
	}

	private static void transition() {
		ClientFactory factory = new ClientFactory();
		for (Entry<String, String> entry : inputMap.entrySet()) {
			if (!"".equals(entry.getKey()) && !"".equals(entry.getValue())) {
				clientMap.put(factory.createClient(new Identifier(entry.getKey(), entry.getValue(), defaultTimeout)),
						new IndicatorPanel());
			}
		}
		gui.setState(new PingState(gui, clientMap, e -> startPing()));
		gui.createContent();
		gui.display();
	}

	private static void startPing() {
		LinkedList<Thread> threadList = new LinkedList<>();
		for (Entry<Client, IndicatorPanel> entry : clientMap.entrySet()) {
			threadList.add(new Thread(new Runner(entry.getKey(), entry.getValue())));
		}
		for (Thread t : threadList) {
			t.start();
		}
	}

}
