package turquia.model;

public class Usuario {
    private String nombre;
    private String username;
    private String password;
    private String rol;

    public Usuario(String nombre, String username, String password, String rol) {
        this.nombre = nombre;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public String getNombre()   { return nombre; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRol()      { return rol; }
}
