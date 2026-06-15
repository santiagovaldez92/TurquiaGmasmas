package turquia.util;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Fábrica de componentes Swing con estilo consistente para toda la app.
 */
public class UIFactory {

    /** Botón con fondo de color sólido y esquinas redondeadas. */
    public static JButton solidButton(String text, Color base, Color hover, Color fg) {
        JButton btn = new JButton(text) {
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
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? hover : base);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(fg);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setFont(Palette.fontBold(13));
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Botón de acento cian (acciones principales). */
    public static JButton primaryButton(String text) {
        return solidButton(text, Palette.CIAN, Palette.CIAN_OSCURO, Palette.GUINDO_OSCURO);
    }

    /** Botón de acento magenta (acciones secundarias / destacar). */
    public static JButton accentButton(String text) {
        return solidButton(text, Palette.MAGENTA, Palette.MAGENTA_OSCURO, Palette.BLANCO);
    }

    /** Botón sutil (cancelar, cerrar, etc). */
    public static JButton ghostButton(String text) {
        return solidButton(text, Palette.GUINDO_BORDE, Palette.GUINDO_CLARO, Palette.TEXTO_CLARO);
    }

    /** Botón de peligro (eliminar). */
    public static JButton dangerButton(String text) {
        return solidButton(text, new Color(0xB3304A), new Color(0xD43C5C), Palette.BLANCO);
    }

    /** Campo de texto con estilo oscuro. */
    public static JTextField textField() {
        JTextField tf = new JTextField();
        styleField(tf);
        return tf;
    }

    public static JPasswordField passwordField() {
        JPasswordField pf = new JPasswordField();
        styleField(pf);
        return pf;
    }

    private static void styleField(JTextField tf) {
        tf.setFont(Palette.fontPlain(13));
        tf.setForeground(Palette.TEXTO_CLARO);
        tf.setBackground(Palette.GUINDO_CLARO);
        tf.setCaretColor(Palette.CIAN);
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Palette.GUINDO_BORDE, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
        tf.setPreferredSize(new Dimension(0, 36));
    }

    public static JComboBox<String> comboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(Palette.fontPlain(13));
        cb.setForeground(Palette.TEXTO_CLARO);
        cb.setBackground(Palette.GUINDO_CLARO);
        cb.setBorder(new LineBorder(Palette.GUINDO_BORDE, 1, true));
        cb.setPreferredSize(new Dimension(0, 36));
        cb.setFocusable(false);
        return cb;
    }

    /** Etiqueta tipo "pill" (chip). */
    public static JLabel pill(String text, Color color) {
        JLabel lbl = new JLabel("  " + text + "  ") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.setColor(Palette.GUINDO_OSCURO);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        lbl.setFont(Palette.fontBold(11));
        lbl.setOpaque(false);
        FontMetrics fm = lbl.getFontMetrics(lbl.getFont());
        lbl.setPreferredSize(new Dimension(fm.stringWidth(text) + 28, 24));
        return lbl;
    }

    /** Título de sección. */
    public static JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Palette.fontBold(22));
        lbl.setForeground(Palette.TEXTO_CLARO);
        return lbl;
    }

    /** Subtítulo / descripción. */
    public static JLabel subtitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Palette.fontPlain(13));
        lbl.setForeground(Palette.TEXTO_GRIS);
        return lbl;
    }

    /** Tarjeta base con fondo guindo claro y bordes redondeados. */
    public static RoundedPanel card() {
        RoundedPanel p = new RoundedPanel(14, Palette.GUINDO_CLARO);
        p.setLayout(new BorderLayout());
        return p;
    }
}
