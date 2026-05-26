@echo off
cd /d "%~dp0"
title Space Invaders - Jogar
cls
echo ===================================================
echo        INICIANDO SPACE INVADERS - V4 PLUS
echo ===================================================
echo.

echo Realizando limpeza de compilacoes antigas (.class)...
del /q *.class 2>nul
echo.

rem Tentativa 1: Microsoft JDK
if exist "C:\Program Files\Microsoft\jdk-21.0.11.10-hotspot\bin\javac.exe" (
    echo Usando o Microsoft JDK para compilar...
    "C:\Program Files\Microsoft\jdk-21.0.11.10-hotspot\bin\javac.exe" --release 8 Main_I.java Jogo_II.java CampoBatalha_III.java Nave_IV.java Ordenadores_V.java EstatisticaAlgoritmo_VI.java >nul 2>nul
    if errorlevel 1 (
        "C:\Program Files\Microsoft\jdk-21.0.11.10-hotspot\bin\javac.exe" Main_I.java Jogo_II.java CampoBatalha_III.java Nave_IV.java Ordenadores_V.java EstatisticaAlgoritmo_VI.java
    )
    if not errorlevel 1 (
        echo Compilacao bem-sucedida! Iniciando o jogo...
        "C:\Program Files\Microsoft\jdk-21.0.11.10-hotspot\bin\java.exe" Main_I
        goto end
    )
)

rem Tentativa 2: IntelliJ JBR
if exist "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.1.1.1\jbr\bin\javac.exe" (
    echo Usando o JetBrains Runtime para compilar...
    "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.1.1.1\jbr\bin\javac.exe" --release 8 Main_I.java Jogo_II.java CampoBatalha_III.java Nave_IV.java Ordenadores_V.java EstatisticaAlgoritmo_VI.java >nul 2>nul
    if errorlevel 1 (
        "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.1.1.1\jbr\bin\javac.exe" Main_I.java Jogo_II.java CampoBatalha_III.java Nave_IV.java Ordenadores_V.java EstatisticaAlgoritmo_VI.java
    )
    if not errorlevel 1 (
        echo Compilacao bem-sucedida! Iniciando o jogo...
        "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.1.1.1\jbr\bin\java.exe" Main_I
        goto end
    )
)

rem Tentativa 3: Java global no PATH
where javac >nul 2>nul
if not errorlevel 1 (
    echo Usando o Java global do sistema para compilar...
    javac --release 8 Main_I.java Jogo_II.java CampoBatalha_III.java Nave_IV.java Ordenadores_V.java EstatisticaAlgoritmo_VI.java >nul 2>nul
    if errorlevel 1 (
        javac Main_I.java Jogo_II.java CampoBatalha_III.java Nave_IV.java Ordenadores_V.java EstatisticaAlgoritmo_VI.java
    )
    if not errorlevel 1 (
        echo Compilacao bem-sucedida! Iniciando o jogo...
        java Main_I
        goto end
    )
)

echo.
echo [ERRO] Nao conseguimos compilar o jogo!
echo Verifique se o JDK do Java esta instalado corretamente no computador.
pause

:end
pause
