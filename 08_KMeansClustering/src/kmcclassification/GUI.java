// package declaration
package kmcclassification;

// import statement(s)
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// class definition
public class GUI {
	// *******************************************************************************************
	// attribute(s)
    private Stage stage;
    private Scene scene;
    private Group root;

    // alterable/modifiable components
    private TextArea outputTextArea;
    private Button runButton;
    private Button resetButton;
	private ChoiceBox selectAttBox1;
	private ChoiceBox selectAttBox2;
	private ComboBox nInputBox;
	private ScatterChart scatterPlot;

	// data variables
    private ArrayList<Centroid> outputCentroids;
    private KMCClassifier kMeansClusteringProgram;

    // flag (for allowing the program to be ran again)
    private boolean isAllowedToRun;

    public final static int WINDOW_WIDTH = 1200;
    public final static int WINDOW_HEIGHT = 565;


	// *******************************************************************************************
	// constructor(s)
	// ===========================================================================================
	public GUI (Stage stage) {
        // initialization of necessary attribute(s)
        this.stage = stage;
        this.root = new Group();
        this.scene = new Scene(this.root, GUI.WINDOW_WIDTH, GUI.WINDOW_HEIGHT, Color.web("#64778d"));

        // set-up GUI components
        this.setInItElements();

        // handle events
        this.setEventHandlers();

        // stage configurations
        this.stage.setTitle("K-Means Clustering Classification");
        this.stage.setResizable(false);
        this.stage.setScene(this.scene);
        this.stage.show();
	}


	// *******************************************************************************************
	// method(s)

	// ===========================================================================================
	/*
	 * 		Designing/decorating the GUI
	 *
	 * */
	private void setInItElements() {
		this.isAllowedToRun = true;
		this.setTexts();
		this.initializeTextArea();
		this.initializeDropdownButtons();
		this.initializeButtons();
		this.initializeScatterPlot();
	}


	// ===========================================================================================
	/*
	 * 		Creating Text objects for the GUI
	 *
	 * */
	private void setTexts() {
		// text for scatter plot's title
        Text scatterPlotTitle = new Text("Kmeans Scatter Plot");

        // position
        scatterPlotTitle.setTranslateX(580);
        scatterPlotTitle.setTranslateY(45);

        // font color
        scatterPlotTitle.setFill(Color.WHITE);

        // font size
        scatterPlotTitle.setFont(Font.font(20));

        // appending the text object to the root node
        this.root.getChildren().add(scatterPlotTitle);

        // =======================================================================================
        // text for the output text area's title
        Text textAreaTitle = new Text("Centroids \n      &\n Clusters");

        // position
        textAreaTitle.setTranslateX(25);
        textAreaTitle.setTranslateY(345);

        // font color
        textAreaTitle.setFill(Color.WHITE);

     	// font size
        textAreaTitle.setFont(Font.font(20));

        // appending the text object to the root node
        this.root.getChildren().add(textAreaTitle);

        // =======================================================================================
        /*
         * 		Text for select attributes 1 & 2 dropdown buttons
         * */
        Text selectAttText1 = new Text("Select Attributes 1");

        // position
        selectAttText1.setTranslateX(25);
        selectAttText1.setTranslateY(33);

        // font color
        selectAttText1.setFill(Color.WHITE);

        // font size
        selectAttText1.setFont(Font.font(20));

        // appending the text object to the root node
        this.root.getChildren().add(selectAttText1);

        // =======================================================================================
        Text selectAttText2 = new Text("Select Attributes 2");

        // position
        selectAttText2.setTranslateX(25);
        selectAttText2.setTranslateY(78);

        // font color
        selectAttText2.setFill(Color.WHITE);

        // font size
        selectAttText2.setFont(Font.font(20));

        // appending the text object to the root node
        this.root.getChildren().add(selectAttText2);

        // =======================================================================================
        // text for the combo box dropdown button for asking the user the value for n
        Text nClustersText = new Text("Enter n clusters");

        // position
        nClustersText.setTranslateX(50);
        nClustersText.setTranslateY(123);

        // font color
        nClustersText.setFill(Color.WHITE);

        // font size
        nClustersText.setFont(Font.font(20));

        // appending the text object to the root node
        this.root.getChildren().add(nClustersText);
	}


