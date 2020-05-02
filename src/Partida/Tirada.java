package Partida;

public class Tirada {
    private Posicio _origen;
    private Posicio _desti;
    private int _desplacamentX;
    private int _desplacamentY;

    public Tirada(Posicio origen, Posicio desti){
        _origen = origen;
        _desti = desti;
        _desplacamentX = _desti.get_fila() - _origen.get_fila();
        _desplacamentY = _desti.get_columna() - _origen.get_columna();
    }

    public Posicio get_origen(){
        return _origen;
    }

    public Posicio get_desti(){
        return _desti;
    }

    public int get_desplaçamentX(){
        return _desplacamentX;
    }

    public int get_desplacamentY(){
        return get_desplacamentY();
    }
}



