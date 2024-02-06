// package declaration
package hiddenmarkovmodel;

//import statement(s)
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// class definition
public class HMM {
	// *******************************************************************************************
	// attribute(s)
	private int nStringsConsidered;
	private ArrayList<String> stringSequences;
	private ArrayList<Sequence> sequences;

	private ArrayList<String> stateValues;
	private ArrayList<String> observableMeasurementValues;

	private double pEGivenS;
	private double pFGivenS;

	private double pEGivenT;
	private double pFGivenT;

	private int nCasesConsidered;
	private ArrayList<String> stringCases;

	private ArrayList<ArrayList<StateEstimator>> solution;


	// *******************************************************************************************
	// constructor
	public HMM () {
		this.readInputFile();
		this.predictProbabilityValues();
	}


	// *******************************************************************************************
	// method(s)
	// ===========================================================================================
	/*
	 *		Read input file from hmm.in
	 */
	private void readInputFile () {
		// locate the input file
		String projectDirectory = System.getProperty("user.dir");
		String packageDirectory = "src" + File.separator + "hiddenmarkovmodel";
		String fileName = "hmm.in";
		File trainDataFile = new File(projectDirectory, packageDirectory + File.separator + fileName);

		String fileContent = "";				// will contain the string contents of the input file

		// read the contents of the train data file
		try {
			Scanner scanner = new Scanner(trainDataFile);

			while (scanner.hasNextLine()) {
				fileContent += scanner.nextLine();
				fileContent += (" \n");
			}

			// close the scanner
			scanner.close();

		} catch (FileNotFoundException e) {
			// catch block
			e.printStackTrace();
		}

		System.out.println("==========================================");
		System.out.println("Contents of the input file:\n");
		System.out.println(fileContent);

		// extract the data from the input file's string contents and store them in appropriate variables
		this.extractContents(fileContent);

	}


	// ===========================================================================================
	/*
	 *		Extract and format data from the read input file.
	 */
	private void extractContents(String fileContent) {
		// extract each line from the input file
		ArrayList<String> lines = new ArrayList<>(Arrays.asList(fileContent.split("\n"))) ;

		// --------------------------------------------------------------------------------------
		/*
		 * 		Number of Strings Considered
		 * */
		// extract the number of strings considered for the hidden markov model
		// also remove whitespaces
		String nStrings = lines.remove(0).replaceAll("\\s", "");

		// parse the formatted string
		this.nStringsConsidered = Integer.parseInt(nStrings);


		// --------------------------------------------------------------------------------------
		/*
		 * 		String sequences
		 * */
		// extract the string sequences considered and store them into an ArrayList
		this.stringSequences = new ArrayList<String>();
		for (int i=0; i!=this.nStringsConsidered; i++) {
			this.stringSequences.add(lines.remove(0).replaceAll("\\s", ""));
		}


		// --------------------------------------------------------------------------------------
		/*
		 * 		Possible values for each state in the Markov chain (MC)
		 * */
		// extract the possible values for each state in the Markov chain (MC)
		this.stateValues = new ArrayList<>(Arrays.asList(lines.remove(0).split("\\s+")));

		// remove extracted empty string/s (if there's any)
		for (int i=0; i<this.stateValues.size(); i++) if (this.stateValues.get(i).isEmpty()) this.stateValues.remove(i);


		// --------------------------------------------------------------------------------------
		/*
		 * 		Possible observable measurement values for each state in the MC
		 * */
		// extract the possible observable measurement values for each state in the MC
		this.observableMeasurementValues = new ArrayList<>(Arrays.asList(lines.remove(0).split("\\s+")));

		// remove extracted empty string/s (if there's any)
		for (int i=0; i<this.observableMeasurementValues.size(); i++) if (this.observableMeasurementValues.get(i).isEmpty()) this.observableMeasurementValues.remove(i);


		// --------------------------------------------------------------------------------------
		/*
		 * 		Pair values for P(E|S) and P(F|S), respectively
		 * */
		// extract the pair values for P(E|S) and P(F|S), respectively
		ArrayList<String> pairGivenS = new ArrayList<>(Arrays.asList(lines.remove(0).split("\\s+")));

		// remove extracted empty string/s (if there's any)
		for (int i=0; i<pairGivenS.size(); i++) if (pairGivenS.get(i).isEmpty()) pairGivenS.remove(i);

		// convert String to double
		this.pEGivenS = Double.parseDouble(pairGivenS.get(0));
		this.pFGivenS = Double.parseDouble(pairGivenS.get(1));

		// --------------------------------------------------------------------------------------
		/*
		 * 		Pair values for P(E|T) and P(F|T), respectively
		 * */
		// extract the pair values for P(E|T) and P(F|T), respectively
		ArrayList<String> pairGivenT = new ArrayList<>(Arrays.asList(lines.remove(0).split("\\s+")));

		// remove extracted empty string/s (if there's any)
		for (int i=0; i<pairGivenT.size(); i++) if (pairGivenT.get(i).isEmpty()) pairGivenT.remove(i);

		// convert String to double
		this.pEGivenT = Double.parseDouble(pairGivenT.get(0));
		this.pFGivenT = Double.parseDouble(pairGivenT.get(1));

		// --------------------------------------------------------------------------------------
		/*
		 * 		Number of cases to be considered for the strings
		 * */
		// extract the number of cases to be considered for the strings
		// also remove whitespaces
		String nCases = lines.remove(0).replaceAll("\\s", "");
		this.nCasesConsidered = Integer.parseInt(nCases);


		// --------------------------------------------------------------------------------------
		/*
		 * 		Cases to be considered for the strings
		 * */
		// extract the cases to be considered for the strings
		this.stringCases = new ArrayList<String>();
		for (int i=0; i!=this.nCasesConsidered; i++) {
			this.stringCases.add(lines.remove(0));
		}

	}


