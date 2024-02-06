// package declaration
package bow;

import java.io.BufferedReader;
//import statement(s)
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
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


    private final static String CLASSIFY_TYPE = "classify";
    private final static String HAM_TYPE = "ham";
    private final static String SPAM_TYPE = "spam";


    // buttons
    private Button classifyDirectorySelector;
    private Button hamDirectorySelector;
    private Button spamDirectorySelector;
    private Button filterButton;

    private Map<String, Map<String, Integer>> classifyMegaDictionary;
    private Map<String, Integer> classifyDictionary;
    private Map<String, Integer> spamDictionary;
    private Map<String, Integer> hamDictionary;

    private TableView classifyTableView;

    private double classifyTotalWords = 0;
    private double hamTotalWords = 0;
    private double spamTotalWords = 0;


    private double classifyFileCount = 0;
    private double hamFileCount = 0;
    private double spamFileCount = 0;
    private double totalFileCount = 0;

    // probability
    private BigDecimal p_spam;
    private BigDecimal p_ham;
    private final static BigDecimal THRESHOLD = BigDecimal.valueOf(0.5);
//    private BigDecimal p_messageGivenSpam;
//    private BigDecimal p_messageGivenHam;
//    private BigDecimal p_message;
//
//    private BigDecimal p_spamGivenMessage;

    public final static String ALPHANEUMERIC_CHARACTERS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
    private List<Character> alphaneumericList;

    // screen dimensions
    public final static int WINDOW_WIDTH = 1200;
    public final static int WINDOW_HEIGHT = 600;

    private final static int SPAM_COLUMN = (BOW.WINDOW_WIDTH/3)*0;
    private final static int HAM_COLUMN = (BOW.WINDOW_WIDTH/3)*1;
    private final static int CLASSIFY_COLUMN = (BOW.WINDOW_WIDTH/3)*2;


    private final static int COLUMN_WIDTH = BOW.WINDOW_WIDTH/3;
    // *******************************************************************************************
    // constructor(s)
    public BOW(Stage stage) {
        // initantiation of necessary variables
        this.stage = stage;

        this.root = new Group();
        this.scene = new Scene(this.root, BOW.WINDOW_WIDTH, BOW.WINDOW_HEIGHT, Color.LIGHTBLUE);

        this.setInItElements();

        // stage configurations
        this.stage.setTitle("Spam Filtering");
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

        // initialize our dictionaries
        this.classifyMegaDictionary = new TreeMap<>();
        this.classifyDictionary = new TreeMap<>();
        this.spamDictionary = new TreeMap<>();
        this.hamDictionary = new TreeMap<>();

        this.readInputFile(null, null, null, null);

        // set up columns
        this.setColumns();

        // set up tableview for output
        this.classifyTableView = new TableView<OutputData>();
        this.setOutputTable(null);
    }


    private void setColumns() {
        this.setSpamColumn();
        this.setHamColumn();
        this.setClassifyColumn();
    }


    // *******************************************************************************************
    // ===========================================================================================
    private void setSpamColumn() {
        this.setSpamButton();
        this.setSpamTexts();
        this.setBOWTable(this.spamDictionary, BOW.SPAM_TYPE);
    }


    // ===========================================================================================
    // sets up texts for the spam column
    private void setSpamTexts() {
        // for word count
        Label wordCountLabel = new Label("Total Words");
        wordCountLabel.setAlignment(Pos.CENTER_RIGHT);
        wordCountLabel.setTranslateX(BOW.COLUMN_WIDTH/2 - 95);
        wordCountLabel.setTranslateY((BOW.WINDOW_HEIGHT/20)*18 + 5);
        this.root.getChildren().add(wordCountLabel);

        TextField wordCountTxtField = new TextField();
        wordCountTxtField.setAlignment(Pos.CENTER);
        wordCountTxtField.setEditable(false);
        wordCountTxtField.setMaxWidth(100);
        wordCountTxtField.setTranslateX(BOW.COLUMN_WIDTH/2);
        wordCountTxtField.setTranslateY((BOW.WINDOW_HEIGHT/20)*18 + 0);

        int wordCount = 0;
        for (Integer frequency : this.spamDictionary.values()) {
            wordCount += frequency;
        }

        this.spamTotalWords = (double) wordCount; // update word count of spam

        String wordCountText = "" + wordCount;
        wordCountTxtField.setText(wordCountText);
        this.root.getChildren().add(wordCountTxtField);

    }


    // ===========================================================================================
    private void setSpamButton() {
        // file Selector button
        this.spamDirectorySelector = new Button("Select Spam Folder");
        this.spamDirectorySelector.setMinWidth(250);
        this.spamDirectorySelector.setMinHeight(10);
        this.spamDirectorySelector.setMaxHeight(30);
        this.spamDirectorySelector.setTranslateX(BOW.SPAM_COLUMN+75);
        this.spamDirectorySelector.setTranslateY((BOW.WINDOW_HEIGHT/20)*1 -5);
        this.spamDirectorySelector.setStyle(
                "-fx-background-radius: 5em; "
        );
        this.root.getChildren().add(this.spamDirectorySelector);

        this.setSpamButtonHandler();

    }

    // ===========================================================================================
    private void setSpamButtonHandler() {
        this.spamDirectorySelector.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                spamDictionary = new TreeMap<>();
                selectFolder(spamDictionary, BOW.SPAM_TYPE);
            }
        });
    }


    // *******************************************************************************************
    // ===========================================================================================
    private void setHamColumn() {
        this.setHamButton();
        this.setHamTexts();
        this.setBOWTable(this.hamDictionary, BOW.HAM_TYPE);
    }


    // ===========================================================================================
    // sets up texts for the spam column
    private void setHamTexts() {
        // for word count
        Label wordCountLabel = new Label("Total Words");
        wordCountLabel.setAlignment(Pos.CENTER_RIGHT);
        wordCountLabel.setTranslateX(BOW.COLUMN_WIDTH/2 - 95 + BOW.COLUMN_WIDTH);
        wordCountLabel.setTranslateY((BOW.WINDOW_HEIGHT/20)*18 + 5);
        this.root.getChildren().add(wordCountLabel);

        TextField wordCountTxtField = new TextField();
        wordCountTxtField.setAlignment(Pos.CENTER);
        wordCountTxtField.setEditable(false);
        wordCountTxtField.setMaxWidth(100);
        wordCountTxtField.setTranslateX(BOW.COLUMN_WIDTH/2 + BOW.COLUMN_WIDTH);
        wordCountTxtField.setTranslateY((BOW.WINDOW_HEIGHT/20)*18 + 0);

        int wordCount = 0;
        for (Integer frequency : this.hamDictionary.values()) {
            wordCount += frequency;
        }

        this.hamTotalWords = (double) wordCount; // update word count of ham

        String wordCountText = "" + wordCount;
        wordCountTxtField.setText(wordCountText);
        this.root.getChildren().add(wordCountTxtField);

    }


    // ===========================================================================================
    private void setHamButton() {
        // file Selector button
        this.hamDirectorySelector = new Button("Select Ham Folder");
        this.hamDirectorySelector.setMinWidth(250);
        this.hamDirectorySelector.setMinHeight(10);
        this.hamDirectorySelector.setMaxHeight(30);
        this.hamDirectorySelector.setTranslateX(BOW.HAM_COLUMN+75);
        this.hamDirectorySelector.setTranslateY((BOW.WINDOW_HEIGHT/20)*1 -5);
        this.hamDirectorySelector.setStyle(
                "-fx-background-radius: 5em; "
        );
        this.root.getChildren().add(this.hamDirectorySelector);

        this.setHamButtonHandler();

    }

    // ===========================================================================================
    private void setHamButtonHandler() {
        this.hamDirectorySelector.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                hamDictionary = new TreeMap<>();
                selectFolder(hamDictionary, BOW.HAM_TYPE);
            }
        });
    }


    // ===========================================================================================
    // sets up table for the resulting bag-of-words representation
    // reference: <https://stackoverflow.com/questions/50231903/javafx-fill-tableview-with-treemap-data>
    // reference: <https://www.youtube.com/watch?v=vego72w5kPU>
    // reference: <https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TableView.html?external_link=true>
    @SuppressWarnings("unchecked")
    private void setBOWTable(Map<String, Integer> dictionary, String type) {
        // instantiate an ObservableList to hold the data from our local variable dictionary that will be displayed in a TableView
        ObservableList<Map.Entry<String, Integer>> dictionaryData = FXCollections.observableArrayList(dictionary.entrySet());

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
        if (type == BOW.SPAM_TYPE) {
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableView.setEditable(false);
            tableView.setMinHeight(450);
            tableView.setMinWidth(200);

            tableView.setTranslateX(80);
            tableView.setTranslateY((BOW.WINDOW_HEIGHT / 20) * 2 + 15);
        } else if (type == BOW.HAM_TYPE) {
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableView.setEditable(false);
            tableView.setMinHeight(450);
            tableView.setMinWidth(200);

            tableView.setTranslateX(80+BOW.HAM_COLUMN);
            tableView.setTranslateY((BOW.WINDOW_HEIGHT / 20) * 2 + 15);
        }


        // add the created TableView to our root node
        this.root.getChildren().add(tableView);
    }


    // *******************************************************************************************
    // ===========================================================================================
    // set up the column for classify data
    private void setClassifyColumn() {
        this.setClassifyButton();
        this.setClassifyTexts();
//		this.setBOWTable(this.classifyDictionary, BOW.CLASSIFY_TYPE);
    }


    // ===========================================================================================
    // sets up texts for the spam column
    private void setClassifyTexts() {
        // for dictionary size
        Label dictionarySizeLabel = new Label("Dictionary Size");
        dictionarySizeLabel.setAlignment(Pos.CENTER_RIGHT);
        dictionarySizeLabel.setTranslateX(BOW.COLUMN_WIDTH/2 + BOW.CLASSIFY_COLUMN - 112);
        dictionarySizeLabel.setTranslateY((BOW.WINDOW_HEIGHT/20)*0 + 20);
        this.root.getChildren().add(dictionarySizeLabel);

        TextField dictionarySizeTxtField = new TextField();
        dictionarySizeTxtField.setAlignment(Pos.CENTER);
        dictionarySizeTxtField.setEditable(false);
        dictionarySizeTxtField.setMaxWidth(100);
        dictionarySizeTxtField.setTranslateX(BOW.COLUMN_WIDTH/2 + BOW.CLASSIFY_COLUMN);
        dictionarySizeTxtField.setTranslateY((BOW.WINDOW_HEIGHT/20)*0 + 15);

        String dictionarySize = "" + this.classifyDictionary.size();
//		String dictionarySize = "";
//		int dictSize = 0;
//		for (Map<String, Integer> indivDict : this.classifyMegaDictionary.values()) {
//			dictSize += indivDict.size();
//		}
//		dictionarySize += dictSize;
        dictionarySizeTxtField.setText(dictionarySize);
        this.root.getChildren().add(dictionarySizeTxtField);

        // for word count
        Label wordCountLabel = new Label("Total Words");
        wordCountLabel.setAlignment(Pos.CENTER_RIGHT);
        wordCountLabel.setTranslateX(BOW.COLUMN_WIDTH/2 - 95 + BOW.CLASSIFY_COLUMN);
        wordCountLabel.setTranslateY((BOW.WINDOW_HEIGHT/20)*1 + 35);
        this.root.getChildren().add(wordCountLabel);

        TextField wordCountTxtField = new TextField();
        wordCountTxtField.setAlignment(Pos.CENTER);
        wordCountTxtField.setEditable(false);
        wordCountTxtField.setMaxWidth(100);
        wordCountTxtField.setTranslateX(BOW.COLUMN_WIDTH/2 + BOW.CLASSIFY_COLUMN);
        wordCountTxtField.setTranslateY((BOW.WINDOW_HEIGHT/20)*1 + 30);

        int wordCount = 0;
//		for ( Map<String, Integer> indivDict : this.classifyMegaDictionary.values()) {
//			for (Integer frequency : indivDict.values()) {
//				wordCount += frequency;
//			}
//		}
        for (Integer frequency : this.classifyDictionary.values()) {
            wordCount += frequency;
        }

        this.classifyTotalWords = (double) wordCount; // update word count of classify

        String wordCountText = "" + wordCount;
        wordCountTxtField.setText(wordCountText);
        this.root.getChildren().add(wordCountTxtField);
    }

    // ===========================================================================================
    private void setClassifyButton() {
        // file Selector button
        this.classifyDirectorySelector = new Button("Select Classify Folder");
        this.classifyDirectorySelector.setMinWidth(250);
        this.classifyDirectorySelector.setMinHeight(10);
        this.classifyDirectorySelector.setMaxHeight(30);
        this.classifyDirectorySelector.setTranslateX(BOW.CLASSIFY_COLUMN+75);
        this.classifyDirectorySelector.setTranslateY((BOW.WINDOW_HEIGHT/20)*4 - 15);
        this.classifyDirectorySelector.setStyle(
                "-fx-background-radius: 5em; "
        );
        this.root.getChildren().add(this.classifyDirectorySelector);

        // file Selector button
        this.filterButton = new Button("Filter");
        this.filterButton.setMinWidth(100);
        this.filterButton.setMinHeight(10);
        this.filterButton.setMaxHeight(30);
        this.filterButton.setTranslateX(BOW.CLASSIFY_COLUMN + 140);
        this.filterButton.setTranslateY((BOW.WINDOW_HEIGHT/20)*5 - 0);
        this.filterButton.setStyle(
                "-fx-background-radius: 5em; "
        );
        this.root.getChildren().add(this.filterButton);

        this.setClassifyButtonHandler();

    }

    // ===========================================================================================
    private void setClassifyButtonHandler() {
        this.classifyDirectorySelector.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                classifyDictionary = new TreeMap<>();
                classifyMegaDictionary = new TreeMap<>();
                selectFolder(classifyDictionary, BOW.CLASSIFY_TYPE);
            }
        });

        this.filterButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                performNaiveBayesClassifier();
            }
        });
    }


    // *******************************************************************************************
    // ===========================================================================================
    // open a folder for reading (classify folder)
    private void selectFolder(Map<String, Integer> dict, String type) {
        File folder = null; // will store the selected folder

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");

        // set default directory
        String fileDirectory = System.getProperty("user.dir");
        fileDirectory += "\\src\\data\\";
        directoryChooser.setInitialDirectory(new File(fileDirectory));


        File selectedDirectory = directoryChooser.showDialog(stage);


        if (selectedDirectory != null) {
             System.out.println(selectedDirectory.getAbsolutePath());
             folder = new File(selectedDirectory.getAbsolutePath());
             this.listFilesForFolder(folder, dict, type);
        }
    }

    // ===========================================================================================
    // creating
    // reference: <https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java>
    private void listFilesForFolder(File folder, Map<String, Integer> dict, String type) {
        if (type == BOW.HAM_TYPE)this.hamFileCount = 0;
        else if (type == BOW.SPAM_TYPE) this.spamFileCount = 0;
        else if (type == BOW.CLASSIFY_TYPE) this.classifyFileCount = 0;

        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry, dict, type);

            } else {
                String[] a =  fileEntry.getName().split("\\.");
                String fileName = a[0];
                System.out.println("Name of File being Read: " + fileName);

                this.readInputFile(fileEntry, dict, type, fileName);

                if (type == BOW.SPAM_TYPE) {
                    this.setSpamTexts();
                    this.setBOWTable(this.spamDictionary, BOW.SPAM_TYPE);
                    this.spamFileCount ++;
                } else if (type == BOW.HAM_TYPE) {
                    this.setHamTexts();
                    this.setBOWTable(this.hamDictionary, BOW.HAM_TYPE);
                    this.hamFileCount ++;
                } else if (type == BOW.CLASSIFY_TYPE) {
                    this.setClassifyTexts();
                    this.classifyFileCount ++;
                }

            }
        }
    }



    // ===========================================================================================
    // Reads input from a file
    private void readInputFile(File passedFile, Map<String, Integer> dict, String type, String fileName) {
        File file = null;
        if (passedFile == null) {
            // do nothing
        } else {
            file = passedFile;
        }

        if (passedFile != null) {
            // Will store the contents of the file
            String fileContent = "";

            // reading from file contents with a Scanner object <https://www.youtube.com/watch?v=lHFlAYaNfdo&t=265s>
            // Use the Scanner class to read each line in the file
            try {
                InputStreamReader fileInLatinMinus1 = new InputStreamReader(new FileInputStream(file), "8859_1");
                Scanner scanner = new Scanner(fileInLatinMinus1);

                while (scanner.hasNextLine()) {
                    fileContent += scanner.nextLine();

                    fileContent += (" \n");
                }

                // close the scanner
                scanner.close();

            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                // catch block
                e.printStackTrace();
            }

            // for checking
//			System.out.println("Read Input File:");
//			System.out.println(fileContent);


            this.formatString(fileContent, dict, type, fileName);
        }


    }



    // ===========================================================================================
    // format Strings
    private void formatString(String fileContent, Map<String, Integer> dict, String type, String fileName) {
        // splitting with more than whitespace <https://stackoverflow.com/questions/4861803/how-do-i-make-java-ignore-the-number-of-spaces-in-a-string-when-splitting>
        String[] words = fileContent.split("\\s+");

        ArrayList<String> formattedWords = new ArrayList<String>();

        // for checking
        // System.out.println("\nSplit Strings:");

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
                // System.out.print(words[i].toLowerCase().charAt(j));
            }
            // add the new formatted word to the list of words formatted if it is not empty or does not contain an accented character
            if (newStr.isEmpty() == false && hasAccentedChar == false) formattedWords.add(newStr);

            // for checking
            // System.out.println();
        }

        // for checking
