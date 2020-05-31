package partida;
/** @file Historial.java
 * @brief Control dels events de la partida.
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

/** @class Historial
 * @brief Tirades i resultats que s'obtenen dirant la partida, també posicions inicials
 */
public class Historial {
    private static String adrecaFitxer = "Partida.txt";     ///< Adreça del fitxer que generarem i guardarem
    public static FileWriter fitxerPartida;                 ///< Standard Input d'escriptura que utilitzarem per a escriure el nostre fitxer

    public static JSONObject partida = new JSONObject();    ///< Objecte JSON on guardarem les dades de la Partida

    /** @brief  Inicialitzador de l'objecte JSON que guardarem
     * @pre --
     * @post Totes les variables estan inicialitzades
     */
    public static void iniciarPartidaNova(String fitxerRegles) throws Exception {
        String contingutFitxer;
        //Hauriem de comprovar si ja existeix el fitxer

        partida.put("resultat_final", "");

        partida.put("fitxerRegles", fitxerRegles);

        JSONArray posIniBlanques = new JSONArray();
        partida.put("posIniBlanques", posIniBlanques);

        JSONArray posIniNegres = new JSONArray();
        partida.put("posIniNegres", posIniNegres);

        partida.put("proper_torn", "");

        JSONArray tirades = new JSONArray();
        partida.put("tirades", tirades);


        File carpeta = new File(System.getProperty("user.dir"));
        File[] llistaDeFitxers = carpeta.listFiles();
        int nPartida = 0;

        if (!(llistaDeFitxers == null)) {
            for (int i = 0; i <= llistaDeFitxers.length - 1; i++) {
                if (llistaDeFitxers[i].isFile() && llistaDeFitxers[i].getName().substring(0, "Partida".length()).equalsIgnoreCase("Partida")) {
                    if (esEnter(llistaDeFitxers[i].getName().substring("Partida".length()).substring(0, 1))) {
                        if (Integer.parseInt(llistaDeFitxers[i].getName().substring("Partida".length()).substring(0, 1)) >= nPartida)
                            nPartida = (Integer.parseInt(llistaDeFitxers[i].getName().substring("Partida".length()).substring(0, 1)));
                    } if (esEnter(llistaDeFitxers[i].getName().substring("Partida".length()).substring(0, 2))) {
                        if (Integer.parseInt(llistaDeFitxers[i].getName().substring("Partida".length()).substring(0, 2)) >= nPartida)
                            nPartida = (Integer.parseInt(llistaDeFitxers[i].getName().substring("Partida".length()).substring(0, 2)));
                    } if (esEnter(llistaDeFitxers[i].getName().substring("Partida".length()).substring(0, 3))) {
                        if (Integer.parseInt(llistaDeFitxers[i].getName().substring("Partida".length()).substring(0, 3)) >= nPartida)
                            nPartida = (Integer.parseInt(llistaDeFitxers[i].getName().substring("Partida".length()).substring(0, 3)));
                    }
                }
            }
        }
        fitxerPartida = new FileWriter("Partida" + (nPartida + 1) + ".json");
    }

    public static void carregarPartidaAnterior(String path, LlegirFitxers fitxerEntradaPartida) throws IOException {

        partida.put("resultat_final", fitxerEntradaPartida.getResultatFinal());

        partida.put("fitxerRegles", fitxerEntradaPartida.getFitxerRegles());

        partida.put("posIniBlanques", fitxerEntradaPartida.getPosIniBlanques());

        partida.put("posIniNegres", fitxerEntradaPartida.getPosIniNegres());

        partida.put("proper_torn", fitxerEntradaPartida.getProperTorn());

        partida.put("tirades", fitxerEntradaPartida.getTirades());

        fitxerPartida = new FileWriter(path);
    }

