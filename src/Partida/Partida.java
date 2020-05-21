package Partida;


import java.util.TreeMap;

import static Partida.LlegirRegles.llegirPartidaComencada;
import static Partida.LlegirRegles.llegirRegles;


public class Partida {
    TreeMap<String, TipusPeca> conjuntPeces = new TreeMap<>();
    Taulell taulell = null;
    String properTorn;
    int limitTornsInaccio = 0;
    int limitEscacsSeguits = 0;


    public Partida (String fitxerPartida) {
        //Hauriem de posar condicions per a saber quina partida començarem
        llegirPartidaComencada(fitxerPartida, conjuntPeces, taulell, properTorn, limitEscacsSeguits, limitTornsInaccio);
    }

    public Partida (String fitxerRegles, int jugadors) {
        llegirRegles(fitxerRegles, conjuntPeces, taulell, limitEscacsSeguits, limitTornsInaccio);
    }

    public Taulell getTaulell() {return taulell; }

    public String getProperTorn() { return properTorn; }

    public int getLimitTornsInaccio() { return limitTornsInaccio; }

    public int getLimitEscacsSeguits () { return limitEscacsSeguits; }
}
