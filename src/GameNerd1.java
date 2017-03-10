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
	private final double g = 0.4;
	private int nstage, seconds, gameseconds;

	public static void main(String[] args) {
		new GameNerd1();
	}

	public GameNerd1() {
		//Setting variables
		manual = true;
		timer = new Timer(16, this); //16 for 60fps
		seconds = 0;
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
		if (manual) {
			frame.addKeyListener(this);
		}
		frame.setFocusable(true);
		frame.add(this);

		timer.start();
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

			// move map
			if (player.getX() < 500) {
				map.setX(0);
			} else if (player.getX() > map.getWidth() - 500) {
				map.setX(1000 - map.getWidth());
			} else {
				map.setX(500 - player.getX());
			}

			// Check Game Over
			if (player.getY() >= 800) {
				reset();
			}

			// Check Clear
			if (player.getX() >= 6536) {
				System.out.println("Clear Time: " + seconds/60);
				System.out.println("Simulating Time: " + gameseconds/60);
				if(!manual) tool.printCommand();
				reset();
			}

			seconds++;
			gameseconds++;
			repaint();
		}
	}
	
	public void autoCommand(){
		if (seconds % 30 == 0) {
			int order = tool.control();
			keyUp = (order / 10 == 1) ? true : false;
			keyLeft = (order % 10 == 1) ? true : false;
			keyRight = (order % 10 == 2) ? true : false;
		}
		tool.updateInfo();
		// Finish test if the player could not get to a specific point
		// in certain time.
		if (player.getX() < seconds * 4) {
			reset();
		}
	}

	public void reset() {
		if(!manual) tool.nextTest(seconds);
		player.respawn();
		map.loadMap();
		seconds = -1;
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
