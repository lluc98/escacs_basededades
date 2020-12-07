package partida;

/** @file PartidaGrafica.java
 * @brief Una partida d'escacs en mode gràfic.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/** @class PartidaGrafica
 * @brief Partida gàfica, extensió de la classe Application de la API de java
 */

public class PartidaGrafica extends Application{

    private static Scene escenaPrincipal;            ///< Primera escena de l'aplicació
    private static Scene escenaCrearCarregarPartida; ///< Escena per crear o carregar una partida
    private static Scene escenaSec;                  ///< Escena per triar fitxers i si fes falta, el nombre de jugadors
    private static Scene escenaPartida;              ///< Escena per jugar la partida
    private static Stage window;                     ///< Finestra de l'aplicació
    private static Partida _partida;                 ///< Joc d'escacs
    private static Group _rajoles = new Group();     ///< Grup de rajoles
    private static Group _peces = new Group();       ///< Grup de peces
    private static int _pixelsRajola;                ///< Pixels d'un costat de la rajola
    private static String usuari1, usuari2;         ///< Usuaris que hauran de fer el login per jugar.

    /** @brief  Inicia l'aplicació d'escacs
     * @pre --
     * @post s'executa una aplicació per poder jugar a escacs
     */
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
        opcions.setAlignment(Pos.CENTER);

        HBox paswrd = new HBox(10);
        paswrd.setAlignment(Pos.CENTER);

        HBox btns = new HBox(10);
        btns.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();

        Button btnSig = new Button("Sign in");
        Button btnLog = new Button("Log in");

        btnSig.setStyle("-fx-background-image: url(" + "/Images/woodTexture.png" + "); -fx-font-weight: bold");
        btnLog.setStyle("-fx-background-image: url(" + "/Images/woodTexture.png" + "); -fx-font-weight: bold");

        TextField usuari = new TextField();
        usuari.setMaxWidth(200);
        usuari.setPromptText("Username");
        usuari.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-border-style: dotted; -fx-border-insets: 1 1 1 1; -fx-text-fill: white; -fx-border-color: white;");

        /*Button showPassword = new Button("Show password");
        showPassword.setStyle("-fx-background-image: url(" + "/Images/woodTexture.png" + "); -fx-font-weight: bold");*/

        PasswordField contrasenya = new PasswordField();
        contrasenya.setPromptText("Password");
        contrasenya.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-border-style: dotted; -fx-border-insets: 1 1 1 1; -fx-text-fill: white; -fx-border-color: white;");

        Label lblInfo = new Label();
        lblInfo.setStyle("-fx-text-fill: white; -fx-font-weight: bold");

        btnSig.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                /*
                Quan es cliqui aquest botó, es mirarà a la bdd si existeix un usuari amb aquest nom, si existeix no es deixara registrar
                si no existeix es registrarà l'usuari. Un cop hagi sortit que l'usuari s'ha registrat, se li demanarà que fagi el login.
                */

                //variables per demanar a la bdd.
                String nom;
                nom = usuari.getText();
                String contra;
                contra = contrasenya.getText();



