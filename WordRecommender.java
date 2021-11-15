import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class WordRecommender {
	private String dictionary;

	public WordRecommender(String dictionaryFile) {
		this.dictionary = dictionaryFile;
	}

	public double getSimilarity(String word1, String word2) {
		// variable that will store the length of the shorter word
		int shorterWordLength = 0;
		int leftSimilarity = 0;
		int rightSimilarity = 0;

		if (word1.length() <= word2.length()) {
			shorterWordLength = word1.length();
		} else {
			shorterWordLength = word2.length();
		}

		for (int i = 0; i < shorterWordLength; i++) {
			// increment leftSimilarity if chars are equal
			if (word1.charAt(i) == word2.charAt(i)) {
				leftSimilarity++;
			}
			// increment rightSimilarity if chars are equal in reverse
			if (word1.charAt(word1.length() - 1 - i) == word2.charAt(word2.length() - 1 - i)) {
				rightSimilarity++;
			}
		}
		// calculate and return left-right similarity
		double similarity = (double) (leftSimilarity + rightSimilarity) / 2;
		return similarity;
	}

	public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN)
			throws FileNotFoundException { // CHANGE THIS!!!!!!!!!!!!!!!
		ArrayList<String> suggestions = new ArrayList<String>();
		ArrayList<Double> topSimilarities = new ArrayList<Double>();

		// populate initial similarities with 0
		for (int i = 0; i < topN; i++) {
			topSimilarities.add(i, 0.0);
		}

		// Make a set containing letters of the input word
		HashSet<Character> aSet = makeSetFromWord(word);

		// create input stream and scanner for the dictionary file
		FileInputStream dictionaryStream = new FileInputStream(this.dictionary);
		Scanner lineScanner = new Scanner(dictionaryStream);

		// while there are still lines in the dictionary file...
		while (lineScanner.hasNextLine()) {
			// create scanner for each line and read the word
			Scanner wordScanner = new Scanner(lineScanner.nextLine());
			String candidate = wordScanner.next();

			// make a set containing letters of the candidate word
			HashSet<Character> bSet = makeSetFromWord(candidate);

			// call method to calculate commonPercent between (the sets of) two words
			double wordCommonality = calculateCommonPercent(aSet, bSet);

			// calculate difference in length of input and candidate words
			int lengthDifference = Math.abs(word.length() - candidate.length());

			// add candidate if tolerance and commonPercent properties are satisfied
			if (lengthDifference <= tolerance && wordCommonality >= commonPercent) {
				// calculate similarity
				double similarity = getSimilarity(word, candidate);
				for (int i = 0; i < topN; i++) {
					// add the elements if similarity is within topN
					// also, remove the last element if size bigger than topN
					if (similarity > topSimilarities.get(i)) {
						topSimilarities.add(i, similarity);
						suggestions.add(i, candidate);
						topSimilarities.remove(topN);
						if (suggestions.size() > topN)
							suggestions.remove(topN);
						break;
					}
				}
			}
			wordScanner.close();
		}
		lineScanner.close();
		return suggestions;
	}

	public HashSet<Character> makeSetFromWord(String word) {
		// creates a hashset of letters given a word
		HashSet<Character> set = new HashSet<Character>();
		for (int i = 0; i < word.length(); i++) {
			set.add(word.charAt(i));
		}
		return set;
	}

	public double calculateCommonPercent(HashSet<Character> aSet, HashSet<Character> bSet) {

		HashSet<Character> intersection = new HashSet<Character>();
		HashSet<Character> union = new HashSet<Character>();

		// populate intersection and union sets
		for (char c : aSet) {
			if (aSet.contains(c) && bSet.contains(c)) {
				intersection.add(c);
			}
			union.add(c);
		}
		for (char c : bSet) {
			union.add(c);
		}

		// find and return the commonPercent of the two given words
		double commonality = (double) intersection.size() / union.size();
		return commonality;
	}
}