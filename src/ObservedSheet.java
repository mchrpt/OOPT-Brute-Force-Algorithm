import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

//We are assuming the first column is the names
public class ObservedSheet extends Spreadsheet{
	public String[] nameList;
	
	public ObservedSheet(File spreadSheet) {
		super(spreadSheet);
	}

	@Override
	public void initializeData() {
		String headings = sheetScanner.nextLine(); //Can be disregarded or used as a check to make sure all the data is handled correctly
		System.out.println(headings);
		
		/*
		while(sheetScanner.hasNextLine()) {
			
		}
		*/
		
	}
	
}
