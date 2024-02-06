/************************************************************************************
 * CMSC 170: Introduction to Object-Oriented Programming
 * Laboratory Week 5 Exercise
 * Topic: Spam Filtering Using A Naïve Bayes Classifier
 *
 * Program Description:
 * 	This is a simple program that implements the concept of Naive Bayes Classifier in filtering spam data.
 *
 * (c) Institute of Computer Science, CAS, UPLB
 *
 * @author 	Keith Ginoel S. Gabinete
 * @date 	2023-10-10 22:36
*************************************************************************************/
// package declaration
package main;

// import statement(s)
import bow.BOW;
import javafx.application.Application;
import javafx.stage.Stage;

// class definition
public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		@SuppressWarnings("unused")
		BOW bagOfWords = new BOW(stage);
	}
}
