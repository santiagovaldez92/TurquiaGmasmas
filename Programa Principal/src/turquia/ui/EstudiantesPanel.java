package turquia.ui;

import turquia.model.*;
import turquia.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * Lista de estudiantes. Comportamiento según rol:
 *  - ESTUDIANTE: ve la lista, puede ver el detalle de cualquiera, pero
 *    solo ve SUS PROPIAS notas (las de otros aparecen ocultas).
 *  - DOCENTE: ve la lista, puede abrir el detalle y asignar/quitar materias
 *    y asignar notas a los estudiantes en las materias que él imparte.
 *  - ADMINISTRADOR: igual que docente (acceso total).
 */
public class EstudiantesPanel extends JPanel {

    private final Usuario usuarioActual;
    private JPanel listContainer;

    public EstudiantesPanel(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout(0, 0));
        setBackground(Palette.GUINDO_OSCURO);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        header.add(UIFactory.sectionTitle("👥 Estudiantes"), BorderLayout.WEST);
        header.add(UIFactory.subtitle(DataStore.getInstance().getEstudiantes().size() + " estudiante(s) registrados"), BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // List
        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setOpaque(false);

        refreshList();

        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Palette.GUINDO_OSCURO);
        scroll.setBackground(Palette.GUINDO_OSCURO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private void refreshList() {
        listContainer.removeAll();
        List<Usuario> estudiantes = DataStore.getInstance().getEstudiantes();

        for (Usuario est : estudiantes) {
            listContainer.add(buildRow(est));
            listContainer.add(Box.createVerticalStrut(10));
        }

        listContainer.revalidate();
        listContainer.repaint();
    }

    private RoundedPanel buildRow(Usuario est) {
        RoundedPanel row = new RoundedPanel(12, Palette.GUINDO);
        row.setLayout(new BorderLayout(14, 0));
        row.setBorder(new EmptyBorder(16, 20, 16, 20));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setPreferredSize(new Dimension(10, 70));

        // Avatar + nombre
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        left.setOpaque(false);

        JLabel avatar = new JLabel("●");
        avatar.setFont(new Font("SansSerif", Font.BOLD, 26));
        avatar.setForeground(Palette.CIAN);
        left.add(avatar);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);
        JLabel name = new JLabel(est.getNombre());
        name.setFont(Palette.fontBold(14));
        name.setForeground(Palette.TEXTO_CLARO);
        JLabel sub = new JLabel("@" + est.getUsername() + "  ·  " + est.getMateriasInscritas().size() + " materia(s)");
        sub.setFont(Palette.fontPlain(12));
        sub.setForeground(Palette.TEXTO_GRIS);
        namePanel.add(name);
        namePanel.add(sub);
        left.add(namePanel);

        row.add(left, BorderLayout.WEST);

        // Botón ver detalle
        JButton verBtn = UIFactory.primaryButton("Ver perfil");
        verBtn.setPreferredSize(new Dimension(110, 36));
        verBtn.addActionListener(e -> abrirDetalle(est));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(verBtn);
        row.add(right, BorderLayout.EAST);

        return row;
    }

