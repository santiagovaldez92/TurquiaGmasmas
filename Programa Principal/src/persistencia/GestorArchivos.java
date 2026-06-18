package persistencia;

import modelos.Estudiante;
import modelos.Materia;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {
    private static final String RUTA_ESTUDIANTES = "estudiantes.txt";
    private static final String RUTA_MATERIAS = "materias.txt";

    // --- MÉTODOS PARA ESTUDIANTES ---

    public static void guardarEstudiantes(List<Estudiante> estudiantes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ESTUDIANTES))) {
            for (Estudiante est : estudiantes) {
                bw.write(est.toLineaTxt());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar estudiantes: " + e.getMessage());
        }
    }

    public static List<Estudiante> leerEstudiantes() {
        List<Estudiante> lista = new ArrayList<>();
        File archivo = new File(RUTA_ESTUDIANTES);
        if (!archivo.exists())
            return lista; // Si no existe, retorna lista vacía

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 4) { // id;nombre;password;carrera
                    lista.add(new Estudiante(datos[0], datos[1], datos[2], datos[3]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer estudiantes: " + e.getMessage());
        }
        return lista;
    }

    // --- MÉTODOS PARA MATERIAS ---

    public static void guardarMaterias(List<Materia> materias) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_MATERIAS))) {
            for (Materia mat : materias) {
                bw.write(mat.toLineaTxt());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar materias: " + e.getMessage());
        }
    }

    public static List<Materia> leerMaterias() {
        List<Materia> lista = new ArrayList<>();
        File archivo = new File(RUTA_MATERIAS);
        if (!archivo.exists())
            return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 3) { // codigo;nombre;creditos
                    lista.add(new Materia(datos[0], datos[1], Integer.parseInt(datos[2])));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer materias: " + e.getMessage());
        }
        return lista;
    }
}