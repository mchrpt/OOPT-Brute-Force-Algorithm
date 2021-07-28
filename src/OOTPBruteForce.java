import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class OOTPBruteForce {

	private int numberOfRecursions;
	private int totalThreadAmt = 1;
	private final int stepMultiplier = 3;
	private double minStuffAmt, minMovementAmt, minControlAmt, minStuffAmtRight, minMovementAmtRight,
			minControlAmtRight; // The lower bound
	private double maxStuffAmt, maxMovementAmt, maxControlAmt, maxStuffAmtRight, maxMovementAmtRight,
			maxControlAmtRight; // The upper bound
	private double stuffStepAmt, movementStepAmt, controlStepAmt, stuffStepAmtRight, movementStepAmtRight,
			controlStepAmtRight; // How much we step forward by each scan

	private BruteForceThread[] threadArr;

	private ObservedSheet observedSheet;
	private CalculatedSheet calculatedSheet;
	private static final DecimalFormat df = new DecimalFormat("#.##");
	private ArrayList<Integer> calculatedPositions;
	private ArrayList<Integer> observedRemovePositions;

	public OOTPBruteForce() {
System.out.println(Double.MAX_VALUE);
		System.out.println("Welcome to the OOTP Brute Force Calculator!");
		System.out.println("Credit to Matthew Hendrickson (Programmer) and Benjamin Mayhew (Statistics)");
		System.out.println("Scanning files...\n");

		loadFiles();

		System.out.println("Ratings compiled and loaded successfully!\n");

		System.out.println("Crossreferencing names between sheets, " + (observedSheet.getSheetSize())
				+ " in observed sheet and " + (calculatedSheet.getSheetSize()) + " in calculated sheet");

		crossReference();

		inputBoundaries();

		System.out
				.println(calculatedPositions.size() + " players will be calculated into their respective positions and "
						+ observedRemovePositions.size() + " player(s) will be disregarded...\n");

		calculatedSheet.pruneData(calculatedPositions);
		observedSheet.pruneData(observedRemovePositions);

		System.out.println("\n\n\nRunning brute force, please wait...");

		createAndExecuteThreads();

	}
	
	public OOTPBruteForce(int numberOfRecursions, double minStuffAmt, double minMovementAmt, double minControlAmt, double minStuffAmtRight, double minMovementAmtRight,
			double minControlAmtRight, double maxStuffAmt, double maxMovementAmt, double maxControlAmt, double maxStuffAmtRight, double maxMovementAmtRight,
			double maxControlAmtRight, double stuffStepAmt, double movementStepAmt, double controlStepAmt, double stuffStepAmtRight, double movementStepAmtRight,
			double controlStepAmtRight) {
		
		this.numberOfRecursions = numberOfRecursions;
		this.minStuffAmt = minStuffAmt;
		this.minMovementAmt = minMovementAmt;
		this.minControlAmt = minControlAmt;
		this.minStuffAmtRight = minStuffAmtRight;
		this.minMovementAmtRight = minMovementAmtRight;
		this.minControlAmtRight = minControlAmtRight;
		
		this.maxStuffAmt = maxStuffAmt;
		this.maxMovementAmt = maxMovementAmt;
		this.maxControlAmt = maxControlAmt;
		this.maxStuffAmtRight = maxStuffAmtRight;
		this.maxMovementAmtRight = maxMovementAmtRight;
		this.maxControlAmtRight = maxControlAmtRight;
		
		this.stuffStepAmt = stuffStepAmt;
		this.movementStepAmt = movementStepAmt;
		this.controlStepAmt = controlStepAmt;
		this.stuffStepAmtRight = stuffStepAmtRight;
		this.movementStepAmtRight = movementStepAmtRight;
		this.controlStepAmtRight = controlStepAmtRight;
		
		loadFiles();
		crossReference();
		calculatedSheet.pruneData(calculatedPositions);
		observedSheet.pruneData(observedRemovePositions);
		createAndExecuteThreads();
	}

	public void loadFiles() {
		try {
			InputStream isObs = this.getClass().getResourceAsStream("data/observed.csv");
			InputStream isCal = this.getClass().getResourceAsStream("data/calculated.csv");
			observedSheet = new ObservedSheet(isObs);
			calculatedSheet = new CalculatedSheet(isCal);
		} catch (Exception e) {
			System.err.println("Error! Files not found! Make sure they are named 'observed.csv' and calculated.csv'!");
			System.exit(0);

		}
	}

	public void crossReference() {
		// We find which positions the players we need are at
		calculatedPositions = new ArrayList<>();
		observedRemovePositions = new ArrayList<>();

		// Compare everything in the observed sheet to the calculated sheet, print any
		// extraneous cases
		for (int i = 0; i < observedSheet.getSheetSize(); i++) {
			String nameObserved = observedSheet.getNameArr()[i];
			boolean isNameFound = false;

			for (int j = 0; j < calculatedSheet.getSheetSize(); j++) {
				String nameCalculated = calculatedSheet.getNameArr()[j];
				if (nameCalculated != null && nameObserved != null && !nameObserved.equals(null)
						&& !nameCalculated.equals(null)) {
					if (nameCalculated.equals(nameObserved)) {
						isNameFound = true;
						calculatedPositions.add(j);
					}
				}
			}
			if (!isNameFound && nameObserved != null) {
				System.err.println("Player: " + nameObserved
						+ " in observed file was not found and will be disregarded in the calculation");
				observedRemovePositions.add(i);
			}

		}
	}

	public void inputBoundaries() {
		// Enter boundaries
		Scanner keyboard = new Scanner(System.in);
		System.out.println(
				"Enter 1 to enter all the values or 2 to auto enter 1, 10, 1 with 12 threads");
		int firstEnterValue = keyboard.nextInt();
		if (firstEnterValue == 1) {
			System.out.println("\nEnter the number of threads (must be at least 4)");
			totalThreadAmt = keyboard.nextInt();

			System.out.println("\nEnter the lower, upper, and step amount for Stuff VL");
			minStuffAmt = keyboard.nextDouble();
			maxStuffAmt = keyboard.nextDouble();
			stuffStepAmt = keyboard.nextDouble();
			System.out.println("min Stuff VL= " + minStuffAmt + " max Stuff VL= " + maxStuffAmt + " stuff Step Amt VL= "
					+ stuffStepAmt);

			System.out.println("\nEnter the lower, upper, and step amount for Movement");
			minMovementAmt = keyboard.nextDouble();
			maxMovementAmt = keyboard.nextDouble();
			movementStepAmt = keyboard.nextDouble();
			System.out.println("min Movement VL= " + minMovementAmt + " max Movement VL= " + maxMovementAmt
					+ " Movement Step Amt VL= " + movementStepAmt);

			System.out.println("\nEnter the lower, upper, and step amount for Control VL");
			minControlAmt = keyboard.nextDouble();
			maxControlAmt = keyboard.nextDouble();
			controlStepAmt = keyboard.nextDouble();
			System.out.println("min Control VL= " + minControlAmt + " max Control VL= " + maxControlAmt
					+ " Control Step Amt VL= " + controlStepAmt);

			System.out.println("\nEnter the lower, upper, and step amount for Stuff Right");
			minStuffAmtRight = keyboard.nextDouble();
			maxStuffAmtRight = keyboard.nextDouble();
			stuffStepAmtRight = keyboard.nextDouble();
			System.out.println("min Stuff VR = " + minStuffAmtRight + " max Stuff VR = " + maxStuffAmtRight
					+ " stuff Step Amt VR = " + stuffStepAmtRight);

			System.out.println("\nEnter the lower, upper, and step amount for Movement VR");
			minMovementAmtRight = keyboard.nextDouble();
			maxMovementAmtRight = keyboard.nextDouble();
			movementStepAmtRight = keyboard.nextDouble();
			System.out.println("min Movement VR= " + minMovementAmtRight + " max Movement VR = " + maxMovementAmtRight
					+ " Movement Step Amt VR = " + movementStepAmtRight);

			System.out.println("\nEnter the lower, upper, and step amount for Control VR");
			minControlAmtRight = keyboard.nextDouble();
			maxControlAmtRight = keyboard.nextDouble();
			controlStepAmtRight = keyboard.nextDouble();
			System.out.println("min Control VR = " + minControlAmtRight + " max Control VR = " + maxControlAmtRight
					+ " Control Step Amt VR = " + controlStepAmtRight);

		} else if (firstEnterValue == 2) {

			numberOfRecursions = 3;
			totalThreadAmt = 12;

			minStuffAmt = 0;
			maxStuffAmt = 10;

			minMovementAmt = 0;
			maxMovementAmt = 10;

			minControlAmt = 0;
			maxControlAmt = 10;

			minStuffAmtRight = 0;
			maxStuffAmtRight = 10;

			minMovementAmtRight = 0;
			maxMovementAmtRight = 10;

			minControlAmtRight = 0;
			maxControlAmtRight = 10;

			stuffStepAmt = 1;
			movementStepAmt = 1;
			controlStepAmt = 1;

			stuffStepAmtRight = 1;
			movementStepAmtRight = 1;
			controlStepAmtRight = 1;

		} else {
			for (int i = 0; i < 100; i++) {
				System.out.println("How dare you enter the wrong number");
			}
			System.out.println("Bitch baseball fuckin sucks anyway badminton is better go eat some crayons");
			System.exit(0);
		}

	}

	public void createAndExecuteThreads() {

		// We need to count how many iterations to get a percentage of when it will
		// finish
		// Arrays start at zero
		threadArr = new BruteForceThread[totalThreadAmt];

		// Start running our threads
		for (int i = 0; i < totalThreadAmt; i++) {
			// max - min / total, add this times i
			double increaseAmountStuff = (maxStuffAmt - minStuffAmt) / totalThreadAmt;
			double minStuffThreadAmt = minStuffAmt + (increaseAmountStuff * i);
			double maxStuffThreadAmt = minStuffThreadAmt + increaseAmountStuff;

			threadArr[i] = new BruteForceThread(i, calculatedSheet, observedSheet, minStuffThreadAmt, minMovementAmt,
					minControlAmt, minStuffAmtRight, minMovementAmtRight, minControlAmtRight, maxStuffThreadAmt,
					maxMovementAmt, maxControlAmt, maxStuffAmtRight, maxMovementAmtRight, maxControlAmtRight,
					stuffStepAmt, movementStepAmt, controlStepAmt, stuffStepAmtRight, movementStepAmtRight,
					controlStepAmtRight);
			threadArr[i].start();
		}

		long totalIterations = getTotalIterations();

		long divideAmount = Math.max((totalIterations / 1000), 1);
		int varianceAmount = Integer.MAX_VALUE;
		long iterations = 0;
		while (threadsAreRunning()) {
			iterations = getIterations();
			if (iterations % divideAmount == 0 || iterations == totalIterations) {
				System.out.println("Iteration " + iterations + "/" + totalIterations + " ["
						+ df.format(((double) iterations / (double) totalIterations) * 100) + "%] "
						+ "Current Lowest Variance " + getLowestVariance());
			}
		}

		Player[] bestPlayerArr = getBestPlayerArr();
		printArr(getBestPlayerArr());

		double stuffAmount = bestPlayerArr[0].stuffMultiplier;
		double movementAmount = bestPlayerArr[0].movementMultiplier;
		double controlAmount = bestPlayerArr[0].controlMultiplier;
		double stuffAmountRight = bestPlayerArr[0].stuffMultiplierRight;
		double movementAmountRight = bestPlayerArr[0].movementMultiplierRight;
		double controlAmountRight = bestPlayerArr[0].controlMultiplierRight;

		System.out.println(getLowestVariance() + " total matches, lowest had stuff = " + df.format(stuffAmount)
				+ " movement = " + df.format(movementAmount) + " control = " + df.format(controlAmount)
				+ " stuff right = " + df.format(stuffAmountRight) + " movement right = "
				+ df.format(movementAmountRight) + " control right = " + df.format(controlAmountRight));
		
		if(numberOfRecursions > 0) {
			numberOfRecursions--;
			
			minStuffAmt = stuffAmount - stuffStepAmt * stepMultiplier;
			minMovementAmt = movementAmount - movementStepAmt * stepMultiplier;
			minControlAmt = controlAmount - controlStepAmt * stepMultiplier; 
			minStuffAmtRight = stuffAmountRight - stuffStepAmt * stepMultiplier;
			minMovementAmtRight = movementAmountRight - movementStepAmt * stepMultiplier;
			minControlAmtRight = controlAmountRight - controlStepAmt * stepMultiplier; 
			
			maxStuffAmt = stuffAmount + stuffStepAmt * stepMultiplier;
			maxMovementAmt = movementAmount + movementStepAmt * stepMultiplier;
			maxControlAmt = controlAmount + controlStepAmt * stepMultiplier; 
			maxStuffAmt = stuffAmount + stuffStepAmt * stepMultiplier;
			maxMovementAmt = movementAmount + movementStepAmt * stepMultiplier;
			maxControlAmt = controlAmount + controlStepAmt * stepMultiplier; 
			
			stuffStepAmt = stuffStepAmt/10;
			movementStepAmt = movementStepAmt/10;
			controlStepAmt = controlStepAmt/10;
			stuffStepAmtRight = stuffStepAmtRight/10;
			movementStepAmtRight = movementStepAmtRight/10;
			controlStepAmtRight = controlStepAmtRight/10;
			
			new OOTPBruteForce(numberOfRecursions, minStuffAmt, minMovementAmt, minControlAmt, minStuffAmtRight, minMovementAmtRight,
					minControlAmtRight, maxStuffAmt, maxMovementAmt, maxControlAmt, maxStuffAmtRight, maxMovementAmtRight,
					maxControlAmtRight, stuffStepAmt, movementStepAmt, controlStepAmt, stuffStepAmtRight, movementStepAmtRight,
					controlStepAmtRight);
		}
	}

	private synchronized Player[] getBestPlayerArr() {
		Player[] bestPlayerArr = null;
		int lowestVariance = 0;
		for (int i = 0; i < totalThreadAmt; i++) {
			if (threadArr[i].getLowestVariance() > lowestVariance) {
				bestPlayerArr = threadArr[i].getPlayerArr();
				lowestVariance = threadArr[i].getLowestVariance();
			}
		}
		System.out.println(lowestVariance);
		return bestPlayerArr;
	}

	private long getTotalIterations() {
		long totalIterations = 0;
		totalIterations = threadArr[0].totalIterations * totalThreadAmt;
		return totalIterations;
	}

	public int getIterations() {
		int totalIterations = 0;
		for (int i = 0; i < totalThreadAmt; i++) {
			totalIterations += threadArr[i].getIterations();
		}
		return totalIterations;
	}

	public int getLowestVariance() {
		int lowestVariance = -1;
		for (int i = 0; i < totalThreadAmt; i++) {
			lowestVariance = Math.max(lowestVariance, threadArr[i].getLowestVariance());
		}
		return lowestVariance;
	}

	public synchronized boolean threadsAreRunning() {
		boolean anyThreadRunning = false;
		for (int i = 0; i < totalThreadAmt; i++) {
			if (threadArr[i].isRunning) {
				anyThreadRunning = true;
			}
		}
		return anyThreadRunning;
	}

	public int sortPlayerArr(Player[] playerArr) {
		int totalPointsForArr = 0;
		if (playerArr != null) {
			for (int i = 0; i < playerArr.length; i++) {
				if (playerArr[i] != null) {

					String nameObserved = observedSheet.getNameArr()[i];
					String nameCalculated = playerArr[i].name;
					if (!nameCalculated.equals(nameObserved) && nameCalculated != null && nameObserved != null) {
						int calculatedPoints = 0;

						for (int j = 0; j < observedSheet.getNameArr().length; j++) {
							if (nameCalculated.equals(observedSheet.getNameArr()[j])) {
								//calculatedPoints = Math.max(4-j, 1);
								totalPointsForArr += calculatedPoints;
							}
						}
					}
				}
			}
		}
		return totalPointsForArr;
	}

	public void printArr(Player[] playerArr) {
		System.out.println("\n\nName ------- Weighted Value");
		int amountMatched = 0;
		for(int i = 0; i < playerArr.length; i++) {
			
			if(playerArr[i].name.equals(observedSheet.nameArr[i])) {
				System.out.println(playerArr[i].name + " --- Match --- " + observedSheet.nameArr[i]);
				amountMatched++;
			}else {
				System.out.println(playerArr[i].name + " ------ " + observedSheet.nameArr[i]);
			}
			
		}
		System.out.println(amountMatched);
		System.out.println("\n\n");

	}

	public static void main(String[] args) {

		// Start main method
		OOTPBruteForce start = new OOTPBruteForce();

	}

}
