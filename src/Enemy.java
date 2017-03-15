import java.awt.*;

import javax.swing.*;

public class Enemy extends Character {

	private boolean active;
	private int type;

	Enemy(double g, int xPos, int yPos, int type) {
		super(new ImageIcon("images\\enemy" + type + ".gif"), g, xPos, yPos, 1, 0, 1);
		this.type = type;
		life = type - 6;
		active = false;
	}

	public int getType() {
		return type;
	}

	public void activate() {
		active = true;
	}

	public void horizontalWall(Map map) {
		direction = (direction == 1) ? 0 : 1;
	}

	public void horizontalCollision(Map map) {
		for (Enemy e : map.enemies) {
			if (e != this && this.getRect().intersects(e.getRect())) {
				if (type == 8 && life < 2 && life % 2 == 0) {
					e.life = 0;
				} else {
					horizontalWall(map);
				}
			}
		}
	}

	public void verticalCollision(Map map) {
		if (type == 8) {
			if (life == 2) {
				img = new ImageIcon((direction == 0) ? "images\\enemy8right.gif" : "images\\enemy8left.gif");
			} else {
				img = new ImageIcon("images\\enemy8.gif");
			}
			width = img.getIconWidth();
			height = img.getIconHeight();
		}
		if (life == 0 && type != 8) {
			isDead = true;
			vy = -6;
		}
	}

	public void attacked() {
		life--;
		if (type == 8) {
			maxvx = (life % 2 == 0) ? 6 : 0;
		}
	}

	public void move(Map map) {
		if (active) {
			if (direction == 0) {
				super.moveR();
			} else if (direction == 1) {
				super.moveL();
			}
			super.move(map);
		}
	}

}
