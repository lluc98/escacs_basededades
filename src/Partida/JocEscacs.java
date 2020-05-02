package Partida;

import javafx.application.Application;

public abstract class JocEscacs {

    public static void main(String[] args){
        try {
            if (args.length == 2) {
                int files = Integer.parseInt(args[0]);
                int columnes = Integer.parseInt(args[1]);
                PartidaText.juga(new Partida(files,columnes));
                return;
            }
            else if (args.length == 3 && args[2].equals("-g")) {
                Application.launch(PartidaGrafica.class, args);
                return;
            }
        }
        catch (Exception e) {
            System.out.println("S'ha produït una excepció.");
            e.printStackTrace();
        }
    }
}