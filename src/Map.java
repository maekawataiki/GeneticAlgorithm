import javax.swing.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Map extends JPanel {

	private Player player;
	private ImageIcon background;
	private int xPos, yPos, width, height;
	public int[][] stage, storage;
	private int nstage;
	public LinkedList<Block> blocks = new LinkedList<Block>();
	public LinkedList<Enemy> enemies = new LinkedList<Enemy>();

	public Map(Player player, int nstage) {
		this.player = player;
		this.nstage = nstage;
		readMap();
		loadMap();
	}

	public int getX() {
		return xPos;
	}

	public int getY() {
		return yPos;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setX(int x) {
		xPos = x;
	}

	public void setY(int y) {
		yPos = y;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(background.getImage(), 0, 0, this);
		for (Block b : blocks) {
			b.draw(g2, this);
		}
		for (Enemy e : enemies) {
			e.draw(g2, this);
		}
		player.draw(g2, this);
	}

	public void loadMap() {
		blocks.clear();
		enemies.clear();
		// Create Objects
		for (int x = 0; x < stage.length; x++) {
			for (int y = 0; y < stage[x].length; y++) {
				stage[x][y] = storage[x][y];
				if (1 <= stage[x][y] && stage[x][y] <= 6) {
					// add block in map
					blocks.add(new Block((y * 32), (x * 32 + 16), stage[x][y]));
				} else if (7 <= stage[x][y] && stage[x][y] <= 8){
					enemies.add(new Enemy(player.g, (y * 32), (x * 32 + 16), stage[x][y]));
					stage[x][y] = 0;
				}
			}
		}
	}

	public void readMap() {
		background = new ImageIcon("images\\background" + nstage + ".png");
		width = background.getIconWidth();
		height = background.getIconHeight();
		this.setBounds(xPos, yPos, width, height);

		// read in objects
		File txtFile = new File("stages\\stage" + nstage + ".txt");
		String line;
		try {
			// initialize file reader and buffered reader objects
			BufferedReader in = new BufferedReader(new FileReader(txtFile));
			line = in.readLine();
			int row = 13;
			int col = line.length();
			stage = new int[row][col];
			storage = new int[row][col];
			// read characters and copy it in array
			for (int x = 0; x < row; x++) {
				for (int y = 0; y < col; y++) {
					storage[x][y] = Integer.parseInt(line.charAt(y) + "");
				}
				line = in.readLine();
			}
		} catch (IOException e) {
			System.err.println("IOException:" + e.getMessage());
		}
	}
}
