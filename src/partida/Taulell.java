package partida;

/** @file Taulell.java
 * @brief Taulell d'Escac
 */
import java.util.*;

import static partida.Historial.guardarPosInicial;

/** @class Taulell
 * @brief Modul que representa el taulell d'escacs el qual pot ser modificable
 */
public class Taulell {

    private int _fila;
    private int _columna;
    private SortedMap<Posicio, Peca> _tauler;
    private TreeMap<Integer,TreeMap<Posicio, Peca>> _eliminats;
    private ArrayList<Peca> _promocio;
    private int n_peces;
    private Integer nTorns;
    private ArrayList<TiradaSimple> _tiradesRefer;


    /** @brief Genera un Taulell amb els atributs definits
     * @param c nombre de columnes del taulell
     * @param f nombre de files del taulell
     * @pre c i f nombres enters entre 4 i 16
     * @post Taulell creat de dimensio f x c
     */
    public Taulell(int c, int f){
        _fila = f;
        _columna = c;
        n_peces = 2;
        nTorns = 1;
        if(_columna < 4 || _columna > 16 || _fila <4 || _fila > 16)
            throw new RuntimeException("Error en el parametres");
        _tauler = new TreeMap<>();
        _eliminats = new TreeMap<>();
        _tiradesRefer = new ArrayList<>();
        _promocio = new ArrayList<>();
    }

    /** @brief Nombre de files */
    public int getFiles() {
        return _fila;
    }


    /** @brief Nombre de columnes */
    public int getColumnes() {
        return _columna;
    }


    /** @brief Assigna les peces entrades al seu lloc a mesura que li van entran
     * @param b peca blanca
     * @param n peca nerga
     * @pre b i n diferent de null
     * @post peces afegides al taulell
     */
    public void afegirPeca(Peca b, Peca n){
        if(n_peces < (_fila*_columna)-_columna) {
            int x, y, y2;
            if ((n_peces / 2) <= _columna) {
                y = 1;
                x = n_peces / 2;
                y2 = _fila;
            } else {
                y = ((n_peces /2)/ (_columna + 1)) + 1;
                x = (n_peces / 2) % _columna;
                if(x == 0){
                    x = _columna;
                }
                y2 = _fila - (y - 1);
            }
            Posicio p = new Posicio(y, x);
            Posicio p2 = new Posicio(y2, x);
            _tauler.put(p, b);
            _tauler.put(p2, n);
            n_peces = n_peces + 2;
            guardarPosInicial(p, b, false, true);
            guardarPosInicial(p2, n, false, false);
        }
        else
            throw new RuntimeException("NO CABEN MÉS PECES AL TAULELL");

    }

    /** @brief Assigna la peca al taulell a la posicio determinada
     * @param peca peça
     * @param pos una posicio del taulell
     * @pre peca i pos diferents de null
     * @post peca afegides al taulell a la posicio pos
     */
    public void assignarPecaTauler(Peca peca, Posicio pos){
        _tauler.put(pos,peca);
    }

