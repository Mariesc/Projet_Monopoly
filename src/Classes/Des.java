package Classes;

import java.util.Random;

public class Des {
	static int valeurMin = 1;
	static int valeurMax = 6;

	private static int lance1, lance2, resultat;
	private static boolean estUnDouble;

	public static int lancerUnDe() {
		Random r = new Random();
		return valeurMin + r.nextInt(valeurMax - valeurMin);

	}

	public static void verifierDouble() {
		if (lance1 == lance2) {
			System.out.println("Double !");
			estUnDouble = true;
		} else {
			estUnDouble = false;
		}

	}

	public static void lancer2Des() {
		lance1 = lancerUnDe();
		lance2 = lancerUnDe();
		resultat = lance1 + lance2;
		verifierDouble();
		System.out.println(lance1 + " " + lance2);
		System.out.println("Dés : " + resultat);

	}

	public static int getLance1() {
		return lance1;
	}

	public static void setLance1(int lance) {
		lance1 = lance;
	}

	public static int getLance2() {
		return lance2;
	}

	public static void setLance2(int lance) {
		lance2 = lance;
	}

	public static int getResultat() {
		return resultat;
	}

	public static void setResultat(int res) {
		resultat = res;
	}

	public static boolean estUnDouble() {
		return estUnDouble;
	}

	

}
