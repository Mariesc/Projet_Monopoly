package Classes;

import java.util.Random;

public class Case {
	int numero;
	String titre;
	String groupe;
	Joueur joueurProprietaire;
	int niveau = -1;
	private int prix;
	private boolean aVendre = true;
	private String[] actions = { "Amende", "Rejouer", "Cadeau", "Prison", Parametres.SORTIR_DE_PRISON };

	public Case(int numero, String titre, String description) {

		this.numero = numero;
		this.titre = titre;
		this.groupe = description;
		this.prix = Parametres.PRIX_DE_BASE;

		if (caseSpeciale())
			aVendre = false;
	}

	private boolean caseSpeciale() {
		return numero == 0 || titre.equals("Prison") || titre.equals("Chance");
	}

	
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDescription() {
		return groupe;
	}

	public void setDescription(String description) {
		this.groupe = description;
	}

	public Joueur getJoueurProprietaire() {
		return joueurProprietaire;
	}

	public void setJoueurProprietaire(Joueur joueurProprietaire) {
		this.joueurProprietaire = joueurProprietaire;
	}

	@Override
	public String toString() {

		String info = "Informations de la case n° " + numero + " : " + titre + " [ " + groupe + " ]\n";

		info += " Joueur propriétaire : ";
		info += joueurProprietaire != null ? joueurProprietaire.getNom() : " aucun propriétaire.";
		info += " \nJoueurs présents sur la case : ";

		return info;
	}

	public String resume() {

		return "Case n° " + numero + " : " + titre + " [ " + groupe + " ]";

	}

	public int getPrix() {
		return prix;
	}

	public String getGroupe() {
		return groupe;
	}

	public void setGroupe(String groupe) {
		this.groupe = groupe;
	}

	public int getNiveau() {
		return niveau;
	}

	public void setNiveau(int niveau) {
		this.niveau = niveau;
	}

	public void setPrix(int prix) {
		this.prix = prix;
	}

	public boolean estAVendre() {

		return aVendre;
	}

	public void setActions(String[] actions) {
		this.actions = actions;
	}

	public String getAction() {

		int i = new Random().nextInt(actions.length);

		return actions[i];
	}

}
