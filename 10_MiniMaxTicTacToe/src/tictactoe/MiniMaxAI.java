// package declaration
package tictactoe;

// import statement(s)
import java.util.ArrayList;

// class definition
public class MiniMaxAI {
	// *******************************************************************************************
	// attribute(s)
	// ----- none


	// *******************************************************************************************
	// constructor
	// ----- default


	// *******************************************************************************************
	// method(s)

	// ===========================================================================================
	/*
	 *		find the best possible state and move for the player
	 *
	 * */
	public static int getBestMove(char[][] state, int type) {
		// ArrayList for the resulting states given a set of possible actions/moves
        ArrayList<State> successors = MiniMaxAI.generateSuccessors(state);

        // ArrayList fot the possible actions/moves
        ArrayList<Integer> possibleMoves = new ArrayList<>();

        // find the best available index/cell in the grid to place an action/move
        int bestIndex = 0;

        // if AI goes first (playing as player X - Maximizer)
        if (type == Player.MAXIMIZER) {
        	// get the values of states
            for (State states: successors)
            	possibleMoves.add(MiniMaxAI.getMinValue(states));

            // initial value of maximum
	        int max = possibleMoves.get(0);

	        // traverse through the possible moves array list
	        for (int i = 1; i < possibleMoves.size(); i++) {
	        	// find the index with the maximum value
	            if( possibleMoves.get(i) > max){
	                max = possibleMoves.get(i);
	                bestIndex = i;
	            }
	        }

        }
        // if AI goes second (playing as player 0 - Minimizer)
        else if (type == Player.MINIMIZER) {
        	// get the values of states
            for (State states: successors)
            	possibleMoves.add(MiniMaxAI.getMaxValue(states));

            // initial value of minimum
	        int min = possibleMoves.get(0);

	     // traverse through the possible moves array list
	        for (int i = 1; i < possibleMoves.size(); i++) {
	        	// find the index with the minimum value
	            if( possibleMoves.get(i) < min){
	                min = possibleMoves.get(i);
	                bestIndex = i;
	            }
	        }
        }

        // get the action/move associated with the best index (for maximizer/minimizer)
        int action = successors.get(bestIndex).getAction();

        // return the best possible move/action
        return action;
	}


	// ===========================================================================================
	/*
	 *		find the best move for player X
	 *
	 * */
	private static int getMaxValue(State state) {
		boolean isTerminal = MiniMaxAI.checkIfTerminal(state.getState());
		if (isTerminal)
			return (MiniMaxAI.getUtility(state.getState()));

		int m = (int) Double.NEGATIVE_INFINITY;

		for (State child : generateSuccessors(state.getState())) {
			m = Math.max(m, MiniMaxAI.getMinValue(child));
		}
		return m;
	}


	// ===========================================================================================
	/*
	 *		find the best move for player O
	 *
	 * */
	private static int getMinValue(State state) {
	    boolean isTerminal = MiniMaxAI.checkIfTerminal(state.getState());
	    if (isTerminal)
	        return (getUtility(state.getState()));

	    int m = (int) Double.POSITIVE_INFINITY;

	    for (State child : generateSuccessors(state.getState())) {
	        m = Math.min(m, MiniMaxAI.getMaxValue(child));
	    }
	    return m;
	}


	// ===========================================================================================
	/*
	 *		returns true if the given state is a terminal state (an end game state)
	 *
	 * */
	private static boolean checkIfTerminal(char[][] state) {
		boolean isTerminal = false;
		isTerminal = TicTacToe.checkIfSomeoneHasWon(state) || TicTacToe.checkIfGridIsFull(state);
		return isTerminal;
	}


	// ===========================================================================================
	/*
	 * 		returns the current value of the state to a player
	 * 		1 	= 	if player X won
	 * 		0 	= 	if the game ends in a draw
	 * 	   -1 	= 	if player X loses (player 0 wins)
	 *
	 * */
	private static int getUtility(char[][] state) {
		// if the game did not end in a draw
		if (!TicTacToe.checkIfGridIsFull(state)) {
			int xMoves = 0;
			int oMoves = 0;

			// count the number of X's and O's in the grid
			for (int row=0; row<state.length; row++)
				for (int column=0; column<state[row].length; column++) {
					if (state[row][column] == Player.MAXIMIZER_X) xMoves++;
					else if (state[row][column] == Player.MINIMIZER_O) oMoves++;
				}

			// Player 'O' can only win if there's an equal number of X's and O's in the grid
			// else if the number of X's in the grid ismore than the number of O's,
			// then it is understandable that the game ends in favor of player X
			if (xMoves>oMoves) return 1;
			else return -1;
		}

		// if the game ends in a draw
		return 0;
	}


	// ===========================================================================================
	/*
	 * 		get the successors of a given state
	 *
	 * */
	private static ArrayList<State> generateSuccessors(char[][] state) {
		ArrayList<State> childrenStates = new ArrayList<State>();

		// predict whose turn is it now
		int xMoves = 0;
		int oMoves = 0;
		char playerSymbol;

		// count the numbers of X's and O's in the grid/state
		for (int row=0; row<state.length; row++)
			for (int column=0; column<state[row].length; column++) {
				if (state[row][column] == Player.MAXIMIZER_X) xMoves++;
				else if (state[row][column] == Player.MINIMIZER_O) oMoves++;
			}

		// MAXIMIZER_X always starts the game; therefore, it's player X's turn again when there's already an equal number of X's and O's in the grid
		if (xMoves == oMoves) playerSymbol = Player.MAXIMIZER_X;
		else playerSymbol = Player.MINIMIZER_O;

        // create all possible children states (possible states given a possible action/move)
        for (int index = 0; index <9; index++) {
        	// create a copy of the parent state
            char[][] newState = new char[state.length][state[0].length];
            for (int row = 0; row < state.length; row++)
            	for (int column = 0; column < state[row].length; column++)
            		newState[row][column] = state[row][column];

            // get the row and column equivalent of the given index number
    		int row = index/state.length;
    		int column = index%state.length;

    		// place the player's symbol in an available cell in the grid
            if(newState[row][column] != Player.MAXIMIZER_X && newState[row][column] != Player.MINIMIZER_O){
            	newState[row][column] = playerSymbol;
                childrenStates.add(new State(index, newState));
            }
        }

        // return the children states
		return childrenStates;
	}
}
