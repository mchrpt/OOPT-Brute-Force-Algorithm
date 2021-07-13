import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public abstract class Spreadsheet{
	
	private File sheetCSV;
	protected Scanner sheetScanner;
	protected long lineCount;
	
	public Spreadsheet(File spreadSheet) {
		sheetCSV = spreadSheet;
		
		try {
			sheetScanner = new Scanner(sheetCSV);
			sheetScanner.useDelimiter(",");
			lineCount = Files.lines(Paths.get(spreadSheet.getAbsolutePath())).count();
			//System.out.println(lineCount);
			initializeData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public abstract void initializeData();
}
