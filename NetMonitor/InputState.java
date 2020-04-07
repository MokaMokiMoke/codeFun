package MaxAndMelonSoft;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputState extends AbstractGUIState<String, String> {

	private Map<JTextField, JTextField> fieldMap;

	public InputState(GUI gui, Map<String, String> dataMap, ActionListener setListener) {
		super(gui, dataMap, setListener);
		fieldMap = new HashMap<>();
	}

	@Override
	void createContent() {
		JButton addButton = new JButton("+");
		addButton.addActionListener(e -> addFieldPair());
		actionButton.setText("Set");
		actionButton.addActionListener(e -> fieldMap.entrySet().stream()
				.forEach(entry -> dataMap.put(entry.getKey().getText(), entry.getValue().getText())));
		controlPanel.add(addButton);
	}

	private void addFieldPair() {
		JTextField nameField = new JTextField(15);
		JTextField ipField = new JTextField(10);
		JPanel formatPanel1 = new JPanel();
		JPanel formatPanel2 = new JPanel();
		nameField.setMaximumSize(nameField.getPreferredSize());
		ipField.setMaximumSize(ipField.getPreferredSize());
		formatPanel1.add(nameField);
		formatPanel2.add(ipField);
		fieldMap.put(nameField, ipField);
		tablePanel.add(formatPanel1);
		tablePanel.add(formatPanel2);
		gui.pack();
		gui.setVisible(true);
	}

}