//		System.out.println("Formatted Strings:");
//		for (String word: formattedWords) {
//			System.out.println(word);
//		}

        this.convertWordsToDictionary(formattedWords, dict, type, fileName);
    }

    // ===========================================================================================
    // convert a list words into a dictionary <https://stackoverflow.com/questions/13543457/how-do-you-create-a-dictionary-in-java>
    private void convertWordsToDictionary(ArrayList<String> wordList, Map<String, Integer> dict, String type, String fileName) {
        // check if the type of data to be passed came from the classify folder or not
        if (type == BOW.CLASSIFY_TYPE) {
            Map<String, Integer> indivDict = new TreeMap<>();

            // traversse through the word list
            for (String word: wordList) {
                // check if the dictionary already contains a certain word from the list
                // if it is just increment its value (frequency) in the dicionary
                // else, add the new word in the dictionary
                if (indivDict.containsKey(word)== true) {
                    Integer value = indivDict.get(word) + 1;
                    indivDict.replace(word, value);
                } else {
                    indivDict.put(word, 1);
                }
            }

            this.classifyMegaDictionary.put(fileName, indivDict);


        }


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

        // export the resulting bag-of-words representation into a file
//        this.exportResults(dict);
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
        System.out.println(fileContent);
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
//		System.out.println("\n\nOutput:");
//	    System.out.println(fileContent);
    }

    // ===========================================================================================
    private void performNaiveBayesClassifier() {
        if (this.spamDictionary.isEmpty() == false && this.hamDictionary.isEmpty() == false) {
            // for exporting results into a file
            String fileDirectory = System.getProperty("user.dir");
            fileDirectory += "\\src\\bow\\classify.out";
            File file = new File(fileDirectory);

            String fileContent = "";


            // start of computation
            this.p_spam = BigDecimal.ZERO;
            this.p_ham = BigDecimal.ZERO;

            this.totalFileCount = this.spamFileCount + this.hamFileCount;
            System.out.println(this.totalFileCount);

            BigDecimal spamFileCounter = BigDecimal.valueOf(this.spamFileCount);
            BigDecimal hamFileCounter = BigDecimal.valueOf(this.hamFileCount);
            BigDecimal totalFileCounter = BigDecimal.valueOf(this.totalFileCount);

            // calculate p_spam and p_ham with rounding to 4 decimal places
            this.p_spam = spamFileCounter.divide(totalFileCounter, 4, RoundingMode.HALF_UP);
            this.p_ham = hamFileCounter.divide(totalFileCounter, 4, RoundingMode.HALF_UP);
            System.out.println(this.p_spam.toString());
            System.out.println(this.p_ham.toString());

            BigDecimal spamTotalWordsBD = BigDecimal.valueOf(this.spamTotalWords);
            BigDecimal hamTotalWordsBD = BigDecimal.valueOf(this.hamTotalWords);


            // calculate p_spamGivenMessage by considering words in classify data
            for (String fileName : this.classifyMegaDictionary.keySet()) {
            	// initialize certain variables
                BigDecimal p_messageGivenSpam = BigDecimal.ONE;
                BigDecimal p_messageGivenHam = BigDecimal.ONE;
                BigDecimal p_message = BigDecimal.ZERO;
                BigDecimal p_spamGivenMessage = BigDecimal.ZERO;

                for (String word : this.classifyMegaDictionary.get(fileName).keySet()) {
                    BigDecimal countWInSpam = BigDecimal.ZERO;
                    BigDecimal p_wordGivenSpam = BigDecimal.ZERO;

                    BigDecimal countWInHam = BigDecimal.ZERO;
                    BigDecimal p_wordGivenHam = BigDecimal.ZERO;


                    if (this.spamDictionary.containsKey(word)) {
//                    	System.out.println(word + ": " + this.spamDictionary.get(word)+ "/" + spamTotalWordsBD);
                        // compute for probability of message given spam
                        // p_messageGivenSpam = products of (P(w|spam)...)
                        countWInSpam = BigDecimal.valueOf((double)this.spamDictionary.get(word));
                        p_wordGivenSpam = countWInSpam.divide(spamTotalWordsBD, 128, RoundingMode.HALF_UP);
                        p_messageGivenSpam = p_messageGivenSpam.multiply(p_wordGivenSpam);
                    }

                    if (this.hamDictionary.containsKey(word)) {
//                    	System.out.println(word + ": " + this.hamDictionary.get(word)+ "/" + hamTotalWordsBD);
                        // compute for probability of message given ham
                        // p_messageGivenSpam = products of (P(w|ham)...)
                    	countWInHam = BigDecimal.valueOf((double)this.hamDictionary.get(word));
                    	p_wordGivenHam = countWInHam.divide(hamTotalWordsBD, 128, RoundingMode.HALF_UP);
                        p_messageGivenHam = p_messageGivenHam.multiply(p_wordGivenHam);
                    }

                }

//                System.out.println(p_messageGivenSpam);
//                System.out.println(p_messageGivenHam);
                BigDecimal input1 = p_messageGivenSpam.multiply(this.p_spam);
                BigDecimal input2 = p_messageGivenHam.multiply(this.p_ham);
                p_message = input1.add(input2);
//                System.out.println(p_message);

                BigDecimal input3 = p_messageGivenSpam.multiply(this.p_spam);
                p_spamGivenMessage = input3.divide(p_message, 4, RoundingMode.HALF_UP);

                // output results
                String classOfMail = "";
                if (p_spamGivenMessage.compareTo(BOW.THRESHOLD)<0) {
                	classOfMail = "HAM";
                } else {
                	classOfMail = "SPAM";
                }

                System.out.println("\nFileName: " + fileName);
                System.out.println("Class: " + classOfMail);
                System.out.println("P(Spam): "+  p_spamGivenMessage);

                // update tableview
                this.setOutputTable(new OutputData(fileName, classOfMail, p_spamGivenMessage));

                fileContent += fileName + " " + classOfMail + " " + p_spamGivenMessage.toString() + "\n";



            }

            System.out.println(this.totalFileCount);
            System.out.println(this.hamTotalWords);
            System.out.println(this.spamTotalWords);

            // export results
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(fileContent);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ===========================================================================================
    @SuppressWarnings("unchecked")
    private void setOutputTable(OutputData data) {
    	if (data == null) {
    		TableColumn fileNameColumn = new TableColumn<OutputData, String>("Filename");
    		fileNameColumn.setCellValueFactory(new PropertyValueFactory<OutputData, String>("fileName"));

    		TableColumn classColumn = new TableColumn<OutputData, String>("Class");
    		classColumn.setCellValueFactory(new PropertyValueFactory<OutputData, String>("classOfMail"));

    		TableColumn p_spamColumn = new TableColumn<OutputData, BigDecimal>("P(Spam)");
    		p_spamColumn.setCellValueFactory(new PropertyValueFactory<OutputData, BigDecimal>("p_spam"));

    		this.classifyTableView.getColumns().add(fileNameColumn);
    		this.classifyTableView.getColumns().add(classColumn);
    		this.classifyTableView.getColumns().add(p_spamColumn);


            // Set TableView properties (adjust these as needed)
    		this.classifyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    		this.classifyTableView.setEditable(false);
    		this.classifyTableView.setMinHeight(375);
    		this.classifyTableView.setMaxHeight(375);
    		this.classifyTableView.setMinWidth(325);
    		this.classifyTableView.setMaxWidth(325);
    		this.classifyTableView.setTranslateX(BOW.CLASSIFY_COLUMN + 30);
    		this.classifyTableView.setTranslateY((BOW.WINDOW_HEIGHT / 20) * 6 + 15);


    		this.root.getChildren().add(this.classifyTableView);
    	} else {
    		this.classifyTableView.getItems().add(data);
    	}

    }

}