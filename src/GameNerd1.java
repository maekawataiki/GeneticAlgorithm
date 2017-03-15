import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;

import javax.swing.*;

public class GameNerd1 extends JPanel implements ActionListener, KeyListener {

	private tool tool;
	private Map map;
	private Player player;
	private Timer timer;
	private boolean keyRight, keyLeft, keyUp, manual;
	private Graphics2D g2;
	private JFrame frame;
	private final double g = 0.5;
	private int nstage, nframe, gameseconds;

	public static void main(String[] args) {
		new GameNerd1();
	}

	public GameNerd1() {
		// Setting variables
		manual = false; // true to play, false using tool
		timer = new Timer(1, this); // 16 for 60fps
		nframe = 0;
		nstage = 1;
		gameseconds = 0;

		player = new Player(g);
		map = new Map(player, nstage);

		if (!manual) {
			tool = new tool(player);
			add(tool);
		}

		setBackground(Color.BLACK);
		add(map);

		frame = new JFrame();
		frame.setSize(1000, 480);
		frame.setTitle("SuperMarioBros");
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addKeyListener(this);
		frame.setFocusable(true);
		frame.add(this);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			// AI operation
			if (!manual) {
				autoCommand();
			}

			// move character
			moveCharacter();

			// move enemies
			moveEnemy();

			// move map
			moveMap();

			// Check Game Over
			if (player.getY() >= 800) {
				reset();
			}

			// Check Clear
			if (player.getX() >= 6536) {
				System.out.println("Clear Time: " + nframe / 60);
				System.out.println("Simulating Time: " + gameseconds / 60);
				if (!manual)
					tool.printCommand();
				nstage++;
				reset();
			}

			nframe++;
			gameseconds++;
			repaint();
		}
	}

	public void moveCharacter() {
		if (keyRight == keyLeft) {
			player.stop();
		} else {
			if (keyLeft) {
				player.moveL();
			} else if (keyRight) {
				player.moveR();
			}
		}
		if (keyUp) {
			player.moveU();
		}
		player.move(map);
	}

	public void moveEnemy() {
		Enemy delete = null;
		for (Enemy e : map.enemies) {
			e.move(map);
			if (e.getX() < player.getX() + 800) {
				e.activate();
			}
			if (e.getY() >= 800) {
				delete = e;
			}
		}
		map.enemies.remove(delete);
	}

	public void moveMap() {
		if (player.getX() < 500) {
			map.setX(0);
		} else if (player.getX() > map.getWidth() - 500) {
			map.setX(1000 - map.getWidth());
		} else {
			map.setX(500 - player.getX());
		}
	}

	public void autoCommand() {
		// update command per certain frame
		if (nframe % 20 == 0) {
			int order = tool.control();
			keyUp = (order / 10 == 1) ? true : false;
			keyLeft = (order % 10 == 1) ? true : false;
			keyRight = (order % 10 == 2) ? true : false;
		}
		tool.updateInfo();
		// Finish test if the player could not get to a specific point
		// in certain time.
		if (player.getX() < nframe * 3) {
			reset();
		}
	}

	public void reset() {
		if (!manual)
			tool.nextTest(nframe);
		player.respawn();
		map.loadMap();
		nframe = -1;
		setVisible(true);
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			keyRight = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			keyLeft = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			keyUp = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE && !timer.isRunning()){
			timer.start();
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			keyRight = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			keyLeft = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			keyUp = false;
		}
	}

	public void keyTyped(KeyEvent e) {
	}

}
