package Partida;

public class Posicio implements Comparable<Posicio>{
    private int _fila;
    private int _columna;
    private String _posicio;

    public Posicio(String p){
        _posicio = p;
        String numero = p.substring(p.length()-1);
        String lletra = p.substring(0,1);
        _columna = lletra.charAt(0) - 'a' + 1;
        _fila = Integer.parseInt(numero);
    }

    public Posicio(int fila, int columna){
        _fila = fila;
        _columna = columna;
        columna = columna + 64;
        char letra = (char)(columna);
        letra = Character.toLowerCase(letra);
        _posicio = String.valueOf(letra);
        _posicio = _posicio+_fila;
    }


    public int get_fila(){
        return _fila;
    }

    public int get_columna(){
        return _columna;
    }

    public String get_posicio(){
        return _posicio;
    }

    @Override
    // Compara per edat i, en cas d'empat, per pes.
    public int compareTo(Posicio o) {
        int r = _fila - o._fila;
        if (r == 0)
            if (_columna > o._columna)
                r = 1;
            else if(_columna < o._columna)
                r = -1;
        return r;
    }


    @Override
    public boolean equals(Object o) {
        boolean r = false;
        if (o != null && (o instanceof Posicio)){
            Posicio m = (Posicio)o;
            r = this._fila == m._fila && this._columna == m._columna;
        }
        return r;
    }
}