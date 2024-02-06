// package declaration
package linearclassification;

// import statement(s)
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.util.Map.Entry;

// class definition
public class Perceptron {
	// *******************************************************************************************
	// attribute(s)
	private double learningRate;	// learning rate
	private double threshold;	// threshold
	private double bias;	// bias
	private ArrayList<ArrayList<Double>> trainDataList;
	// *******************************************************************************************
	// constructor(s)
	public Perceptron() {
		this.readInputFile();
	}

	// *******************************************************************************************
	// method(s)
	// ===========================================================================================
	// read input file
	private void readInputFile () {
		// locate the input.txt file
		String filePath = System.getProperty("user.dir");
		filePath += "\\src\\linearclassification\\input.txt";

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

		// extract the data from the input file's string contents and store them in appropriate variables
		this.extractContents(fileContent);
	}

	// ===========================================================================================
	// extract data from the input file
	private void extractContents (String fileContent) {
		String[] lines = fileContent.split("\n");

		// extract learning rate, threshold and bias
		String rStr = lines[0];
		String tStr = lines[1];
		String bStr = lines[2];

		this.learningRate = Double.parseDouble(rStr);
		this.threshold = Double.parseDouble(tStr);
		this.bias = Double.parseDouble(bStr);

		// extract the train data
		String trainStr = "";
		for (int i=3; i<lines.length; i++) {
			trainStr += lines[i] + "\n";
		}

		String[] trainData = trainStr.split("\n");
		// trainData array now contains row lines

		// separate each row's contents by whitespaces
		ArrayList<String[]> intialTrainDataList = new ArrayList<String[]>();
		for (int i=0; i<trainData.length; i++) {
			String[] row = trainData[i].split("\\s+");
			intialTrainDataList.add(row);
		}

		// parse or convert string elements of row arrays into double
		this.trainDataList = new ArrayList<ArrayList<Double>>();
		for (String[] row: intialTrainDataList) {
			ArrayList<Double> rowList = new ArrayList<Double>();
			for (int i=0; i<row.length; i++) {
				rowList.add(Double.parseDouble(row[i]));
			}
			this.trainDataList.add(rowList);
		}

		// execute the algorithm for linear classification using perceptron
		this.performAlgorithm();
	}


