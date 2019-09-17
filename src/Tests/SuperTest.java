package Tests;

import java.util.ArrayList;
import Classes.Case;
import Classes.Joueur;
import Classes.Partie;
import Classes.Plateau;

public class SuperTest {

	public static final int PREMIER_COUP = 1;
	public static final int DEUXIEME_COUP = 2;
	public static final int TROISIEME_COUP = 3;

	public static final boolean PEUT_JOUER = true;

	public static final String[] PRISON = new String[] { "Prison" };
	public static final String[] AMENDE = new String[] { "Amende" };
	public static final String[] CADEAU = new String[] { "Cadeau" };
	public static final String[] REJOUER = new String[] { "Rejouer" };
	public static ArrayList<String> listeCartesSpeciales;
	public static final String SORTIR_DE_PRISON = "SortirPrison";

	protected void initJeuUnJoueur() {
		plateau = new Plateau();
		plateau.initialiserPlateau();
		listeJoueurs = new ArrayList<>();
		listeCartesSpeciales = new ArrayList<>();

		joueurA = new Joueur("Paul");
		listeJoueurs.add(joueurA);

		plateau.setJoueurCourant(joueurA);

		caseDepart = plateau.getListeCases().get(0);

	}

	public void tireLaCarte(String[] action, Partie partie) {
		Joueur joueurCourant = partie.getPlateau().getJoueurCourant();

		int positionCarteChance = 0;

		// On positionne le joueur juste avant la carte chance
		for (Case oCase : partie.getPlateau().getListeCases())

			if (oCase.getTitre().equals("Chance")) {
				positionCarteChance = oCase.getNumero();
				oCase.setActions(action);
				joueurCourant.setPosition(positionCarteChance - 3);
				break;
			}
		DesPipes.setResultat(3);

	}

	protected void initJeuUnJoueurAvecController(Partie controller) {
		plateau.initialiserPlateau();
		listeJoueurs = new ArrayList<>();

		joueurA = new Joueur("Paul");
		listeJoueurs.add(joueurA);

		plateau.setJoueurCourant(joueurA);

		caseDepart = plateau.getListeCases().get(0);

	}

	protected Case getCase(String nomCase) {

		for (Case oCase : plateau.getListeCases())
			if (oCase.getTitre().equals(nomCase))
				return oCase;

		return null;
	}

	protected Case getCase(int index) {
		return plateau.getListeCases().get(index);
	}

	protected void initJeuDeuxJoueurs() {

		initJeuUnJoueur();

		joueurB = new Joueur("Marie");
		listeJoueurs.add(joueurB);

		plateau.setJoueurCourant(joueurB);
	}

	protected void deplacementUneCaseEtAchat(Plateau plateau, Case caseDepart, Joueur joueur) {

		joueur.deplacer(1);

		Case caseCourante = getCase(joueur.getPosition());

		joueur.acheteTerrain(caseCourante);

	}

	protected Plateau plateau;
	protected Joueur joueurA, joueurB;
	protected Case caseDepart;
	protected ArrayList<Joueur> listeJoueurs;

}
