/************************************************************************************
 * CMSC 170: Introduction to Object-Oriented Programming
 * Laboratory Week 4 Exercise
 * Topic: Representing Text as Bags-of-Words
 *
 * Program Description:
 * 	This is a simple program that reads an input file and produce a bag-of-words
 * representation for the text in the input file.
 *
 * (c) Institute of Computer Science, CAS, UPLB
 *
 * @author 	Keith Ginoel S. Gabinete
 * @date 	2023-02-10 10:29
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
