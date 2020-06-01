package partida;
/** @file JocEscacs.java
 * @brief Un joc de Escacs amb mode text i mode gràfic, versió amb control de torns.
 */

import javafx.application.Application;

/** @class JocEscacs
 * @brief Programa principal
 */
public abstract class JocEscacs {

    /**
     @pre \p args és per veure si s'executa l'aplicació en mode text o en mode gràfic
     @post S'executa un joc d'Escacs; amb el paràmetre -g s'executa en mode gràfic
     */
    public static void main(String[] args){
        try {
            if (args.length == 0) {
                PartidaText.iniciaAplicacio();
                return;
            }
            else if (args.length == 1 && args[0].equals("-g")) {
                System.out.println("Benvingut a l'aplicació!!");
                Application.launch(PartidaGrafica.class);
                return;
            }
        }
        catch (Exception e) {
            System.out.println("S'ha produït una excepció.");
            e.printStackTrace();
        }
    }
}