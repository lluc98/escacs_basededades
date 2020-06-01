package partida;
/** @file Rajola.java
 * @brief Rajola del taulell
 */

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/** @class Rajola
 * @brief Element del taulell on es situen les peces, fa la funció de "background" del nostre taulell
 */
public class Rajola extends ImageView {

    /** @brief  Genera una rajola
     * @param ima Imatge a carregar
     * @param amplada valor d'amplada que ha de tenir l'imatge
     * @pre ima té valor
     * @post S'ha generat una rajola amb una imatge i un tamany definits.
     */
    public Rajola(Image ima, int amplada){
        setImage(ima);
        setFitWidth(amplada);
        setPreserveRatio(true);
        setSmooth(true);
        setCache(true);
    }
}
