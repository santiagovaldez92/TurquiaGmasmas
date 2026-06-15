package turquia.util;

import turquia.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Almacén central de datos en memoria (Singleton).
 * Mantiene usuarios, materias, notas y reportes durante la ejecución.
 */
public class DataStore {

    private static DataStore instance;

    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Materia> materias = new ArrayList<>();
    private final List<Nota> notas = new ArrayList<>();
    private final List<Reporte> reportes = new ArrayList<>();

    private int nextUserId = 1;

    private DataStore() {
        inicializarDatos();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void inicializarDatos() {
        // ── Materias base ──────────────────────────────────────────────
        materias.add(new Materia(1, "Programación",                "PRG"));
        materias.add(new Materia(2, "Estructura de Datos",         "EDD"));
        materias.add(new Materia(3, "Cálculo",                     "CAL"));
        materias.add(new Materia(4, "Álgebra",                     "ALG"));
        materias.add(new Materia(5, "Arquitectura de Computadoras",  "ARQ"));
        materias.add(new Materia(6, "Investigación Operativa",      "INV"));
        materias.add(new Materia(7, "Inglés",                       "ING"));

        // ── Usuarios base ──────────────────────────────────────────────
        usuarios.add(new Usuario(nextUserId++, "Diogo Rojas Guerra", "diogo",  "1234",  Rol.ESTUDIANTE));
        usuarios.add(new Usuario(nextUserId++, "María Fernández",    "maria",  "admin", Rol.ADMINISTRADOR));
        usuarios.add(new Usuario(nextUserId++, "Carlos Velasco",     "carlos", "profe", Rol.DOCENTE));

        // Algunos estudiantes y docentes adicionales para que la lista no esté vacía
        usuarios.add(new Usuario(nextUserId++, "Ana Quispe Mamani",   "ana",    "1234", Rol.ESTUDIANTE));
        usuarios.add(new Usuario(nextUserId++, "Luis Paredes Cruz",   "luis",   "1234", Rol.ESTUDIANTE));
        usuarios.add(new Usuario(nextUserId++, "Patricia Mendoza",    "patricia","profe", Rol.DOCENTE));
    }

    // ── Usuarios ─────────────────────────────────────────────────────────
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Usuario> getEstudiantes() {
        List<Usuario> r = new ArrayList<>();
        for (Usuario u : usuarios) if (u.getRol() == Rol.ESTUDIANTE) r.add(u);
        return r;
    }

    public List<Usuario> getDocentes() {
        List<Usuario> r = new ArrayList<>();
        for (Usuario u : usuarios) if (u.getRol() == Rol.DOCENTE) r.add(u);
        return r;
    }

    public Usuario buscarPorUsername(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public Usuario buscarPorId(int id) {
        for (Usuario u : usuarios) if (u.getId() == id) return u;
        return null;
    }

    public boolean existeUsername(String username) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equalsIgnoreCase(username)) return true;
        }
        return false;
    }

    public Usuario crearUsuario(String nombre, String username, String password, Rol rol) {
        Usuario u = new Usuario(nextUserId++, nombre, username, password, rol);
        usuarios.add(u);
        return u;
    }

    public void eliminarUsuario(int id) {
        usuarios.removeIf(u -> u.getId() == id);
        notas.removeIf(n -> n.getIdEstudiante() == id);
    }

    // ── Materias ─────────────────────────────────────────────────────────
    public List<Materia> getMaterias() {
        return materias;
    }

    public Materia buscarMateria(int id) {
        for (Materia m : materias) if (m.getId() == id) return m;
        return null;
    }

    // ── Notas ────────────────────────────────────────────────────────────
    public List<Nota> getNotas() {
        return notas;
    }

    public Nota buscarNota(int idEstudiante, int idMateria) {
        for (Nota n : notas) {
            if (n.getIdEstudiante() == idEstudiante && n.getIdMateria() == idMateria) return n;
        }
        return null;
    }

    public void asignarNota(int idEstudiante, int idMateria, double valor) {
        Nota existente = buscarNota(idEstudiante, idMateria);
        if (existente != null) {
            existente.setValor(valor);
        } else {
            notas.add(new Nota(idEstudiante, idMateria, valor));
        }
    }

    // ── Asignación de materias ──────────────────────────────────────────
    public void asignarMateriaAEstudiante(int idEstudiante, int idMateria) {
        Usuario u = buscarPorId(idEstudiante);
        if (u != null && !u.getMateriasInscritas().contains(idMateria)) {
            u.getMateriasInscritas().add(idMateria);
        }
    }

    public void quitarMateriaAEstudiante(int idEstudiante, int idMateria) {
        Usuario u = buscarPorId(idEstudiante);
        if (u != null) {
            u.getMateriasInscritas().remove(Integer.valueOf(idMateria));
        }
        notas.removeIf(n -> n.getIdEstudiante() == idEstudiante && n.getIdMateria() == idMateria);
    }

    public void asignarMateriaADocente(int idDocente, int idMateria) {
        Usuario u = buscarPorId(idDocente);
        if (u != null && !u.getMateriasImpartidas().contains(idMateria)) {
            u.getMateriasImpartidas().add(idMateria);
        }
    }

    public void quitarMateriaADocente(int idDocente, int idMateria) {
        Usuario u = buscarPorId(idDocente);
        if (u != null) {
            u.getMateriasImpartidas().remove(Integer.valueOf(idMateria));
        }
    }

    // ── Reportes ─────────────────────────────────────────────────────────
    public List<Reporte> getReportes() {
        return reportes;
    }

    public void agregarReporte(Reporte r) {
        reportes.add(r);
    }
}
