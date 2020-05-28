package partida;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Rajola extends ImageView {

    public Rajola(Image ima, int amplada){
        setImage(ima);
        setFitWidth(amplada);
        setPreserveRatio(true);
        setSmooth(true);
        setCache(true);
    }
}
