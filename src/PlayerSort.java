import java.util.Comparator;

public class PlayerSort implements Comparator<Player>{
	private double stuffMultiplier, movementMultiplier, controlMultiplier, stuffMultiplierRight, movementMultiplierRight, controlMultiplierRight;

	public PlayerSort(double currStuffAmt, double currMovementAmt, double currControlAmt, double currStuffAmtRight, double currMovementAmtRight, double currControlAmtRight) {
		super();
		this.stuffMultiplier = currStuffAmt;
		this.movementMultiplier = currMovementAmt;
		this.controlMultiplier = currControlAmt;
		this.stuffMultiplierRight = currStuffAmtRight;
		this.movementMultiplierRight = currMovementAmtRight;
		this.controlMultiplierRight = currControlAmtRight;
	}

	@Override
	public int compare(Player p1, Player p2) {
		double p1Stats = (p1.stuff * stuffMultiplier) + (p1.movement * movementMultiplier) + (p1.control * controlMultiplier) + (p1.stuffRight * stuffMultiplierRight) + (p1.movementRight * movementMultiplierRight) + (p1.controlRight * controlMultiplierRight);
		double p2Stats = (p2.stuff * stuffMultiplier) + (p2.movement * movementMultiplier) + (p2.control * controlMultiplier) + (p2.stuffRight * stuffMultiplierRight) + (p2.movementRight * movementMultiplierRight) + (p2.controlRight * controlMultiplierRight);
		
		if(p2Stats - p1Stats > 0) {
			return 1;
		}else {
			return -1;
		}

	}

}
