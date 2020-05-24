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
        if(opcio == "Començar"){System.out.println("Entra el fitxer de regles: (nomFitxer.json)"); }
        else{System.out.println("Entra el fitxer de la partida que vols carregar:");}
        String nomFitxer = teclat.nextLine();
        _partida = new Partida(nomFitxer); //un metode que carrega el fitxer.
        jugar();
    }

    private static void jugar(){
        boolean continuar = true;
        Scanner teclat = new Scanner(System.in);
        String colorTorn = _partida.getProperTorn();
        String posInici = null; //encara que es digui posInici, potser hi ha "taules" o "rendirse"
        String posFinal = null;
        String res = null;
        do{
            if(res.equals("taules")){
                System.out.println("Acceptes les taules? (Si/No)");
                boolean correcte = false;
                do {
                    posInici = teclat.nextLine();
                    if(posInici.equals("Si") || posInici.equals("No")){ correcte = true; }
                }while(!correcte);
                if(posInici.equals("Si")){
                    //_partida.taules();
                    continuar = false;
                }
            }else{
                posInici = llegirPosicioInici();
                if(posInici.equals("Rendirse")){
                    System.out.println("El jugador amb les peces " + colorTorn + " es rendeix");
                    continuar = false;
                    //_partida.rendirse();
                }else if(posInici.equals("Taules")){
                    res = "taules";
                }else if(posInici.equals("Ajornar")){
                    //res = _partida.ajornar();
                }else{
                    posInici = posInici+ " ";
                    posFinal = llegirPosicioDesti();
                    //res = _partida.ferTirada(posInici+posInici);
                }

                processarMissatge(res);
            }
            if(res.equals("Tirada feta")){
                //colorTorn = _partida.canviarTorn();
                //_partida.mostrarTaulell();
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
        String s = null;
        String res = "";
        Scanner teclat = new Scanner(System.in);
        do{
            if(s.equals("-")){ //vol dir que vol fer enroc
                res = "- ";
            }else if(s.equals("  ")){ //ha entrat una posició
                correcte = posicioCorrecte(s);
                res = res + s;
            }
        } while(!correcte);
        return res;
    }

    private static boolean posicioCorrecte(String s){
        boolean correcte = false;
        /*String res = _partida.posCorrecte(s);
        if(res.equals("NO")){ //no es una posicio

        }else if(res.equals("Posició invàlida")) { //posicio sense peça

        }else if(res.equals("Posició amb una peça")){ //la peça no es del teu color

        }
        else{
            correcte = true;
        }*/
        return correcte;
    }
}
