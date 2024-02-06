// package declaration
package bow;

//import statement(s)
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

// class definition
public class BOW {
	// *******************************************************************************************
	// attribute(s)
	private Stage stage;
	private Scene scene;

	private Group root;
	private Canvas canvas;

	private Button fileSelector;
	private Map<String, Integer> dictionary;

	public final static String ALPHANEUMERIC_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
	private List<Character> alphaneumericList;

	// screen dimensions
	public final static int WINDOW_WIDTH = 400;
	public final static int WINDOW_HEIGHT = 600;

	// *******************************************************************************************
	// constructor(s)
	public BOW(Stage stage) {
		// initantiation of necessary variables
		this.stage = stage;

		this.root = new Group();
		this.scene = new Scene(this.root, BOW.WINDOW_WIDTH, BOW.WINDOW_HEIGHT, Color.LIGHTBLUE);

		this.setInItElements();

		// stage configurations
		this.stage.setTitle("Bag-Of-Words");
		this.stage.setResizable(false);

		this.stage.setScene(this.scene);
		this.stage.show();
	}

	// *******************************************************************************************
	// method(s)
	// sets up nodes inside the scene
	private void setInItElements() {
		// set background for the canvas
		this.canvas = new Canvas(BOW.WINDOW_WIDTH, BOW.WINDOW_HEIGHT);
		this.root.getChildren().add(this.canvas);

		// list of alphaneumeric characters
		this.alphaneumericList = new ArrayList<Character>();
		for (int i=0; i<BOW.ALPHANEUMERIC_CHARACTERS.length(); i++) {
			this.alphaneumericList.add(BOW.ALPHANEUMERIC_CHARACTERS.charAt(i));
		}

		// initialize our dictionary
		this.dictionary = new TreeMap<>();

		this.readInputFile(null);

		// setup buttons
		this.setInItButtons();

		// setup texts
		this.setInItTexts();

		// setup output table
		this.setInItTable();
	}

	// ===========================================================================================
	private void setInItTexts() {
		// for title
		Label titleLabel = new Label("BAG-OF-WORDS");
		titleLabel.setFont(new Font("Arial", 30));
		titleLabel.setAlignment(Pos.CENTER_RIGHT);
		titleLabel.setTranslateX(BOW.WINDOW_WIDTH/2 - 120);
		titleLabel.setTranslateY((BOW.WINDOW_HEIGHT/20)*1 -18);

		// for file selector
		Label fileSelectorLabel = new Label("Select File");
		fileSelectorLabel.setAlignment(Pos.CENTER_RIGHT);
		fileSelectorLabel.setTranslateX(BOW.WINDOW_WIDTH/2 - 80);
		fileSelectorLabel.setTranslateY((BOW.WINDOW_HEIGHT/20)*2);

		// for dictionary size
		Label dictionarySizeLabel = new Label("Dictionary Size");
		dictionarySizeLabel.setAlignment(Pos.CENTER_RIGHT);
		dictionarySizeLabel.setTranslateX(BOW.WINDOW_WIDTH/2 - 112);
		dictionarySizeLabel.setTranslateY((BOW.WINDOW_HEIGHT/20)*3 + 10);

		TextField dictionarySizeTxtField = new TextField();
		dictionarySizeTxtField.setAlignment(Pos.CENTER);
		dictionarySizeTxtField.setEditable(false);
		dictionarySizeTxtField.setMaxWidth(100);
		dictionarySizeTxtField.setTranslateX(BOW.WINDOW_WIDTH/2);
		dictionarySizeTxtField.setTranslateY((BOW.WINDOW_HEIGHT/20)*3+5);

		String dictionarySize = "" + this.dictionary.size();
		dictionarySizeTxtField.setText(dictionarySize);

		// for word count
		Label wordCountLabel = new Label("Total Words");
		wordCountLabel.setAlignment(Pos.CENTER_RIGHT);
		wordCountLabel.setTranslateX(BOW.WINDOW_WIDTH/2 - 95);
		wordCountLabel.setTranslateY((BOW.WINDOW_HEIGHT/20)*4 + 20);

		TextField wordCountTxtField = new TextField();
		wordCountTxtField.setAlignment(Pos.CENTER);
		wordCountTxtField.setEditable(false);
		wordCountTxtField.setMaxWidth(100);
		wordCountTxtField.setTranslateX(BOW.WINDOW_WIDTH/2);
		wordCountTxtField.setTranslateY((BOW.WINDOW_HEIGHT/20)*4+15);

		int wordCount = 0;
		for (Integer frequency : this.dictionary.values()) {
		    wordCount += frequency;
		}

		String wordCountText = "" + wordCount;
		wordCountTxtField.setText(wordCountText);


		this.root.getChildren().addAll(titleLabel, fileSelectorLabel, dictionarySizeLabel, wordCountLabel, dictionarySizeTxtField, wordCountTxtField);
	}

