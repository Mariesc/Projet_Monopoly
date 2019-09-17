package Tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import Classes.Case;
import Classes.Des;
import Classes.Joueur;
import Classes.Parametres;
import Classes.Partie;

class CaseTest extends SuperTest {

	@Test
	void caseVidePasDeProprietaire() {

		// Arrange
		initJeuUnJoueur();

		// Action
		// Assert
		assertEquals(caseDepart.getJoueurProprietaire(), null);

	}

	@Test
	void joueurAcheteCaseEtDevientProprietaire() {

		// Arrange
		initJeuUnJoueur();

		// Action
		caseDepart.setJoueurProprietaire(joueurA);

		// Assert
		assertEquals(caseDepart.getJoueurProprietaire(), joueurA);
	}

	@Test
	void joueurAcheteCaseEtDepenseArgent() {

		// Arrange
		initJeuUnJoueur();
		Des.lancer2Des();
		joueurA.deplacer(Des.getResultat());
		Case caseCourante = getCase(joueurA.getPosition());
		int fortunePrecendente = joueurA.getFortune();

		// Action
		joueurA.acheteTerrain(caseCourante);

		// Assert
		assertEquals(joueurA, caseCourante.getJoueurProprietaire());
		assertEquals(fortunePrecendente - caseCourante.getPrix(), joueurA.getFortune());
	}

	@Test
	void joueurAmelioreCaseEtDepenseArgent() {

		// Arrange
		Partie main = new Partie();

		Joueur joueur = main.getPlateau().getJoueurCourant();

		joueur.acheteTerrain(main.getCase(2));

		joueur.deplacer(2);

		Case caseCourante = main.getCase(2);

		assertEquals(0, caseCourante.getNiveau());

		main.getPlateau().gestionArriveeCase(main.getCase(2), true);
		assertEquals(1, caseCourante.getNiveau());

		// Assert
		assertEquals(1, caseCourante.getNiveau());
	}

	@Test
	void casePasAVendre() {

		// Arrange
		// On demande à la case 0 (départ) si elle est à vendre
		Case d = new Case(0, "Départ", "Ceci est la case départ");

		// Action
		boolean aVendre = d.estAVendre();

		// assert
		assertEquals(false, aVendre);
	}

	@Test
	void caseAvendre() {

		// Arrange
		// On demande à la case 3 (normale) si elle est à vendre
		Case d2 = new Case(2, "Case 2", "Ceci est la case normale");

		// Action
		boolean aVendre2 = d2.estAVendre();

		// Assert
		assertEquals(true, aVendre2);
	}

	@Test
	void caseNonAVendreNonProposee() {

		// Arrange
		initJeuUnJoueur();

		// Action
		// Le joueur arrive sur la case
		// Par défaut, l'IHM de test souhaite automatiquement acheter la case
		plateau.gestionArriveeCase(getCase(0), true);

		// Assert
		// Le joueur n'a pas pu acheter la case départ
		assertEquals(null, getCase(0).getJoueurProprietaire());

	}

	@Test
	void casePrisonExiste() {

		// Arrange
		initJeuUnJoueur();
		boolean prisonExiste = false;

		// Action
		for (Case oCase : plateau.getListeCases())
			if (oCase.getTitre().equals("Prison"))
				prisonExiste = true;

		// Assert
		assertEquals(true, prisonExiste);
	}

	@Test
	void casePrisonPasAVendre() {

		// Arrange
		initJeuUnJoueur();

		// Action
		// Le joueur arrive sur la prison
		// Par défaut, l'IHM de test souhaite automatiquement acheter la case
		plateau.gestionArriveeCase(getCase("Prison"), true);

		// Assert
		// Le joueur n'a pas pu acheter la case prison
		assertEquals(null, getCase("Prison").getJoueurProprietaire());
	}

	@Test
	void QuatreCasesChancesExistent() {

		initJeuUnJoueur();

		int compteur = 0;

		for (Case oCase : plateau.getListeCases())

			if (oCase.getTitre().equals("Chance"))
				compteur++;

		assertEquals(4, compteur);

	}

	@Test
	void casesChancePasAVendre() {

		// Arrange
		initJeuUnJoueur();

		plateau.gestionArriveeCase(getCase("Chance"), true);

		assertEquals(null, getCase("Chance").getJoueurProprietaire());
	}

