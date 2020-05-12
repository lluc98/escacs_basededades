package Partida;

import java.util.TreeMap;

public class Peca {
    private TipusPeca _tipus;
    private boolean _primerMoviment;
    private boolean _equip;

    public Peca(String s, boolean equip, TreeMap<String,TipusPeca> m){
        _tipus = m.get(s);
        if(_tipus != null){
            _primerMoviment = false;
            _equip = equip;
        }
        else{
            throw new RuntimeException("Tipus de peça no trobada");
        }
    }

    public boolean movimentValid(TiradaSimple t){
        boolean valid;
        if(!_equip){
            t.canviarSigneDesplacament();
        }

        if(_tipus.tipusPecaValida(t,_primerMoviment)){
            valid = true;
        }
        else{
            valid = false;
        }

        if(!_equip){
            t.canviarSigneDesplacament();
        }
        return valid;

    }

    public boolean get_equip(){
        return _equip;
    }

    public void primerMovFet(){
        if(_primerMoviment == false){
            _primerMoviment = true;
        }
    }
}