	// ===========================================================================================
	/*
	 *		Predict the probability values of the string cases in each given sequence
	 */
	private void predictProbabilityValues () {
		// storing the extracted possible state values and observable measurement values for each state in the MC
		Sequence.setEFPairProbabilities(this.pEGivenS, this.pFGivenS, this.pEGivenT, this.pFGivenT);

		// storing the extracted sequences into a list (while computing for the probabilities of each hidden state inside the sequence)
		this.sequences = new ArrayList<Sequence>();
		for (String stringSequence : this.stringSequences)
			this.sequences.add(new Sequence(stringSequence, this.stateValues, this.observableMeasurementValues));

		/*
		 * 	getting the solution for the asked string cases / probability statements
		 * */
		this.solution = new ArrayList<ArrayList<StateEstimator>> ();

		// solve for each probability statement for each given sequence
		for (Sequence sequence : this.sequences) {
			ArrayList<StateEstimator> statesEstimator = new ArrayList<StateEstimator>();

			// string cases / probability statements
			for (String stringCase : this.stringCases) {
				StateEstimator newEstimator = new StateEstimator(stringCase, sequence);

				// if there's an error extracting the input file contents properly then inform the user and exit the program
				if (newEstimator.checkValidity() == false) {
					System.out.println("------------------------------------------------");
					System.out.println("SYNTAX ERROR!");
					System.out.println("Please check the contents of your input file.");
					System.out.println("Make sure there's no typographical error");
					System.out.println("and that you're following the correct format.");
					System.out.println("------------------------------------------------");
					System.out.println("\nTerminating the program... Bye!");

					System.exit(0);
				}

				statesEstimator.add(newEstimator);
			}

			this.solution.add(statesEstimator);
		}

		// results
		String outputText = "";
		for (int i=0; i<this.sequences.size(); i++) {
			outputText += this.sequences.get(i).getSequence() + "\n";
			for (StateEstimator estimator : this.solution.get(i)) {
				outputText += estimator.getProbabilityStatement() + " = " + String.format("%.4f", estimator.getProbability()) + "\n";
			}
		}

		// display the results on the console
		System.out.println("==========================================");
		System.out.println("Output:\n");
		System.out.println(outputText);

		// output results into a file
		this.exportResults(outputText.trim());
	}


	// ===========================================================================================
	/*
	 * 		Exporting results into a file <hmm.out>
	 * */
	private void exportResults(String outputText) {
		// decide where to export the results
		String projectDirectory = System.getProperty("user.dir");
		String packageDirectory = "src" + File.separator + "hiddenmarkovmodel";
		String fileName = "hmm.out";
		File file = new File(projectDirectory, packageDirectory + File.separator + fileName);

		// write the contents of outputText into the declared output file
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(outputText);
			writer.close();

			// for checking (console)
			System.out.println("\n==========================================");
			System.out.println("Successfully exported results into a file!");
			System.out.println("Please check hmm.out");
			System.out.println("==========================================");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
