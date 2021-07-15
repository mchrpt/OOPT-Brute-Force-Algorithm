import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Spreadsheet{

	private InputStream sheetCSV;
	private Scanner sheetScanner;
	protected long lineCount;
	protected String[] nameArr;
	protected ArrayList<String> sheetData;
	public Spreadsheet(InputStream spreadSheet) {
		sheetCSV = spreadSheet;

		try {
			sheetScanner = new Scanner(new BufferedReader(new InputStreamReader(sheetCSV)));

			String data;
			sheetData = new ArrayList<>();
			sheetScanner.nextLine();
			while(sheetScanner.hasNextLine() && (data = sheetScanner.nextLine()) != null) {
				sheetData.add(data);
				//System.out.println(data);
			}
			lineCount = sheetData.size();
			//System.out.println(lineCount);
			initializeData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] getNameArr() {
		return nameArr;
	}

	public int getSheetSize() {
		return (int) lineCount;
	}


	public abstract void initializeData();
	public abstract void pruneData(ArrayList<Integer> positionList);
}
