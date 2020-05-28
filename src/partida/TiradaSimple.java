package partida;
/** @file TiradaSimple.java
 * @brief Control de tirades.
 */

/** @class TiradaSimple
 * @brief Tirada que realitza cada jugador en cada torn
 */
public class TiradaSimple {
    private Posicio _origen;        ///< Posició origen de la Tirada
    private Posicio _desti;         ///< Posició destí de la Tirada
    private int _desplacamentX;     ///< Valor de desplaçament en l'eix X
    private int _desplacamentY;     ///< Valor de desplaçament en l'eix Y
    private int _matar;             ///< Valor de la qualitat de matar
    private int _volar;             ///< Valor de la qualitat de volar
    private boolean _equip;         ///< Equip del jugador que realitza la tirada

    /** @brief  Genera una TiradaSimple sense matar ni volar
     * @param origen Posició origen de la Tirada
     * @param desti Posició desti de la Tirada
     * @param equip Equip del Jugador que ralitza la Tirada
     * @pre origen, desti i equip són correctes
     * @post totes les variables de la tirada estan ben posades
     */
    public TiradaSimple(Posicio origen, Posicio desti, boolean equip){
        _origen = origen;
        _desti = desti;
        _desplacamentX = _desti.get_fila() - _origen.get_fila();
        _desplacamentY = _desti.get_columna() - _origen.get_columna();
        _equip = equip;
    }

    /** @brief Desplaçament */
    public int get_desplacamentX() {
        return _desplacamentX;
    }

    /** @brief Equip */
    public boolean is_equip() {
        return _equip;
    }

    /** @brief  Genera una TiradaSimple amb matar i volar
     * @param origen Posició origen de la Tirada
     * @param desti Posició desti de la Tirada
     * @param equip Equip del Jugador que ralitza la Tirada
     * @param matar Capacitat de matar que té una Peça
     * @param volar Capacitat de volar que té una Peça
     * @pre origen, desti, equip, matar i volar són correctes
     * @post totes les variables de la tirada estan ben posades
     */
    public TiradaSimple(Posicio origen, Posicio desti, boolean equip, int matar, int volar){
        _origen = origen;
        _desti = desti;
        _desplacamentX = _desti.get_fila() - _origen.get_fila();
        _desplacamentY = _desti.get_columna() - _origen.get_columna();
        _equip = equip;
        _matar = matar;
        _volar = volar;
    }

    /** @brief Origen */
    public Posicio get_origen(){
        return _origen;
    }

    /** @brief Destí */
    public Posicio get_desti(){
        return _desti;
    }

    /** @brief Desplaçament en l'eix de les X */
    public int get_desplaçamentX(){
        return _desplacamentX;
    }

    /** @brief Desplaçament en l'eix de les Y */
    public int get_desplacamentY(){
        return _desplacamentY;
    }

    /** @brief  Canvia el signe de desplaçament X i Y
     * @pre --
     * @post Desplaçament X i Desplaçament Y tenen els signes invertits
     */
    public void canviarSigneDesplacament(){
        _desplacamentX = _desplacamentX * -1;
        _desplacamentY = _desplacamentY * -1;
    }

    /** @brief  Guarda el valor de Matar
     * @pre --
     * @post A Matar s'hi assigna el valor n
     */
    public void guardarMatar(int n){
        _matar = n;
    }

    /** @brief  Guarda el valor de Volar
     * @pre --
     * @post A Volar s'hi assigna el valor n
     */
    public void guardarVolar(int n){
        _volar = n;
    }

    /** @brief Equip del Jugador de la Tirada (true = BLANQUES, false = NEGRES */
    public boolean get_equip(){
        return _equip;
    }

    /** @brief Matar */
    public int get_matar(){
        return _matar;
    }

    /** @brief Volar */
    public int get_volar(){
        return _volar;
    }

}
