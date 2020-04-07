package MaxAndMelonSoft;

import java.awt.Dimension;

import javax.swing.JFrame;

public class GUI extends JFrame {

	public static final long serialVersionUID = 1L;
	private AbstractGUIState<?, ?> state;

	public GUI() {
		super("Net Monitor");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(400, 300));
	}

	public void display() {
		checkStatePresence();
		state.display();
	}

	public void createContent() {
		checkStatePresence();
		state.createContent();
	}

	public void setState(AbstractGUIState<?, ?> state) {
		this.state = state;
	}

	private void checkStatePresence() throws NoStateSuppliedException {
		if (state == null) {
			throw new NoStateSuppliedException("No state supplied to GUI before execution");
		}
	}

	@SuppressWarnings("serial")
	static class NoStateSuppliedException extends RuntimeException {

		NoStateSuppliedException(String message) {
			super(message);
		}

		NoStateSuppliedException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
