package Partida;


import java.util.StringTokenizer;
import java.util.TreeMap;

import static Partida.LlegirRegles.llegirPartidaComencada;
import static Partida.LlegirRegles.llegirRegles;


public class Partida {
    TreeMap<String, TipusPeca> conjuntPeces = new TreeMap<>();
    Taulell taulell = null;
    String properTorn;
    int limitTornsInaccio = 0;
    int limitEscacsSeguits = 0;
    Jugador jugadorBlanques = new Jugador (true);
    Jugador jugadorNegres = new Jugador (false);


    public Partida (String fitxerPartida) {
        //Hauriem de posar condicions per a saber quina partida començarem
        llegirPartidaComencada(fitxerPartida, conjuntPeces, taulell, properTorn, limitEscacsSeguits, limitTornsInaccio);
    }

    public Partida (String fitxerRegles, int jugadors) {
        llegirRegles(fitxerRegles, conjuntPeces, taulell, limitEscacsSeguits, limitTornsInaccio);

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
        boolean correcte = false;
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

        StringTokenizer defaultTokenizer = new StringTokenizer(tirada);
        if (defaultTokenizer.countTokens() == 3) { //Enrroc
            Posicio origen = new Posicio((defaultTokenizer.nextToken())); // origen
            defaultTokenizer.nextToken(); // -
            Posicio desti = new Posicio((defaultTokenizer.nextToken())); //destí

            Enrroc enrroc = new Enrroc(origen,desti,colorTorn);

            if (taulell.validarEnrroc(enrroc)) {
                if (enrroc.realitzarEnroc(taulell)) {
                    return "tot correcte";
                } else {
                    return "no s'ha pogut realitza l'enrroc";
                }
            }
            else {
                return "enrroc no vàlid";
            }


        } else if (defaultTokenizer.countTokens() == 2) { //Tirada Normal
            Posicio origen = new Posicio((defaultTokenizer.nextToken())); //origen
            Posicio desti = new Posicio((defaultTokenizer.nextToken())); //destí
            TiradaSimple tiradaActual = new TiradaSimple(origen, desti, colorTorn);

            if (taulell.tiradaValida(tiradaActual)) {
                taulell.realitzarTirada(tiradaActual);
            }
            else {
                return "tirada no valida";
            }
        } else { return "entrada no valida"; }
        return "Alguna cosa ha sortit malament";
    }

    public Taulell getTaulell() {return taulell; }

    public String getProperTorn() { return properTorn; }

    public int getLimitTornsInaccio() { return limitTornsInaccio; }

    public int getLimitEscacsSeguits () { return limitEscacsSeguits; }
}
