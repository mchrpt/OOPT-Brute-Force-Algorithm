import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;
import java.nio.file.Files;
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
	
	private Player[][] sortedPlayerDoubleArr;
			
	public OOTPBruteForce() {

		System.out.println("Welcome to the OOTP Brute Force Calculator!");
		System.out.println("Credit to Matthew Hendrickson (Programmer) and Benjamin Mayhew (Statistics)");
		System.out.println("Scanning files...\n");
		
		loadFiles();
		
		System.out.println("Ratings compiled and loaded successfully!\n");
		
		System.out.println("Crossreferencing names between sheets, " + (observedSheet.getSheetSize()) + " in observed sheet and " + (calculatedSheet.getSheetSize()) + " in calculated sheet");
		
		crossReference();
		
		inputBoundaries();
		
		System.out.println(calculatedPositions.size() + " players will be calculated into their respective positions and " + observedRemovePositions.size() + " player(s) will be disregarded...\n");
		
		calculatedSheet.pruneData(calculatedPositions);
		observedSheet.pruneData(observedRemovePositions);
		
		System.out.println("\n\n\nRunning brute force, please wait...");
		
		createDoubleArr();
		
		runDoubleArrCalculations();

	}
	
	public void loadFiles() {
		try{
			InputStream isObs = this.getClass().getResourceAsStream("/data/observed.csv");
			InputStream isCal = this.getClass().getResourceAsStream("/data/calculated.csv");
			observedSheet = new ObservedSheet(isObs);
			calculatedSheet = new CalculatedSheet(isCal);
		}catch(Exception e) {
			System.err.println("Error! Files not found! Make sure they are named 'observed.csv' and calculated.csv'!");
			System.exit(0);
			
		}
	}
	
	public void crossReference() {
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
	}
	public void inputBoundaries(){
		//Enter boundaries
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
				
	}
	public void createDoubleArr() {
		int totalIterations = (int)((maxStuffAmt / stuffStepAmt) * (maxMovementAmt / movementStepAmt) * (maxControlAmt / controlStepAmt));
		sortedPlayerDoubleArr = new Player[totalIterations][];
		
		//Run the calculations and place all the data into a double array
		//We then use the saved data to brute force every possible combination to get our closest fit
		int iterations = 0;
		for(double currStuffAmt = minStuffAmt; currStuffAmt <= maxStuffAmt; currStuffAmt += stuffStepAmt) {
			for(double currMovementAmt = minMovementAmt; currMovementAmt <= maxMovementAmt; currMovementAmt += movementStepAmt) {
				for(double currControlAmt = minControlAmt; currControlAmt <= maxControlAmt; currControlAmt += controlStepAmt) {
					
					Player[] sortedArr = new Player[calculatedSheet.getPlayerArr().length];
					
					for(int i = 0; i < calculatedSheet.getPlayerArr().length; i++) {
						Player oldPlayer = calculatedSheet.getPlayerArr()[i];
						
						if(oldPlayer == null) {
							System.out.println("null player in position " + i);
						}
						
						Player newPlayer = new Player();
						newPlayer.stuff = oldPlayer.stuff;
						newPlayer.movement = oldPlayer.movement;
						newPlayer.control = oldPlayer.control;
						newPlayer.stuffMultiplier = currStuffAmt;
						newPlayer.movementMultiplier = currMovementAmt;
						newPlayer.controlMultiplier = currControlAmt;
						newPlayer.name = oldPlayer.name;
						sortedArr[i] = newPlayer;
					}

					
					Arrays.sort(sortedArr, new PlayerSort(currStuffAmt, currMovementAmt, currControlAmt));
					sortedPlayerDoubleArr[iterations] = sortedArr;
					
					if(iterations % (totalIterations / 1000) == 0) {
						System.out.println(iterations + " " + currStuffAmt + " " + currMovementAmt + " " + currControlAmt);
					}
					
					
					iterations++;
				}
			}
		}
	}
	public void runDoubleArrCalculations() {
		System.out.println("\n\n" + sortedPlayerDoubleArr.length + " Arrays with " + sortedPlayerDoubleArr[0].length + " players per array");
		int maxPointsOverall = Integer.MAX_VALUE;
		double stuffAmount = 0, movementAmount = 0, controlAmount = 0;
		Player[] finalPlayerArr = null;
		for(Player[] playerArr : sortedPlayerDoubleArr) {
			
			if(playerArr != null) {
				int totalPointsForArr = 0;
				for(int i = 0; i < playerArr.length; i++) {
					if(playerArr[i] != null) {
						
					
					String nameObserved = observedSheet.getNameArr()[i];
					String nameCalculated = playerArr[i].name; 
					if(!nameCalculated.equals(nameObserved) && nameCalculated != null && nameObserved != null) {
						int posOfObservedName = 0;
					
							for(int j = 0; j < observedSheet.getNameArr().length; j++) {
								if(nameCalculated.equals(observedSheet.getNameArr()[j])) {
									posOfObservedName = j;
								}
							
							
						}
					if(posOfObservedName == 0) {
							//System.err.println("Error! Name not found in the list! " + nameCalculated);
						}else {
							totalPointsForArr += Math.abs(i-posOfObservedName);//i = the pos of the calculated name
							
						}
						
						
						}
					}
				}
				
				if(totalPointsForArr < maxPointsOverall && playerArr != null && playerArr[0] != null) {
					maxPointsOverall = totalPointsForArr;
					finalPlayerArr = playerArr;
					stuffAmount = playerArr[0].stuffMultiplier;
					movementAmount = playerArr[0].movementMultiplier;
					controlAmount = playerArr[0].controlMultiplier;
					
					
				}
				
				
			}
			
		}
		printArr(finalPlayerArr);
		System.out.println(maxPointsOverall + " total variance, highest had stuff = " + stuffAmount + " movement = " + movementAmount + " control = " + controlAmount);
		
	}
	public void printDoubleArr() {
		for(Player[] playerArr : sortedPlayerDoubleArr) {
			if(playerArr != null) {
				for(Player player : playerArr) {
					System.out.println(player.name + ", " + player.stuff + ", " + player.movement + ", " + player.control);
				
				}
			}
			System.out.println("\n\n\n\n");
		}
		
	}
	public void printArr(Player[] playerArr) {
		System.out.println("\n\nName, Stuff, Movement, Control, ------ Weighted Stuff, Weighted Movement, Weighted Control");
		for(Player player : playerArr) {
			System.out.println(player.name + ", " + player.stuff + ", " + player.movement + ", " + player.control + " ------ " + player.stuffMultiplier * player.stuff + ", " + player.movementMultiplier * player.movement + ", " + player.controlMultiplier * player.control + " = " + ((player.stuffMultiplier * player.stuff) + (player.movementMultiplier * player.movement) + (player.controlMultiplier * player.control)));
		}
		System.out.println("\n\n");
	}
	
	public static void main(String[] args) {
		
		OOTPBruteForce start = new OOTPBruteForce();

	}

}
