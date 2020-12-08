package partida;
/** @file LlegirFitxers.java
 * @brief Lectura dels fitxers de Regles i Partida
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;

/** @class LlegirFitxers
 * @brief Classe de lectura de fitxers d'entrada
 */

public class LlegirFitxers {
    private String fitxerRegles;
    private TreeMap<String, TipusPeca> conjuntPeces = new TreeMap<String, TipusPeca>();       ///< Contenidor de tots els conjunts de peces
    private Taulell taulell = new Taulell(9,9);                         ///< Objecte que conté les dades de Taulell
    private int limitEscacsSeguits = 0;                                       ///< Torns que es pot estar sense matar
    private int limitTornsInaccio = 0;                                        ///< Limit d'escacs que es poden fer abans d'acabar la partida
    private String resultatFinal = "";                                        ///< Valor de resultat en que queda una Partida
    private String properTorn = "BLANQUES";                                   ///< Valor que diu qui comença el primer torn
    private String contingut;                                                 ///< String que conté tot el contingut del fitxer
    private JSONArray posIniBlanques;                                         ///< Llista de posicions inicials de l'equip de peces Blanques
    private JSONArray posIniNegres;                                           ///< Llista de posicions inicials de l'equip de peces Negres
    private JSONArray tirades;                                                ///< Llista de tirades realitzades.

    /** @brief Conjunt de Tirades */
    public JSONArray getTirades() {
        return tirades;
    }

    /** @brief Posicions inicials del equip de Peces blanques */
    public JSONArray getPosIniBlanques() {
        return posIniBlanques;
    }

    /** @brief Posicions inicials del equip de Peces negres */
    public JSONArray getPosIniNegres() {
        return posIniNegres;
    }

    /** @brief Contenidor del conjunt de Peces */
    public TreeMap<String, TipusPeca> getConjuntPeces() {
        return conjuntPeces;
    }

    /** @brief Taulell */
    public Taulell getTaulell() {
        return taulell;
    }

    /** @brief Escacs Seguits */
    public int getLimitEscacsSeguits() {
        return limitEscacsSeguits;
    }

    /** @brief Limit de Torns d'Inaccio */
    public int getLimitTornsInaccio() {
        return limitTornsInaccio;
    }

    /** @brief Resultat Final */
    public String getResultatFinal() {
        return resultatFinal;
    }

    /** @brief Jugador que comença el Proper Torn */
    public String getProperTorn() {
        return properTorn;
    }

    public String getFitxerRegles () { return fitxerRegles; }

    /** @brief  Llegeix el fitxer de Regles
     * @param path drecera del fitxer de Regles
     * @pre path és vàlid
     * @post totes les variables del fitxer de Regles estan assignades.
     */
    public void llegirRegles(String path, boolean comencada) throws Exception {
                String contingut = new String((Files.readAllBytes((Paths.get(path)))));
            JSONObject regles = new JSONObject(contingut);
            //Ara tenim guardabt tot el contingut del fitxer Regles al JSONObject regles, fem constructors.

            int nFiles = regles.getInt("nFiles");
            int nCols = regles.getInt("nCols");

            taulell = new Taulell(nCols, nFiles);

            JSONArray peces = regles.getJSONArray("peces");

            for (int i=0; i<peces.length(); i++) {

                ArrayList<Moviment> movimentNormal = new ArrayList<>();
                ArrayList<Moviment> movimentInicial = new ArrayList<>();

                JSONObject peca = peces.getJSONObject(i);
                String nom = peca.getString("nom");
                String simbol = peca.getString("simbol");
                String imatgeBlanca = peca.getString("imatgeBlanca");
                String imatgeNegra = peca.getString("imatgeNegra");
                int valor = peca.getInt("valor");

                JSONArray moviments = peca.getJSONArray("moviments");

                for (int j=0; j<moviments.length(); j++){
                    JSONArray moviment = moviments.getJSONArray(j);
                    String horitzontal = String.valueOf(moviment.get(0));
                    String vertical = String.valueOf(moviment.get(1));
                    int movMatar = moviment.getInt(2);
                    int movSaltar = moviment.getInt(3);

                    Moviment m = new Moviment(horitzontal,vertical,movMatar,movSaltar);

                    movimentNormal.add(m);
                }

                JSONArray movimentsInicials = peca.getJSONArray("movimentsInicials");

                for (int j=0; j<movimentsInicials.length(); j++){
                    JSONArray moviment = movimentsInicials.getJSONArray(j);
                    String horitzontal = String.valueOf(moviment.get(0));
                    String vertical = String.valueOf(moviment.get(1));
                    int movMatar = moviment.getInt(2);
                    int movSaltar = moviment.getInt(3);

                    Moviment mIni = new Moviment(horitzontal,vertical,movMatar,movSaltar);

                    movimentInicial.add(mIni);
                }

                boolean promocio = peca.getBoolean("promocio");
                boolean invulnerabilitat = peca.getBoolean(("invulnerabilitat"));

                TipusPeca tpeca = new TipusPeca(nom, simbol, imatgeBlanca, imatgeNegra, valor, movimentNormal, movimentInicial, promocio, invulnerabilitat);

                conjuntPeces.put(nom, tpeca);
            }

            JSONArray posInicial = regles.getJSONArray("posInicial");


            for (int i=0; i<posInicial.length(); i++) {
                String pecaIni = posInicial.getString(i);
                Peca pecaInicialBlanca = new Peca(pecaIni, true, conjuntPeces);
                Peca pecaInicialNegre = new Peca(pecaIni, false, conjuntPeces);
                if (!comencada)
                    taulell.afegirPeca(pecaInicialBlanca,pecaInicialNegre);
            }


            //Això ens ho emportem a Partida
            limitEscacsSeguits = regles.getInt("limitEscacsSeguits");
            limitTornsInaccio = regles.getInt("limitTornsInaccio");

            JSONArray enrocs = regles.getJSONArray("enrocs");

            for (int i=0; i<enrocs.length(); i++) {
                JSONObject enroc = enrocs.getJSONObject(i);
                String pecaA = enroc.getString("pecaA");
                String pecaB = enroc.getString("pecaB");
                boolean quiets = enroc.getBoolean("quiets");
                boolean buitAlMig = enroc.getBoolean("buitAlMig");
                CaracteristiquesEnroc ce = new CaracteristiquesEnroc(pecaA, pecaB, quiets, buitAlMig);
                conjuntPeces.get(pecaA).afegirEnrroc(ce);
            }
    }

