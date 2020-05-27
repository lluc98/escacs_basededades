package Partida;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Historial {
    private static String adrecaFitxer = "Partida.txt";
    public static FileWriter fitxerPartida;

    public static JSONObject partida = new JSONObject();

    static {
        try {
            fitxerPartida = new FileWriter("PartidaNova.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void iniciarPartidaNova() {
        String contingutFitxer;
        //Hauriem de comprovar si ja existeix el fitxer

        partida.put("resultat_final", "");

        partida.put("fitxerRegles", "");

        JSONArray posIniBlanques = new JSONArray();
        partida.put("posIniBlanques", posIniBlanques);

        JSONArray posIniNegres = new JSONArray();
        partida.put("posIniNegres", posIniNegres);

        partida.put("proper_torn", "");

        JSONArray tirades = new JSONArray();
        partida.put("tirades", tirades);

    }

    public static void guardarPosInicial(String [] args, boolean moguda, boolean equip) {
        JSONObject infoPos = new JSONObject();
        infoPos.put("pos",args[0]);
        infoPos.put("tipus",args[1]);
        infoPos.put("moguda",moguda);
        if (equip == true) {
            JSONArray posIniBlanques = partida.getJSONArray("posIniBlanques");
            posIniBlanques.put(infoPos);
        } else {
            JSONArray posIniNegres = partida.getJSONArray("posIniNegres");
            posIniNegres.put(infoPos);
        }
    }

    public static void guardarFitxerRegles (String nomFitxerRegles) {
        partida.put("fitxerRegles", nomFitxerRegles);
    }

    public static void guardarProperTorn (String properTorn) {
        partida.put("proper_torn", properTorn);
    }

    public static void guardarTirada(String[] args) {
        JSONObject infoTirada = new JSONObject();
        infoTirada.put("torn", args[0]);
        infoTirada.put("origen", args[1]);
        infoTirada.put("desti", args[2]);
        infoTirada.put("resultat", args[3]);
        JSONArray tirades = partida.getJSONArray("tirades");
        tirades.put(infoTirada);
    }

    public static TiradaSimple getUltimaTirada() {
        JSONArray tirades = partida.getJSONArray("tirades");
        JSONObject tirada = tirades.getJSONObject(tirades.length() - 1);

        String torn = tirada.getString("torn");
        String origen = tirada.getString("origen");
        String desti = tirada.getString("desti");

        boolean equip = torn == "BLANQUES";
        Posicio o = new Posicio(origen);
        Posicio d = new Posicio(desti);
        return new TiradaSimple(o, d, equip);
    }

    public static void eliminarUltimaTirada() {
        JSONArray tirades = partida.getJSONArray("tirades");
        tirades.remove(tirades.length() - 1);
    }

    public static void guardarPartida(String resultat) {
        //resultat = "BLANQUES", han guanyat les blanques, "NEGRES" viceversa
        //resultat = "TAULES"
        //resultat = "AJORNAT"

        String contingut = "";


        if (resultat == "BLANQUES") {
            partida.put("resultat_final", "BLANQUES GUANYEN");
        } else if (resultat == "NEGRES") {
            partida.put("resultat_final", "NEGRES GUANYEN");
        } else if (resultat == "TAULES") {
            partida.put("resultat_final", "TAULES");
        } else {
            partida.put("resultat_final", "");
        }

        String a = partida.toString();
        try {
            fitxerPartida.write(a);
            fitxerPartida.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
