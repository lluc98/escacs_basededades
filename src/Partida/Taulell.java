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
        _tauler = new TreeMap<Posicio, Peca>();

    }

    public void afegirPeca(Peca c){
        int x, y, y2;
        if(n_peces/2<_columna){
            y = 1;
            x = n_peces/2;
            y2 = _columna;
        }
        else{
            y = 2;
            x = (n_peces/2)%_columna;
            y2 = _columna-1;
        }


    }
}
