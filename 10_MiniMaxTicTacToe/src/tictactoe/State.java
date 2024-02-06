// package declaration
package tictactoe;

// class definition
public class State {
	// *******************************************************************************************
	// attribute(s)
	private int action;
	private char[][] state;


	// *******************************************************************************************
	// constructor
	public State(int move, char[][] grid) {
		this.setAction(move);
		this.setState(grid);
	}


	// *******************************************************************************************
	// method(s)

	// ===========================================================================================
	/*
	 * 		Getter(s)
	 * */
	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public char[][] getState() {
		return state;
	}

	public void setState(char[][] state) {
		this.state = state;
	}
}
