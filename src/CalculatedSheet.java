import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class CalculatedSheet extends Spreadsheet{
	private Player[] playerArr;
	public CalculatedSheet(InputStream spreadSheet) {
		super(spreadSheet);
	}


	@Override
	public void initializeData() {
		playerArr = new Player[(int) (lineCount)];
		nameArr = new String[(int)lineCount];
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
						playerArr[i].stuff = lineScanner.nextInt();
						//System.out.println(stuffArr[i]);
					}else if(count == 2) {
						playerArr[i].movement = lineScanner.nextInt();
						//System.out.println(movementArr[i]);
					}else if(count == 3) {
						playerArr[i].control = lineScanner.nextInt();
						//System.out.println(controlArr[i]);
					}else if(count == 4){
						playerArr[i].stuffRight = lineScanner.nextInt();
					}else if(count == 5) {
						playerArr[i].movementRight = lineScanner.nextInt();
					}else if(count == 6) {
						playerArr[i].controlRight = lineScanner.nextInt();
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
}

