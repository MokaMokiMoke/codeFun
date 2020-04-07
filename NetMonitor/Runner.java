package MaxAndMelonSoft;

public class Runner implements Runnable {

	private Client client;
	private IndicatorPanel indicatorPanel;

	public Runner(Client client, IndicatorPanel panel) {
		this.client = client;
		this.indicatorPanel = panel;
	}

	@Override
	public void run() {
		while (true) {
			indicatorPanel.setIndication(client.ping());
			sleep(1000);
		}
	}

	private void sleep(long mils) {
		try {
			Thread.sleep(mils);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
