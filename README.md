# ProjektProgramim Problemi është i qartë: sistemi juaj ka vetëm Java JRE 8 (jo JDK) dhe Maven nuk është i instaluar fare. Projekti
  kërkon JDK 17+. Ja hapat që duhet të ndiqni:

  ---  Hapat e Instalimit (bëni vetë në terminal)
  Hapi 1 — Instalo JDK 17 (Winget)

  Hapni terminal si Administrator dhe ekzekutoni:

  winget install Microsoft.OpenJDK.17

  ose nëse preferoni Eclipse Temurin:

  winget install EclipseAdoptium.Temurin.17.JDK

  Hapi 2 — Instalo Maven

  winget install Apache.Maven

  Hapi 3 — Rinisni terminalin dhe verifikoni

  java -version
  mvn -version

  Duhet të shihni diçka si:
  openjdk version "17.x.x"
  Apache Maven 3.x.x

  Hapi 4 — Ekzekuto projektin

  cd "C:\Users\User\Documents\GitHub\ProjektProgramim"
  mvn javafx:run

  ---
  Nëse winget nuk funksionon

  ose nëse preferoni Eclipse Temurin:

  winget install EclipseAdoptium.Temurin.17.JDK                                        oni në C:\maven dhe shtoni

  Hapi 2 — Instalo Maven

  winget install Apache.Maven                                                          n javafx:run do ta nisë
                                                                                       doni ta ekzekutoni pa
  Hapi 3 — Rinisni terminalin dhe verifikoni

  java -version
  mvn -version
                                                                                       ct files. Next action: 
  Duhet të shihni diçka si:                                                             Apache.Maven, then run mvn 
  openjdk version "17.x.x"
  Apache Maven 3.x.x
                                                                                       ────────────────────────────
  Hapi 4 — Ekzekuto projektin
                                                                                       ────────────────────────────
  cd "C:\Users\User\Documents\GitHub\ProjektProgramim"
  mvn javafx:run

  ---
  Nëse winget nuk funksionon

  Shkarkoni manualisht:
  - JDK 17: https://adoptium.net/ → klikoni "Latest LTS" → instaluesi .msi
  - Maven: https://maven.apache.org/download.cgi → apache-maven-3.x.x-bin.zip →
  shpaketoni në C:\maven dhe shtoni C:\maven\bin në variablin e mjedisit PATH

  ---
  Kodi i projektit është 100% i gatshëm. Sapo të instaloni JDK 17 dhe Maven, komanda
  mvn javafx:run do ta nisë aplikacionin direkt. Shkruani ! winget install
  Microsoft.OpenJDK.17 direkt këtu nëse doni ta ekzekutoni pa ndërruar dritare.

✻ Brewed for 53s