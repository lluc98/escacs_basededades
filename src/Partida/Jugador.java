package Partida;
public class Jugador {
    private String _nom;
    boolean _equip;

    Jugador(String nom, boolean equip){
        _nom = nom;
        _equip = equip;
    }

    public boolean ferTirada(Taulell taulell, Posicio origen, Posicio desti){
        TiradaSimple t = new TiradaSimple(origen, desti, _equip);
        if(taulell.tiradaValida(t)){
            taulell.realitzarTirada(t);
            return true;
        }
        else return false;
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

