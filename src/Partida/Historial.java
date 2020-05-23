package Partida;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Historial {
    private static FileWriter fitxerPartida;
    private static String adrecaFitxer = "Partida.txt";

    public static void iniciarPartidaNova() {
        String resultatfinal;

        FileWriter fitxerPartida;
        JSONObject partida = new JSONObject(adrecaFitxer);
        //Hauriem de comprovar si ja existeix el fitxer
        partida.put("fitxerRegles", "");

        JSONArray posIniBlanques = new JSONArray();
        partida.put("posIniBlanques", posIniBlanques);

        JSONArray posIniNegres = new JSONArray();
        partida.put("posIniBlanques", posIniNegres);

        partida.put("proper_torn","");

        JSONArray tirades = new JSONArray();
        partida.put("tirades", tirades);

        partida.put("resultat_final","");
    }

    public static void guardarTirada(String[] args) {
        try {
            JSONObject infoTirada = new JSONObject();
            infoTirada.put("torn", args[0]);
            infoTirada.put("origen", args[1]);
            infoTirada.put("desti", args[2]);
            infoTirada.put("resultat", args[3]);
            String contingut = new String((Files.readAllBytes((Paths.get("PartidaActual.txt")))));
            JSONObject fitxerTirades = new JSONObject(contingut);
            JSONArray tirades = fitxerTirades.getJSONArray("tirades");
            tirades.put(infoTirada);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getUltimaTirada(String torn, String origen, String desti, String resultat) {
        try {
            String contingut = new String((Files.readAllBytes((Paths.get("PartidaActual.txt")))));
            JSONObject fitxerTirades = new JSONObject(contingut);
            JSONArray tirades = fitxerTirades.getJSONArray("tirades");
            JSONObject tirada = tirades.getJSONObject(tirades.length() - 1);

            torn = tirada.getString("torn");
            origen = tirada.getString("origen");
            desti = tirada.getString("desti");
            resultat = tirada.getString("resultat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void eliminarUltimaTirada() {
        try {
            String contingut = new String((Files.readAllBytes((Paths.get("PartidaActual.txt")))));
            JSONObject fitxerTirades = new JSONObject(contingut);
            JSONArray tirades = fitxerTirades.getJSONArray("tirades");
            tirades.remove(tirades.length() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void guardarPartida () {

    }
}
