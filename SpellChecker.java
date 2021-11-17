import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class SpellChecker {
	// keep track of the dictionary file's name
	private String dictionaryName;
	// will store every word in the dictionary file
	private HashSet<String> dictionary;
	// keep track of the output file's name
	private String outputName;
	private WordRecommender wordRec;

	public SpellChecker() {
		dictionary = new HashSet<String>();
	}

	// receive a valid dictionary
	public void receiveDictionary() {
		// user will enter a string until that string is a valid dictionary
		boolean waitingForDict = true;
		FileInputStream dictionaryStream = null;
		Scanner scnr = new Scanner(System.in);
		// catch IO exceptions, and ask user for new input if it isn't valid
		try {
			while (waitingForDict) {
				System.out.printf(Util.DICTIONARY_PROMPT);

				String dictionaryName = scnr.next();
				// create a stream from this input to determine if it is a valid file
				dictionaryStream = new FileInputStream(dictionaryName);
				Scanner dictionaryScanner = new Scanner(dictionaryStream);
				// use scanner to put every word in dictionary into the dictionary HashSet
				while (dictionaryScanner.hasNext()) {
					this.dictionary.add(dictionaryScanner.next());
				}
				waitingForDict = false;
				System.out.printf(Util.DICTIONARY_SUCCESS_NOTIFICATION, dictionaryName);
				// create WordRecommender object
				this.wordRec = new WordRecommender(dictionaryName);
				dictionaryStream.close();
			}
		} catch (IOException e) {
			System.out.printf(Util.FILE_OPENING_ERROR);
		}
	}

	// so dictionary field can be set for testing purposes
	public void setDictionary(String name) throws IOException {
		this.dictionaryName = name;
		FileInputStream dictionaryStream = new FileInputStream(name);
		Scanner dictionaryScanner = new Scanner(dictionaryStream);
		while (dictionaryScanner.hasNext()) {
			this.dictionary.add(dictionaryScanner.next());
		}
		dictionaryScanner.close();
		dictionaryStream.close();
	}

	// receive a valid file to be spell-checked
	public FileInputStream receiveFile() {
		// user will enter a string until that string is a valid dictionary
		boolean waitingForFile = true;
		Scanner scnr = new Scanner(System.in);
		while (waitingForFile) {
			try {
				System.out.printf(Util.FILENAME_PROMPT);
				String file = scnr.next();
				FileInputStream fileStream = new FileInputStream(file);
				waitingForFile = false;
				this.outputName = file.substring(0, file.length() - 4).concat("_chk.txt");
				System.out.printf(Util.FILE_SUCCESS_NOTIFICATION, file, this.outputName);
				return fileStream;
			} catch (FileNotFoundException e) {
				System.out.printf(Util.FILE_OPENING_ERROR);
			}
		}
		return null;
	}

	// determines if a word is misspelled
	// returns true if it is misspelled
	// and false if it isn't
	public boolean isMisspelled(String currentWord) {
		// if the word equals any word in the dictionary, it is considered spelled
		// correctly, and method will return false
		if (this.dictionary.contains(currentWord)) {
			return false;
		}
		return true;
	}

	// takes a misspelled word as an argument and returns how it will deal with the
	// word
	// based on 3 options
	public String howToReplace(String misspelledWord) throws FileNotFoundException {
		System.out.printf(Util.MISSPELL_NOTIFICATION, misspelledWord);
		// creates WordRecommender object to determine if there are any suggestions
		// WordRecommender wordRec = new WordRecommender(this.dictionaryName);
		ArrayList<String> suggestions = this.wordRec.getWordSuggestions(misspelledWord, 2, 0.5, 4);

		// takes in user choice, noValidChoice ends while loop when valid choice is made
		Scanner choiceScanner = new Scanner(System.in);
		boolean noValidChoice = true;
		String optionChoice = "";

		// if there are no suggestions, user will have 2 options
		if (suggestions.size() == 0) {
			System.out.printf(Util.NO_SUGGESTIONS);
			System.out.printf(Util.TWO_OPTION_PROMPT);
			while (noValidChoice) {
				// user chooses to choose a suggestion, leave as is, or manually enter a
				// replacement
				optionChoice = choiceScanner.next();
				if (optionChoice.equals("a") || optionChoice.equals("t")) {
					noValidChoice = false;
				} else {
					System.out.printf(Util.INVALID_RESPONSE);
				}
			}
			// if there are 3 suggestions, user chooses from "a", "t", "r"
		} else {
			System.out.printf(Util.FOLLOWING_SUGGESTIONS);
			for (int i = 0; i < suggestions.size(); i++) {
				System.out.printf(Util.SUGGESTION_ENTRY, i + 1, suggestions.get(i));
			}
			System.out.printf(Util.THREE_OPTION_PROMPT);
			while (noValidChoice) {
				optionChoice = choiceScanner.next();
				if (optionChoice.equals("a") || optionChoice.equals("t") || optionChoice.equals("r")) {
					noValidChoice = false;
				} else {
					System.out.printf(Util.INVALID_RESPONSE);
				}
			}
		}
		return optionChoice;
	}

	// modifies the misspelled word based on the user's choice
	public String modifyWord(String misspelledWord, String optionChoice) throws FileNotFoundException {
		// creates WordRecommender object to get suggestions for misspelled word
		int topN = 4; // hardcoded to 4; can be changed

		ArrayList<String> suggestions = wordRec.getWordSuggestions(misspelledWord, 2, 0.5, topN);
		Scanner newWordScanner = new Scanner(System.in);

		// if user chooses 'r', next choose one of the valid suggestions
		if (optionChoice.equals("r")) {
			System.out.printf(Util.AUTOMATIC_REPLACEMENT_PROMPT);
			int suggestionChosen = 0;
			while (suggestionChosen > topN || suggestionChosen < 1) {
				try {
					String suggestionString = newWordScanner.next();
					suggestionChosen = Integer.valueOf(suggestionString);
				} catch (NumberFormatException e) {
					// will catch the exception if user input is other than int
					// and assign suggestionChosen to 0
					suggestionChosen = 0;
				}
				if (suggestionChosen > topN || suggestionChosen < 1) {
					System.out.printf(Util.INVALID_RESPONSE);
				}
			}
			misspelledWord = suggestions.get(suggestionChosen - 1);
			// if user chooses "a", misspelled word will stay the same
		} else if (optionChoice.equals("a")) {
			// if user chooses "t", next enter spelling
		} else if (optionChoice.equals("t")) {
			System.out.printf(Util.MANUAL_REPLACEMENT_PROMPT);
			misspelledWord = newWordScanner.next();
			// if user entered none of these, print INVALID_RESPONSE format string
			// and try again
		} else {
			System.out.printf(Util.INVALID_RESPONSE);
		}
		// return the updated word
		return misspelledWord;
	}

	public void start() {
		// receives a valid dictionary and inputFile, and creates a FileInputStream and
		// FileOutputStream
		receiveDictionary();
		FileInputStream inputFileStream = receiveFile();
		FileOutputStream os;
		try {
			os = new FileOutputStream(this.outputName);
			PrintWriter out = new PrintWriter(os);
			Scanner fileScanner = new Scanner(inputFileStream);
			// while there is another word in the input file, if it is misspelled
			// see if the user wants to modify it
			while (fileScanner.hasNext()) {
				String currentWord = fileScanner.next();
				if (isMisspelled(currentWord)) {
					String optionChoice = howToReplace(currentWord);
					currentWord = modifyWord(currentWord, optionChoice);
					out.print(currentWord + " ");
				} else {
					out.print(currentWord + " ");
				}
			}
			inputFileStream.close();
			fileScanner.close();
			out.close();
		} catch (IOException e) {
			System.out.println("Output file not found.");
		}
	}

}