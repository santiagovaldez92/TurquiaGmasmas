@echo off
echo ========================================
echo   TURQUIA - Compilar y Ejecutar
echo ========================================

REM Crear carpeta de clases
if not exist "classes" mkdir classes

REM Compilar todos los .java
echo Compilando...
javac -cp src -d classes src\turquia\Main.java src\turquia\model\*.java src\turquia\util\*.java src\turquia\ui\*.java

if %ERRORLEVEL% NEQ 0 (
    echo ERROR al compilar. Asegurate de tener JDK instalado.
    pause
    exit /b
)

echo Compilacion exitosa!
echo Iniciando aplicacion...
java -cp classes turquia.Main
pause
