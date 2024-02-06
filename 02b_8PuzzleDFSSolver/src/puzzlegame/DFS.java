package puzzlegame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

// extends the superclass BruteForce
public class DFS extends BruteForce {
	private long afterUsedMem;	// check memory usage after the algorithm's execution

	// constructor
	public DFS(int[][] initialState) {
		// initialize necessary variables
		this.initialState = new State(initialState, null, null);

		this.frontier = new LinkedList<State>();
		this.exploredFrontier = new ArrayList<State>();

		this.path = new LinkedList<String>();
	}

	// method(s)
	public State solve() {
		State currentState = null;
		State newState = null;

		try {
			// push the initial state to our frontier
			this.frontier.push(this.initialState);

			// ==============================================================================
			int outLoopCounter = 1;

			// while there is still a state to examine and the goal state hasn't been reached
			while (this.frontier.isEmpty() == false) {

				currentState = this.frontier.pop();				// pop a state from the frontier
																// the popped state is the current state
				this.exploredFrontier.add(currentState);		// add the current state to explored states

				// terminate the loop if goal state has been reached
				if (this.checkIfGoalStateReached(currentState.getState())) {
					System.out.println("\tSOLVED!");	// for checking
					System.out.println("\n=====================");	// for checking
					return currentState;		// return the object

				} else {
					int inLoopCounter = 1;

					// find all the possible actions that can be performed on the current state
					for (String action : this.getPossibleActions(currentState.getState())) {

						// for checking
						System.out.println("=====================");
						System.out.println("Loop #" + outLoopCounter + "." + inLoopCounter);
						System.out.println("---------------------");
						System.out.println("CURRENT STATE");
						this.printState(currentState.getState());
						System.out.println("---------------------");
						System.out.println("POSSIBLE ACTION: " + action);

						// get the new state when a certain possible action is performed on the current state
						// append the action performed to path
						newState = new State(this.getResult(currentState.getState(), action), currentState, action);

						// for checking
						System.out.println("---------------------");
						System.out.println("NEW STATE");
						this.printState(newState.getState());

						// check if newState is already explored or not
						// if it doesn't push it to our frontier queue
						if (this.checkIfExplored(newState.getState(), this.exploredFrontier) == false) {
							frontier.push(newState);
						}

						// memory usage
						long currentUsedMem = GameStage.getUsedMemory();
						if (currentUsedMem > this.afterUsedMem) afterUsedMem = currentUsedMem;

						// for checking
						System.out.println("=====================\n");
						inLoopCounter++;


					}
				}
//				if (outLoopCounter==20) break;
				// for checking
				outLoopCounter ++;
			}


		} catch(OutOfMemoryError oom) {
			System.out.println("Something went wrong! :(");
			return null;
		}

		// return value
		return null;

	}

	// will check if a state is already explored or not
	private boolean checkIfExplored (int[][] state, ArrayList<State> exploredFrontier) {

		// check if the given state exists within the exploredFrontier
		for (State e : exploredFrontier) {
			if (Arrays.deepEquals(state, e.getState())) {
				return true;
			}
		}
		return false;
	}

	public long getUsedMem() {
		return this.afterUsedMem;
	}

}
