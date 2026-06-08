package turquia.util;

import turquia.model.Usuario;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private static final List<Usuario> usuarios = new ArrayList<>();

    static {
        // Usuarios de prueba
        usuarios.add(new Usuario("Diogo Rojas Guerra",  "diogo",  "1234",   "Estudiante"));
        usuarios.add(new Usuario("María Fernández",     "maria",  "admin",  "Administrador"));
        usuarios.add(new Usuario("Carlos Velasco",      "carlos", "profe",  "Docente"));
    }

    public static Usuario login(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equalsIgnoreCase(username)
                    && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}
