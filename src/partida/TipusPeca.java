package partida;
/** @file TipusPeca.java
 * @brief Tipus de cada Peça que hi ha en el fitxer de Regles.json
 */

import java.util.ArrayList;
import java.util.Iterator;

/** @class TipusPeca
 * @brief Contenidor de Tipus de Peces.
 */
public class TipusPeca {
    private String _nom;                                                ///< Nom de la Peça
    private String _simbol;                                             ///< Simbol de la Peça, normalment és la seva Primera lletra
    private String _imgBlanca;                                          ///< Drecera de l'imatge de la versió de l'equip de fitxes Blanques
    private String _imgNegra;                                           ///< Drecera de l'imatge de la versió de l'equip de fitxes Negres
    private int _valor;                                                 ///< Valor que té la peça
    private boolean _promocio;                                          ///< Capacitat de promorcionar
    private boolean _invulnerabilitat;                                  ///< Capacitat de invulnerabilitat de la Peça
    private ArrayList<Moviment> _llistaMoviments;                       ///< Llista de moviments que pot realitzar cada Peça
    private ArrayList<Moviment> _llistaMovimentsInicials;               ///< Llista de moviments inicials que pot realitzar cada Peça
    private ArrayList<CaracteristiquesEnroc> _llistaEnrocs;             ///< Llista de moviments d'enroc que pot realitzar cada Peça

    /** @brief Simbol de la Peça */
    public String get_simbol() {
        return _simbol;
    }

    /** @brief Drecera de l'imatge de la Peça blanca */
    public String get_imgBlanca() {
        return _imgBlanca;
    }

    /** @brief Drecera de l'imatge de la Peça negra */
    public String get_imgNegra() {
        return _imgNegra;
    }

    /** @brief Valor de la Peça */
    public int get_valor() {
        return _valor;
    }

    /** @brief Llista de Moviments */
    public ArrayList<Moviment> get_llistaMoviments() {
        return _llistaMoviments;
    }

    /** @brief Llista de Moviments Inicials */
    public ArrayList<Moviment> get_llistaMovimentsInicials() {
        return _llistaMovimentsInicials;
    }

    /** @brief Llista de Moviments d'Enroc */
    public ArrayList<CaracteristiquesEnroc> get_llistaEnrocs() {
        return _llistaEnrocs;
    }

    /** @brief  Genera una partida carregada
     * @param nom Nom de la Peça
     * @param simbol Inicial de la Peça
     * @param blanca Drecera de la versió Blanca de la Peça
     * @param negra Drecera de la versió Negra de la Peça
     * @param valor Valor numèric de la Peça
     * @param listMov Llista de Moviments de la Peça
     * @param listIini Llista de Moviments inicials de la Peça
     * @param promocio Capacitat de promocionar-se que té una Peça
     * @param invulnerabilitat Capacitat de invulnerabilitat que té una Peça
     * @pre Tots els camps s'han omplert correctament
     * @post Totes les variables per a assignar un tipus de peça estan settejades.
     */
    public TipusPeca(String nom, String simbol, String blanca, String negra, int valor, ArrayList<Moviment> listMov, ArrayList<Moviment> listIini, boolean promocio, boolean invulnerabilitat ){
        _nom = nom;
        _simbol = simbol;
        _imgBlanca = blanca;
        _imgNegra = negra;
        _valor = valor;
        _promocio = promocio;
        _invulnerabilitat = invulnerabilitat;
        _llistaMoviments = listMov;
        _llistaMovimentsInicials = listIini;
        _llistaEnrocs = new ArrayList<>();



    }

    /** @brief  Mira si un tipus de Peça és vàlid
     * @param t Tirada que ha de realitzar un tipis de peça
     * @param primerMov Valor que diu si es el primer moviment de la Peça
     * @pre TiradaSimple t és correcte
     * @post S'ha validat si el tipus de Peça pot realitzar la tirada t.
     */
    public boolean tipusPecaValida(TiradaSimple t, boolean primerMov){
        boolean trobat = false;
        Iterator<Moviment> it1;
        Iterator<Moviment> it2;
        if(primerMov == false){
            it1 = _llistaMovimentsInicials.iterator(); //Recorrec la llista de moviments inicials
            while(it1.hasNext()){
                if(it1.next().movimentvalid(t)){
                    return true;
                }
            }
        }


        it2 = _llistaMoviments.iterator(); // Recorrec la llista de moviments
        while(it2.hasNext() && !trobat){
            if(it2.next().movimentvalid(t)){
                trobat = true;
            }
        }
        return trobat;
    }

    /** @brief Nom del tipus de Peça */
    public String get_nom(){
        return _nom;
    }

    /** @brief Valor d'invulnerabilitat */
    public boolean get_invulnerabilitat() {return _invulnerabilitat;}

    /** @pre --
     * @post S'ha afegit l'enrroc amb caracteristiques c
     */
    public void afegirEnrroc(CaracteristiquesEnroc c){
        _llistaEnrocs.add(c);
    }

    /** @brief  Comprova si l'enrroc és vàlid
     * @param e valor de l'Enroc
     * @param b Peça a Enrocar
     * @pre --
     * @post S'ha validat l'Enrroc
     */
    public boolean validarEnrroc(Enrroc e, Peca b){
        boolean trobat = false;
        Iterator<CaracteristiquesEnroc> it;
        if(_llistaEnrocs != null){
            it = _llistaEnrocs.iterator();
            while(it.hasNext() && !trobat){
                if(it.next().enrrocValid(e, b)){
                    trobat = true;
                }
            }
        }
        return trobat;

    }

    /** @brief Capacitat de Promocionar-se */
    public boolean getPromocio(){
        return _promocio;
    }
}


