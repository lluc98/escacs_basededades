package partida;

public class Enrroc {
    private Posicio p1;
    private Posicio p2;
    private boolean _equip;
    private boolean _quiets;
    private boolean _buitAlmig;
    private TiradaSimple _t1;
    private TiradaSimple _t2;

    public Enrroc(Posicio a, Posicio b, boolean equip){
        p1 = a;
        p2 = b;
        _equip = equip;

    }

    public Posicio get_p1(){
        return p1;
    }

    public Posicio get_p2(){
        return p1;
    }

    public boolean get_equip(){
        return _equip;
    }

    public  void assignaCaract(boolean quiets, boolean buit){
        _quiets = quiets;
        _buitAlmig = buit;
    }

    public boolean getQuiets(){
        return _quiets;
    }

    public boolean getBuit(){
        return _buitAlmig;
    }

    public boolean realitzarEnroc(Taulell t){
        int posCentral;
        int posCentral2;
        if(p1.get_columna() < p2.get_columna()){ //si la peça a enrrocar queda a la esquerra
            posCentral = p1.get_columna()+p2.get_columna();
            posCentral = (int) Math.ceil((double)posCentral/2d);
            posCentral2 = posCentral-1;
        }
        else{ //si la peça a enrrocar queda a la dreta
            posCentral = p1.get_columna()/p2.get_columna();
            posCentral2 = posCentral+1;
        }

        TiradaSimple aux = new TiradaSimple(p1,p2,_equip);
        if(t.hiHaPecesEntremig(aux) && _buitAlmig){ //si hi ha peces entremig i ha destar buit entre mig retorna fals
            return false;
        }
        else{
            if(t.contePeçaCasella(p1) || t.contePeçaCasella(p2)){
                return false;
            }
            Posicio desti1 = new Posicio(p1.get_fila(),posCentral);
            Posicio desti2 = new Posicio(p2.get_fila(),posCentral2);
            _t1 = new TiradaSimple(p1,desti1,_equip,0,1);
            _t2 = new TiradaSimple(p2,desti2,_equip,0,1);
            t.realitzarTirada(_t1);
            t.realitzarTirada(_t2);
            return true;
        }
    }
}

