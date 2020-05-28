package Partida;
/** @file Partida.java
 * @brief Joc d'escacs.
 */

import java.util.StringTokenizer;
import java.util.TreeMap;

import static Partida.Historial.*;

/** @class Partida
 * @brief Versió d'un joc d'escacs amb peces personalitzables i
 * taulells de mida variable.
 */
public class partida {
    TreeMap<String, TipusPeca> conjuntPeces = new TreeMap<>();     ///< Englobem totes les peces d'entrada en aquesta variable
    Taulell taulell = new Taulell(8,9);                      ///< Objecte que conté totes les dades de taulell
    String properTorn = "BLANQUES";                                ///< BLANQUES o NEGRES
    boolean colorTorn = true;                                      ///< true = BLANQUES, false = NEGRES
    int limitTornsInaccio = 0;                                     ///< Torns que es pot estar sense matar
    int limitEscacsSeguits = 0;                                    ///< Limit d'escacs que es poden fer abans d'acabar la partida
    Jugador jugadorBlanques = new Jugador (true);           ///< Objecte que conté les dades del jugador de BLANQUES
    Jugador jugadorNegres = new Jugador (false);            ///< Objecte que conté les dades del jugador de NEGRES
    int nJugadors;                                                 ///< Nombre de jugadors humans
    Historial historial = new Historial();                         ///< Modul que controla l'historial de tirades
    LlegirFitxers fitxerEntradaPartida = new LlegirFitxers();      ///< Modul que llegeix els fitxers d'entrada

    /** @brief  Genera una partida carregada
     * @param fitxerPartida nom del fitxer d'entrada
     * @pre fitxerPartida existeix i esta en un format correcte
     * @post totes les variables per a continuar una partida estan settejades.
     */
    public partida(String fitxerPartida) {
        //Hauriem de posar condicions per a saber quina partida començarem
        fitxerEntradaPartida.llegirPartidaComencada(fitxerPartida);
        conjuntPeces = fitxerEntradaPartida.getConjuntPeces();
        taulell = fitxerEntradaPartida.getTaulell();
        properTorn = fitxerEntradaPartida.getProperTorn();
        limitEscacsSeguits = fitxerEntradaPartida.getLimitEscacsSeguits();
        limitTornsInaccio = fitxerEntradaPartida.getLimitTornsInaccio();
    }

    /** @brief  Genera una partida carregada
     * @param fitxerRegles nom del fitxer d'entrada
     * @param jugadors nombre de jugadors humans
     * @pre fitxerRegles existeix i esta en un format correcte
     * @post totes les variables per a començar una partida estan settejades.
     */
    public partida(String fitxerRegles, int jugadors) {
        fitxerEntradaPartida.llegirRegles(fitxerRegles);
        conjuntPeces = fitxerEntradaPartida.getConjuntPeces();
        taulell = fitxerEntradaPartida.getTaulell();
        limitEscacsSeguits = fitxerEntradaPartida.getLimitEscacsSeguits();
        limitTornsInaccio = fitxerEntradaPartida.getLimitTornsInaccio();
        properTorn = "BLANQUES";
        nJugadors = jugadors;
    }

    /** @brief Canvia de torn */
    public String canviarTorn() {
        if (properTorn == "BLANQUES") {
            properTorn = "NEGRES";
        } else {
            properTorn = "BLANQUES";
        }
        return properTorn;
    }

    /** @brief  Comprova si la posició selecionada al origen és correcte
     * @param posicioIndefinida nom de la posició origen entrada per l'usuari
     * @pre --
     * @post Avisem del valor de la posició
     */
    public String posCorrecteOrigen (String posicioIndefinida) {
        boolean colorTorn = "BLANQUES" == properTorn;
        Posicio p = new Posicio(posicioIndefinida);

        if (taulell.contePeçaCasella(p)) {
            Peca pecaActual = taulell.getPeca(p);
            if (pecaActual.get_equip() == colorTorn) {
                return "Tot correcte";
            }
            else {
                return "Peça del color incorrecte";
            }
        }
        else {
            return "Posició invàlida";
        }
    }

    /** @brief  Comprova si la posició selecionada al destí és correcte
     * @param posicioIndefinida nom de la posició origen entrada per l'usuari
     * @pre --
     * @post Avisem del valor de la posició
     */
    public String posCorrecteDesti (String posicioIndefinida) {
        boolean colorTorn = "BLANQUES" == properTorn;
        Posicio p = new Posicio(posicioIndefinida);
        if (p.get_fila() > 0 && p.get_fila() <= taulell.getFiles() && p.get_columna() > 0 && p.get_columna() <= taulell.getColumnes()){
            if (taulell.contePeçaCasella(p)) {
                Peca pecaActual = taulell.getPeca(p);
                if (pecaActual.get_equip() != colorTorn) {
                    return "Tot correcte";
                } else {
                    return "Enrroc";
                }
            }
            else{ return "Tot correcte";}
        }
        else {
            return "Posició invàlida";
        }
    }

