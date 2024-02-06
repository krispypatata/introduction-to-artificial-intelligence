// package declaration
package kmcclassification;

// import statement(s)
import java.util.ArrayList;

// class definition
public class WineInfo {
	// *******************************************************************************************
	// attribute(s)
	private double alcohol;
	private double malicAcid;
	private double ash;
	private double ashAlcanity;
	private double magnesium;
	private double totalPhenols;
	private double flavanoids;
	private double nonflavanoidPhenols;
	private double proanthocyanins;
	private double colorIntensity;
	private double hue;
	private double OD280;
	private double proline;
	private double customerSegment;

	private static ArrayList<WineInfo> infoList;

	// constants
	public final static String ALCHOHOL_ATT = "Alcohol";
	public final static String MALIC_ACID_ATT = "Malic_Acid";
	public final static String ASH_ATT = "Ash";
	public final static String ASH_ALCANITY_ATT = "Ash_Alcanity";
	public final static String MAGNESIUM_ATT = "Magnesium";
	public final static String TOTAL_PHENOLS_ATT = "Total_Phenols";
	public final static String FLAVANOIDS_ATT = "Flavanoids";
	public final static String NONFLAVANOIDS_PHENOLS_ATT = "Nonflavanoid_Phenols";
	public final static String PROANTHOCYANINS_ATT = "Proanthocyanins";
	public final static String COLOR_INTENSITY_ATT = "Color_Intensity";
	public final static String HUE__ATT = "Hue";
	public final static String OD280_ATT = "OD280";
	public final static String PROLINE_ATT = "Proline";
	public final static String CUSTOMER_SEGMENT_ATT = "Customer_Segment";

	// *******************************************************************************************
	// constructor
	WineInfo (double alcohol, double malicAcid, double ash, double ashAlcanity, double magnesium, double totalPhenols,
			double flavanoids, double nonflavanoidPhenols, double proanthocyanins, double colorIntensity, double hue,
			double OD280, double proline, double customerSegment)
	{

		this.alcohol = alcohol;
		this.malicAcid = malicAcid;
		this.ash = ash;
		this.ashAlcanity = ashAlcanity;
		this.magnesium = magnesium;
		this.totalPhenols = totalPhenols;
		this.flavanoids = flavanoids;
		this.nonflavanoidPhenols = nonflavanoidPhenols;
		this.proanthocyanins = proanthocyanins;
		this.colorIntensity = colorIntensity;
		this.hue = hue;
		this.OD280 = OD280;
		this.proline = proline;
		this.customerSegment = customerSegment;

		// add the new data wine info to
		if (WineInfo.infoList != null) WineInfo.infoList.add(this);
		else {
			WineInfo.infoList = new ArrayList<WineInfo>();
			WineInfo.infoList.add(this);
		}
	}


	// *******************************************************************************************
	// method(s)

	// ===========================================================================================
	/*
	 * 		Getter(s)
	 *
	 * */
	public double getAttribute (String selectedAttribute) {
		switch (selectedAttribute) {
			case WineInfo.ALCHOHOL_ATT:
				return this.alcohol;

			case WineInfo.MALIC_ACID_ATT:
				return this.malicAcid;

			case WineInfo.ASH_ATT:
				return this.ash;

			case WineInfo.ASH_ALCANITY_ATT:
				return this.ashAlcanity;

			case WineInfo.MAGNESIUM_ATT:
				return this.magnesium;

			case WineInfo.TOTAL_PHENOLS_ATT:
				return this.totalPhenols;

			case WineInfo.FLAVANOIDS_ATT:
				return this.flavanoids;

			case WineInfo.NONFLAVANOIDS_PHENOLS_ATT:
				return this.nonflavanoidPhenols;

			case WineInfo.PROANTHOCYANINS_ATT:
				return this.proanthocyanins;

			case WineInfo.COLOR_INTENSITY_ATT:
				return this.colorIntensity;

			case WineInfo.HUE__ATT:
				return this.hue;

			case WineInfo.OD280_ATT:
				return this.OD280;

			case WineInfo.PROLINE_ATT:
				return this.proline;

			case WineInfo.CUSTOMER_SEGMENT_ATT:
				return this.customerSegment;

			default:
				return -1;
		}
	}

	public static ArrayList<WineInfo> getWineInfoList () {
		return WineInfo.infoList;
	}

	// ===========================================================================================
	/*
	 * 		cleanup
	 *
	 * */
	public static void releaseWineInfoList () {
		WineInfo.infoList = null;
	}

}
