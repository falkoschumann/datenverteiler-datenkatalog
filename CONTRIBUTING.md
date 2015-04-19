Wie Sie mithelfen können
========================

Mängel, Fehler und fehlende Funktionen können Sie im [Issue Tracker][issues]
berichten. Gerne auch mit [Pull Request][pulls] für einen Lösungsvorschlag.


Distributionspaket erzeugen
---------------------------

**Vorraussetzung:** Java 8 und Maven 3 sind installiert.

Das Projekt von https://github.com/falkoschumann/datenverteiler-datenkatalog.git
klonen und die folgenden beiden Befehle auf der Konsole ausführen: 

    mvn install
    mvn assembly:single


Hinweise
--------

  - Textbausteine die in Templates verwendet werden, müssen vorformatiert
    werden. Das gilt unter anderen für die Projektbeschreibung im `pom.xml` und
    Actions in `changes.xml`. Zur Vorformatierung gehört vor allem die
    Begrenzung der Zeilenlänge auf 80 Zeichen.


[issues]: https://github.com/falkoschumann/datenverteiler-datenkatalog/issues
[pulls]: https://github.com/falkoschumann/datenverteiler-datenkatalog/pulls
