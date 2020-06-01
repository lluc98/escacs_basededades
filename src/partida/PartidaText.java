package partida;
/** @file PartidaText.java
 * @brief Partida d'escacs en mode Text.
 */

import java.nio.file.NoSuchFileException;
import java.util.Scanner;

/** @class PartidaText
 * @brief Versió d'un joc d'escacs amb peces personalitzables i
 * taulells de mida variable, en mode text.
 */
public abstract class PartidaText {

    private static Partida _partida;                ///< Objecte partida que controla la situacó de la partida

    /** @brief  Inicia el joc
     * @pre --
     * @post Iniciador del joc, intordueixes les dades basiques i aquest crida els metodes necessaris.
     */
    public static void iniciaAplicacio() throws Exception {
        Scanner teclat = new Scanner(System.in);
        System.out.println("Benvingut a l'aplicació!!");
        System.out.println("Vols començar una partida o carregar-ne una de començada? (Començar/Carregar)");
        String opcio = null;
        while(opcio == null){
            opcio = teclat.nextLine();
            if (!opcio.equalsIgnoreCase("Començar") && !opcio.equalsIgnoreCase("Carregar")) {
                System.out.println("Opcio incorrecte, introdueix-ne una correcte (Començar/Carregar)");
                opcio = null;
            }
        }
        if(opcio.equalsIgnoreCase("Començar")){
            int nJug = -1;
            while(nJug < 0 || nJug > 2){
                nJug = demanarNjugadors();
            }
            System.out.println("Entra el fitxer de regles: (nomFitxer.json)");
            while(true){
                try{
                    String nomFitxer = teclat.nextLine();
                    _partida = new Partida(nomFitxer, nJug);
                    break;
                }catch(NoSuchFileException e){
                    System.out.println(e);
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
        else{
            System.out.println("Entra el fitxer de la partida que vols carregar:");
            String nomFitxer;
            while(true){
                try{
                    nomFitxer = teclat.nextLine();
                    _partida = new Partida(nomFitxer); //un metode que carrega el fitxer.
                    break;
                }catch(NoSuchFileException e){
                    System.out.println(e);
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
        jugar();
    }

    /** @brief  Crea un ombre de jugadors
     * @pre --
     * @post retorna el nombre de jugadors que entra l'usuari
     */
    public static int demanarNjugadors(){
        Scanner teclat = new Scanner(System.in);
        String nombre = "";
        while(!esEnter(nombre)){
            System.out.println("Entre el nombre de jugadors reals (0, 1, 2)");
            nombre = teclat.nextLine();
        }
        return Integer.parseInt(nombre);
    }

    /** @pre --
     * @post True si s és un enter, false altrament.
     */
    public static boolean esEnter(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    /** @brief  Gestiona la partida
     * @pre --
     * @post S'ha jugat una partida
     */
    private static void jugar() throws Exception {
        boolean continuar = true;
        StringBuilder colorTorn = new StringBuilder(_partida.getProperTorn());
        StringBuilder posInici = null;
        StringBuilder posFinal = null;
        StringBuilder res = new StringBuilder();
        System.out.println("Comencen les " + colorTorn);
        do{
            _partida.mostrarTaulell();
            if(res.toString().equals("taules")){
                continuar = taules();
                res = new StringBuilder(); //entrarà al processarRes però no farà cap acció
            }else if(res.toString().equalsIgnoreCase("promocio")){
                res = new StringBuilder(promocio(posInici, posFinal));
            }else{
                posInici = new StringBuilder(); //fem això perquè a l'hora de fer append no es borra el que hi havia i si fem new dins del mètode, no es retorna el que se li ha posat
                posFinal = new StringBuilder();
                res = new StringBuilder();
                continuar = tirada(res, posInici, posFinal, colorTorn);
            }
            if(processarRes(res, colorTorn)){
                colorTorn = new StringBuilder(_partida.canviarTorn());
            }

        }while(continuar);
    }

    /** @brief  Gestiona la tirada
     * @param res Resultat de la tirada
     * @param posInici Posició inicial de la tirada
     * @param posFinal Posició final de la tirada
     * @param colorTorn color del equip que fa la tirada
     * @pre --
     * @post retorna true si s'ha realitzat la tirada, false altrament
     */
    private static boolean tirada(StringBuilder res, StringBuilder posInici, StringBuilder posFinal, StringBuilder colorTorn) throws Exception {
        Scanner teclat = new Scanner(System.in);
        boolean continuar = true;
        System.out.println("Torn del jugador de peces " + colorTorn.toString());
        System.out.println("Entra el que vols fer. (Rendirse/Taules/Ajornar/Desfer/Refer/(o escrius la posició de la peça que vols moure");
        String s = llegirPosicioInici();
        posInici.append(s);
        if(s.equalsIgnoreCase("Rendirse")){
            continuar = false;
            res.append("rendirse"); //ens podem permetre anar fent "new" ja que gràcies al garbage collector, no ocuparem memòria extra pels new, la memòria que estava ocupada anteriorment s'alliberarà
        }else if(s.equalsIgnoreCase("Taules")){
            res.append("taules");
        }else if(s.equalsIgnoreCase("Ajornar")){
            continuar = false;
            res.append("ajornar");
        }else if(s.equalsIgnoreCase("desfer")){
            StringBuilder result = new StringBuilder();
            TiradaSimple tirada = _partida.desferTirada(result);
            if(tirada == null){
                res.append("noDesfer");
            }
            else{
                res.append("desfer fet");
            }
        }else if(s.equalsIgnoreCase("refer")){
            StringBuilder result = new StringBuilder();
            TiradaSimple tirada = _partida.referTirada(result);
            if(tirada == null){
                res.append("noRefer");
            }
            else{
                res.append("refer fet");
            }
        }
        else{
            System.out.println("Pots moure aquesta peça, a on la vols moure? O escriu 'no' si prefereixes moure una altre peça");
            posInici.append(" ");
            String segPos;
            segPos = llegirPosicioDesti();
            String[] tokens = segPos.split(" ");
            if(tokens.length > 1){
                if(tokens[0].equalsIgnoreCase("correcte")) {
                    System.out.println(posInici.toString() + tokens[1]);
                    posFinal.append(tokens[1]);
                    res.append(_partida.ferTirada(posInici.toString() + tokens[1]));
                }else if(tokens[0].equalsIgnoreCase("enroc")){
                    posFinal.append(tokens[1]);
                    res.append(_partida.ferTirada(posInici.toString() + "- " + tokens[1]));
                }else{
                    System.out.println("Fail");
                    System.exit(-1);
                }
            }
            String[] tokensRes = res.toString().split(" ");
            if(res.toString().equalsIgnoreCase("EscacsSeguits") || res.toString().equalsIgnoreCase("TornsInaniccio") || res.toString().equalsIgnoreCase("escacmat")){
                continuar = false;
            }
            if(tokensRes.length>1){
                if(tokensRes[1].equalsIgnoreCase("escacmat")){
                    continuar = false;
                }
            }
        }
        return continuar;
    }

    /** @brief  Gestiona una promocio
     * @param posInici Posició inicial de la tirada
     * @param posFinal Posició final de la tirada
     * @pre --
     * @post retorna un missatge donant informació del resultat de la promoció
     */
    private static String promocio(StringBuilder posInici, StringBuilder posFinal){
        Scanner teclat = new Scanner(System.in);
        String s = teclat.nextLine();
        String res = "";
        if(s.equalsIgnoreCase("si")){
            System.out.println("Per quina peça vols fer");
            menuPromocio();
            String peca;
            do{
                peca = teclat.nextLine();
                res = _partida.ferPromocio(posInici.toString()+posFinal.toString(), peca);
                if(res.equalsIgnoreCase("noPromValid")){
                    System.out.println("Peça no vàlida, entra'n una de vàlida");
                }
            }while(!res.contains("siProm"));
            String[] tokens = res.split(" ");
            if(tokens.length > 1){
                res = tokens[1];
            }
            res = res + " " + "siProm";
        }else{
            res = res + "noProm";
        }
        return res;
    }

    /** @pre --
     * @post S'ha triat una Peça per a convertir la Peça promocionada.
     */
    private static void menuPromocio(){
        String[] llsitaPeces = _partida.getLlistaPeces();
        System.out.println("Pots fer promoció d'aquestes peces");
        for(int i = 0; i<llsitaPeces.length; i++){
            if(!llsitaPeces[i].equalsIgnoreCase("rei")){
                System.out.println(llsitaPeces[i]);
            }
        }
    }

    /** @pre --
     * @post S'ha guardat una partida que ha acabat amb taules
     */
    private static boolean taules(){
        Scanner teclat = new Scanner(System.in);
        boolean correcte = false;
        String opcio;
        do{
            System.out.println("Acceptes les taules? (Si/No)");
            opcio = teclat.nextLine();
            if(opcio.equalsIgnoreCase("Si") || opcio.equalsIgnoreCase("No")){ correcte = true; }
        }while(!correcte);
        if(opcio.equals("Si")){
            _partida.taules();
            return false;
        }
        return true;
    }

    /** @brief  Processa un resultat d'una tirada
     * @param res Resultat de la tirada
     * @param colorTorn color del euqip actual
     * @pre --
     * @post retorna true si s'ha realitzat la tirada, false altrament.
     */
    private static boolean processarRes(StringBuilder res, StringBuilder colorTorn) throws Exception {
        if(res.toString().equalsIgnoreCase("tiradaV")){ //S'ha realitzat la tirada i no ha passat res més
            System.out.println("S'ha realitzat la tirada correctament");
            return true;
        }else if(res.toString().equalsIgnoreCase("tiradaMort")){ //S'ha realitzat una tirada i s'ha matat mínim una peça
            System.out.println("S'ha realitzat la tirada i s'ha matat una peça");
            return true;
        } else if(res.toString().equalsIgnoreCase("taules")){ //L'usuari ha demanat taules
            System.out.println("El jugador amb les peces " + colorTorn.toString() + " demana taules");
            return true;
        }else if(res.toString().equalsIgnoreCase("escac")){ //L'usuari ha fet la tirada, i amb aquesta, amenaça el rei enemic
            System.out.println("Escac al rei ");
            if(colorTorn.toString().equals("BLANQUES")){
                System.out.println("NEGRE");
            }else {
                System.out.println("BLANC");
            }
            return true;
        }else if(res.toString().equalsIgnoreCase("noTirada")){ //No s'ha pogut fer la tirada que diu el jugador
            System.out.println("Tirada invàlida, no s'ha realitzat");
        }else if(res.toString().equalsIgnoreCase("noTiradaEscac")){ //s'ha desfet un moviment
            System.out.println("Vigila! No s'ha fet la tirada perquè el teu rei està amenaçat! gi-li-po-lles.");
            System.out.println("Un rei de veritat t'hagues matat ja...");
            System.out.println("El millor atac és una bona defensa! Compte amb el teu rei.");
        }else if(res.toString().equalsIgnoreCase("desfer fet")){ //s'ha desfet un moviment
            System.out.println("S'ha desfer l'ultim moviment");
        }else if(res.toString().equalsIgnoreCase("noDesfer")){ //no queden moviments per desfer
            System.out.println("No queden moviments per desfer");
        }else if(res.toString().equalsIgnoreCase("refer fet")){ //s'ha refet un moviment
            System.out.println("S'ha refet la tirada");
        }else if(res.toString().equalsIgnoreCase("noRefer")){ //no queden moviments per refer
            System.out.println("No queden moviments per refer.");
        }else if(res.toString().equalsIgnoreCase("promocio")){ //L'usuari pot triar si fer o no promoció
            System.out.println("Vols fer promoció per alguna peça?");
            System.out.println("Si dius que no, no tornaràs a tenir la oportunitat");
        }else if(res.toString().equalsIgnoreCase("noProm")){ //L'usuari no ha volgut fer la promoció
            System.out.println("No has volgut fer la promoció, canvi de torn");
            return true;
        }else if(res.toString().contains("siProm")){ //s'ha fet la promoció
            System.out.println("Has fet la promoció!");
            String[] tokens = res.toString().split(" ");
            if(tokens.length > 1){
                if(tokens[1].equalsIgnoreCase("escac")){
                    System.out.print("Escac al rei ");
                    if(colorTorn.toString().equals("BLANQUES")){
                        System.out.println("NEGRE");
                    }else{
                        System.out.println("BLANC");
                    }
                }else{ //escac i mat
                    System.out.print("Hi ha escac i mat a les fitxes ");
                    if(colorTorn.toString().equals("BLANQUES")){
                        System.out.println("NEGRE");
                    }else{
                        System.out.println("BLANC");
                    }
                    _partida.escacIMat();
                }

            }
            return true;
        }else if(res.toString().equalsIgnoreCase("ajornar")){ //l'usuari ajorna la partida
            System.out.println("El jugador amb les peces " + colorTorn.toString() + " ajorna la partida");
            _partida.ajornar();
        }else if(res.toString().equalsIgnoreCase("rendirse")){ //l'usuari es rendeix
            System.out.println("El jugador amb les peces " + colorTorn.toString() + " es rendeix");
            _partida.rendirse();
        }else if(res.toString().equalsIgnoreCase("noEnroc")){ //l'usuari no ha volgut fer l'enroc
            System.out.println("No s'ha volgut fer l'enroc, pots tornar a entrar les posicions.");
        }else if(res.toString().equalsIgnoreCase("enrocNo")){ //l'enroc no s'ha pogut fer
            System.out.println("No s'ha fet l'enroc");
        }else if(res.toString().contains("enrocFet")) { //s'ha fet l'enroc
            System.out.println("S'ha fet l'enroc");
            String[] tokens = res.toString().split(" ");
            if(tokens[1].equalsIgnoreCase("escac")){
                System.out.print("Escac al rei ");
                if(colorTorn.toString().equals("BLANQUES")){
                    System.out.println("NEGRE");
                }else{
                    System.out.println("BLANC");
                }
            }else{ //escac i mat
                System.out.print("Hi ha escac i mat a les fitxes ");
                if(colorTorn.toString().equals("BLANQUES")){
                    System.out.println("NEGRE");
                }else{
                    System.out.println("BLANC");
                }
                _partida.escacIMat();
            }
            return true;
        }else if(res.toString().equalsIgnoreCase("EscacsSeguits")){ //masses escacs seguits, s'acaba la partida
            _partida.taulesEscacsSeguits();
            System.out.println("S'ha superat el nombre de torns que restulta amb 'Escac'. S'acaba la partida");

        }else if(res.toString().equalsIgnoreCase("TornsInaniccio")){ //masses torns sense que passi res, s'acaba la partida
            _partida.taulesTornsInaccio();
            System.out.println("S'ha superat el nombre de torns sense matar cap peça. S'acaba la partida");
        }else if(res.toString().equalsIgnoreCase("escacmat")){ //escac i mat, s'acaba la partida
            System.out.println("Escac i mat!! El jugador " + _partida.getProperTorn() + " ha guanyat la partida");
            _partida.escacIMat();
        }else if(res.toString().equalsIgnoreCase("no")){
            System.out.println("Pots tornar a entrar la posició Inicial");
        }
        else{
            throw new Exception("Ha passat algun error");
        }
        return false;
    }

    /** @brief  Llegeix la posició inicial que demana l'usuari i la processa
     * @pre --
     * @post Retorna un avís amb el resultat de la posició inicial que ha seleccionat l'usuari
     */
    private static String llegirPosicioInici() throws Exception {
        boolean correcte = false;
        String s;
        Scanner teclat = new Scanner(System.in);
        do{
            System.out.println("Entra una posició amb una peça del teu equip");
            s = teclat.nextLine();
            if(s.equalsIgnoreCase("Rendirse") || s.equalsIgnoreCase("Taules") || s.equalsIgnoreCase("Ajornar") || s.equalsIgnoreCase("desfer") || s.equalsIgnoreCase("refer")){
                return s;
            }
            else{
                //si entra aqui vol dir que ha entrat una posicio o aixo creiem
                correcte = posicioCorrecteOrigen(s);
                if(!correcte){
                    System.out.println("Entra una posició correcte");
                }
            }
        } while(!correcte);
        return s;
    }

    /** @brief  Llegeix la posició destí que demana l'usuari i la processa
     * @pre --
     * @post Retorna un avís amb el resultat de la posició destí que ha seleccionat l'usuari
     */
    private static String llegirPosicioDesti() throws Exception {
        String correcte = "";
        String s;
        Scanner teclat = new Scanner(System.in);
        do{
            System.out.println("Si vols fer un enrroc entra la posició on es troba la peça amb la que el vols fer");
            s = teclat.nextLine();
            if(s.contains("no")){ //fem contains per d'aquesta manera l'usuari pot entrar una string amb caràcters random, però si  conté "no", entra aqui
                return "no";
            }else{ //ha entrat una suposada posició
                correcte = posicioCorrecteDesti(s);
                if(correcte.equalsIgnoreCase("no")){
                    System.out.println("Entra una posició correcte");
                }
            }
        } while(correcte.equalsIgnoreCase("no"));
        return correcte;
    }

    /** @brief  Calcula si es correcte la posició d'origen que ha seleccionat l'usuari
     * @param s valor de la posició origen
     * @pre --
     * @post retorna si la posició origen és vàlida, false altrament.
     */
    private static boolean posicioCorrecteOrigen(String s) throws Exception {
        if(s.equals("")) {
            return false;
        }
        boolean correcte = false;
        String res = _partida.posCorrecteOrigen(s);
        if(res.equals("Posició invàlida")) { //posicio sense peça
            System.out.println("Posició invàlida");
        }else if(res.equals("Peça del color incorrecte")){ //la peça no es del teu color
            System.out.println("La peça que hi ha en aquesta posició no és del teu color");
        }
        else if(res.equalsIgnoreCase("tot correcte")){
            correcte = true;
        }else{
            throw new Exception("Error posició correcte origen");
        }
        return correcte;
    }

    /** @brief  Calcula si es correcte la posició destí que ha seleccionat l'usuari
     * @param s valor de la posició destí
     * @pre --
     * @post retorna si la posició destí és vàlida, false altrament.
     */
    private static String posicioCorrecteDesti(String s) throws Exception {
        if(s.equals("")){
            return "no";
        }
        String correcte = "";
        String res = _partida.posCorrecteDesti(s);
        if(res.equalsIgnoreCase("Posició invàlida")) { //posicio sense peça
            System.out.println("La posició que has entrat no té cap peça");
            correcte = "no";
        }else if(res.equalsIgnoreCase("Enroc")){ //la peça no es del teu color
            correcte = "enroc " + s;
        }
        else if(res.equalsIgnoreCase("tot correcte")){
            System.out.println("Posició correcte");
            correcte = "correcte " + s;
        }else{
            throw new Exception("Error Posició correcte destí");
        }
        return correcte;
    }
}
