package Partida;

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
            int nJug = 0;
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
        Scanner teclat = new Scanner(System.in);
        String colorTorn = _partida.getProperTorn();
        String posInici = null; //encara que es digui posInici, potser hi ha "taules" o "rendirse"
        String posFinal = null;
        String res = "";
        System.out.println("Comencen les " + colorTorn);
        do{
            _partida.mostrarTaulell();
            if(res.equals("taules")){
                boolean correcte = false;
                do{
                    System.out.println("Acceptes les taules? (Si/No)");
                    posInici = teclat.nextLine();
                    if(posInici.equals("Si") || posInici.equals("No")){ correcte = true; }
                }while(!correcte);
                if(posInici.equals("Si")){
                    _partida.taules();
                    continuar = false;
                }
                res = "";
            }else if(res.equalsIgnoreCase("promocio")){
                String s = teclat.nextLine();
                if(s.equalsIgnoreCase("si")){
                    System.out.println("Per quina peça vols fer");
                    String peca = teclat.nextLine();
                    do{
                        //res = _partida.ferPromocio(posInici+posFinal, peca);
                        if(res.equalsIgnoreCase("no valid")){
                            System.out.println("Peça no vàlida, entra'n una de vàlida");
                        }
                    }while(!res.equalsIgnoreCase("feta"));
                    res = "si promocio";
                }else{
                    res = "no promocio";
                }

            }else{
                System.out.println("Torn del jugador de peces " + colorTorn);
                System.out.println("Entra el que vols fer. (Rendirse/Taules/Ajornar/Desfer/Refer/(o escrius la posició de la peça que vols moure");
                posInici = llegirPosicioInici();
                if(posInici.equalsIgnoreCase("Rendirse")){
                    continuar = false;
                    res = "rendirse";
                }else if(posInici.equalsIgnoreCase("Taules")){
                    res = "taules";
                }else if(posInici.equalsIgnoreCase("Ajornar")){
                    continuar = false;
                    res = "ajornar";
                }else if(posInici.equalsIgnoreCase("desfer")){
                    //_partida.desfer();
                    res = "desfer fet";
                }else if(posInici.equalsIgnoreCase("refer")){
                    //_partida.refer();
                    res = "refer fet";
                }
                else{
                    System.out.println("Pots moure aquesta peça, a on la vols moure? O escriu 'no' si prefereixes moure una altre peça");
                    posInici = posInici+ " ";
                    posFinal = llegirPosicioDesti();
                    String[] tokens = posFinal.split(" ");
                    if(tokens.length > 1){
                        if(tokens[0].equalsIgnoreCase("correcte")) {
                            res = _partida.ferTirada(posInici + tokens[1]);
                        }else if(tokens[0].equalsIgnoreCase("enroc")){
                            System.out.println("Segur que vols fer aquest enroc?");
                            String s = teclat.nextLine();
                            if(s.equalsIgnoreCase("si")){
                                res = _partida.ferTirada(posInici + "- " + tokens[1]);
                            }else if(s.equalsIgnoreCase("no")){
                                res = "no enroc";
                            }
                        }else{
                            System.out.println("Fail");
                            System.exit(-1);
                        }
                    }else{//si entra aqui sabem segur que ha retornat "no", per tant, que no vol moure la peça que ha dit
                        System.out.println("Pots tornar a entrar la posició Inicial");
                    }
                }
            }

            processarRes(res, colorTorn);

        }while(continuar);
    }

    private static void processarRes(String res, String colorTorn){
        if(res.equals("tirada vàlida") || res.equalsIgnoreCase("return tirada vàlida i s'ha matat")){
            System.out.println(res);
            colorTorn = _partida.canviarTorn();
            res = "";
        }else if(res.equals("taules")){
            System.out.println("El jugador amb les peces " + colorTorn + " demana taules");
            colorTorn = _partida.canviarTorn();
        }else if(res.equals("return tirada vàlida i s'ha matat")){
            System.out.println(res);
        }else if(res.equals("no s'ha realitzat la tirada")){
            System.out.println(res);
        }else if(res.equalsIgnoreCase("desfer fet")){
            System.out.println(res);
        }else if(res.equalsIgnoreCase("refer fet")){
            System.out.println(res);
        }else if(res.equalsIgnoreCase("promocio")){
            System.out.println("Vols fer promoció d'alguna peça?");
        }else if(res.equalsIgnoreCase("no promocio")){
            System.out.println("No has volgut fer la promoció, canvi de torn");
        }else if(res.equalsIgnoreCase("si promocio")){
            System.out.println("Has fet la promoció!");
        }else if(res.equalsIgnoreCase("ajornar")){
            System.out.println("El jugador amb les peces " + colorTorn + " ajorna la partida");
            _partida.ajornar();
        }else if(res.equalsIgnoreCase("rendirse")){
            System.out.println("El jugador amb les peces " + colorTorn + " es rendeix");
            _partida.rendirse();
        }else if(res.equalsIgnoreCase("no enroc")){
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
        String res = _partida.posCorrecte(s);
        if(res.equals("NO")){ //no es una posicio
            System.out.println("El que has entrat no és una posició");
        }else if(res.equals("Posició invàlida")) { //posicio sense peça
            System.out.println("La posició que has entrat no té cap peça");
        }else if(res.equals("Posició amb una peça")){ //la peça no es del teu color
            System.out.println("La peça que hi ha en aquesta posició no és del teu color");
        }
        else{
            correcte = true;
        }
        return correcte;
    }

    private static String posicioCorrecteDesti(String s){
        String correcte = "";
        String res = _partida.posCorrecte(s);
        if(res.equals("NO")){ //no es una posicio
            System.out.println("El que has entrat no és una posició");
            correcte = "no";
        }else if(res.equals("Posició invàlida")) { //posicio sense peça
            System.out.println("La posició que has entrat no té cap peça");
            correcte = "no";
        }else if(res.equals("Enroc")){ //la peça no es del teu color
            correcte = res + s;
        }
        else{
            System.out.println("Posició correcte");
            correcte = res + s;
        }
        return correcte;
    }
}
