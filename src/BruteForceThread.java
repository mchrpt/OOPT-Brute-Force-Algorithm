import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class BruteForceThread extends Thread {

	private double minStuffAmt, minMovementAmt, minControlAmt, minStuffAmtRight, minMovementAmtRight,
			minControlAmtRight; // The lower bound
	private double maxStuffAmt, maxMovementAmt, maxControlAmt, maxStuffAmtRight, maxMovementAmtRight,
			maxControlAmtRight; // The upper bound
	private double stuffStepAmt, movementStepAmt, controlStepAmt, stuffStepAmtRight, movementStepAmtRight,
			controlStepAmtRight;
	private static final DecimalFormat df = new DecimalFormat(".#####");

	public long totalIterations;

	private int threadNum;

	public boolean isRunning;

	private Player[] playerArr;

	private int varianceAmount = 0;
	private long iterations = 0;

	private CalculatedSheet calculatedSheet;
	private ObservedSheet observedSheet;
	private ArrayList<Player[]> equivilantVarianceList;

	public BruteForceThread(int threadNum, CalculatedSheet calculatedSheet, ObservedSheet observedSheet,
			double minStuffAmt, double minMovementAmt, double minControlAmt, double minStuffAmtRight,
			double minMovementAmtRight, double minControlAmtRight, double maxStuffAmt, double maxMovementAmt,
			double maxControlAmt, double maxStuffAmtRight, double maxMovementAmtRight, double maxControlAmtRight,
			double stuffStepAmt, double movementStepAmt, double controlStepAmt, double stuffStepAmtRight,
			double movementStepAmtRight, double controlStepAmtRight) {

		this.threadNum = threadNum;

		this.calculatedSheet = calculatedSheet;
		this.observedSheet = observedSheet;

		this.minStuffAmt = minStuffAmt;
		this.maxStuffAmt = maxStuffAmt;
		this.stuffStepAmt = stuffStepAmt;

		this.minMovementAmt = minMovementAmt;
		this.maxMovementAmt = maxMovementAmt;
		this.movementStepAmt = movementStepAmt;

		this.minControlAmt = minControlAmt;
		this.maxControlAmt = maxControlAmt;
		this.controlStepAmt = controlStepAmt;

		this.minStuffAmtRight = minStuffAmtRight;
		this.maxStuffAmtRight = maxStuffAmtRight;
		this.stuffStepAmtRight = stuffStepAmtRight;

		this.minMovementAmtRight = minMovementAmtRight;
		this.maxMovementAmtRight = maxMovementAmtRight;
		this.movementStepAmtRight = movementStepAmtRight;

		this.minControlAmtRight = minControlAmtRight;
		this.maxControlAmtRight = maxControlAmtRight;
		this.controlStepAmtRight = controlStepAmtRight;

	}

	@Override
	public void run() {

		long N1 = (long) ((maxStuffAmt - minStuffAmt) / stuffStepAmt) + 1;
		long N2 = (long) ((maxMovementAmt - minMovementAmt) / movementStepAmt) + 1;
		long N3 = (long) ((maxControlAmt - minControlAmt) / controlStepAmt) + 1;
		long N4 = (long) ((maxStuffAmtRight - minStuffAmtRight) / stuffStepAmtRight) + 1;
		long N5 = (long) ((maxMovementAmtRight - minMovementAmtRight) / movementStepAmtRight) + 1;
		long N6 = (long) ((maxControlAmtRight - minControlAmtRight) / controlStepAmtRight) + 1;

		totalIterations = (long) (N1 * N2 * N3 * N4 * N5 * N6);

		isRunning = true;
		playerArr = new Player[0];

		// System.out.println("Thread: " + threadNum + " Iterations: " + totalIterations
		// + ": " + minStuffAmt + "-" + maxStuffAmt + " /// " + minMovementAmt + "-" +
		// maxMovementAmt + " /// " + minControlAmt + "-" + maxControlAmt + " /// " +
		// minStuffAmtRight + "-" + maxStuffAmtRight + " /// " + minMovementAmtRight +
		// "-" + maxMovementAmtRight + " /// " + minControlAmtRight + "-" +
		// maxControlAmtRight);

		// Run the calculations and place all the data into a double array
		// We then use the saved data to brute force every possible combination to get
		// our closest fit

		for (double currStuffAmt = minStuffAmt; currStuffAmt <= maxStuffAmt; currStuffAmt += stuffStepAmt) {
			for (double currMovementAmt = minMovementAmt; currMovementAmt <= maxMovementAmt; currMovementAmt += movementStepAmt) {
				for (double currControlAmt = minControlAmt; currControlAmt <= maxControlAmt; currControlAmt += controlStepAmt) {
					for (double currStuffAmtRight = minStuffAmtRight; currStuffAmtRight <= maxStuffAmtRight; currStuffAmtRight += stuffStepAmtRight) {
						for (double currMovementAmtRight = minMovementAmtRight; currMovementAmtRight <= maxMovementAmtRight; currMovementAmtRight += movementStepAmtRight) {
							for (double currControlAmtRight = minControlAmtRight; currControlAmtRight <= maxControlAmtRight; currControlAmtRight += controlStepAmtRight) {

								Player[] sortedArr = new Player[calculatedSheet.getPlayerArr().length];

								for (int i = 0; i < calculatedSheet.getPlayerArr().length; i++) {
									Player oldPlayer = calculatedSheet.getPlayerArr()[i];

									if (oldPlayer == null) {
										System.out.println("null player in position " + i);
									}

									// Create a copy of the player object
									Player newPlayer = new Player();
									newPlayer.stuff = oldPlayer.stuff;
									newPlayer.movement = oldPlayer.movement;
									newPlayer.control = oldPlayer.control;
									newPlayer.stuffMultiplier = currStuffAmt;
									newPlayer.movementMultiplier = currMovementAmt;
									newPlayer.controlMultiplier = currControlAmt;
									newPlayer.stuffRight = oldPlayer.stuffRight;
									newPlayer.movementRight = oldPlayer.movementRight;
									newPlayer.controlRight = oldPlayer.controlRight;
									newPlayer.stuffMultiplierRight = currStuffAmtRight;
									newPlayer.movementMultiplierRight = currMovementAmtRight;
									newPlayer.controlMultiplierRight = currControlAmtRight;

									newPlayer.name = oldPlayer.name;
									sortedArr[i] = newPlayer;
								}

								// Sort the array from highest to lowest stats
								Arrays.sort(sortedArr, new PlayerSort(currStuffAmt, currMovementAmt, currControlAmt,
										currStuffAmtRight, currMovementAmtRight, currControlAmtRight));

								// Run calculations to discover the current variance
								int currVarianceAmt = sortPlayerArr(sortedArr);

								// If the current variance is lower than the last lowest variance, replace it
								if (currVarianceAmt > varianceAmount) {
									//equivilantVarianceList = new ArrayList();
									varianceAmount = currVarianceAmt;
									playerArr = sortedArr;
								} else if (currVarianceAmt == varianceAmount) {
									//equivilantVarianceList.add(playerArr);
								}

								iterations++;
							}
						}
					}
				}
			}
		}
		isRunning = false;
		 System.out.println("Thread " + threadNum + " finished calculating " +
		 iterations + " ---- Variance = " + varianceAmount);
	}

	public long getIterations() {
		return iterations;
	}

	public int getLowestVariance() {
		return varianceAmount;
	}

	public Player[] getPlayerArr() {
		return playerArr;
	}

	public ArrayList<Player[]> getEquiviantVarianceList() {
		return equivilantVarianceList;
	}

	public int sortPlayerArr(Player[] playerArr) {
		int totalPointsForArr = 0;
		if (playerArr != null) {
			for (int i = 0; i < playerArr.length; i++) {
				if (playerArr[i] != null) {

					String nameObserved = observedSheet.getNameArr()[i];
					String nameCalculated = playerArr[i].name;
					if (nameCalculated.equals(nameObserved)) {
							totalPointsForArr += 1;	
							
					}
				}
			}
		}
		return totalPointsForArr;
	}

/*
 * public int sortPlayerArr(Player[] playerArr) { int totalPointsForArr = -1; if
 * (playerArr != null) { for (int i = 0; i < playerArr.length; i++) { if
 * (playerArr[i] != null) {
 * 
 * String nameObserved = observedSheet.getNameArr()[i]; String nameCalculated =
 * playerArr[i].name; if (!nameCalculated.equals(nameObserved) && nameCalculated
 * != null && nameObserved != null) { int posOfObservedName = -1;
 * 
 * for (int j = 0; j < observedSheet.getNameArr().length; j++) { if
 * (nameCalculated.equals(observedSheet.getNameArr()[j])) { posOfObservedName =
 * j; }
 * 
 * } if (posOfObservedName == -1) {
 * System.err.println("Error! Name not found in the list! " + nameCalculated); }
 * else { if (totalPointsForArr == -1) { totalPointsForArr++; }
 * totalPointsForArr += Math.abs(i - posOfObservedName);// i = the pos of the
 * calculated name }
 * 
 * } } } } return totalPointsForArr; }
 */
}
