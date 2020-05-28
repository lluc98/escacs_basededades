package partida;
/** @file Partida.java
 * @brief Posició taulell.
 */

/** @class Posicio
 * @brief Controlador de Posicions del taulell
 */
public class Posicio implements Comparable<Posicio>{
    private int _fila;          ///< Nombre de fila
    private int _columna;       ///< Nombre de columna
    private String _posicio;    ///< Entrada de fila i columna p.e. a5

    /** @brief  Genera una Posicó
     * @param p Nom de la posció
     * @pre p té un valor vàlid
     * @post S'ha assignat la posició entrada
     */
    public Posicio(String p){
        _posicio = p;
        String numero = p.substring(p.length()-1);
        String lletra = p.substring(0,1);
        _columna = lletra.charAt(0) - 'a' + 1;
        _fila = Integer.parseInt(numero);
    }

    /** @brief  Genera una Posicó
     * @param fila fila de la posció
     * @param columna columna de la posció
     * @pre fila i columna tenen un valor vàlid
     * @post S'ha assignat la posició entrada
     */
    public Posicio(int fila, int columna){
        _fila = fila;
        _columna = columna;
        columna = columna + 64;
        char letra = (char)(columna);
        letra = Character.toLowerCase(letra);
        _posicio = String.valueOf(letra);
        _posicio = _posicio+_fila;
    }

    /** @brief Fila */
    public int get_fila(){
        return _fila;
    }

    /** @brief Columna */
    public int get_columna(){
        return _columna;
    }

    /** @brief Posició */
    public String get_posicio(){
        return _posicio;
    }

    /** @brief  Comparació de dues Posicions
     * @param o Posició amb la que es compara l'objecte actual
     * @pre Posició o és valida
     * @post 0 si són iguals, > 0 si l'actual és més gran, < 0 si l'actual és més petita
     */
    @Override
    public int compareTo(Posicio o) {
        int r = _fila - o._fila;
        if (r == 0)
            if (_columna > o._columna)
                r = 1;
            else if(_columna < o._columna)
                r = -1;
        return r;
    }


    /** @brief  Comparació de dues Posicions
     * @param o Posició amb la que es compara l'objecte actual
     * @pre Posició o és valida
     * @post true iguals, false altrament
     */
    @Override
    public boolean equals(Object o) {
        boolean r = false;
        if (o != null && (o instanceof Posicio)){
            Posicio m = (Posicio)o;
            r = this._fila == m._fila && this._columna == m._columna;
        }
        return r;
    }
}