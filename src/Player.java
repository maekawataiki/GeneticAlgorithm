import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Player extends Character {

	private boolean clear = false;

	public Player(double g) {
		super(new ImageIcon("images\\player01right.gif"), g, 64, 368, 6, -12, 0);
		life = 1;
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
		if (isDead) {
			img = new ImageIcon("images\\player04.gif");
		} else if (vx == 0) {
			if (direction == 0) {
				img = new ImageIcon((onfloor) ? "images\\player01right.gif" : "images\\player03right.gif");
			} else {
				img = new ImageIcon((onfloor) ? "images\\player01left.gif" : "images\\player03left.gif");
			}
		} else {
			if (direction == 0) {
				img = new ImageIcon((onfloor) ? "images\\player02right.gif" : "images\\player03right.gif");
			} else {
				img = new ImageIcon((onfloor) ? "images\\player02left.gif" : "images\\player03left.gif");
			}
		}
	}

	public void respawn() {
		super.respawn(64, 368);
		life = 1;
		clear = false;
		img = new ImageIcon("images\\player01right.gif");
	}

	protected void horizontalWall(Map map) {
		vx = 0;
	}

	protected void horizontalCollision(Map map) {
		for (Enemy e : map.enemies) {
			if (this.getRect().intersects(e.getRect())) {
				if(e.getType() == 8 && e.life % 2 == 1){
					// if enemy is shell and not moving
					e.direction = direction;
					xPos = (direction == 0)? e.getX() - width - 1 : e.getX() + e.getWidth() + 1;
					e.attacked();
				} else if (e.life > 0 || e.getType() == 8) {
					life--;
				}
			}
		}
	}

	protected void verticalCollision(Map map) {
		Enemy delete = null;
		for (Enemy e : map.enemies) {
			if (this.getRect().intersects(e.getRect())) {
				if (vy > 0) {
					e.attacked();
					yPos = e.getY() - height;
					vy = -12;
					if(e.getType() != 8 && e.life == 0){
						delete = e;
					}
				}
			}
		}
		map.enemies.remove(delete);
		if (life == 0) {
			isDead = true;
			vy = -12;
		}
	}
}
