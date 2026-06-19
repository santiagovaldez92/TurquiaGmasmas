
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import turquia.ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        // 1. Intentar aplicar el tema Nimbus para que Swing se vea moderno
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si Nimbus no está disponible, se usa el tema del sistema operativo
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // 2. Iniciar la aplicación abriendo primero el Login de forma segura
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}