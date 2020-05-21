package Partida;

public class Moviment {
    private String _horitzontal;
    private String _vertical;
    private int _movMatar;
    private int _movSaltar;

    public String get_horitzontal() {
        return _horitzontal;
    }

    public String get_vertical() {
        return _vertical;
    }

    public int get_movMatar() {
        return _movMatar;
    }

    public int get_movSaltar() {
        return _movSaltar;
    }

    public Moviment(String horitzontal, String vertical, int matar, int saltar){
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

    public boolean movimentvalid(TiradaSimple t){
        if(valid(t.get_desplaçamentX(), _horitzontal) && valid(t.get_desplacamentY(), _vertical)){
            t.guardarMatar(_movMatar);
            t.guardarVolar(_movSaltar);
            return true;
        }
        else{
            return false;
        }
    }

    private boolean valid(int desp, String s){
        int mov;
        try {
            mov = Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            mov = 100;
        }
        if(mov == 100){
            if(desp>=0){
                if(s.equals("a") || s.equals("b") || s.equals("m") || s.equals("n") ){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if(s.equals("-a") || s.equals("-b")){
                return true;
            }
            else{
                return false;
            }
        }
        else if(desp == mov){
            return true;
        }
        else{
            return false;
        }
    }
}

