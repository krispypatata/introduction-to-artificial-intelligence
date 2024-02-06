// package declaration
package puzzlegame;

// import statement(s)
import java.util.LinkedList;

// class deinition
public class Node {
	// *******************************************************************************************
	// attribute(s)
	private int[][] state;
	private Node parent;
	private String action;
	private LinkedList<String> path;

	private int gValue;
	private int hValue;
	private int fValue;

	// *******************************************************************************************
	// constructor
	public Node (int[][] passedState, Node parent, String action) {
		// initialize the state contents
		this.state = new int[GameStage.TILE_ROWS][GameStage.TILE_COLUMNS];
		for (int i=0; i<GameStage.TILE_ROWS; i++) {
			for (int j=0; j<GameStage.TILE_COLUMNS; j++) {
				this.state[i][j] = passedState[i][j];
			}
		}

		this.parent = parent;
		this.action = action;

		// get the path
		this.path = new LinkedList<String>();
		Node temp = this;
		while (temp.getParent()!=null) {
			this.path.addFirst(temp.action);
			temp = temp.getParent();
		}

		this.gValue = this.path.size();				// cost of the path from the initial state to the current state
		this.hValue = this.getManhattanSum();		// Sum of all manhattan distances computed from each tile
		this.fValue = this.gValue + this.hValue;	// f(n) = g(n) + h(n)

	}

	// *******************************************************************************************
	// method(s)
	// ======================================================================================================
	// computes the sum of all the manhattan distance of each tile
	private int getManhattanSum() {
		int sum = 0;

		// gets the manhattan distance of each tile with values from 1-8 (0 or empty tile is not included)
		for (int i=1; i<9; i++) {
			sum += this.getManhattanDistance(i);
		}

		return sum;
	}

	// ======================================================================================================
	// computes the manhattan distance of a tile
	private int getManhattanDistance(int value) {
		int manhattanDistance = 0;
		int x1 = 0, y1 = 0, x2 = 0, y2 = 0;

		// find the location/position of the value/tile in the game board/2d integer array
		for (int i=0; i<GameStage.TILE_ROWS; i++) {
			for (int j=0; j<GameStage.TILE_COLUMNS; j++) {
				if (AStar.GOAL_STATE[i][j] == value) {
					x1 = i;
					y1 = j;
				}

				if (this.state[i][j] == value) {
					x2 = i;
					y2 = j;
				}
			}
		}

		// computes the manhattan distance
		// manhattan distance = |x1 - x2| + |y1 - y2|
		manhattanDistance = Math.abs(x1-x2) + Math.abs(y1-y2);

		return manhattanDistance;
	}

	// ======================================================================================================
	// getters
	public int[][] getState () {
		return this.state;
	}

	public LinkedList<String> getPath() {
		return this.path;
	}

	public Node getParent () {
		return this.parent;
	}

	public String getAction() {
		return this.action;
	}

	public int getGValue() {
		return this.gValue;
	}

	public int getHValue() {
		return this.hValue;
	}

	public int getFValue() {
		return this.fValue;
	}

}
