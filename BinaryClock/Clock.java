import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Date;

public class Clock extends Canvas{
	private static final long serialVersionUID = 1254350238918281618L;
	// Zeit-Variable
	private Date zeit;
	// Größen:
	private final int SIZE_ON = 18;
	// Doppelpufferung:
	private Image dbimage;
	private Graphics dbg;
	// 3D Arrays... COOL ;D
	private final int[][][] time_sec = {
			{//	  x    y
				{250, 100},
				{250, 70},
				{250, 40},
				{250, 10}
			},{
				{220, 100},
				{220, 70},
				{220, 40}
			}
	};
	private final int[][][] time_min = {
			{
				{170, 100},
				{170, 70},
				{170, 40},
				{170, 10}
			},{
				{140, 100},
				{140, 70},
				{140, 40}
			}
	};
	private final int[][][] time_std = {
			{
				{90, 100},
				{90, 70},
				{90, 40},
				{90, 10}
			},{
				{60, 100},
				{60, 70}
			}
	};
	
	public Clock(){
		zeit = new Date();
	}

	public void paint(final Graphics g){
		// Paint der Super-Klasse aufrufen:
		super.paint(g);
		// Malen:
		this.setBackground(Color.BLACK);
		// Leere Felder Zeichnen:
		this.drawFields(g);
		// Zeit Zeichnen:
		zeit = new Date();
		this.finishSec(g);
		this.finishMin(g);
		this.finishStd(g);
	}
	
	public void update(Graphics g){
		// Doppelpuffer:
		if (dbimage == null){
			dbimage = createImage(this.getSize().width, this.getSize().height);
			dbg = dbimage.getGraphics();
		}
		dbg.setColor(getBackground());
		paint(dbg);
		g.drawImage(dbimage, 0, 0, this);
	}
	
	public boolean drawFields(Graphics g){
		g.setColor(Color.WHITE);
		// Zeiche Sekunden-Felder:
		for (int a = 0; a < 2; a++){
			for (int i = 0; i < time_sec[1].length; i++){
				//			x-Coorodinate		y-Coordinate
				g.drawOval(time_sec[a][i][0], time_sec[a][i][1], 20, 20);
			}
		}
		g.drawOval(time_sec[0][3][0], time_sec[0][3][1], 20, 20);
		// Zeichne Minuten-Felder:
		for (int a = 0; a < 2; a++){
			for (int i = 0; i < time_min[1].length; i++){
				//			x-Coorodinate		y-Coordinate
				g.drawOval(time_min[a][i][0], time_min[a][i][1], 20, 20);
			}
		}
		g.drawOval(time_min[0][3][0], time_min[0][3][1], 20, 20);
		// Zeichne Stunden-Felder:
		for (int a = 0; a < 2; a++){
			for (int i = 0; i < time_std[1].length; i++){
				//			x-Coorodinate		y-Coordinate
				g.drawOval(time_std[a][i][0], time_std[a][i][1], 20, 20);
			}
		}
		g.drawOval(time_std[0][2][0], time_std[0][2][1], 20, 20);
		g.drawOval(time_std[0][3][0], time_std[0][3][1], 20, 20);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean finishSec(Graphics g){
		// Ziffern einzeln holen:
		int sek[] = {
				( zeit.getSeconds() - ((zeit.getSeconds() / 10) *10)),
				( zeit.getSeconds() / 10)
		};
		for (int i = 0; i < 2; i++){
			int rechner = sek[i];
			// Binärzahlen für Ziffer bestimmen:
			int indexArray = 0;
			while (rechner > 0){
				if (rechner % 2 == 1){
					g.setColor(Color.RED);
					g.fillOval( (time_sec[i][indexArray][0] +1), (time_sec[i][indexArray][1] +1), SIZE_ON, SIZE_ON);
				}
				rechner = rechner / 2;
				indexArray++;
			}
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean finishMin(Graphics g){
		// Ziffern einzeln holen:
		int min[] = {
				( zeit.getMinutes() - ((zeit.getMinutes() / 10) *10)),
				( zeit.getMinutes() / 10)
		};
		for (int i = 0; i < 2; i++){
			int rechner = min[i];
			// Binärzahlen für Ziffer bestimmen:
			int indexArray = 0;
			while (rechner > 0){
				if (rechner % 2 == 1){
					g.setColor(Color.RED);
					g.fillOval( (time_min[i][indexArray][0] +1), (time_min[i][indexArray][1] +1), SIZE_ON, SIZE_ON);
				}
				rechner = rechner / 2;
				indexArray++;
			}
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean finishStd(Graphics g){
		// Ziffern einzeln holen:
		int std[] = {
				( zeit.getHours() - ((zeit.getHours() / 10) *10)),
				( zeit.getHours() / 10)
		};
		for (int i = 0; i < 2; i++){
			int rechner = std[i];
			// Binärzahlen für Ziffer bestimmen:
			int indexArray = 0;
			while (rechner > 0){
				if (rechner % 2 == 1){
					g.setColor(Color.RED);
					g.fillOval( (time_std[i][indexArray][0] +1), (time_std[i][indexArray][1] +1), SIZE_ON, SIZE_ON);
				}
				rechner = rechner / 2;
				indexArray++;
			}
		}
		return true;
	}

}
