import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

public class WordRecommenderTest {
	private WordRecommender testWordRec1 = new WordRecommender("testDictionary1.txt");
	private WordRecommender testWordRec2 = new WordRecommender("testDictionary2.txt");

	// -------- TESTING getWordSuggestions() -------- //

	@Test
	public void wordSuggestionsForMissingFile() {
		Exception e = assertThrows(FileNotFoundException.class, () -> {
			WordRecommender testWordRecommender = new WordRecommender("MissingFile.txt");
			ArrayList<String> testSuggestions = testWordRecommender.getWordSuggestions("word", 2, 0.5, 4);
		});
	}

	// Dictionary 1

	@Test
	public void wordSuggestionsForRaed() throws FileNotFoundException {
		ArrayList<String> testSuggestions = testWordRec1.getWordSuggestions("raed", 1, 0.75, 3);
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("reed", "read", "red"));
		assertEquals(expected, testSuggestions);
	}

	@Test
	public void wordSuggestionsForDredd() throws FileNotFoundException {
		ArrayList<String> testSuggestions = testWordRec1.getWordSuggestions("dredd", 1, 0.6, 3);
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("reed", "dreads", "read"));
		assertEquals(expected, testSuggestions);
	}

	@Test
	public void wordSuggestionsForRendre() throws FileNotFoundException {
		ArrayList<String> testSuggestions = testWordRec1.getWordSuggestions("rendre", 2, 0.5, 4);
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("read", "reads", "reed", "dear"));
		assertEquals(expected, testSuggestions);
	}

	// Dictionary 2

	@Test
	public void wordSuggestionsForScip() throws FileNotFoundException {
		ArrayList<String> testSuggestions = testWordRec2.getWordSuggestions("scip", 1, 0.5, 3);
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("skip", "scope", "spock"));
		assertEquals(expected, testSuggestions);
	}

	@Test
	public void wordSuggestionsForScype() throws FileNotFoundException {
		ArrayList<String> testSuggestions = testWordRec2.getWordSuggestions("scype", 0, 0.5, 3);
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("scope"));
		assertEquals(expected, testSuggestions);
	}

	// -------- TESTING getSimilarity() -------- //

	@Test
	public void getSimilarityABCDEvsABCDE() {
		assertEquals(5.0, testWordRec1.getSimilarity("abcde", "abcde"), 0.0);
	}

	@Test
	public void getSimilarityABCDFvsABCDE() {
		assertEquals(4.0, testWordRec1.getSimilarity("abcdf", "abcde"), 0.0);
	}

	@Test
	public void getSimilarityABCDEFvsABCDE() {
		assertEquals(2.5, testWordRec1.getSimilarity("abcdef", "abcde"), 0.0);
	}

	@Test
	public void getSimilarityBCDvsABCD() {
		assertEquals(1.5, testWordRec1.getSimilarity("bcd", "abcd"), 0.0);
	}

	@Test
	public void getSimilarityABCDEvsEDCBA() {
		assertEquals(1.0, testWordRec1.getSimilarity("abcde", "edcba"), 0.0);
	}

	// -------- TESTING makeSetFromWord() -------- //

	@Test
	public void makeSetFromHello() {
		HashSet<Character> testSet = new HashSet<Character>();
		testSet.add('H');
		testSet.add('e');
		testSet.add('l');
		testSet.add('o');
		assertEquals(testSet, testWordRec1.makeSetFromWord("Hello"));
	}

	// -------- TESTING calculateCommonPercent() -------- //

	@Test
	public void calculateCommonPercentABCDEvsABCD() {
		HashSet<Character> set1 = testWordRec1.makeSetFromWord("abcde");
		HashSet<Character> set2 = testWordRec1.makeSetFromWord("abcd");
		assertEquals(0.8, testWordRec1.calculateCommonPercent(set1, set2), 0.0);
	}

	@Test
	public void calculateCommonPercentABCDEvsABCDE() {
		HashSet<Character> set1 = testWordRec1.makeSetFromWord("abcde");
		HashSet<Character> set2 = testWordRec1.makeSetFromWord("abcde");
		assertEquals(1.0, testWordRec1.calculateCommonPercent(set1, set2), 0.001);
	}

	@Test
	public void calculateCommonPercentABCDEvsFGHIJ() {
		HashSet<Character> set1 = testWordRec1.makeSetFromWord("abcde");
		HashSet<Character> set2 = testWordRec1.makeSetFromWord("fghij");
		assertEquals(0.0, testWordRec1.calculateCommonPercent(set1, set2), 0.0);
	}

	@Test
	public void calculateCommonPercentABCDEvsEFGHI() {
		HashSet<Character> set1 = testWordRec1.makeSetFromWord("abcde");
		HashSet<Character> set2 = testWordRec1.makeSetFromWord("efghi");
		assertEquals(0.111, testWordRec1.calculateCommonPercent(set1, set2), 0.001);
	}

	@Test
	public void calculateCommonPercentABBBBvsBBBBC() {
		HashSet<Character> set1 = testWordRec1.makeSetFromWord("abbbb");
		HashSet<Character> set2 = testWordRec1.makeSetFromWord("bbbbc");
		assertEquals(0.333, testWordRec1.calculateCommonPercent(set1, set2), 0.001);
	}
}
