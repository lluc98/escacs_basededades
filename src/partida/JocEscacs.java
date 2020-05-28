package partida;

import javafx.application.Application;

public abstract class JocEscacs {

    public static void main(String[] args){
        /**
         * 1.-Preguntar si es vol començar una partida o carregar-ne una
         * 2.1-Si es vol començar una partida, preguntar pel fitxer de regles (es pregunta aqui perquè si ho preguntessim després de preguntar si es vol que
         * sigui de text o gràfic, repetiriem codi)
         * 2.1.1.-Preguntar si es vol crear una partida de text o gràfica
         * 2.1.1.1.- Si es vol partida de text, crear un "partidaText" on li passem el nom del fitxer de regles que hem demanat abans
         * 2.1.2.1- ((partida grafica))
         *
         * 2.2.-Si es volgués carregar una partida, demanar el nom del fitxer on hi ha la partida que volem carregar.
         *
         */
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