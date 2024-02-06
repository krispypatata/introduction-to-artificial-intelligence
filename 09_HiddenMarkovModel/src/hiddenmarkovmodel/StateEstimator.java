// package declaration
package hiddenmarkovmodel;

// class definition
public class StateEstimator {
	// *******************************************************************************************
	// attribute(s)
	private String probabilityStatement;
	private Sequence sequence;

	private String givenState;
	private String givenObservableMeasurement;
	private boolean isValidCase;

	private char stateAsked;
	private char observableMeasurementAsked;
	private int indexAsked;

	private double probability;


	// *******************************************************************************************
	// constructor(s)
	StateEstimator (String stringCase, Sequence sequence) {
		this.probabilityStatement = stringCase;
		this.sequence = sequence;

		this.extractGivens();
		if (this.isValidCase == true) this.evaluateProbabilityStatement();
	}


	// *******************************************************************************************
	// method(s)

	// ===========================================================================================
	/*
	 * 		Extract the probability statement where tasked to compute for
	 * */
	private void extractGivens() {
		this.isValidCase = false;
		String[] components = this.probabilityStatement.split("\\s+[gG][iI][vV][eE][nN]\\s+");
		if (components.length==2) this.isValidCase = true;
		else return;

		// extract the left and right sides of the string 'given'
		this.givenState = components[0];
		this.givenObservableMeasurement = components[1];

		// remove whitespaces
		this.givenState = this.givenState.replaceAll("\\s", "");
		this.givenObservableMeasurement = this.givenObservableMeasurement.replaceAll("\\s", "");

		// extract the given character representations of the state asked and observable measurement asked
		this.stateAsked = this.givenState.charAt(0);
		this.observableMeasurementAsked = this.givenObservableMeasurement.charAt(0);

		// check if the given probability statement is still valid
		if ( ( Character.toLowerCase(this.stateAsked) != Character.toLowerCase(this.sequence.getSRepresentation())
				&& Character.toLowerCase(this.stateAsked) != Character.toLowerCase(this.sequence.getTRepresentation()) )
				||
			( Character.toLowerCase(this.observableMeasurementAsked) != Character.toLowerCase(this.sequence.getERepresentation())
				&& Character.toLowerCase(this.observableMeasurementAsked) != Character.toLowerCase(this.sequence.getFRepresentation()) )
				) {
			this.isValidCase = false;
			return;
		}

		// extract the index of the state we wish to compute the probability of
		int stateIndex = Integer.parseInt(this.givenState.substring(1));
		int observableMeasurementIndex = Integer.parseInt(this.givenObservableMeasurement.substring(1));

		this.indexAsked = 0;
		if (stateIndex == observableMeasurementIndex) this.indexAsked = stateIndex;
		else {
			this.indexAsked = -1;
			this.isValidCase = false;
			return;
		}
	}


	// ===========================================================================================
	/*
	 *		Solve/Get the values of the given probability statement
	 * */
	private void evaluateProbabilityStatement() {
		this.probability = this.sequence.getStateProbability(this.stateAsked, this.observableMeasurementAsked, this.indexAsked);
	}


	// ===========================================================================================
	/*
	 * 		Getter(s)
	 *
	 * */
	public String getProbabilityStatement() {
		return this.probabilityStatement;
	}

	public boolean checkValidity() {
		return this.isValidCase;
	}

	public double getProbability() {
		return this.probability;
	}

}