    /** @brief Fa totes les comprovacions per veure si la tirada entrada es valida o no
     * @param t tirada que es vol validar
     * @pre t diferent de null
     * @post mira si la tirada es valida o no
     */
    public boolean tiradaValida(TiradaSimple t){
        Posicio origen = t.get_origen();
        Posicio desti = t.get_desti();
        Peca p = _tauler.get(origen);
        if(p != null && p.get_equip()==t.get_equip()){ //Miro que a la posicio origen hi ha un peça i es del equip correcte
            if((desti.get_fila() > 0) && (desti.get_fila() <= _fila) && (desti.get_columna() > 0) && (desti.get_columna() <= _columna)){ //miro si la posicio desti esta dins del taulell
                Peca c = _tauler.get(desti);
                if((c != null && c.get_equip()==t.get_equip()) || c!=null && c.esInvulnerable()){ //miro si hi ha peça a la posicio desti i de quin equip es
                    return false;
                }
                if(p.movimentValid(t)){
                    if(validMatar(t,c) && validVolar(t)){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }

        }
        else{
            return false;
        }

    }

    /** @brief Metode que escull una posició aleatoria amb una peça d'un equip.
     * @param equip Equip que ha de tenir la Peça trida.
     * @pre --
     * @post Retorna una Posició amb una Peça del equip demanat.
     */
    public Posicio escollirPosPeca(boolean equip){
        Random rand = new Random();
        Posicio p;
        int i1, i2;
        while(true){
            i1 = rand.nextInt(7)+1;
            i2 = rand.nextInt(7)+1;
            p = new Posicio(i1,i2);
            Peca peca = _tauler.get(p);
            if(peca != null && peca.get_equip()==equip){
                return p;
            }
        }
    }

    /** @brief Comprova si hi ha un jaque. Si hi ha jaque retorna la tirada que el fa
     * @param equip indica l'equip que vol mirar si esta fent ESCAC
     * @pre --
     * @post mira si hi ha escac o no
     */
    public TiradaSimple hihaJaque(boolean equip){
        Iterator<Map.Entry<Posicio, Peca>> it = _tauler.entrySet().iterator();
        Posicio posRei = buscaRei(equip);
        Peca pRei = _tauler.get(posRei);
        Posicio pos ;
        Peca p;
        boolean trobat = false;
        while(it.hasNext()){
            Map.Entry<Posicio, Peca> entry = it.next();
            pos = entry.getKey();
            p = entry.getValue();
            if(p.get_equip()!=pRei.get_equip()) {
                TiradaSimple tirada = new TiradaSimple(pos, posRei, equip);
                if (p.movimentValid(tirada)) {
                    if (validMatar(tirada, pRei) && validVolar(tirada)) {
                        return tirada;
                    }
                }
            }
        }
        return null;
    }

    /** @brief Comprova si hi ha un jaque mate. Si hi ha jaque mate retorna true
     * @param t tirada de fa jaque
     * @pre --
     * @post mira si hi ha escac i mat o no
     */
    public boolean hiHaJaqueMate(TiradaSimple t){
        Posicio pr = t.get_desti();
        Peca rei = _tauler.get(pr);
        if(!potTirarRei(t) && !potMatarPeça(t)) {
            if (t.get_volar() == 2 || t.get_volar() == 1) {
                return true;
            }else if(!potFicarseMig(t)){
                return true;
            }
        }
        return false;
    }

    /** @brief Comprova si hi ha alguna peça que es pot ficar entre mig del recorregut de la tirada de jaque
     * @param t tirada de fa jaque
     * @pre --
     * @post mira si alguna peça pot ficarse en mig la tirada o no
     */
    public boolean potFicarseMig(TiradaSimple t){
        int x, y;
        x = 0;
        y = 0;
        Posicio posOrigen = t.get_origen();
        int f = posOrigen.get_fila();
        int c = posOrigen.get_columna();
        while(x!= t.get_desplaçamentX() || y!= t.get_desplacamentY()){
            Posicio pos = new Posicio(f,c);
            Peca pec = _tauler.get(pos);

            if(!pos.equals(t.get_origen()) && !pos.equals(t.get_desti())){
                TiradaSimple tiradaSimple = posAmenaçada(pos,pec,t.get_equip(),"REI");
                if(tiradaSimple !=null){
                    return true;
                }
            }

            if(x!=t.get_desplaçamentX()){
                if(t.get_desplaçamentX()>0){
                    x++;
                    f++;
                }
                else{
                    x--;
                    f--;

                }

            }
            if(y!=t.get_desplacamentY()){
                if(t.get_desplacamentY() > 0){
                    y++;
                    c++;
                }
                else{
                    y--;
                    c--;
                }

            }
        }
        return false;
    }

    /** @brief Mira si es pot matar la peça que està amenaçan el rei
     * @param t tirada de fa jaque
     * @pre --
     * @post mira si se pot matar a la peça amenaçant o no
     */
    public boolean potMatarPeça(TiradaSimple t){
        Posicio p = t.get_origen();
        Peca peca = _tauler.get(p);
        TiradaSimple tiradaSimple = posAmenaçada(p,peca,t.get_equip(), "cuberta");
        return tiradaSimple != null;
    }

    /** @brief Mira si el rei pot tirar a algun lloc estant amençat
     * @param t tirada de fa jaque
     * @pre --
     * @post mira si el rei pot tirar o no
     */
    public boolean potTirarRei(TiradaSimple t){
        Posicio pr = t.get_desti();
        int x = -1;
        int y = -1;
        while(x!=2 || y!=-1){
            Posicio p = new Posicio(pr.get_fila()+x,pr.get_columna()+y);
            if((p.get_fila() > 0) && (p.get_fila() <= _fila) && (p.get_columna() > 0) && (p.get_columna() <= _columna)){
                Peca peca = _tauler.get(p);
                if(peca==null || peca.get_equip()!=_tauler.get(pr).get_equip()){
                    TiradaSimple tirada = posAmenaçada(p,peca,!t.get_equip(),"cuberta");
                    if(tirada==null){
                        return true;
                    }
                }
            }
            if(y == 1){
                x++;
                y=-1;
            }
            else{
                y++;
            }
        }
        return false;
    }

    /** @brief Comprova que les habilitats de matar de la peça amb les que vol fer a la tirada
     * @param t tirada que es vol fer
     * @param p peça que si existeix, es voldria matar
     * @pre t diferent de null
     * @post mira si es pot fer l'habilitat de matar que s'executa en la tirada
     */
    private boolean validMatar(TiradaSimple t, Peca p){
        if(t.get_matar() == 0 && p==null){
            return true;
        }
        else if(t.get_matar() == 1){
            return true;
        }
        else if(t.get_matar() == 2 && p!=null){
            return true;
        }
        else{
            return false;
        }
    }


    /** @brief Comprova que les habilitats de volar de la peça amb les que vol fer a la tirada
     * @param t tirada que es vol fer
     * @pre t diferent de null
     * @post mira si es pot fer l'habilitat de volar que s'executa en la tirada
     */
    private boolean validVolar(TiradaSimple t){
        if(t.get_volar() == 0 && !hiHaPecesEntremig(t)){
            return true;
        }
        else if(t.get_volar() == 1 || t.get_volar()==2){
            return true;
        }
        else{
            return false;
        }
    }

    /** @brief Comprova si hi ha peces entremig de la tirada que es vol executar
     * @param t tirada que es vol fer
     * @pre t diferent de null
     * @post mira si hi ha peces entremig de t o no
     */
    public boolean hiHaPecesEntremig(TiradaSimple t){
        int x, y;
        x = 0;
        y = 0;
        Posicio posOrigen = t.get_origen();
        int f = posOrigen.get_fila();
        int c = posOrigen.get_columna();
        while(x!= t.get_desplaçamentX() || y!= t.get_desplacamentY()){
            Posicio pos = new Posicio(f,c);
            Peca pec = _tauler.get(pos);
            if(pec!=null){
                if(!pos.equals(t.get_origen()) && !pos.equals(t.get_desti())){
                    return true;
                }
            }
            if(x!=t.get_desplaçamentX()){
                if(t.get_desplaçamentX()>0){
                    x++;
                    f++;
                }
                else{
                    x--;
                    f--;

                }

            }
            if(y!=t.get_desplacamentY()){
                if(t.get_desplacamentY() > 0){
                    y++;
                    c++;
                }
                else{
                    y--;
                    c--;
                }

            }
        }
        return false;
    }

    /** @brief Realitza la tirada t al taulell. Retorna un enter que s'incrementara si es maten peces enemigues.
     * @param t tirada que es vol fer
     * @pre t diferent de null
     * @post tirada feta i taulell modificat
     */
    public int realitzarTirada(TiradaSimple t){
        TreeMap<Posicio,Peca> eli = new TreeMap<>();
        int res = 1;
        Peca p = _tauler.get(t.get_origen());
        Peca d = _tauler.get(t.get_desti());
        if(d!=null){
            res ++;
            eli.put(t.get_desti(),d);
        }
        if(t.get_volar() == 2){
            res = res + eliminarPecesEntremig(t, eli);
        }
        _tauler.put(t.get_desti(),p);
        _tauler.remove(t.get_origen());
        p.primerMovFet();
        p.incrementarMov();
        _eliminats.put(nTorns,eli);
        ++nTorns;

        return  res;
    }


    /** @brief Comprova si hi ha peces entremig de la tirada i si n'hi ha les mata
     * @param t tirada que es vol fer
     * @param eli map on es guardaran les peces mortes i la posicio on han mort
     * @pre t diferent de null
     * @post mira si hi ha peces entremig de t o no i les mata si n'hi ha
     */
    private int eliminarPecesEntremig(TiradaSimple t, TreeMap<Posicio, Peca> eli){
        int x, y, res, f, c;
        res = 0;
        x = 0;
        y = 0;
        Posicio posOrigen = t.get_origen();
        f = posOrigen.get_fila();
        c = posOrigen.get_columna();
        while(x!= t.get_desplaçamentX() || y!= t.get_desplacamentY()) {
            Posicio pos = new Posicio(f, c);
            Peca pec = _tauler.get(pos);
            if (pec != null) {
                if(!pos.equals(t.get_origen()) && !pos.equals(t.get_desti()) && pec.get_equip()!=t.get_equip()) { //si troba un peça que no sigui ni desti ni origen
                    eli.put(pos, pec);
                    _tauler.remove(pos);
                    res++;
                }
            }
            if (x != t.get_desplaçamentX()) {
                if (t.get_desplaçamentX() > 0) {
                    x++;
                    f++;
                } else {
                    x--;
                    f--;
                }
            }
            if (y != t.get_desplacamentY()) {
                if (t.get_desplacamentY() > 0) {
                    y++;
                    c++;
                } else {
                    y--;
                    c--;
                }
            }
        }
        return res;
    }

    /** @brief Busca al rei del equip "equip" i retorna la seva posicio.
     * @param equip boolea que indica l'equip del rei buscat
     * @pre cert
     * @post posicio del rei trobada
     */
    public Posicio buscaRei(boolean equip){
        Iterator<Map.Entry<Posicio, Peca>> it = _tauler.entrySet().iterator();
        boolean trobat = false;
        Posicio pos = null;
        Peca p = null;
        while(it.hasNext() && !trobat){
            Map.Entry<Posicio, Peca> entry = it.next();
            pos = entry.getKey();
            p = entry.getValue();
            if(p.esRei(equip)){
                trobat = true;
            }
        }
        return pos;
    }

    /** @brief Llista de Peces eliminades*/
    public TreeMap<Posicio,Peca> getEliminats(){
        return _eliminats.get(_eliminats.size());
    }

    /** @brief Fa totes les comprovacions per veure si l'enroc entrat es valid o no
     * @param e enroc que es vol validar
     * @pre e diferent de null
     * @post mira si l'enroc es valid o no
     */
    public boolean validarEnrroc(Enrroc e){
        Posicio a = e.get_p1();
        Posicio b = e.get_p2();
        Peca p = _tauler.get(a);
        Peca p2 = _tauler.get(b);
        if(p != null && p.get_equip() == e.get_equip() && p2 != null && p2.get_equip() == e.get_equip() && a.get_fila() == b.get_fila()){
            if(p.enrrocValid(e, p2)){

                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    /** @brief Comprova si a la posicio entrada hi ha un peça al taulell
     * @param p posicio que es vol mirar si conte un peça
     * @pre cert
     * @post mira si el taulell te una peça a la posicio p
     */
    public boolean contePeçaCasella(Posicio p){
        Peca peca = _tauler.get(p);
        return peca !=  null;
    }

    /** @brief retorna la peça que hi ha a la posicio p
     * @param p posicio on esta la peça que es vol retornar
     * @pre cert
     * @post retorna la peça del taulell que esta a la poscio p
     */
    public Peca getPeca(Posicio p) {
        return _tauler.get(p);
    }

    /** @brief Comprova si a la posicio entrada es pot fer una promocio
     * @param p posicio que es vol mirar si es pot fer la promocio
     * @param _equip equip de la peça que vols fer promocio
     * @pre cert
     * @post mira si es pot fer promocio o no
     */
    public boolean hiHaPromocio(Posicio p, boolean _equip){
        if(p.get_fila() == _columna && _equip == true && _tauler.get(p).getPromocio()==true){ //si esta dalt de tot  i es una peça blanca i pot fer promocio
            return true;
        }
        else if(p.get_fila() == 1 && _equip == false && _tauler.get(p).getPromocio()==true){ //si esta baix de tot i es una peça negra i pot fer promocio
            return true;
        }
        else{
            return false;
        }
    }

    /** @brief canvia la peça que hi ha a la posicio pos per la peça pec
     * @param pec nova peça que es ficara al taulell
     * @param pos posicio on se canviara la peça
     * @pre peça i pos existeixen
     * @post promocio feta
     */
    public void realitzarPromocio(Posicio pos, Peca pec){
        _tauler.put(pos,pec);
        _promocio.add(pec);
    }

    /** @brief boolea que et diu si el _tiradesRefer esta buit o no
     * @pre cert
     * @post mira si _tiradesRefer esta buit o no
     */
    public boolean estaBuidaRefer(){
        return _tiradesRefer.isEmpty();
    }

    /** @brief Carrega les tirades realitzades en una partida anterior
     * @param t tirada a carregar
     * @param resultat resultat de la partida a carregar
     * @param mapTipus estructura on es guarden els tipus de peces
     * @pre --
     * @post Refa les tirades a partir d'una partida anterior per a que es pugui seguir.
     */
    public void carregarTirades(TiradaSimple t, String resultat, TreeMap<String,TipusPeca> mapTipus, String Torn) {
        Peca p = _tauler.get(t.get_origen());
        if (!resultat.isEmpty()) {
            StringTokenizer defaultTokenizer = new StringTokenizer(resultat);
            String s = defaultTokenizer.nextToken();
            if (s.equalsIgnoreCase("PROMOCIÓ:")) {
                boolean equip = false;
                if(Torn.equalsIgnoreCase("BLANQUES")) equip = true;
                String vella = defaultTokenizer.nextToken();
                defaultTokenizer.nextToken();
                String nova = defaultTokenizer.nextToken();
                Peca v = new Peca(vella, t.get_equip(), mapTipus);
                Peca n = new Peca(nova, equip, mapTipus);
                realitzarTirada(t);
                _tauler.put(t.get_desti(), n);

            } else if (s.equalsIgnoreCase("ENROC:")) {
                Posicio p1origen = new Posicio(defaultTokenizer.nextToken());
                Posicio p2origen = new Posicio(defaultTokenizer.nextToken());
                String guio = defaultTokenizer.nextToken();
                Posicio p1desti = new Posicio(defaultTokenizer.nextToken());
                Posicio p2desti = new Posicio(defaultTokenizer.nextToken());
                TiradaSimple t1 = new TiradaSimple(p1origen, p1desti, t.get_equip(), 0, 1);
                TiradaSimple t2 = new TiradaSimple(p2origen, p2desti, t.get_equip(), 0,1);
                realitzarTirada(t1);
                realitzarTirada(t2);
            } else {
                realitzarTirada(t);
            }
        } else {
            realitzarTirada(t);
        }

    }

    /** @brief desfa la tirada que li entren
     * @param t tirada a desfer
     * @param resultat resultat de la partida a refer
     * @param mapTipus estructura on es guarden els tipus de peces
     * @pre --
     * @post tirada desfeta
     */
    public TiradaSimple desferTirada(TiradaSimple t, String resultat, TreeMap<String,TipusPeca> mapTipus){
        _eliminats.remove(nTorns);
        --nTorns;
        Peca p = _tauler.get(t.get_desti());
        boolean enroc = false;
        if(!resultat.isEmpty()){
            StringTokenizer defaultTokenizer = new StringTokenizer(resultat);
            String s = defaultTokenizer.nextToken();
            if(s.equalsIgnoreCase("PROMOCIÓ:")){
                String vella = defaultTokenizer.nextToken();
                defaultTokenizer.nextToken();
                String nova = defaultTokenizer.nextToken();
                Peca v = new Peca(vella,t.get_equip(),mapTipus);
                Peca n = new Peca(nova,t.get_equip(),mapTipus);
                _tauler.put(t.get_origen(),v);
                _tauler.remove(t.get_desti());
                _promocio.add(n);
                v.decrementarMov();
                if(p.getNMovs()==1){
                    p.restaurarMov();
                }

            } else if (s.equalsIgnoreCase("ENROC:")){
                enroc = true;
                Posicio p1origen = new Posicio(defaultTokenizer.nextToken());
                Posicio p2origen = new Posicio(defaultTokenizer.nextToken());
                String guio = defaultTokenizer.nextToken();
                Posicio p1desti = new Posicio(defaultTokenizer.nextToken());
                Posicio p2desti = new Posicio(defaultTokenizer.nextToken());
                TiradaSimple t1 = new TiradaSimple(p1desti,p1origen,t.get_equip());
                TiradaSimple t2 = new TiradaSimple(p2desti,p2origen,t.get_equip());
                realitzarTirada(t1);
                realitzarTirada(t2);
                nTorns = nTorns-3;
                _tauler.get(p1origen).decrementarMov();
                _tauler.get(p1origen).decrementarMov();
                _tauler.get(p2origen).decrementarMov();
                _tauler.get(p2origen).decrementarMov();
                if(_tauler.get(p1origen).getNMovs()==0){
                    _tauler.get(p1origen).restaurarMov();
                }
                if(_tauler.get(p2origen).getNMovs()==0){
                    _tauler.get(p2origen).restaurarMov();
                }
            }
            else {
                _tauler.put(t.get_origen(), p);
                _tauler.remove(t.get_desti());
                p.decrementarMov();
                if(p.getNMovs()==0){
                    p.restaurarMov();
                }
            }
        }
        else {
            _tauler.put(t.get_origen(),p);
            _tauler.remove(t.get_desti());
            p.decrementarMov();
            if(p.getNMovs()==0){
                p.restaurarMov();
            }
        }


        if(enroc == false) {
            TreeMap<Posicio, Peca> eli = _eliminats.get(nTorns);
            Iterator<Map.Entry<Posicio, Peca>> it = eli.entrySet().iterator();
            while (it.hasNext()) { //fico les peces mortes (si hi havia) de la tirada anterior

                Map.Entry<Posicio, Peca> entry = it.next();
                Posicio pos = entry.getKey();
                Peca peca = entry.getValue();
                assignarPecaTauler(peca, pos);
            }
        }
        _tiradesRefer.add(t);
        return new TiradaSimple(t.get_desti(),t.get_origen(),t.get_equip());
    }


    /** @brief reinicia l'array de tirades que s'han desfet
     * @pre --
     * @post tiradesRefer reiniciat
     */
    public void reiniciaTiradesRefer(){
        _tiradesRefer = new ArrayList<>();
    }

    /** @brief refem l'ultima jugada desfeta i guardem el resultat
     * @param resultat resultat del refer que farem
     * @pre s'ha fet  minim un desfer previament
     * @post tirada refeta
     */
    public TiradaSimple referTirada(StringBuilder resultat){
        TiradaSimple t = _tiradesRefer.get(_tiradesRefer.size()-1);
        t.guardarMatar(1);
        t.guardarVolar(2);
        _tiradesRefer.remove(_tiradesRefer.size()-1);
        Peca v = _tauler.get(t.get_origen());
        Peca d = _tauler.get(t.get_desti());
        if(d != null && v.get_equip() == d.get_equip()){
            Enrroc e = new Enrroc(t.get_origen(), t.get_desti(), t.get_equip(), false, false);
            resultat.append(e.realitzarEnroc(this));
        }
        else{
            realitzarTirada(t);
            if(hiHaPromocio(t.get_desti(),t.get_equip())){
                Peca n = _promocio.get(_promocio.size()-1);
                _promocio.remove(_promocio.size()-1);
                _tauler.put(t.get_desti(),n);
                resultat.append("PROMOCIÓ: " + v.getNom() + " - " + n.getNom());
            }
            else{
                TiradaSimple t2 = hihaJaque(t.get_equip());
                if(t2 != null){
                    resultat.append("ESCAC");
                }
                else{
                    resultat.append("");
                }
            }
        }
        return t;
    }

    /** @brief Mira si les peces del equip contrari estan amenaçades
     * @param equip Blanc o Negre
     @pre --
     @post moviments retorna la Tirada que s'ha de fer per a matar la Peça amenaçada
     */
    public TiradaSimple hiHaAlgunaAmenaça(boolean equip){
        Iterator<Map.Entry<Posicio, Peca>> it = _tauler.entrySet().iterator();
        Posicio pos = null;
        Peca p = null;
        while(it.hasNext()){
            Map.Entry<Posicio, Peca> entry = it.next();
            pos = entry.getKey();
            p = entry.getValue();
            if(p.get_equip()==equip){
                TiradaSimple t = posAmenaçada(pos,p,equip," ");
                if(t!=null){
                    return  t;
                }
            }
        }
        return null;
    }

    /** @brief Mira si la posició esta amenaçada una posició a partir d'una Peça
     * @param p Posició observada
     * @param pObservada Peça Observada
     * @param equip Blanc o Negre
     @pre Posició dins el Taulell, Peça != null
     @post Retorna la Tirada que amenaça a la Peça
     */
    public TiradaSimple posAmenaçada(Posicio p, Peca pObservada, boolean equip, String rei) {
        Iterator<Map.Entry<Posicio, Peca>> it = _tauler.entrySet().iterator();
        Posicio pos;
        Peca peca;
        while (it.hasNext()) {
            Map.Entry<Posicio, Peca> entry = it.next();
            pos = entry.getKey();
            peca = entry.getValue();
            if (peca.get_equip() != equip) {
                TiradaSimple tirada = new TiradaSimple(pos, p, equip);
                if (peca.movimentValid(tirada)) {
                    if (validMatar(tirada, pObservada) && validVolar(tirada)) {
                        if (rei.equals("cuberta") && peca.getNom().equals("REI")) { //si el rei es el que amenaça miro si la peça que amenaça esta sent cubrida
                            TiradaSimple t2 = posAmenaçada(p, _tauler.get(tirada.get_origen()), !tirada.get_equip(), " ");
                            if (t2 == null) {
                                return tirada;
                            }
                        } else if (!peca.getNom().equals(rei)) { //el rei no es pot ficar entremig dun jaque
                            return tirada;
                        }
                    }
                }
            }
        }
        return null;
    }

    /** @brief mostra el taulell amb les seves peces
     * @pre taulell creat
     * @post mostra el taulell
     */
    public String mostra() {
        String s = "";
        Character x = 'a';
        s = s + " ";
        for(int j=0; j< _columna; j++){
            s = s + " " + x;
            x++;
        }
        s = s + "\n";
        for(int i = _fila; i>0; i--){
            s = s + " ";
            for(int j=0; j< _columna; j++){
                s = s + " " + "-";
            }
            s = s + "\n";
            s = s + i;
            s = s + "|";
            for(int j=1; j<= _columna; j++){

                Posicio p = new Posicio(i,j);
                if(contePeçaCasella(p)){
                    s = s + _tauler.get(p).toString();
                }
                else{
                    s = s + " ";
                }
                s = s + "|";
            }
            s = s + "\n";

        }
        s = s + " ";
        for(int j=0; j< _columna; j++){
            s = s + " " + "-";
        }

        s = s + "\n";
        return s;
    }

}
