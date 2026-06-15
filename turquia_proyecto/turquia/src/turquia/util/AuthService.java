package turquia.util;

import turquia.model.Usuario;

public class AuthService {

    public static Usuario login(String username, String password) {
        return DataStore.getInstance().buscarPorUsername(username, password);
    }
}
