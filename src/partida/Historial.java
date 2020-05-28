package partida;
/** @file Historial.java
 * @brief Control dels events de la partida.
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

/** @class Historial
 * @brief Tirades i resultats que s'obtenen dirant la partida, també posicions inicials
 */
public class Historial {
    private static String adrecaFitxer = "Partida.txt";     ///< Adreça del fitxer que generarem i guardarem
    public static FileWriter fitxerPartida;                 ///< Standard Input d'escriptura que utilitzarem per a escriure el nostre fitxer

    public static JSONObject partida = new JSONObject();    ///< Objecte JSON on guardarem les dades de la Partida

    /** @brief Try Catch per a comprovar excepcions del SO on s'executi el programa */
    static {
        try {
            fitxerPartida = new FileWriter("PartidaNova.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** @brief  Inicialitzador de l'objecte JSON que guardarem
     * @pre --
     * @post Totes les variables estan inicialitzades
     */
    public static void iniciarPartidaNova() {
        String contingutFitxer;
        //Hauriem de comprovar si ja existeix el fitxer

        partida.put("resultat_final", "");

        partida.put("fitxerRegles", "");

        JSONArray posIniBlanques = new JSONArray();
        partida.put("posIniBlanques", posIniBlanques);

        JSONArray posIniNegres = new JSONArray();
        partida.put("posIniNegres", posIniNegres);

        partida.put("proper_torn", "");

        JSONArray tirades = new JSONArray();
        partida.put("tirades", tirades);

    }

    /** @brief  Guarda una Posició Inicial d'una Peçá
     * @param args [0] Posició on comença una Peça i [1]
     * @param moguda Atribut de si s'ha mogut respecta la posició inicial de la Partida
     * @param equip Atribut de quin Equip és la Peça
     * @pre --
     * @post totes les variables han estat guardades al objecte JSON
     */
    public static void guardarPosInicial(String [] args, boolean moguda, boolean equip) {
        JSONObject infoPos = new JSONObject();
        infoPos.put("pos",args[0]);
        infoPos.put("tipus",args[1]);
        infoPos.put("moguda",moguda);
        if (equip == true) {
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


        if (resultat == "BLANQUES") {
            partida.put("resultat_final", "BLANQUES GUANYEN");
        } else if (resultat == "NEGRES") {
            partida.put("resultat_final", "NEGRES GUANYEN");
        } else if (resultat == "TAULES") {
            partida.put("resultat_final", "TAULES");
        } else {
            partida.put("resultat_final", "");
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
}
