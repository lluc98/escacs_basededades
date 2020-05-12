package Partida;

import java.util.ArrayList;
import java.util.Iterator;

public class TipusPeca {
    private String _nom;
    private char _simbol;
    private String _imgBlanca;
    private String _imgNegra;
    private int _valor;
    private boolean _promocio;
    private boolean _invulnerabilitat;
    private ArrayList<Moviment> _llistaMoviments;
    private ArrayList<Moviment> _llistaMovimentsInicials;


    public TipusPeca(String nom, char simbol, String blanca, String negra, int valor, ArrayList<Moviment> listMov,ArrayList<Moviment> listIini, boolean promocio, boolean invulnerabilitat ){
        _nom = nom;
        _simbol = simbol;
        _imgBlanca = blanca;
        _imgNegra = negra;
        _valor = valor;
        _promocio = promocio;
        _invulnerabilitat = invulnerabilitat;
        _llistaMoviments = listMov;
        _llistaMovimentsInicials = listIini;


    }

    public boolean tipusPecaValida(TiradaSimple t, boolean primerMov){
        boolean trobat = false;
        Iterator<Moviment> it;
        if(primerMov == false){
            it = _llistaMovimentsInicials.iterator(); //Recorrec la llista de moviments inicials
        }
        else{
            it = _llistaMoviments.iterator(); // Recorrec la llista de moviments
        }
        while(it.hasNext() && !trobat){
            if(it.next().movimentvalid(t)){
                trobat = true;
            }
        }
        return trobat;
    }

}


