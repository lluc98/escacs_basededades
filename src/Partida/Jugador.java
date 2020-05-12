package Partida;
import java.util.Scanner
public class Jugador {
    private String _nom;
    boolean _equip;

    Jugador(String nom, boolean equip){
        _nom = nom;
        _equip = equip;
    }

    public void ferJugada(){
        System.out.println("Quina jugada vols realitzar(Tirada, Enroc, Rendirse o Taules)");
        Scanner s = new Scanner(System.in);
        String jugada = s.nextLine();
        
    }
}