	@Test
	void amendeEnArrivantSurCaseChance() {

		// Arrange
		Partie partie = new Partie();
		Joueur joueur = partie.getPlateau().getJoueurCourant();

		tireLaCarte(AMENDE, partie);

		int fortuneAvant = joueur.getFortune();

		partie.getPlateau().actionJoueur(PREMIER_COUP, PEUT_JOUER);

		int fortuneApres = joueur.getFortune();

		// Le joueur a bien payé son amende
		assertTrue(fortuneAvant > fortuneApres);
	}

	@Test
	void cadeauEnArrivantSurCaseChance() {

		// Arrange
		Partie partie = new Partie();
		Joueur joueur = partie.getPlateau().getJoueurCourant();

		tireLaCarte(CADEAU, partie);
		int fortuneAvant = joueur.getFortune();

		partie.getPlateau().actionJoueur(PREMIER_COUP, PEUT_JOUER);

		int fortuneApres = joueur.getFortune();

		// Le joueur a bien payé son amende
		assertTrue(fortuneAvant < fortuneApres);
	}

	@Test
	void carteChanceEnvoiDirectEnPrison() {

		// Arrange
		Partie partie = new Partie();

		Joueur joueur = partie.getPlateau().getJoueurCourant();

		tireLaCarte(PRISON, partie);

		partie.getPlateau().actionJoueur(PREMIER_COUP, PEUT_JOUER);

		assertEquals(Parametres.CASE_PRISON, joueur.getPosition());
		assertEquals(true, joueur.estPrisonnier());
	}

	// Si le joueur possède une carte spéciale "Sortir de prison", il peut
	// l'utiliser

	@Test
	void carteSortirDePrison() {

		// Arrange
		Partie partie = new Partie();
		Joueur joueur = partie.getPlateau().getJoueurCourant();

		// Le joueur arrive en prison

		DesPipes.setResultat(Parametres.CASE_PRISON);
		joueur.ajouterCarteSpeciale(SORTIR_DE_PRISON);

		partie.getPlateau().actionJoueur(PREMIER_COUP, PEUT_JOUER);

		// Le joueur est bien sorti de prison

		assertFalse(joueur.estPrisonnier());

	}

	@Test
	void joueurPerdCarteSpecialeApresUtilisation() {

		// Arrange
		Partie partie = new Partie();
		Joueur joueur = partie.getPlateau().getJoueurCourant();

		// Le joueur arrive en prison

		DesPipes.setResultat(Parametres.CASE_PRISON);
		joueur.ajouterCarteSpeciale(SORTIR_DE_PRISON);

		partie.getPlateau().actionJoueur(PREMIER_COUP, PEUT_JOUER);

		// Le joueur est bien sorti de prison

		assertFalse(joueur.estPrisonnier());

		assertEquals(1, joueur.getListesCartesSpeciales().size()); // Ils en ont déjà une au départ.

	}
	
	

	@Test
	void prixCaseAugmenteAvecNiveau() {

		// Arrange
		Partie partie = new Partie();
		Joueur joueur = partie.getPlateau().getJoueurCourant();
		Joueur joueur2 = partie.getPlateau().getListeJoueurs().get(1);

		int fortuneAvant = joueur2.getFortune();
		joueur.acheteTerrain(partie.getCase(1));

		partie.getPlateau().setJoueurCourant(joueur2);
		joueur2.setPosition(1);

		partie.getPlateau().gestionArriveeCase(partie.getCase(1), PEUT_JOUER);

		int fortuneApres = joueur2.getFortune();

		assertEquals(fortuneAvant - 200, fortuneApres);

	}

	@Test
	void prixCaseAugmenteAvecNiveauMaison() {

		// Arrange
		Partie partie = new Partie();

		Joueur joueur = partie.getPlateau().getJoueurCourant();
		Joueur joueur2 = partie.getPlateau().getListeJoueurs().get(1);

		int fortuneAvant = joueur2.getFortune();
		joueur.acheteTerrain(partie.getCase(1));
		joueur.upgradeCase(partie.getCase(1));

		partie.getPlateau().setJoueurCourant(joueur2);
		joueur2.setPosition(1);

		partie.getPlateau().gestionArriveeCase(partie.getCase(1), PEUT_JOUER);

		int fortuneApres = joueur2.getFortune();

		assertEquals(fortuneAvant - 400, fortuneApres);

	}

}
