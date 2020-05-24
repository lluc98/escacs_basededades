package Partida;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PartidaGrafica extends Application{

    private static Scene escenaPrincipal, escenaSec;
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

    }

}
