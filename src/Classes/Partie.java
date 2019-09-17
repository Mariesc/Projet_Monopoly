package Classes;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @category Classe principale d'exécution du programme
 * @param args
 */
public class Partie {

	public static final String LIGNE_POINTILLES = "------------------------------------------";

	public int nbDoubles = 0;
	public static int nbTours = 1;
	public Plateau plateau;

	private Scanner scanner;

	public InterfaceIHM ihm;

	public static void main(String[] args) {

		// Création nouvelle partie
		Partie partie = new Partie();
		// Lancer la partie
		partie.lancer();
	}

	public Partie() {

		// Initialisation de la partie
		initialiserJeu();
	}

	public void lancer() {

		// Tant que la partie n'est pas terminée
		// (faillite d'un joueur et nombre de joueur inférieur ou égal à 1)
		while (!plateau.finDePartie)
			boucleJeu(scanner);

	}

	private void initialiserJeu() {
		// Création de l'IHM : IHMTest ou IHMClavier
		ihm = new IHMClavier();

		// Création et initialisation du plateau
		plateau = new Plateau(this);
		plateau.initialiserPlateau();

		// Création du scanner (gestion des entrées (automatique ou clavier))
		scanner = new Scanner(System.in);
		// Choix du nombre de joueur + initialisation
		int nbJoueurs = ihm.choixNbJoueurs(scanner);
		plateau.setListeJoueurs(ihm.initialiserJoueurs(nbJoueurs, scanner));
		plateau.setJoueurCourant(plateau.getListeJoueurs().get(0));
	}

	/**
	 * 1 - Le joueur courant lance 2 dés 2 - Il effectue un déplacement 3 - Il
	 * effectue une action (ex: achat) 4 - Il rejoue éventuellement(double), sinon
	 * on passe au joueur suivant
	 */
	public void boucleJeu(Scanner scanner) {

		ArrayList<Joueur> listeJoueurs = plateau.getListeJoueurs();

		// A chaque tour...
		for (int i = 0; i < listeJoueurs.size(); i++) {
			int nbCoupsJoueur = 0;
			boolean peutJouer = true;
			plateau.setJoueurCourant(listeJoueurs.get(i));

			affichageDebutTour();
			// Si le joueur est en prison, est-ce-qu'il s'est libéré et donc peut jouer
			peutJouer = payerAmendePrison();

			// Si oui et tant qu'il fait des doubles (3 max), il joue
			while (peutJouer) {

				Des.lancer2Des();

				if (Des.estUnDouble())
					nbDoubles++;
				
				nbCoupsJoueur++;

				// ACTION DU JOUEUR (qui peut faire évoluer peut jouer)

				peutJouer = plateau.actionJoueur(nbCoupsJoueur, peutJouer);

			}
			
			nbDoubles = 0;
			System.out.println(LIGNE_POINTILLES);
			// Si on est à la fin du tour, on passe au tour suivant
			if (i == listeJoueurs.size() - 1)
				nbTours++;

			if (plateau.finDePartie)
				return;
		}
	}

	/**
	 * Gestion de l'affichage de début de tour (informations sur le tour et le
	 * joueur courant)
	 */
	public void affichageDebutTour() {

		Joueur joueur = plateau.getJoueurCourant();
		System.out.println(LIGNE_POINTILLES);
		System.out.println("Tour n° " + nbTours + " :");
		String statut = joueur.estPrisonnier() ? " [En prison] " : " [En liberté] ";
		System.out.println("Au tour du joueur : " + joueur.getNom() + statut + ".");
		joueur.afficherInformations();
	}

	// Si le joueur est prisonnier, on lui offre la possibilité
	// d'en sortir

	public boolean payerAmendePrison() {

		boolean peutJouer = true;
		Joueur joueur = plateau.getJoueurCourant();

		if (joueur.estPrisonnier()) {
			if (ihm.choixPayerAmendePrison(scanner) == 1) {
				joueur.payeAmendePrison();
				System.out.println("Le joueur " + joueur.getNom() + " choisit de payer une amende et sort de prison.");
				peutJouer = false;
			} else
				System.out.println("Le joueur " + joueur.getNom()
						+ " choisit de ne pas payer une amende et tente sa chance aux dés.");
		}
		return peutJouer;
	}

	public Plateau getPlateau() {
		return plateau;
	}

	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
	}

	public Scanner getScanner() {
		return scanner;
	}

	public void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}

	public InterfaceIHM getIhm() {
		return ihm;
	}

	public void setIhm(InterfaceIHM ihm) {
		this.ihm = ihm;
	}

	int choixAchatCase() {
		return ihm.choixAchatCase(plateau, scanner);
	}

	int choixAmeiolationPatrimoine() {
		return ihm.choixAmeliorationPatrimoine(plateau, scanner);
	}

	public Case getCase(String nomCase) {

		for (Case oCase : plateau.getListeCases())
			if (oCase.getTitre().equals(nomCase))
				return oCase;

		return null;
	}

	public Case getCase(int index) {
		return plateau.getListeCases().get(index);
	}

	public boolean choixUtilisationCarteSpeciale() {
		return ihm.choixUtilisationCarteSpeciale();

	}

}
