import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class SpellChecker {
	private String dictionary;
	private String outputName;

	public SpellChecker() {
	}

	public FileInputStream receiveDictionary() {
		boolean waitingForDict = true;
		while (waitingForDict) {
			try {
				System.out.printf(Util.DICTIONARY_PROMPT);
				Scanner scnr = new Scanner(System.in);
				this.dictionary = scnr.next();
				FileInputStream dictionaryStream = new FileInputStream(this.dictionary);
				waitingForDict = false;
				System.out.printf(Util.DICTIONARY_SUCCESS_NOTIFICATION, this.dictionary);
				return dictionaryStream;
				// any other exceptions to catch?
			} catch (FileNotFoundException e) {
				System.out.printf(Util.FILE_OPENING_ERROR);
			}
		}
		return null;
	}

	public FileInputStream receiveFile() {
		boolean waitingForFile = true;
		while (waitingForFile) {
			try {
				System.out.printf(Util.FILENAME_PROMPT);
				Scanner scnr = new Scanner(System.in);
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

	public boolean isMisspelled(String currentWord, FileInputStream dictionaryStream) {
		Scanner dictionaryScanner = new Scanner(dictionaryStream);
		ArrayList<String> test = new ArrayList<String>();
		while (dictionaryScanner.hasNext()) {
			String dictionaryEntry = dictionaryScanner.next();
			if (currentWord.equals(dictionaryEntry)) {
				return false;
			}
		}
		dictionaryScanner.close();
		return true;

	}

	// takes in the misspelled word and modifies it based on user's choice
	public String modifyWord(String misspelledWord) {
		// lists suggestions, if any
		System.out.printf(Util.MISSPELL_NOTIFICATION, misspelledWord);
		WordRecommender wordRec = new WordRecommender(this.dictionary);
		ArrayList<String> suggestions = wordRec.getWordSuggestions(misspelledWord, 2, 0.5, 4);
		System.out.printf(Util.FOLLOWING_SUGGESTIONS);
		for (int i = 0; i < suggestions.size(); i++) {
			System.out.printf(Util.SUGGESTION_ENTRY, i, suggestions.get(i));
		}
		// takes in user choice, noValidChoice ends while loop when valid choice is made
		Scanner choiceScanner = new Scanner(System.in);
		String optionChosen = choiceScanner.next();
		boolean noValidChoice = true;
		while (noValidChoice) {
			// print 2 option or 3 option prompt
			if (suggestions.size() == 0) {
				System.out.printf(Util.NO_SUGGESTIONS);
				System.out.printf(Util.TWO_OPTION_PROMPT);
			} else {
				System.out.printf(Util.THREE_OPTION_PROMPT);
			}
			// user chooses to choose a suggestion, leave as is, or manually enter a
			// replacement
			if (optionChosen.equals("r")) {
				System.out.printf(Util.AUTOMATIC_REPLACEMENT_PROMPT);
				int suggestionChosen = suggestions.size();
				while (suggestionChosen >= suggestions.size() || suggestionChosen <= 0) {
					suggestionChosen = choiceScanner.nextInt();
					if (suggestionChosen >= suggestions.size() || suggestionChosen <= 0) {
						System.out.printf(Util.INVALID_RESPONSE);
					}
				}
				misspelledWord = suggestions.get(suggestionChosen);
				noValidChoice = false;
			} else if (optionChosen.equals("a")) {
				noValidChoice = false;
			} else if (optionChosen.equals("t")) {
				System.out.printf(Util.MANUAL_REPLACEMENT_PROMPT);
				misspelledWord = choiceScanner.next();
				noValidChoice = false;
			} else {
				System.out.printf(Util.INVALID_RESPONSE);
			}
		}
		// return the updated word
		return misspelledWord;
	}

	public void start() {
		FileInputStream dictionaryStream = receiveDictionary();
		FileInputStream inputFileStream = receiveFile();
		FileOutputStream os;
		try {
			os = new FileOutputStream(this.outputName);
			PrintWriter out = new PrintWriter(os);
			Scanner fileScanner = new Scanner(inputFileStream);
			while (fileScanner.hasNext()) {
				String currentWord = fileScanner.next();
				if (isMisspelled(currentWord, dictionaryStream)) {
					currentWord = modifyWord(currentWord);
				}
				out.print(currentWord + " ");
			}
			fileScanner.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}