// package declaration
package knnclassification;


//import statement(s)
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


// class definition
public class KNNClassifier {
	// *******************************************************************************************
	// attribute(s)
	private ArrayList<DiabetesInfo> trainData;
	private ArrayList<DiabetesInfo> inputData;
	private Map<Double, DiabetesInfo> kNearestNeighbors; // sorted dictionary that will hold the distances between a certain input info and the info in our train data
	private int k;


	// *******************************************************************************************
	// constructor(s)
	public KNNClassifier () {
		// initializing objects
		this.trainData = new ArrayList<DiabetesInfo>();
		this.inputData = new ArrayList<DiabetesInfo>();
		this.kNearestNeighbors = new TreeMap<>();

		this.showPrompt();


		// reading train data and input data from a file
//		this.readTrainDataFile();
//		this.readInputFile();
	}


	// *******************************************************************************************
	// method(s)

	// ===========================================================================================
	/*
	 *		simple prompt (for asking a value for k)
	 */
	private void showPrompt() {
        Scanner scanner = new Scanner(System.in);
        int enteredInput;
        boolean isRunning = true;

        // ask for a valid integer input
        while (isRunning) {
            try {
                System.out.print("Enter a value of k [0 to exit]: ");

                // ask an integer input from the user
                enteredInput = scanner.nextInt();


                if (enteredInput == 0) {
                	System.out.println("\n=====================================");
                	System.out.println("Thank you for using our program. Bye!");
                	System.out.println("=====================================");
                	isRunning = false;
                }
                else if (enteredInput < 0) {
                	System.out.println("\n------------------------------------");
                    System.out.println("Invalid Input!");
                    System.out.println("Please enter a positive integer only.");
                    System.out.println("------------------------------------\n");
            		continue;
            	}
                else {
                	// update the value of k
                	this.k = enteredInput;

                	// for checking
                	System.out.println("\n=====================================");
                    System.out.println("Value of k: " + this.k);

                    // reading train data and input data from a file
                    this.readTrainDataFile();
                    this.readInputFile();
                }

            } catch (InputMismatchException e) {
            	System.out.println("\n------------------------------------");
                System.out.println("Invalid Input!");
                System.out.println("Please enter an integer input only.");
                System.out.println("------------------------------------\n");

                // fetch what was left in the input stream (to avoid infinite loop)
                scanner.nextLine();
            }
        }

        // Close the scanner
        scanner.close();
    }


