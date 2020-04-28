package Partida;

public class Posicio {
    private int _fila;
    private int _columna;
    private String _posicio;

    public Posicio(String p){
        _posicio = p;
        String numero = p.substring(p.length()-1);
        String lletra = p.substring(0,1);
        _fila = lletra.charAt(0) - 'a' + 1;
        _columna = Integer.parseInt(numero);
    }
    public int fila(){
        return _fila;
    }

    public int columna(){
        return _fila;
    }

    public String posicio(){
        return _posicio;
    }
}