                if(/* s'ha trobat l'usuari */true){
                    lblInfo.setText("No et pots registrar, hi ha un usuari amb el mateix nom");
                }
                else{
                    /* es posarà l'usuari a la bdd amb la contrasenya que ha posat */
                    lblInfo.setText("T'has registrat. Ara pots fer el login");
                    usuari.clear();
                    contrasenya.clear();
                }

            }
        });
        btnLog.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Es carregarà una partida");
                /*
                Quan es cliqui aquest boto es mirarà si l'usuari existeix, si l'usuari existeix, entrarà i anirà a la escena de començar/carregar partida,
                sinò, sortirà un error i es demanara que es torni a entrar l'usuari i contrasenya
                */
                String nom = usuari.getText();
                String contra = contrasenya.getText();

                Jedis jedis = new Jedis("localhost", 6379);
                String userName = jedis.hget("user:"+nom, "nom");
                String password = jedis.hget("user:"+nom, "contrasenya");
                System.out.println("HOLA " + userName);

                if(userName != null && password.equals(contra)){
                    crearCrearCarregarPartida();

                    window.setScene(escenaCrearCarregarPartida);
                }
                else if(userName == null){ //Usuari no registrat
                    lblInfo.setText("No s'ha trobat l'usuari");
                }
                else{ //usuari registrat, contrasenya incorrecte
                    lblInfo.setText("Contrasenya incorrecte");
                    contrasenya.clear();
                }
            }
        });

        /*showPassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });*/

        root.setStyle("-fx-background-image: url("+ "/Images/darkWoodTexture.png" + ");-fx-background-size: stretch;");

        paswrd.getChildren().add(contrasenya);
        /*paswrd.getChildren().add(showPassword);*/

        btns.getChildren().add(btnSig);
        btns.getChildren().add(btnLog);

        opcions.setAlignment(Pos.CENTER);

        opcions.getChildren().add(usuari);
        opcions.getChildren().add(paswrd);
        opcions.getChildren().add(btns);
        opcions.getChildren().add(lblInfo);

        root.setCenter(opcions);
        root.setBottom(botoInferior("Exit"));

        escenaPrincipal = new Scene(root, 500d, 500d);

    }


    /** @brief  Crea l'escena principal
     * @pre --
     * @post Escena principal on l'usuari pot triar si començar una partida nova o carregar-ne una de començada o sortir.
     */
    private void crearCrearCarregarPartida(){
        VBox opcions = new VBox(10);

        BorderPane root = new BorderPane();

        Button btnCom = new Button("Començar nova partida");
        Button btnCar = new Button("Carregar una partida");

        btnCom.setStyle("-fx-background-image: url(" + "/Images/woodTexture.png" + "); -fx-font-weight: bold");
        btnCar.setStyle("-fx-background-image: url(" + "/Images/woodTexture.png" + "); -fx-font-weight: bold");
        btnCom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Es començarà una nova partida");
                escenaSecundaria(1);
                window.setScene(escenaSec);
            }
        });
        btnCar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Es carregarà una partida");
                escenaSecundaria(2);
                window.setScene(escenaSec);
            }
        });
        root.setStyle("-fx-background-image: url("+ "/Images/darkWoodTexture.png" + ");-fx-background-size: stretch;");
        opcions.setAlignment(Pos.CENTER);

        opcions.getChildren().add(btnCom);
        opcions.getChildren().add(btnCar);

        root.setCenter(opcions);
        root.setBottom(botoInferior("Exit"));

        escenaCrearCarregarPartida = new Scene(root, 500d, 500d);
    }

    /** @brief  Crea l'escena secundaria
     * @pre \p opcio si igual a 1 indica que es comença una partida, si val 2 es carrega una partida
     * @post Es crea una escena secundaria on l'usuari haurà d'entrar el nom del fitxer que necessita i \p opcio igual a 1, el nombre de jugadors,
     * juntament amb un botó per confirmar el que ha entrat i un botó per anar a l'escena anterior en cas que s'hagi equivocat.
     */
    private void escenaSecundaria(int opcio){
        BorderPane root = new BorderPane();

        root.setStyle("-fx-background-image: url("+ "/Images/darkWoodTexture.png" + ");-fx-background-size: stretch;");
        if(opcio == 1){ //un cop es sapiga quina estructura fem, s'haura de fer una llista de les regles que hi ha ((((si fem aixo de les regles al final))))
            root.setTop(topLabel("Posa un nom a la partida.", root.widthProperty()));
        }
        else{ //un cop es sapiga quina estructura fem, s'haura de fer una llista de les partides que hi ha
            root.setTop(topLabel("Tria la partida que vols carregar", root.widthProperty()));
        }
        root.setBottom(botoInferior("Cancelar"));

        root.setCenter(prepararCentre(opcio));

        escenaSec = new Scene(root, 600d,500d);

    }

    /** @brief  Es prepara el centre per l'escena secundaria
     * @pre \p opcio si igual a 1 indica que es comença una partida, si val 2 es carrega una partida
     * @return Un node on hi haurà un TextField perquè l'usuari hi entri el nom del fitxer, si \p opcio igual a 1, també hi haurà un CheckBox
     * per triar el nombre de jugadors i per últim un botó per confirmar les dades que ha entrat
     */
    private Node prepararCentre(int opcio) {
        TextField nomFitxer = new TextField();
        nomFitxer.setPromptText("Ex: nomFitxer.json");
        nomFitxer.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-border-style: dotted; -fx-border-insets: 1 1 1 1; -fx-text-fill: white; -fx-border-color: white;");

        Label lbl = new Label("Nom fitxer:");
        lbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");

        Label missatge = new Label();
        missatge.setStyle("-fx-text-fill: white; -fx-font-weight: bold");

        HBox hb = new HBox(10);
        hb.getChildren().addAll(lbl, nomFitxer);
        hb.setAlignment(Pos.CENTER);

        VBox vb = new VBox(10);

        Button subBtn = new Button("Submit");
        subBtn.setStyle("-fx-background-image: url(" + "/Images/woodTexture.png" + "); -fx-font-weight: bold");

        vb.getChildren().add(hb);
        ListView<String> llistaPartides = new ListView<>();


        if(opcio == 0){
            ObservableList<String> nomPartides = FXCollections.observableArrayList();
            /*
            Posar tots els noms de partides a l'observableList
             */

            llistaPartides.setItems(nomPartides);
        }


        subBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String s = llistaPartides.getSelectionModel().getSelectedItem();
                if(s.isEmpty() || s == null){
                    missatge.setText("Entre un fitxer.");
                }
                else{
                    if(opcio == 1){
                        try {
                            _partida = new Partida(s, 2);
                            crearEscenaPartida();
                            window.setScene(escenaPartida);
                        } catch (IOException e) {
                            missatge.setText("No s'ha trobat el fitxer de regles, entra'n un altre de correcte.");
                            System.out.println(e);
                            System.out.println("No s'ha trobat el fitxer de regles");
                        } catch (Exception e){
                            System.out.println(e);
                            System.out.println("Hi ha hagut un error");
                        }
                    }
                    else{
                        try {
                            _partida = new Partida(s);
                            crearEscenaPartida();
                            window.setScene(escenaPartida);
                        } catch (IOException e) {
                            missatge.setText("No s'ha trobat el fitxer de la partida, entra'n un altre de correcte");
                            System.out.println(e);
                            System.out.println("No s'ha trobat el fitxer de la partida");
                        } catch (Exception e){
                            System.out.println(e);
                            System.out.println("Hi ha hagut un error");
                        }
                    }
                }
            }
        });

        vb.getChildren().addAll(subBtn, missatge);
        vb.setAlignment(Pos.CENTER);
        return vb;
    }
    /** @brief  Crea el títol del top de l'escena secundaria
     * @pre \p s és el text que es mostrarà com a títol, \p d és una variable perquè el label quedi centrat.
     * @return un node on hi haurà el títol que es posarà a l'escena secundaria.
     */
    private static Node topLabel(String s, ReadOnlyDoubleProperty d){
        Label lbl = new Label(s);
        lbl.setAlignment(Pos.BASELINE_CENTER);
        lbl.setPrefHeight(60);
        lbl.prefWidthProperty().bind(d);
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 25px; -fx-font-family: " + "Arial Black" + "; -fx-text-fill: white;");

        return lbl;
    }

    /** @brief  Crea el botó per tirar enrere
     * @pre \p s és el text que hi haurà dins el botó
     * @return un node que serà contindrà un botó que tindrà la funcionalitat de tirar enrere i anar a l'escena principal
     */
    private static Node botoInferior(String s){
        Pane root = new Pane();
        Button btnInf = new Button(s);
        btnInf.setStyle("-fx-background-image: url(" + "/Images/woodTexture.png" + "); -fx-font-weight: bold");
        if(s.equals("Exit")){
            btnInf.setOnAction(e -> Platform.exit());
        }
        else {
            btnInf.setOnAction(e -> window.setScene(escenaCrearCarregarPartida));
        }
        root.setPrefHeight(60);

        btnInf.setLayoutX(30);
        btnInf.setLayoutY(5);
        root.getChildren().add(btnInf);

        return root;
    }

    /** @brief  Crea l'escena partida
     * @pre --
     * @post Escena de la partida creada on hi haurà el taulell i a la dreta una part amb diferents opcions per fer durant la partida
     */
    private static void crearEscenaPartida(){
        BorderPane root = new BorderPane();
        int files = _partida.getFiles();
        int columnes = _partida.getColumnes();
        Rectangle2D r = Screen.getPrimary().getVisualBounds();
        _pixelsRajola = Math.min((int)r.getHeight()/files, (int)r.getWidth()/columnes) - 10;
        root.setCenter(crearContingutPartida(root));
        root.setRight(crearOpcions(root));
        escenaPartida = new Scene(root);
    }

    /** @brief  Crea les opcions que es podran fer durant la partida
     * @pre \p p és el panell de l'escena de la partida, necessari per funcionalitats on es requereix canviar l'escena
     * @return un node que conté un BorderPane on al top hi ha un label per els avisos útils per l'usuari, al centre hi ha
     * les opcions que són diferents botons i al bottom hi ha un botó per sortir de l'aplicació
     */
    private static Node crearOpcions(BorderPane p){
        BorderPane root = new BorderPane();
        root.setPrefWidth(300);

        VBox opcionsGenerals = new VBox(15);
        Button redo = new Button("Refer");
        Button undo = new Button("Desfer");
        Button surrender = new Button("Rendir-se");
        Button postpone = new Button("Ajornar");
        Button tie = new Button("Demanar taules");
        Button exit = new Button("EXIT");
        redo.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-border-style: dotted; -fx-border-insets: 1 1 1 1;");
        undo.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-border-style: dotted; -fx-border-insets: 1 1 1 1;");
        surrender.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-border-style: dotted; -fx-border-insets: 1 1 1 1;");
        postpone.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-border-style: dotted; -fx-border-insets: 1 1 1 1;");
        tie.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-border-style: dotted; -fx-border-insets: 1 1 1 1;");
        exit.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-border-style: dotted; -fx-border-insets: 1 1 1 1; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 1, 1;");

        Label lbl = new Label("Torn de " + _partida.getProperTorn());
        lbl.setStyle("-fx-font-weight: bold;");
        redo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StringBuilder resultat = new StringBuilder();
                TiradaSimple tirada = _partida.referTirada(resultat);
                Iterator<Node> itr = _peces.getChildren().iterator();
                PecaGrafica pGraf = null;
                String[] tokens = resultat.toString().split(" ");
                if(tokens[0].equalsIgnoreCase("enroc:")){ //vol dir que hi ha enroc
                    while(itr.hasNext()){
                        pGraf = (PecaGrafica) itr.next();
                        if(posTauler(pGraf.get_oldX()) == (int)(tokens[2].charAt(0))-1-96 && posTauler(pGraf.get_oldY()) == tokens[2].charAt(1)-1-48){ //resto 1 perquè en lluc no fa servir la posició 0 i jo si i després resto l'altre numero perquè retorna en codi ascii
                            break;
                        }
                    }

                    pGraf.move((int)(tokens[5].charAt(0))-1-96,tokens[5].charAt(1)-1-48);

                    pGraf = null;
                    itr = _peces.getChildren().iterator();
                    while(itr.hasNext()){
                        pGraf = (PecaGrafica) itr.next();
                        if(posTauler(pGraf.get_oldX()) == (int)(tokens[1].charAt(0))-1-96 && posTauler(pGraf.get_oldY()) == tokens[1].charAt(1)-1-48){ //resto 1 perquè en lluc no fa servir la posició 0 i jo si i després resto l'altre numero perquè retorna en codi ascii
                            break;
                        }
                    }
                    pGraf.move((int)(tokens[4].charAt(0))-1-96,tokens[4].charAt(1)-1-48);

                    //en el refer no hi haurà escac i mat.
                    if(tokens.length == 6){
                        modificarLblAvisos("Enroc refet", p);
                    }else{
                        modificarLblAvisos("Enroc refet i escac al rei enemic", p);
                    }
                    _partida.canviarTorn();
                    lbl.setText("Torn de " + _partida.getProperTorn());
                }else if(tokens[0].equalsIgnoreCase("promoció:")){ //ha fet promoció
                    int x = tirada.get_origen().get_columna()-1;
                    int y = tirada.get_origen().get_fila()-1;

                    eliminarPeca(x, y);

                    //ens assegurem que si s'ha eliminat peces, s'eliminin també
                    TreeMap<Posicio, Peca> eliminades = _partida.getTaulell().getEliminats();
                    if(eliminades != null){
                        pGraf = null;
                        Iterator<Map.Entry<Posicio, Peca>> it = eliminades.entrySet().iterator();
                        while(it.hasNext()){
                            Map.Entry<Posicio, Peca> entry = it.next();
                            Posicio pos = entry.getKey();
                            itr = _peces.getChildren().iterator();
                            while(itr.hasNext()){
                                pGraf = (PecaGrafica) itr.next();
                                if(posTauler(pGraf.get_oldX()) == pos.get_columna()-1 && posTauler(pGraf.get_oldY()) == pos.get_fila()-1){ //resto 1 perquè en lluc no fa servir la posició 0 i jo si i després resto l'altre numero perquè retorna en codi ascii
                                    break;
                                }
                            }
                            _peces.getChildren().remove(pGraf);
                        }
                    }
                    Peca f = _partida.getPeca(new Posicio(tirada.get_desti().get_fila(), tirada.get_desti().get_columna()));
                    if(f!=null){
                        pGraf = crearFitxa(f, tirada.get_desti().get_columna()-1, tirada.get_desti().get_fila()-1, p);
                        _peces.getChildren().add(pGraf);
                    }

                    //en el refer no hi haurà escac i mat.
                    if(tokens.length == 6){
                        modificarLblAvisos("Promoció refeta i escac al rei enemic", p);
                    }else{
                        modificarLblAvisos("Promoció refeta", p);
                    }

                    _partida.canviarTorn();
                    lbl.setText("Torn de " + _partida.getProperTorn());

                }else if(tirada != null){ //tirada normal
                    //mirem si hi s'havia eliminat alguna peça a la tirada que volem refer.
                    TreeMap<Posicio, Peca> eliminades = _partida.getTaulell().getEliminats();
                    if(eliminades != null){
                        pGraf = null;
                        Iterator<Map.Entry<Posicio, Peca>> it = eliminades.entrySet().iterator();
                        while(it.hasNext()){
                            Map.Entry<Posicio, Peca> entry = it.next();
                            Posicio pos = entry.getKey();
                            itr = _peces.getChildren().iterator();
                            while(itr.hasNext()){
                                pGraf = (PecaGrafica) itr.next();
                                if(posTauler(pGraf.get_oldX()) == pos.get_columna()-1 && posTauler(pGraf.get_oldY()) == pos.get_fila()-1){ //resto 1 perquè en lluc no fa servir la posició 0 i jo si i després resto l'altre numero perquè retorna en codi ascii
                                    break;
                                }
                            }
                            _peces.getChildren().remove(pGraf);
                        }
                    }
                    itr = _peces.getChildren().iterator();
                    while(itr.hasNext()){
                        pGraf = (PecaGrafica) itr.next();
                        if(posTauler(pGraf.get_oldX()) == tirada.get_origen().get_columna()-1 && posTauler(pGraf.get_oldY()) == tirada.get_origen().get_fila()-1){ //resto 1 perquè en lluc no fa servir la posició 0 i jo si
                            break;
                        }
                    }
                    if(tokens.length > 1){
                        modificarLblAvisos("Tirada refeta i escac al rei enemic", p);
                    }else{
                        modificarLblAvisos("Tirada refeta", p);
                    }
                    pGraf.move(tirada.get_desti().get_columna()-1, tirada.get_desti().get_fila()-1);
                    _partida.canviarTorn();
                    lbl.setText("Torn de " + _partida.getProperTorn());
                }else{
                    modificarLblAvisos("No hi ha tirades per refer. Fes alguna altre acció", p);
                }
            }
        });
        undo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StringBuilder resultat = new StringBuilder();
                TiradaSimple tirada = _partida.desferTirada(resultat);
                Iterator<Node> itr = _peces.getChildren().iterator();
                PecaGrafica pGraf = null;
                String[] tokens = resultat.toString().split(" ");
                if(tokens[0].equalsIgnoreCase("enroc:")){ //si és 6 vol dir que hi ha hagut enroc
                    while(itr.hasNext()){
                        pGraf = (PecaGrafica) itr.next();
                        if(posTauler(pGraf.get_oldX()) == (int)(tokens[5].charAt(0))-1-96 && posTauler(pGraf.get_oldY()) == tokens[5].charAt(1)-1-48){ //resto 1 perquè en lluc no fa servir la posició 0 i jo si i després resto l'altre numero perquè retorna en codi ascii
                            break;
                        }
                    }
                    pGraf.move((int)(tokens[2].charAt(0))-1-96,tokens[2].charAt(1)-1-48);
                    pGraf = null;
                    itr = _peces.getChildren().iterator();
                    while(itr.hasNext()){
                        pGraf = (PecaGrafica) itr.next();
                        if(posTauler(pGraf.get_oldX()) == (int)(tokens[4].charAt(0))-1-96 && posTauler(pGraf.get_oldY()) == tokens[4].charAt(1)-1-48){ //resto 1 perquè en lluc no fa servir la posició 0 i jo si i després resto l'altre numero perquè retorna en codi ascii
                            break;
                        }
                    }
                    pGraf.move((int)(tokens[1].charAt(0))-1-96,tokens[1].charAt(1)-1-48);
                    tokens = _partida.resultatUltimaTirada().split(" ");
                    if(tokens.length == 0){
                        modificarLblAvisos("Enroc desfet", p);
                    }else{
                        modificarLblAvisos("Enroc desfet i escac al rei enemic", p);
                    }
                    _partida.canviarTorn();
                    lbl.setText("Torn de " + _partida.getProperTorn());
                }else if(tokens[0].equalsIgnoreCase("promoció:")){//ha fet promoció
                    int x = tirada.get_origen().get_columna();
                    int y = tirada.get_origen().get_fila();
                    eliminarPeca(x-1, y-1);
                    Peca f = _partida.getPeca(new Posicio(tirada.get_desti().get_fila(), tirada.get_desti().get_columna()));
                    if(f!=null){
                        pGraf = crearFitxa(f, x-1, y-1, p);
                        _peces.getChildren().add(pGraf);
                    }
                    pGraf.move(tirada.get_desti().get_columna()-1, tirada.get_desti().get_fila()-1);
                    TreeMap<Posicio, Peca> eliminades = _partida.getTaulell().getEliminats();
                    if(eliminades != null){
                        Iterator<Map.Entry<Posicio, Peca>> it = eliminades.entrySet().iterator();
                        while(it.hasNext()){
                            Map.Entry<Posicio, Peca> entry = it.next();
                            Posicio pos = entry.getKey();
                            Peca peca = entry.getValue();
                            if(peca!=null){
                                PecaGrafica pGrafi = crearFitxa(peca, pos.get_columna()-1, pos.get_fila()-1, p);
                                _peces.getChildren().add(pGrafi);
                            }
                        }
                    }
                    tokens = _partida.resultatUltimaTirada().split(" ");
                    if(tokens.length == 6){
                        modificarLblAvisos("Promoció desfeta i escac al rei enemic", p);
                    }else{
                        modificarLblAvisos("Promoció desfeta", p);
                    }
                    _partida.canviarTorn();
                    lbl.setText("Torn de " + _partida.getProperTorn());
                }
                else if(tirada != null){ //no hi ha hagut enrroc ni promoció: tirada normal
                    while(itr.hasNext()){
                        pGraf = (PecaGrafica) itr.next();
                        if(posTauler(pGraf.get_oldX()) == tirada.get_origen().get_columna()-1 && posTauler(pGraf.get_oldY()) == tirada.get_origen().get_fila()-1){ //resto 1 perquè en lluc no fa servir la posició 0 i jo si
                            break;
                        }
                    }
                    pGraf.move(tirada.get_desti().get_columna()-1, tirada.get_desti().get_fila()-1);

                    TreeMap<Posicio, Peca> eliminades = _partida.getTaulell().getEliminats();
                    if(eliminades != null){
                        Iterator<Map.Entry<Posicio, Peca>> it = eliminades.entrySet().iterator();
                        while(it.hasNext()){
                            Map.Entry<Posicio, Peca> entry = it.next();
                            Posicio pos = entry.getKey();
                            Peca peca = entry.getValue();
                            if(peca!=null){
                                PecaGrafica pGrafi = crearFitxa(peca, pos.get_columna()-1, pos.get_fila()-1, p);
                                _peces.getChildren().add(pGrafi);
                            }
                        }
                    }
                    if(tokens.length > 1){
                        modificarLblAvisos("Tirada desfeta i escac al rei enemic", p);
                    }else{
                        modificarLblAvisos("Tirada desfeta", p);
                    }
                    _partida.canviarTorn();
                    lbl.setText("Torn de " + _partida.getProperTorn());
                }else{
                    modificarLblAvisos("No hi ha tirades per desfer. Fes alguna altre acció", p);
                }


            }
        });
        surrender.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _partida.rendirse();
                VBox root = new VBox(30);
                root.setStyle("-fx-background-image: url(" + "/Images/darkWoodTexture.png" + "); -fx-background-size: stretch; -fx-padding: 50 0 0 0;");
                Label accept = new Label("El jugador " + _partida.getProperTorn() + " s'ha rendit.");
                accept.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18;");
                Label gg = new Label("Bona partida, fins la pròxima");
                gg.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18;");
                Label avisSortir = new Label("(Clica la imatge per acabar)");
                avisSortir.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
                ImageView i = new ImageView("/Images/gifNen.gif");
                i.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Platform.exit();
                    }
                });
                root.getChildren().addAll(accept, gg, avisSortir, i);
                root.setPadding(new Insets(50));
                root.setAlignment(Pos.CENTER);
                root.setPadding(new Insets(20));
                Scene s = new Scene(root, 500d, 500d);
                window.setScene(s);
            }
        });
        postpone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _partida.ajornar();
                VBox root = new VBox(30);
                root.setStyle("-fx-background-image: url(" + "/Images/darkWoodTexture.png" + "); -fx-backbround-size: stretch; -fx-padding: 50 0 0 0;");
                Label accept = new Label("El jugador " + _partida.getProperTorn() + " ha ajornat la partida.");
                accept.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18;");
                Label gg = new Label("Fins la pròxima!!");
                gg.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18;");
                Label avisSortir = new Label("(Clica la imatge per acabar)");
                avisSortir.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
                ImageView i = new ImageView("/Images/ajornarGif.gif");
                i.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Platform.exit();
                    }
                });
                root.getChildren().addAll(accept, gg, avisSortir, i);
                root.setPadding(new Insets(50));
                root.setAlignment(Pos.CENTER);
                root.setPadding(new Insets(20));
                Scene s = new Scene(root, 500d, 500d);
                window.setScene(s);
            }
        });
        tie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                p.setRight(crearPaneTaules(p));
            }
        });
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                VBox root = new VBox(30);
                root.setStyle("-fx-background-image: url(" + "/Images/darkWoodTexture.png" + "); -fx-backbround-size: stretch; -fx-padding: 50 0 0 0;");
                Label accept = new Label("No treguis el joc així...");
                accept.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18;");
                Label gg = new Label("Que la partida no es guarda...");
                gg.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18;");
                Label avisSortir = new Label("(Clica la imatge per acabar)");
                avisSortir.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
                ImageView i = new ImageView("/Images/rageQuit.gif");
                i.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Platform.exit();
                    }
                });
                root.getChildren().addAll(accept, gg, avisSortir, i);
                root.setPadding(new Insets(50));
                root.setAlignment(Pos.CENTER);
                root.setPadding(new Insets(20));
                Scene s = new Scene(root, 500d, 500d);
                window.setScene(s);
            }
        });

        HBox avisos = new HBox(15);
        Label av = new Label("No hi ha cap avís");
        av.setWrapText(true);
        av.setAlignment(Pos.CENTER);
        av.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
        avisos.getChildren().add(av);
        avisos.setAlignment(Pos.CENTER);
        avisos.setStyle("-fx-border-style: solid; -fx-border-width: 3 3 3 3; -fx-background-image: url(" +"/Images/woodTexture.png" +"); -fx-background-size: stretch;");
        avisos.setPrefHeight(200);
        avisos.setPrefHeight(280);
        avisos.setPadding(new Insets(10));

        HBox botoMarxar = new HBox(15);
        botoMarxar.getChildren().add(exit);
        botoMarxar.setAlignment(Pos.BOTTOM_CENTER);

        opcionsGenerals.setAlignment(Pos.CENTER);
        opcionsGenerals.getChildren().addAll(lbl, undo, redo, surrender, postpone, tie, botoMarxar);
        opcionsGenerals.setStyle("-fx-border-style: solid; -fx-border-width: 3 3 3 3; -fx-background-image: url(" +"/Images/woodTexture.png" +"); -fx-background-size: stretch;");
        opcionsGenerals.setPadding(new Insets(20));

        BorderPane.setMargin(avisos, new Insets(10));
        BorderPane.setMargin(opcionsGenerals, new Insets(10));

        root.setStyle("-fx-background-image: url(" + "/Images/darkWoodTexture.png" +"); -fx-background-size: stretch;");
        root.setPadding(new Insets(20));
        root.setTop(avisos);
        root.setCenter(opcionsGenerals);
        root.setBottom(botoMarxar);
        return root;
    }

    /** @brief  Crea un panell per acceptar/declinar les taules un cop s'han demanat
     * @pre \p p és el panell de l'escena de la partida, necessari per funcionalitats on es requereix canviar l'escena
     * @return un node que conté un text on indica a l'usuari quin dels dos ha demanat les taules i dos botons, un per
     * acceptar les taules i l'altre per declinar-les
     */
    private static Node crearPaneTaules(BorderPane p){
        VBox root = new VBox(10);
        root.setStyle("-fx-background-image: url("+ "/Images/darkWoodTexture.png" + ");-fx-background-size: stretch; -fx-padding: 50 0 0 0;");
        root.setId("2");
        Label lbl = new Label("El jugador " + _partida.getProperTorn() + " ha demanat taules");
        lbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
        Button si = new Button("Si");
        si.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-border-style: dotted; -fx-border-insets: 1 1 1 1; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 1, 1;");
        Button no = new Button("No");
        no.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-border-style: dotted; -fx-border-insets: 1 1 1 1; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 1, 1;");
        si.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _partida.taules();
                VBox root = new VBox(30);
                root.setStyle("-fx-background-image: url(" + "/Images/darkWoodTexture.png" + "); -fx-backbround-size: stretch;");
                Label accept = new Label("El jugador " + _partida.getProperTorn() + " ha acceptat les taules");
                accept.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18;");
                accept.setWrapText(true);
                Label gg = new Label("Fins la pròxima!");
                gg.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18;");
                Label avisSortir = new Label("(Clica la imatge per acabar)");
                avisSortir.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
                ImageView i = new ImageView("/Images/donarMa.gif");
                i.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Platform.exit();
                    }
                });
                root.getChildren().addAll(accept, gg, avisSortir, i);
                root.setAlignment(Pos.CENTER);
                Scene s = new Scene(root, 500d, 500d);
                window.setScene(s);
            }
        });
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                p.setRight(crearOpcions(p)); //com que hi ha poca informació i el java disposa de garbage collector,podem anar creant aquesta part i el garbage collector s'encarregarà d'eliminar l'anterior
            }
        });
        lbl.setAlignment(Pos.CENTER);
        si.setAlignment(Pos.CENTER);
        no.setAlignment(Pos.CENTER);

        root.setPadding(new Insets(20));
        root.getChildren().addAll(lbl, si, no);
        root.setAlignment(Pos.CENTER);
        return root;
    }

    /** @brief  Crea el taulell i posiciona les peces
     * @pre \p p és el panell de l'escena de la partida, necessari per funcionalitats on es requereix canviar l'escena
     * @return un node que conté un Pane on hi ha el taulell muntat i les peces posades a la seva posició corresponent
     */
    private static Node crearContingutPartida(BorderPane p){
        Image img1;
        img1 = new Image("/Images/fons1_2.png");
        Image img2;
        img2 = new Image("/Images/fons2_2.png");

        Pane root = new Pane();
        root.setPrefSize(_partida.getColumnes() * _pixelsRajola, _partida.getFiles() * _pixelsRajola);
        root.getChildren().addAll(_rajoles, _peces);
        for(int i = 0; i < _partida.getFiles(); ++i){
            for(int j = 0; j < _partida.getColumnes(); ++j){
                Rajola rajola;
                if((i+j)%2==0){
                    rajola = new Rajola(img2, _pixelsRajola);
                }
                else{
                    rajola = new Rajola(img1, _pixelsRajola);
                }
                rajola.setX(j * _pixelsRajola);
                rajola.setY(i * _pixelsRajola);
                _rajoles.getChildren().add(rajola);
                Peca f = _partida.getPeca(new Posicio(i+1, j+1));
                if(f!=null){
                    PecaGrafica pGraf = crearFitxa(f, j, i, p);
                    _peces.getChildren().add(pGraf);
                }
            }
        }
        return root;
    }

    /** @brief  Crea la fitxa gràfica que anirà al taulell gràfic
     * @pre \p f és la peça lògica que es vol representar gràficament, \p i i \p j són la columna i fila, respectivament
     * on s'ha de posicionar la peça i \p p és el panell de l'escena de la partida, necessari per funcionalitats on es requereix canviar l'escena
     * @return un node que conté un BorderPane on al top hi ha un label per els avisos útils per l'usuari, al centre hi ha
     * les opcions que són diferents botons i al bottom hi ha un botó per sortir de l'aplicació
     */
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
                String res = null;
                StringBuilder s = new StringBuilder();
                StringBuilder posFinal = new StringBuilder();
                //montem la posició final
                posFinal.append((char)(97+newX)).append(newY+1);
                //comprovem que la posició final sigui correcte
                res = _partida.posCorrecteDesti(posFinal.toString());
                if(res.equalsIgnoreCase("enroc")){
                    //montem el moviment com a enroc
                    mov = s.append((char)(97+oldX)).append(oldY+1).append(" - ").append((char)(97 + newX)).append(newY+1).toString();
                }else{
                    //montem el moviment normal
                    mov = s.append((char)(97+oldX)).append(oldY+1).append(' ').append((char)(97 + newX)).append(newY+1).toString();
                }
                realitzarTirada(mov, newX, newY, p, pane, oldX);
            }
        });

        return p;
    }

    /** @brief  Realitza una tirada
     * @pre \p mov és una string on hi ha el moviment que es vol fer, \p newX i newY són les noves posicions gràfiques
     * on anirà la peça gràfica en cas de que la tirada s'hagi realitzat correctament, \p p és la peça gràfica amb la
     * que es vol realitzar la tirada, \p p és el panell de l'escena de la partida, necessari per funcionalitats on es
     * requereix canviar l'escena i \p oldX és la columna on estava la peça gràfica, necessari en el cas de que es
     * fagi enroc per trobar on moure les peces de l'enroc.
     * @post S'ha intentat fer una tirada lògica i es processa el resultat que retorna:
     * -si retorna tiradaV es mou \p p a \p newX i \p newY i es canvia de torn
     * -si retorna tiradaMort s'eliminen les peces mortes i es mou \p p a \p newX i \p newY
     * -si retorna noTirada tornem \p p a la posició inicial ja que no s'ha pogur realitzar la tirada
     * -si retorna promocio es mou \p p a \p newX \p newY i es demana per quina peça es vol promocionar
     * -si retorna enrocFet es busca l'altre peça participant a l'enroc i es mouen a les posicions que toquen i es passa
     * de torn
     * -si retorna escac si fa falta eliminem peçes que hagin mort en aquesta tirada, movem \p p a \p newX i \p newY
     * i passem de torn
     * -si retorna EscacSeguits s'acaba la partida per taules per masses torns seguits amb escac al rei d'un equip
     * -si retorna TornsInacció s'acaba la partida per taules per masses torns seguits sense cap peça morta
     * -si retorna noTiradaEscac tornem \p p a la posició inicial perquè no s'ha pogut fer la tirada
     * -si retorna escacmat s'acaba la partida per escac i mat i es canvia per una escena final
     * -si retorna enrocNo tornem \p p a la posició inicial perquè no s'ha pogut realitzar l'enroc demanat.
     */
    private static void realitzarTirada(String mov, int newX, int newY, PecaGrafica p, BorderPane pane, int oldX){
        String res;
        res = _partida.ferTirada(mov);
        //es processa el que ha retornat partida.
        if(res.equalsIgnoreCase("tiradaMort")){ //ha matat la peça que hi havia anteriorment a aquesta posició
            res = "Tirada vàlida i s'ha matat la peça/les peces corresponent/s";
            eliminarPeces(pane);
            p.move(newX, newY);
            passarTorn(pane);
        }else if(res.equalsIgnoreCase("tiradaV")){ //la tirada s'ha fet a nivell lògic, ara a gràfic
            p.move(newX, newY);
            res = "Tirada vàlida i feta";
            passarTorn(pane);
        }else if(res.equalsIgnoreCase("noTirada")){
            res = "Tirada invàlida, no s'ha realitzat.";
            p.abortMove();
        }else if(res.equalsIgnoreCase("promocio")){
            eliminarPeces(pane);
            p.move(newX, newY);
            crearEscenaPromoció(pane, mov, newX, newY); //si no funciona d'aquesta manera, dins de l'eventHandler dels botons faig que es canviï la peça ia i aqui, despres de cridar aquest mètode, busco la peça que hi ha a la posició i li envio al ferpromocio.
            res = "Escena promoció creada";
        }else if(res.equalsIgnoreCase("enrocFet")) {
            res = "S'ha fet l'enroc";
            Iterator<Node> itr = _peces.getChildren().iterator();
            PecaGrafica pGraf = null;
            while (itr.hasNext()) {
                pGraf = (PecaGrafica) itr.next();
                if (posTauler(pGraf.get_oldX()) == newX && posTauler(pGraf.get_oldY()) == newY) {
                    break;
                }
            }
            if (oldX > newX) {
                p.move(oldX - 2, newY);
                pGraf.move(oldX - 1, newY);
            } else {
                p.move(oldX + 2, newY);
                pGraf.move(oldX + 1, newY);
            }
            passarTorn(pane);
        }else if(res.equalsIgnoreCase("escac")){
            eliminarPeces(pane);
            p.move(newX, newY);
            passarTorn(pane);
        }else if(res.equalsIgnoreCase("EscacsSeguits")){
            res = "Masses escacs seguits, s'acaba la partida en taules";
            _partida.taulesEscacsSeguits();
        }else if(res.equalsIgnoreCase("TornsInaniccio")){
            res = "Masses torns sense acció, s'acaba la partida en taules";
            _partida.taulesTornsInaccio();
        }else if(res.equalsIgnoreCase("noTiradaEscac")){
            res = "No s'ha fet la tirada perquè el teu rei està amenaçat!";
            p.abortMove();
        }
        else if(res.equalsIgnoreCase("escacmat")){
            p.move(newX, newY);
            res = "Escac i mat. S'acaba la partida. Ha guanyat el jugador " + _partida.getProperTorn();
            _partida.escacIMat();
            crearEscenaFinal();

        }else if(res.equalsIgnoreCase("enrocNo")){
            p.abortMove();
            res = "Aquestes peces no poden fer enroc";
        }
        else{
            p.abortMove();
        }
        modificarLblAvisos(res, pane);
    }

    /** @brief  Crea l'escena final de la partida quan un jugador guanya.
     * @pre --
     * @post Es crea l'escena amb un label indicant el jugador que ha guanyat, i un gif.
     */
    private static void crearEscenaFinal(){
        VBox root = new VBox(30);
        root.setStyle("-fx-background-image: url(" + "/Images/darkWoodTexture.png" + "); -fx-backbround-size: stretch;");

        Label accept = new Label("El jugador " + _partida.getProperTorn() + " ha GUANYAT!");
        accept.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18;");
        accept.setWrapText(true);

        Label gg = new Label("Fins la pròxima!");
        gg.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18;");

        Label avisSortir = new Label("(Clica la imatge per acabar)");
        avisSortir.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");

        ImageView i = new ImageView("/Images/victoryGif.gif");
        i.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Platform.exit();
            }
        });
        root.getChildren().addAll(accept, gg, avisSortir, i);
        root.setAlignment(Pos.CENTER);
        Scene s = new Scene(root, 500d, 500d);
        window.setScene(s);
    }

    /** @brief  S'eliminen les peces que han mort en la tirada
     * @pre \p pane és el panell de l'escena de la partida, necessari per funcionalitats on es requereix canviar l'escena
     * @post Es demanen les peces eliminades en la tirada que s'ha realitzat i si n'hi ha, s'eliminen del taulell gràfic
     */
    private static void eliminarPeces(BorderPane pane) {
        TreeMap<Posicio, Peca> eliminades = _partida.getTaulell().getEliminats();
        if(eliminades != null){
            Iterator<Node> itr;
            PecaGrafica pGraf = null;
            Iterator<Map.Entry<Posicio, Peca>> it = eliminades.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<Posicio, Peca> entry = it.next();
                Posicio pos = entry.getKey();
                itr = _peces.getChildren().iterator();
                while(itr.hasNext()){
                    pGraf = (PecaGrafica) itr.next();
                    if(posTauler(pGraf.get_oldX()) == pos.get_columna()-1 && posTauler(pGraf.get_oldY()) == pos.get_fila()-1){ //resto 1 perquè en lluc no fa servir la posició 0 i jo si i després resto l'altre numero perquè retorna en codi ascii
                        break;
                    }
                }
                _peces.getChildren().remove(pGraf);
            }
        }else{
            modificarLblAvisos("No hi ha peces per eliminar", pane);
        }
    }

    /** @brief  Crea panell per fer la promoció
     * @pre \p p és el panell de l'escena de la partida, necessari per funcionalitats on es requereix canviar l'escena,
     * \p mov és el moviment que s'ha fet, necessàri per cridar el fer promoció lògic, \p x i \p y són les coordenades
     * de la posició destí desgloçades
     * @post Es crea un panell on hi ha un label preguntant per quina peça es vol canviar i un botó per cada peça per
     * la que es pugui fer la promoció
     */
    private static void crearEscenaPromoció(BorderPane p, String mov, int x, int y){
        VBox root = new VBox(10);
        root.setPrefWidth(300);

        Label lbl = new Label("Per quina peça vols fer la promoció. Si no ho fas ara, no ho podras fer");
        lbl.setWrapText(true);

        root.getChildren().add(lbl);
        String[] llsitaPeces = _partida.getLlistaPeces();
        for(String s : llsitaPeces){
            if(!s.equalsIgnoreCase("rei")){
                Button btn = new Button(s);
                btn.setStyle("-fx-background-image: url(" + "/Images/woodTexture.png" + "); -fx-font-weight: bold");
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String resProm = _partida.ferPromocio(mov,s);
                        eliminarPeca(x, y);
                        Peca f = _partida.getPeca(new Posicio(y+1, x+1));
                        if(f!=null){
                            PecaGrafica novaPeca = crearFitxa(f, x, y, p);
                            _peces.getChildren().add(novaPeca);
                        }

                        p.setRight(crearOpcions(p));

                        if(resProm.equalsIgnoreCase("escacmat")){
                            crearEscenaFinal();
                        }else if(resProm.contains("Escac")){
                            modificarLblAvisos("Promoció feta i escac al rei enemic!", p);
                        }
                        else{
                            modificarLblAvisos("Promoció feta", p);
                        }
                        passarTorn(p);
                    }
                });
                btn.setAlignment(Pos.CENTER);
                root.getChildren().add(btn);
            }

        }
        lbl.setAlignment(Pos.CENTER);
        lbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        p.setRight(root);
        p.setStyle("-fx-background-image: url(" + "/Images/darkWoodTexture.png" +"); -fx-background-size: stretch;");
    }

    /** @brief  Modifica els avisos per l'usuari
     * @pre \p s és l'avís que volem que es mostri per l'usuari i \p p és el panell de l'escena de la partida, necessari
     * per funcionalitats on es requereix canviar l'escena
     * @post Label d'avisos modificat amb el nou avís siguent \s
     */
    private static void modificarLblAvisos(String s, BorderPane p){
        if(!s.equalsIgnoreCase("Escena promoció creada")){
            BorderPane dreta = (BorderPane) p.getRight();
            HBox top = (HBox) dreta.getTop();
            Label txt = (Label) top.getChildren().get(0);
            txt.setText(s);
        }
    }

    /** @brief  Es passa de torn
     * @pre \p p és el panell de l'escena de la partida, necessari per trobar el panell d'opcions i poder indicar el
     * canvi de torn graficament
     * @return un node que conté un BorderPane on al top hi ha un label per els avisos útils per l'usuari, al centre hi ha
     * les opcions que són diferents botons i al bottom hi ha un botó per sortir de l'aplicació
     */
    private static void passarTorn(BorderPane p){
        _partida.canviarTorn();
        BorderPane dreta = (BorderPane) p.getRight();
        VBox opcions = (VBox) dreta.getCenter();
        Label lbl = (Label) opcions.getChildren().get(0);
        lbl.setText("Torn de " + _partida.getProperTorn());
    }

    /** @brief  S'elimina una peça gràfica del taulell
     * @pre \p x i \p y són les coordenades de la posició de la peça que s'ha d'eliminar
     * @post La peça que es trobava a la posició amb coordenades \p x i \p y s'ha eliminat del group _fitxes i del
     * taulell gràfic
     */
    private static void eliminarPeca(int x, int y){
        PecaGrafica pg = null;
        for(Node n : _peces.getChildren()){
            pg = (PecaGrafica)n;
            if(posTauler(pg.get_oldX()) == x && posTauler(pg.get_oldY()) == y){
                break;
            }
        }
        pg.setVisible(false);
        _peces.getChildren().remove(pg);
    }

    /** @brief  Calcula la posició al taulell gràfic
     * @pre \p p són pixels
     * @return un enter que és la posició del taulell gràfic dels píxels \p p
     */
    private static int posTauler(double p){
        return (int)(p+_pixelsRajola/2)/_pixelsRajola;
    }

}
