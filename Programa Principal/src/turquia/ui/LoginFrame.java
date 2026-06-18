package turquia.ui;

import turquia.model.Usuario;
import turquia.util.AuthService;
import turquia.util.Palette;
import turquia.util.RoundedPanel;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    public LoginFrame() {
        setTitle("TURQUIA – Acceder al Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);

        // Panel principal con fondo de paisaje simulado
        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(new BorderLayout());

        // ── Header amarillo ──────────────────────────────────────────────
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 18));
        header.setBackground(Palette.AMARILLO);
        header.setPreferredSize(new Dimension(900, 70));

        JLabel lblLogo = buildLogoLabel();
        header.add(lblLogo);
        bg.add(header, BorderLayout.NORTH);

        // ── Tarjeta central de login ──────────────────────────────────────
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        RoundedPanel card = new RoundedPanel(18, new Color(255, 255, 255, 235));
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(380, 310));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 5, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Título
        JLabel title = new JLabel("Acceder al Sistema", SwingConstants.CENTER);
        title.setFont(Palette.fontPlain(18));
        title.setForeground(Palette.NAVY);
        gbc.gridy = 0;
        gbc.insets = new Insets(28, 30, 18, 30);
        card.add(title, gbc);

        // Campo usuario
        gbc.gridy = 1;
        gbc.insets = new Insets(4, 30, 4, 30);
        txtUsuario = buildTextField("Usuario:");
        card.add(txtUsuario, gbc);

        // Campo password
        gbc.gridy = 2;
        txtPassword = buildPasswordField("Contraseña:");
        card.add(txtPassword, gbc);

        // Botón ingresar
        gbc.gridy = 3;
        gbc.insets = new Insets(14, 30, 28, 30);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        JButton btnLogin = buildLoginButton();
        card.add(btnLogin, gbc);

        center.add(card);
        bg.add(center, BorderLayout.CENTER);

        // ── Footer ───────────────────────────────────────────────────────
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(255,255,255,180));
        footer.setPreferredSize(new Dimension(900, 46));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(180,180,180,120)));

        JLabel copy = new JLabel(
            "  © 2024 UAJMS – Universidad Autónoma Juan Misael Saracho." +
            "  Desarrollado por la Unidad de Sistemas DTIC – UAJMS");
        copy.setFont(Palette.fontPlain(11));
        copy.setForeground(Palette.TEXTO_GRIS);
        footer.add(copy, BorderLayout.CENTER);
        bg.add(footer, BorderLayout.SOUTH);

        setContentPane(bg);

        // Enter key triggers login
        getRootPane().setDefaultButton(btnLogin);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private JLabel buildLogoLabel() {
        JLabel lbl = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // "TURQUIA" text
                g2.setFont(new Font("SansSerif", Font.BOLD, 36));
                FontMetrics fm = g2.getFontMetrics();

                String text = "TURQUiA";
                int totalW = fm.stringWidth(text);
                int x = (getWidth() - totalW) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

                // Shadow
                g2.setColor(new Color(0,0,0,40));
                g2.drawString(text, x+2, y+2);

                // White text
                g2.setColor(Color.WHITE);
                g2.drawString(text, x, y);

                // Orange dot on 'i'
                int iPos = x + fm.stringWidth("TURQUi") - fm.charWidth('i') + fm.charWidth('i')/2;
                g2.setColor(Palette.NARANJA_PT);
                g2.fillOval(iPos - 5, y - fm.getAscent() - 6, 12, 12);

                g2.dispose();
            }
        };
        lbl.setPreferredSize(new Dimension(260, 50));
        return lbl;
    }

    private JTextField buildTextField(String placeholder) {
        JTextField tf = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(160,160,160));
                    g2.setFont(getFont().deriveFont(Font.PLAIN, 13f));
                    Insets ins = getInsets();
                    g2.drawString(placeholder, ins.left + 2, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 1);
                    g2.dispose();
                }
            }
        };
        styleInput(tf);
        return tf;
    }

    private JPasswordField buildPasswordField(String placeholder) {
        JPasswordField pf = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(160,160,160));
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
                    Insets ins = getInsets();
                    g2.drawString(placeholder, ins.left + 2, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 1);
                    g2.dispose();
                }
            }
        };
        styleInput(pf);
        return pf;
    }

    private void styleInput(JTextField tf) {
        tf.setPreferredSize(new Dimension(300, 42));
        tf.setFont(Palette.fontPlain(14));
        tf.setForeground(Palette.NAVY);
        tf.setBackground(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 210, 230), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));
        tf.setOpaque(true);
    }

    private JButton buildLoginButton() {
        JButton btn = new JButton("  Ingresar  ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover() ? Palette.VERDE_HOVER : Palette.VERDE_BTN;
                g2.setColor(c);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 22, 22));
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setFont(Palette.fontBold(14));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(130, 40));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> doLogin());
        return btn;
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
                "Usuario o contraseña incorrectos.\n\nUsuarios de prueba:\n  diogo / 1234\n  maria / admin\n  carlos / profe",
                "Error de acceso",
                JOptionPane.WARNING_MESSAGE);
            txtPassword.setText("");
        }
    }

    // ── Fondo con gradiente tipo montaña ─────────────────────────────────
    static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();

            // Cielo con gradiente
            GradientPaint sky = new GradientPaint(0, 0, new Color(0x4A7FAB),
                                                   0, h, new Color(0x2E5E4E));
            g2.setPaint(sky);
            g2.fillRect(0, 0, w, h);

            // Montañas traseras
            g2.setColor(new Color(58, 107, 85, 180));
            int[] xMt1 = {0, 150, 280, 400, 520, 650, 780, w, w, 0};
            int[] yMt1 = {h-180, h-320, h-260, h-380, h-290, h-400, h-340, h-200, h, h};
            g2.fillPolygon(xMt1, yMt1, xMt1.length);

            // Montañas medias
            g2.setColor(new Color(45, 90, 58, 200));
            int[] xMt2 = {0, 100, 220, 360, 480, 600, 720, 840, w, w, 0};
            int[] yMt2 = {h-120, h-260, h-190, h-310, h-230, h-350, h-270, h-190, h-140, h, h};
            g2.fillPolygon(xMt2, yMt2, xMt2.length);

            // Vegetación delantera
            g2.setColor(new Color(30, 74, 42, 220));
            g2.fillRect(0, h - 110, w, 110);

            // Niebla leve
            GradientPaint fog = new GradientPaint(0, h-200, new Color(255,255,255,0),
                                                   0, h, new Color(255,255,255,30));
            g2.setPaint(fog);
            g2.fillRect(0, 0, w, h);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
