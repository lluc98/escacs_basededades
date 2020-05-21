package Partida;

import com.sun.corba.se.spi.activation.RepositoryOperations;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Iterator;

public class Taulell {

    private int _fila;
    private int _columna;
    private SortedMap<Posicio, Peca> _tauler;
    private int n_peces;

    public Taulell(int c, int f){
        _fila = f;
        _columna = c;
        n_peces = 0;
        if(_columna < 4 || _columna > 16 || _fila <4 || _fila > 16)
            throw new RuntimeException("Error en el parametres");
        _tauler = new TreeMap<>();

    }

    public void afegirPeca(Peca b, Peca n){
        if(n_peces < (_fila*_columna)-_columna) {
            int x, y, y2;
            if ((n_peces / 2) < _columna) {
                y = 1;
                x = n_peces / 2;
                y2 = _columna;
            } else {
                y = (n_peces / (_columna + 1)) + 1;
                x = (n_peces / 2) % _columna;
                y2 = _columna - (y - 1);
            }
            Posicio p = new Posicio(x, y);
            Posicio p2 = new Posicio(x, y2);
            _tauler.put(p, b);
            _tauler.put(p2, n);
        }
        else
            throw new RuntimeException("NO CABEN MÉS PECES AL TAULELL");


    }

    public void assignarPecaTauler(Peca peca, Posicio pos){
        _tauler.put(pos,peca);
    }

    public boolean tiradaValida(TiradaSimple t){
        Posicio origen = t.get_origen();
        Posicio desti = t.get_desti();
        Peca p = _tauler.get(origen);
        if(p != null && p.get_equip()==t.get_equip()){ //Miro que a la posicio origen hi ha un peça i es del equip correcte
            if((desti.get_fila() > 0) && (desti.get_fila() <= _fila) && (desti.get_columna() > 0) && (desti.get_columna() <= _columna)){ //miro si la posicio desti esta dins del taulell
                Peca c = _tauler.get(desti);
                if((c != null && c.get_equip()==t.get_equip()) || c!=null && c.esInvulnerable()){ //miro si hi ha peça a la posicio desti i de quin equip es
                    return false;
                }
                if(p.movimentValid(t)){
                    if(validMatar(t,c) && validVolar(t)){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }

        }
        else{
            return false;
        }

    }

    public boolean hihaJaque(boolean equip){
        Iterator<Map.Entry<Posicio, Peca>> it = _tauler.entrySet().iterator();
        Posicio posRei = buscaRei(equip);
        Peca pRei = _tauler.get(posRei);
        Posicio pos = null;
        Peca p = null;
        boolean trobat = false;
        while(it.hasNext() && !trobat){
            Map.Entry<Posicio, Peca> entry = it.next();
            pos = entry.getKey();
            p = entry.getValue();
            TiradaSimple tirada = new TiradaSimple(pos, posRei, equip);
            if(p.movimentValid(tirada)){
                if(validMatar(tirada,pRei) && validVolar(tirada)){
                    trobat = true;
                }
            }
        }
        return trobat;
    }

    private boolean validMatar(TiradaSimple t, Peca p){
        if(t.get_matar() == 0 && p==null){
            return true;
        }
        else if(t.get_matar() == 1){
            return true;
        }
        else if(t.get_matar() == 2 && p!=null){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean validVolar(TiradaSimple t){
        if(t.get_volar() == 0 && !hiHaPecesEntremig(t)){
            return true;
        }
        else if(t.get_volar() == 1 || t.get_volar()==2){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean hiHaPecesEntremig(TiradaSimple t){
        int x, y;
        x = 0;
        y = 0;
        while(x!= t.get_desplaçamentX() && y!= t.get_desplacamentY()){
            Posicio pos = new Posicio(x,y);
            Peca pec = _tauler.get(pos);
            if(pec!=null){
                return true;
            }
            if(x!=t.get_desplaçamentX()){
                if(t.get_desplaçamentX()>0){
                    x++;
                }
                else{
                    x--;
                }

            }
            if(y!=t.get_desplacamentY()){
                if(t.get_desplacamentY() > 0){
                    y++;
                }
                else{
                    y--;
                }
            }
        }
        return false;
    }

    public void realitzarTirada(TiradaSimple t){
        Peca p = _tauler.get(t.get_origen());
        if(t.get_volar() == 2){
            eliminarPecesEntremig(t);
        }
        _tauler.put(t.get_desti(),p);
        _tauler.remove(t.get_origen());
        p.primerMovFet();
    }

    private void eliminarPecesEntremig(TiradaSimple t){
        int x, y;
        x = 0;
        y = 0;
        while(x!= t.get_desplaçamentX() && y!= t.get_desplacamentY()) {
            Posicio pos = new Posicio(x, y);
            Peca pec = _tauler.get(pos);
            if (pec != null) {
                _tauler.remove(pos);
            }
            if (x != t.get_desplaçamentX()) {
                if (t.get_desplaçamentX() > 0) {
                    x++;
                } else {
                    x--;
                }

            }
            if (y != t.get_desplacamentY()) {
                if (t.get_desplacamentY() > 0) {
                    y++;
                } else {
                    y--;
                }
            }
        }
    }

    public Posicio buscaRei(boolean equip){
        Iterator<Map.Entry<Posicio, Peca>> it = _tauler.entrySet().iterator();
        boolean trobat = false;
        Posicio pos = null;
        Peca p = null;
        while(it.hasNext() && !trobat){
            Map.Entry<Posicio, Peca> entry = it.next();
            pos = entry.getKey();
            p = entry.getValue();
            if(p.esRei(equip)){
                trobat = true;
            }
        }
        return pos;
    }

    public boolean validarEnrroc(Enrroc e){
        Posicio a = e.get_p1();
        Posicio b = e.get_p2();
        Peca p = _tauler.get(a);
        Peca p2 = _tauler.get(b);
        if(p != null && p.get_equip() == e.get_equip() && p2 != null && p2.get_equip() == e.get_equip() && a.get_fila() == b.get_fila()){
            if(p.enrrocValid(e, p2)){

                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public boolean contePeçaCasella(Posicio p){
        Peca peca = _tauler.get(p);
        return peca !=  null;
    }

    public Peca getPeca(Posicio p) {
        return _tauler.get(p);
    }

    public boolean hiHaPromocio(Posicio p, boolean _equip){
        if(p.get_fila() == _columna && _equip == true){ //si esta dalt de tot  i es una peça blanca
            return true;
        }
        else if(p.get_fila() == 1 && _equip == false){ //si esta baix de tot i es una peça negra
            return false;
        }
        else{
            return false;
        }
    }

    public void realitzarPromocio(Posicio pos, Peca pec){
        _tauler.put(pos,pec);
    }

}
