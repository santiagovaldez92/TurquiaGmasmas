package turquia.ui;

import turquia.model.Usuario;
import turquia.util.Palette;
import turquia.util.RoundedPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InicioPanel extends JPanel {

    public InicioPanel(Usuario usuario) {
        setLayout(new BorderLayout(0, 0));
        setBackground(Palette.GRIS_FONDO);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // ── Bienvenida ────────────────────────────────────────────────────
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setBorder(new EmptyBorder(0, 0, 26, 0));

        JLabel welcome = new JLabel("¡Bienvenido, " + firstName(usuario.getNombre()) + "!");
        welcome.setFont(Palette.fontBold(26));
        welcome.setForeground(Palette.NAVY);
        topRow.add(welcome, BorderLayout.WEST);

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy"));
        JLabel date = new JLabel(capitalize(ts));
        date.setFont(Palette.fontPlain(13));
        date.setForeground(Palette.TEXTO_GRIS);
        topRow.add(date, BorderLayout.EAST);

        add(topRow, BorderLayout.NORTH);

        // ── Cards grid ────────────────────────────────────────────────────
        JPanel grid = new JPanel(new GridLayout(2, 3, 18, 18));
        grid.setOpaque(false);

        grid.add(buildCard("👥", "Estudiantes", "Gestión de alumnos", Palette.VERDE_BTN));
        grid.add(buildCard("📅", "Horarios", "Consulta de horarios", new Color(0x5C7AEA)));
        grid.add(buildCard("📚", "Materias", "Próximamente", new Color(0xE67E22)));
        grid.add(buildCard("👨‍🏫", "Docentes", "Próximamente", new Color(0x8E44AD)));
        grid.add(buildCard("📊", "Reportes", "Próximamente", new Color(0xE74C3C)));
        grid.add(buildCard("⚙️", "Configuración", "Próximamente", new Color(0x7F8C8D)));

        add(grid, BorderLayout.CENTER);

        // ── Info strip ────────────────────────────────────────────────────
        JPanel strip = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        strip.setOpaque(false);
        strip.setBorder(new EmptyBorder(18, 0, 0, 0));

        strip.add(buildBadge("Rol: " + usuario.getRol(), Palette.NAVY));
        strip.add(buildBadge("UAJMS – Turquia v1.0", Palette.AMARILLO_OS));

        add(strip, BorderLayout.SOUTH);
    }

    private JPanel buildCard(String icon, String title, String subtitle, Color accent) {
        RoundedPanel card = new RoundedPanel(14, Color.WHITE);
        card.setLayout(new BorderLayout(0, 0));
        card.setBorder(new EmptyBorder(22, 22, 22, 22));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Accent bar on left
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
        ttl.setForeground(Palette.NAVY);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(Palette.fontPlain(12));
        sub.setForeground(Palette.TEXTO_GRIS);

        content.add(ico);
        content.add(ttl);
        content.add(sub);
        card.add(content, BorderLayout.CENTER);

        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                card.repaint();
                ((RoundedPanel)card).setBackground2(new Color(248, 250, 255));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                ((RoundedPanel)card).setBackground2(Color.WHITE);
            }
        });

        return card;
    }

    private JLabel buildBadge(String text, Color color) {
        JLabel lbl = new JLabel("  " + text + "  ") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        lbl.setFont(Palette.fontBold(12));
        lbl.setForeground(Color.WHITE);
        lbl.setOpaque(false);
        lbl.setPreferredSize(new Dimension(text.length() * 9 + 20, 30));
        return lbl;
    }

    private String firstName(String fullName) {
        return fullName.split(" ")[0];
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
