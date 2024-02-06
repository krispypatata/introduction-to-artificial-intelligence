// package declaration
package tictactoe;

// import statement(s)
import java.util.InputMismatchException;
import java.util.Scanner;

// import statement(s)

// class definition
public class TicTacToe {
	// *******************************************************************************************
	// attribute(s)
	// players
	private Player user;
	private Player ai;

	// for game conditions
	private int turn;
	private Player winner;
	private boolean isGameDraw;
	private boolean isGameOver;

	// grid/game board
	private char[][] grid;


	// *******************************************************************************************
	// constructor(s)
	public TicTacToe() {
		// initializing some attributes' values
		this.isGameOver = false;
		this.isGameDraw = false;
		this.winner = null;

		this.initializeGrid();
		this.play();
	}


	// *******************************************************************************************
	// method(s)

	// ===========================================================================================
	/*
	 *		start the game
	 * */
	private void play() {
		// Scanner object for getting inputs from the user
		Scanner scanner = new Scanner(System.in);

		// display the main menu
		this.showMenu(scanner);

		Player player;
		this.turn = 0;

		// run until a winning condition has been met or there are no more available slots in the grid
		while (!this.isGameOver) {
			if (this.turn == this.user.getType() ) player = this.user;
			else player = this.ai;

			// print the current state of the grid
			this.printGameState();

			// ask an input from the user if it is his/her turn
			// else allow the AI to make a move
			if (player.getName().equals("User"))
				this.askMove(scanner, player, this.grid);
			else if (player.getName().equals("AI")) this.generateAIMove(player, this.grid);

			// check if someone has already won or if the game ends in a draw
			this.checkIfGameOver(player);
		}

		// game over's prompt message
		this.printGameState();
		if (this.isGameDraw) {
			System.out.println("DRAW!");
		} else {
			System.out.println(this.winner.getName().toUpperCase() + " WON!");
		}
		System.out.println("===============================================");

		// close the Scanner object
		scanner.close();
	}


	// ===========================================================================================
	/*
	 *		display the game's main menu
	 * */
	private void showMenu(Scanner scanner) {

		// Greetings message
		System.out.println("===============================================");
		System.out.println("WELCOME!");
		System.out.println("\t This is a simple game of Tic-Tac-Toe.");
		System.out.println("You'll be playing against an AI on a 3x3 Grid.");
		System.out.println("The objective is simple : players  take  turns \n"
						+ "placing their symbol (X or O) in an empty cell \n"
						+ "with the goal of forming a horizontal,vertical, \n"
						+ "or diagonal line of three of their symbols.");
		System.out.println("===============================================");
		System.out.println("\nDo you want to go first?");


		// ask the user if he/she wants to go first (play as 'X')
		while (true) {
			try {
				System.out.print("Enter Choice [y/n]: ");
				String input = scanner.nextLine().trim().toLowerCase();

				// user goes first
				if (input.equals("y")) {
					this.user = new Player(Player.MAXIMIZER, "User");
					this.ai = new Player(Player.MINIMIZER, "AI");
					System.out.println("\n===============================================");
					System.out.println("User goes first!");
					break;
				}
				// ai goes first
				else if (input.equals("n")) {
					this.ai = new Player(Player.MAXIMIZER, "AI");
					this.user = new Player(Player.MINIMIZER, "User");
					System.out.println("===============================================");
					System.out.println("AI goes first!");
					break;
				} else {
					System.out.println("\n-----------------------------------------------");
					System.out.println("Invalid Input!");
					System.out.println("-----------------------------------------------\n");
				}
			} catch (InputMismatchException  e) {
				System.out.println("\n-----------------------------------------------");
				System.out.println("Invalid Input!");
				System.out.println("-----------------------------------------------\n");
			}
		}
	}


	// ===========================================================================================
	/*
	 * 		initialize the contents of the game's grid with integer values from 1-9
	 * */
	private void initializeGrid() {
		this.grid = new char[3][3];

		for (int row=0; row<this.grid.length; row++)
			for (int column=0; column<this.grid[row].length; column++)
				this.grid[row][column] = (char) ( (row*this.grid[row].length + column+1) + '0' );
	}


	// ===========================================================================================
	/*
	 * 		print the current state of the grid
	 * 		also, put some decorations
	 *
	 * */
	private void printGameState() {
		System.out.println("\n================================================");
		System.out.println("GRID:");
		for (int row=0; row<this.grid.length; row++) {
			System.out.println("-------");

			for (int column=0; column<this.grid[row].length; column++) {
				if (column == 0) System.out.print("¦");
				System.out.print(this.grid[row][column]);
				System.out.print("¦");
			}

			System.out.println();
			if (row==this.grid.length-1) System.out.println("-------");

		}

		System.out.println("================================================");
	}


