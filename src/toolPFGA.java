import java.awt.*;
import java.util.LinkedList;

import javax.swing.*;

// 10s 0(not jump) or 1(jump). 
//  1s 0(neutral), 1(left), or 2(right)
public class toolPFGA extends JPanel {
	private final int xi = 6;
	private final int island = 1;
	protected Player player;
	private LinkedList<int[]>[] gene = new LinkedList[island];
	private int[][] family = new int[4][xi + 1];
	private int[] population = new int[island];
	private int generation, number, command, focus;
	private JLabel focusLabel, generationLabel, numberLabel, scoreLabel, leftLabel,
			upLabel, rightLabel;

	public toolPFGA(Player player) {
		this.player = player;
		generation = 1;
		number = 0;
		command = 1;
		focus = 0;

		focusLabel = new JLabel("ISLAND: " + (focus+1));
		focusLabel.setForeground(Color.WHITE);
		focusLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
		generationLabel = new JLabel("    GEN: "
				+ String.format("%03d", generation));
		generationLabel.setForeground(Color.WHITE);
		generationLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
		numberLabel = new JLabel("     #"
				+ String.format("%02d", (number + 1)));
		numberLabel.setForeground(Color.WHITE);
		numberLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
		scoreLabel = new JLabel("      score: "
				+ String.format("%05d", player.getX()) + "      ");
		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
		leftLabel = new JLabel("\u2190");
		leftLabel.setForeground(Color.WHITE);
		leftLabel.setHorizontalAlignment(JLabel.CENTER);
		leftLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
		leftLabel.setPreferredSize(new Dimension(40, 40));
		leftLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		upLabel = new JLabel("\u2191");
		upLabel.setForeground(Color.WHITE);
		upLabel.setHorizontalAlignment(JLabel.CENTER);
		upLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
		upLabel.setPreferredSize(new Dimension(40, 40));
		upLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		rightLabel = new JLabel("\u2192");
		rightLabel.setForeground(Color.WHITE);
		rightLabel.setHorizontalAlignment(JLabel.CENTER);
		rightLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
		rightLabel.setPreferredSize(new Dimension(40, 40));
		rightLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));

		setOpaque(false);
		setBounds(18, 18, 900, 40);
		//add(focusLabel);
		add(generationLabel);
		add(numberLabel);
		add(scoreLabel);
		add(leftLabel);
		add(upLabel);
		add(rightLabel);
		
		for(int x = 0; x < island; x++){
			gene[x] = new LinkedList<int[]>();
			gene[x].add(createGene());
			population[x] = 1;
		}
	}

	public int assignGene() {
		return (int) (Math.random() * 3) + (int) (Math.random() * 2) * 10;
	}

	public int[] createGene() {
		int i[] = new int[xi + 1];
		i[0] = -1;
		for (int x = 1; x < xi; x++) {
			i[x] = assignGene();
		}
		return i;
	}

	public int[][] makeChild(int[] a, int[] b) {
		int[][] childs = new int[2][xi + 1];
		childs[0][0] = -1;
		childs[1][0] = -1;

		// Decide the cut points
		int nCut = (int) (Math.random() * (xi - 1)) + 1;
		int[] cutPoint = new int[nCut];
		for (int x = 0; x < cutPoint.length; x++) {
			cutPoint[x] = (int) (Math.random() * (xi - 1)) + 2;
		}

		// Cross: Multiple Point Cross (Preserve best two)
		int flag = 1;
		for (int x = 1; x < xi + 1; x++) {
			childs[0][x] = (flag == 1) ? a[x] : b[x];
			childs[1][x] = (flag == 1) ? b[x] : a[x];
			boolean cut = false;
			for (int y = 0; y < cutPoint.length; y++) {
				if (x == cutPoint[y])
					cut = true;
			}
			if (cut)
				flag *= -1;
		}

		// Mutation
		int choose = (int) (Math.random() * 2);
		int nMutation = (int) (Math.random() * (xi + 1));
		for (int x = 0; x < nMutation; x++) {
			int p = (int) (Math.random() * xi + 1);
			childs[choose][p] = assignGene();
		}

		return childs;
	}

	public void makeFamily() {
		// 0
		int n = (int) (Math.random() * population[focus]);
		family[0] = gene[focus].get(n);
		gene[focus].remove(n);
		population[focus]--;

		// 1
		if (population[focus] == 0) {
			family[1] = createGene();
		} else {
			n = (int) (Math.random() * population[focus]);
			family[1] = gene[focus].get(n);
			gene[focus].remove(n);
			population[focus]--;
		}

		int[][] childs = makeChild(family[0], family[1]);

		// 2
		family[2] = childs[0];

		// 3
		family[3] = childs[1];
	}

	public void nextGen() {
		// return family to population
		if (family[2][0] >= family[0][0] && family[2][0] >= family[1][0]
				&& family[3][0] >= family[0][0] && family[3][0] >= family[1][0]) {
			gene[focus].add(family[2]);
			gene[focus].add(family[3]);
			gene[focus].add((family[0][0] > family[1][0]) ? family[0] : family[1]);
			population[focus] += 3;
		} else if (family[0][0] > family[2][0] && family[0][0] > family[3][0]
				&& family[1][0] > family[2][0] && family[1][0] > family[3][0]) {
			gene[focus].add((family[0][0] > family[1][0]) ? family[0] : family[1]);
			population[focus]++;
		} else if ((family[0][0] > family[2][0] && family[0][0] > family[3][0])
				|| (family[1][0] > family[2][0] && family[1][0] > family[3][0])) {
			gene[focus].add((family[0][0] > family[1][0]) ? family[0] : family[1]);
			gene[focus].add((family[2][0] > family[3][0]) ? family[2] : family[3]);
			population[focus] += 2;
		} else {
			gene[focus].add((family[2][0] > family[3][0]) ? family[2] : family[3]);
			gene[focus].add(createGene());
			population[focus] += 2;
		}
	}

	public void nextTest(int score) {
		// mark the score
		family[number][0] = score;
		// give next command, move to next generation
		// if it is the last test in the generation
		number++;
		command = 1;
		if (number == 4) {
			nextGen();
			number = 0;
			focus++;
			if (focus == island){
				focus = 0;
				generation++;
				if(generation % 50 == 1 && island > 1){
					immigration();
				}
			}
			makeFamily();
		}
		if (family[number][0] != -1) {
			nextTest(family[number][0]);
		}

		// Update Label
		focusLabel.setText("ISLAND: " + (focus+1));
		generationLabel.setText("    GEN: " + String.format("%03d", generation));
		numberLabel
				.setText("     #" + String.format("%02d", (number + 1)));
	}
	
	public void immigration(){
		for(int x = 0; x < island; x++){
			int n = (int) (Math.random() * population[x]);
			int destination = (int) (Math.random() * island);
			gene[destination].add(gene[x].get(n));
			gene[x].remove(n);
		}
	}

	public int control() {
		// read command and give order
		int order = family[number][command];
		command = (command == xi) ? 1 : command + 1;

		// Update label
		String up = (order / 10 == 1) ? "\u2191" : "";
		String left = (order % 10 == 1) ? "\u2190" : "";
		String right = (order % 10 == 2) ? "\u2192" : "";
		leftLabel.setText(left);
		upLabel.setText(up);
		rightLabel.setText(right);

		// return order
		return order;
	}

	public void updateScore() {
		// Update Label
		scoreLabel.setText("      score: "
				+ String.format("%05d", player.getX()) + "      ");
	}
	
	public void printCommand() {
		for (int z = 1; z <= xi; z++) {
			if (family[number][z] / 10 == 1) {
				System.out.print("\u2191");
			}
			if (family[number][z] % 10 == 1) {
				System.out.print("\u2190");
			} else if (family[number][z] % 10 == 2) {
				System.out.print("\u2192");
			}
			System.out.print(", ");
		}
		System.out.println();
	}
}
