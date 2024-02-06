// package declaration
package knnclassification;

// import statement(s)
// -------------------

// class definition
public class DiabetesInfo {
	// *******************************************************************************************
	// attribute(s)
	private double numPregnancies;
	private double glucoseValue;
	private double bloodPressure;
	private double skinThickness;
	private double insulinValue;
	private double bmi;
	private double pedigreeFunction;
	private double age;
	private int outcome;


	// *******************************************************************************************
	// constructor(s)
	DiabetesInfo (double numPregnancies, double glucoseValue, double bloodPressure, double skinThickness, double insulinValue, double bmi, double pedigreeFunction, double age, int outcome) {
		// initialize values for the internal attributes
		this.numPregnancies = numPregnancies;
		this.glucoseValue = glucoseValue;
		this.bloodPressure = bloodPressure;
		this.skinThickness = skinThickness;
		this.insulinValue = insulinValue;
		this.bmi = bmi;
		this.pedigreeFunction = pedigreeFunction;
		this.age = age;
		this.outcome = outcome;
	}


	// *******************************************************************************************
	// method(s)

	// ===========================================================================================
	/*
	 * 		For checking
	 *
	 * 		showClassification:
	 * 			true - show outcome attribute
	 * 			false - don't show outcome attribute
	 */
	void viewInfo (boolean showClassification) {
		System.out.print(this.numPregnancies + "\t| ");
		System.out.print(this.glucoseValue + "\t| ");
		System.out.print(this.bloodPressure + "\t| ");
		System.out.print(this.skinThickness + "\t| ");
		System.out.print(this.insulinValue + "\t| ");
		System.out.print(this.bmi + "\t| ");
		System.out.print(this.pedigreeFunction + "\t| ");
		System.out.print(this.age);

		if (showClassification) {
			System.out.print("\t| ");
			System.out.println(this.outcome);
		} else System.out.println();

	}

	// ===========================================================================================
	/*
	 * 		For exporting info into a file
	 */
	String getStringEquivalent() {
		String strInfo = "";

		strInfo += this.numPregnancies + ","
				+ this.glucoseValue + ","
				+ this.bloodPressure + ","
				+ this.skinThickness + ","
				+ this.insulinValue + ","
				+ this.bmi + ","
				+ this.pedigreeFunction + ","
				+ this.age + ","
				+ this.outcome;

		return (strInfo);
	}


	// ===========================================================================================
	/*
	 *		Getter(s)
	 */

	public double getNumPregnancies() {
		return numPregnancies;
	}


	public double getGlucoseValue() {
		return glucoseValue;
	}


	public double getBloodPressure() {
		return bloodPressure;
	}


	public double getSkinThickness() {
		return skinThickness;
	}


	public double getInsulinValue() {
		return insulinValue;
	}


	public double getBmi() {
		return bmi;
	}


	public double getPedigreeFunction() {
		return pedigreeFunction;
	}


	public double getAge() {
		return age;
	}


	public double getOutcome() {
		return outcome;
	}


	// ===========================================================================================
	/*
	 * 		Setter(s) - package private
	 */

	void setOutcome(int outcome) {
		this.outcome = outcome;
	}
}