	// ===========================================================================================
	private void setInItButtons() {
		// file Selector button
		this.fileSelector = new Button("Browse...");
		this.fileSelector.setMinWidth(100);
		this.fileSelector.setMinHeight(10);
		this.fileSelector.setMaxHeight(30);
		this.fileSelector.setTranslateX(BOW.WINDOW_WIDTH/2);
		this.fileSelector.setTranslateY((BOW.WINDOW_HEIGHT/20)*2-5);
		this.fileSelector.setStyle(
				"-fx-background-radius: 5em; "
		);
		this.root.getChildren().add(this.fileSelector);

		this.setButtonHandlers();

	}

	// ===========================================================================================
	private void setButtonHandlers() {
		this.fileSelector.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				selectFile();
			}
		});
	}

	// ===========================================================================================
	// Reads input from a file
	private void readInputFile(File passedFile) {
		File file;
		if (passedFile == null) {
			// load the file from the current project directory
			// reference: reading file from the current directory <https://stackoverflow.com/questions/1480398/java-reading-a-file-from-current-directory>
			String fileDirectory = System.getProperty("user.dir");
			fileDirectory += "\\src\\files\\" + "sample.txt";
			file = new File(fileDirectory);
		} else {
			file = passedFile;
		}


		// Will store the contents of the file
		String fileContent = "";

		// reading from file contents with a Scanner object <https://www.youtube.com/watch?v=lHFlAYaNfdo&t=265s>
		// Use the Scanner class to read each line in the file
		try {
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				fileContent += scanner.nextLine();
				fileContent += (" \n");
			}

			// close the scanner
			scanner.close();

		} catch (FileNotFoundException e) {
			// catch block
			e.printStackTrace();
		}

		// for checking
		System.out.println("Read Input File:");
		System.out.println(fileContent);

		this.formatString(fileContent);
	}

	// ===========================================================================================
	// selects/opens a file for reading inputs
	private void selectFile() {
		// instantiate a FileChooser object
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open New Input File");

		// set default directory
		String fileDirectory = System.getProperty("user.dir");
		fileDirectory += "\\src\\files\\";
		fileChooser.setInitialDirectory(new File(fileDirectory));

		// adds an extension filter
		ExtensionFilter extensionFilter = new ExtensionFilter("Text Files", "*.txt");
		fileChooser.getExtensionFilters().add(extensionFilter);

		// opens the dialog box
		File selectedFile = fileChooser.showOpenDialog(this.stage);

		// has read a file successfully
		if (selectedFile != null) {
			this.readInputFile(selectedFile);

			this.setInItButtons();
			this.setInItTexts();
			this.setInItTable();
		}
	}

	// ===========================================================================================
	// format Strings
	private void formatString(String fileContent) {
		// splitting with more than whitespace <https://stackoverflow.com/questions/4861803/how-do-i-make-java-ignore-the-number-of-spaces-in-a-string-when-splitting>
		String[] words = fileContent.split(" +");

		ArrayList<String> formattedWords = new ArrayList<String>();

		// for checking
		System.out.println("\nSplit Strings:");
		// remove all non-alphanumeric characters
		for (int i=0; i<words.length; i++) {
			String newStr = ""; 				// will store the formatted string
			Boolean hasAccentedChar = false;	// for checking if a character is accented or not

			// traverse through each character in the word/string
			for (int j=0; j<words[i].length(); j++) {
				// character to check
				Character c = words[i].toLowerCase().charAt(j);

				// check if the character being read is alphaneumeric or not
				if (this.alphaneumericList.contains(c)) {
					newStr += c;
				} else {
					// if the character is not in the alphaneumeric list but still a letter (accented chars)
					if (Character.isLetter(c)) hasAccentedChar = true;
				}

				// for checking
				System.out.print(words[i].toLowerCase().charAt(j));
			}
			// add the new formatted word to the list of words formatted if it is not empty or does not contain an accented character
			if (newStr.isEmpty() == false && hasAccentedChar == false) formattedWords.add(newStr);

			// for checking
			System.out.println();
		}

		// for checking
		System.out.println("Formatted Strings:");
		for (String word: formattedWords) {
			System.out.println(word);
		}

		this.convertWordsToDictionary(formattedWords);
	}

	// ===========================================================================================
	// convert a list words into a dictionary <https://stackoverflow.com/questions/13543457/how-do-you-create-a-dictionary-in-java>
	private void convertWordsToDictionary(ArrayList<String> wordList) {
		// create a dictionary
		Map<String, Integer> dict = new TreeMap<>();

		// traversse through the word list
		for (String word: wordList) {
			// check if the dictionary already contains a certain word from the list
			// if it is just increment its value (frequency) in the dicionary
			// else, add the new word in the dictionary
			if (dict.containsKey(word)== true) {
				Integer value = dict.get(word) + 1;
				dict.replace(word, value);
			} else {
				dict.put(word, 1);
			}
		}


		// update the dictionary local variable
		this.dictionary = dict;

		// export the resulting bag-of-words representation into a file
		this.exportResults(dict);
	}


	// ===========================================================================================
	// outputs the resulting bag-of-words representation
	void exportResults (Map<String, Integer> dict) {
		// load the file from the current project directory
		// reference: reading file from the current directory <https://stackoverflow.com/questions/1480398/java-reading-a-file-from-current-directory>
		String fileDirectory = System.getProperty("user.dir");
		fileDirectory += "\\src\\files\\output.txt";
		File file = new File(fileDirectory);

		String fileContent = "";

		int wordCount = 0;
		for (Integer frequency : dict.values()) {
		    wordCount += frequency;
		}

		fileContent += "Dictionary Size: " + dict.size() + "\n";
		fileContent += "Total Number of Words: " + wordCount + "\n";

		for (Entry<String, Integer> entry : dict.entrySet()) {
		    String key = entry.getKey();
		    Integer value = entry.getValue();

		    fileContent += key + " " + value + "\n";

		}


		try {
			FileWriter writer = new FileWriter(file);
			writer.write(fileContent);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	    // for checking
		System.out.println("\n\nOutput:");
	    System.out.println(fileContent);
	}

	// ===========================================================================================
	// sets up table for the resulting bag-of-words representation
	// reference: <https://stackoverflow.com/questions/50231903/javafx-fill-tableview-with-treemap-data>
	// reference: <https://www.youtube.com/watch?v=vego72w5kPU>
	// reference: <https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TableView.html?external_link=true>
	@SuppressWarnings("unchecked")
	private void setInItTable() {
	    // instantiate an ObservableList to hold the data from our local variable dictionary that will be displayed in a TableView
	    ObservableList<Map.Entry<String, Integer>> dictionaryData = FXCollections.observableArrayList(this.dictionary.entrySet());

	    // instantiate a TableView and set the ObservableList dictionaryDate as its data source
	    TableView<Map.Entry<String, Integer>> tableView = new TableView<>(dictionaryData);

	    // create a column for the words in the dictionary
	    TableColumn<Map.Entry<String, Integer>, String> wordColumn = new TableColumn<>("Word");
	    wordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));

	    // create a column for the frequency of each word in the dictionary
	    TableColumn<Map.Entry<String, Integer>, Integer> frequencyColumn = new TableColumn<>("Frequency");
	    frequencyColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue()));

	    // add the created columns to our TableView
	    tableView.getColumns().addAll(wordColumn, frequencyColumn);

	    // set TableView properties
	    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    tableView.setEditable(false);
	    tableView.setMaxSize(400, 400);

	    tableView.setTranslateX(80);
	    tableView.setTranslateY((BOW.WINDOW_HEIGHT / 20) * 6);

	    // add the created TableView to our root node
	    this.root.getChildren().add(tableView);
	}

}



