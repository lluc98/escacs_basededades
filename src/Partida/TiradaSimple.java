package Partida;

public class TiradaSimple {
    private Posicio _origen;
    private Posicio _desti;
    private int _desplacamentX;
    private int _desplacamentY;
    private int _matar;
    private int _volar;
    private boolean _equip;

    public TiradaSimple(Posicio origen, Posicio desti, boolean equip){
        _origen = origen;
        _desti = desti;
        _desplacamentX = _desti.get_fila() - _origen.get_fila();
        _desplacamentY = _desti.get_columna() - _origen.get_columna();
        _equip = equip;
    }

    public int get_desplacamentX() {
        return _desplacamentX;
    }

    public boolean is_equip() {
        return _equip;
    }

    public TiradaSimple(Posicio origen, Posicio desti, boolean equip, int matar, int volar){
        _origen = origen;
        _desti = desti;
        _desplacamentX = _desti.get_fila() - _origen.get_fila();
        _desplacamentY = _desti.get_columna() - _origen.get_columna();
        _equip = equip;
        _matar = matar;
        _volar = volar;
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

    public void canviarSigneDesplacament(){
        _desplacamentX = _desplacamentX * -1;
        _desplacamentY = _desplacamentY * -1;
    }

    public void guardarMatar(int n){
        _matar = n;
    }
    public void guardarVolar(int n){
        _volar = n;
    }

    public boolean get_equip(){
        return _equip;
    }

    public int get_matar(){
        return _matar;
    }
    public int get_volar(){
        return _volar;
    }

}
