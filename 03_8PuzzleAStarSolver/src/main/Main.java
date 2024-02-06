/************************************************************************************
 * CMSC 170: Introduction to Object-Oriented Programming
 * Laboratory Week 3 Exercise: Solving 8-puzzle using A* Search
 * Topic: Solving 8-puzzle using A* Search
 *
 * Program Description:
 * 	This is a simple 8-puzzle game.
 *
 * 	Game Background:
 * 		8-puzzle is a slight modification of the more known 15-puzzle game.
 * 	This game is played on a 3x3 grid with each tile containing values from 0-8.
 * 	The goal of the game is to come up with board configuration wherein the tiles
 * 	are ordered with values in the right succession (ie. ascending, from Left-Right
 *	and Up-Down).
 *
 *	Game Mechanics:
 *	- The puzzle is a 3x3 board, where each cell contains values from 0-8, with no repetition.
 *	- The 0-valued tile is also interpreted as an empty cell.
 *	- A non-zero tile next to an empty cell can be moved to the location of the empty cell,
 *	thereby leaving its previous position empty or 0-valued in turn.
 *	- The board contains shuffled 0-8 values.
 *	- The mouse pointer is used to move valid cells one at a time.- Valid cells are defined as
 *	cells adjacent to the 0-valued tile.
 *	- The player continues to move cells until the tiles are ordered in the right succession.
 *	- The player must tick the minimum number of tiles to achieve an optimal solution.
 *
 * (c) Institute of Computer Science, CAS, UPLB
 *
 * @author 	Keith Ginoel S. Gabinete
 * @date 	2023-09-019 12:26
*************************************************************************************/
// package declaration
package main;

// import statement(s)
import javafx.application.Application;
import javafx.stage.Stage;
import puzzlegame.GameStage;

// class definition
public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		@SuppressWarnings("unused")
		GameStage gameStage = new GameStage(stage);
	}
}
