package Classes;

import java.util.ArrayList;

public class Joueur {

	private String nom;
	private int fortune;
	private int position; // le numéro de la case sur lequel le joueur est positionné

	private ArrayList<Case> listeProprietes;
	private boolean estPrisonnier;
	private ArrayList<String> listesCartesSpeciales;

	public Joueur(String nom) {
		this.nom = nom;
		this.fortune = Parametres.FORTUNE_INITIALE;
		this.position = Parametres.CASE_DEPART;
		this.listeProprietes = new ArrayList<>();
		this.listesCartesSpeciales = new ArrayList<>();

	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getFortune() {
		return fortune;
	}

	public void setFortune(int fortune) {
		this.fortune = fortune;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void deplacer(int lancerDe) {

		// On retire le joueur de sa case initiale

		int nouvelIndex = (position + lancerDe) % Parametres.NB_CASES;

		// Si le joueur repasse par la case départ, il touche un certain montant

		if (position + lancerDe > Parametres.NB_CASES - 1) {
			fortune += Parametres.MONTANT_NOUVEAU_TOUR;
			System.out.println("Le joueur " + nom + " repasse par la case départ et touche "
					+ Parametres.MONTANT_NOUVEAU_TOUR + ".");
			System.out.println("Le joueur " + nom + " possède désormais " + fortune + " €.");
		}

		position = nouvelIndex;

	}

	public void acheteTerrain(Case caseCourante) {

		fortune -= caseCourante.getPrix();

		caseCourante.setJoueurProprietaire(this);
		caseCourante.setNiveau(caseCourante.getNiveau() + 1);
		listeProprietes.add(caseCourante);
	}

	public void upgradeCase(Case caseCourante) {
		caseCourante.setNiveau(caseCourante.getNiveau() + 1);
		fortune -= caseCourante.getPrix();
		caseCourante.setPrix(Parametres.PRIX_DE_BASE * (int) Math.pow(2, caseCourante.getNiveau()));
	}

	public ArrayList<Case> getListesProprietes() {

		return this.listeProprietes;
	}

	public void ajouterPropriete(Case oCase) {
		this.listeProprietes.add(oCase);
	}

	public void afficherInformations() {
		String info = "FORTUNE : " + fortune + " €\n ";

		if (!listeProprietes.isEmpty()) {
			info += "BIENS : \n";
			for (Case oCase : listeProprietes)
				info += "\n  - " + oCase.resume() + " | " + Parametres.mapNiveau.get(oCase.getNiveau()) + " - " + oCase.getPrix() + " € .";

		} else
			info += "Le joueur ne possède aucun bien.";

		System.out.println(info);
		System.out.println(Partie.LIGNE_POINTILLES);
		System.out.println("ACTION JOUEUR : ");

	}

	public ArrayList<Case> getListeProprietes() {
		return listeProprietes;
	}

	public void setListeProprietes(ArrayList<Case> listeProprietes) {
		this.listeProprietes = listeProprietes;
	}

	public boolean estPrisonnier() {
		return estPrisonnier;
	}

	public void setEstPrisonnier(boolean estPrisonnier) {
		this.estPrisonnier = estPrisonnier;
	}

	public void payeAmendePrison() {
		this.estPrisonnier = false;
		this.fortune -= Parametres.MONTANT_AMENDE_PRISON;
	}

	public void ajouterCarteSpeciale(String carteSpeciale) {
		this.listesCartesSpeciales.add(carteSpeciale);

	}
	
	public void retirerCarteSpeciale(String carteSpeciale) {
		this.listesCartesSpeciales.remove(carteSpeciale);

	}
	
	

	public ArrayList<String> getListesCartesSpeciales() {
		return listesCartesSpeciales;
	}

}
