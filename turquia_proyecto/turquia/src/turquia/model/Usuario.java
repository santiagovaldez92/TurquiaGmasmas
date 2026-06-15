package turquia.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int id;
    private String nombre;
    private String username;
    private String password;
    private Rol rol;

    // Solo aplica si rol == ESTUDIANTE: ids de materias inscritas
    private List<Integer> materiasInscritas;

    // Solo aplica si rol == DOCENTE: ids de materias que imparte
    private List<Integer> materiasImpartidas;

    public Usuario(int id, String nombre, String username, String password, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.materiasInscritas = new ArrayList<>();
        this.materiasImpartidas = new ArrayList<>();
    }

    public int getId()              { return id; }
    public String getNombre()       { return nombre; }
    public String getUsername()     { return username; }
    public String getPassword()     { return password; }
    public Rol getRol()             { return rol; }

    public void setNombre(String nombre)     { this.nombre = nombre; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRol(Rol rol)               { this.rol = rol; }

    public List<Integer> getMateriasInscritas()  { return materiasInscritas; }
    public List<Integer> getMateriasImpartidas() { return materiasImpartidas; }

    // Compatibilidad con código previo: getRol().getEtiqueta()
    public String getRolEtiqueta() {
        return rol.getEtiqueta();
    }
}