	// ===========================================================================================
	/*
	 * 		Creating TextArea object for the GUI
	 *
	 * */
	private void initializeTextArea() {
		TextArea textArea = new TextArea();
		textArea.setWrapText(true); 				// so there won't be a horizontal scroll bar
		textArea.setEditable(false);				// so text inside can't be edited by the user

		// initial text
		textArea.setText("Don't forget to RESET the program first if you want to run it again!");
		// position
		textArea.setTranslateX(135);
		textArea.setTranslateY(145);

		// size
		textArea.setPrefSize(410, 410);

		// appending the TextArea object to the root node
		this.root.getChildren().add(textArea);

		this.outputTextArea = textArea;

		// =======================================================================================
        // vertical line divider decoration
        Line verticalLineDecorator = new Line(560, 10, 560, GUI.WINDOW_HEIGHT-10);
        verticalLineDecorator.setStroke(Color.web("#d6d6d6")); // Set the line color

        // appending the Line object to the root node
        this.root.getChildren().add(verticalLineDecorator);
	}


	// ===========================================================================================
	/*
	 * 		Creating dropdown buttons (ChoiceBox/ComboBox object/s)  for the GUI
	 *
	 * */
	private void initializeDropdownButtons() {
		// =======================================================================================
        /*
         *		for selecting attributes 1 & 2
         *
         * */
        // list of choices
        ArrayList<String> choices = new ArrayList<>();
        choices.add(WineInfo.ALCHOHOL_ATT);
        choices.add(WineInfo.MALIC_ACID_ATT);
        choices.add(WineInfo.ASH_ATT);
        choices.add(WineInfo.ASH_ALCANITY_ATT);
        choices.add(WineInfo.MAGNESIUM_ATT);
        choices.add(WineInfo.TOTAL_PHENOLS_ATT);
        choices.add(WineInfo.FLAVANOIDS_ATT);
        choices.add(WineInfo.NONFLAVANOIDS_PHENOLS_ATT);
        choices.add(WineInfo.PROANTHOCYANINS_ATT);
        choices.add(WineInfo.COLOR_INTENSITY_ATT);
        choices.add(WineInfo.HUE__ATT);
        choices.add(WineInfo.OD280_ATT);
        choices.add(WineInfo.PROLINE_ATT);
        choices.add(WineInfo.CUSTOMER_SEGMENT_ATT);

        // =======================================================================================
        // ChoiceBox object for selecting attributes 1
        ChoiceBox<String> choiceBox1 = new ChoiceBox<>();

        // adding choices to the available dropdown options
        choiceBox1.getItems().addAll(choices);

        // position
        choiceBox1.setTranslateX(195);
        choiceBox1.setTranslateY(10);

        // size
        choiceBox1.setPrefWidth(195);
        choiceBox1.setMaxHeight(1);

        // appending the ChoiceBox object to the root node
        this.root.getChildren().add(choiceBox1);

        // update the class reference variable for the instantiated object
        this.selectAttBox1 = choiceBox1;

        // =======================================================================================
        // ChoiceBox object for selecting attributes 2
        ChoiceBox<String> choiceBox2 = new ChoiceBox<>();

        // adding choices to the available dropdown options
        choiceBox2.getItems().addAll(choices);

        // position
        choiceBox2.setTranslateX(195);
        choiceBox2.setTranslateY(55);

        // size
        choiceBox2.setPrefWidth(195);
        choiceBox2.setMaxHeight(1);

        // appending the ChoiceBox object to the root node
        this.root.getChildren().add(choiceBox2);

        // update the class reference variable for the instantiated object
        this.selectAttBox2 = choiceBox2;

        // =======================================================================================
        // ComboBox object for entering the value of n (n-clusters)
        ComboBox<Integer> comboBox = new ComboBox<>();
        for (int i = 0; i <= 178; i++) {
            comboBox.getItems().add(i);
        }

        // Set an initial value (optional)
        comboBox.setEditable(true);

        // position
        comboBox.setTranslateX(195);
        comboBox.setTranslateY(100);

        // size
        comboBox.setPrefWidth(195);
        comboBox.setMaxHeight(1);

        // reminder
        comboBox.setPromptText("n must be <= 10");

        // appending the ComboBox object to the root node
        this.root.getChildren().add(comboBox);

        // update the class reference variable for the instantiated object
        this.nInputBox = comboBox;
	}


