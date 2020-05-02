package Partida;

import java.util.TreeMap;

public class Peca {
    private TipusPeca _tipus;
    private boolean _primerMoviment;
    private boolean _equip;

    public Peca(String s, boolean equip, TreeMap<String,TipusPeca> m){

        _primerMoviment = false;
        _tipus = m.get(s);
        if(_tipus != null){
            _primerMoviment = false;
            _equip = equip;
        }
        else{
            throw new RuntimeException("Tipus de peça no trobada");
        }
    }
}
