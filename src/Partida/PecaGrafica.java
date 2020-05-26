package Partida;

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
        Pane formaPeca = crearFitxa();
    }

    private static Pane crearFitxa(){
        //String tipus = _fitxa.get_tipus().get;
        return null;
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
