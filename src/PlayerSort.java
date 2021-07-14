import java.util.Comparator;

public class PlayerSort implements Comparator<Player>{
	private double stuffMultiplier, movementMultiplier, controlMultiplier;
	
	public PlayerSort(double currStuffAmt, double currMovementAmt, double currControlAmt) {
		super();
		this.stuffMultiplier = currStuffAmt;
		this.movementMultiplier = currMovementAmt;
		this.controlMultiplier = currControlAmt;
	}
	
	@Override
	public int compare(Player p1, Player p2) {
		if(p1 == null) {
			p1 = new Player();
		}
		
		if(p2 == null) {
			p2 = new Player();
		}
		
		double p1Stats = (p1.stuff * stuffMultiplier) + (p1.movement * movementMultiplier) + (p1.control * controlMultiplier);
		double p2Stats = (p2.stuff * stuffMultiplier) + (p2.movement * movementMultiplier) + (p2.control * controlMultiplier);
		return (int)(p2Stats - p1Stats);
	}

}