    /** @brief  Llegeix el fitxer de Partida
     * @param path drecera del fitxer de Partida
     * @pre path és vàlid
     * @post totes les variables del fitxer de Partida estan assignades. La Partida està acabada.
     */
    public String llegirPartidaAcabada (String path) throws  Exception{
        try {
            contingut = new String((Files.readAllBytes(Paths.get(path))));
            JSONObject partidaN =  new JSONObject(contingut);
            String pathRegles = partidaN.getString("fitxerRegles");

            llegirRegles(pathRegles, true);

            posIniBlanques = partidaN.getJSONArray("posIniBlanques");

            for (int i=0; i<posIniBlanques.length(); i++) {
                JSONObject posicio = posIniBlanques.getJSONObject(i);

                String pos = posicio.getString("pos");
                String tipus = posicio.getString("tipus");
                boolean moguda = posicio.getBoolean("moguda");

                Peca p = new Peca(tipus, true, conjuntPeces);
                Posicio posicioB = new Posicio(pos);
                taulell.assignarPecaTauler(p, posicioB);
            }

            posIniNegres = partidaN.getJSONArray("posIniBlanques");

            for (int i=0; i<posIniNegres.length(); i++) {
                JSONObject posicio = posIniNegres.getJSONObject(i);

                String pos = posicio.getString("pos");
                String tipus = posicio.getString("tipus");
                boolean moguda = posicio.getBoolean("moguda");

                Peca p = new Peca(tipus, false, conjuntPeces);
                Posicio posicioN = new Posicio(pos);
                taulell.assignarPecaTauler(p, posicioN);
            }

            //ens ho enduem a partida
            properTorn = partidaN.getString("proper_torn");

            tirades = partidaN.getJSONArray("tirades");
            for (int i = 0; i<tirades.length(); i++) {
                JSONObject tirada = tirades.getJSONObject(i);

                String torn = tirada.getString("torn");
                String origen = tirada.getString("origen");
                String desti = tirada.getString("desti");
                String resultat = tirada.getString("resultat");

                //Ho hem de guardar a algun lloc
            }

            //Això ens diferencia de una partida que no esta acabada
            String resultatFinal = partidaN.getString("resultat_final");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contingut;
    }

    /** @brief  Llegeix el fitxer de Partida
     * @param path drecera del fitxer de Partida
     * @pre path és vàlid
     * @post totes les variables del fitxer de Partida estan assignades. La Partida no està acabada.
     */
    public String llegirPartidaComencada (String contingutFitxer, boolean comencada) throws Exception {
        contingut = contingutFitxer;
        JSONObject partidaN =  new JSONObject(contingut);
        fitxerRegles = partidaN.getString("fitxerRegles");

        llegirRegles(fitxerRegles, comencada);

        posIniBlanques = partidaN.getJSONArray("posIniBlanques");

        for (int i=0; i<posIniBlanques.length(); i++) {
            JSONObject posicio = posIniBlanques.getJSONObject(i);

            String pos = posicio.getString("pos");
            String tipus = posicio.getString("tipus");
            boolean moguda = posicio.getBoolean("moguda");

            Peca p = new Peca(tipus, true, conjuntPeces);
            Posicio posicioB = new Posicio(pos);
            taulell.assignarPecaTauler(p, posicioB);
        }

        posIniNegres = partidaN.getJSONArray("posIniNegres");

        for (int i=0; i<posIniNegres.length(); i++) {
            JSONObject posicio = posIniNegres.getJSONObject(i);

            String pos = posicio.getString("pos");
            String tipus = posicio.getString("tipus");
            boolean moguda = posicio.getBoolean("moguda");

            Peca p = new Peca(tipus, false, conjuntPeces);
            Posicio posicioN = new Posicio(pos);
            taulell.assignarPecaTauler(p, posicioN);
        }

        //ens ho enduem a partida
        properTorn = partidaN.getString("proper_torn");

        tirades = partidaN.getJSONArray("tirades");
        for (int i = 0; i<tirades.length(); i++) {
            JSONObject tirada = tirades.getJSONObject(i);

            String torn = tirada.getString("torn");
            String origen = tirada.getString("origen");
            String desti = tirada.getString("desti");
            String resultat = tirada.getString("resultat");

            //Ho hem de guardar a algun lloc
        }

        //Això ens diferencia de una partida que no esta acabada
        resultatFinal = partidaN.getString("resultat_final");
        return contingut;
    }
}
