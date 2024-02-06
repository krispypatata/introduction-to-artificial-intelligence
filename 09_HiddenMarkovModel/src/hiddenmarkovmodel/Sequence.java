// package declaration
package hiddenmarkovmodel;

// import statement(s)
import java.util.ArrayList;

// class definition
public class Sequence {
	// *******************************************************************************************
	// attribute(s)
	private String sequence;

	private char s;
	private char t;
	private char e;
	private char f;

	private double pSGivenS;
	private double pSGivenT;
	private double pTGivenS;
	private double pTGivenT;

	private char[] states;
	private ArrayList<State> stateList;

	private static double pEGivenS;
	private static double pFGivenS;

	private static double pEGivenT;
	private static double pFGivenT;

	// *******************************************************************************************
	// constructor
	Sequence (String sequence, ArrayList<String> stateValues, ArrayList<String> observableMeasurementValues) {
		this.sequence = sequence;
		this.s = stateValues.get(0).charAt(0);
		this.t = stateValues.get(1).charAt(0);
		this.e = observableMeasurementValues.get(0).charAt(0);
		this.f = observableMeasurementValues.get(1).charAt(0);

		// perform the algorithm to predict probabilities in the hidden markov model
		this.computeTransitionProbabilities();
		this.computeStateProbabilities();
	}

	// *******************************************************************************************
	// method(s)

	// ===========================================================================================
	/*
	 *		Solve for the transition probabilities
	 * */
	private void computeTransitionProbabilities () {
		/*
		 * 		Compute for the denominators
		 * */
		int totalSWithNextState = 0;
		int totalTWithNextState = 0;

		// disregard the last character since we only want to compute for the probabilities of the states with next states
		for (int i=0; i<this.sequence.length()-1; i++) {
			char c = this.sequence.charAt(i);

			// count the total occurences of each state value
			if (c == this.s) {
				totalSWithNextState += 1;
			} else if (c == this.t) {
				totalTWithNextState += 1;
			}
		}

		/*
		 * 		Compute for the numerators
		 * */
		int sGivenSCount = 0;
		int sGivenTCount = 0;
		int tGivenSCount = 0;
		int tGivenTCount = 0;

		for (int i=0; i<this.sequence.length()-1; i++) {
			char currentState = this.sequence.charAt(i);
			char nextState = this.sequence.charAt(i+1);

			// S followed by an S
			if (currentState == this.s && nextState == this.s) sGivenSCount += 1;

			// T followed by an S
			if (currentState == this.t && nextState == this.s) sGivenTCount += 1;

			// S followed by a T
			if (currentState == this.s && nextState == this.t) tGivenSCount += 1;

			// T followed by a T
			if (currentState == this.t && nextState == this.t) tGivenTCount += 1;
		}

		/*
		 * 		Compute for the actual values of the transition probabilities
		 *
		 * */
		this.pSGivenS = (double) sGivenSCount / totalSWithNextState;
		this.pSGivenT = (double) sGivenTCount / totalTWithNextState;
		this.pTGivenS = (double) tGivenSCount / totalSWithNextState;
		this.pTGivenT = (double) tGivenTCount / totalTWithNextState;
	}


	// ===========================================================================================
	/*
	 * 		Getter(s)
	 *
	 * */
	private void computeStateProbabilities () {
		this.states = this.sequence.toCharArray();
		this.stateList = new ArrayList<State>();

		// compute for the probability of the first state
		this.stateList.add(new State(this.states[0], this));

		// compute for the probability of the rest of the states
		for (int i=1; i!=this.states.length; i++) {
			this.stateList.add( new State(this.states[i], this.stateList.get(i-1), this) );
		}
	}


	// ===========================================================================================
	/*
	 * 		Getter(s) and Setter(s)
	 *
	 * */
	public String getSequence () {
		return this.sequence;
	}

	public char[] getStates () {
		return this.states;
	}

	public char getSRepresentation () {
		return this.s;
	}

	public char getTRepresentation () {
		return this.t;
	}

	public char getERepresentation () {
		return this.e;
	}

	public char getFRepresentation () {
		return this.f;
	}

	public double getPSGivenS () {
		return this.pSGivenS;
	}

	public double getPSGivenT () {
		return this.pSGivenT;
	}

	public double getPTGivenS () {
		return this.pTGivenS;
	}

	public double getPTGivenT () {
		return this.pTGivenT;
	}

	/*get the computed probability of a certain hidden set in our list of states*/
	public double getStateProbability (char stateAsked, char observableMeasurementAsked, int indexAsked) {
		if (Character.toLowerCase(this.s) == Character.toLowerCase(stateAsked)) {
			if (Character.toLowerCase(this.e) == Character.toLowerCase(observableMeasurementAsked)) {
				return this.stateList.get(indexAsked).getPSGivenE();
			} else if (Character.toLowerCase(this.f) == Character.toLowerCase(observableMeasurementAsked)) {
				return this.stateList.get(indexAsked).getPSGivenF();
			}
		}
		else if (Character.toLowerCase(this.t) == Character.toLowerCase(stateAsked)) {
			if (Character.toLowerCase(this.e) == Character.toLowerCase(observableMeasurementAsked)) {
				return this.stateList.get(indexAsked).getPTGivenE();
			} else if (Character.toLowerCase(this.f) == Character.toLowerCase(observableMeasurementAsked)) {
				return this.stateList.get(indexAsked).getPTGivenF();
			}
		}
		return 0;
	}

	/*set values for the class attributes*/
	public static void setEFPairProbabilities (double pEGivenS, double pFGivenS, double pEGivenT, double pFGivenT) {
		Sequence.pEGivenS = pEGivenS;
		Sequence.pFGivenS = pFGivenS;

		Sequence.pEGivenT = pEGivenT;
		Sequence.pFGivenT = pFGivenT;
	}

	public static double getPEGivenS () {
		return Sequence.pEGivenS;
	}

	public static double getPFGivenS () {
		return Sequence.pFGivenS;
	}

	public static double getPEGivenT () {
		return Sequence.pEGivenT;
	}

	public static double getPFGivenT () {
		return Sequence.pFGivenT;
	}

}
