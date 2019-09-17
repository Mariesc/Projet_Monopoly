package Classes;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import Classes.Parametres.groupe;

public class Plateau {

	private static final int DEUXIEME_COUP = 2;
	private ArrayList<Case> listeCases;
	private ArrayList<Joueur> listeJoueurs;
	private ArrayList<Case> listeCasesGares;

	static int nombreCases = Parametres.NB_CASES;
	private Joueur joueurCourant;
	private Partie controller;

	public boolean finDePartie;

	public Plateau(Partie controller) {

		listeCases = new ArrayList<>();
		listeJoueurs = new ArrayList<>();
		listeCasesGares = new ArrayList<>();
		this.controller = controller;
	}

	public Plateau() {
		listeCases = new ArrayList<>();
		listeJoueurs = new ArrayList<>();
		listeCasesGares = new ArrayList<>();
	}

	public ArrayList<Case> getListeCases() {
		return listeCases;
	}

	public void initialiserPlateau() {

		int num = 0;
		Case caseDepart = new Case(num, "Case d�part", "La case d�part");
		listeCases.add(caseDepart);

		for (groupe groupe : Parametres.groupe.values()) {

			for (String nomCase : groupe.getListeCases()) {
				num++;
				Case oCase = new Case(num, nomCase, groupe.name());
				listeCases.add(oCase);

				if (nomCase.indexOf("Gare") != -1) {
					listeCasesGares.add(oCase);
				}

				if (nomCase.equals("Prison"))
					Parametres.CASE_PRISON = num;

			}

		}

		Parametres.NB_CASES = num + 1;
		nombreCases = num + 1;

	}

	public ArrayList<Case> getListePositionGares() {
		return listeCasesGares;
	}

	public void setListeJoueurs(ArrayList<Joueur> listeJoueurs) {
		this.listeJoueurs = listeJoueurs;
	}

	public ArrayList<Joueur> getListeJoueurs() {
		return listeJoueurs;
	}

	public static int getNombreCases() {
		return nombreCases;
	}

	public static void setNombreCases(int nombreCases) {
		Plateau.nombreCases = nombreCases;
	}

	public Joueur getJoueurCourant() {
		return joueurCourant;
	}

	public void setJoueurCourant(Joueur joueurCourant) {
		this.joueurCourant = joueurCourant;
	}

	public void setListeCases(ArrayList<Case> listeCases) {
		this.listeCases = listeCases;
	}

	public boolean peuxPayer(Case caseCourante) {
		return (this.joueurCourant.getFortune() >= caseCourante.getPrix());
	}

	public void failliteJoueur() {

		System.out.println("Le joueur " + joueurCourant.getNom() + " a fait faillite : il/elle est �limin�(e).\n");
		listeJoueurs.remove(joueurCourant);

		Consumer<Case> cSupprProprio = c -> c.setJoueurProprietaire(null);
		Consumer<Case> cResetNiveau = c -> c.setNiveau(-1);
		Consumer<Case> cPrint = c -> System.out.println("La case " + c.resume() + " a �t� remise � la banque.");

		listeCases.stream().filter(c -> c.getJoueurProprietaire() != null)
				.filter(c -> c.getJoueurProprietaire().equals(joueurCourant))
				.forEach(cSupprProprio.andThen(cResetNiveau).andThen(cPrint));

		if (listeJoueurs.size() != 0) {
			System.out.println("Joueurs restants : ");
			listeJoueurs.forEach(j -> j.afficherInformations());
		}

		if (listeJoueurs.size() <= 1)
			affichageFinPartie();

	}

	private void affichageFinPartie() {

		if (listeJoueurs.size() != 0) {
			String gagnant = listeJoueurs.get(0).getNom();
			System.out.println("Joueur gagnant : " + gagnant);
		}
		System.out.println("La partie est termin�e.");

		finDePartie = true;
	}

	public void payerLoyer(Case caseCourante) {

		int prix = caseCourante.getPrix();
		Joueur proprietaire = caseCourante.getJoueurProprietaire();

		// Le joueur courant perd de l'argent
		this.joueurCourant.setFortune(joueurCourant.getFortune() - prix);
		System.out.println("Le joueur " + joueurCourant.getNom() + " a pay� " + prix + "� aupr�s de "
				+ proprietaire.getNom() + ".");

		// Le propri�taire gagne l'argent
		proprietaire.setFortune(proprietaire.getFortune() + prix);
	}

	private boolean arriveEnPrison() {
		return joueurCourant.getPosition() == getCase("Prison").getNumero() && !joueurCourant.estPrisonnier();
	}

