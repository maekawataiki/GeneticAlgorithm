import javax.swing.*;

import java.awt.*;

public abstract class Character {
	protected ImageIcon img;
	protected int width, height, life;
	protected double xPos, yPos, vx, vy, maxvx, jumpForce, g;
	protected int direction; // direction: 0 -> right, 1 -> left
	protected boolean onfloor, isDead;

	Character() {
	}

	Character(ImageIcon img, double g, double xPos, double yPos, double maxvx, double jumpForce, int direction) {
		this.img = img;
		width = img.getIconWidth();
		height = img.getIconHeight();
		this.xPos = xPos;
		this.yPos = yPos;
		vx = 0;
		vy = 0;
		this.maxvx = maxvx;
		this.g = g;
		this.jumpForce = jumpForce;
		this.direction = direction;

		onfloor = false;
		isDead = false;
	}

	public void draw(Graphics2D g2, JPanel panel) {
		g2.drawImage(img.getImage(), (int) xPos, (int) yPos, panel);
	}

	public int getX() {
		return (int) xPos;
	}

	public int getY() {
		return (int) yPos;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	protected void moveL() {
		vx = -maxvx;
		direction = 1;
	}

	protected void moveR() {
		vx = maxvx;
		direction = 0;
	}

	protected void stop() {
		vx = 0;
	}

	protected void moveU() {
		if (onfloor) {
			vy = jumpForce;
		}
	}

	protected abstract void horizontalWall(Map map);

	protected abstract void horizontalCollision(Map map);

	protected abstract void verticalCollision(Map map);

	protected void move(Map map) {
		// gravity acceleration
		vy += g;
		double newyPos = yPos + vy;
		double newxPos = xPos + vx;

		// stop when move out from the map
		if (newxPos < 0) {
			newxPos = 0;
			vx = 0;
		}
		if (newyPos < 0) {
			newyPos = 0;
			vy = 0;
		}

		// die when fall from map
		if (newyPos + height >= map.getHeight() && isDead == false) {
			isDead = true;
		}

		// fall straight down if dead
		if (isDead) {
			yPos = newyPos;
		}

		// move when alive
		else {
			// horizontal collision and movement
			newxPos = Math.ceil(newxPos);
			int xmax = (int) Math.floor((Math.max(newxPos, xPos) + width - 1) / 32);
			int xmin = (int) Math.floor((Math.min(newxPos, xPos)) / 32);
			int ymax = (int) Math.floor((yPos + height - 16 - 1) / 32);
			int ymin = (int) Math.floor((yPos - 16) / 32);
			if (xmin <= 0)
				xmin = 0;
			if (ymin <= 0)
				ymin = 0;
			for (int x = xmin; x <= xmax; x++) {
				for (int y = ymin; y <= ymax; y++) {
					if (map.stage[y][x] != 0) {
						if (vx > 0) {
							newxPos = x * 32 - width;
						} else if (vx < 0) {
							newxPos = x * 32 + 32;
						}
						horizontalWall(map);
						break;
					}
				}
			}
			xPos = newxPos;
			horizontalCollision(map);

			// vertical collision and movement
			newyPos = Math.ceil(newyPos);
			xmax = (int) Math.floor((xPos + width - 1) / 32);
			xmin = (int) Math.floor(xPos / 32);
			ymax = (int) Math.floor((Math.max(newyPos, yPos) + height - 16 - 1) / 32);
			ymin = (int) Math.floor((Math.min(newyPos, yPos) - 16) / 32);
			if (xmin < 0)
				xmin = 0;
			if (ymin < 0)
				ymin = 0;
			if (ymax > 12)
				ymax = 12;
			for (int x = xmin; x <= xmax; x++) {
				for (int y = ymin; y <= ymax; y++) {
					if (map.stage[y][x] != 0) {
						if (vy > 0) {
							newyPos = y * 32 - height + 16;
							vy = 0;
							onfloor = true;
						} else if (vy < 0) {
							newyPos = y * 32 + 32 + 16;
							vy = 0;
							if (map.stage[y][x] == 1) {
								for (Block b : map.blocks) {
									if (b.getX() == x * 32 && b.getY() == y * 32 + 16) {
										b.mutation(2);
									}
								}
								map.stage[y][x] = 2;
							}
						}
						break;
					}
				}
			}
			yPos = newyPos;
			verticalCollision(map);

			if (vy != 0)
				onfloor = false;
		}
	}

	protected void respawn(int x, int y) {
		xPos = x;
		yPos = y;
		isDead = false;
	}

	public Rectangle getRect() {
		return new Rectangle((int) xPos + 1, (int) yPos + 1, width - 1, height - 1);
	}
}
