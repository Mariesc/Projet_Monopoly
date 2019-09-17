package Classes;

import java.util.ArrayList;
import java.util.Scanner;

public interface InterfaceIHM {
	
	int choixAchatCase(Plateau plateau, Scanner scanner);

	int choixNbJoueurs(Scanner scanner);
	
	int choixAmeliorationPatrimoine(Plateau plateau, Scanner scanner);
	
	int choixPayerAmendePrison(Scanner scanner);
	
	ArrayList<Joueur> initialiserJoueurs(int nbJoueurs, Scanner scanner);

	boolean choixUtilisationCarteSpeciale();
	

	
}
