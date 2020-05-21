package Partida;

import java.util.ArrayList;
import java.util.Iterator;

public class TipusPeca {
    private String _nom;
    private String _simbol;
    private String _imgBlanca;
    private String _imgNegra;
    private int _valor;
    private boolean _promocio;
    private boolean _invulnerabilitat;
    private ArrayList<Moviment> _llistaMoviments;
    private ArrayList<Moviment> _llistaMovimentsInicials;
    private ArrayList<CaracteristiquesEnroc> _llistaEnrocs;


    public TipusPeca(String nom, String simbol, String blanca, String negra, int valor, ArrayList<Moviment> listMov,ArrayList<Moviment> listIini, boolean promocio, boolean invulnerabilitat ){
        _nom = nom;
        _simbol = simbol;
        _imgBlanca = blanca;
        _imgNegra = negra;
        _valor = valor;
        _promocio = promocio;
        _invulnerabilitat = invulnerabilitat;
        _llistaMoviments = listMov;
        _llistaMovimentsInicials = listIini;
        _llistaEnrocs = null;


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

    public String get_nom(){
        return _nom;
    }
    public boolean get_invulnerabilitat() {return _invulnerabilitat;}

    public void afegirEnrroc(CaracteristiquesEnroc c){
        _llistaEnrocs.add(c);
    }

    public boolean validarEnrroc(Enrroc e, Peca b){
        boolean trobat = false;
        Iterator<CaracteristiquesEnroc> it;
        if(_llistaEnrocs != null){
            it = _llistaEnrocs.iterator();
            while(it.hasNext() && !trobat){
                if(it.next().enrrocValid(e, b)){
                    trobat = true;
                }
            }
        }
        return false;

    }

}


