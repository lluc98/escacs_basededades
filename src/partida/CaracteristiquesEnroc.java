package partida;
/** @file CaracteristiquesEnrroc.java
 * @brief Moviment d'enrroc.
 */

/** @class CaracteristiquesEnroc
 * @brief Controlador d'enrrocs.
 */
public class CaracteristiquesEnroc {
    private String _pecaA;              ///< Tipus de Peça A
    private String _pecaB;              ///< Tipus de Peça B
    private boolean _quiets;            ///< Valor que diu si s'han mogut previament les Peces
    private boolean _buitAlmig;         ///< Valor que ens diu si tenen espai per a fer l'Enrroc

    /** @brief  Assigna les caracteristiques Enrroc
     * @param pecaA Valor de la Peça A
     * @param pecaB Valor de la Peça B
     * @param quiets Variable que avisa si s'han mogut prèviament les Peces A i B
     * @param buitAlmig Valor que ens indica si tenen espai per a realitzar l'Enrroc les Peces A i B
     * @pre --
     * @post Atributs assignats
     */
    public CaracteristiquesEnroc(String pecaA, String pecaB, boolean quiets, boolean buitAlmig){
        _pecaA = pecaA;
        _pecaB = pecaB;
        _quiets = quiets;
        _buitAlmig = buitAlmig;
    }

    /** @brief  Valida l'Enrroc
     * @param e Tipus Enrroc
     * @param b Peça B
     * @pre Enrroc != NULL
     * @post Enrroc validat
     */
    public boolean enrrocValid(Enrroc e, Peca b){
        if(b.esPecaNom(_pecaB)){
            e.assignaCaract(_quiets, _buitAlmig);
            return  true;
        }
        return false;
    }
}