	/**
	 * Gestion des actions r�alis�es sur cette case
	 */

	public boolean gestionArriveeCase(Case caseCourante, boolean peutJouer) {
		System.out.println(caseCourante.resume());

		// Si la case est une case Chance, on r�alise les actions de cette case
		if (caseChance()) {
			// R�cup�ration de la carte chance
			Case caseChance = carteChance();
			peutJouer = actionCarteChance(caseChance);
		}

		// Si cette case est la case prison et qu'il n'�tait pas d�j� prisonnier, on met
		// le joueur est prison
		if (arriveEnPrison()) {

			if (joueurCourant.getListesCartesSpeciales().contains(Parametres.SORTIR_DE_PRISON)) {

				boolean utiliseCarteSpeciale = controller.choixUtilisationCarteSpeciale();

				if (!utiliseCarteSpeciale) {
					System.out.println(
							"Le joueur refuse d'utiliser sa carte sp�ciale [Sortir de prison] pour des raisons de strat�gie.");
					fairePrisonnier();
				} else {
					System.out.println(
							"Le joueur utilise sa carte sp�ciale [Sortir de Prison] ! Il n'y mettra pas les pieds, tel un cadre de l'UMP.");
					joueurCourant.getListesCartesSpeciales().remove(Parametres.SORTIR_DE_PRISON);

				}
			} else {
				System.out.println("Le joueur n'a pas de carte sp�ciale [Sortir de prison] et va en prison.");
				fairePrisonnier();
				peutJouer = false;
			}
		}

		// Si la case n'est pas � vendre on ne fait rien

		if (!caseCourante.estAVendre())
			return peutJouer;

		// Si la case n'est � personne (terrain nu/gare � vendre), on propose l'achat
		if (caseCourante.getJoueurProprietaire() == null) {

			gestionAchat(caseCourante);

			if (!Des.estUnDouble())
				peutJouer = false;

		}

		else if (joueurCourantProprietaire(caseCourante) && !this.getListePositionGares().contains(caseCourante)) {
			gestionUpgrade(caseCourante);

			if (!Des.estUnDouble())
				peutJouer = false;
		}
		// Sinon, la case appartient d�j� � quelqu'un, on demande de payer le loyer
		else {
			gestionLoyer(caseCourante);

			if (!Des.estUnDouble())
				peutJouer = false;
		}
		// Si la partie est termin�e, on ne peut plus jouer
		if (finDePartie)
			peutJouer = false;

		return peutJouer;

	}

	/**
	 * Gestion de la carte chance Action r�alis�e lorsque le joueur est sur la case
	 * Chance
	 */
	public boolean actionCarteChance(Case caseChance) {

		boolean peutJouer = false;
		System.out.println("Carte CHANCE ! ");

		switch (caseChance.getAction()) {

		case "Amende":
			joueurCourant.setFortune(joueurCourant.getFortune() - Parametres.MONTANT_AMENDE_CHANCE);
			System.out.println("Le joueur paye une amende : " + Parametres.MONTANT_AMENDE_CHANCE + " �.");
			break;
		case "Cadeau":
			System.out.println("Le joueur gagne " + Parametres.MONTANT_CADEAU_CHANCE + ".");
			joueurCourant.setFortune(joueurCourant.getFortune() + Parametres.MONTANT_CADEAU_CHANCE);
			break;
		case "Prison":
			System.out.println("Le joueur part imm�diatement en prison...");

			// gestionArriveeCase(Parametres.CASE_PRISON, true);
			fairePrisonnier();
			break;
		case "Rejouer":
			System.out.println("Le joueur relance les d�s !");
			peutJouer = true;
			break;
		case Parametres.SORTIR_DE_PRISON:
			System.out.println("Le joueur obtient une cart sp�ciale [Sortir de prison]");
			joueurCourant.ajouterCarteSpeciale(Parametres.SORTIR_DE_PRISON);
			break;
		}

		return peutJouer;
	}

	public boolean gestionDeplacement(boolean peutJouer) {
		// D�placement du joueur en fonction de son lanc� de d�s
		joueurCourant.deplacer(Des.getResultat());
		// On r�cup�re la case d'arriv�e du joueur
		Case caseCourante = listeCases.get(joueurCourant.getPosition());
		// Gestion des actions r�alis�es sur cette case
		peutJouer = gestionArriveeCase(caseCourante, peutJouer);

		return peutJouer;
	}

	/**
	 * Met le joueur courant en prison
	 */
	private void fairePrisonnier() {
		joueurCourant.setEstPrisonnier(true);
		joueurCourant.setPosition(Parametres.CASE_PRISON);
		System.out.println("Le joueur " + joueurCourant.getNom() + " vient d'arriver en prison.");
	}