	// ===========================================================================================
	/*
	 * 		Creating Button objects for the GUI
	 * 		(Buttons for running and resetting the program)
	 *
	 * */
	private void initializeButtons() {
		// run button
        Button runBtn = new Button("RUN");

        // position
        runBtn.setTranslateX(430);
        runBtn.setTranslateY(30);

        // size
        runBtn.setMinWidth(90);
        runBtn.setMinHeight(20);

        // appending the Button object to the root node
        this.root.getChildren().add(runBtn);

        // update the class reference variable for the instantiated object
        this.runButton = runBtn;

        // =======================================================================================
		// reset button
        Button resetBtn = new Button("RESET");

        // position
        resetBtn.setTranslateX(430);
        resetBtn.setTranslateY(80);

        // size
        resetBtn.setMinWidth(90);
        resetBtn.setMinHeight(20);

        // appending the Button object to the root node
        this.root.getChildren().add(resetBtn);

        // update the class reference variable for the instantiated object
        this.resetButton = resetBtn;
	}


	// ===========================================================================================
	/*
	 * 		Creating a scatter plot (ScatterChart object) for the GUI
	 *
	 * */
	private void initializeScatterPlot() {
		// =======================================================================================
		// axes for the scatter plot
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X-axis");
        yAxis.setLabel("Y-axis");

        // instantiation of the scatter plot (ScatterChart object)
        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);

        // <https://www.tutorialspoint.com/how-to-change-the-color-and-font-of-the-tick-marks-in-a-javafx-xy-chart>
        // label and text colors
        xAxis.setTickLabelFill(Color.WHITE);
        yAxis.setTickLabelFill(Color.WHITE);
        xAxis.lookup(".axis-label").setStyle("-fx-text-fill: white;");
        yAxis.lookup(".axis-label").setStyle("-fx-text-fill: white;");

        // position
        scatterChart.setTranslateX(580);
        scatterChart.setTranslateY(60);

		// size
        scatterChart.setPrefSize(600, 480);

        // appending the ScatterChart object to the root node
        this.root.getChildren().add(scatterChart);

