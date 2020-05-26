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
            System.out.println("Torn del jugador de peces " + colorTorn);
            //_partida.mostrarTaulell();
            if(res.equals("taules")){
                boolean correcte = false;
                do {
                    System.out.println("Acceptes les taules? (Si/No)");
                    posInici = teclat.nextLine();
                    if(posInici.equals("Si") || posInici.equals("No")){ correcte = true; }
                }while(!correcte);
                if(posInici.equals("Si")){
                    //_partida.taules();
                    continuar = false;
                }
                res = "";
            }else{
                System.out.println("Entra el que vols fer. (Rendirse/Taules/Ajornar/(o escrius la posició de la peça que vols moure");
                posInici = llegirPosicioInici();
                if(posInici.equals("Rendirse")){
                    System.out.println("El jugador amb les peces " + colorTorn + " es rendeix");
                    continuar = false;
                    //_partida.rendirse();
                }else if(posInici.equals("Taules")){
                    res = "taules";
                }else if(posInici.equals("Ajornar")){
                    System.out.println("El jugador amb les peces " + colorTorn + " ajorna la partida");
                    continuar = false;
                    //_partida.ajornar();
                }else{
                    System.out.println("Pots moure aquesta peça, a on la vols moure? O escriu " + "no" + " si prefereixes moure una altre peça");
                    posInici = posInici+ " ";
                    posFinal = llegirPosicioDesti();
                    if(!posFinal.equals("no")){
                        res = _partida.ferTirada(posInici+posInici);
                    }
                    else{
                        System.out.println("Pots tornar a entrar la posició Inicial");
                    }
                }
            }
            if(res.equals("tirada vàlida")){
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
            }else{
                System.out.println("Alguna cosa ha sortit malament");
            }
        }while(continuar);
    }

    private static void processarMissatge(String s){
        //mirar el que retorna "ferTirada"
        //Podria ser que el moviment fos invalid ...................

    }

    private static String llegirPosicioInici(){
        boolean correcte = false;
        String s;
        Scanner teclat = new Scanner(System.in);
        do{
            s = teclat.nextLine();
            if(s.equals("Rendirse") || s.equals("Taules") || s.equals("Ajornar")){
                return s;
            }
            else{
                //si entra aqui vol dir que ha entrat una posicio o aixo creiem
                correcte = posicioCorrecte(s);
            }
        } while(!correcte);
        return s;
    }

    private static String llegirPosicioDesti(){
        boolean correcte = false;
        String s;
        String res;
        Scanner teclat = new Scanner(System.in);
        do{
            res = "";
            System.out.println("Entra la següent posició. Si vols fer un enrroc entra ( '-' seguit de la posició)");
            s = teclat.nextLine();
            if(s.charAt(0) == '-'){ //vol dir que vol fer enroc
                String array[] = s.split("-");
                res = "- ";
                correcte = posicioCorrecte(array[1]); //comprovem que la posició sigui correcte.
                if(!correcte){
                    System.out.println("Entra una posició correcte");
                }else{
                    res = res + array[1];
                }
            }else if(s.contains("no")){ //fem contains per d'aquesta manera l'usuari pot entrar una string amb caràcters random, però si  conté "no", entra aqui
                return s;
            }else{ //ha entrat una suposada posició
                correcte = posicioCorrecte(s);
                if(!correcte){
                    System.out.println("Entra una posició correcte");
                }else{
                    res = res + s;
                }
            }
        } while(!correcte);
        return res;
    }

    private static boolean posicioCorrecte(String s){
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
}
