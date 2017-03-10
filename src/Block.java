import java.awt.*;
import javax.swing.*;
public class Block {
	private ImageIcon block;
	private int xPos, yPos, width, height, type;
	
	Block(int xPos, int yPos, int type){
		this.type = type;
		block = new ImageIcon("images\\block" + type + ".gif");
		this.xPos=xPos;
		this.yPos=yPos;
		width = block.getIconWidth();
		height = block.getIconHeight();
	}
	public void mutation(int type){
		this.type = type;
		block = new ImageIcon("images\\block" + type + ".gif");
	}
	public void draw(Graphics2D g2, JPanel panel){
		g2.drawImage(block.getImage(), xPos, yPos, panel);
	}
	public int getX(){
		return xPos;
	}
	public int getY(){
		return yPos;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public Rectangle getRect(){
		return new Rectangle(xPos, yPos, width, height);
	}
}
