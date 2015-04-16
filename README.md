Funktionsbibliothek Datenkatalog [![Build Status](https://travis-ci.org/falkoschumann/datenverteiler-datenkatalog.svg?branch=develop)](https://travis-ci.org/falkoschumann/datenverteiler-datenkatalog)
================================

Die Funktionsbibliothek Datenkatalog soll die Arbeit mit dem Datenkatalog der
BSVRZ-Software vereinfachen und zusätzliche Werkzeuge dafür anbieten.

Die Projektdokumentation befindet sich unter
http://falkoschumann.github.io/datenverteiler-datenkatalog/


Enthaltene Funktionen
---------------------

  - Nutzung von POJOs (Plain old Java Objects) anstelle der generischen API der
    Datenverteiler-Applikationsfunktionen für den Zugriff auf die Attribute von
    Attributgruppen.


Geplante Funktionen
-------------------

  - Value-Objekte für Ganzzahl und Kommazahl abgeleitet von `java.lang.Number`
    sowie für Zeitstempel verwenden. 
  - Erweiterung des POJO-Ansatzes auf die Nutzung Systemobjekte mit
    Zugriffsmethoden für Mengen und Konfigurationsdaten, Parameter und
    Onlinedaten.
  - Metamodell für den generischen Zugriff auf den Datenkatalog sowie für die
    Nutzung durch die folgenden Generatoren.
  - Generator für die POJO-Klassen von Attributgruppen und Systemobjekten.
  - Generator für einen HTML-Datenkatalog.
  - Generator für Diagramme mit den Beziehungen zwischen Objekten (Referenzen in
    Mengen und Objektreferenzen in Attributgruppen).
  - Generator für Abhängigkeitsdiagramm der Konfigurationsbereiche.


---

Dieses Projekt ist nicht Teil des NERZ e.V. Die offizielle Software sowie
weitere Informationen zur bundeseinheitlichen Software für
Verkehrsrechnerzentralen (BSVRZ) finden Sie unter http://www.nerz-ev.de.
