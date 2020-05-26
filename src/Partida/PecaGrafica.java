package Partida;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class PecaGrafica extends StackPane {
    private int _pixels;
    private Peca _fitxa;
    private double _mouseX, _mouseY;
    private double _oldX, _oldY;


    public PecaGrafica(Peca fitxa, int pixels, int x, int y){
        _fitxa = fitxa;
        _pixels = pixels;
        move(x,y);
        Pane formaPeca = crearFitxa(_pixels * 0.3d, _pixels * 0.3d);
        getChildren().add(formaPeca);
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

    private Pane crearFitxa(double ample, double alt){
        boolean equip = _fitxa.get_equip();
        String strImg;
        if(equip){
            strImg = _fitxa.get_tipus().get_imgBlanca();
        }
        else{
            strImg = _fitxa.get_tipus().get_imgNegra();
        }
        Pane root = new Pane();
        root.setStyle("-fx-background-image: url(" + "/Images/" + strImg + ");-fx-background-size: stretch;");
        root.setPrefSize(ample, alt);
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


}
