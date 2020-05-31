package partida;

import java.io.IOException;
import java.util.Scanner;

public abstract class PartidaText {

    private static Partida _partida;

    public static void iniciaAplicacio() throws IOException {
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
            System.out.println("Entra el fitxer de regles: (nomFitxer.json)");
            String nomFitxer = teclat.nextLine();
            int nJug;
            System.out.println("Entre el nombre de jugadors reals (0, 1, 2)");
            nJug = teclat.nextInt();
            _partida = new Partida(nomFitxer, nJug);
        }
        else{
            System.out.println("Entra el fitxer de la partida que vols carregar:");
            String nomFitxer = teclat.nextLine();
            _partida = new Partida(nomFitxer); //un metode que carrega el fitxer.
        }
        jugar();
    }

    private static void jugar(){
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

    private static boolean tirada(StringBuilder res, StringBuilder posInici, StringBuilder posFinal, StringBuilder colorTorn) {
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
                    res.append(_partida.ferTirada(posInici.toString() + tokens[1]));
                    posFinal.append(tokens[1]);
                }else if(tokens[0].equalsIgnoreCase("enroc")){
                    System.out.println("Segur que vols fer aquest enroc?");
                    String opcio = teclat.nextLine();
                    if(opcio.equalsIgnoreCase("si")){
                        res.append(_partida.ferTirada(posInici.toString() + "- " + tokens[1]));
                        posFinal.append(tokens[1]);
                    }else if(opcio.equalsIgnoreCase("no")){
                        res.append("noEnroc");
                    }
                }else{
                    System.out.println("Fail");
                    System.exit(-1);
                }
            }else{//si entra aqui sabem segur que ha retornat "no", per tant, que no vol moure la peça que ha dit
                System.out.println("Pots tornar a entrar la posició Inicial");
            }
            if(res.toString().equalsIgnoreCase("EscacsSeguits") || res.toString().equalsIgnoreCase("TornsInaniccio")){
                continuar = false;
            }
        }
        return continuar;
    }

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

    private static void menuPromocio(){
        String[] llsitaPeces = _partida.getLlistaPeces();
        System.out.println("Pots fer promoció d'aquestes peces");
        for(int i = 0; i<llsitaPeces.length; i++){
            if(!llsitaPeces[i].equalsIgnoreCase("rei")){
                System.out.println(llsitaPeces[i]);
            }
        }
    }

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


    private static boolean processarRes(StringBuilder res, StringBuilder colorTorn){
        if(res.toString().equals("tiradaV")){ //S'ha realitzat la tirada i no ha passat res més
            System.out.println("S'ha realitzat la tirada correctament");
            return true;
        }else if(res.toString().equalsIgnoreCase("tiradaMort")){ //S'ha realitzat una tirada i s'ha matat mínim una peça
            System.out.println("S'ha realitzat la tirada i s'ha matat una peça");
            return true;
        } else if(res.toString().equals("taules")){ //L'usuari ha demanat taules
            System.out.println("El jugador amb les peces " + colorTorn.toString() + " demana taules");
            return true;
        }else if(res.toString().equals("escac")){ //L'usuari ha fet la tirada, i amb aquesta, amenaça el rei enemic
            System.out.println("Escac al rei ");
            if(colorTorn.toString().equals("BLANQUES")){
                System.out.println("NEGRE");
            }else{
                System.out.println("BLANC");
            }
        }else if(res.toString().equals("noTirada")){ //No s'ha pogut fer la tirada que diu el jugador
            System.out.println("No s'ha realitzat la tirada");
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
                System.out.println("Escac al rei ");
                if(colorTorn.toString().equals("BLANQUES")){
                    System.out.println("NEGRE");
                }else{
                    System.out.println("BLANC");
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
        }else if(res.toString().equalsIgnoreCase("enrocFet")) { //s'ha fet l'enroc
            System.out.println("S'ha fet l'enroc");
            return true;
        }else if(res.toString().equalsIgnoreCase("EscacsSeguits")){ //masses escacs seguits, s'acaba la partida
            //_partida.taulesEscacsSeguits();
            System.out.println("S'ha superat el nombre de torns que restulta amb 'Escac'. S'acaba la partida");

        }else if(res.toString().equalsIgnoreCase("TornsInaniccio")){ //masses torns sense que passi res, s'acaba la partida
            //_partida.taulesTornsInaccio();
            System.out.println("S'ha superat el nombre de torns sense matar cap peça. S'acaba la partida");
        }
        return false;
    }

    private static String llegirPosicioInici(){
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

    private static String llegirPosicioDesti(){
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

    private static boolean posicioCorrecteOrigen(String s){
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
        }
        return correcte;
    }

    private static String posicioCorrecteDesti(String s){
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
            //System.out.println(correcte);
        }
        return correcte;
    }
}