    public static boolean esEnter(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    /** @brief  Guarda una Posició Inicial d'una Peçá
     * @param posicio Posició inicial de la Peça
     * @param peca Peça que guardem
     * @param moguda Atribut de si s'ha mogut respecta la posició inicial de la Partida
     * @param equip Atribut de quin Equip és la Peça
     * @pre --
     * @post totes les variables han estat guardades al objecte JSON
     */
    public static void guardarPosInicial(Posicio posicio, Peca peca, boolean moguda, boolean equip) {
        JSONObject infoPos = new JSONObject();
        infoPos.put("pos",posicio.get_posicio());
        infoPos.put("tipus",peca.getNom());
        infoPos.put("moguda",moguda);
        if (equip) {
            JSONArray posIniBlanques = partida.getJSONArray("posIniBlanques");
            posIniBlanques.put(infoPos);
        } else {
            JSONArray posIniNegres = partida.getJSONArray("posIniNegres");
            posIniNegres.put(infoPos);
        }
    }

    /** @brief  Guarda el fitxer de Regles al fitxar de Partida
     * @pre --
     * @post Nom del fitxer de Regles guardat al objecte Partida JSON
     */
    public static void guardarFitxerRegles (String nomFitxerRegles) {
        partida.put("fitxerRegles", nomFitxerRegles);
    }

    /** @brief  Guarda el Torn de qui li toca realitzar la Tirada
     * @pre --
     * @post Nom del Equip que ha de fer el proper Torn guardat al objecte Partida JSON
     */
    public static void guardarProperTorn (String properTorn) {
        partida.put("proper_torn", properTorn);
    }

    /** @brief  Guarda un Tirada amb el seu resultat
     * @param t Tiarada que s'ha realitzat
     * @param resultat Resultat de la tirada realitzada
     * @pre --
     * @post Tots els atributs de tiradaa i resultat guardats en el fitxer JSON
     */
    public static void guardarTirada(TiradaSimple t, String resultat) {
        JSONObject infoTirada = new JSONObject();
        String equip = "NEGRES";
        if (t.get_equip()) equip = "BLANQUES";
        infoTirada.put("torn", equip);
        infoTirada.put("origen", t.get_origen().get_posicio());
        infoTirada.put("desti", t.get_desti().get_posicio());
        infoTirada.put("resultat", resultat);
        JSONArray tirades = partida.getJSONArray("tirades");
        tirades.put(infoTirada);
    }

    /** @brief  Retorna l'ultima Tirada de que esta guardada
     * @pre S'ha realitzat alguna tirada
     * @post S'ha retornat l'objecte TiradaSimple
     */
    public static TiradaSimple getUltimaTirada() {
        JSONArray tirades = partida.getJSONArray("tirades");
        JSONObject tirada = tirades.getJSONObject(tirades.length() - 1);

        String torn = tirada.getString("torn");
        String origen = tirada.getString("origen");
        String desti = tirada.getString("desti");

        boolean equip = torn == "BLANQUES";
        Posicio o = new Posicio(origen);
        Posicio d = new Posicio(desti);
        return new TiradaSimple(o, d, equip);
    }

    /** @brief  Elimina l'ultima tirada realitzada
     * @pre S'ha realitzat alguna tirada
     * @post S'ha eliminat l'últim element de la llista de Tirades
     */
    public static void eliminarUltimaTirada() {
        JSONArray tirades = partida.getJSONArray("tirades");
        tirades.remove(tirades.length() - 1);
    }

    /** @brief  Partida guardad en un Fitxer i el valor del Resultat
     * @pre --
     * @post S'ha guardat una partida en un Fitxer assignant un resultat "" o amb valor.
     */
    public static void guardarPartida(String resultat) {
        //resultat = "BLANQUES", han guanyat les blanques, "NEGRES" viceversa
        //resultat = "TAULES"
        //resultat = "AJORNAT"

        String contingut = "";


        if (resultat.equalsIgnoreCase("BLANQUES")) {
            partida.put("resultat_final", "BLANQUES GUANYEN");
        } else if (resultat.equalsIgnoreCase("NEGRES")) {
            partida.put("resultat_final", "NEGRES GUANYEN");
        } else if (resultat.equalsIgnoreCase("TAULES")) {
            partida.put("resultat_final", "TAULES");
        } else {
            partida.put("resultat_final", "AJORNAMENT");
        }

        String a = partida.toString();
        try {
            fitxerPartida.write(a);
            fitxerPartida.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** @brief  Modifica el resultat de lúltima Tirada
     * @param resultat Valor a substituïr en l'ultima Tirada
     * @pre --
     * @post El resultat s'ha sobreescrit en l'ultim element de la llista de Tirades del objecte JSON
     */
    public static void modificarResultatUltimaTirada(String resultat) {
        JSONArray tirades = partida.getJSONArray("tirades");
        JSONObject tirada = tirades.getJSONObject(tirades.length() - 1);

        tirada.put("resultat", resultat);
    }

    /** @brief  Obté el resultat de l'última Tirada
     * @pre --
     * @post retorna el valor del resultat de l'última Tirada de la llista de Tirades
     */
    public static String getUltimResultat () {
        JSONArray tirades = partida.getJSONArray("tirades");
        JSONObject tirada = tirades.getJSONObject(tirades.length() - 1);

        return tirada.getString("resultat");
    }

    public static String getResultat (int i) {
        JSONArray tirades = partida.getJSONArray("tirades");
        JSONObject tirada = tirades.getJSONObject(i);

        return tirada.getString("resultat");
    }
    public static TiradaSimple getTirada(int i) {
        JSONArray tirades = partida.getJSONArray("tirades");
        JSONObject tirada = tirades.getJSONObject(i);

        String torn = tirada.getString("torn");
        String origen = tirada.getString("origen");
        String desti = tirada.getString("desti");

        boolean equip = torn == "BLANQUES";
        Posicio o = new Posicio(origen);
        Posicio d = new Posicio(desti);
        return new TiradaSimple(o, d, equip);
    }
    public static int longTiradades(){
        JSONArray tirades = partida.getJSONArray("tirades");
        return tirades.length()-1;
    }

    public static void afegirResultatUltimaTirada(String resultat) {
        JSONArray tirades = partida.getJSONArray("tirades");
        JSONObject tirada = tirades.getJSONObject(tirades.length() - 1);
        String s = tirada.getString("resultat");
        tirada.put("resultat", s+resultat);
    }

    public static boolean fitxerTiradesBuit(){
        JSONArray tirades = partida.getJSONArray("tirades");
        return tirades.length()==0;
    }
}
