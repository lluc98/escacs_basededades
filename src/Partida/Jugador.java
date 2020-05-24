package Partida;
public class Jugador {

    boolean _equip;
    Jugador(boolean equip){
        _equip = equip;
    }

    public int ferTirada(Taulell taulell, Posicio origen, Posicio desti, Historial h){
        int res = 0;
        TiradaSimple t = new TiradaSimple(origen, desti, _equip);
        if(taulell.tiradaValida(t)){
            res= res + taulell.realitzarTirada(t);
            //h.guardar
        }

        return res;
    }

    public boolean observarPromocio(Posicio desti, Taulell taulell){
        return taulell.hiHaPromocio(desti, _equip);
    }

    public void ferPromocio(Posicio posicio, Taulell taulell, Peca p){
        taulell.realitzarPromocio(posicio, p);
    }

    public boolean ObservarJaque(Taulell taulell){
        return taulell.hihaJaque(_equip);
    }

    public boolean ShaProvocatJaque(Taulell taulell){ return taulell.hihaJaque(!_equip); }


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

