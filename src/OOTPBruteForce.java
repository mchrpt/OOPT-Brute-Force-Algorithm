import java.io.File;

public class OOTPBruteForce {

	private double minStuffAmt, minMovementAmt, minControlAmt; //The lower bound
	private double maxStuffAmt, maxMovementAmt, maxControlAmt; //The upper bound
	private double stuffStepAmt, movementStepAmt, controlStepAmt; //How much we step forward by each scan

	private ObservedSheet observedSheet;
	
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
		
		System.out.println("Ratings compiled and loaded successfully!\n");
		
		/*
		File batting_OVR_Folder = new File("batting_data/ovr");
		File[] batting_OVR_Files = batting_OVR_Folder.listFiles();
		System.out.println("Found " + batting_OVR_Files.length + " in batting_data/ovr");

		File batting_VL_Folder = new File("batting_data/vl");
		File[] batting_VL_Files = batting_VL_Folder.listFiles();
		System.out.println("Found " + batting_VL_Files.length + " in batting_data/vl");

		File batting_VR_Folder = new File("batting_data/vr");
		File[] batting_VR_Files = batting_VR_Folder.listFiles();
		System.out.println("Found " + batting_VR_Files.length + " in batting_data/vr");

		File pitching_VL_Folder = new File("pitching_data/vl");
		File[] pitching_VL_Files = pitching_VL_Folder.listFiles();
		System.out.println("Found " + pitching_VL_Files.length + " in pitching_data/vl");

		File pitching_VR_Folder = new File("pitching_data/vr");
		File[] pitching_VR_Files = pitching_VR_Folder.listFiles();
		System.out.println("Found " + pitching_VR_Files.length + " in pitching_data/vr\n");
		*/
		
	}
	
	public static void main(String[] args) {
		
		OOTPBruteForce start = new OOTPBruteForce();

	}

}
