package partida;

import java.util.Scanner;

public abstract class PartidaText {

    private static Partida _partida;

    public static void iniciaAplicacio() {
        Scanner teclat = new Scanner(System.in);
        System.out.println("Benvingut a l'aplicació!!");
        System.out.println("Vols començar una partida o carregar-ne una de començada? (Començar/Carregar)");
        String opcio = null;
        while(opcio == null){
            opcio = teclat.nextLine();
            if (!opcio.equals("Començar") && !opcio.equals("Carregar")) {
                System.out.println("Opcio incorrecte, introdueix-ne una correcte (Començar/Carregar)");
                opcio = null;
            }
        }
        if(opcio.equals("Començar")){
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
        StringBuilder posInici = new StringBuilder();
        StringBuilder posFinal = new StringBuilder();
        StringBuilder res = new StringBuilder();
        System.out.println("Comencen les " + colorTorn);
        do{
            _partida.mostrarTaulell();
            if(res.toString().equals("taules")){
                continuar = taules();
                res = new StringBuilder();
            }else if(res.toString().equalsIgnoreCase("promocio")){
                res = new StringBuilder(promocio(posInici, posFinal));
            }else{
                continuar = tirada(res, posInici, posFinal, colorTorn);
            }

            processarRes(res, colorTorn);

        }while(continuar);
    }

    private static boolean tirada(StringBuilder res, StringBuilder posInici, StringBuilder posFinal, StringBuilder colorTorn) {
        Scanner teclat = new Scanner(System.in);
        boolean continuar = true;
        System.out.println("Torn del jugador de peces " + colorTorn.toString());
        System.out.println("Entra el que vols fer. (Rendirse/Taules/Ajornar/Desfer/Refer/(o escrius la posició de la peça que vols moure");
        String s = llegirPosicioInici();
        posInici = new StringBuilder(s);
        if(s.equalsIgnoreCase("Rendirse")){
            continuar = false;
            res = new StringBuilder("rendirse"); //ens podem permetre anar fent "new" ja que gràcies al garbage collector, no ocuparem memòria extra pels new, la memòria que estava ocupada anteriorment s'alliberarà
        }else if(s.equalsIgnoreCase("Taules")){
            res = new StringBuilder("taules");
        }else if(s.equalsIgnoreCase("Ajornar")){
            continuar = false;
            res = new StringBuilder("ajornar");
        }else if(s.equalsIgnoreCase("desfer")){
            //_partida.desfer();
            res = new StringBuilder("desfer fet");
        }else if(s.equalsIgnoreCase("refer")){
            //_partida.refer();
            res = new StringBuilder("refer fet");
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
                }else if(tokens[0].equalsIgnoreCase("enroc")){
                    System.out.println("Segur que vols fer aquest enroc?");
                    String opcio = teclat.nextLine();
                    if(opcio.equalsIgnoreCase("si")){
                        res = new StringBuilder(_partida.ferTirada(posInici.toString() + "- " + tokens[1]));
                    }else if(opcio.equalsIgnoreCase("no")){
                        res = new StringBuilder("no enroc");
                    }
                }else{
                    System.out.println("Fail");
                    System.exit(-1);
                }
            }else{//si entra aqui sabem segur que ha retornat "no", per tant, que no vol moure la peça que ha dit
                System.out.println("Pots tornar a entrar la posició Inicial");
            }
        }
        return continuar;
    }

    private static String promocio(StringBuilder posInici, StringBuilder posFinal){
        Scanner teclat = new Scanner(System.in);
        String s = teclat.nextLine();
        String res;
        if(s.equalsIgnoreCase("si")){
            System.out.println("Per quina peça vols fer");
            String peca;
            do{
                peca = teclat.nextLine();
                res = _partida.ferPromocio(posInici.toString()+posFinal.toString(), peca);
                if(res.equalsIgnoreCase("no valid")){
                    System.out.println("Peça no vàlida, entra'n una de vàlida");
                }
            }while(!res.equalsIgnoreCase("feta"));
            res = "si promocio";
        }else{
            res = "no promocio";
        }
        return res;
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


    private static void processarRes(StringBuilder res, StringBuilder colorTorn){
        if(res.toString().equals("tirada vàlida") || res.toString().equalsIgnoreCase("return tirada vàlida i s'ha matat")){
            System.out.println(res);
            colorTorn = new StringBuilder(_partida.canviarTorn());
            res = new StringBuilder("");
        }else if(res.toString().equals("taules")){
            System.out.println("El jugador amb les peces " + colorTorn + " demana taules");
            colorTorn = new StringBuilder(_partida.canviarTorn());
        }else if(res.toString().equals("return tirada vàlida i s'ha matat")){
            System.out.println(res);
        }else if(res.toString().equals("no s'ha realitzat la tirada")){
            System.out.println(res);
        }else if(res.toString().equalsIgnoreCase("desfer fet")){
            System.out.println(res);
        }else if(res.toString().equalsIgnoreCase("refer fet")){
            System.out.println(res);
        }else if(res.toString().equalsIgnoreCase("promocio")){
            System.out.println("Vols fer promoció d'alguna peça?");
        }else if(res.toString().equalsIgnoreCase("no promocio")){
            System.out.println("No has volgut fer la promoció, canvi de torn");
        }else if(res.toString().equalsIgnoreCase("si promocio")){
            System.out.println("Has fet la promoció!");
        }else if(res.toString().equalsIgnoreCase("ajornar")){
            System.out.println("El jugador amb les peces " + colorTorn + " ajorna la partida");
            _partida.ajornar();
        }else if(res.toString().equalsIgnoreCase("rendirse")){
            System.out.println("El jugador amb les peces " + colorTorn + " es rendeix");
            _partida.rendirse();
        }else if(res.toString().equalsIgnoreCase("no enroc")){
            System.out.println("No has volgut fer l'enroc");
        }
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
        String correcte = "";
        String res = _partida.posCorrecteDesti(s);
        if(res.equalsIgnoreCase("Posició invàlida")) { //posicio sense peça
            System.out.println("La posició que has entrat no té cap peça");
            correcte = "no";
        }else if(res.equals("Enrroc")){ //la peça no es del teu color
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
