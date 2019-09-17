package Classes;

import java.util.HashMap;

public class Parametres {

	public static int NB_CASES;
	public static final int FORTUNE_INITIALE = 2500;
	public static final int MONTANT_NOUVEAU_TOUR = 200;
	public static final int MONTANT_AMENDE_PRISON = 50;
	public static final int MONTANT_AMENDE_CHANCE = 200;
	public static final int MONTANT_CADEAU_CHANCE = 200;
	public static final int PRIX_DE_BASE = 200;
	public static final String SORTIR_DE_PRISON = "SortirPrison";
	
	public static final int LANCERS_DOUBLES_MAX = 3;
	

	public static final int CASE_DEPART = 0;
	public static int CASE_PRISON;

	public static final String[] groupeMarron = { "Boulevard de Belleville", "Rue Lecourbe" };

	public static final String[] groupeBleuCiel = { "Rue de Vaugirard", "Rue de Courcelles",
	"Avenue de la République" };

	public static final String[] groupeViolet = { "Boulevard de la Villette", "Avenue de Neuilly", "Rue de Paradis" };

	public static final String[] groupeOrange = { "Avenue Mozart", "Boulevard Saint-Michel", "Place Pigalle" };

	public static final String[] groupeRouge = { "Avenue Matignon", "Boulevard Malesherbes", "Avenue Henri-Martin" };

	public static final String[] groupeJaune = { "Faubourg Saint-Honoré", "Place de la Bourse", "Rue La Fayette" };

	public static final String[] groupeVert = { "Avenue de Breteuil", "Avenue Foch", "Boulevard des Capucines" };

	public static final String[] groupeBleu = { "Avenue des Champs-Élysées", "Rue de la Paix" };

	public static final String[] groupeGare1 = { "Gare du Nord" };

	public static final String[] groupeGare2 = { "Gare du Sud" };

	public static final String[] groupeGare3 = { "Gare de Lyon" };

	public static final String[] groupeGare4 = { "Gare St Lazarre" };

	public static final String[] groupePrison = { "Prison" };

	public static final String[] groupeChance1 = { "Chance" };

	public static final String[] groupeChance2 = { "Chance" };

	public static final String[] groupeChance3 = { "Chance" };

	public static final String[] groupeChance4 = { "Chance" };
	


	public static enum groupe {
		marron(groupeMarron), chance1(groupeChance1), bleuCiel(groupeBleuCiel), gare1(groupeGare1),
		prison(groupePrison), violet(groupeViolet), chance2(groupeChance2), orange(groupeOrange), gare3(groupeGare3),
		chance3(groupeChance3), rouge(groupeRouge), jaune(groupeJaune), gare4(groupeGare4), vert(groupeVert),
		chance4(groupeChance4), bleu(groupeBleu), gare2(groupeGare2);

		private final String[] listeCases;

		groupe(String[] listeCases) {

			this.listeCases = listeCases;

		}

		public String[] getListeCases() {
			return this.listeCases;
		}

	}

	public static final HashMap<Integer, String> mapNiveau = new HashMap<Integer, String>() {
		/**
		 * Correspondance des niveau des case avec le nombre de maison ou hotel
		 */
		private static final long serialVersionUID = 1L;

		{
			put(0, "Terrain");
			put(1, "Une maison");
			put(2, "Deux maisons");
			put(3, "Trois maisons");
			put(4, "Hotel");
		}
	};

	
	

}
