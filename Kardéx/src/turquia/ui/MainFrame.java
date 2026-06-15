package turquia.ui;

import turquia.model.Usuario;
import turquia.util.Palette;
import turquia.util.RoundedPanel;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class MainFrame extends JFrame {

    private final Usuario usuario;
    private JPanel contentPanel;

    public MainFrame(Usuario usuario) {
        this.usuario = usuario;

        setTitle("TURQUIA – Sistema Universitario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 600));

        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(Palette.GRIS_FONDO);

        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildNavBar(),    BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    // ── Top bar: logo + user info ─────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Palette.AMARILLO);
        top.setPreferredSize(new Dimension(0, 90));
        top.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Logo
        JLabel logo = buildLogoLabel();
        top.add(logo, BorderLayout.WEST);

        // User card
        RoundedPanel userCard = new RoundedPanel(10, Palette.NAVY);
        userCard.setLayout(new BorderLayout(12, 0));
        userCard.setBorder(new EmptyBorder(12, 18, 12, 18));
        userCard.setPreferredSize(new Dimension(280, 68));

        // Avatar icon
        JLabel avatar = new JLabel("👤");
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        avatar.setForeground(Palette.VERDE_BTN);
        userCard.add(avatar, BorderLayout.WEST);

        // Name + role
        JPanel namePanel = new JPanel(new GridLayout(2, 1, 0, 0));
        namePanel.setOpaque(false);
        JLabel lblName = new JLabel(usuario.getNombre().toUpperCase());
        lblName.setFont(Palette.fontBold(13));
        lblName.setForeground(Color.WHITE);
        JLabel lblRol = new JLabel(usuario.getRol());
        lblRol.setFont(Palette.fontPlain(12));
        lblRol.setForeground(new Color(180, 200, 220));
        namePanel.add(lblName);
        namePanel.add(lblRol);
        userCard.add(namePanel, BorderLayout.CENTER);

        // Salir button
        JButton btnSalir = buildSalirButton();
        userCard.add(btnSalir, BorderLayout.EAST);

        JPanel rightWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 11));
        rightWrap.setOpaque(false);
        rightWrap.add(userCard);
        top.add(rightWrap, BorderLayout.EAST);

        return top;
    }

    // ── Nav bar ───────────────────────────────────────────────────────────
    private JPanel buildNavBar() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Palette.GRIS_FONDO);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nav.setBackground(Palette.NAVY);
        nav.setPreferredSize(new Dimension(0, 50));

        // Inicio
        nav.add(buildNavButton("🏠", "Inicio", true, () -> showPage("inicio")));
        nav.add(buildNavButton("👥", "ESTUDIANTES", false, () -> showPage("estudiantes")));
        nav.add(buildNavButton("📅", "HORARIOS", false, () -> showPage("horarios")));

        // Spacer
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(9999, 50));
        nav.add(spacer);

        // Ayuda (derecha)
        JButton ayuda = buildNavButtonRight("❓", "Ayuda");
        nav.add(ayuda);

        wrapper.add(nav, BorderLayout.NORTH);

        // Content area
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Palette.GRIS_FONDO);
        showPage("inicio");
        wrapper.add(contentPanel, BorderLayout.CENTER);

        return wrapper;
    }

    private JButton buildNavButton(String icon, String label, boolean active, Runnable action) {
        Color activeBg   = Palette.AMARILLO;
        Color inactiveBg = Palette.NAVY;
        Color hoverBg    = Palette.NAVY_LIGHT;

        JButton btn = new JButton(icon + "  " + label) {
            boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Color bg;
                if (active) bg = activeBg;
                else if (hovered) bg = hoverBg;
                else bg = inactiveBg;
                g2.setColor(bg);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(active ? Palette.NAVY : Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = 18;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                if (active) {
                    g2.setColor(Palette.AMARILLO_OS);
                    g2.fillRect(0, getHeight() - 3, getWidth(), 3);
                }
                g2.dispose();
            }
        };
        btn.setFont(active ? Palette.fontBold(13) : Palette.fontPlain(13));
        btn.setForeground(active ? Palette.NAVY : Color.WHITE);
        btn.setPreferredSize(new Dimension(label.length() * 11 + 50, 50));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (action != null) btn.addActionListener(e -> action.run());
        return btn;
    }

    private JButton buildNavButtonRight(String icon, String label) {
        JButton btn = new JButton(icon + "  " + label) {
            boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(hovered ? Palette.NAVY_LIGHT : Palette.NAVY);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setFont(Palette.fontPlain(13));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(100, 50));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Sección de ayuda en construcción.", "Ayuda", JOptionPane.INFORMATION_MESSAGE));
        return btn;
    }

    // ── Content pages ─────────────────────────────────────────────────────
    private void showPage(String page) {
        contentPanel.removeAll();
        switch (page) {
            case "inicio"      -> contentPanel.add(new InicioPanel(usuario), BorderLayout.CENTER);
            case "estudiantes" -> contentPanel.add(buildComingSoon("ESTUDIANTES", "👥"), BorderLayout.CENTER);
            case "horarios"    -> contentPanel.add(buildComingSoon("HORARIOS", "📅"), BorderLayout.CENTER);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel buildComingSoon(String title, String icon) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Palette.GRIS_FONDO);
        JLabel lbl = new JLabel(icon + "  " + title + " – Próximamente");
        lbl.setFont(Palette.fontBold(22));
        lbl.setForeground(new Color(180, 190, 210));
        p.add(lbl);
        return p;
    }

    // ── Logo label ────────────────────────────────────────────────────────
    private JLabel buildLogoLabel() {
        JLabel lbl = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setFont(new Font("SansSerif", Font.BOLD, 42));
                FontMetrics fm = g2.getFontMetrics();
                String text = "TURQUiA";

                int x = 0, y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.setColor(new Color(0,0,0,30));
                g2.drawString(text, x+2, y+2);
                g2.setColor(Color.WHITE);
                g2.drawString(text, x, y);

                int iPos = x + fm.stringWidth("TURQUi") - fm.charWidth('i') + fm.charWidth('i')/2;
                g2.setColor(Palette.NARANJA_PT);
                g2.fillOval(iPos - 6, y - fm.getAscent() - 4, 13, 13);

                g2.dispose();
            }
        };
        lbl.setPreferredSize(new Dimension(300, 90));
        return lbl;
    }

    // ── Salir button ──────────────────────────────────────────────────────
    private JButton buildSalirButton() {
        JButton btn = new JButton("➡ Salir") {
            boolean hov = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hov = true; repaint(); }
                    public void mouseExited(MouseEvent e)  { hov = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hov ? new Color(220,60,60) : new Color(180,40,40));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setFont(Palette.fontBold(12));
        btn.setPreferredSize(new Dimension(82, 36));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this,
                "¿Deseas cerrar sesión?", "Salir", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            }
        });
        return btn;
    }
}
