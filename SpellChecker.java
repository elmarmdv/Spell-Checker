import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class SpellChecker {
	private ArrayList<String> dictionary;
	private ArrayList<String> inputFile;
	private String outputName;

	public SpellChecker() {
		// TODO: You can modify the body of this constructor,
		// or you can leave it blank. You must keep the signature, however.
	}

	public void receiveDictionary() {
		boolean waitingForDict = true;
		while (waitingForDict) {
			try {
				System.out.printf(Util.DICTIONARY_PROMPT);
				Scanner scnr = new Scanner(System.in);
				String dictionary = scnr.next();
				FileInputStream dictionaryStream = new FileInputStream(dictionary);
				// put dictionary into an array
				// wanted this to be its own method
				// so originally thought to return FileInputStream
				Scanner dictionaryScanner = new Scanner(dictionaryStream);
				while (dictionaryScanner.hasNext()) {
					this.dictionary.add(dictionaryScanner.next());
				}
				waitingForDict = false;
				System.out.printf(Util.DICTIONARY_SUCCESS_NOTIFICATION, dictionary);
				// any other exceptions to catch?
			} catch (FileNotFoundException e) {
				System.out.println(Util.FILE_OPENING_ERROR);
			}
		}
	}

	public void receiveFile() {
		boolean waitingForFile = true;
		while (waitingForFile) {
			try {
				System.out.printf(Util.FILENAME_PROMPT);
				Scanner scnr = new Scanner(System.in);
				String file = scnr.next();
				FileInputStream fileStream = new FileInputStream(file);
				Scanner fileScanner = new Scanner(fileStream);
				while (fileScanner.hasNext()) {
					this.inputFile.add(fileScanner.next());
				}
				waitingForFile = false;
				this.outputName = file.substring(0, file.length() - 4).concat("_chk.txt");
				System.out.printf(Util.FILE_SUCCESS_NOTIFICATION, file, this.outputName);
			} catch (FileNotFoundException e) {
				System.out.printf(Util.FILE_OPENING_ERROR);
			}
		}
	}

	public boolean checkSpelling(String currentWord) {
		boolean notAWord = true;
		for (int i = 0; i < this.dictionary.size(); i++) {
			if (currentWord == this.dictionary.get(i)) {
				notAWord = false;
			}
		}
		return notAWord;

	}

	public static String replaceWord(String currentWord) {
		System.out.printf(Util.MISSPELL_NOTIFICATION, currentWord);
		System.out.printf(Util.THREE_OPTION_PROMPT);
		// takes in user choice, noValidChoice ends while loop when valid choice is made
		Scanner choiceScanner = new Scanner(System.in);
		String optionChosen = choiceScanner.next();
		boolean noValidChoice = true;
		while (noValidChoice) {
			if (optionChosen.equals("r")) {
				// word recommender here!!
				System.out.printf(Util.AUTOMATIC_REPLACEMENT_PROMPT);
				int suggestionChosen = choiceScanner.nextInt();
				noValidChoice = false;
			} else if (optionChosen.equals("a")) {
				noValidChoice = false;
			} else if (optionChosen.equals("t")) {
				System.out.printf(Util.MANUAL_REPLACEMENT_PROMPT);
				currentWord = choiceScanner.next();
				noValidChoice = false;
			} else {
				System.out.printf(Util.INVALID_RESPONSE);
			}
		}
		return currentWord;
	}

	public void createOutput() throws FileNotFoundException {
		FileOutputStream os = new FileOutputStream(this.outputName);
		PrintWriter out = new PrintWriter(os);
		for (int i = 0; i < this.inputFile.size(); i++) {
			String currentWord = this.inputFile.get(i);
			if (checkSpelling(currentWord)) {
				currentWord = replaceWord(currentWord);
			}
			out.print(currentWord + " ");
		}
	}

	public static void start() {

	}

}