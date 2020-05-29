package partida;
/** @file Peca.java
 * @brief Peça dels Escacs
 */

import java.util.TreeMap;

/** @class Peça
 * @brief Element d'una partida d'escacs que interactua amb el joc
 */
public class Peca {
    private TipusPeca _tipus;               ///< Conjunt d'atributs que defineixen la Peça
    private boolean _primerMoviment;        ///< Controlador de si s'ha fet el primer moviment
    private boolean _equip;                 ///< Controlador del equip al que pertany la Peça

    /** @brief Genera una peça buida
     * @pre --
     * @post Peça buida generada
     */
    public Peca(){
        _primerMoviment = false;
        _equip = false;
    }

    /** @brief Genera una Peça amb els atributs definits
     * @param s Nom de la Peça
     * @param equip Equip de la Peça
     * @param m Atributs de la Peça
     * @pre m té mapejat un Tipus de Peça amb el nom s
     * @post Peça nova generada
     */
    public Peca(String s, boolean equip, TreeMap<String,TipusPeca> m){
        _tipus = m.get(s);
        if(_tipus != null){
            _primerMoviment = false;
            _equip = equip;
        }
        else{
            throw new RuntimeException("Tipus de peça no trobada");
        }
    }

    /** @brief Genera una Peça amb els atributs definits
     * @param s Nom de la Peça
     * @param equip Equip de la Peça
     * @param m Atributs de la Peça
     * @param moguda Booleà que avisa si s'ha mogut ja la Peça
     * @pre m té mapejat un Tipus de Peça amb el nom s
     * @post Peça nova generada amb atribut moguda definit
     */
    public Peca(String s, boolean equip, TreeMap<String,TipusPeca> m, boolean moguda){
        _tipus = m.get(s);
        if(_tipus != null){
            _primerMoviment = moguda;
            _equip = equip;
        }
        else{
            throw new RuntimeException("Tipus de peça no trobada");
        }
    }

    /** @brief Validació de la validesa d'una tirada
     * @param t Tirada Simple
     * @pre --
     * @post S'ha vàlidat la tirada
     */
    public boolean movimentValid(TiradaSimple t){
        boolean valid;
        if(!_equip){
            t.canviarSigneDesplacament();
        }

        if(_tipus.tipusPecaValida(t,_primerMoviment)){
            valid = true;
        }
        else{
            valid = false;
        }

        if(!_equip){
            t.canviarSigneDesplacament();
        }
        return valid;

    }

    /** @brief Equip */
    public boolean get_equip(){
        return _equip;
    }

    /** @brief Mètode per a canviar Primer Moviment
     * @pre --
     * @post Atribut _primerMoviment canviat
     */
    public void primerMovFet(){
        if(_primerMoviment == false){
            _primerMoviment = true;
        }
    }

    /** @brief Valida si la Peça actual és el REI
     * @pre --
     * @post true si la Peça actual és el REI, fals altrament
     */
    public boolean esRei(boolean equip){
        return (_tipus.get_nom().equals("REI") && _equip != equip);
    }

    /** @brief Validació del Enrroc mirant si és el primer Moviment
     * @param e Enrroc que es vol realitzar
     * @param b Peça amb la que es vol fer l'Enrroc
     * @pre --
     * @post S'ha vàlidat si les dues Peces no han fet el primer Moviment
     */
    public boolean enrrocValid(Enrroc e, Peca b){
        if(_tipus.validarEnrroc(e, b)){
            if(e.getQuiets()==true && _primerMoviment==true){
                return false;
            }
            else{
                return true;
            }
        }
        else{
            return false;
        }
    }



    /** @brief Comparació del nom de dues Peces
     * @pre --
     * @post true es diuen igual, false altrament.
     */
    public  boolean esPecaNom(String nom){
        return(_tipus.get_nom().equals(nom));
    }

    /** @brief Nom de la Peça */
    public String getNom(){
        return _tipus.get_nom();
    }

    /** @brief Invulnerabilitat */
    public  boolean esInvulnerable(){ return _tipus.get_invulnerabilitat();}

    /** @brief Tipus */
    public TipusPeca get_tipus() { return _tipus; }

    /** @brief  Converteix la Peça actual en la seva primera lletra, en majuscula o minscula
     * en funció de quin equip sigui (NEGRES minuscula, BLANQUES majuscula )
     * @pre --
     * @post Retorna una lletra que és la primera del nom de la Peça, en majuscula o minuscula.
     */
    public String toString(){
        String s = "";
        String p = _tipus.get_simbol();
        char c = p.charAt(0);
        if(_equip == false){
            c = Character.toLowerCase(c);
        }

        s = s + c;

        return s;

    }
}
