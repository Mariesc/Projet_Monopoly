package Tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import Classes.Parametres;

class GroupeTest extends SuperTest {

	@Test
	void totalCases() {

		// Arrange
		initJeuUnJoueur();
		int nbCase = Parametres.NB_CASES;

		// Assert
		assertEquals(nbCase, plateau.getListeCases().size());
	}

}
