package MaxAndMelonSoft;

import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JLabel;

public class PingState extends AbstractGUIState<Client, IndicatorPanel> {

	public PingState(GUI gui, Map<Client, IndicatorPanel> dataMap, ActionListener setListener) {
		super(gui, dataMap, setListener);
	}

	@Override
	void createContent() {
		JLabel nameLabel;
		actionButton.setText("Start Ping");
		for (Entry<Client, IndicatorPanel> entry : dataMap.entrySet()) {
			nameLabel = new JLabel(entry.getKey().getId().getName());
			tablePanel.add(nameLabel);
			tablePanel.add(entry.getValue());
		}
		actionButton.addActionListener(e -> actionButton.setEnabled(false));
	}

}
