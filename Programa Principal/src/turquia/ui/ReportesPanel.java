package turquia.ui;

import turquia.model.*;
import turquia.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * Reportes / informes hacia la administración.
 *  - DOCENTE: puede redactar y enviar un nuevo reporte. Ve también
 *    los reportes que él mismo ha enviado.
 *  - ADMINISTRADOR: ve todos los reportes recibidos de los docentes.
 */
public class ReportesPanel extends JPanel {

    private final Usuario usuario;
    private JPanel listContainer;

    public ReportesPanel(Usuario usuario) {
        this.usuario = usuario;
        setLayout(new BorderLayout(0, 0));
        setBackground(Palette.GUINDO_OSCURO);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        header.add(UIFactory.sectionTitle("📨 Reportes"), BorderLayout.WEST);

        String subtitleText = usuario.getRol() == Rol.ADMINISTRADOR
            ? "Informes enviados por los docentes"
            : "Envía un informe a la administración";
        header.add(UIFactory.subtitle(subtitleText), BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout(0, 20));
        main.setOpaque(false);

        if (usuario.getRol() == Rol.DOCENTE) {
            main.add(buildFormPanel(), BorderLayout.NORTH);
        }

        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setOpaque(false);
        refreshList();

        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Palette.GUINDO_OSCURO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        main.add(scroll, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);
    }

    private RoundedPanel buildFormPanel() {
        RoundedPanel form = new RoundedPanel(14, Palette.GUINDO);
        form.setLayout(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        form.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.insets = new Insets(4, 0, 4, 0);

        JLabel titleLbl = new JLabel("✍️ Nuevo reporte para administración");
        titleLbl.setFont(Palette.fontBold(15));
        titleLbl.setForeground(Palette.CIAN);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 12, 0);
        form.add(titleLbl, gbc);

        JLabel asuntoLbl = new JLabel("Asunto:");
        asuntoLbl.setFont(Palette.fontPlain(12));
        asuntoLbl.setForeground(Palette.TEXTO_GRIS);
        gbc.gridy = 1;
        gbc.insets = new Insets(4, 0, 2, 0);
        form.add(asuntoLbl, gbc);

        JTextField asuntoField = UIFactory.textField();
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        form.add(asuntoField, gbc);

        JLabel mensajeLbl = new JLabel("Mensaje:");
        mensajeLbl.setFont(Palette.fontPlain(12));
        mensajeLbl.setForeground(Palette.TEXTO_GRIS);
        gbc.gridy = 3;
        gbc.insets = new Insets(4, 0, 2, 0);
        form.add(mensajeLbl, gbc);

        JTextArea mensajeArea = new JTextArea(4, 20);
        mensajeArea.setLineWrap(true);
        mensajeArea.setWrapStyleWord(true);
        mensajeArea.setFont(Palette.fontPlain(13));
        mensajeArea.setForeground(Palette.TEXTO_CLARO);
        mensajeArea.setBackground(Palette.GUINDO_CLARO);
        mensajeArea.setCaretColor(Palette.CIAN);
        mensajeArea.setBorder(new LineBorder(Palette.GUINDO_BORDE, 1, true));

        JScrollPane mensajeScroll = new JScrollPane(mensajeArea);
        mensajeScroll.setBorder(null);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 12, 0);
        form.add(mensajeScroll, gbc);

        JButton enviarBtn = UIFactory.accentButton("Enviar reporte");
        enviarBtn.setPreferredSize(new Dimension(160, 38));
        enviarBtn.addActionListener(e -> {
            String asunto = asuntoField.getText().trim();
            String mensaje = mensajeArea.getText().trim();
            if (asunto.isEmpty() || mensaje.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa el asunto y el mensaje antes de enviar.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Reporte r = new Reporte(usuario.getNombre(), asunto, mensaje);
            DataStore.getInstance().agregarReporte(r);
            asuntoField.setText("");
            mensajeArea.setText("");
            refreshList();
            JOptionPane.showMessageDialog(this, "Reporte enviado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnRow.setOpaque(false);
        btnRow.add(enviarBtn);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 0, 0);
        form.add(btnRow, gbc);

        return form;
    }

    private void refreshList() {
        listContainer.removeAll();
        List<Reporte> reportes = DataStore.getInstance().getReportes();

        // Si es docente, solo mostrar los suyos. Si es admin, mostrar todos.
        List<Reporte> visibles = reportes.stream()
            .filter(r -> usuario.getRol() == Rol.ADMINISTRADOR || r.getAutor().equals(usuario.getNombre()))
            .toList();

        if (visibles.isEmpty()) {
            JLabel empty = new JLabel(usuario.getRol() == Rol.ADMINISTRADOR
                ? "No se han recibido reportes todavía."
                : "Aún no has enviado ningún reporte.");
            empty.setFont(Palette.fontPlain(13));
            empty.setForeground(Palette.TEXTO_TENUE);
            listContainer.add(empty);
        }

        // Mostrar los más recientes primero
        for (int i = visibles.size() - 1; i >= 0; i--) {
            listContainer.add(buildReporteCard(visibles.get(i)));
            listContainer.add(Box.createVerticalStrut(10));
        }
        listContainer.revalidate();
        listContainer.repaint();
    }

    private RoundedPanel buildReporteCard(Reporte r) {
        RoundedPanel card = new RoundedPanel(12, Palette.GUINDO);
        card.setLayout(new BorderLayout(0, 6));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 9999));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel asunto = new JLabel(r.getAsunto());
        asunto.setFont(Palette.fontBold(14));
        asunto.setForeground(Palette.CIAN);
        top.add(asunto, BorderLayout.WEST);

        JLabel meta = new JLabel(r.getAutor() + "  ·  " + r.getFechaTexto());
        meta.setFont(Palette.fontPlain(11));
        meta.setForeground(Palette.TEXTO_TENUE);
        top.add(meta, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);

        JTextArea body = new JTextArea(r.getMensaje());
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setEditable(false);
        body.setOpaque(false);
        body.setFont(Palette.fontPlain(13));
        body.setForeground(Palette.TEXTO_CLARO);
        body.setBorder(new EmptyBorder(8, 0, 0, 0));
        card.add(body, BorderLayout.CENTER);

        return card;
    }
}
