package partida;
/** @file Moviment.java
 @brief Un moviment per a una Peça d'Escacs.
 */

/** @class Moviment
 @brief Un moviment amb coordenades i atributs de matar i saltar.
 @details Les coordenades de moviment es calculen de forma horitzontal i vertical,
 de forma que ens indiquen si son moviments definits o lineals. L'atribut movMatar
 ens indica si el moviment permet matar Peces; l'atribut movSlaltar ens indica si el moviment
 permet saltar Peces.
 */
public class Moviment {
    private String _horitzontal;        ///< Tipus de Moviment en horitzontal
    private String _vertical;           ///< Tipus de Moviment en vertical
    private int _movMatar;              ///< Capacitat de Matar
    private int _movSaltar;             ///< Capacitat de Saltar

    /** @brief Moviment horitzontal */
    public String get_horitzontal() {
        return _horitzontal;
    }

    /** @brief Moviment vertical */
    public String get_vertical() {
        return _vertical;
    }

    /** @brief Capacitat de Matar */
    public int get_movMatar() {
        return _movMatar;
    }

    /** @brief Capacitat de Saltar */
    public int get_movSaltar() {
        return _movSaltar;
    }

    /** @param horitzontal Moviment Horitzontal
     @param vertical Moviment Vertical
     @param matar Capacitat de Matar
     @param saltar Capacitat de Saltar
     */
    public Moviment(String horitzontal, String vertical, int matar, int saltar){
        _horitzontal = horitzontal;
        _vertical = vertical;
        _movMatar = matar;
        _movSaltar = saltar;
    }

    /** @brief Retorna la Capacitat de Saltar */
    public int potSaltar(){
        return _movSaltar;
    }

    /** @brief Retorna la Capacitat de Matar */
    public int potMatar(){
        return _movMatar;
    }

    /** @brief Diu si un moviment és vàlid.
     @pre /p t es el tauler de joc
     @return la validesa del moviment de la tirada actual
     */
    public boolean movimentvalid(TiradaSimple t){
        String x = _horitzontal.substring(_horitzontal.length()-1);
        String y = _vertical.substring(_vertical.length()-1);
        if(x.equals(y)){
            if(Math.abs(t.get_desplaçamentX()) != Math.abs(t.get_desplacamentY())){
                return false;
            }
        }
        if(valid(t.get_desplaçamentX(), _horitzontal) && valid(t.get_desplacamentY(), _vertical)){
            t.guardarMatar(_movMatar);
            t.guardarVolar(_movSaltar);
            return true;
        }
        else{
            return false;
        }
    }

    /** @brief Diu si un moviment és vàlid.
     @pre /p desp i /p s són el desplaçament i el eix on es realitza
     @return la validesa del desplaçament actual
     */
    private boolean valid(int desp, String s){
        int mov;
        try {
            mov = Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            mov = 100;
        }
        if(desp == mov){
            return true;
        }
        else if(mov == 100){
            if(desp>=0){
                if(s.equals(" a") || s.equals(" b") || s.equals(" m") || s.equals(" n") || s.equals(" - a") || s.equals(" - b") ){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if(s.equals(" - a") || s.equals(" - b") || s.equals(" a") || s.equals(" b")){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
}

