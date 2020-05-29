package partida;
/** @file Partida.java
 * @brief Partida d'escacs.
 */

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.TreeMap;

import static partida.Historial.*;

/** @class Partida
 * @brief Versió d'un joc d'escacs amb peces personalitzables i
 * taulells de mida variable.
 */
public class Partida {
    private TreeMap<String, TipusPeca> conjuntPeces = new TreeMap<>();     ///< Englobem totes les peces d'entrada en aquesta variable
    private Taulell taulell = new Taulell(8,9);                      ///< Objecte que conté totes les dades de taulell
    private String properTorn = "BLANQUES";                                ///< BLANQUES o NEGRES
    private boolean colorTorn = true;                                      ///< true = BLANQUES, false = NEGRES
    private int limitTornsInaccio = 0;                                     ///< Torns que es pot estar sense matar
    private int limitEscacsSeguits = 0;                                    ///< Limit d'escacs que es poden fer abans d'acabar la partida
    private Jugador jugadorBlanques = new Jugador (true);           ///< Objecte que conté les dades del jugador de BLANQUES
    private Jugador jugadorNegres = new Jugador (false);            ///< Objecte que conté les dades del jugador de NEGRES
    private int nJugadors;                                                 ///< Nombre de jugadors humans
    private Historial historial = new Historial();                         ///< Modul que controla l'historial de tirades
    private LlegirFitxers fitxerEntradaPartida = new LlegirFitxers();      ///< Modul que llegeix els fitxers d'entrada
    private int TornsInaccio = 0;                                          ///< Contador de torns actual d'Inacció
    private int EscacsSeguits = 0;                                         ///< Contador d'escacs seguits actual

    /** @brief  Genera una partida carregada
     * @param fitxerPartida nom del fitxer d'entrada
     * @pre fitxerPartida existeix i esta en un format correcte
     * @post totes les variables per a continuar una partida estan settejades.
     */
    public Partida(String fitxerPartida) throws IOException {
        //Hauriem de posar condicions per a saber quina partida començarem
        fitxerEntradaPartida.llegirPartidaComencada(fitxerPartida);
        conjuntPeces = fitxerEntradaPartida.getConjuntPeces();
        taulell = fitxerEntradaPartida.getTaulell();
        properTorn = fitxerEntradaPartida.getProperTorn();
        limitEscacsSeguits = fitxerEntradaPartida.getLimitEscacsSeguits();
        limitTornsInaccio = fitxerEntradaPartida.getLimitTornsInaccio();
        carregarPartidaAnterior(fitxerPartida);
    }

