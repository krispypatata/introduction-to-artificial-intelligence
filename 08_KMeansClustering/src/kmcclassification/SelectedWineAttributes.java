// package declaration
package kmcclassification;

// import statement(s)
import java.util.ArrayList;

// class definition
public class SelectedWineAttributes extends DoublePair {
	// *******************************************************************************************
	// attribute(s)
	private ArrayList<Double> distances;
	private int classification;

	// *******************************************************************************************
	// constructor(s)
	SelectedWineAttributes (double x, double y) {
		super(x, y);
		this.distances = new ArrayList<Double>();
	}

	// *******************************************************************************************
	// method(s)

	// ===========================================================================================
	/*
	 * 		Getter(s)
	 *
	 * */
	public ArrayList<Double> getDistances () {
		return this.distances;
	}

	public int getClassification() {
		return classification;
	}

	// ===========================================================================================
	/*
	 * 		Setter(s)
	 *
	 * */
	public void setClassification(int classification) {
		this.classification = classification;
	}
}
