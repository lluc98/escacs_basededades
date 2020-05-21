package Partida;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;

public class LlegirRegles {

    public static void llegirRegles(String path, TreeMap<String, TipusPeca> conjuntPeces, Taulell taulell, int limitEscacsSeguits, int limitTornsInaccio ) {
        try {
            String contingut = new String((Files.readAllBytes((Paths.get(path)))));
            JSONObject regles = new JSONObject(contingut);
            //Ara tenim guardabt tot el contingut del fitxer Regles al JSONObject regles, fem constructors.

            int nFiles = regles.getInt("nFiles");
            int nCols = regles.getInt("nCols");

            taulell = new Taulell(nCols, nFiles);

            JSONArray peces = regles.getJSONArray("peces");

            for (int i=0; i<peces.length(); i++) {

                ArrayList<Moviment> movimentNormal = new ArrayList<>();
                ArrayList<Moviment> movimentInicial = new ArrayList<>();

                JSONObject peca = peces.getJSONObject(i);
                String nom = peca.getString("nom");
                String simbol = peca.getString("simbol");
                String imatgeBlanca = peca.getString("imatgeBlanca");
                String imatgeNegra = peca.getString("imatgeNegra");
                int valor = peca.getInt("valor");

                JSONArray moviments = peca.getJSONArray("moviments");

                for (int j=0; j<moviments.length(); j++){
                    JSONArray moviment = moviments.getJSONArray(j);
                    String horitzontal = String.valueOf(moviment.get(0));
                    String vertical = String.valueOf(moviment.get(1));
                    int movMatar = moviment.getInt(2);
                    int movSaltar = moviment.getInt(3);

                    Moviment m = new Moviment(horitzontal,vertical,movMatar,movSaltar);

                    movimentNormal.add(m);
                }

                JSONArray movimentsInicials = peca.getJSONArray("movimentsInicials");

                for (int j=0; j<movimentsInicials.length(); j++){
                    JSONArray moviment = moviments.getJSONArray(j);
                    String horitzontal = String.valueOf(moviment.get(0));
                    String vertical = String.valueOf(moviment.get(1));
                    int movMatar = moviment.getInt(2);
                    int movSaltar = moviment.getInt(3);

                    Moviment m = new Moviment(horitzontal,vertical,movMatar,movSaltar);

                    movimentInicial.add(m);
                }

                boolean promocio = peca.getBoolean("promocio");
                boolean invulnerabilitat = peca.getBoolean(("invulnerabilitat"));

                TipusPeca tpeca = new TipusPeca(nom, simbol, imatgeBlanca, imatgeNegra, valor, movimentNormal, movimentInicial, promocio, invulnerabilitat);

                conjuntPeces.put(nom, tpeca);
            }

            JSONArray posInicial = regles.getJSONArray("posInicial");


            for (int i=0; i<posInicial.length(); i++) {
                String pecaIni = posInicial.getString(i);
                Peca pecaInicialBlanca = new Peca(pecaIni, true, conjuntPeces);
                Peca pecaInicialNegre = new Peca(pecaIni, false, conjuntPeces);
                taulell.afegirPeca(pecaInicialBlanca,pecaInicialNegre);
            }


            //Això ens ho emportem a Partida
            limitEscacsSeguits = regles.getInt("limitEscacsSeguits");
            limitTornsInaccio = regles.getInt("limitTornsInaccio");

            JSONArray enrocs = regles.getJSONArray("enrocs");

            for (int i=0; i<enrocs.length(); i++) {
                JSONObject enroc = enrocs.getJSONObject(i);
                String pecaA = enroc.getString("peçaA");
                String pecaB = enroc.getString("peçaB");
                boolean quiets = enroc.getBoolean("quiets");
                boolean buitAlMig = enroc.getBoolean("buitAlMig");
                CaracteristiquesEnroc ce = new CaracteristiquesEnroc(pecaA, pecaB, quiets, buitAlMig);
                conjuntPeces.get(pecaA).afegirEnrroc(ce);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void llegirPartidaAcabada (String path, TreeMap<String, TipusPeca> conjuntPeces, Taulell taulell, String properTorn, int limitEscacsSeguits, int limitTornsInaccio) {
        try {
            String contingut = new String((Files.readAllBytes(Paths.get(path))));
            JSONObject partidaN =  new JSONObject(contingut);
            String pathRegles = partidaN.getString("fitxerRegles");

            llegirRegles(pathRegles, conjuntPeces, taulell, limitEscacsSeguits, limitTornsInaccio);

            JSONArray posIniBlanques = partidaN.getJSONArray("posIniBlanques");

            for (int i=0; i<posIniBlanques.length(); i++) {
                JSONObject posicio = posIniBlanques.getJSONObject(i);

                String pos = posicio.getString("pos");
                String tipus = posicio.getString("tipus");
                boolean moguda = posicio.getBoolean("moguda");

                Peca p = new Peca(tipus, true, conjuntPeces);
                Posicio posicioB = new Posicio(pos);
                taulell.assignarPecaTauler(p, posicioB);
            }

            JSONArray posIniNegres = partidaN.getJSONArray("posIniBlanques");

            for (int i=0; i<posIniNegres.length(); i++) {
                JSONObject posicio = posIniNegres.getJSONObject(i);

                String pos = posicio.getString("pos");
                String tipus = posicio.getString("tipus");
                boolean moguda = posicio.getBoolean("moguda");

                Peca p = new Peca(tipus, false, conjuntPeces);
                Posicio posicioN = new Posicio(pos);
                taulell.assignarPecaTauler(p, posicioN);
            }

            //ens ho enduem a partida
            properTorn = partidaN.getString("proper_torn");

            JSONArray tirades = partidaN.getJSONArray("tirades");
            for (int i = 0; i<tirades.length(); i++) {
                JSONObject tirada = tirades.getJSONObject(i);

                String torn = tirada.getString("torn");
                String origen = tirada.getString("origen");
                String desti = tirada.getString("desti");
                String resultat = tirada.getString("resultat");

                //Ho hem de guardar a algun lloc
            }

            //Això ens diferencia de una partida que no esta acabada
            String resultatFinal = partidaN.getString("resultat_final");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void llegirPartidaComencada (String path, TreeMap<String, TipusPeca> conjuntPeces, Taulell taulell, String properTorn, int limitTornsInaccio, int limitEscacsSeguits ) {
        try {
            String contingut = new String((Files.readAllBytes(Paths.get(path))));
            JSONObject partidaN =  new JSONObject(contingut);
            String pathRegles = partidaN.getString("fitxerRegles");

            llegirRegles(pathRegles, conjuntPeces, taulell, limitEscacsSeguits, limitTornsInaccio);

            JSONArray posIniBlanques = partidaN.getJSONArray("posIniBlanques");

            for (int i=0; i<posIniBlanques.length(); i++) {
                JSONObject posicio = posIniBlanques.getJSONObject(i);

                String pos = posicio.getString("pos");
                String tipus = posicio.getString("tipus");
                boolean moguda = posicio.getBoolean("moguda");

                Peca p = new Peca(tipus, true, conjuntPeces);
                Posicio posicioB = new Posicio(pos);
                taulell.assignarPecaTauler(p, posicioB);
            }

            JSONArray posIniNegres = partidaN.getJSONArray("posIniBlanques");

            for (int i=0; i<posIniNegres.length(); i++) {
                JSONObject posicio = posIniNegres.getJSONObject(i);

                String pos = posicio.getString("pos");
                String tipus = posicio.getString("tipus");
                boolean moguda = posicio.getBoolean("moguda");

                Peca p = new Peca(tipus, false, conjuntPeces);
                Posicio posicioN = new Posicio(pos);
                taulell.assignarPecaTauler(p, posicioN);
            }

            //ens ho enduem a partida
            properTorn = partidaN.getString("proper_torn");

            JSONArray tirades = partidaN.getJSONArray("tirades");
            for (int i = 0; i<tirades.length(); i++) {
                JSONObject tirada = tirades.getJSONObject(i);

                String torn = tirada.getString("torn");
                String origen = tirada.getString("origen");
                String desti = tirada.getString("desti");
                String resultat = tirada.getString("resultat");

                //Ho hem de guardar a algun lloc
            }

            //Això ens diferencia de una partida que no esta acabada
            String resultatFinal = partidaN.getString("resultat_final");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
