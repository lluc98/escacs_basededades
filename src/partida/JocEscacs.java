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
     @pre \p args és N M -g on 8 <= N <= 26 i 2 <= M <= 26
     @post S'executa un joc d'Escacs amb N files i M columnes; amb l'opció -g s'executa en mode gràfic
     @details 1.-Preguntar si es vol començar una partida o carregar-ne una
      * 2.1-Si es vol començar una partida, preguntar pel fitxer de regles (es pregunta aqui perquè si ho preguntessim després de preguntar si es vol que
      * sigui de text o gràfic, repetiriem codi)
      * 2.1.1.-Preguntar si es vol crear una partida de text o gràfica
      * 2.1.1.1.- Si es vol partida de text, crear un "partidaText" on li passem el nom del fitxer de regles que hem demanat abans
      * 2.1.2.1- ((partida grafica))
      *
      * 2.2.-Si es volgués carregar una partida, demanar el nom del fitxer on hi ha la partida que volem carregar.
     *
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