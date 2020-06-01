package partida;
/** @file PecaGrafica.java
 * @brief Aspecte de la Peça en mode gràfic
 */

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

/** @class PecaGrafica
 * @brief Elment del taulell que ens mostra l'aspecte de la Peça
 */

public class PecaGrafica extends StackPane {
    private int _pixels;                    ///< Tamany de la Peça
    private Peca _fitxa;                    ///< Objecte que ens defineix els atributs de la Peça
    private double _mouseX, _mouseY;        ///< Posició on es desplaça la Peça
    private double _oldX, _oldY;            ///< Posició on es troba la Peça


    /** @brief  Genera una Peça en mode gràfic
     * @param fitxa Peça
     * @param pixels Tamany de la Peça
     * @param x Posició x on volem moure la Peça
     * @param y Posició y on volem moure la Peça
     * @pre Peça existent
     * @post tots els atributs de la PeçaGrafica estan settejats
     */
    public PecaGrafica(Peca fitxa, int pixels, int x, int y){
        _fitxa = fitxa;
        _pixels = pixels;
        move(x,y);
        StackPane formaPeca = crearFitxa(_pixels*0.6, _pixels*0.6);
        getChildren().add(formaPeca);
        setAlignment(Pos.CENTER);
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                _mouseY = event.getSceneY();
                _mouseX = event.getSceneX();
            }
        });
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                relocate(_oldX + event.getSceneX() - _mouseX, _oldY + event.getSceneY() - _mouseY);
            }
        });
    }

    /** @brief  Crea una fitxa en el mmode gràfic
     * @param ample Tamany d'amplada
     * @param alt Tamany d'altura
     * @pre --
     * @post Retorna una Peça gràfica
     */
    private StackPane crearFitxa(double ample, double alt) {
        boolean equip = _fitxa.get_equip();
        String strImg = "/Images/";
        String nomPeca = _fitxa.get_tipus().get_nom();
        StackPane root = new StackPane();
        if(nomPeca.equalsIgnoreCase("peo") || nomPeca.equalsIgnoreCase("rei") || nomPeca.equalsIgnoreCase("dama") || nomPeca.equalsIgnoreCase("torre")
                || nomPeca.equalsIgnoreCase("alfil") || nomPeca.equalsIgnoreCase("cavall")){
            if(equip){
                strImg = strImg + _fitxa.get_tipus().get_imgBlanca();
            }
            else{
                strImg = strImg +  _fitxa.get_tipus().get_imgNegra();
            }
            root.setPrefSize(ample, alt);
            root.setStyle("-fx-background-image: url(" + strImg + ");-fx-background-size: stretch;");
            root.setTranslateX(_pixels * 0.19);
            root.setTranslateY(_pixels * 0.19);
        }
        else{ //és una peça customitzada
            Text text = new Text(nomPeca);
            if(equip){
                strImg = strImg + "customB.png";
                text.setFill(Color.BLACK);
            }
            else{
                strImg = strImg + "customN.png";
                text.setFill(Color.WHITE);
            }
            text.setFont(Font.font("arial", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, _pixels*0.15));
            root.setPrefSize(ample, alt);
            root.setStyle("-fx-background-image: url(" + strImg + ");-fx-background-size: stretch;");
            root.getChildren().add(text);
            root.setAlignment(Pos.CENTER);
            text.setTextAlignment(TextAlignment.CENTER);
            root.setTranslateX(_pixels * 0.19);
            root.setTranslateY(_pixels * 0.19);
        }
        return root;
    }

    /**@pre --
     * @post Cambia la posició de la Peça
     */
    public void move(int x, int y){
        _oldX = x * _pixels;
        _oldY = y * _pixels;
        relocate(_oldX, _oldY);
    }

    /** @brief Cancela un moviment, recoloca les posicions inicials de la Peça gràfica */
    public void abortMove(){
        relocate(_oldX, _oldY);
    }

    /** @brief Peça de PeçaGrafica */
    public Peca getTipus(){
        return _fitxa;
    }

    /** @brief Posició x de la Peça */
    public double get_oldX(){
        return _oldX;
    }

    /** @brief Posició y de la Peça */
    public double get_oldY(){
        return _oldY;
    }

    /** @brief Setteja la posició x de la Peça */
    public void setOldX(int x){
        _oldX = x;
    }

    /** @brief Setteja la posició y de la Peça */
    public void setOldY(int y){
        _oldY = y;
    }


}
