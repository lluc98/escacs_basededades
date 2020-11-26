package partida;
/** @file Jugador.java
 * @brief Jugador fisic
 */
import static partida.Historial.*;
/** @class Jugador
 * @brief modul que analitzara i executara les jugades del usuari
 */
public class Jugador {


    private boolean _equip;

    /** @brief Genera un Jugador amb els atributs definits
     * @param equip equip del jugador
     * @pre --
     * @post Jugador creat
     */
    Jugador(boolean equip){
        _equip = equip;
    }

    /** @brief Equip */
    public boolean get_equip() {
        return _equip;
    }

    /** @brief Fa totes les comprovacions per veure si la tirada entrada es valida o no i es realitza la tirada
     * @param taulell taulell on es fa la tirada
     * @param origen posicio origen on esta la peça que es vol moure
     * @param desti posicio desti on es vol anar
     * @pre --
     * @post retorna 0 si no s'ha fet la tirada, mes gran de 0 si s'ha fet
     */
    public int ferTirada(Taulell taulell, Posicio origen, Posicio desti){ //0 si tirada no valida, 1 tirada valida, res>1 tirada valida i hi ha morts
        int res = 0;
        TiradaSimple t = new TiradaSimple(origen, desti, _equip);
        if(taulell.tiradaValida(t)){
            res= res + taulell.realitzarTirada(t);
            guardarTirada(t, "");
            taulell.reiniciaTiradesRefer();
        }

        return res;
    }

    /** @brief Comprova si a la posicio entrada es pot fer una promocio
     * @param desti posicio on es comprova si hi ha promocio
     * @param taulell taulell de la partida
     * @pre cert
     * @post mira si es pot fer promocio o no
     */
    public boolean observarPromocio(Posicio desti, Taulell taulell){
        return taulell.hiHaPromocio(desti, _equip);
    }

    /** @brief canvia la peça que hi ha a la posicio pos per la peça pec
     * @param posicio posicio on se canviara la peça
     * @param taulell taulell de la partida
     * @param p nova peça que es ficara al taulell
     * @pre peça i pos existeixen
     * @post promocio feta
     */
    public void ferPromocio(Posicio posicio, Taulell taulell, Peca p){
        taulell.realitzarPromocio(posicio, p);
    }

    /** @brief Comprova si hi ha un jaque i si hi ha escac i mat.
     * @param taulell taulell de la partida
     * @pre --
     * @post mira si hi ha escac o escac i mat o no
     */
    public int observarJaque(Taulell taulell){
        TiradaSimple t = taulell.hihaJaque(_equip);
        if(t!=null){
            if(taulell.hiHaJaqueMate(t)){
                return 2;
            }
            return 1;
        }
        return  0;
    }

    /** @brief Comprova si hi ha un jaque.
     * @param taulell taulell de la partida
     * @pre --
     * @post mira si hi ha escac o no
     */
    public boolean shaProvocatJaque(Taulell taulell){
        TiradaSimple t =taulell.hihaJaque(!_equip);
        return t != null;
    }

    /** @brief Fa totes les comprovacions per veure si l'enroc es valid o no i es realitza l'enroc si es valid
     * @param taulell taulell on es fa la tirada
     * @param p1 posicio origen on esta la peça que es vol enroca
     * @param p2 posicio desti on esta la peça que cubrira a p1
     * @pre --
     * @post cert si es fa l'enrroc, fals altrament
     */
    public boolean ferEnrroc(Taulell taulell, Posicio p1, Posicio p2){
        Enrroc enrroc = new Enrroc(p1,p2, _equip);
        if(taulell.validarEnrroc(enrroc)){
            String a = "";
            a = enrroc.realitzarEnroc(taulell);
            if(!a.equals("")){
                TiradaSimple t= new TiradaSimple(p1,p2,_equip);
                guardarTirada(t, a);
                return true;
            }
            else return false;
        }
        else return false;
    }
}

