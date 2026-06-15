package turquia.ui;

import turquia.model.Rol;
import turquia.model.Usuario;
import turquia.util.DataStore;
import turquia.util.Palette;
import turquia.util.RoundedPanel;
import turquia.util.UIFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InicioPanel extends JPanel {

    public InicioPanel(Usuario usuario, MainFrame main) {
        setLayout(new BorderLayout(0, 0));
        setBackground(Palette.GUINDO_OSCURO);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // ── Bienvenida ────────────────────────────────────────────────────
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setBorder(new EmptyBorder(0, 0, 26, 0));

        JLabel welcome = new JLabel("¡Bienvenido, " + firstName(usuario.getNombre()) + "!");
        welcome.setFont(Palette.fontBold(26));
        welcome.setForeground(Palette.TEXTO_CLARO);
        topRow.add(welcome, BorderLayout.WEST);

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy"));
        JLabel date = new JLabel(capitalize(ts));
        date.setFont(Palette.fontPlain(13));
        date.setForeground(Palette.TEXTO_GRIS);
        topRow.add(date, BorderLayout.EAST);

        add(topRow, BorderLayout.NORTH);

        // ── Cards grid según rol ──────────────────────────────────────────
        List<CardDef> cards = buildCardsForRole(usuario);

        int cols = 3;
        int rows = (int) Math.ceil(cards.size() / (double) cols);
        JPanel grid = new JPanel(new GridLayout(rows, cols, 18, 18));
        grid.setOpaque(false);

        for (CardDef cd : cards) {
            JPanel card = buildCard(cd.icon, cd.title, cd.subtitle, cd.accent);
            if (cd.page != null) {
                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                        main.navigateTo(cd.page);
                    }
                });
            }
            grid.add(card);
        }
        // Rellenar huecos si no es múltiplo exacto
        while (grid.getComponentCount() < rows * cols) {
            JPanel empty = new JPanel();
            empty.setOpaque(false);
            grid.add(empty);
        }

        JPanel gridWrap = new JPanel(new BorderLayout());
        gridWrap.setOpaque(false);
        gridWrap.add(grid, BorderLayout.NORTH);
        add(gridWrap, BorderLayout.CENTER);

        // ── Info strip ────────────────────────────────────────────────────
        JPanel strip = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        strip.setOpaque(false);
        strip.setBorder(new EmptyBorder(18, 0, 0, 0));

        strip.add(UIFactory.pill("Rol: " + usuario.getRolEtiqueta(), rolColor(usuario.getRol())));
        strip.add(UIFactory.pill("UAJMS – TURQUIA v2.0", Palette.MAGENTA));

        if (usuario.getRol() == Rol.ESTUDIANTE) {
            int n = usuario.getMateriasInscritas().size();
            strip.add(UIFactory.pill(n + " materia(s) inscrita(s)", Palette.CIAN_OSCURO));
        } else if (usuario.getRol() == Rol.DOCENTE) {
            int n = usuario.getMateriasImpartidas().size();
            strip.add(UIFactory.pill(n + " materia(s) a cargo", Palette.CIAN_OSCURO));
        } else if (usuario.getRol() == Rol.ADMINISTRADOR) {
            int total = DataStore.getInstance().getUsuarios().size();
            strip.add(UIFactory.pill(total + " usuario(s) registrados", Palette.CIAN_OSCURO));
        }

        add(strip, BorderLayout.SOUTH);
    }

    private Color rolColor(Rol rol) {
        switch (rol) {
            case ESTUDIANTE: return Palette.CIAN_OSCURO;
            case DOCENTE: return Palette.MAGENTA_OSCURO;
            case ADMINISTRADOR: return Palette.ADVERTENCIA;
            default: return Palette.CIAN_OSCURO;
        }
    }

    // ── Definición de tarjetas según rol ────────────────────────────────
    private static class CardDef {
        String icon, title, subtitle, page;
        Color accent;
        CardDef(String icon, String title, String subtitle, Color accent, String page) {
            this.icon = icon; this.title = title; this.subtitle = subtitle;
            this.accent = accent; this.page = page;
        }
    }

    private List<CardDef> buildCardsForRole(Usuario usuario) {
        List<CardDef> list = new ArrayList<>();
        Rol rol = usuario.getRol();

        if (rol == Rol.ESTUDIANTE) {
            list.add(new CardDef("👥", "Estudiantes", "Ver compañeros registrados", Palette.CIAN, "estudiantes"));
            list.add(new CardDef("👨‍🏫", "Docentes", "Ver docentes registrados", Palette.MAGENTA, "docentes"));
            list.add(new CardDef("📅", "Horarios", "Tus materias y notas", Palette.CIAN_OSCURO, "horarios"));
        } else if (rol == Rol.DOCENTE) {
            list.add(new CardDef("👥", "Estudiantes", "Gestión de alumnos y notas", Palette.CIAN, "estudiantes"));
            list.add(new CardDef("📅", "Horarios", "Tus materias asignadas", Palette.CIAN_OSCURO, "horarios"));
            list.add(new CardDef("📚", "Materias", "Catálogo de materias", Palette.MAGENTA, "materias"));
            list.add(new CardDef("📨", "Reportes", "Enviar informe a administración", Palette.ADVERTENCIA, "reportes"));
        } else if (rol == Rol.ADMINISTRADOR) {
            list.add(new CardDef("👥", "Estudiantes", "Gestión de alumnos y notas", Palette.CIAN, "estudiantes"));
            list.add(new CardDef("👨‍🏫", "Docentes", "Gestión de docentes", Palette.MAGENTA, "docentes"));
            list.add(new CardDef("📅", "Horarios", "Materias y horarios", Palette.CIAN_OSCURO, "horarios"));
            list.add(new CardDef("📚", "Materias", "Catálogo de materias", Palette.MAGENTA_OSCURO, "materias"));
            list.add(new CardDef("📨", "Reportes", "Informes recibidos", Palette.ADVERTENCIA, "reportes"));
            list.add(new CardDef("🛠", "Usuarios", "Crear, editar y eliminar usuarios", Palette.EXITO, "usuarios"));
        }
        return list;
    }

    private JPanel buildCard(String icon, String title, String subtitle, Color accent) {
        RoundedPanel card = new RoundedPanel(14, Palette.GUINDO);
        card.setLayout(new BorderLayout(0, 0));
        card.setBorder(new EmptyBorder(22, 22, 22, 22));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel accentBar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(accent);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
            }
        };
        accentBar.setOpaque(false);
        accentBar.setPreferredSize(new Dimension(5, 0));
        card.add(accentBar, BorderLayout.WEST);

        JPanel content = new JPanel(new GridLayout(3, 1, 0, 4));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(0, 14, 0, 0));

        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JLabel ttl = new JLabel(title);
        ttl.setFont(Palette.fontBold(15));
        ttl.setForeground(Palette.TEXTO_CLARO);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(Palette.fontPlain(12));
        sub.setForeground(Palette.TEXTO_GRIS);

        content.add(ico);
        content.add(ttl);
        content.add(sub);
        card.add(content, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground2(Palette.GUINDO_CLARO);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground2(Palette.GUINDO);
            }
        });

        return card;
    }

    private String firstName(String fullName) {
        return fullName.split(" ")[0];
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
