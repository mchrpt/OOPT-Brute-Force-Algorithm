import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class OOTPBruteForce {

	private double minStuffAmt, minMovementAmt, minControlAmt; //The lower bound
	private double maxStuffAmt, maxMovementAmt, maxControlAmt; //The upper bound
	private double stuffStepAmt, movementStepAmt, controlStepAmt; //How much we step forward by each scan

	private ObservedSheet observedSheet;
	private CalculatedSheet calculatedSheet;
	
	private ArrayList<Integer> calculatedPositions;
	private ArrayList<Integer> observedRemovePositions;
			
	public OOTPBruteForce() {
		
		System.out.println("Welcome to the Advanced (sorta) OOTP Brute Force Calculator!");
		System.out.println("Credit to Matthew Hendrickson (Programmer) and Ben Mayhew (Moral support and statistics)");
		System.out.println("Scanning files...\n");

		//TODO: Find all the files and smash them into one CSV 
		File calculated_Folder = new File("data/calculated");
		File[] calculated_Files = calculated_Folder.listFiles();
		System.out.println("Found " + calculated_Files.length + " file(s) in data/calculated\n");
		
		File observed_Folder = new File("data/observed");
		File[] observed_Files = observed_Folder.listFiles();
		System.out.println("Found " + observed_Files.length + " file(s) in data/observed\n");
		
		observedSheet = new ObservedSheet(observed_Files[0]);
		calculatedSheet = new CalculatedSheet(calculated_Files[0]);
		
		System.out.println("Ratings compiled and loaded successfully!\n");
		
		System.out.println("Crossreferencing names between sheets, " + (observedSheet.getSheetSize() - 1) + " in observed sheet and " + (calculatedSheet.getSheetSize() - 1) + " in calculated sheet");
		
		//We find which positions the players we need are at 
		calculatedPositions = new ArrayList<Integer>();
		observedRemovePositions = new ArrayList<Integer>();
		
		//Compare everything in the observed sheet to the calculated sheet, print any extraneous cases
		for(int i = 0; i < observedSheet.getSheetSize(); i++) {
			String nameObserved = observedSheet.getNameArr()[i];
			boolean isNameFound = false;
			
			for(int j = 0; j < calculatedSheet.getSheetSize(); j++) {
				String nameCalculated = calculatedSheet.getNameArr()[j];
				if(nameCalculated != null && nameObserved != null && !nameObserved.equals(null) && !nameCalculated.equals(null)) {
					if(nameCalculated.equals(nameObserved)) {
						isNameFound = true;
						calculatedPositions.add(j);
					}
				}
			}
			if(!isNameFound && nameObserved != null) {
				System.err.println("Player: " + nameObserved + " in observed file was not found and will be disregarded in the calculation");
				observedRemovePositions.add(i);
			}
			
		}
		System.out.println(calculatedPositions.size() + " players will be calculated into their respective positions and " + observedRemovePositions.size() + " player(s) will be disregarded...\n");
		
		calculatedSheet.pruneData(calculatedPositions);
		observedSheet.pruneData(observedRemovePositions);
		
		Scanner keyboard = new Scanner(System.in);
		System.out.println("\nEnter the lower, upper, and step amount for Stuff");
		minStuffAmt = keyboard.nextDouble();
		maxStuffAmt = keyboard.nextDouble();
		stuffStepAmt = keyboard.nextDouble();
		System.out.println("min Stuff = " + minStuffAmt + " max Stuff = " + maxStuffAmt + " stuff Step Amt = " + stuffStepAmt);
				
		System.out.println("\nEnter the lower, upper, and step amount for Movement");
		minMovementAmt = keyboard.nextDouble();
		maxMovementAmt = keyboard.nextDouble();
		movementStepAmt = keyboard.nextDouble();
		System.out.println("min Movement = " + minMovementAmt + " max Movement = " + maxMovementAmt + " Movement Step Amt = " + movementStepAmt);
		
		System.out.println("\nEnter the lower, upper, and step amount for Control");
		minControlAmt = keyboard.nextDouble();
		maxControlAmt = keyboard.nextDouble();
		controlStepAmt = keyboard.nextDouble();
		System.out.println("min Control = " + minControlAmt + " max Control = " + maxControlAmt + " Control Step Amt = " + controlStepAmt);
		
		int iterations = 0;
		for(double currStuffAmt = minStuffAmt; currStuffAmt < maxStuffAmt - stuffStepAmt; currStuffAmt += stuffStepAmt) {
			for(double currMovementAmt = minMovementAmt; currMovementAmt < maxMovementAmt - movementStepAmt; currMovementAmt += movementStepAmt) {
				for(double currControlAmt = minControlAmt; currControlAmt < maxControlAmt - controlStepAmt; currControlAmt += controlStepAmt) {
					iterations++;
					Arrays.sort(calculatedSheet.getPlayerArr(), new PlayerSort(currStuffAmt, currMovementAmt, currControlAmt));
					calculatedSheet.printArr();
					System.out.println(iterations + " " + currStuffAmt + " " + currMovementAmt + " " + currControlAmt);
				}
			}
		}
		System.out.println(iterations);
	}
	
	public static void main(String[] args) {
		
		OOTPBruteForce start = new OOTPBruteForce();

	}

}
