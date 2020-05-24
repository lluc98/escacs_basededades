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

    public Peca(String s, boolean equip, TreeMap<String,TipusPeca> m, boolean moguda){
        _tipus = m.get(s);
        if(_tipus != null){
            _primerMoviment = moguda;
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

    public boolean esRei(boolean equip){
        return (_tipus.get_nom().equals("REI") && _equip != equip);
    }

    public boolean enrrocValid(Enrroc e, Peca b){
        if(_tipus.validarEnrroc(e, b)){
            if(e.getQuiets()==true && _primerMoviment==true){
                return false;
            }
            else{
                return true;
            }
        }
        else{
            return false;
        }
    }

    public  boolean esPecaNom(String nom){
        return(_tipus.get_nom().equals(nom));
    }

    public  boolean esInvulnerable(){ return _tipus.get_invulnerabilitat();}

    public TipusPeca get_tipus() { return _tipus; }

    public String toString(){
        String s = "";
        String p = _tipus.get_simbol();
        char c = p.charAt(0);
        if(_equip == false){
            c = Character.toLowerCase(c);
        }

        s = s + c;

        return s;

    }
}
