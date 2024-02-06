package puzzlegame;


import java.util.LinkedList;

public class State {
	int[][] state;
	LinkedList<String> path;
	State parent;
	String action;

	// constructor
	public State (int[][] passedState, State parent, String action) {
		// initialize the state contents
		this.state = new int[GameStage.TILE_ROWS][GameStage.TILE_COLUMNS];
		for (int i=0; i<GameStage.TILE_ROWS; i++) {
			for (int j=0; j<GameStage.TILE_COLUMNS; j++) {
				this.state[i][j] = passedState[i][j];
			}
		}

		// initialize other variables
		this.action = action;
		this.parent = parent;
		this.path = new LinkedList<String>();

	}

	// getters
	public LinkedList<String> getPath () {
		// get the path
		State temp = this;
		while (temp.getParent()!=null) {
			this.path.addFirst(temp.action);
			temp = temp.getParent();
		}

		return this.path;
	}

	public int[][] getState () {
		return this.state;
	}

	public State getParent () {
		return this.parent;
	}
}
