package Classes;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;

public class IHMTest implements InterfaceIHM {

	@Override
	public int choixNbJoueurs(Scanner scanner) {
		return 4;
	}

	@Override
	public int choixAchatCase(Plateau plateau, Scanner scanner) {
		return 1;
	}

	@Override
	public ArrayList<Joueur> initialiserJoueurs(int nbJoueurs, Scanner scanner) {

		ArrayList<Joueur> listeJoueurs = new ArrayList<>();
		listeJoueurs.add(new Joueur("Paul"));
		listeJoueurs.add(new Joueur("Marie"));
		listeJoueurs.add(new Joueur("Romain"));
		listeJoueurs.add(new Joueur("Ryann"));

		Consumer<Joueur> c = j -> j.ajouterCarteSpeciale(Parametres.SORTIR_DE_PRISON);

		listeJoueurs.forEach(c);

		return listeJoueurs;

	}

	@Override
	public int choixAmeliorationPatrimoine(Plateau plateau, Scanner scanner) {
		return 1;
	}

	@Override
	public int choixPayerAmendePrison(Scanner scanner) {
		return new Random().nextInt(1);
	}

	@Override
	public boolean choixUtilisationCarteSpeciale() {

		return true;

	}

}
