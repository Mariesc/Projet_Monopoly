package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import Classes.*;

class PlateauTest extends SuperTest {

	@Test
	void PlateauContientJoueurs() {

		// Arrange
		// A l'initialisation, le plateau doit contenir le nombre de joueurs choisis.
		ArrayList<Joueur> listeJoueurs = new ArrayList<>();
		listeJoueurs.add(new Joueur("Paul"));
		listeJoueurs.add(new Joueur("Marie"));
		listeJoueurs.add(new Joueur("Romain"));

		// Action
		initJeuUnJoueur();
		plateau.setListeJoueurs(listeJoueurs);

		// Assert
		assert (plateau.getListeJoueurs().containsAll(listeJoueurs));

	}

	@Test
	void PlateauContientCases() {

		// Action
		// Le plateau doit contenir le nombre de cases définies en paramètre.
		initJeuUnJoueur();

		// Assert
		assert (plateau.getListeCases().size() == Parametres.NB_CASES);

	}

	@Test
	void PlateauContientObjetsCases() {

		// Arrange
		// Le plateau doit contenir des objets de type Case.
		initJeuUnJoueur();

		// Action
		ArrayList<Case> listeCases = plateau.getListeCases();

		// Assert
		for (int i = 0; i < listeCases.size(); i++) {

			Case oCase = listeCases.get(i);
			assert (oCase.getNumero() == i);
			assert (oCase instanceof Case);
		}

	}

	@Test
	void PlateauPeuxPayer() {

		// Arrange
		// Le plateau doit contenir des objets de type Case.
		initJeuUnJoueur();		
		joueurA.setFortune(50);
		Case caseCourante = plateau.getListeCases().get(1);

		// Action
		// Assert
		assertEquals(plateau.peuxPayer(caseCourante), false);

		// Arrange
		joueurA.setFortune(10000);

		// Action
		// Assert
		assertEquals(plateau.peuxPayer(caseCourante), true);
	}

	@Test
	void joueurPayeLoyerSiProprietaire() {

		// Arrange
		initJeuDeuxJoueurs();
		deplacementUneCaseEtAchat(plateau, caseDepart, joueurA);
		int fortuneVisiteurAvant = joueurB.getFortune();
		int fortuneProprietaireAvant = joueurA.getFortune();
		joueurB.deplacer(1);
		Case caseCourante = getCase(joueurB.getPosition());

		// Action
		plateau.payerLoyer(caseCourante);
		int fortuneVisiteurApres = joueurB.getFortune();

		// Assert
		assertEquals(fortuneVisiteurApres, fortuneVisiteurAvant - caseCourante.getPrix());
		assertEquals(joueurA.getFortune(), fortuneProprietaireAvant + caseCourante.getPrix());
	}

	@Test
	void faillite() {

		// Arrange
		// Le plateau doit contenir des objets de type Case.
		initJeuUnJoueur();		
		joueurA.setFortune(50);

		// Actions
		plateau.failliteJoueur();

		// Assert
		assertEquals(plateau.getListeJoueurs().contains(joueurA), false);
	}

	@Test
	void joueurEstProprietaireDeCetteCase() {

		// Arrange
		initJeuDeuxJoueurs();
		/*ArrayList<Joueur> listeJoueurs = new ArrayList<>();
		Plateau plateau = SuperTest.initPlateau(listeJoueurs);
		Joueur joueurA = SuperTest.initJoueur(listeJoueurs, "Paul");
		Joueur joueurB = SuperTest.initJoueur(listeJoueurs, "Marie");
		plateau.setJoueurCourant(joueurB);*/

		joueurB.setFortune(5000);
		joueurB.acheteTerrain(getCase(2));
		joueurB.deplacer(2);

		// Action Le joueurA se déplace sur son propre terrain
		// Assert
		assertEquals(plateau.joueurCourantProprietaire(getCase(joueurB.getPosition())), true);

		// Action Le joueurB se déplace sur le terrain du joueurA
		joueurA.setFortune(5000);
		plateau.setJoueurCourant(joueurA);
		joueurA.deplacer(2);
		// Assert
		assertEquals(plateau.joueurCourantProprietaire(getCase(joueurA.getPosition())), false);

	}

	@Test
	public void proprietesDuJoueurPerdantSontRemisesALaBanque() {

		// Arrange
		initJeuDeuxJoueurs();
		joueurB.setFortune(50);
		joueurB.acheteTerrain(getCase(2));
		joueurA.deplacer(1);
		joueurA.acheteTerrain(getCase(1));

		// Action
		plateau.failliteJoueur();

		// Assert
		assertEquals(null, getCase(2).getJoueurProprietaire());
		assertEquals(-1, getCase(2).getNiveau());
	}

}
