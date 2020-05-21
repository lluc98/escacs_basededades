package Partida;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;

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
        Partida partida = new Partida(nomFitxer); //un metode que carrega el fitxer.
        jugar(partida);
    }

    private static void jugar(Partida partida){
        boolean continuar = true;
        Taulell taulell = partida.getTaulell();
        String primerEquip = partida.getProperTorn();
        boolean colorTorn = false;
        if (primerEquip.equals("BLANQUES")){
            colorTorn = true;
        }
        do{
            //Declarem les posicions del moviment que vol fer el jugador, les inicialitzem a null.
            Posicio posInici = null;
            Posicio posFinal = null;
            Peca pecaMoguda = null;

            //Demanem la primera posició.
            System.out.println("Entra la posició on es troba la peça que vols jugar");
            pecaMoguda = llegirPosicioInici(posInici, taulell, colorTorn);

            if (pecaMoguda == null) {
                System.out.println("Alguna cosa ha sortit malament per a moure aquesta peça");
            } else {
                TiradaSimple tiradaActual = new TiradaSimple(posInici, posFinal, colorTorn);
                //Demanem la segona posició.
                System.out.println("Entra la posició on vols que vagi la peça seleccionada");
                llegirPosicioDesti(posFinal, taulell, colorTorn, pecaMoguda, tiradaActual);
            }

            //taulell.mostrarTaulellText();

        }while(continuar);
    }

    private static Peca llegirPosicioInici(Posicio p, Taulell taulell, boolean colorTorn){
        boolean correcte = false;
        Peca pecaActual = null;
        do{
            Scanner teclat = new Scanner(System.in);
            String pos = teclat.nextLine();

            p = new Posicio(pos);
            if (taulell.contePeçaCasella(p)) {
                pecaActual = taulell.getPeca(p);
                if (pecaActual.get_equip() == colorTorn) {
                    correcte = true;
                }
                else {
                    System.out.println("Peça del color incorrecte");
                }
            }
            else {
                System.out.println("Posició invàlida, entre'n una altre");
            }
        } while(!correcte);
        return pecaActual;
    }

    private static void llegirPosicioDesti(Posicio p, Taulell taulell, boolean colorTorn, Peca pecaMoguda, TiradaSimple tiradaActual){
        boolean correcte = false;
        do{
            Scanner teclat = new Scanner(System.in);
            String pos = teclat.nextLine();

            p = new Posicio(pos);
            if (taulell.contePeçaCasella(p)) {
                Peca pecaActual = taulell.getPeca(p);
                if (pecaActual.get_equip() != colorTorn && pecaMoguda.movimentValid(tiradaActual)) {
                    correcte = true;
                } else if (pecaActual.get_equip() == colorTorn && pecaMoguda.movimentValid(tiradaActual)) {
                    System.out.println("Posició ja ocupada per una peça del teu equip");
                } else if (!pecaMoguda.movimentValid(tiradaActual)) {
                    System.out.println("Moviment invàlid");
                } else {
                    System.out.println("No pots realitzar aquest moviment");
                }
            } else if (!taulell.contePeçaCasella(p) && pecaMoguda.movimentValid(tiradaActual)){
                correcte = true;
            } else {
                System.out.println("No pots realitzar aquest moviment");
            }
        } while(!correcte);
    }
}