	/**
	 * Gestion de l'action du joueur apr�s qu'il ait lanc� les d�s
	 * 
	 * @param nbCoupsJoueur nombre de fois d'affil� que le joueur � lanc� les d�s
	 * @param peutJouer     si le joueur � le droit de jouer
	 * @return peutJouer qui a pu �voluer suite � ses actions
	 */
	public boolean actionJoueur(int nbCoupsJoueur, boolean peutJouer) {

		// Si il a trop fait de doubles d'affil�, le mettre en prison
		if (controller.nbDoubles == Parametres.LANCERS_DOUBLES_MAX) {
			fairePrisonnier();
			System.out
					.println("Le joueur " + joueurCourant.getNom() + " a jou� 3 doubles d'affil�e : il va en prison.");
			return false;
		}

		// Remise � FAUX de la sauvegarde de l'�tat prisonnier
		boolean etaitPrisonnier = false;

		// Si le joueur prisonnier
		if (joueurCourant.estPrisonnier()) {
			etaitPrisonnier = true;
			System.out.print("Est en prison...");

			// Il n'a malheureusement pas fait de double
			if (!Des.estUnDouble()) {
				peutJouer = false;
				System.out.println("et ne peux pas en sortir (il fallait faire un double !)");
				// Il a fait un double et est lib�r�
			} else {
				joueurCourant.setEstPrisonnier(false);
				System.out.println("et est lib�r�(e) !");
			}
		}

		// Si le joueur courant peut jouer IL SE DEPLACE avec son lanc� de d�s
		if (peutJouer)
			peutJouer = gestionDeplacement(peutJouer);

		// Si le joueur �tait prisonnier OU qu'il a fait 2 lanc�s d'affil�s,
		// il ne peux plus jouer
		else if (!Des.estUnDouble() || etaitPrisonnier || nbCoupsJoueur == DEUXIEME_COUP)
			peutJouer = false;

		return peutJouer;
	}

	public Case carteChance() {

		int positionJoueur = joueurCourant.getPosition();

		Case oCase = getCase(positionJoueur);

		return oCase.getTitre().equals("Chance") ? oCase : null;
	}

	public void gestionAchat(Case caseCourante) {

		// Si le joueur peut payer
		if (peuxPayer(caseCourante)) {

			// Passage par contr�leur et demande � la vue si elle souhaite acheter
			int choix = controller.choixAchatCase();

			// Si le joueur veut acheter, il ach�te
			if (choix == 1) {
				joueurCourant.acheteTerrain(caseCourante);
				System.out.println(
						"Le joueur " + caseCourante.getJoueurProprietaire().getNom() + " ach�te cette case.\n");
			}

			// Si il ne peux pas payer, on l'indique et on attend une seconde si on est au
			// clavier

		} else {
			System.out.println("Vous n'avez pas assez d'argent pour acheter la case !\n");
			if (controller.ihm.getClass().getSimpleName().equals("IHMClavier"))
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}

	}

	/**
	 * Si le joueur peux payer le loyer au proprio il paye, sinon il fait faillite
	 */
	public void gestionLoyer(Case caseCourante) {

		System.out.println("Le joueur " + caseCourante.getJoueurProprietaire().getNom()
				+ " poss�de cette case. Vous devez payer un loyer.\n");
		if (peuxPayer(caseCourante))
			payerLoyer(caseCourante);
		else
			failliteJoueur();

	}

	/**
	 * Si le joueur peux payer l'upgrade il fait l'upgrade
	 */
	private void gestionUpgrade(Case caseCourante) {
		if (peuxPayer(caseCourante)) {
			// Passage par contr�leur et demande � la vue si elle souhaite am�liorer
			if (controller.choixAmeiolationPatrimoine() == 1 && caseCourante.getNiveau() < 5) {
				joueurCourant.upgradeCase(caseCourante);
				System.out.print("Vous avez am�lior� " + caseCourante.resume() + "\n");
			}
		} else {
			System.out.println("Vous n'avez pas assez d'argent pour am�liorer la case !\n");
		}

	}

	protected Case getCase(String nomCase) {

		for (Case oCase : listeCases)
			if (oCase.getTitre().equals(nomCase))
				return oCase;

		return null;

	}

	protected Case getCase(int index) {
		return listeCases.get(index);
	}

	public boolean joueurCourantProprietaire(Case caseCourante) {
		return (caseCourante.getJoueurProprietaire() == joueurCourant);
	}

	private boolean caseChance() {
		return joueurCourant.getPosition() == getCase("Chance").getNumero();
	}

}
