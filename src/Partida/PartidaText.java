package Partida;

import java.util.Scanner;

public abstract class PartidaText {

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
        /*Partida partida = new Partida(nomFitxer); //un metode que carrega el fitxer.
        jugar(partida);*/
    }

    private static void jugar(Partida partida){
        boolean continuar = true;
        do{
            //Declarem les posicions del moviment que vol fer el jugador, les inicialitzem a null.
            Posicio posInici = null;
            Posicio posFinal = null;

            //Demanem la primera posició.
            System.out.println("Entra la posició on es troba la peça que vols jugar");
            llegirPosicio(posInici);

            //Demanem la segona posició.
            System.out.println("Entra la posició on vols que vagi la peça seleccionada");
            llegirPosicio(posFinal);



        }while(continuar);
    }

    private static void llegirPosicio(Posicio p){
        boolean correcte = false;
        do{
            Scanner teclat = new Scanner(System.in);
            String pos = teclat.nextLine();
            p = new Posicio(pos);
            //correcte = p.valida();
            if(!correcte){
                System.out.println("Posició invàlida, entre'n una altre");
            }
        }while(!correcte);
    }

}
