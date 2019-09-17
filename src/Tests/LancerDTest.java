package Tests;
import org.junit.jupiter.api.Test;

import Classes.Des;

public class LancerDTest extends SuperTest {

	@Test
	void lancerD() {


		for (int i = 0; i < 100; i++) {
			int D = Des.lancerUnDe();
			assert (D > 0 && D < 7);
	
		}
	}


	@Test
	void verifDouble() {
		// Arrange
		DesPipes.lancerDouble();
		// Action
		boolean D = Des.estUnDouble();
		// Assert
		assert (D == true);
		
		// Arrange
		DesPipes.lancerSimple();
		// Action
		D = Des.estUnDouble();
		// Assert
		assert (D == false);
	}

	@Test
	void lance2D() {	
		for (int i = 0; i < 100; i++) {

			// Action
			Des.lancer2Des();

			// Assert
			int res = Des.getResultat();
			assert (res > 0 && res < 13);
		}
	}


}
