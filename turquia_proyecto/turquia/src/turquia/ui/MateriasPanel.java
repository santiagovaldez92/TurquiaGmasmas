package turquia.ui;

import turquia.model.*;
import turquia.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Catálogo de materias. Visible para Docentes y Administradores.
 * Los docentes pueden auto-asignarse o quitarse materias que imparten.
 * Los administradores pueden ver cuántos docentes/estudiantes hay por materia.
 */
public class MateriasPanel extends JPanel {

    private final Usuario usuario;
    private JPanel listContainer;

    public MateriasPanel(Usuario usuario) {
        this.usuario = usuario;
        setLayout(new BorderLayout(0, 0));
        setBackground(Palette.GUINDO_OSCURO);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        header.add(UIFactory.sectionTitle("📚 Materias"), BorderLayout.WEST);
        header.add(UIFactory.subtitle("Catálogo de materias de la carrera"), BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setOpaque(false);

        refresh();

        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Palette.GUINDO_OSCURO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private void refresh() {
        listContainer.removeAll();
        List<Materia> materias = DataStore.getInstance().getMaterias();

        for (Materia m : materias) {
            listContainer.add(buildRow(m));
            listContainer.add(Box.createVerticalStrut(10));
        }
        listContainer.revalidate();
        listContainer.repaint();
    }

    private RoundedPanel buildRow(Materia m) {
        RoundedPanel row = new RoundedPanel(12, Palette.GUINDO);
        row.setLayout(new BorderLayout(14, 0));
        row.setBorder(new EmptyBorder(16, 20, 16, 20));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));
        row.setPreferredSize(new Dimension(10, 76));

        JPanel accentBar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(Palette.CIAN);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
            }
        };
        accentBar.setOpaque(false);
        accentBar.setPreferredSize(new Dimension(5, 0));
        row.add(accentBar, BorderLayout.WEST);

        // Info
        int docentesCount = 0, estudiantesCount = 0;
        for (Usuario u : DataStore.getInstance().getDocentes()) {
            if (u.getMateriasImpartidas().contains(m.getId())) docentesCount++;
        }
        for (Usuario u : DataStore.getInstance().getEstudiantes()) {
            if (u.getMateriasInscritas().contains(m.getId())) estudiantesCount++;
        }

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(0, 14, 0, 0));

        JLabel title = new JLabel(m.getSigla() + " — " + m.getNombre());
        title.setFont(Palette.fontBold(15));
        title.setForeground(Palette.TEXTO_CLARO);

        JLabel sub = new JLabel(docentesCount + " docente(s)  ·  " + estudiantesCount + " estudiante(s) inscrito(s)");
        sub.setFont(Palette.fontPlain(12));
        sub.setForeground(Palette.TEXTO_GRIS);

        info.add(title);
        info.add(Box.createVerticalStrut(2));
        info.add(sub);
        row.add(info, BorderLayout.CENTER);

        // Acción para docentes: asignarse/quitarse
        if (usuario.getRol() == Rol.DOCENTE) {
            boolean asignado = usuario.getMateriasImpartidas().contains(m.getId());
            JButton btn = asignado ? UIFactory.dangerButton("Dejar de impartir") : UIFactory.accentButton("Impartir esta materia");
            btn.setPreferredSize(new Dimension(180, 36));
            btn.addActionListener(e -> {
                if (asignado) {
                    DataStore.getInstance().quitarMateriaADocente(usuario.getId(), m.getId());
                } else {
                    DataStore.getInstance().asignarMateriaADocente(usuario.getId(), m.getId());
                }
                refresh();
            });
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            right.setOpaque(false);
            right.add(btn);
            row.add(right, BorderLayout.EAST);
        } else if (usuario.getRol() == Rol.ADMINISTRADOR) {
            JLabel pill = UIFactory.pill(m.getSigla(), Palette.MAGENTA);
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            right.setOpaque(false);
            right.add(pill);
            row.add(right, BorderLayout.EAST);
        }

        return row;
    }
}
