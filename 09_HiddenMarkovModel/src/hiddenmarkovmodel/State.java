// package declaration
package hiddenmarkovmodel;

// class definition
public class State {
	// *******************************************************************************************
	// attribute(s)
	private char value;
	private State previousState;

	private Sequence sequenceBasis;

	private double pS;
	private double pT;

	private double pE;
	private double pF;


	private double pSGivenE;
	private double pSGivenF;

	private double pTGivenE;
	private double pTGivenF;

	// *******************************************************************************************
	// constructor(s)
	State (char value, State previousState, Sequence sequenceBasis) {
		this.value = value;
		this.previousState = previousState;
		this.sequenceBasis = sequenceBasis;

		this.computeStateProbabilities();
		this.computeEFPairProbabilities();
		this.computeProbabilityInMarkovChain();
	}

	// constructor for the starting state of the sequence
	State (char state, Sequence sequenceBasis) {
		this.value = state;
		this.sequenceBasis = sequenceBasis;

		this.previousState = null;

		if (this.value == this.sequenceBasis.getSRepresentation()) {
			this.pS = (double) 1;
			this.pT = (double) 0;
		}
		else if (this.value == this.sequenceBasis.getTRepresentation()) {
			this.pT = (double) 1;
			this.pS = (double) 0;
		}

		this.computeEFPairProbabilities();
		this.computeProbabilityInMarkovChain();
	}


	// *******************************************************************************************
	// method(s)

	// ===========================================================================================
	/*
	 *		Solve for the probabilities of the state
	 * */
	private void computeStateProbabilities() {
		// check the value of the state first
		if (this.value == this.sequenceBasis.getSRepresentation()) {
			this.pS = this.sequenceBasis.getPSGivenS() * this.previousState.getPS() + this.sequenceBasis.getPSGivenT() * this.previousState.getPT();
			this.pT = 1 - this.pS;
		}
		else if (this.value == this.sequenceBasis.getTRepresentation()) {
			this.pT = this.sequenceBasis.getPTGivenT() * this.previousState.getPT() + this.sequenceBasis.getPTGivenS() * this.previousState.getPS();
			this.pS = 1 - this.pT;
		}
	}


	// ===========================================================================================
	/*
	 *		Solve for the probabilities of observable measurement values
	 * */
	private void computeEFPairProbabilities () {
		this.pE = Sequence.getPEGivenS() * this.pS + Sequence.getPEGivenT() * this.pT;
		this.pF = Sequence.getPFGivenS() * this.pS + Sequence.getPFGivenT() * this.pT;
	}


	// ===========================================================================================
	/*
	 *		Solve for the probabilities of the state in the Markov Chain using the observable measurement values
	 * */
	private void computeProbabilityInMarkovChain () {
		this.pSGivenE = (Sequence.getPEGivenS() * this.pS) / this.pE;
		this.pSGivenF = (Sequence.getPFGivenS() * this.pS) / this.pF;

		this.pTGivenE = (Sequence.getPEGivenT() * this.pT) / this.pE;
		this.pTGivenF = (Sequence.getPFGivenT() * this.pT) / this.pF;
	}


	// ===========================================================================================
	/*
	 *		Getter(s)
	 * */
	public double getPS() {
		return this.pS;
	}

	public double getPT() {
		return this.pT;
	}

	public double getPE() {
		return this.pE;
	}

	public double getPF() {
		return this.pF;
	}

	public double getPSGivenE() {
		return this.pSGivenE;
	}

	public double getPSGivenF() {
		return this.pSGivenF;
	}

	public double getPTGivenE() {
		return this.pTGivenE;
	}

	public double getPTGivenF() {
		return this.pTGivenF;
	}
}
