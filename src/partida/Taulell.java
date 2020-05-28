package partida;

import java.util.*;


public class Taulell {

    private int _fila;
    private int _columna;
    private SortedMap<Posicio, Peca> _tauler;
    private TreeMap<Integer,TreeMap<Posicio, Peca>> _eliminats;
    private int n_peces;
    private Integer nTorns;
    private ArrayList<TiradaSimple> _tiradesRefer;

    public Taulell(int c, int f){
        _fila = f;
        _columna = c;
        n_peces = 2;
        nTorns = 1;
        if(_columna < 4 || _columna > 16 || _fila <4 || _fila > 16)
            throw new RuntimeException("Error en el parametres");
        _tauler = new TreeMap<>();
        _eliminats = new TreeMap<>();
        _tiradesRefer = new ArrayList<>();
    }


    public int getFiles() {
        return _fila;
    }



    public int getColumnes() {
        return _columna;
    }

    public void afegirPeca(Peca b, Peca n){
        if(n_peces < (_fila*_columna)-_columna) {
            int x, y, y2;
            if ((n_peces / 2) <= _columna) {
                y = 1;
                x = n_peces / 2;
                y2 = _fila;
            } else {
                y = ((n_peces /2)/ (_columna + 1)) + 1;
                x = (n_peces / 2) % _columna;
                if(x == 0){
                    x = _columna;
                }
                y2 = _fila - (y - 1);
            }
            Posicio p = new Posicio(y, x);
            Posicio p2 = new Posicio(y2, x);
            _tauler.put(p, b);
            _tauler.put(p2, n);
            n_peces = n_peces + 2;
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

    public TiradaSimple hihaJaque(boolean equip){
        Iterator<Map.Entry<Posicio, Peca>> it = _tauler.entrySet().iterator();
        Posicio posRei = buscaRei(equip);
        Peca pRei = _tauler.get(posRei);
        Posicio pos ;
        Peca p;
        boolean trobat = false;
        while(it.hasNext()){
            Map.Entry<Posicio, Peca> entry = it.next();
            pos = entry.getKey();
            p = entry.getValue();
            if(p.get_equip()!=pRei.get_equip()) {
                TiradaSimple tirada = new TiradaSimple(pos, posRei, equip);
                if (p.movimentValid(tirada)) {
                    if (validMatar(tirada, pRei) && validVolar(tirada)) {
                        return tirada;
                    }
                }
            }
        }
        return null;
    }

    public boolean hiHaJaqueMate(TiradaSimple t){
        return false;
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
        Posicio posOrigen = t.get_origen();
        int f = posOrigen.get_fila();
        int c = posOrigen.get_columna();
        while(x!= t.get_desplaçamentX() || y!= t.get_desplacamentY()){
            Posicio pos = new Posicio(f,c);
            Peca pec = _tauler.get(pos);
            if(pec!=null){
                if(!pos.equals(t.get_origen()) && !pos.equals(t.get_desti())){
                    return true;
                }
            }
            if(x!=t.get_desplaçamentX()){
                if(t.get_desplaçamentX()>0){
                    x++;
                    f++;
                }
                else{
                    x--;
                    f--;

                }

            }
            if(y!=t.get_desplacamentY()){
                if(t.get_desplacamentY() > 0){
                    y++;
                    c++;
                }
                else{
                    y--;
                    c--;
                }

            }
        }
        return false;
    }

    public int realitzarTirada(TiradaSimple t){
        TreeMap<Posicio,Peca> eli = new TreeMap<>();
        int res = 1;
        Peca p = _tauler.get(t.get_origen());
        Peca d = _tauler.get(t.get_desti());
        if(d!=null){
            res ++;
            eli.put(t.get_desti(),d);
        }
        if(t.get_volar() == 2){
            res = res + eliminarPecesEntremig(t, eli);
        }
        _tauler.put(t.get_desti(),p);
        _tauler.remove(t.get_origen());
        p.primerMovFet();

        _eliminats.put(nTorns,eli);
        nTorns ++;

        return  res;
    }

    private int eliminarPecesEntremig(TiradaSimple t, TreeMap<Posicio, Peca> eli){
        int x, y, res, f, c;
        res = 0;
        x = 0;
        y = 0;
        Posicio posOrigen = t.get_origen();
        f = posOrigen.get_fila();
        c = posOrigen.get_columna();
        while(x!= t.get_desplaçamentX() || y!= t.get_desplacamentY()) {
            Posicio pos = new Posicio(f, c);
            Peca pec = _tauler.get(pos);
            if (pec != null) {
                if(!pos.equals(t.get_origen()) && !pos.equals(t.get_desti())) { //si troba un peça que no sigui ni desti ni origen
                    eli.put(pos, pec);
                    _tauler.remove(pos);
                    res++;
                }
            }
            if (x != t.get_desplaçamentX()) {
                if (t.get_desplaçamentX() > 0) {
                    x++;
                    f++;
                } else {
                    x--;
                    f--;
                }
            }
            if (y != t.get_desplacamentY()) {
                if (t.get_desplacamentY() > 0) {
                    y++;
                    c++;
                } else {
                    y--;
                    c--;
                }
            }
        }
        return res;
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
            return true;
        }
        else{
            return false;
        }
    }

    public void realitzarPromocio(Posicio pos, Peca pec){
        _tauler.put(pos,pec);
    }

    public void desferTirada(TiradaSimple t){
        nTorns--;
        TreeMap<Posicio,Peca> eli = _eliminats.get(nTorns);
        Iterator<Map.Entry<Posicio, Peca>> it = eli.entrySet().iterator();
        while(it.hasNext()) { //fico les peces mortes (si hi havia) de la tirada anterior

            Map.Entry<Posicio, Peca> entry = it.next();
            Posicio pos = entry.getKey();
            Peca peca = entry.getValue();
            assignarPecaTauler(peca,pos);
        }
        Peca p = _tauler.get(t.get_desti());
        _tauler.put(t.get_origen(),p);
        _tauler.remove(t.get_desti());
        _tiradesRefer.add(t);
    }

    public void reiniciaTiradesRefer(){
        _tiradesRefer = new ArrayList<>();
    }

    public TiradaSimple referTirada(StringBuilder resultat){
        TiradaSimple t = _tiradesRefer.get(0);
        realitzarTirada(t);
        return t;
    }



    public String mostra() {
        String s = "";
        Character x = 'a';
        s = s + " ";
        for(int j=0; j< _columna; j++){
            s = s + " " + x;
            x++;
        }
        s = s + "\n";
        for(int i = _fila; i>0; i--){
            s = s + " ";
            for(int j=0; j< _columna; j++){
                s = s + " " + "-";
            }
            s = s + "\n";
            s = s + i;
            s = s + "|";
            for(int j=1; j<= _columna; j++){

                Posicio p = new Posicio(i,j);
                if(contePeçaCasella(p)){
                    s = s + _tauler.get(p).toString();
                }
                else{
                    s = s + " ";
                }
                s = s + "|";
            }
            s = s + "\n";

        }
        s = s + " ";
        for(int j=0; j< _columna; j++){
            s = s + " " + "-";
        }

        s = s + "\n";
        return s;
    }

}
