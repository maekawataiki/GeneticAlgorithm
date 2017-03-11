import javax.swing.*;


public class tool extends toolGA{
	
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
