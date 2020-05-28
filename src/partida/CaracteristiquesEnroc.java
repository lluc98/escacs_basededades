package partida;

public class CaracteristiquesEnroc {
    private String _pecaA;
    private String _pecaB;
    private boolean _quiets;
    private boolean _buitAlmig;

    public CaracteristiquesEnroc(String pecaA, String pecaB, boolean quiets, boolean buitAlmig){
        _pecaA = pecaA;
        _pecaB = pecaB;
        _quiets = quiets;
        _buitAlmig = buitAlmig;
    }

    public boolean enrrocValid(Enrroc e, Peca b){
        if(b.esPecaNom(_pecaB)){
            e.assignaCaract(_quiets, _buitAlmig);
            return  true;
        }
        return false;
    }



}
