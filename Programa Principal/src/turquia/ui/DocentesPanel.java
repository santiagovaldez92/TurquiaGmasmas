package turquia.ui;

import turquia.model.*;
import turquia.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Lista de docentes. Todos los roles que pueden acceder ven la lista
 * y el detalle (nombre, materias que imparte). El administrador además
 * puede asignar/quitar materias a los docentes.
 */
public class DocentesPanel extends JPanel {

    private final Usuario usuarioActual;
    private JPanel listContainer;

    public DocentesPanel(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout(0, 0));
        setBackground(Palette.GUINDO_OSCURO);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        header.add(UIFactory.sectionTitle("👨‍🏫 Docentes"), BorderLayout.WEST);
        header.add(UIFactory.subtitle(DataStore.getInstance().getDocentes().size() + " docente(s) registrados"),
                BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setOpaque(false);

        refreshList();

        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Palette.GUINDO_OSCURO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private void refreshList() {
        listContainer.removeAll();
        List<Usuario> docentes = DataStore.getInstance().getDocentes();

        if (docentes.isEmpty()) {
            JLabel empty = new JLabel("No hay docentes registrados.");
            empty.setFont(Palette.fontPlain(13));
            empty.setForeground(Palette.TEXTO_TENUE);
            listContainer.add(empty);
        }

        for (Usuario doc : docentes) {
            listContainer.add(buildRow(doc));
            listContainer.add(Box.createVerticalStrut(10));
        }
        listContainer.revalidate();
        listContainer.repaint();
    }

    private RoundedPanel buildRow(Usuario doc) {
        RoundedPanel row = new RoundedPanel(12, Palette.GUINDO);
        row.setLayout(new BorderLayout(14, 0));
        row.setBorder(new EmptyBorder(16, 20, 16, 20));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setPreferredSize(new Dimension(10, 70));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        left.setOpaque(false);

        JLabel avatar = new JLabel("●");
        avatar.setFont(new Font("SansSerif", Font.BOLD, 26));
        avatar.setForeground(Palette.MAGENTA);
        left.add(avatar);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);
        JLabel name = new JLabel(doc.getNombre());
        name.setFont(Palette.fontBold(14));
        name.setForeground(Palette.TEXTO_CLARO);

        StringBuilder materiasTxt = new StringBuilder();
        for (int idMat : doc.getMateriasImpartidas()) {
            Materia m = DataStore.getInstance().buscarMateria(idMat);
            if (m != null) {
                if (materiasTxt.length() > 0)
                    materiasTxt.append(", ");
                materiasTxt.append(m.getSigla());
            }
        }
        String materiasStr = materiasTxt.length() > 0 ? materiasTxt.toString() : "Sin materias asignadas";

        JLabel sub = new JLabel("@" + doc.getUsername() + "  ·  " + materiasStr);
        sub.setFont(Palette.fontPlain(12));
        sub.setForeground(Palette.TEXTO_GRIS);
        namePanel.add(name);
        namePanel.add(sub);
        left.add(namePanel);

        row.add(left, BorderLayout.WEST);

        JButton verBtn = UIFactory.primaryButton("Ver perfil");
        verBtn.setPreferredSize(new Dimension(110, 36));
        verBtn.addActionListener(e -> abrirDetalle(doc));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(verBtn);
        row.add(right, BorderLayout.EAST);

        return row;
    }

