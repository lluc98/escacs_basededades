package partida;
/** @file CPU.java
 * @brief Jugador "màquina"
 */

import javafx.geometry.Pos;

import javax.swing.border.StrokeBorder;
import java.util.Random;

/** @class CPU
 * @brief S'encarrega de substituïr un jugador fisic
 */
public class CPU {
    private boolean _equip;             ///< Equip de la màquina

    /** @brief  Inicialitzador del jugador CPU
     * @pre --
     * @post El jugador CPU sap el seu equip
     */
    CPU(boolean equip){
        _equip = equip;
    }

    /** @brief  Realitza l'acció de tirar una peça
     * @param taulell Posició inicial de la Peça
     * @pre El taulell està actualitzat
     * @post S'ha realitzat una tirada automàtica
     */
    public void ferTirada(Taulell taulell){
        TiradaSimple t= taulell.hihaJaque(_equip);
        if(t!=null){
            //intentar truere el jaqu

        }
        else{
            t = taulell.hiHaAlgunaAmenaça(_equip);
            if(t!=null){
                //intentar truere la amenaça
            }
            else{
                t = taulell.hiHaAlgunaAmenaça(!_equip);
                if(t!=null){
                    taulell.realitzarTirada(t);
                }
                else{
                    Random rand = new Random();
                    boolean trobat = false;
                    while(!trobat){
                        Posicio p = taulell.escollirPosPeca(_equip);
                        int i1 = rand.nextInt(7)+1;
                        int i2 = rand.nextInt(7)+1;
                        int it = 0;
                        Posicio p2 = new Posicio(i1,i2);
                        TiradaSimple tirada = new TiradaSimple(p,p2,_equip);
                        while(!trobat && it <64){
                            i1 = rand.nextInt(7)+1;
                            i2 = rand.nextInt(7)+1;
                            p2 = new Posicio(i1,i2);
                            tirada = new TiradaSimple(p,p2,_equip);
                            if(taulell.tiradaValida(tirada)){
                                taulell.realitzarTirada(tirada);
                                trobat =true;
                            }
                            it++;
                        }
                    }
                }
            }
        }

    }
}
