package Partida;

public class Moviment {
    private char _horitzontal;
    private char _vertical;
    private int _movMatar;
    private int _movSaltar;

    public Moviment(char horitzontal, char vertical, int matar, int saltar){
        _horitzontal = horitzontal;
        _vertical = vertical;
        _movMatar = matar;
        _movSaltar = saltar;
    }

    public int potSaltar(){
        return _movSaltar;
    }

    public int potMatar(){
        return _movMatar;
    }
}