    private void abrirDetalle(Usuario est) {
        boolean esPropio = usuarioActual.getId() == est.getId();
        boolean puedeGestionar = usuarioActual.getRol() == Rol.DOCENTE || usuarioActual.getRol() == Rol.ADMINISTRADOR;
        boolean puedeVerNotas = esPropio || puedeGestionar;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            "Perfil de " + est.getNombre(), true);
        dialog.setSize(560, 560);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Palette.GUINDO_OSCURO);
        dialog.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Palette.GUINDO_OSCURO);
        content.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Cabecera
        JLabel title = new JLabel(est.getNombre());
        title.setFont(Palette.fontBold(20));
        title.setForeground(Palette.TEXTO_CLARO);
        content.add(title);

        JLabel subt = new JLabel("@" + est.getUsername() + "  ·  Estudiante");
        subt.setFont(Palette.fontPlain(13));
        subt.setForeground(Palette.TEXTO_GRIS);
        subt.setBorder(new EmptyBorder(4, 0, 18, 0));
        content.add(subt);

        // ── Materias inscritas ──────────────────────────────────────────
        JLabel materiasLbl = new JLabel("📚 Materias inscritas");
        materiasLbl.setFont(Palette.fontBold(14));
        materiasLbl.setForeground(Palette.CIAN);
        materiasLbl.setBorder(new EmptyBorder(0, 0, 8, 0));
        content.add(materiasLbl);

        RoundedPanel materiasBox = new RoundedPanel(10, Palette.GUINDO);
        materiasBox.setLayout(new BoxLayout(materiasBox, BoxLayout.Y_AXIS));
        materiasBox.setBorder(new EmptyBorder(10, 14, 10, 14));

        Runnable[] refreshHolder = new Runnable[1];

        refreshHolder[0] = () -> {
            materiasBox.removeAll();
            List<Integer> ids = est.getMateriasInscritas();
            if (ids.isEmpty()) {
                JLabel empty = new JLabel("No tiene materias inscritas.");
                empty.setFont(Palette.fontPlain(12));
                empty.setForeground(Palette.TEXTO_TENUE);
                materiasBox.add(empty);
            }
            for (int idMat : ids) {
                Materia m = DataStore.getInstance().buscarMateria(idMat);
                if (m == null) continue;
                materiasBox.add(buildMateriaRow(est, m, puedeVerNotas, puedeGestionar, refreshHolder));
                materiasBox.add(Box.createVerticalStrut(6));
            }
            materiasBox.revalidate();
            materiasBox.repaint();
        };
        refreshHolder[0].run();

        JScrollPane materiasScroll = new JScrollPane(materiasBox);
        materiasScroll.setBorder(null);
        materiasScroll.getViewport().setBackground(Palette.GUINDO_OSCURO);
        materiasScroll.setPreferredSize(new Dimension(0, 180));
        materiasScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        content.add(materiasScroll);

        // ── Asignar materia (solo docente/admin) ─────────────────────────
        if (puedeGestionar) {
            content.add(Box.createVerticalStrut(16));
            JLabel asignarLbl = new JLabel("➕ Asignar nueva materia");
            asignarLbl.setFont(Palette.fontBold(14));
            asignarLbl.setForeground(Palette.MAGENTA);
            asignarLbl.setBorder(new EmptyBorder(0, 0, 8, 0));
            content.add(asignarLbl);

            JPanel addRow = new JPanel(new BorderLayout(10, 0));
            addRow.setOpaque(false);
            addRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            List<Materia> todas = DataStore.getInstance().getMaterias();
            String[] nombres = new String[todas.size()];
            for (int i = 0; i < todas.size(); i++) nombres[i] = todas.get(i).getNombre();
            JComboBox<String> combo = UIFactory.comboBox(nombres);

            JButton addBtn = UIFactory.accentButton("Asignar");
            addBtn.setPreferredSize(new Dimension(110, 36));
            addBtn.addActionListener(e -> {
                int idx = combo.getSelectedIndex();
                if (idx < 0) return;
                Materia m = todas.get(idx);
                if (est.getMateriasInscritas().contains(m.getId())) {
                    JOptionPane.showMessageDialog(dialog, "El estudiante ya está inscrito en esa materia.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                DataStore.getInstance().asignarMateriaAEstudiante(est.getId(), m.getId());
                refreshHolder[0].run();
            });

            addRow.add(combo, BorderLayout.CENTER);
            addRow.add(addBtn, BorderLayout.EAST);
            content.add(addRow);
        }

        // ── Botón cerrar ──────────────────────────────────────────────────
        content.add(Box.createVerticalGlue());
        JPanel closeRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeRow.setOpaque(false);
        JButton closeBtn = UIFactory.ghostButton("Cerrar");
        closeBtn.setPreferredSize(new Dimension(100, 36));
        closeBtn.addActionListener(e -> dialog.dispose());
        closeRow.add(closeBtn);
        content.add(closeRow);

        dialog.add(content, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    /** Fila individual de materia dentro del detalle del estudiante. */
    private JPanel buildMateriaRow(Usuario est, Materia m, boolean puedeVerNotas, boolean puedeGestionar, Runnable[] refreshHolder) {
        RoundedPanel row = new RoundedPanel(8, Palette.GUINDO_CLARO);
        row.setLayout(new BorderLayout(10, 0));
        row.setBorder(new EmptyBorder(8, 12, 8, 12));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        row.setPreferredSize(new Dimension(10, 44));

        JLabel nameLbl = new JLabel(m.getSigla() + " · " + m.getNombre());
        nameLbl.setFont(Palette.fontPlain(13));
        nameLbl.setForeground(Palette.TEXTO_CLARO);
        row.add(nameLbl, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        Nota nota = DataStore.getInstance().buscarNota(est.getId(), m.getId());

        if (puedeVerNotas) {
            String notaTxt = nota != null ? String.format("%.1f (%s)", nota.getValor(), nota.getEstado()) : "Sin nota";
            Color notaColor = nota == null ? Palette.TEXTO_TENUE :
                (nota.getValor() >= 51 ? Palette.EXITO : Palette.ERROR);
            JLabel notaLbl = new JLabel(notaTxt);
            notaLbl.setFont(Palette.fontBold(12));
            notaLbl.setForeground(notaColor);
            right.add(notaLbl);
        } else {
            JLabel hiddenLbl = new JLabel("🔒 Nota privada");
            hiddenLbl.setFont(Palette.fontPlain(12));
            hiddenLbl.setForeground(Palette.TEXTO_TENUE);
            right.add(hiddenLbl);
        }

        if (puedeGestionar) {
            JButton editBtn = UIFactory.ghostButton("Calificar");
            editBtn.setPreferredSize(new Dimension(90, 30));
            editBtn.setFont(Palette.fontPlain(11));
            editBtn.addActionListener(e -> {
                String input = JOptionPane.showInputDialog(this,
                    "Nota para " + est.getNombre() + " en " + m.getNombre() + " (0-100):",
                    nota != null ? String.valueOf(nota.getValor()) : "");
                if (input != null && !input.trim().isEmpty()) {
                    try {
                        double val = Double.parseDouble(input.trim());
                        if (val < 0 || val > 100) {
                            JOptionPane.showMessageDialog(this, "La nota debe estar entre 0 y 100.", "Valor inválido", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        DataStore.getInstance().asignarNota(est.getId(), m.getId(), val);
                        refreshHolder[0].run();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Ingresa un número válido.", "Valor inválido", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            right.add(editBtn);

            JButton removeBtn = UIFactory.dangerButton("✕");
            removeBtn.setPreferredSize(new Dimension(34, 30));
            removeBtn.setFont(Palette.fontPlain(11));
            removeBtn.addActionListener(e -> {
                DataStore.getInstance().quitarMateriaAEstudiante(est.getId(), m.getId());
                refreshHolder[0].run();
            });
            right.add(removeBtn);
        }

        row.add(right, BorderLayout.EAST);
        return row;
    }
}
