package puzzlegame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public abstract class BruteForce {
	// attribute(s)
	protected State initialState;

	protected int[][] goalState = {{1,2,3}, {4,5,6}, {7,8,0}};

	protected LinkedList<State> frontier;
	protected ArrayList<State> exploredFrontier;

	protected ArrayList<String> path;

	// method(s)
	protected ArrayList<String> getPossibleActions (int[][] state) {

		ArrayList<String> possibleActions = new ArrayList<String>();

		int[] position = this.getBlankTilePosition(state);
		int blankX = position[0];
		int blankY = position[1];


		// if the blank/empty tile is in row 2 or row 3, then it can be swapped upwards
		if (blankX > 0) possibleActions.add("U");

		// if the blank/empty tile is in column 1 or column 2, then it can be swapped rightwards
		if (blankY < 2) possibleActions.add("R");

		// if the blank/empty tile is in row 1 or row 2, then it can be swapped downwards
		if (blankX < 2) possibleActions.add("D");

		// if the blank/empty tile is in column 2 or column 3, then it can be swapped leftwards
		if (blankY > 0) possibleActions.add("L");

		return possibleActions;
	}

	// given a state, and an action the getResult function will return the next state
	public int[][] getResult (int[][] state, String action) {
		int[][] newState = new int[GameStage.TILE_ROWS][GameStage.TILE_COLUMNS];

		int[] position = this.getBlankTilePosition(state);
		int blankX = position[0];
		int blankY = position[1];

		// copy the contents of the initial state to the new state
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				newState[i][j] = state[i][j];
			}
		}

		// perform appropriate actions/swapping of values
		if (action == "U") {
			newState[blankX][blankY] = state[blankX-1][blankY];
			newState[blankX-1][blankY] = state[blankX][blankY];
		} else if (action == "R") {
			newState[blankX][blankY] = state[blankX][blankY+1];
			newState[blankX][blankY+1] = state[blankX][blankY];
		} else if (action == "D") {
			newState[blankX][blankY] = state[blankX+1][blankY];
			newState[blankX+1][blankY] = state[blankX][blankY];
		} else if (action == "L") {
			newState[blankX][blankY] = state[blankX][blankY-1];
			newState[blankX][blankY-1] = state[blankX][blankY];
		}

		// return the resulting state
		return newState;
	}

	// a function that returns true if the current state is equivalent to the goal state, and false if otherwise.
	protected boolean checkIfGoalStateReached(int[][] state) {
		boolean isGoalState = false;
		if (Arrays.deepEquals(state, this.goalState)) isGoalState = true;
		return isGoalState;
	}

	// display a state's contents
	protected void printState (int[][] state) {
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				System.out.print(" " + state[i][j] + " ");
			}
			System.out.println();
		}
	}

	// gets the position of the blank/empty tile in a given state
	protected int[] getBlankTilePosition (int[][] state) {
		int[] position = new int[2];

		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				if (state[i][j] == 0) {
					position[0] = i;
					position[1] = j;
				}
			}
		}

		return position;
	}

}
