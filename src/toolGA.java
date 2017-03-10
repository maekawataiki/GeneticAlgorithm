import java.awt.*;

import javax.swing.*;

public class toolGA extends JPanel {

	private final int xi = 6;
	private final int testPerGen = 20;
	protected Player player;
	private int[] score = new int[testPerGen];
	private int[][] gene = new int[testPerGen][xi]; // 10s 0(not jump) or
													// 1(jump). 1s 0(neutral),
													// 1(left), or 2(right)
	private int generation, number, command, genTotalScore;
	private JLabel generationLabel, numberLabel, scoreLabel, leftLabel, upLabel,
			rightLabel;

	public toolGA(Player player) {
		this.player = player;
		generation = 1;
		number = 0;
		command = 0;

		generationLabel = new JLabel("GEN: "
				+ String.format("%03d", generation));
		generationLabel.setForeground(Color.WHITE);
		generationLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
		numberLabel = new JLabel("          #"
				+ String.format("%02d", (number + 1)));
		numberLabel.setForeground(Color.WHITE);
		numberLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
		scoreLabel = new JLabel("          score: "
				+ String.format("%05d", player.getX()) + "          ");
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
		add(generationLabel);
		add(numberLabel);
		add(scoreLabel);
		add(leftLabel);
		add(upLabel);
		add(rightLabel);

		firstGen();
	}

	public void assignGen(int x, int y) {
		gene[x][y] = (int) (Math.random() * 3) + (int) (Math.random() * 2) * 10;
	}

	public void firstGen() {
		for (int x = 0; x < gene.length; x++) {
			for (int y = 0; y < xi; y++) {
				assignGen(x, y);
			}
		}
	}

	public void nextGen() {
		generation++;
		number = 0;

		//System.out.println("Average Score in GEN " + (generation - 1) + ": "
		//		+ (genTotalScore / testPerGen));

		// Choose parents: Top 10 gene
		int[][] parents = new int[testPerGen / 2][xi];
		for (int count = 0; count < parents.length; count++) {
			int index = -1;
			int max = -1;
			for (int x = 0; x < testPerGen; x++) {
				if (score[x] > max) {
					max = score[x];
					index = x;
				}
			}
			for (int z = 0; z < xi; z++) {
				parents[count][z] = gene[index][z];
			}
			if (count == 0) {
				//System.out.println("Maximum Score in GEN " + (generation - 1)
				//		+ ": " + max);
			}
			score[index] = 0;
		}

		// Cross: Two point cross (Preserve best two)
		for (int count = 0; count < testPerGen; count += 2) {
			int parent1 = count / 2;
			int parent2 = (count / 2 + 1) % 10;
			int cut1 = (int) (Math.random() * xi);
			int cut2 = (int) (Math.random() * (xi - cut1)) + cut1;
			for (int z = 0; z < xi; z++) {
				gene[count][z] = (cut1 <= z && z <= cut2) ? parents[parent2][z]
						: parents[parent1][z];
				gene[count + 1][z] = (cut1 <= z && z <= cut2) ? parents[parent1][z]
						: parents[parent2][z];
				// Mutation: 5%
				if (0 == (int) (Math.random() * 20)) {
					assignGen(count, z);
				}
				if (0 == (int) (Math.random() * 20)) {
					assignGen(count + 1, z);
				}
			}
		}

		genTotalScore = 0;
	}

	public void nextTest(int score) {
		// mark the score
		this.score[number] = score;
		genTotalScore += this.score[number];

		// give next command, move to next generation
		// if it is the last test in the generation
		number++;
		command = 0;
		if (number == testPerGen) {
			nextGen();
		}

		// Update Label
		generationLabel.setText("GEN: " + String.format("%03d", generation));
		numberLabel
				.setText("          #" + String.format("%02d", (number + 1)));
	}

	public int control() {
		// read command and give order
		int order = gene[number][command];
		command = (command == xi - 1) ? 0 : command + 1;

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
		scoreLabel.setText("          score: "
				+ String.format("%05d", player.getX()) + "          ");
	}

	public void printCommand() {
		for (int z = 0; z < xi; z++) {
			if (gene[number][z] / 10 == 1) {
				System.out.print("\u2191");
			}
			if (gene[number][z] % 10 == 1) {
				System.out.print("\u2190");
			} else if (gene[number][z] % 10 == 2) {
				System.out.print("\u2192");
			}
			System.out.print(", ");
		}
		System.out.println();
	}

}
