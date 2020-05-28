package partida;
/** @file Enrroc.java
 * @brief Enrroc
 */

/** @class Enrroc
 * @brief Objecte que guarda un tipus d'enrroc vàlid de la Partida
 */
public class Enrroc {
    private Posicio p1;                   ///< Posició de la primera Peça
    private Posicio p2;                   ///< Posició de la segona Peça
    private boolean _equip;               ///< Valor del equip que pot fer aquest enrroc
    private boolean _quiets;              ///< Valor que avisa si s'han mogut la Peça A o la Peça B
    private boolean _buitAlmig;           ///< Valor que avisa si hi ha espai per a realitzar l'Enrroc
    private TiradaSimple _t1;             ///< Valor de la Tirada que ha de realitzar la Peça 1
    private TiradaSimple _t2;             ///< Valor de la Tirada que ha de realitzar la Peca 2

    /** @brief  Assigna els atributs de l'Enrroc
     * @param a Valor de la Posició A
     * @param b Valor de la Posició b
     * @param equip Bándol del equip que pot realitzar aquest Enrroc
     * @pre --
     * @post Atributs assignats
     */
    public Enrroc(Posicio a, Posicio b, boolean equip){
        p1 = a;
        p2 = b;
        _equip = equip;

    }

    /** @brief Posició 1 */
    public Posicio get_p1(){
        return p1;
    }

    /** @brief Posició 2 */
    public Posicio get_p2(){
        return p2;
    }

    /** @brief Equip */
    public boolean get_equip(){
        return _equip;
    }

    /** @brief Guarda les caracteristiques del enrroc
     * @param quiets Valor que indica si s'han mogut les Peces del Enrroc
     * @param buit Valor que indica si hi ha espai per a realitzar l'Enrroc
     * @pre --
     * @post S'han guardat les caracteristiques de quiets i buit.
     */
    public void assignaCaract(boolean quiets, boolean buit){
        _quiets = quiets;
        _buitAlmig = buit;
    }

    /** @brief Quiets */
    public boolean getQuiets(){
        return _quiets;
    }

    /** @brief Buit */
    public boolean getBuit(){
        return _buitAlmig;
    }

    /** @brief Realitza l'acció de l'Enrroc
     * @param t Valor del Taulell
     * @pre --
     * @post true s'ha realitzat l'Enrroc, false altrament.
     */
    public boolean realitzarEnroc(Taulell t){
        int posCentral;
        int posCentral2;
        if(p1.get_columna() < p2.get_columna()){ //si la peça a enrrocar queda a la esquerra
            posCentral = p1.get_columna()+p2.get_columna();
            posCentral = (int) Math.ceil((double)posCentral/2d);
            posCentral2 = posCentral-1;
        }
        else{ //si la peça a enrrocar queda a la dreta
            posCentral = p1.get_columna()+p2.get_columna();
            posCentral = (int) Math.ceil((double)posCentral/2d);
            posCentral2 = posCentral+1;
        }

        TiradaSimple aux = new TiradaSimple(p1,p2,_equip);
        if(t.hiHaPecesEntremig(aux) && _buitAlmig){ //si hi ha peces entremig i ha destar buit entre mig retorna fals
            return false;
        }
        else{
            Posicio desti1 = new Posicio(p1.get_fila(),posCentral);
            Posicio desti2 = new Posicio(p2.get_fila(),posCentral2);
            if(t.contePeçaCasella(desti1) || t.contePeçaCasella(desti2)){
                return false;
            }
            _t1 = new TiradaSimple(p1,desti1,_equip,0,1);
            _t2 = new TiradaSimple(p2,desti2,_equip,0,1);
            t.realitzarTirada(_t1);
            t.realitzarTirada(_t2);
            return true;
        }
    }
}

