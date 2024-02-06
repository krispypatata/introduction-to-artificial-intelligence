/************************************************************************************
 * CMSC 170: Introduction to Object-Oriented Programming
 * Lab Topic 09: Classification using K-Means Clustering
 *
 * Program Description:
 * 	This is a simple program that implements the k-means clustering algorithm in
 * grouping a set of data points accordingly based on some centroids.
 *
 * (c) Institute of Computer Science, CAS, UPLB
 *
 * @author 	Keith Ginoel S. Gabinete
 * @date 	2023-11-15 23:44
*************************************************************************************/

// package declaration
package main;

//import statement(s)
import javafx.application.Application;
import javafx.stage.Stage;
import kmcclassification.GUI;

// class definition
public class Main extends Application {
	public static void main (String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		@SuppressWarnings("unused")
		// instantiate a new screen GUI
		GUI window = new GUI(stage);
	}
}
