// package declaration
package puzzlegame;

// import statement(s)
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

// class definition
public class AStar {
	// *******************************************************************************************
	// attribute(s)
	public final static int[][] GOAL_STATE = {{1,2,3}, {4,5,6}, {7,8,0}};

	private Node initialState;

	private LinkedList<Node> openList;
	private LinkedList<Node> closedList;

	private long afterUsedMem;	// check memory usage after the algorithm's execution

	// *******************************************************************************************
	// constructor(s)
	public AStar (int[][] initialState) {
		this.initialState = new Node(initialState, null, null);

		this.openList = new LinkedList<Node>();
		this.closedList = new LinkedList<Node>();
	}

	// *******************************************************************************************
	// method(s)
	// ======================================================================================================
	// performs the A* informed search algorithm (to solve the given 8-puzzle problem)
	Node solve() {
		this.openList.add(this.initialState);	// frontier

		int outerLoop = 1;	// for checking

		while (!this.openList.isEmpty()) {
			// bestNode = openList.removeMinF();
			Node bestNode = this.getNodeWithMinF(this.openList);
			this.openList.remove(bestNode); // removes the best node in the frontier/ list of nodes to examine

			// closedList.add( bestNode );
			this.closedList.add(bestNode);	// removes the best node in the explored list/frontier

			if ( this.checkIfGoalStateReached(bestNode.getState()) ) {
				System.out.println("\tSOLVED!");	// for checking
				System.out.println("\n=====================");	// for checking

				return bestNode;
			}

			int innerLoop = 1;	// for checking

			for (String action: this.getPossibleActions(bestNode.getState())) {
				// x = Result( bestNode, a );
				Node newNode = new Node(this.getResult(bestNode.getState(), action), bestNode, action);

				// for checking
				System.out.println("=====================");
				System.out.println("Loop #" + outerLoop + "." + innerLoop);
				System.out.println("---------------------");
				System.out.println("CURRENT STATE");
				this.printState(bestNode.getState());
				System.out.println("---------------------");
				System.out.println("POSSIBLE ACTION: " + action);
				System.out.println("---------------------");
				System.out.println("NEW STATE");
				this.printState(newNode.getState());

				boolean isExplored = this.checkIfExplored(newNode, this.closedList);
				boolean isGLessThanDuplicate = this.compareDuplicatesGValues(newNode, this.closedList);

				// if( ( x is not in ( openList or closedList) ) or ( x is in openList and x.g < duplicate.g)):
				if ( (isExplored == false) || (isExplored == true && isGLessThanDuplicate == true) ){
					this.openList.add(newNode);
				}

				// memory usage
				long currentUsedMem = GameStage.getUsedMemory();
				if (currentUsedMem > this.afterUsedMem) afterUsedMem = currentUsedMem;

				// for checking
				System.out.println("=====================\n");
				innerLoop++;
			}

			outerLoop++;	// for checking

			// if (outerLoop == 2) break; // for checking
		}

		return null;
	}

	// ======================================================================================================
	// finds the node with the minimum value of f (in the openList)
	private Node getNodeWithMinF(LinkedList<Node> nodeList) {
		Node temp = null;
		for (Node node : nodeList) {
			if (temp == null) temp = node;
			else if (node.getFValue()<temp.getFValue()) {
				temp = node;
			}
		}

		return temp;
	}

	// ======================================================================================================
	// will check if a state is already explored or not
	private boolean checkIfExplored (Node passedNode, LinkedList<Node> nodeList) {
		// check if the given state exists within the exploredFrontier
		for (Node n : nodeList) {
			if (Arrays.deepEquals(passedNode.getState(), n.getState())) {
				return true;
			}
		}
		return false;
	}

	// ======================================================================================================
	private boolean compareDuplicatesGValues (Node passedNode, LinkedList<Node> nodeList) {
		// check if the given state exists within the exploredFrontier
		for (Node n : nodeList) {
			if (Arrays.deepEquals(passedNode.getState(), n.getState())) {
				if (passedNode.getGValue() < n.getGValue()) return true;
			}
		}
		return false;
	}

	// ======================================================================================================
	// ======================================================================================================
	// ======================================================================================================
	// scrapped from Exercises 1 & 2
	// method(s)
	private ArrayList<String> getPossibleActions (int[][] state) {

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

	// ======================================================================================================
	// given a state, and an action the getResult function will return the next state
	int[][] getResult (int[][] state, String action) {
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

	// ======================================================================================================
	// a function that returns true if the current state is equivalent to the goal state, and false if otherwise.
	private boolean checkIfGoalStateReached(int[][] state) {
		boolean isGoalState = false;
		if (Arrays.deepEquals(state, AStar.GOAL_STATE)) isGoalState = true;
		return isGoalState;
	}

	// ======================================================================================================
	// display a state's contents
	private void printState (int[][] state) {
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				System.out.print(" " + state[i][j] + " ");
			}
			System.out.println();
		}
	}

	// ======================================================================================================
	// gets the position of the blank/empty tile in a given state
	private int[] getBlankTilePosition (int[][] state) {
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

	public long getUsedMem() {
		return this.afterUsedMem;
	}

}
