package partida;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PartidaGrafica extends Application{

    private static Scene escenaPrincipal, escenaSec, escenaPartida;
    private static Stage window; //aqui hi posarem el primaryStage, s'ha de fer per a l'hora de fer el canvi d'escena des d'un botó
    private static Partida _partida;
    private static Group _rajoles = new Group();
    private static Group _fitxes = new Group();
    private static int _pixelsRajola;

    public static void main(){
        launch(); //crida init+start
    }

    @Override
    public void start(Stage primaryStage){
        crearEscenaPrincipal();
        window = primaryStage;
        window.setScene(escenaPrincipal);
        window.setTitle("Joc Escacs");
        window.show();
    }

    private void crearEscenaPrincipal(){
        VBox opcions = new VBox(10);
        BorderPane root = new BorderPane();
        Button btnCom = new Button("Començar nova partida");
        Button btnCar = new Button("Carregar una partida");
        btnCom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Es començarà una nova partida");
                escenaSecondaria(1);
                window.setScene(escenaSec);
            }
        });
        btnCar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Es carregarà una partida");
                escenaSecondaria(2);
                window.setScene(escenaSec);
            }
        });
        root.setStyle("-fx-background-image: url("+ "/Images/FonsPrincipal.png" + ");-fx-background-size: stretch;");
        opcions.setAlignment(Pos.CENTER);

        opcions.getChildren().add(btnCom);
        opcions.getChildren().add(btnCar);

        root.setCenter(opcions);
        root.setBottom(botoInferior("Exit"));

        escenaPrincipal = new Scene(root, 500d, 500d);
    }

    private void escenaSecondaria(int opcio){
        BorderPane root = new BorderPane();

        if(opcio == 1){
            root.setStyle("-fx-background-image: url("+ "/Images/fonsComencar.jpg" + ");-fx-background-size: stretch;");
            root.setTop(topLabel("Entra el fitxer de regles que vols començar", root.widthProperty()));
        }
        else{
            root.setStyle("-fx-background-image: url("+ "/Images/fonsCarregar.jpg" + ");-fx-background-size: stretch;");
            root.setTop(topLabel("Entra el fitxer de la partida que vols carregar", root.widthProperty()));
        }
        root.setBottom(botoInferior("Cancelar"));

        root.setCenter(prepararCentre(opcio));

        escenaSec = new Scene(root, 600d,500d);

    }

    private Node prepararCentre(int opcio) {
        TextField nomFitxer = new TextField();
        nomFitxer.setPromptText("Ex: nomFitxer.json");
        Label lbl = new Label("Nom fitxer:");
        Label missatge = new Label();
        HBox hb = new HBox(10);
        hb.getChildren().addAll(lbl, nomFitxer);
        hb.setAlignment(Pos.CENTER);
        VBox vb = new VBox(10);
        Button subBtn = new Button("Submit");
        Button rmvBtn = new Button("Cancelar");
        ChoiceBox cb = new ChoiceBox();
        cb.getItems().addAll(0,1,2);
        vb.getChildren().add(hb);
        if(opcio==1){ vb.getChildren().add(cb); }
        subBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //_partida = new Partida(nomFitxer.getText());
                String s = nomFitxer.getText();
                if(s.isEmpty() || s == null){
                    missatge.setText("Entre un fitxer vàlid");
                }
                else if(opcio == 1 && cb.getValue()==null){
                    missatge.setText("Entre un nombre de jugadors");
                }
                else{
                    if(opcio == 1){
                        _partida = new Partida(s, (int) cb.getValue());
                    }
                    else{
                        _partida = new Partida(s);
                    }
                    crearEscenaPartida();
                    window.setScene(escenaPartida);
                }
            }
        });
        rmvBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                nomFitxer.clear();
            }
        });

        vb.getChildren().addAll(subBtn, missatge);
        vb.setAlignment(Pos.CENTER);
        return vb;
    }

    private static Node topLabel(String s, ReadOnlyDoubleProperty d){
        Label lbl = new Label(s);
        lbl.setAlignment(Pos.BASELINE_CENTER);
        lbl.setPrefHeight(60);
        lbl.prefWidthProperty().bind(d);
        lbl.setStyle("-fx-font-weight: bolder; -fx-font-size: 25px; -fx-font-family: " + "Arial Black" + "; -fx-fill: #818181; " +
                "-fx-effect: innershadow(three-pass-box, rgb(255,99,71), 6, 0.0, 0, 2);");

       return lbl;
    }

    private static Node botoInferior(String s){
        Pane root = new Pane();
        Button btnInf = new Button(s);
        if(s.equals("Exit")){
            btnInf.setOnAction(e -> Platform.exit());
        }
        else {
            btnInf.setOnAction(e -> window.setScene(escenaPrincipal));
        }
        root.setPrefHeight(60);

        btnInf.setLayoutX(30);
        btnInf.setLayoutY(5);
        root.getChildren().add(btnInf);

        return root;
    }

    private static void crearEscenaPartida(){
        BorderPane root = new BorderPane();
        int files = _partida.getFiles();
        int columnes = _partida.getColumnes();
        Rectangle2D r = Screen.getPrimary().getVisualBounds();
        _pixelsRajola = Math.min((int)r.getHeight()/files, (int)r.getWidth()/columnes) - 10;
        System.out.println((int)r.getHeight()-10);
        System.out.println((int)r.getHeight()-10);
        root.setCenter(crearContingutPartida(root));
        root.setRight(crearOpcions(root));
        escenaPartida = new Scene(root);
    }

    private static Node crearOpcions(BorderPane p){
        BorderPane root = new BorderPane();
        VBox opcionsGenerals = new VBox(15);
        root.setPrefWidth(200);
        Button redo = new Button("Refer");
        Button undo = new Button("Desfer");
        Button surrender = new Button("Rendir-se");
        Button postpone = new Button("Ajornar");
        Button tie = new Button("Demanar taules");
        Button exit = new Button("Exit");
        Label lbl = new Label("Torn de " + _partida.getProperTorn());
        lbl.setId("1");
        redo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        undo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        surrender.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //_partida.rendirse();
                VBox root = new VBox();
                Label accept = new Label("El jugador " + _partida.getProperTorn() + " s'ha rendit.");
                Label gg = new Label("Bona partida, fins la pròxima");
                root.getChildren().addAll(accept, gg);
                root.setPadding(new Insets(50));
                root.setAlignment(Pos.CENTER);
                Scene s = new Scene(root, 500d, 500d);
                window.setScene(s);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.exit();
            }
        });
        postpone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _partida.ajornar();
                VBox root = new VBox();
                Label accept = new Label("El jugador " + _partida.getProperTorn() + " ha ajornat la partida.");
                Label gg = new Label("Fins la pròxima");
                root.getChildren().addAll(accept, gg);
                root.setPadding(new Insets(50));
                root.setAlignment(Pos.CENTER);
                Scene s = new Scene(root, 500d, 500d);
                window.setScene(s);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.exit();
            }
        });
        tie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                p.setRight(crearPaneTaules(p));
                _partida.canviarTorn();
            }
        });
        exit.setOnAction( e-> Platform.exit());
        Label avisos = new Label();
        HBox botoMarxar = new HBox(15);
        botoMarxar.getChildren().add(exit);
        botoMarxar.setAlignment(Pos.BOTTOM_CENTER);
        opcionsGenerals.setAlignment(Pos.CENTER);
        opcionsGenerals.getChildren().addAll(lbl, undo, redo, surrender, postpone, tie, botoMarxar);
        root.setPadding(new Insets(20));
        root.setTop(avisos);
        root.setCenter(opcionsGenerals);
        root.setBottom(botoMarxar);
        return root;
    }

    private static Node crearPaneTaules(BorderPane p){
        VBox root = new VBox();
        root.setId("2");
        Label lbl = new Label("El jugador " + _partida.getProperTorn() + " ha demanat taules");
        Button si = new Button("Si");
        Button no = new Button("No");
        si.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                VBox root = new VBox();
                Label accept = new Label("S'han acceptat les taules, s'acaba la partida");
                Label gg = new Label("Bona partida, fins la pròxima");
                root.getChildren().addAll(accept, gg);
                Scene s = new Scene(root, 500d, 500d);
                window.setScene(s);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.exit();
            }
        });
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                p.setRight(crearOpcions(p)); //com que hi ha poca informació i el java disposa de garbage collector,podem anar creant aquesta part i el garbage collector s'encarregarà d'eliminar l'anterior
            }
        });
        root.getChildren().addAll(lbl, si, no);
        root.setAlignment(Pos.CENTER);
        return root;
    }


    private static Node crearContingutPartida(BorderPane p){
        Image img;
        img = new Image("/Images/fons.png");
        Pane root = new Pane();
        root.setPrefSize(_partida.getColumnes() * _pixelsRajola, _partida.getFiles() * _pixelsRajola);
        root.getChildren().addAll(_rajoles, _fitxes);
        for(int i = 0; i < _partida.getFiles(); ++i){
            for(int j = 0; j < _partida.getColumnes(); ++j){
                Rajola rajola = new Rajola(img, _pixelsRajola);
                rajola.setX(j * _pixelsRajola);
                rajola.setY(i * _pixelsRajola);
                _rajoles.getChildren().add(rajola);
                Peca f = _partida.getPeca(new Posicio(i+1, j+1));
                if(f!=null){
                    PecaGrafica pGraf = crearFitxa(f, j, i, p);
                    _fitxes.getChildren().add(pGraf);
                }
            }
        }
        return root;
    }

    private static PecaGrafica crearFitxa(Peca f, int i, int j,BorderPane pane){
        PecaGrafica p = new PecaGrafica(f, _pixelsRajola, i, j);
        p.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int newX = posTauler(p.getLayoutX());
                int newY = posTauler(p.getLayoutY());
                String mov = null;
                int oldX = posTauler(p.get_oldX());
                int oldY = posTauler(p.get_oldY());
                StringBuilder s = new StringBuilder();
                mov = s.append((char)(97+oldX)).append(oldY+1).append(' ').append((char)(97 + newX)).append(newY+1).toString();
                String res = _partida.ferTirada(mov);
                if(res.equalsIgnoreCase("return tirada vàlida i s'ha matat")){ //ha matat la peça que hi havia anteriorment a aquesta posició
                    eliminarPeca(new Posicio(newX, newY));
                    p.move(newX, newY);
                    passarTorn(pane);
                }else if(res.equalsIgnoreCase("tirada vàlida")){ //la tirada s'ha fet a nivell lògic, ara a gràfic
                    p.move(newX, newY);
                    passarTorn(pane);
                }else if(res.equalsIgnoreCase("no s'ha realitzat la tirada")){
                    p.abortMove();
                }else if(res.equalsIgnoreCase("promocio")){

                }else if(res.equalsIgnoreCase("enroc")){

                }else if(res.equalsIgnoreCase("no ernoc")){
                    p.abortMove();
                }
                else{
                    p.abortMove();
                }

            }
        });

        return p;
    }

    private static void passarTorn(BorderPane p){
        _partida.canviarTorn();
        BorderPane dreta = (BorderPane) p.getRight();
        VBox opcions = (VBox) dreta.getCenter();
        Label lbl = (Label) opcions.getChildren().get(0);
        lbl.setText("Torn de " + _partida.getProperTorn());
    }

    private static void eliminarPeca(Posicio p){
        PecaGrafica pg = null;
        for(Node n : _fitxes.getChildren()){
            pg = (PecaGrafica)n;
            if(posTauler(pg.get_oldX()) == p.get_columna() && posTauler(pg.get_oldY()) == p.get_fila()){
                break;
            }
        }
        pg.setVisible(false);
        _fitxes.getChildren().remove(pg);
    }

    private static int posTauler(double p){
        return (int)(p+_pixelsRajola/2)/_pixelsRajola;
    }

}