	// ===========================================================================================
	/*
	 *		Fetch train data from diabetes.csv
	 */
	private void readTrainDataFile () {
		// locate the diabetes.csv file
		String filePath = System.getProperty("user.dir");
		filePath += "\\src\\knnclassification\\diabetes.csv";

		File trainDataFile = new File(filePath);
		String fileContent = "";				// will contain the string contents of the input file

		// read the contents of the input.txt file
		try {
			Scanner scanner = new Scanner(trainDataFile);

			// Set the delimiter to a comma.
			scanner.useDelimiter(",");

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

//		System.out.println(fileContent);

		this.extractTrainDataContents(fileContent);

	}

	// ===========================================================================================
	/*
	 *		Extracting train data properly and storing them locally (here in this program)
	 */
	private void extractTrainDataContents(String fileContent) {
		String[] lines = fileContent.split("\n");

//		System.out.println(lines.length);

		// separate each line's contents by comma
		for (int i=0; i<lines.length; i++) {
			String[] info = lines[i].split(",");

			double[] infoValues = new double[info.length];

			// convert each string element of info into a double
			for (int j=0; j<infoValues.length; j++) {
				infoValues[j] = Double.parseDouble(info[j]);
			}

			// instantiate a DiabetesInfo object given the contents of the extracted train data values as parameters for the constructor
			DiabetesInfo diabetesInfo = new DiabetesInfo(infoValues[0], infoValues[1], infoValues[2], infoValues[3], infoValues[4], infoValues[5], infoValues[6], infoValues[7], (int) infoValues[8]);

			// add the created object into our ArrayList of input data
			this.trainData.add(diabetesInfo);
		}

		// for checking
		System.out.println("\n" + this.trainData.size() + " train data points were extracted from diabetes.csv");

	}


	// ===========================================================================================
	/*
	 *		Fetch data from input.in
	 */
	private void readInputFile () {
		// locate the input.in file
		String filePath = System.getProperty("user.dir");
		filePath += "\\src\\knnclassification\\input.in";

		File inputFile = new File(filePath);
		String fileContent = "";				// will contain the string contents of the input file

		// read the contents of the input.txt file
		try {
			Scanner scanner = new Scanner(inputFile);

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

		// for checking
//		System.out.println(fileContent);

		// extract the data from the input file's string contents
		this.extractInputContents(fileContent);
	}


	// ===========================================================================================
	/*
	 *		Extracting input data
	 */
	private void extractInputContents(String fileContent) {
		String[] lines = fileContent.split("\n");

		// separate each line's contents by comma
		for (int i=0; i<lines.length; i++) {
			String[] info = lines[i].split(",");

			double[] infoValues = new double[info.length];

			// convert each string element of info into a double
			for (int j=0; j<infoValues.length; j++) {
				infoValues[j] = Double.parseDouble(info[j]);
			}

			// instantiate a DiabetesInfo object given the contents of the extracted input values as parameters for the object's constructor
			DiabetesInfo diabetesInfo = new DiabetesInfo(infoValues[0], infoValues[1], infoValues[2], infoValues[3], infoValues[4], infoValues[5], infoValues[6], infoValues[7], 0);

			// add the created object into our ArrayList of input data
			this.inputData.add(diabetesInfo);
		}

		// for checking
		System.out.println(this.inputData.size() + " input data points were extracted from input.in");
		System.out.println("\nExtracted input data:");
		for (DiabetesInfo info : this.inputData) {
			info.viewInfo(false);
		}

		// classify the new feature vectors using the k-nearest neighbors algorithm (KNN)
		this.performKNNAlgorithm();
	}


	// ===========================================================================================
	/*
	 *		performing the knn algorithm to classify the new input data
	 *
	 *		Given k and a feature vector, x (inputInfo):
	 *			-> For each feature vector, v, in the training data set:
	 *				Compute the distance from x to v using Euclidean distance
	 *					d = sqrt [ Summation(x_i - v_i ) from i to n]
	 *
	 */
	private void performKNNAlgorithm() {
		// for output file
		String outputText = "";

		// for checking (console)
		System.out.println("\nClassifying the new feature vectors using the k-nearest neighbors (KNN) Algorithm...");
		System.out.println("Results:");
		System.out.println("k = " + this.k);


		// for checking (distance computation)
//		 System.out.println("Start");
//		 int input1 = 0;

		// loop through our input data
		for (DiabetesInfo inputInfo: this.inputData) {

			// loop through the contents of our train data
			// compute for the eucledian distance between the given input datum and each datum in the train data
			for (DiabetesInfo trainDataInfo: this.trainData) {
				double distance = 0;

				double numPregnanciesDifference = inputInfo.getNumPregnancies() - trainDataInfo.getNumPregnancies();
				distance += numPregnanciesDifference*numPregnanciesDifference;

				double glucoseValueDifference = inputInfo.getGlucoseValue() - trainDataInfo.getGlucoseValue();
				distance += glucoseValueDifference*glucoseValueDifference;

				double bloodPressureDifference = inputInfo.getBloodPressure() - trainDataInfo.getBloodPressure();
				distance += bloodPressureDifference*bloodPressureDifference;

				double skinThicknessDifference = inputInfo.getSkinThickness() - trainDataInfo.getSkinThickness();
				distance += skinThicknessDifference*skinThicknessDifference;

				double insulinValueDifference = inputInfo.getInsulinValue() - trainDataInfo.getInsulinValue();
				distance += insulinValueDifference*insulinValueDifference;

				double bmiDifference = inputInfo.getBmi() - trainDataInfo.getBmi();
				distance += bmiDifference*bmiDifference;

				double pedigreeFunctionDifference = inputInfo.getPedigreeFunction() - trainDataInfo.getPedigreeFunction();
				distance += pedigreeFunctionDifference*pedigreeFunctionDifference;

				double ageDifference = inputInfo.getAge() - trainDataInfo.getAge();
				distance += ageDifference*ageDifference;

				distance = Math.sqrt(distance);

				// for checking (distance computation)
//				 System.out.println(distance);

				// store the computed distance (along its the train data info) into our sorted dictionary/treemap
				this.kNearestNeighbors.put(distance, trainDataInfo);
			}

			// for checking (to prove that the kNearestNeighbors contents are already sorted
//			for (Double distance : this.kNearestNeighbors.keySet()) {
//				System.out.println(distance);
//			}

			// counters necessary for classification of input data
			int nonDiabeticCounter = 0;
			int diabeticCounter = 0;
			int infoCounter = 0;

			// get the first k items in our tree map
			// these items represent the k-nearest neighbors of the selected input datum
			for (DiabetesInfo info: this.kNearestNeighbors.values()) {

				// increment our counter for the outcomes
				if (info.getOutcome() == 0) nonDiabeticCounter+=1;
				else diabeticCounter+=1;

				if (++infoCounter == this.k) break;
			}

			// for checking
			System.out.print("[ Neighbors: " + this.trainData.size());
			System.out.print("| Non-diabetic: " + nonDiabeticCounter);
			System.out.print("| Diabetic: " + diabeticCounter + " ]\t");

			// update the classification attribute of the selected input datum
			if (diabeticCounter > nonDiabeticCounter) inputInfo.setOutcome(1);
			else inputInfo.setOutcome(0);

			// clear the contents of our sorted dictionary (so that the next input datum can use it)
			this.kNearestNeighbors.clear();

			// add the newly classified input datum to our train data
			// to satisfy one of the program's requirements (newly-classified input data should be considered for the classification of the rest of the input data)
			this.trainData.add(inputInfo);

			// for checking
//			System.out.println(this.trainData.size());

			// updating the output text contents
			outputText += inputInfo.getStringEquivalent() + "\n";

			// for checking
			inputInfo.viewInfo(true);

			// for checking (distance computation)
//			 if (++input1 == 1) break;
		}

		// remove the new line character at the end of the string
		outputText = outputText.substring(0, outputText.length() - 1);

		// call the method for exporting the output data into a file
		this.exportResults(outputText);

	}


	// ===========================================================================================
	/*
	 * 		Exporting the results of the classification of the input data (using the KNN algorithm) into a text file
	 */
	private void exportResults(String outputText) {
		// decide where to export the results
		String filePath = System.getProperty("user.dir");
		filePath += "\\src\\knnclassification\\output.txt";
		File file = new File(filePath);

		// write the contents of outputText into the declared output file
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(outputText);
			writer.close();

			// for checking (console)
			System.out.println("\nSuccessfully exported results into a file!");
			System.out.println("Please check output.txt");
			System.out.println("\n=====================================");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// reset/clear the values/contents of some attributes (for the next program's iteration)
		this.trainData.clear();
		this.inputData.clear();
		this.kNearestNeighbors.clear();

		// for checking
//		System.out.println(this.trainData.size());
//		System.out.println(this.inputData.size());

	}

}
