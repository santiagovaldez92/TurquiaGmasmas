#!/bin/bash
echo "========================================"
echo "  TURQUIA - Compilar y Ejecutar"
echo "========================================"

mkdir -p classes

echo "Compilando..."
javac -cp src -d classes $(find src -name "*.java")

if [ $? -ne 0 ]; then
    echo "ERROR al compilar. Asegurate de tener JDK instalado."
    exit 1
fi

echo "Compilacion exitosa!"
echo "Iniciando aplicacion..."
java -cp classes turquia.Main
