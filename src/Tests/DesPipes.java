package Tests;

import Classes.Des;

public class DesPipes extends Des {

	public static int lancerDouble() {
		boolean lancerdouble = false;

		while (!lancerdouble) {
			Des.lancer2Des();
			lancerdouble = Des.estUnDouble();
		}
		return Des.getResultat();
	}
	
	public static int lancerSimple() {
		boolean lancerdouble = true;

		while (lancerdouble) {
			Des.lancer2Des();
			lancerdouble = Des.estUnDouble();
		}
		return Des.getResultat();
	}

}
