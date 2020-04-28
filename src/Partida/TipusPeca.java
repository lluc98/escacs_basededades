package Partida;

import java.util.ArrayList;

public class TipusPeca {
    private String _nom;
    private char _simbol;
    private String _imgBlanca;
    private String _imgNegra;
    private int _valor;
    private boolean _promocio;
    private boolean _invulnerabilitat;
    private ArrayList<Moviment> _llistaMoviments;
    private int _n;

    public TipusPeca(String nom, char simbol, String blanca, String negra, int valor, boolean promocio, boolean invulnerabilitat ){
        _nom = nom;
        _simbol = simbol;
        _imgBlanca = blanca;
        _imgNegra = negra;
        _valor = valor;
        _promocio = promocio;
        _invulnerabilitat = invulnerabilitat;
        _llistaMoviments = new ArrayList<>();
        _n=0;

    }

    public void afegirMoviment(Moviment m){
        _llistaMoviments.set(_n,m);
        _n++;
    }
}


