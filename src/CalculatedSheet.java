import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class CalculatedSheet extends Spreadsheet{
	private int[] stuffArr, movementArr, controlArr;
	private Player[] playerArr;

	
	public CalculatedSheet(InputStream spreadSheet) {
		super(spreadSheet);
	}
	
	
	@Override
	public void initializeData() {

		stuffArr = new int[(int) (lineCount)];
		movementArr = new int[(int) (lineCount)];
		controlArr = new int[(int) (lineCount)];
		nameArr = new String[(int) (lineCount)];
		playerArr = new Player[(int) (lineCount)];

		Scanner lineScanner;
		//Put all the data and prioritize them accordingly
		for(int i = 0; i < lineCount; i++) {
				String line = sheetData.get(i);
				lineScanner = new Scanner(line).useDelimiter(",");
				
				int count = 0;
				while(lineScanner.hasNext()) {
					
					if(count == 0) {
						playerArr[i] = new Player();
						playerArr[i].name = nameArr[i] = lineScanner.next();
					}else if(count == 1) { 
						playerArr[i].stuff = stuffArr[i] = lineScanner.nextInt(); 
						//System.out.println(stuffArr[i]);
					}else if(count == 2) {
						playerArr[i].movement = movementArr[i] = lineScanner.nextInt();
						//System.out.println(movementArr[i]);
					}else if(count == 3) {
						playerArr[i].control = controlArr[i] = lineScanner.nextInt();
						//System.out.println(controlArr[i]);
					}else {
						//Extra space! Skipping!
					}
					count++;
				}
				
			}
		}
		
	
	
	@Override
	public void pruneData(ArrayList<Integer> calculatedPositions) {
		Player[] newPlayerArr = new Player[calculatedPositions.size()];
		
		int count = 0;
		for(int position : calculatedPositions) {
			newPlayerArr[count] = playerArr[position];
			count++;
		}
		playerArr = newPlayerArr;
		System.out.println("Calculated sheet successfully pruned with " + playerArr.length + " players remaining");
	}
	
	
	public Player[] getPlayerArr() {
		return playerArr;
	}
	
	public void printArr() {
		for(Player player : playerArr) {
			System.out.println(player.name + ", " + player.stuff + ", " + player.movement + ", " + player.control);
		}
	}
}