	// ===========================================================================================
	/*
	 * 		generate the best possible move for the AI
	 *
	 * */
	private void generateAIMove (Player ai, char[][] grid) {
		System.out.print(ai.getName() + "'s turn (" + ai.getMoveSymbol() + "): ");
		int index = MiniMaxAI.getBestMove(grid, ai.getType());
		System.out.print(index+1);
		this.placeMove(grid, index, ai);
	}


	// ===========================================================================================
	/*
	 * 		ask a move from the player/s
	 *
	 * */
	private void askMove (Scanner scanner, Player player, char[][] grid) {

		int inputValue;
		boolean inputIsValid = false;
		while (!inputIsValid) {
			try {
				System.out.print(player.getName() + "'s turn (" + player.getMoveSymbol() + "): ");
				String input = scanner.nextLine().trim().toLowerCase();

				inputValue = Integer.parseInt(input);
				if (inputValue >=1 && inputValue <=9) {
					inputIsValid = this.placeMove(grid, inputValue-1, player);
				}
				else {
					System.out.println("\n-----------------------------------------------");
					System.out.println("Invalid Input!");
					System.out.println("-----------------------------------------------\n");
				}

			} catch (Throwable e) {
				System.out.println("\n-----------------------------------------------");
				System.out.println("Invalid Input!");
				System.out.println("-----------------------------------------------\n");
			}
		}
	}


	// ===========================================================================================
	/*
	 * 		execute a move on the Tic Tac Toe's grid
	 *
	 * */
	private boolean placeMove(char[][] grid, int index, Player player) {
		int row = index/grid.length;
		int column = index%grid.length;

		// check if the cell is avaiable or not
		if (grid[row][column] == Player.MAXIMIZER_X || grid[row][column] == Player.MINIMIZER_O) {
			System.out.println("\n-----------------------------------------------");
			System.out.println("Tile is already occupied!");
			System.out.println(index+1);
			System.out.println("-----------------------------------------------\n");

			// if unsuccessful
			return false;
		}

		// update the cell's value
		this.grid[row][column] = player.getMoveSymbol();

		// update turn counter
		this.turn = (this.turn + 1)%2;

		// if successful
		return true;
	}


	// ===========================================================================================
	/*
	 * 		check if someone has already won the game
	 *
	 * */
	public static boolean checkIfSomeoneHasWon(char[][] grid) {
		/* Winning condition: 3-cell straight line */
		// horizontal
		for (int row=0; row<grid.length; row++) {
			if (grid[row][0] == grid[row][1] && grid[row][1] == grid[row][2]) {
				return true;
			}
		}

		// vertical
		for (int column=0; column<grid.length; column++) {
			if (grid[0][column] == grid[1][column] && grid[1][column] == grid[2][column]) {
				return true;
			}
		}

		// rising diagonal
		if (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]) {
			return true;
		}


		// falling diagonal
		if (grid[2][0] == grid[1][1] && grid[1][1] == grid[0][2]) {
			return true;
		}

		// if there's no winner yet
		return false;
	}


	// ===========================================================================================
	/*
	 * 		check if the grid is already full
	 *
	 * */
	public static boolean checkIfGridIsFull(char[][] grid) {
	    // check if there's still an available slot in the grid to place a move
	    for (int row = 0; row < grid.length; row++)
	        for (int column = 0; column < grid[row].length; column++)
	            if (grid[row][column] != Player.MAXIMIZER_X && grid[row][column] != Player.MINIMIZER_O)
	                return false;

	    // all cells are occupied
	    return true;
	}


	// ===========================================================================================
	/*
	 * 		check if the game is already over (either in a draw or with a winner)
	 *
	 * */
	private void checkIfGameOver(Player player) {
	    // check if someone has won the game
	    boolean someoneHasWon = TicTacToe.checkIfSomeoneHasWon(this.grid);

	    // if someone has already won
	    if (someoneHasWon) {
	        // determine who's the winner
	        this.winner = player;
	        this.isGameOver = true;
	        return;
	    }

	    // if grid is already full
	    if (checkIfGridIsFull(this.grid)) {
	        this.isGameDraw = true;
	        this.isGameOver = true;
	    }
	}

}