	// ===========================================================================================
	// perform computations
	private void performAlgorithm() {
		// for code readability when performing calculations
		Double r = this.learningRate;
		Double t = this.threshold;
		Double b = this.bias;

		// counters
		// number of x variables (just equal to the size of an arrayList inside the trainDataList);
		int n = this.trainDataList.get(0).size()-1;

		// table dimensions for the results of the computation
		// rows is just equal to the number of rows in fetched train data
		int nRow = this.trainDataList.size();

		// number of columns = ( n [number of variables x] + 1 [column for bias] )*2 [for w_0,...w_n, w_b)  + 3 (columns for a, y and z)
		int nCol = (n+1)*2 + 3;

		// ===========================================================================================
		// Initializing the table
		// ===========================================================================================
		// create a table with dimensions nRow X nCol
		ArrayList<Vector<Double>> initialTable = new ArrayList<Vector<Double>>();

		// represent rows of the table as vectors
		// each vector should be of size nCol
		for (int i=0; i<nRow; i++) {
			Vector<Double> row = new Vector<Double>(nCol);
			for (int j = 0; j < nCol; j++) row.add(0.0);					// initialize the values of the elements of the vector to 0

			// plugin some values/data we fetched earlier
			ArrayList<Double> trainDataRow = this.trainDataList.get(i);

			// adding the values of the variables x
			for (int j=0; j<n; j++) {
				row.set(j, trainDataRow.get(j));
			}

			// adding the value of the bias at column n (after the column for x_n)
			row.set(n, b);

			// adding the value of z at the last column
			row.set(row.size()-1, trainDataRow.get(n));

			// add the created vector into our table
			initialTable.add(row);
		}

		// column names
		Vector<String> colNames = new Vector<String>(nCol);
		for (int i=0; i<n; i++) {
			colNames.add("x"+i);
		}
		colNames.add("b");

		for (int i=0; i<n; i++) {
			colNames.add("w"+i);
		}
		colNames.add("wb");
		colNames.add("a");
		colNames.add("y");
		colNames.add("z");


		// for checking
		System.out.println("Learning rate: " + r);
		System.out.println("Threshold: " + t);
		System.out.println("Bias: " + b);
		System.out.println("INITIAL TABLE");
		for (Vector<Double> row: initialTable) {
			System.out.println(row);
		}
		System.out.println();

		// ===========================================================================================
		// Performing actual computations on the given table
		// ===========================================================================================
		ArrayList<Vector<Double>> oldTable = new ArrayList<>(initialTable);
		ArrayList<Vector<Double>> table = new ArrayList<>(initialTable);

		// positions/columns of data variables
		int xPos = 0;
		int bPos = n;
		int wPos = bPos+1;
		int zPos = table.get(0).size()-1;
		int yPos = zPos-1;
		int aPos = yPos-1;
		DecimalFormat df = new DecimalFormat("0.0");

		// For displaying/exporting results/output
		String outputText = "";

		boolean hasConvergence = false;
		int iteration = 1;
		while (hasConvergence == false) {
			// loop through the rows in the table
			for (int i=0; i<nRow; i++) {
				Vector<Double> row = table.get(i);

				// if first row, then don't modify the weights
				// just modify a and y
				if (i==0) {

					if (iteration!=1) {
						// replace weights of the table with the values of weights from the extra row in the old table
						Vector<Double> previousRow = oldTable.get(nRow-1);

						for (int pos=wPos; pos<aPos; pos++) {
							// get the weights in the extra row
							Double w_a = 0.0;
							w_a = previousRow.get(pos) + r*previousRow.get(pos-wPos)*(previousRow.get(zPos)-previousRow.get(yPos));
							row.set(pos, w_a);	// replace the first row weights with them
						}
					}


				} else {
					Vector<Double> previousRow = table.get(i-1);

					// adjust weights of the table
					for (int pos=wPos; pos<aPos; pos++) {

						// w_a = w_c + r*x_p*(z-y)
						Double w_a = 0.0;
						w_a = previousRow.get(pos) + r*previousRow.get(pos-wPos)*(previousRow.get(zPos)-previousRow.get(yPos));
						row.set(pos, w_a);

						// for checking
						// System.out.println("w_a = "+ previousRow.get(pos) + "*" + previousRow.get(pos-(n+1)) + "*" + "(" + previousRow.get(zPos) + "-" + previousRow.get(yPos)+")");
					}
				}

				// compute for a
				// a = sum (xiwi) + bwb
				Double a = 0.0;
				for (int pos=xPos; pos<wPos; pos++) {
					a += row.get(pos)*row.get(pos+(n+1));
				}
				row.set(aPos, a);

				// compute for y
				// y = (a>=threshold)?1:0
				Double y  = 0.0;
				y = (double) ((a>=t)?1:0);
				row.set(yPos, y);


				// fix formatting of numbers in the row
				for (int j =0; j<nCol; j++) {
					row.set(j, Double.parseDouble(  df.format(row.get(j)) ) );
				}
			} // end of for loop

			// clone the newly modified table to oldTable
			oldTable = new ArrayList<>(table);


			// for checking and modifying the output file
			outputText += "Iteration " + iteration + ":\n";
			System.out.println("Iteration " + iteration + ":");

			// display column names
			for (String colName: colNames) {
				outputText += "\t" + colName;
				System.out.print("\t" + colName);
			}
			outputText += "\n";
			System.out.println();

			// loop through the contents of the table
			for (Vector<Double> row: table) {
				for (int i=0; i<row.size(); i++) {
					double value = row.get(i);
					if (i<n || i>aPos) {
						outputText += "\t" + (int)value;
						System.out.print("\t" + (int)value);
					} else {
						outputText += "\t" + value;
						System.out.print("\t" + row.get(i));
					}

				}
				// append new line at the end of each row
				outputText += "\n";
				System.out.println();
			}
			// append new line at the end of each iteration
			System.out.println();
			outputText += "\n";

			iteration += 1;														// update the value of iteration count
			hasConvergence = this.checkWeightsConvergence(table, wPos, aPos);	// check if the adjusted weights converge

//			if (iteration == 4) break;
			// assume that the weights are non-converging if the algorithm still runs after 1 000 iterations
			if (iteration > 1000) {
				System.out.println("NON-CONVERGING");
				outputText += "NON-CONVERGING";
				break;
			}
		}

		// export the results of the computation into a file
		this.exportResults(outputText);

	}


	// ===========================================================================================
	// for checking of weights convergence
	private boolean checkWeightsConvergence(ArrayList<Vector<Double>> table, int wPos, int aPos) {
		boolean hasConvergence = false;
		boolean assumption = true;		// assume that there's convergence on all weights

		// loop throught the rows in the table
		for (int i=0; i<(table.size()-1); i++) {
			Vector<Double> row = table.get(i);
			Vector<Double> nextRow = table.get(i+1);

			// check if each weight on the current row is equal to its corresponding weight on the next row
			// double values can't be normally compared with '==' symbol
			// reference: <https://stackoverflow.com/questions/8081827/how-to-compare-two-double-values-in-java>
			for (int pos=wPos; pos<aPos; pos++) {
				// check if (tables' weights are still 0)
				if ( (Double.compare(row.get(pos), 0) ==0)   && (nextRow.get(pos) == 0) ) {
					assumption = false;
					continue;
				}

				// if ( Math.abs(row.get(pos) - nextRow.get(pos)) > 1e-10) // this is also a valid condition for checking equality of doubles
				if ( Double.compare(row.get(pos), nextRow.get(pos)) != 0) {
					assumption = false;					// weights do not converge
				}
			}
		}

		// if the initial assumption remains true, then there's a convergence in weights
		if (assumption == true) {
			hasConvergence = true;
		}

		// return the results
		return hasConvergence;
	}


	// ===========================================================================================
	// export results into a file
	private void exportResults(String outputText) {
		// decide where to export the results
		String filePath = System.getProperty("user.dir");
		filePath += "\\src\\linearclassification\\output.txt";
		File file = new File(filePath);

		// write the contents outputText into the output file
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(outputText);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
