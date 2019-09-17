package Tests;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import Classes.Case;
import Classes.Des;
import Classes.Joueur;
import Classes.Parametres;
import Classes.Partie;
import Classes.Plateau;

class JoueurTest extends SuperTest {

	private static final String[] ACTION_SORTIR_DE_PRISON = new String[] { Parametres.SORTIR_DE_PRISON };

	@Test
	void ValeursInitialesDuJoueur() {

		// Arrange
		initJeuUnJoueur();
		// Assert
		assert (joueurA.getFortune() == Parametres.FORTUNE_INITIALE);
		assert (joueurA.getPosition() == 0);

	}

	@Test
	void DeplacementJoueur() {

		// Arrange
		initJeuUnJoueur();
		Des.lancer2Des();
		int indexDestination = joueurA.getPosition() + Des.getResultat();

		// Action
		joueurA.deplacer(Des.getResultat());

		Case caseDestination = getCase(indexDestination);

		// Assert
		assertNotEquals(joueurA.getPosition(), caseDepart.getNumero()); // le joueur n'est plus sur la case départ
		assertEquals(joueurA.getPosition(), caseDestination.getNumero()); // le joueur est sur la case arrivée

	}

	@Test
	void joueurPossedePlusieursCases() {

		// Arrange
		initJeuUnJoueur();

		// Action
		Case case2 = deplacementEtAchat(plateau, caseDepart, joueurA);
		Case case3 = deplacementEtAchat(plateau, case2, joueurA);
		Case case4 = deplacementEtAchat(plateau, case3, joueurA);

		Case[] casesAchetees = { case2, case3, case4 };

		ArrayList<Case> listePossessions = joueurA.getListesProprietes();

		// Assert
		assert (listePossessions.containsAll(Arrays.asList(casesAchetees)));

	}

	@Test
	void joueurPeutRejouerOuPas() {

		// Arrange
		initJeuUnJoueur();

		for (int i = 0; i < 150; i++) {
			int nbCoupsJoueur = 0;
			boolean peutJouer = true;
			boolean aRejoue = false;

			// Action
			while (peutJouer) {
				Des.lancer2Des();
				nbCoupsJoueur++;

				if (!Des.estUnDouble() || nbCoupsJoueur == 2) {
					peutJouer = false;

				} else {
					aRejoue = true;
				}
			}

			// Assert
			if (aRejoue)
				assert (nbCoupsJoueur == DEUXIEME_COUP);
			else
				assert (nbCoupsJoueur == PREMIER_COUP);
		}
	}

	public void prepareTourComplet() {
		initJeuUnJoueur();

		caseDepart = getCase(Parametres.NB_CASES - 1);

		joueurA.setPosition(Parametres.NB_CASES - 1);
	}

	@Test
	void joueurFaitUnTourComplet() {
		// Arrange
		prepareTourComplet();

		// Action
		joueurA.deplacer(1);

		// Assert
		assertEquals(joueurA.getPosition(), 0);
	}

	public void preparePassageCaseRapporteArgent() {
		initJeuUnJoueur();

		joueurA.setPosition(Parametres.NB_CASES - 1);

	}

	@Test
	void passageCaseDepartRapporteArgent() {

		// Arrange
		preparePassageCaseRapporteArgent();

		int ancienneFortune = joueurA.getFortune();

		// Action
		joueurA.deplacer(1);

		// Assert
		assertEquals(joueurA.getFortune(), ancienneFortune + Parametres.MONTANT_NOUVEAU_TOUR);
	}

	private Case deplacementEtAchat(Plateau plateau, Case caseDepart, Joueur joueur) {

		joueur.deplacer(Des.lancerUnDe());

		Case caseCourante = getCase(joueur.getPosition());

		joueur.acheteTerrain(caseCourante);

		return caseCourante;

	}

	@Test
	void joueurHorsCasePrisonPasEmprisonne() {

		// Arrange
		initJeuUnJoueur();
		// Action
		// Assert
		assertEquals(false, joueurA.estPrisonnier());

	}

	@Test
	void joueurSurPrisonEstEmprisonne() {

		// Arrange
		initJeuUnJoueur();

		joueurA.deplacer(Parametres.CASE_PRISON);

		plateau.gestionArriveeCase(getCase(Parametres.CASE_PRISON), true);

		// Assert
		assertEquals(true, joueurA.estPrisonnier());
	}

	@Test
	void prisonnierLibereSiLancerDouble() {

		// Arrange
		Partie partie = new Partie();

		Joueur joueurCourant = partie.getPlateau().getJoueurCourant();

		// Le joueur est déjà sur la case prison depuis 1 tour
		joueurCourant.deplacer(Parametres.CASE_PRISON);
		joueurCourant.setEstPrisonnier(true);

		// Action
		DesPipes.lancerDouble();

		partie.getPlateau().actionJoueur(PREMIER_COUP, PEUT_JOUER);

		// Assert
		assertEquals(false, joueurCourant.estPrisonnier());

	}

	@Test
	void prisonnierResteEnPrisonSiLancerSimple() {

		// Arrange
		Partie partie = new Partie();

		Joueur joueurCourant = partie.getPlateau().getJoueurCourant();

		// Le joueur est déjà sur la case prison depuis 1 tour
		joueurCourant.deplacer(Parametres.CASE_PRISON);
		joueurCourant.setEstPrisonnier(true);

		// Action
		DesPipes.lancerSimple();

		partie.getPlateau().actionJoueur(PREMIER_COUP, PEUT_JOUER);

		// Assert
		assertEquals(true, joueurCourant.estPrisonnier());

	}

