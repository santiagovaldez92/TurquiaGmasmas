package turquia.ui;

import turquia.model.Usuario;
import turquia.util.AuthService;
import turquia.util.Palette;
import turquia.util.RoundedPanel;
import turquia.util.UIFactory;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    public LoginFrame() {
        setTitle("TURQUIA – Acceder al Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(new BorderLayout());

        // ── Header ───────────────────────────────────────────────────────
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 18));
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(900, 80));
        header.add(buildLogoLabel(40));
        bg.add(header, BorderLayout.NORTH);

        // ── Tarjeta central de login ────────────────────────────────────
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        RoundedPanel card = new RoundedPanel(18, Palette.GUINDO);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(380, 320));
        card.setBorder(new LineBorder(Palette.GUINDO_BORDE, 1, true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 5, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel title = new JLabel("Acceder al Sistema", SwingConstants.CENTER);
        title.setFont(Palette.fontBold(20));
        title.setForeground(Palette.CIAN);
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 30, 22, 30);
        card.add(title, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(4, 30, 4, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(Palette.fontPlain(12));
        lblUser.setForeground(Palette.TEXTO_GRIS);
        card.add(lblUser, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(2, 30, 10, 30);
        txtUsuario = UIFactory.textField();
        card.add(txtUsuario, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(4, 30, 4, 30);
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(Palette.fontPlain(12));
        lblPass.setForeground(Palette.TEXTO_GRIS);
        card.add(lblPass, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(2, 30, 4, 30);
        txtPassword = UIFactory.passwordField();
        card.add(txtPassword, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(18, 30, 30, 30);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        JButton btnLogin = UIFactory.primaryButton("  Ingresar  ");
        btnLogin.setPreferredSize(new Dimension(130, 40));
        btnLogin.setFont(Palette.fontBold(14));
        btnLogin.addActionListener(e -> doLogin());
        card.add(btnLogin, gbc);

        center.add(card);
        bg.add(center, BorderLayout.CENTER);

        // ── Footer ───────────────────────────────────────────────────────
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setPreferredSize(new Dimension(900, 46));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, Palette.GUINDO_BORDE));

        JLabel copy = new JLabel(
            "  © 2024 UAJMS – Universidad Autónoma Juan Misael Saracho." +
            "  Sistema TURQUIA – Unidad de Sistemas DTIC");
        copy.setFont(Palette.fontPlain(11));
        copy.setForeground(Palette.TEXTO_TENUE);
        footer.add(copy, BorderLayout.CENTER);
        bg.add(footer, BorderLayout.SOUTH);

        setContentPane(bg);
        getRootPane().setDefaultButton(btnLogin);
    }

    static JLabel buildLogoLabel(int fontSize) {
        JLabel lbl = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setFont(new Font("SansSerif", Font.BOLD, fontSize));
                FontMetrics fm = g2.getFontMetrics();
                String text = "TURQUiA";

                int x = (getWidth() - fm.stringWidth(text)) / 2;
                if (getWidth() == 0) x = 0;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

                g2.setColor(new Color(0,0,0,60));
                g2.drawString(text, x+2, y+2);

                g2.setColor(Palette.CIAN);
                g2.drawString(text, x, y);

                int iPos = x + fm.stringWidth("TURQUi") - fm.charWidth('i') + fm.charWidth('i')/2;
                g2.setColor(Palette.MAGENTA);
                int dotSize = Math.max(8, fontSize / 3);
                g2.fillOval(iPos - dotSize/2, y - fm.getAscent() - dotSize/3, dotSize, dotSize);

                g2.dispose();
            }
        };
        lbl.setPreferredSize(new Dimension(260, fontSize + 14));
        return lbl;
    }

    private void doLogin() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword());

        Usuario u = AuthService.login(user, pass);
        if (u != null) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                MainFrame main = new MainFrame(u);
                main.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this,
                "Usuario o contraseña incorrectos.\n\nUsuarios de prueba:\n  diogo / 1234  (Estudiante)\n  maria / admin (Administrador)\n  carlos / profe (Docente)",
                "Error de acceso",
                JOptionPane.WARNING_MESSAGE);
            txtPassword.setText("");
        }
    }

    // ── Fondo decorativo: guindo oscuro con destellos cian/magenta ───────
    static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();

            // Fondo base con gradiente guindo
            GradientPaint base = new GradientPaint(0, 0, Palette.GUINDO_OSCURO,
                                                     w, h, new Color(0x150810));
            g2.setPaint(base);
            g2.fillRect(0, 0, w, h);

            if (w > 0 && h > 0) {
                // Resplandor cian (esquina superior izquierda)
                float radius1 = Math.max(w, h) * 0.5f;
                RadialGradientPaint cianGlow = new RadialGradientPaint(
                    new java.awt.geom.Point2D.Float(w * 0.15f, h * 0.15f), radius1,
                    new float[]{0f, 1f},
                    new Color[]{ new Color(0,229,255,40), new Color(0,229,255,0) }
                );
                g2.setPaint(cianGlow);
                g2.fillRect(0, 0, w, h);

                // Resplandor magenta (esquina inferior derecha)
                float radius2 = Math.max(w, h) * 0.55f;
                RadialGradientPaint magentaGlow = new RadialGradientPaint(
                    new java.awt.geom.Point2D.Float(w * 0.85f, h * 0.9f), radius2,
                    new float[]{0f, 1f},
                    new Color[]{ new Color(255,20,147,35), new Color(255,20,147,0) }
                );
                g2.setPaint(magentaGlow);
                g2.fillRect(0, 0, w, h);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
