// package declaration
package kmcclassification;

/*
 * 	Super Class
 *
 * */
// class definition
public abstract class DoublePair {
	// *******************************************************************************************
	// attribute(s)
	protected Double x;
	protected Double y;

	// *******************************************************************************************
	// constructor(s) - instantiate with children's constructor(s)
	public DoublePair (Double x, Double y) {
		this.x = x;
		this.y = y;
	}

	// *******************************************************************************************
	// method(s)
	// ===========================================================================================
	/*
	 * 		Getter(s)
	 *
	 * */
	public Double getX() {
		return x;
	}
	public Double getY() {
		return y;
	}

	// ===========================================================================================
	/*
	 * 		Setter(s)
	 *
	 * */
	public void setX(Double x) {
		this.x = x;
	}
	public void setY(Double y) {
		this.y = y;
	}
}
