import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class SpellCheckerTest {

	private SpellChecker testSpellChecker = new SpellChecker();
	private WordRecommender testWordRecommender = new WordRecommender("testDictionary.txt");

	@Test
	void testReceiveFiles() {
		Exception e = new FileNotFoundException(Util.FILE_OPENING_ERROR);

		assertThrows(FileNotFoundException.class, () -> {
			FileInputStream fs = new FileInputStream("sdjflksjdlfksjdf.fssdf");
		});

		assertEquals(Util.FILE_OPENING_ERROR, e.getMessage());
	}

	@Test
	void testReceiveDictionary() throws FileNotFoundException {
		FileInputStream dictionaryStream = new FileInputStream("testDictionary.txt");
		Scanner scnr = new Scanner(dictionaryStream);

		assertEquals("red", scnr.next());
		assertEquals("read", scnr.next());
		assertEquals("reads", scnr.next());
		assertEquals("reed", scnr.next());
		assertEquals("door", scnr.next());
		assertEquals("dear", scnr.next());
		assertEquals("dreads", scnr.next());

	}

	@Test
	void testReceiveFile() throws FileNotFoundException {
		FileInputStream fileStream = new FileInputStream("testfile.txt");
		Scanner scnr = new Scanner(fileStream);

		assertEquals("the", scnr.next());
		assertEquals("old", scnr.next());
	}

	@Test
	void testIsMisspelled() throws FileNotFoundException {
		testSpellChecker.dictionary = "testDictionary.txt";
		assertEquals(true, testSpellChecker.isMisspelled("the"));
	}

	@Test
	void testIsMisspelled2() throws FileNotFoundException {
		testSpellChecker.dictionary = "testDictionary.txt";
		assertEquals(false, testSpellChecker.isMisspelled("red"));
	}

	@Test
	void testmodifyWord() throws FileNotFoundException {
		assertEquals("purple", testSpellChecker.modifyWord("purple", "a"));
	}

}
