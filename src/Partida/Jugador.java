package Partida;
public class Jugador {


    boolean _equip;

    public boolean get_equip() {
        return _equip;
    }

    Jugador(boolean equip){
        _equip = equip;
    }

    public int ferTirada(Taulell taulell, Posicio origen, Posicio desti){ //0 si tirada no valida, 1 tirada valida, res>1 tirada valida i hi ha morts
        int res = 0;
        TiradaSimple t = new TiradaSimple(origen, desti, _equip);
        if(taulell.tiradaValida(t)){
            res= res + taulell.realitzarTirada(t);
        }

        return res;
    }

    public boolean observarPromocio(Posicio desti, Taulell taulell){
        return taulell.hiHaPromocio(desti, _equip);
    }

    public void ferPromocio(Posicio posicio, Taulell taulell, Peca p){
        taulell.realitzarPromocio(posicio, p);
    }

    public int ObservarJaque(Taulell taulell){ //res == 1 retorna jaque, res == 2 retorna jaque mate, res == 0 retorna no hi ha res
        int res = 0;
        TiradaSimple t = taulell.hihaJaque(_equip);
        if(t != null){
            if(taulell.hiHaJaqueMate(t)){
                res = 2;
            }
            else{
                res = 1;
            }
        }
        return res;
    }

    public boolean ShaProvocatJaque(Taulell taulell){
        TiradaSimple t =taulell.hihaJaque(!_equip);
        return t != null;
    }


    public boolean ferEnrroc(Taulell taulell, Posicio p1, Posicio p2){
        Enrroc enrroc = new Enrroc(p1,p2, _equip);
        if(taulell.validarEnrroc(enrroc)){
            if(enrroc.realitzarEnroc(taulell)){
                return true;
            }
            else return false;
        }
        else return false;
    }
}

