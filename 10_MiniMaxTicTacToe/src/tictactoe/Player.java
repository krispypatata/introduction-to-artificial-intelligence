// package declaration
package tictactoe;

// import statement(s)


// class definition
public class Player {
	// *******************************************************************************************
	// attribute(s)
	protected int type;
	protected char moveSymbol;
	protected String name;

	/* constants */
	// symbols
	public static char MAXIMIZER_X = 'X';
	public static char MINIMIZER_O = 'O';

	// types
	public static int MAXIMIZER = 0;
	public static int MINIMIZER = 1;
	// *******************************************************************************************
	// constructor
	public Player (int type, String name) {
		this.type = type;
		this.name = name;

		if (this.type == MAXIMIZER) this.moveSymbol = Player.MAXIMIZER_X;
		else this.moveSymbol = Player.MINIMIZER_O;
	}


	// *******************************************************************************************
	// method(s)

	// ===========================================================================================
	/*
	 * 		Getter(s)
	 * */
	public char getMoveSymbol () {
		return this.moveSymbol;
	}

	public int getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}
}
