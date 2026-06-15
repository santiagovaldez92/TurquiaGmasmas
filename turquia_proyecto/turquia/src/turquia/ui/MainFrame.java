package turquia.ui;

import turquia.model.Rol;
import turquia.model.Usuario;
import turquia.util.Palette;
import turquia.util.RoundedPanel;
import turquia.util.UIFactory;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private final Usuario usuario;
    private JPanel contentPanel;
    private final List<JButton> navButtons = new ArrayList<>();

    public MainFrame(Usuario usuario) {
        this.usuario = usuario;

        setTitle("TURQUIA – Sistema Universitario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 650));

        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(Palette.GUINDO_OSCURO);

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildBody(),   BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    // ── Top bar ──────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Palette.GUINDO);
        top.setPreferredSize(new Dimension(0, 90));
        top.setBorder(new CompoundBorder(
            new MatteBorder(0,0,2,0, Palette.GUINDO_BORDE),
            new EmptyBorder(0, 24, 0, 24)
        ));

        JLabel logo = LoginFrame.buildLogoLabel(34);
        logo.setHorizontalAlignment(SwingConstants.LEFT);
        JPanel logoWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoWrap.setOpaque(false);
        logoWrap.add(logo);
        top.add(logoWrap, BorderLayout.WEST);

        // User card
        RoundedPanel userCard = new RoundedPanel(10, Palette.GUINDO_CLARO);
        userCard.setLayout(new BorderLayout(12, 0));
        userCard.setBorder(new CompoundBorder(
            new LineBorder(Palette.GUINDO_BORDE, 1, true),
            new EmptyBorder(12, 18, 12, 18)
        ));
        userCard.setPreferredSize(new Dimension(290, 68));

        JLabel avatar = new JLabel("●");
        avatar.setFont(new Font("SansSerif", Font.BOLD, 30));
        avatar.setForeground(rolColor(usuario.getRol()));
        userCard.add(avatar, BorderLayout.WEST);

        JPanel namePanel = new JPanel(new GridLayout(2, 1, 0, 0));
        namePanel.setOpaque(false);
        JLabel lblName = new JLabel(usuario.getNombre().toUpperCase());
        lblName.setFont(Palette.fontBold(13));
        lblName.setForeground(Palette.TEXTO_CLARO);
        JLabel lblRol = new JLabel(usuario.getRolEtiqueta());
        lblRol.setFont(Palette.fontPlain(12));
        lblRol.setForeground(Palette.TEXTO_GRIS);
        namePanel.add(lblName);
        namePanel.add(lblRol);
        userCard.add(namePanel, BorderLayout.CENTER);

        JButton btnSalir = UIFactory.dangerButton("➡ Salir");
        btnSalir.setPreferredSize(new Dimension(82, 36));
        btnSalir.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this,
                "¿Deseas cerrar sesión?", "Salir", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            }
        });
        userCard.add(btnSalir, BorderLayout.EAST);

        JPanel rightWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 11));
        rightWrap.setOpaque(false);
        rightWrap.add(userCard);
        top.add(rightWrap, BorderLayout.EAST);

        return top;
    }

    private Color rolColor(Rol rol) {
        switch (rol) {
            case ESTUDIANTE: return Palette.CIAN;
            case DOCENTE: return Palette.MAGENTA;
            case ADMINISTRADOR: return Palette.ADVERTENCIA;
            default: return Palette.CIAN;
        }
    }

    // ── Cuerpo: navbar + contenido ──────────────────────────────────────
    private JPanel buildBody() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Palette.GUINDO_OSCURO);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nav.setBackground(Palette.GUINDO);
        nav.setPreferredSize(new Dimension(0, 50));
        nav.setBorder(new MatteBorder(0,0,1,0, Palette.GUINDO_BORDE));

        // Inicio (siempre visible)
        nav.add(buildNavButton("🏠", "Inicio", "inicio"));

        Rol rol = usuario.getRol();

        if (rol == Rol.ESTUDIANTE) {
            nav.add(buildNavButton("👥", "ESTUDIANTES", "estudiantes"));
            nav.add(buildNavButton("👨‍🏫", "DOCENTES", "docentes"));
            nav.add(buildNavButton("📅", "HORARIOS", "horarios"));
        } else if (rol == Rol.DOCENTE) {
            nav.add(buildNavButton("👥", "ESTUDIANTES", "estudiantes"));
            nav.add(buildNavButton("📅", "HORARIOS", "horarios"));
            nav.add(buildNavButton("📚", "MATERIAS", "materias"));
            nav.add(buildNavButton("📨", "REPORTES", "reportes"));
        } else if (rol == Rol.ADMINISTRADOR) {
            nav.add(buildNavButton("👥", "ESTUDIANTES", "estudiantes"));
            nav.add(buildNavButton("👨‍🏫", "DOCENTES", "docentes"));
            nav.add(buildNavButton("📅", "HORARIOS", "horarios"));
            nav.add(buildNavButton("📚", "MATERIAS", "materias"));
            nav.add(buildNavButton("📨", "REPORTES", "reportes"));
            nav.add(buildNavButton("🛠", "USUARIOS", "usuarios"));
        }

        // Spacer
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(9999, 50));
        nav.add(spacer);

        JButton ayuda = buildNavButtonRight("❓", "Ayuda");
        nav.add(ayuda);

        wrapper.add(nav, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Palette.GUINDO_OSCURO);
        showPage("inicio");
        wrapper.add(contentPanel, BorderLayout.CENTER);

        return wrapper;
    }

    private JButton buildNavButton(String icon, String label, String page) {
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
                boolean active = "active".equals(getName());
                Color bg = active ? Palette.GUINDO_CLARO : (hovered ? Palette.GUINDO_CLARO : Palette.GUINDO);
                g2.setColor(bg);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(active ? Palette.CIAN : Palette.TEXTO_CLARO);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = 18;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                if (active) {
                    g2.setColor(Palette.CIAN);
                    g2.fillRect(0, getHeight() - 3, getWidth(), 3);
                }
                g2.dispose();
            }
        };
        btn.setFont(Palette.fontPlain(13));
        btn.setForeground(Palette.TEXTO_CLARO);
        btn.setPreferredSize(new Dimension(label.length() * 11 + 50, 50));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setName(page.equals("inicio") ? "active" : "");
        btn.addActionListener(e -> showPage(page));
        navButtons.add(btn);

        // Store page key for later activation toggling
        btn.putClientProperty("page", page);
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
                g2.setColor(hovered ? Palette.GUINDO_CLARO : Palette.GUINDO);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Palette.TEXTO_CLARO);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setFont(Palette.fontPlain(13));
        btn.setForeground(Palette.TEXTO_CLARO);
        btn.setPreferredSize(new Dimension(100, 50));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Sistema TURQUIA – Simulación académica UAJMS.\n\nMenú dinámico según tu rol: " + usuario.getRolEtiqueta() + ".",
            "Ayuda", JOptionPane.INFORMATION_MESSAGE));
        return btn;
    }

    // ── Navegación de páginas ─────────────────────────────────────────────
    private void showPage(String page) {
        // Actualizar estado "active" de los botones
        for (JButton b : navButtons) {
            String p = (String) b.getClientProperty("page");
            b.setName(page.equals(p) ? "active" : "");
            b.repaint();
        }

        contentPanel.removeAll();
        switch (page) {
            case "inicio"      -> contentPanel.add(new InicioPanel(usuario, this), BorderLayout.CENTER);
            case "estudiantes" -> contentPanel.add(new EstudiantesPanel(usuario), BorderLayout.CENTER);
            case "docentes"    -> contentPanel.add(new DocentesPanel(usuario), BorderLayout.CENTER);
            case "horarios"    -> contentPanel.add(new HorariosPanel(usuario), BorderLayout.CENTER);
            case "materias"    -> contentPanel.add(new MateriasPanel(usuario), BorderLayout.CENTER);
            case "reportes"    -> contentPanel.add(new ReportesPanel(usuario), BorderLayout.CENTER);
            case "usuarios"    -> contentPanel.add(new UsuariosPanel(usuario), BorderLayout.CENTER);
            default            -> contentPanel.add(buildComingSoon(page), BorderLayout.CENTER);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /** Permite que otros paneles naveguen (ej. desde Inicio a un módulo). */
    public void navigateTo(String page) {
        showPage(page);
    }

    private JPanel buildComingSoon(String title) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Palette.GUINDO_OSCURO);
        JLabel lbl = new JLabel("Sección \"" + title + "\" en construcción");
        lbl.setFont(Palette.fontBold(20));
        lbl.setForeground(Palette.TEXTO_GRIS);
        p.add(lbl);
        return p;
    }
}