    private void abrirDetalle(Usuario doc) {
        boolean puedeGestionar = usuarioActual.getRol() == Rol.ADMINISTRADOR;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Perfil de " + doc.getNombre(), true);
        dialog.setSize(520, 480);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Palette.GUINDO_OSCURO);
        dialog.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Palette.GUINDO_OSCURO);
        content.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel(doc.getNombre());
        title.setFont(Palette.fontBold(20));
        title.setForeground(Palette.TEXTO_CLARO);
        content.add(title);

        JLabel subt = new JLabel("@" + doc.getUsername() + "  ·  Docente");
        subt.setFont(Palette.fontPlain(13));
        subt.setForeground(Palette.TEXTO_GRIS);
        subt.setBorder(new EmptyBorder(4, 0, 18, 0));
        content.add(subt);

        JLabel materiasLbl = new JLabel("📚 Materias que imparte");
        materiasLbl.setFont(Palette.fontBold(14));
        materiasLbl.setForeground(Palette.MAGENTA);
        materiasLbl.setBorder(new EmptyBorder(0, 0, 8, 0));
        content.add(materiasLbl);

        RoundedPanel materiasBox = new RoundedPanel(10, Palette.GUINDO);
        materiasBox.setLayout(new BoxLayout(materiasBox, BoxLayout.Y_AXIS));
        materiasBox.setBorder(new EmptyBorder(10, 14, 10, 14));

        Runnable[] refreshHolder = new Runnable[1];
        refreshHolder[0] = () -> {
            materiasBox.removeAll();
            List<Integer> ids = doc.getMateriasImpartidas();
            if (ids.isEmpty()) {
                JLabel empty = new JLabel("No imparte materias actualmente.");
                empty.setFont(Palette.fontPlain(12));
                empty.setForeground(Palette.TEXTO_TENUE);
                materiasBox.add(empty);
            }
            for (int idMat : ids) {
                Materia m = DataStore.getInstance().buscarMateria(idMat);
                if (m == null)
                    continue;
                materiasBox.add(buildMateriaRow(doc, m, puedeGestionar, refreshHolder));
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

        if (puedeGestionar) {
            content.add(Box.createVerticalStrut(16));
            JLabel asignarLbl = new JLabel("➕ Asignar nueva materia");
            asignarLbl.setFont(Palette.fontBold(14));
            asignarLbl.setForeground(Palette.CIAN);
            asignarLbl.setBorder(new EmptyBorder(0, 0, 8, 0));
            content.add(asignarLbl);

            JPanel addRow = new JPanel(new BorderLayout(10, 0));
            addRow.setOpaque(false);
            addRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            List<Materia> todas = DataStore.getInstance().getMaterias();
            String[] nombres = new String[todas.size()];
            for (int i = 0; i < todas.size(); i++)
                nombres[i] = todas.get(i).getNombre();
            JComboBox<String> combo = UIFactory.comboBox(nombres);

            JButton addBtn = UIFactory.accentButton("Asignar");
            addBtn.setPreferredSize(new Dimension(110, 36));
            addBtn.addActionListener(e -> {
                int idx = combo.getSelectedIndex();
                if (idx < 0)
                    return;
                Materia m = todas.get(idx);
                if (doc.getMateriasImpartidas().contains(m.getId())) {
                    JOptionPane.showMessageDialog(dialog, "El docente ya imparte esa materia.", "Aviso",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                DataStore.getInstance().asignarMateriaADocente(doc.getId(), m.getId());
                refreshHolder[0].run();
            });

            addRow.add(combo, BorderLayout.CENTER);
            addRow.add(addBtn, BorderLayout.EAST);
            content.add(addRow);
        }

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

    private JPanel buildMateriaRow(Usuario doc, Materia m, boolean puedeGestionar, Runnable[] refreshHolder) {
        RoundedPanel row = new RoundedPanel(8, Palette.GUINDO_CLARO);
        row.setLayout(new BorderLayout(10, 0));
        row.setBorder(new EmptyBorder(8, 12, 8, 12));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        row.setPreferredSize(new Dimension(10, 44));

        JLabel nameLbl = new JLabel(m.getSigla() + " · " + m.getNombre());
        nameLbl.setFont(Palette.fontPlain(13));
        nameLbl.setForeground(Palette.TEXTO_CLARO);
        row.add(nameLbl, BorderLayout.WEST);

        if (puedeGestionar) {
            JButton removeBtn = UIFactory.dangerButton("Quitar");
            removeBtn.setPreferredSize(new Dimension(80, 30));
            removeBtn.setFont(Palette.fontPlain(11));
            removeBtn.addActionListener(e -> {
                DataStore.getInstance().quitarMateriaADocente(doc.getId(), m.getId());
                refreshHolder[0].run();
            });
            row.add(removeBtn, BorderLayout.EAST);
        }

        return row;
    }
}
