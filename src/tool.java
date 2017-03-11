import javax.swing.*;

// extends one of
// toolGA
// toolPFGA
public class tool extends toolPFGA{
	
	public tool(Player player){
		super(player);
	}
	
	public int control(){
		return super.control();
	}
	
	public void updateInfo(){
		super.updateScore();
	}
	
	public void nextTest(int seconds){
		int score = (super.player.getX() == 6536) ? 6536 + (2000 - seconds)
				: super.player.getX();
		super.nextTest(score);
	}
	public void printCommand(){
		super.printCommand();
	}
	
}
