import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

class SpellCheckerTest {

	private SpellChecker testSpellChecker = new SpellChecker();

	@Test
	void testReceiveFiles() {
		Exception e = new FileNotFoundException(Util.FILE_OPENING_ERROR);

		assertThrows(FileNotFoundException.class, () -> {
			FileInputStream fs = new FileInputStream("sdjflksjdlfksjdf.fssdf");
		});

		assertEquals(Util.FILE_OPENING_ERROR, e.getMessage());
	}

	@Test
	void testIsMisspelled() throws FileNotFoundException {
		FileInputStream dictionaryStream = new FileInputStream("testDictionary.txt");
		assertEquals(true, testSpellChecker.isMisspelled("papaya", dictionaryStream));
	}

	@Test
	void testIsMisspelled2() throws FileNotFoundException {
		FileInputStream dictionaryStream = new FileInputStream("testDictionary.txt");
		assertEquals(false, testSpellChecker.isMisspelled("red", dictionaryStream));
	}

}
