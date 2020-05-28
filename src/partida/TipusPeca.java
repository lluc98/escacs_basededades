package partida;

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


    public String get_simbol() {
        return _simbol;
    }

    public String get_imgBlanca() {
        return _imgBlanca;
    }

    public String get_imgNegra() {
        return _imgNegra;
    }

    public int get_valor() {
        return _valor;
    }

    public ArrayList<Moviment> get_llistaMoviments() {
        return _llistaMoviments;
    }

    public ArrayList<Moviment> get_llistaMovimentsInicials() {
        return _llistaMovimentsInicials;
    }

    public ArrayList<CaracteristiquesEnroc> get_llistaEnrocs() {
        return _llistaEnrocs;
    }

    public TipusPeca(String nom, String simbol, String blanca, String negra, int valor, ArrayList<Moviment> listMov, ArrayList<Moviment> listIini, boolean promocio, boolean invulnerabilitat ){
        _nom = nom;
        _simbol = simbol;
        _imgBlanca = blanca;
        _imgNegra = negra;
        _valor = valor;
        _promocio = promocio;
        _invulnerabilitat = invulnerabilitat;
        _llistaMoviments = listMov;
        _llistaMovimentsInicials = listIini;
        _llistaEnrocs = new ArrayList<>();



    }

    public boolean tipusPecaValida(TiradaSimple t, boolean primerMov){
        boolean trobat = false;
        Iterator<Moviment> it1;
        Iterator<Moviment> it2;
        if(primerMov == false){
            it1 = _llistaMovimentsInicials.iterator(); //Recorrec la llista de moviments inicials
            while(it1.hasNext()){
                if(it1.next().movimentvalid(t)){
                    return true;
                }
            }
        }


        it2 = _llistaMoviments.iterator(); // Recorrec la llista de moviments
        while(it2.hasNext() && !trobat){
            if(it2.next().movimentvalid(t)){
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
        return trobat;

    }


}


