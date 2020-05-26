package Partida;


import java.util.StringTokenizer;
import java.util.TreeMap;



public class Partida {
    TreeMap<String, TipusPeca> conjuntPeces = new TreeMap<>();
    Taulell taulell = new Taulell(8,9);
    String properTorn = "BLANQUES";
    boolean colorTorn = true;
    int limitTornsInaccio = 0;
    int limitEscacsSeguits = 0;
    Jugador jugadorBlanques = new Jugador (true);
    Jugador jugadorNegres = new Jugador (false);
    int nJugadors;
    Historial historial = new Historial();
    LlegirFitxers fitxerEntradaPartida = new LlegirFitxers();

    public Partida (String fitxerPartida) {
        //Hauriem de posar condicions per a saber quina partida començarem
        fitxerEntradaPartida.llegirPartidaComencada(fitxerPartida);
        conjuntPeces = fitxerEntradaPartida.getConjuntPeces();
        taulell = fitxerEntradaPartida.getTaulell();
        properTorn = fitxerEntradaPartida.getProperTorn();
        limitEscacsSeguits = fitxerEntradaPartida.getLimitEscacsSeguits();
        limitTornsInaccio = fitxerEntradaPartida.getLimitTornsInaccio();
    }

    public Partida (String fitxerRegles, int jugadors) {
        fitxerEntradaPartida.llegirRegles(fitxerRegles);
        conjuntPeces = fitxerEntradaPartida.getConjuntPeces();
        taulell = fitxerEntradaPartida.getTaulell();
        limitEscacsSeguits = fitxerEntradaPartida.getLimitEscacsSeguits();
        limitTornsInaccio = fitxerEntradaPartida.getLimitTornsInaccio();
        properTorn = "BLANQUES";
        nJugadors = jugadors;
    }

    public String canviarTorn() {
        if (properTorn == "BLANQUES") {
            properTorn = "NEGRES";
        } else {
            properTorn = "BLANQUES";
        }
        return properTorn;
    }

    public String posCorrecte (String posicioIndefinida) {
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

    public void rendirse () {
        if (properTorn == "BLANQUES") {
            Historial.guardarPartida("NEGRES");
        }
        else {
            Historial.guardarPartida("BLANQUES");
        }
    }

    public void taules () {
        Historial.guardarPartida("TAULES");
    }

    public void ajornar () {
        Historial.guardarPartida("");
    }

    public void mostrarTaulell () {
        System.out.println(taulell.mostra());
    }

    public Peca getPeca ( Posicio p) {
        return taulell.getPeca(p);
    }

    public Taulell getTaulell() {return taulell; }

    public String getProperTorn() { return properTorn; }

    public int getLimitTornsInaccio() { return limitTornsInaccio; }

    public int getLimitEscacsSeguits () { return limitEscacsSeguits; }

//   public int getFiles () { return taulell.getFiles(); }

//   public int getColumnes () { return taulell.getColumnes(); }
}
