import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class PannelContent {
	private JFrame f;
	private Clock clock;

	public PannelContent(){
		try {
			this.contentPacker();
		} catch (InterruptedException e) {
			// Wenn beim Warten ein Problem auftritt:
			e.printStackTrace();
		}
	}
	
	public void contentPacker() throws InterruptedException{
		f = new JFrame("Binär Uhr");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setResizable(false);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent w){
				System.exit(0);
			}
		});
		{
			clock = new Clock();
			f.getContentPane().add(clock, BorderLayout.CENTER);
		}
		f.setSize(334, 176);
		f.setVisible(true);
		while (true){
			clock.repaint();
			// WICHIG! 20ms warten...
			Thread.sleep(20);
		}
	}

}
