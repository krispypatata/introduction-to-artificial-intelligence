// package declaration
package kmcclassification;

// import statement(s)
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// class definition
public class KMCClassifier {
	// *******************************************************************************************
	// attribute(s)
	private ArrayList<SelectedWineAttributes> trainData;
	private ArrayList<Centroid> centroids;
	private ArrayList<Centroid> previousCentroids;

	private String selectedAttributeX;
	private String selectedAttributeY;
	private int nClusters;

	private String outputText;

	// *******************************************************************************************
	// constructor
	public KMCClassifier(String selectedAttribute1, String selectedAttribute2, int nInputValue) {
		// initialize attribute(s)
		this.trainData = new ArrayList<SelectedWineAttributes>();
		this.centroids = new ArrayList<Centroid>();
		this.previousCentroids = new ArrayList<Centroid>();

		this.selectedAttributeX = selectedAttribute1;
		this.selectedAttributeY = selectedAttribute2;
		this.nClusters = nInputValue;

		// fetch train data and perform the k-means clustering algorithm
		this.readTrainDataFile();
	}


	// *******************************************************************************************
	// method(s)
	// ===========================================================================================
	/*
	 *		Fetch train data from diabetes.csv
	 */
	private void readTrainDataFile () {
		// locate the train data file
		String projectDirectory = System.getProperty("user.dir");
		String packageDirectory = "src" + File.separator + "kmcclassification";
		String fileName = "data_wine.csv";
		File trainDataFile = new File(projectDirectory, packageDirectory + File.separator + fileName);

		String fileContent = "";				// will contain the string contents of the input file

		// read the contents of the train data file
		try {
			Scanner scanner = new Scanner(trainDataFile);

			scanner.nextLine(); // ignore the column names
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

		// format data contents
		this.extractTrainDataContents(fileContent);

	}


	private void extractTrainDataContents(String fileContent) {
		String[] lines = fileContent.split("\n");

		// separate each line's contents by comma
		for (int i=0; i<lines.length; i++) {
			String[] info = lines[i].split(",");

			double[] infoValues = new double[info.length];

			// convert each string element of info into a double
			for (int j=0; j<infoValues.length; j++) {
				infoValues[j] = Double.parseDouble(info[j]);
			}

			// instantiate a DiabetesInfo object given the contents of the extracted train data values as parameters for the constructor
			WineInfo wineInfo = new WineInfo(infoValues[0], infoValues[1], infoValues[2], infoValues[3], infoValues[4], infoValues[5],
					infoValues[6], infoValues[7], infoValues[8], infoValues[9], infoValues[10], infoValues[11], infoValues[12],
					infoValues[13]);

		}

		// for checking
//		System.out.println("\n" + WineInfo.getWineInfoList().size() + " train data points were extracted from data_wine.csv");

		// execute the K-Means Clustering Algorithm
		this.performKMeansClustering();
	}

	private void performKMeansClustering() {
		// example
//		this.selectedAttributeX = WineInfo.ASH_ALCANITY_ATT;
//		this.selectedAttributeY = WineInfo.MALIC_ACID_ATT;
//		this.nClusters = 7;

		int k = this.nClusters;

		/*
		 * 		Start of the computation
		 *
		 * */

		// get the wine attributes the user selected for k-means clustering
		for (WineInfo wineInfo : WineInfo.getWineInfoList()) {
			SelectedWineAttributes trainDatum = new SelectedWineAttributes(wineInfo.getAttribute(selectedAttributeX), wineInfo.getAttribute(selectedAttributeY));

			// add the double-paired attributes in our list of train data
			this.trainData.add(trainDatum);
		}

//		System.out.println(this.trainData.size());

		/*
		 * 		Choosing k (random) number of data points (seed) to be the initial centroids
		 *
		 * */
		// random number generator
		Random randomGenerator = new Random();
		int randomX;
		int randomY;

		// get random x and y data points
		// instantiate a new Centroid object and add it to our ArrayList of centroids
		for (int i=0; i<k; i++) {
			randomX = randomGenerator.nextInt(this.trainData.size());
			randomY = randomGenerator.nextInt(this.trainData.size());

			Centroid newCentroid = new Centroid(this.trainData.get(randomX).getX(), this.trainData.get(randomY).getY(), i);
			this.centroids.add(newCentroid);
		}

		// for checking
//		for (Centroid centroid : this.centroids) {
//			System.out.println("Centroid " + centroid.getClusterNumber() + ": ("+ centroid.getX() + ", " + centroid.getY() + ")");
//		}


		/*
		 * 		Computation of distances, classification of data points, and centroids update
		 *
		 * */

		boolean notConverging = true;
		int iterationsWithAllEqualCentroids = 0;

		int iterationsCounter = 0;
		while (notConverging) {
			// have a backup of the current centroids
			this.previousCentroids.clear();
			for (Centroid centroid : this.centroids) {
				previousCentroids.add(new Centroid(centroid));
			}

			/*
			 * 		Assign each data point to the closest centroid
			 *
			 * */
			// compute for the distances of each data point from the centroids
			for (SelectedWineAttributes xyDataPair : this.trainData) {
				Double distance;
				Double x1 = xyDataPair.getX();
				Double y1 = xyDataPair.getY();
				for (Centroid centroid : this.centroids) {
					Double x2 = centroid.getX();
					Double y2 = centroid.getY();

					// Euclidean distance measure
					// d = sqrt[ (x2-x1)^2 + (y2-y1)^2 ]
					distance = Math.sqrt( (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) );

					xyDataPair.getDistances().add(distance);
				}
			}

			// classify each xy data point based on its computed distances from the given centroids
			// the shortest distance will determine the classification of the xy data point
			for (SelectedWineAttributes xyDataPair : this.trainData) {
				int classification = 0;
				double shortestDistance = xyDataPair.getDistances().get(0);
				for (int index=1; index<xyDataPair.getDistances().size(); index++) {
					if (xyDataPair.getDistances().get(index) < shortestDistance) {
						classification = index;
						shortestDistance = xyDataPair.getDistances().get(index);
					}

				}

				// update the data point classification
				xyDataPair.setClassification(classification);

				// reset distances for the next iteration of the algorithm
				xyDataPair.getDistances().clear();
			}

			/*
			 * 		Re-compute the centroids using the current cluster memberships
			 *
			 * */
			// arranging classified data points
			for (Centroid centroid : this.centroids) {
				centroid.getClassifiedList().clear();
				for (SelectedWineAttributes xyDataPair : this.trainData) {
					if (xyDataPair.getClassification() == centroid.getClusterNumber())
						centroid.getClassifiedList().add(xyDataPair);
				}
			}


			// compute for the new values of the centroids
			for (int index=0; index<this.centroids.size(); index++) {
				Centroid centroid = this.centroids.get(index);
				Double sumX = (double) 0;
				Double sumY = (double) 0;
				Double meanX = (double) 0;
				Double meanY = (double) 0;
				double n = (double) centroid.getClassifiedList().size();

				// compute for the sum of x values and y values
				for (SelectedWineAttributes xyDataPair : centroid.getClassifiedList()) {
					sumX += xyDataPair.getX();
					sumY += xyDataPair.getY();
				}

				// compute for the mean of x values and mean of y values
				meanX = sumX / n;
				meanY = sumY / n;

				// update the centroids value
				this.centroids.get(index).setX(meanX);
				this.centroids.get(index).setY(meanY);
			}

			// check if the values of the current centroids are equal to the values of the previous centroids
			double epsilon = 1e-6;		// tolerable error
			int nEqualCentroids = 0;
			for (int index=0; index<this.centroids.size(); index ++) {
				boolean hasEqualValues = true;
				if (
						(Math.abs(this.centroids.get(index).getX() - this.previousCentroids.get(index).getX()) < epsilon )
						&& (Math.abs(this.centroids.get(index).getY() - this.previousCentroids.get(index).getY()) < epsilon )
					)
				{
					// do nothing

				} else {
					hasEqualValues = false;
				}

				// update the number of equal centroids
				if (hasEqualValues)
					nEqualCentroids ++;
			}

			// if all items on the current centroid list and the previous centroid list are equal, then update the number of iterations with equal centroids
			if (nEqualCentroids == this.centroids.size()) {
				iterationsWithAllEqualCentroids += 1;
			}

			// must have at least 2 iterations with no change in centroid
			if (iterationsWithAllEqualCentroids == 2) {
				notConverging = false;
			}

			iterationsCounter ++;


		} // end of while loop

//		System.out.println("ITERATIONS: " + iterationsCounter);

		this.outputText = "";
		String outputForCSV = "";
		for (Centroid centroid : this.centroids) {
			this.outputText += "Centroid " + centroid.getClusterNumber() + ": ("+ centroid.getX() + ", " + centroid.getY() + ")\n";
			outputForCSV += "Centroid " + centroid.getClusterNumber() + "\n"+ centroid.getX() + ", " + centroid.getY() + "\n";
			for (SelectedWineAttributes xyDataPair : centroid.getClassifiedList()) {
				this.outputText += "[" + xyDataPair.getX() + ", " + xyDataPair.getY() + "]\n";
				outputForCSV += xyDataPair.getX() + ", " + xyDataPair.getY() + "\n";
			}

			this.outputText += "\n";
			outputForCSV += "\n";
		}

		System.out.println(this.outputText);

		// export the results into a csv file
		this.exportResults(outputForCSV);
	}


	// ===========================================================================================
	/*
	 * 		Exporting the results of the classification of the input data (using the KNN algorithm) into a text file
	 */
	private void exportResults(String outputText) {
		// decide where to export the results
		String projectDirectory = System.getProperty("user.dir");
		String packageDirectory = "src" + File.separator + "kmcclassification";
		String fileName = "output.csv";
		File file = new File(projectDirectory, packageDirectory + File.separator + fileName);

		// write the contents of outputText into the declared output file
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(outputText);
			writer.close();

			// for checking (console)
			System.out.println("Successfully exported results into a file!");
			System.out.println("Please check output.csv");
			System.out.println("\n=====================================");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// ===========================================================================================
	/*
	 * 		Reset/clear the values/contents of some attributes (for the next program's iteration)
	 *
	 * */
	public void cleanup() {
	    this.trainData.clear();

	    for (Centroid centroid : this.centroids)
	        centroid.getClassifiedList().clear();

	    for (Centroid centroid : this.previousCentroids)
	        centroid.getClassifiedList().clear();

	    this.centroids.clear();
	    this.previousCentroids.clear();
	    WineInfo.getWineInfoList().clear();

	    // release memory
	    this.trainData = null;
	    this.centroids = null;
	    this.previousCentroids = null;
	    WineInfo.releaseWineInfoList();
	}


	// ===========================================================================================
	/*
	 * 		Getter(s)
	 *
	 * */
	public String getOutputText () {
		return this.outputText;
	}

	public ArrayList<Centroid> getCentroids() {
		return this.centroids;
	}
}

