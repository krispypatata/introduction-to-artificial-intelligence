// package declaration
package puzzlegame;

// import statement(s)
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

// class definition
public class GameStage {
	// *******************************************************************************************
	// attribute(s)
	private Scene scene;
	private Stage stage;

	private Group root;
	private Canvas canvas;
	private GridPane gridPane;

	public final static int WINDOW_WIDTH = 400;
	public final static int WINDOW_HEIGHT = 500;
	public final static int GAMEBOARD_TOP_PADDING = 75;

	// gameboard is 3x3
	private final static int TILE_ROWS = 3;
	private final static int TILE_COLUMNS = 3;
	private final static int TILE_GAP = 20;

	private boolean isSolvable;
	private boolean isGameOver = false;

	// for the gameBoard
	private ArrayList<Integer> puzzleInput;
	private int[][] gameBoard;


	// *******************************************************************************************
	// constructor(s)
	public GameStage (Stage stage) {
		this.stage = stage;
		this.root = new Group();

		// setting the game board
		this.setInItGameElements();

		// setting the scene
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, Color.LIGHTBLUE);

		this.stage.getIcons().add(new Image("numbers/8.png"));
		this.stage.setTitle("8-Puzzle Game");
		this.stage.setResizable(false);

		this.stage.setScene(this.scene);
		this.stage.show();
	}


	// *******************************************************************************************
	// method(s)
	// Adds the Canvas and the GridPane to our root node
	private void setInItGameElements() {

		// set background for the canvas
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);

		// add canvas to our root (child 1)
		this.root.getChildren().add(this.canvas);

		// add the Grid Pane
		this.gridPane = new GridPane();

		// set up the game board
		this.setInItTiles();

		// add the gridPane to our root (child 2)
		this.root.getChildren().add(this.gridPane);
	}

	// ===========================================================================================
	// Sets up the number tiles in the game board
	private void setInItTiles() {
		// read inputs from a file
		this.readInputFile();

		// check if the read input is solvable
		this.isSolvable = this.checkSolvability();

		// transform the ArrayList (containing the fetched inputs) into a 2d-array
		this.createTileBasis();

		// check the game state
		this.checkGameState();

		// updates UI
		this.addTilesToGrid();
		this.setGridLayoutProperties();
	}

	// ===========================================================================================
	// Reads input from a file
	private void readInputFile() {
		// load the file from the current project directory
		// reference: reading file from the current directory <https://stackoverflow.com/questions/1480398/java-reading-a-file-from-current-directory>
		String fileDirectory = System.getProperty("user.dir");
		fileDirectory += "\\src\\puzzlegame\\" + "puzzle.in";
		File file = new File(fileDirectory);

		// Will store the contents of the file
		String fileContent = "";

		// will store the fetched integer inputs from the file
		this.puzzleInput = new ArrayList<Integer>();

		// reading from file contents with a Scanner object <https://www.youtube.com/watch?v=lHFlAYaNfdo&t=265s>
		// Use the Scanner class to read each line in the file
		try {
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				fileContent += scanner.nextLine();
			}

			// loop through the elements of fileContent and check whether the selected element is a digit or not
			// if it is a digit, typecast it into an integer and store it in our ArrayList of integers
			for (int index=0; index<fileContent.length(); index++) {
				char c = fileContent.charAt(index);
				if (Character.isDigit(c)) this.puzzleInput.add((int) (c - '0'));
			}

			// close the scanner
			scanner.close();

		} catch (FileNotFoundException e) {
			// catch block
			e.printStackTrace();
		}
	}

	// ===========================================================================================
	// creates a 2d array representation of the puzzle game
	private void createTileBasis() {
		// initializing the gameBoard object
		this.gameBoard = new int[GameStage.TILE_ROWS][GameStage.TILE_COLUMNS];

		// setting up number tiles' values (based on the ArrayList containing the read inputs)
		int index = 0;
		for (int row=0; row<3; row++) {
			for (int col=0; col<3; col++) {
				gameBoard[row][col] = this.puzzleInput.get(index);
				index += 1;
			}
		}
	}

	// ===========================================================================================
	// Adds number tiles to our Grid Pane
	private void addTilesToGrid() {
		// Adding the number tiles into our Grid Pane (from left-to-right & up-to-down)
		for (int row=0; row<3; row++) {
			for (int col=0; col<3; col++) {
				Tile number = new Tile(this, gameBoard[row][col]);
				GridPane.setConstraints(number.getImgView(), col, row);
				this.gridPane.getChildren().add(number.getImgView());
			}
		}
	}

	// ===========================================================================================
	// formats the gridPane
	private void setGridLayoutProperties() {
		this.gridPane.setPrefSize(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		this.gridPane.setPadding(new Insets(GameStage.GAMEBOARD_TOP_PADDING, 0, 0, 0));
		this.gridPane.setHgap(GameStage.TILE_GAP);
		this.gridPane.setVgap(GameStage.TILE_GAP);
		this.gridPane.setAlignment(Pos.CENTER);
	}

	// ===========================================================================================
	// checks the if puzzle game is solvable or not (using the Inversion Count Theory)
	// references:
	// <https://www.youtube.com/watch?v=bhmCmbj9VAg&t=19s>
	// <https://www.geeksforgeeks.org/check-instance-8-puzzle-solvable/>
	private boolean checkSolvability() {
		int inversionCount = 0;

		// loop through the contents of the ArrayList holding our puzzle integer inputs
		// check whether each integer input precedes a number with lower value than them
		// increment the inversionCount if the above statement is true
		for (int i=0; i<this.puzzleInput.size(); i++) {
			int currentNumber = this.puzzleInput.get(i);				// integer input

			for (int j=i; j<this.puzzleInput.size(); j++){
				int precededNumber = this.puzzleInput.get(j);
				if (currentNumber > precededNumber && precededNumber!=0) inversionCount++; 	// updates the inversion counter
			}
		}

		// if the total inversion count is even, then the puzzle is solvable; otherwise, it isn't solvable
		if (inversionCount%2 ==0) return true;
		else return false;
	}

	// ===========================================================================================
	// gets the position (x,y) of the number/tile in the gameBoard
	int[] getPosition(int value) {
		int posX = 0, posY = 0;

		for (int i=0; i<GameStage.TILE_ROWS; i++) {
			for (int j=0; j<GameStage.TILE_COLUMNS; j++) {
				if (this.gameBoard[i][j] == value) {
					posX = i;
					posY = j;
				}
			}
		}

		// return the x and y coordinates of the number in the game board
		return new int[] {posX, posY};
	}

	// ===========================================================================================
	void swapTiles (int toSwap, int[] toSwapPosition, int blank, int[] blankPosition) {
		// clicked non-empty cell
		int toSwapX = toSwapPosition[0];
		int toSwapY = toSwapPosition[1];

		// empty cell
		int blankX = blankPosition[0];
		int blankY = blankPosition[1];

		// swaps the position of the two numbers in the 2d array
		this.gameBoard[blankX][blankY] = toSwap;
		this.gameBoard[toSwapX][toSwapY] = blank;

		// updates UI
		this.updateGridPane();
	}

	// ===========================================================================================
	private void updateGridPane() {
		this.addTilesToGrid();
	}

	// ===========================================================================================
	// detects the winning condition
	void checkGameState() {
		int[][] solvedState = { {1,2,3}, {4,5,6}, {7,8,0} };

		// check if the two arrays are equal
		if (Arrays.deepEquals(this.gameBoard, solvedState)) {
//			System.out.println("YOU WIN!");		// uncomment for testing
			this.isGameOver = true;
		}

		this.displayMessage();
	}

	// ===========================================================================================
	// display the current game status
	private void displayMessage() {
		// set up the canvas for drawing/writing
		GraphicsContext gc = this.canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);

		// display text on the canvas
		Font theFont = Font.font("Monospaced", FontWeight.BOLD, 45);
		gc.setFont(theFont);		// font of the text
		gc.setFill(Color.DARKBLUE);	// color of text;

		// centers Text
		gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

		if (this.isSolvable) {
			if (this.isGameOver) {
				gc.fillText("YOU WIN!", GameStage.WINDOW_WIDTH/2, GameStage.WINDOW_HEIGHT*0.12);
			} else {
				gc.fillText("8-PUZZLE GAME", GameStage.WINDOW_WIDTH/2, GameStage.WINDOW_HEIGHT*0.12);
			}
		} else {
			gc.fillText("UNSOLVABLE!", GameStage.WINDOW_WIDTH/2, GameStage.WINDOW_HEIGHT*0.12);
		}
	}

	// ===========================================================================================
	// determines whether to allow the user to swap tiles or not
	boolean allowSwapping() {
		return (this.isSolvable==true && this.isGameOver == false);
	}

}
