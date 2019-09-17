package Classes;

import java.util.ArrayList;
import java.util.Scanner;

public class IHMClavier implements InterfaceIHM {

	@Override
	public int choixAchatCase(Plateau plateau, Scanner scanner) { 

		Joueur joueurCourant = plateau.getJoueurCourant();
		Case caseCourante = plateau.getListeCases().get(joueurCourant.getPosition());
		
		int number;
		do {
			System.out.print("Souhaitez-vous acheter ce terrain (0 = non, 1 = oui) ? " + caseCourante.resume());
			while (!scanner.hasNextInt()) {
				scanner.next();
				System.out.println("Veuillez saisir 0 ou 1");
			}
			number = scanner.nextInt();
		}
		while(number != 0 && number != 1);

		return number;
	}

	@Override
	public int choixNbJoueurs(Scanner scanner) {

		int number;
		do {
			System.out.println("Combien de joueur voulez-vous ? ");
			while (!scanner.hasNextInt()) {
				scanner.next();
				System.out.println("Veuillez saisir un nombre");
			}
			number = scanner.nextInt();
		}
		while(number < 1);

		return number;
	}

	@Override
	public ArrayList<Joueur> initialiserJoueurs(int nbJoueurs, Scanner scanner) {

		ArrayList<Joueur> listeJoueurs = new ArrayList<>();

		int compteur = 1;
		while (compteur <= nbJoueurs) {
			System.out.println("Quel nom pour le joueur " + compteur + " ? ");
			String nomj = scanner.next();
			listeJoueurs.add(new Joueur(nomj));
			compteur = compteur + 1;
		}

		return listeJoueurs;
	}

	@Override
	public int choixAmeliorationPatrimoine(Plateau plateau, Scanner scanner) {
		Joueur joueurCourant = plateau.getJoueurCourant();

		Case caseCourante = plateau.getListeCases().get(joueurCourant.getPosition());


		int number;
		do {
			if (caseCourante.getNiveau()<5)
				System.out.print("Souhaitez-vous ajouter une maison (0 = non, 1 = oui) ? " + caseCourante.resume());
			else
				System.out.print("Souhaitez-vous ajouter un hotel (0 = non, 1 = oui) ? " + caseCourante.resume());

			while (!scanner.hasNextInt()) {
				scanner.next();
				System.out.println("Veuillez saisir 0 ou 1");
			}
			number = scanner.nextInt();
		}
		while(number != 0 && number != 1);

		return number;
	}

	@Override
	public int choixPayerAmendePrison(Scanner scanner) {
		int number;
		do {
			System.out.print("Souhaitez-vous payer une amende de " + Parametres.MONTANT_AMENDE_PRISON + "€ pour sortir de prison le prochain tour ? (0 = non, 1 = oui) ? ");
			while (!scanner.hasNextInt()) {
				scanner.next();
				System.out.println("Veuillez saisir 0 ou 1");
			}
			number = scanner.nextInt();
		}
		while(number != 0 && number != 1);

		return number;
	}

	@Override
	public boolean choixUtilisationCarteSpeciale() {
		System.out.println("Par défaut, on utilise notre carte spéciale. TODO --");
		return true;
	}

}