    /** @brief  Genera una partida carregada
     * @param fitxerRegles nom del fitxer d'entrada
     * @param jugadors nombre de jugadors humans
     * @pre fitxerRegles existeix i esta en un format correcte
     * @post totes les variables per a començar una partida estan settejades.
     */
    public Partida(String fitxerRegles, int jugadors) throws IOException {
        fitxerEntradaPartida.llegirRegles(fitxerRegles);
        conjuntPeces = fitxerEntradaPartida.getConjuntPeces();
        taulell = fitxerEntradaPartida.getTaulell();
        limitEscacsSeguits = fitxerEntradaPartida.getLimitEscacsSeguits();
        limitTornsInaccio = fitxerEntradaPartida.getLimitTornsInaccio();
        properTorn = "BLANQUES";
        nJugadors = jugadors;
        iniciarPartidaNova(fitxerRegles);
        guardarFitxerRegles(fitxerRegles);
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
        if (limitEscacsSeguits <= EscacsSeguits) {
            return "EscacsSeguits";
        }
        if (limitTornsInaccio <= TornsInaccio) {
            return "TornsInaniccio";
        }
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
            TornsInaccio++;
            EscacsSeguits = 0;

            if (jugadorActual.ferEnrroc(taulell, origen, desti)) {
                return "s'ha realitzat el enrroc correctament";
            } else {
                return "no s'ha realitzat l'enrroc";
            }
        } else if (defaultTokenizer.countTokens() == 2) { //Tirada Normal
            Posicio origen = new Posicio((defaultTokenizer.nextToken())); //origen
            Posicio desti = new Posicio((defaultTokenizer.nextToken())); //destí

            int resultatTirada = jugadorActual.ferTirada(taulell, origen, desti);
            if (resultatTirada > 0) {
                if (jugadorActual.ShaProvocatJaque(taulell)) {
                    desferTirada();
                    return "no s'ha realitzat la tirada";
                } else if (jugadorActual.observarPromocio(desti, taulell)) {
                    TornsInaccio++;
                    EscacsSeguits = 0;
                    return "promocio";
                } else if (jugadorActual.ShaProvocatJaque(taulell)) {
                    TornsInaccio++;
                    EscacsSeguits++;
                    modificarResultatUltimaTirada("ESCAC");
                    return "hi ha jaque";
                }
            }
            //0 no valid
            //1 tirada valida
            //>1 tirada valida + morts;
            if (resultatTirada == 1) {
                TornsInaccio++;
                EscacsSeguits = 0;
                return "tirada vàlida";
            } else if (resultatTirada > 1) {
                TornsInaccio = 0;
                EscacsSeguits = 0;
                return "return tirada vàlida i s'ha matat";
            } else {
                return "no s'ha realitzat la tirada";
            }
        }
        return "Alguna cosa ha sortit malament";
    }

    /** @brief Acció de perdre la partida, cada jugador ho pot decidir en el seu torn
     * @pre --
     * @post Es tanca la partida amb el guanyador siguent l'equip contrari
     */
    public void rendirse () {
        guardarProperTorn(properTorn);
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
        guardarProperTorn(properTorn);
        Historial.guardarPartida("TAULES");
    }

    /** @brief  Acció d'ajornar la partida
     * @pre --
     * @post Es tanca la partida, sense resultat, per a poder segui-la
     */
    public void ajornar () {
        guardarProperTorn(properTorn);
        Historial.guardarPartida("");
    }

    /** @brief  Acció de mostrar el taulell
     * @pre --
     * @post Es mostra el taulell en format text
     */
    public void mostrarTaulell () {
        System.out.println(taulell.mostra());
    }

    /** @brief Peça de la posició p
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

        if(nomPeça.equalsIgnoreCase("REI") || conjuntPeces.get(nomPeça) == null){
            return "no valid";
        }
        else{
            Peca p = new Peca(nomPeça, jugadorActual.get_equip(),conjuntPeces);
            Peca v;
            StringTokenizer defaultTokenizer = new StringTokenizer(tirada);
            Posicio origen = new Posicio((defaultTokenizer.nextToken())); //origen
            Posicio desti = new Posicio((defaultTokenizer.nextToken())); //destí
            jugadorActual.ferPromocio(desti,taulell,p);
            v = taulell.getPeca(origen);
            modificarResultatUltimaTirada("PROMOCIÓ: " + v.getNom() + " - " + p.getNom() );
            if(jugadorActual.ShaProvocatJaque(taulell)){
                return "feta jaque";
            }
            else return "feta";
        }
    }

    /** @brief  Desfem l'última tirada
     * @pre Hi ha alguna tirada per a desfer
     * @post Desfet l'ultima tirada al taulell i al fitxer de partida
     */
    public TiradaSimple desferTirada () {
        TiradaSimple ultimaTirada = getUltimaTirada();
        String res = getUltimResultat();
        TiradaSimple tiradaDesfeta = taulell.desferTirada(ultimaTirada, res, conjuntPeces);
        eliminarUltimaTirada();
        return tiradaDesfeta;
    }

    /** @brief  Refem l'última tirada
     * @pre Hi ha alguna tirada per a refer
     * @post Desfet l'ultima tirada al taulell i al fitxer de partida
     */
   public TiradaSimple referTirada () {
       StringBuilder resultat = new StringBuilder();
       TiradaSimple ultimaTirada = taulell.referTirada(resultat);
       String r = resultat.toString();
       guardarTirada(ultimaTirada, r);
       return ultimaTirada;
   }

    public String [] getLlistaPeces() {
        String [] peces = new String[conjuntPeces.size()];
        for (int i = 0; i < conjuntPeces.size() ; i++) {
            peces [i] = conjuntPeces.get(i).get_nom();
        }
        return peces;
    }
}
