// package declaration
package kmcclassification;

// import statement(s)
import java.util.ArrayList;

// class definition
public class Centroid extends DoublePair {
	// *******************************************************************************************
	// attribute(s)
	private int clusterNumber;
	private ArrayList<SelectedWineAttributes> classifiedList;

	// *******************************************************************************************
	// constructor(s)
	// ===========================================================================================
	public Centroid (Double x, Double y, int clusterNumber) {
		super(x, y);
		this.clusterNumber = clusterNumber;

		this.setClassifiedList(new ArrayList<SelectedWineAttributes>());
	}

	// ===========================================================================================
	// constructor for cloning
	public Centroid (Centroid centroid) {
		super(centroid.getX(), centroid.getY());
		this.clusterNumber = centroid.getClusterNumber();

		this.setClassifiedList(new ArrayList<SelectedWineAttributes>());
	}

	// *******************************************************************************************
	// method(s)

	// ===========================================================================================
	/*
	 * 		Getter(s)
	 *
	 * */
	public int getClusterNumber() {
		return this.clusterNumber;
	}

	public ArrayList<SelectedWineAttributes> getClassifiedList() {
		return classifiedList;
	}

	// ===========================================================================================
	/*
	 * 		Setter(s)
	 *
	 * */
	public void setClassifiedList(ArrayList<SelectedWineAttributes> classifiedList) {
		this.classifiedList = classifiedList;
	}
}
