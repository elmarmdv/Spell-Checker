import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class SpellCheckerTest {

	private SpellChecker testSpellChecker = new SpellChecker();

	@Test
	void testReceiveFiles() {
		Exception e = new FileNotFoundException(Util.FILE_OPENING_ERROR);

		assertThrows(FileNotFoundException.class, () -> {
			FileInputStream fs = new FileInputStream("sdjflksjdlfksjdf.fssdf");
			fs.close();
		});

		assertEquals(Util.FILE_OPENING_ERROR, e.getMessage());
	}

	@Test
	void testReceiveDictionary() throws FileNotFoundException {
		FileInputStream dictionaryStream = new FileInputStream("testDictionary1.txt");
		Scanner scnr = new Scanner(dictionaryStream);

		assertEquals("red", scnr.next());
		assertEquals("read", scnr.next());
		assertEquals("reads", scnr.next());
		assertEquals("reed", scnr.next());
		assertEquals("door", scnr.next());
		assertEquals("dear", scnr.next());
		assertEquals("dreads", scnr.next());

		scnr.close();

	}

	@Test
	void testReceiveFile() throws FileNotFoundException {
		FileInputStream fileStream = new FileInputStream("testfile.txt");
		Scanner scnr = new Scanner(fileStream);

		assertEquals("the", scnr.next());
		assertEquals("old", scnr.next());
		scnr.close();
	}

	@Test
	void testIsMisspelled() throws IOException {
		testSpellChecker.setDictionary("engDictionary.txt");
		assertEquals(false, testSpellChecker.isMisspelled("the"));
	}

	@Test
	void testIsMisspelled2() throws IOException {
		testSpellChecker.setDictionary("engDictionary.txt");
		assertEquals(true, testSpellChecker.isMisspelled("aaaa"));
	}

	@Test
	void testmodifyWord() throws IOException {
		testSpellChecker.setDictionary("engDictionary.txt");
		assertEquals("parple", testSpellChecker.modifyWord("parple", "a"));
	}

}
