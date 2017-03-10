import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Player extends Character {

	private boolean clear = false;

	public Player(double g) {
		super(new ImageIcon("images\\mario01right.gif"), g, 64, 368, 6, -12, 0);
	}

	public void moveL() {
		super.moveL();
	}

	public void moveR() {
		super.moveR();
	}

	public void stop() {
		super.stop();
	}

	public void moveU() {
		super.moveU();
	}

	public void move(Map map) {
		// check clear
		if (xPos >= 6324) {
			clear = true;
		}
		if (clear == true) {
			vy = 1;
			vx = 0;
			direction = 0;
			if (onfloor) {
				vx = (xPos < 6536) ? 3 : 0;
			}
		}
		super.move(map);
		if (vx == 0) {
			if (direction == 0) {
				img = (onfloor) ? new ImageIcon("images\\mario01right.gif")
						: new ImageIcon("images\\mario20right.gif");
			} else {
				img = (onfloor) ? new ImageIcon("images\\mario01left.gif")
						: new ImageIcon("images\\mario20left.gif");
			}
		} else {
			if (direction == 0) {
				img = (onfloor) ? new ImageIcon("images\\mario02right.gif")
						: new ImageIcon("images\\mario20right.gif");
			} else {
				img = (onfloor) ? new ImageIcon("images\\mario02left.gif")
						: new ImageIcon("images\\mario20left.gif");
			}
		}
	}

	public void respawn() {
		super.respawn(64, 368);
		clear = false;
		img = new ImageIcon("images\\mario01right.gif");
	}
}
