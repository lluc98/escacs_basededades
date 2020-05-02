package Partida;

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

    public boolean tiradaValida(Tirada t){
        Posicio origen = t.get_origen();
        Posicio desti = t.get_desti();
        Peca p = _tauler.get(origen);
        if(p != null){
            if((desti.get_fila() > 0) && (desti.get_fila() <= _fila) && (desti.get_columna() > 0) && (desti.get_columna() <= _columna)){
                if(p.movimentValid(t)){

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
}
