import java.io.InputStream;
import java.util.ArrayList;

//We are assuming the first column is the names
public class ObservedSheet extends Spreadsheet {

	public ObservedSheet(InputStream isObs) {
		super(isObs);
	}

	@Override
	public void initializeData() {
		 // Can be disregarded or used as a check to make sure all the data is
													// handled correctly
		nameArr = new String[(int) (lineCount)];

		// Put all the data and prioritize them accordingly
		for (int i = 0; i < lineCount; i++) {
				nameArr[i] = sheetData.get(i);
				// System.out.println(i + " " + nameArr[i]);
			}
		}


	@Override
	public void pruneData(ArrayList<Integer> observedRemovePositions) {
		String[] newNameArr = new String[(int) (lineCount - observedRemovePositions.size())];

		int count = 0;
		int totalRemovedPositions = 0;
		for(int i = 0; i < nameArr.length; i++) {
			boolean foundPrunablePosition = false;
			int positionToPrune = -1;
			for(int position : observedRemovePositions) {
				if(i == position) {
					foundPrunablePosition = true;
					positionToPrune = position;
					totalRemovedPositions++;
				}
			}

			if(foundPrunablePosition) {
				observedRemovePositions.remove((Object)positionToPrune);
			}else {
				newNameArr[count] = nameArr[i];
				count++;
			}

		}
		nameArr = newNameArr;
		System.out.println("Observed sheet successfully pruned with " + (nameArr.length) + " players remaining");

	}
}
