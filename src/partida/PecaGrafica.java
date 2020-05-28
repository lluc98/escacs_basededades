package partida;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class PecaGrafica extends StackPane {
    private int _pixels;
    private Peca _fitxa;
    private double _mouseX, _mouseY;
    private double _oldX, _oldY;


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
            if(equip){
                strImg = strImg + "customB.png";
            }
            else{
                strImg = strImg + "customN.png";
            }
            Text text = new Text(nomPeca);
            root.setPrefSize(ample, alt);
            root.setStyle("-fx-background-image: url(" + strImg + ");-fx-background-size: stretch;");
            root.getChildren().add(text);
            root.setAlignment(Pos.CENTER);
            text.setTextAlignment(TextAlignment.CENTER);
        }
        return root;
    }

    public void move(int x, int y){
        _oldX = x * _pixels;
        _oldY = y * _pixels;
        relocate(_oldX, _oldY);
    }

    public void abortMove(){
        relocate(_oldX, _oldY);
    }

    public Peca getTipus(){
        return _fitxa;
    }

    public double get_oldX(){
        return _oldX;
    }

    public double get_oldY(){
        return _oldY;
    }

    public void setOldX(int x){
        _oldX = x;
    }

    public void setOldY(int y){
        _oldY = y;
    }


}