	@Test
	void prisonnierChangeDeCaseSiLibereDePrison() {

		// Arrange
		Partie partie = new Partie();

		Joueur joueurCourant = partie.getPlateau().getJoueurCourant();

		// Le joueur est déjà sur la case prison depuis 1 tour
		joueurCourant.deplacer(Parametres.CASE_PRISON);
		joueurCourant.setEstPrisonnier(true);

		// Action
		DesPipes.lancerDouble();

		partie.getPlateau().actionJoueur(PREMIER_COUP, PEUT_JOUER);

		assertEquals(true, joueurCourant.getPosition() > Parametres.CASE_PRISON);

	}

	@Test
	void prisonnierResteSurCasePrisonSiLancerSimple() {

		// Arrange
		Partie partie = new Partie();

		Joueur joueurCourant = partie.getPlateau().getJoueurCourant();

		// Le joueur est déjà sur la case prison depuis 1 tour
		joueurCourant.deplacer(Parametres.CASE_PRISON);
		joueurCourant.setEstPrisonnier(true);

		// Action
		DesPipes.lancerSimple();

		partie.getPlateau().actionJoueur(PREMIER_COUP, PEUT_JOUER);

		assertEquals(true, joueurCourant.getPosition() == Parametres.CASE_PRISON);

	}

	@Test
	void joueurEnchaine3DoublesEtEstPrisonnier() {

		// Arrange
		Partie partie = new Partie();

		Joueur joueurCourant = partie.getPlateau().getJoueurCourant();

		partie.nbDoubles = 3;
		// Action
		partie.getPlateau().actionJoueur(TROISIEME_COUP, PEUT_JOUER);

		// Assert
		assertEquals(true, joueurCourant.estPrisonnier());

	}

	@Test
	void joueurEnchaine3DoublesEtVaSurCasePrison() {

		// Arrange
		Partie partie = new Partie();

		Joueur joueurCourant = partie.getPlateau().getJoueurCourant();
		joueurCourant.setPosition(8);
		
		partie.nbDoubles = 3;

		// Action
		partie.getPlateau().actionJoueur(TROISIEME_COUP, PEUT_JOUER);

		// Assert
		assertEquals(Parametres.CASE_PRISON, joueurCourant.getPosition());
	}

	@Test
	void joueurAcheteMaison() {

		// Arrange
		initJeuDeuxJoueurs();

		int fortuneAvantLoyer = joueurB.getFortune();

		// Actions
		joueurB.deplacer(1);
		Case caseCourante = getCase(joueurB.getPosition());

		joueurB.acheteTerrain(caseCourante);
		// Assert
		assertEquals(caseCourante.getNiveau(), 0);

		joueurB.acheteTerrain(caseCourante);

		// Assert
		assertEquals(caseCourante.getNiveau(), 1);
		assertEquals(fortuneAvantLoyer - caseCourante.getPrix() * 2, joueurB.getFortune());

	}

	@Test
	void joueurNePeutAmeliorerGare() {

		// Arrange
		initJeuDeuxJoueurs();

		// Actions
		Case caseCourante = getCase(7);

		joueurB.acheteTerrain(caseCourante);

		joueurB.deplacer(7);

		// Assert
		assertEquals(caseCourante.getNiveau(), 0);

	}

	@Test
	void joueurPayeAmendePrison() {

		// Arrange
		initJeuUnJoueur();

		int fortuneAvantLoyer = joueurA.getFortune();
		joueurA.deplacer(Parametres.CASE_PRISON);
		plateau.gestionArriveeCase(getCase(Parametres.CASE_PRISON), true);

		// Action
		joueurA.payeAmendePrison();

		// Assert
		assertEquals(fortuneAvantLoyer - Parametres.MONTANT_AMENDE_PRISON, joueurA.getFortune());
		assertEquals(false, joueurA.estPrisonnier());
	}

	@Test
	void joueurSurCarteChanceRelanceLeDé() {

		Partie partie = new Partie();

		tireLaCarte(REJOUER, partie);

		boolean peutRejouer = partie.getPlateau().actionJoueur(PREMIER_COUP, PEUT_JOUER);

		assertTrue(peutRejouer);

	}

	@Test
	void joueurObtientCarteSpecialeSurCaseChance() {

		// Arrange
		Partie partie = new Partie();
		Joueur joueur = partie.getPlateau().getJoueurCourant();

		placerJoueurAvantCaseChance(partie);

		partie.getPlateau().actionJoueur(PREMIER_COUP, PEUT_JOUER);

		// Le joueur obtient bien une carte spéciale "Sortir de prison"

		assertEquals(2, joueur.getListesCartesSpeciales().size());

	}

	public void placerJoueurAvantCaseChance(Partie partie) {
		Joueur joueurCourant = partie.getPlateau().getJoueurCourant();

		int positionCarteChance = 0;

		// On positionne le joueur juste avant la carte chance
		for (Case oCase : partie.getPlateau().getListeCases())

			if (oCase.getTitre().equals("Chance")) {
				positionCarteChance = oCase.getNumero();
				oCase.setActions(ACTION_SORTIR_DE_PRISON);
				joueurCourant.setPosition(positionCarteChance - 3);
				break;
			}
		DesPipes.setResultat(3);
	}

}