    /** @brief  Funció principal del programa: realitza una tirada un jugador
     * @param tirada posició origen i destí separats per un espai
     * @pre Les posicions han estat validades
     * @post S'ha realitzat una tirada
     */
    public String ferTirada (String tirada) {
        boolean colorTorn = "BLANQUES" == properTorn;
        Jugador jugadorActual = jugadorBlanques;
        if (colorTorn != jugadorBlanques.get_equip()) {
            jugadorActual = jugadorNegres;
        }

        StringTokenizer defaultTokenizer = new StringTokenizer(tirada);
        if (defaultTokenizer.countTokens() == 3) { //Enrroc
            Posicio origen = new Posicio((defaultTokenizer.nextToken())); // origen
            defaultTokenizer.nextToken(); // -
            Posicio desti = new Posicio((defaultTokenizer.nextToken())); //destí

            if (jugadorActual.ferEnrroc(taulell, origen, desti)) {
                return "s'ha realitzat el enrroc correctament";
            } else {
                return "no s'ha realitzat l'enrroc";
            }

        } else if (defaultTokenizer.countTokens() == 2) { //Tirada Normal
            Posicio origen = new Posicio((defaultTokenizer.nextToken())); //origen
            Posicio desti = new Posicio((defaultTokenizer.nextToken())); //destí

            int resultatTirada = jugadorActual.ferTirada(taulell, origen, desti);
            if(resultatTirada > 0){
                if(jugadorActual.ShaProvocatJaque(taulell)) {
                    return "no s'ha realitzat la tirada";
                }
                else if(jugadorActual.observarPromocio(desti,taulell)){
                    return "promocio";
                }
                else if(jugadorActual.ShaProvocatJaque(taulell)){
                    return "hi ha jaque";
                }
            }
            //0 no valid
            //1 tirada valida
            //>1 tirada valida + morts;
            if (resultatTirada == 1) {
                return "tirada vàlida";
            } else if (resultatTirada > 1) {
                return "return tirada vàlida i s'ha matat";
            } else {
                return "no s'ha realitzat la tirada";
            }
        }
        return "Alguna cosa ha sortit malament";
    }

    /** @brief  Acció de perdre la partida, cada jugador ho pot decidir en el seu torn
     * @pre --
     * @post Es tanca la partida amb el guanyador siguent l'equip contrari
     */
    public void rendirse () {
        if (properTorn == "BLANQUES") {
            Historial.guardarPartida("NEGRES");
        }
        else {
            Historial.guardarPartida("BLANQUES");
        }
    }

    /** @brief  Acció de empatar la partida, cada jugador ho pot decidir en el seu torn i l'altre hi ha d'estar d'acord.
     * @pre --
     * @post Es tanca la partida amb un empat
     */
    public void taules () {
        Historial.guardarPartida("TAULES");
    }

    /** @brief  Acció d'ajornar la partida
     * @pre --
     * @post Es tanca la partida, sense resultat, per a poder segui-la
     */
    public void ajornar () {
        Historial.guardarPartida("");
    }

    /** @brief  Acció de mostrar el taulell
     * @pre --
     * @post Es mostra el taulell en format text
     */
    public void mostrarTaulell () {
        System.out.println(taulell.mostra());
    }

    /** @brief  Peça de la posició p
     * @pre Posició p té una peça
     * @post Retorna la peça de la posició entrada
     */
    public Peca getPeca ( Posicio p) {
        return taulell.getPeca(p);
    }

    /** @brief Taulell */
    public Taulell getTaulell() {return taulell; }

    /** @brief Torn del proper jugador, BLANQUES o NEGRES */
    public String getProperTorn() { return properTorn; }

    /** @brief Limit de torns d'inanicció abans de tancar la partida */
    public int getLimitTornsInaccio() { return limitTornsInaccio; }

    /** @brief Limit d'escacs sense mat seguits abans de tancar la partida */
    public int getLimitEscacsSeguits () { return limitEscacsSeguits; }

    /** @brief nombre de Files que te el taulell */
    public int getFiles () { return taulell.getFiles(); }

    /** @brief Nombre de Columnes que te el taulell */
    public int getColumnes () { return taulell.getColumnes(); }

    /** @brief  Acció de promocionar una peça.
     * @param tirada posició origen i destí separats per un espai
     * @param nomPeça nom de la peça a la que s'hi vol promocionar
     * @pre posicions origen i destí vàlides.
     * @post S'ha promocinat la peça del jugador actual i s'avisa al principal si ha sortit bé o no.
     */
    public String ferPromocio(String tirada, String nomPeça){
        boolean colorTorn = "BLANQUES" == properTorn;
        Jugador jugadorActual = jugadorBlanques;
        if (colorTorn != jugadorBlanques.get_equip()) {
            jugadorActual = jugadorNegres;
        }

        if(nomPeça.equals("REI") || conjuntPeces.get(nomPeça) == null){
            return "no valid";
        }
        else{
            Peca p = new Peca(nomPeça, jugadorActual.get_equip(),conjuntPeces);
            StringTokenizer defaultTokenizer = new StringTokenizer(tirada);
            Posicio origen = new Posicio((defaultTokenizer.nextToken())); //origen
            Posicio desti = new Posicio((defaultTokenizer.nextToken())); //destí
            jugadorActual.ferPromocio(desti,taulell,p);
            if(jugadorActual.ShaProvocatJaque(taulell)){
                return "promocio feta i hi ha jaque";
            }
            else return "promocio feta";
        }
    }

    /** @brief  Desfem l'última tirada
     * @pre Hi ha alguna tirada per a desfer
     * @post Desfet l'ultima tirada al taulell i al fitxer de partida
     */
    public void desferTirada () {
        TiradaSimple ultimaTirada = getUltimaTirada();
        taulell.desferTirada(ultimaTirada);
        eliminarUltimaTirada();
    }

    /** @brief  Refem l'última tirada
     * @pre Hi ha alguna tirada per a refer
     * @post Desfet l'ultima tirada al taulell i al fitxer de partida
     */
//   public void referTirada () {
//       TiradaSimple ultimaTirada = taulell.referTirada();
//       guardarTirada(ultimaTirada, "");
//   }
}
