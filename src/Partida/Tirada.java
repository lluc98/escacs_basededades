package Partida;

public class Tirada {
    private Posicio _origen;
    private Posicio _desti;

    public Tirada(Posicio origen, Posicio desti){
        _origen = origen;
        _desti = desti;
    }

    public Posicio origen(){
        return _origen;
    }

    public Posicio desti(){
        return _desti;
    }
}

