package MaxAndMelonSoft;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public abstract class AbstractGUIState<K, V> {

	private static JLabel warning = new JLabel("All fields should be filled!");
	Map<K, V> dataMap;
	GUI gui;
	JPanel contentPanel, tablePanel, controlPanel;
	JButton actionButton;

	private AbstractGUIState() {
		contentPanel = new JPanel(new BorderLayout());
		tablePanel = new JPanel(new GridLayout(0, 2, 5, 5));
		controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		actionButton = new JButton();
		controlPanel.add(actionButton);
		contentPanel.add(tablePanel);
		contentPanel.add(controlPanel, BorderLayout.SOUTH);
		contentPanel.add(warning, BorderLayout.NORTH);
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		contentPanel.setBorder(padding);
	}

	public AbstractGUIState(GUI gui, Map<K, V> dataMap, ActionListener setListener) {
		this();
		this.dataMap = dataMap;
		this.gui = gui;
		actionButton.addActionListener(setListener);
	}

	void display() {
		gui.setContentPane(contentPanel);
		gui.pack();
		gui.setVisible(true);
	}

	abstract void createContent();

}
