# Projecte de Programació (GEINF - UdG)

## Primavera 2020

> Trobareu l'enunciat i la normativa al [Moodle](https://moodle2.udg.edu/course/view.php?id=23855)

**Directoris**

- [src](src) conté el codi font
- [jar](jar) conté el fitxer JAR de l'aplicació
- [doc](doc) conté la documentació demanada en format PDF (diagrama de
  classes, proves efectuades i manual d'usuari)
- [doc/html](doc/html) conté la documentació en format html generada amb [Doxygen](http://www.doxygen.nl/)


## Dubtes Actuals
- Si el tauler té una amplada imparell, i el rei està col·locat a la casella del mig, a quina de les torres es podrà fer l'enrroc?
- Quan es vol continuar una partida guardada, per carregar-la, tenim un mètode a partida per fer-ho o creem un mòdul nou que llegeixi i que retorni la partida?
- Fa falta el mòdul TipusPeça? Perquè al final es podria fer que el mòdul Partida, tingués una estructura de dades amb tots els tipus de Peces i després a l'hora de muntar el tauler busquem la Peça que s'hagi de posar a cada posició i creem una nova instància copiada de la Peça corresponent. D'aquesta manera tampoc faria falta el mòdul TipusPeça i cada tipus de Peça es diferenciaria pel seu nom (per exemple).
