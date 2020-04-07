package MaxAndMelonSoft;

import java.awt.Color;
import javax.swing.JPanel;

public class IndicatorPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Color trueColor = Color.GREEN;
	private Color falseColor = Color.RED;

	public IndicatorPanel() {
		super();
		this.setIndication(false);
	}

	public void setIndication(boolean enabled) {
		setBackground(enabled ? trueColor : falseColor);
	}

}