        // update the class reference variable for the instantiated object
        this.scatterPlot = scatterChart;
	}


	// ===========================================================================================
	/*
	 * 		Updating the contents of the scatter plot
	 *
	 * */
	@SuppressWarnings("unchecked")
	private void updateScatterPlot(String selectedAttributes1, String selectedAttributes2) {
		// get the data needed to be displayed in the scatter plot (output from the K-means clustering algorithm)
		this.outputCentroids = this.kMeansClusteringProgram.getCentroids();

		// update the label names for the x and y axes of the scatter plot with the names of the selected attributes
		this.scatterPlot.getXAxis().setLabel(selectedAttributes1);
		this.scatterPlot.getYAxis().setLabel(selectedAttributes2);

		// loop through the output data (centroids)
		// get the clustered data in each centroid (selected attributes data classified by the cluster specified by each centroid)
        for (Centroid centroid : this.outputCentroids) {
        	// new series = each clustered data
            XYChart.Series<Number, Number> cluster = new XYChart.Series<>();
            cluster.setName("Cluster " + centroid.getClusterNumber());

            // XYChart = each datum stored in the clustered data
            for (SelectedWineAttributes dataPoint : centroid.getClassifiedList()) {
            	cluster.getData().add(new XYChart.Data<>(dataPoint.getX(), dataPoint.getY()));
            }

            // add the cluster/series the scatter plot
            this.scatterPlot.getData().add(cluster);
        }

        /*
         * Making the scatter plot look more clean and presentable
         * 		--> 1. data points being clamped at the right side of the scatter plot (because of the values of x)
         *		--> 2. legends (at the bottom) consuming space at the screen
         * */

        // for fixing issue 1
        // we'll just set a lower and upper bounds to our scatter plot's x-axis in the output GUI
	    // find the minimum and maximum x-values from our output data
	    double minX = Double.MAX_VALUE;
	    double maxX = Double.MIN_VALUE;

	    for (Centroid centroid : this.outputCentroids) {
	        for (SelectedWineAttributes dataPoint : centroid.getClassifiedList()) {
	        	// get the x-values (since we're dealing with the issue on the scatter plot's x-axis only)
	            double xValue = dataPoint.getX();

	            // update values of the bounds variables
	            minX = Math.min(minX, xValue);
	            maxX = Math.max(maxX, xValue);
	        }
	    }

	    // first disable the scatter plot's built-in auto-ranging setup for the bounds of its x-axis
	    this.scatterPlot.getXAxis().setAutoRanging(false);

	    // setting the lower and upper bounds of the scatter plot's x-axis
	    ((ValueAxis<Number>) this.scatterPlot.getXAxis()).setLowerBound(minX-1);
	    ((ValueAxis<Number>) this.scatterPlot.getXAxis()).setUpperBound(maxX+1);

	    // for fixing issue 2
        // disable legends at the bottom of the scatter plot
        this.scatterPlot.setLegendVisible(false);

        // cleanup resources used by the k-means clustering program
        this.kMeansClusteringProgram.cleanup();
	}


	// ===========================================================================================
	/*
	 * 		Event Handlers
	 *
	 * */
	private void setEventHandlers () {
		this.runButton.setOnMouseClicked( new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				outputTextArea.setText("RUN CLICKED");
				if (isAllowedToRun)
				runKMeansClusteringAlgorithm();
				else outputTextArea.setText("RESET the GUI first!");
			}
		});


		this.resetButton.setOnMouseClicked( new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				outputTextArea.setText("RESET CLICKED");
				reset();
			}
		});
	}


	// ===========================================================================================
	/*
	 * 		Executes the K-means Clustering Classification Algorithm
	 *
	 * */
	private void runKMeansClusteringAlgorithm () {
		// check first if input boxes have input values
		if (this.selectAttBox1.getValue() == null || this.selectAttBox2.getValue() == null || this.nInputBox.getValue() == null) {
			this.outputTextArea.setText("CANNOT RUN THE PROGRAM! "
					+ "\n" + "Ensure that all inputs are not empty."
			);

			return;
		}

		// check if the entered value of n is valid
		int nInputValue = 0;
		String nInput = (String) this.nInputBox.getValue();
        try {
            nInputValue = Integer.parseInt(nInput);

            // check if the parsed value of n is within the valid range
            if (nInputValue <= 0 || nInputValue > 10) {
            	this.outputTextArea.setText("Invalid Input!"
            			+ "\n" + "n must be greater than 0 but less than or equal to 10."
            	);
            	return;
            }
        } catch (NumberFormatException e) {
        	this.outputTextArea.setText("Invalid input format!");
            return;
        }

        // if no error and the inputs are valid
        String selectedAttributes1 = (String) this.selectAttBox1.getValue();
        String selectedAttributes2 = (String) this.selectAttBox2.getValue();
        this.kMeansClusteringProgram = new KMCClassifier(selectedAttributes1, selectedAttributes2, nInputValue);

        // update the GUI
        this.outputTextArea.setText(kMeansClusteringProgram.getOutputText());
        this.updateScatterPlot(selectedAttributes1, selectedAttributes2);

        // do not allow the user to run the program again until he/she clicks the RESET button first
        this.isAllowedToRun = false;
	}


	// ===========================================================================================
	/*
	 * 		Resets the GUI
	 *
	 * */
	@SuppressWarnings("unused")
	private void reset() {
		// just create a new instance of a GUI object
		GUI newWindow = new GUI(this.stage);
	}

} // end of the class definition
