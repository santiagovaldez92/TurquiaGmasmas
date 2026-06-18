package turquia.ui;

import turquia.model.*;
import turquia.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Muestra las materias relacionadas con el usuario:
 *  - ESTUDIANTE: sus materias inscritas + su nota en cada una.
 *  - DOCENTE: las materias que imparte y la cantidad de estudiantes inscritos.
 *  - ADMINISTRADOR: vista general de todas las materias con conteo.
 */
public class HorariosPanel extends JPanel {

    public HorariosPanel(Usuario usuario) {
        setLayout(new BorderLayout(0, 0));
        setBackground(Palette.GUINDO_OSCURO);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        header.add(UIFactory.sectionTitle("📅 Horarios y Materias"), BorderLayout.WEST);

        String subtitleText = switch (usuario.getRol()) {
            case ESTUDIANTE -> "Materias en las que estás inscrito";
            case DOCENTE -> "Materias que impartes";
            case ADMINISTRADOR -> "Vista general de materias";
        };
        header.add(UIFactory.subtitle(subtitleText), BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setOpaque(false);

        switch (usuario.getRol()) {
            case ESTUDIANTE -> buildEstudianteView(listContainer, usuario);
            case DOCENTE -> buildDocenteView(listContainer, usuario);
            case ADMINISTRADOR -> buildAdminView(listContainer);
        }

        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Palette.GUINDO_OSCURO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private void buildEstudianteView(JPanel container, Usuario est) {
        List<Integer> ids = est.getMateriasInscritas();
        if (ids.isEmpty()) {
            container.add(emptyMsg("Aún no estás inscrito en ninguna materia. Pídele a un docente que te asigne."));
            return;
        }
        for (int idMat : ids) {
            Materia m = DataStore.getInstance().buscarMateria(idMat);
            if (m == null) continue;
            Nota nota = DataStore.getInstance().buscarNota(est.getId(), m.getId());

            String notaTxt = nota != null ? String.format("%.1f — %s", nota.getValor(), nota.getEstado()) : "Sin calificar";
            Color notaColor = nota == null ? Palette.TEXTO_TENUE :
                (nota.getValor() >= 51 ? Palette.EXITO : Palette.ERROR);

            // Buscar docente(s) que imparten esta materia
            String docenteTxt = buscarDocentes(m.getId());

            container.add(buildMateriaCard(m, "👨‍🏫 " + docenteTxt, notaTxt, notaColor, Palette.CIAN));
            container.add(Box.createVerticalStrut(10));
        }
    }

    private void buildDocenteView(JPanel container, Usuario doc) {
        List<Integer> ids = doc.getMateriasImpartidas();
        if (ids.isEmpty()) {
            container.add(emptyMsg("No tienes materias asignadas todavía. Ve a Materias para asignarte una."));
            return;
        }
        for (int idMat : ids) {
            Materia m = DataStore.getInstance().buscarMateria(idMat);
            if (m == null) continue;

            int inscritos = 0;
            for (Usuario u : DataStore.getInstance().getEstudiantes()) {
                if (u.getMateriasInscritas().contains(m.getId())) inscritos++;
            }

            container.add(buildMateriaCard(m, "👥 " + inscritos + " estudiante(s) inscrito(s)",
                "Ir a Estudiantes para calificar", Palette.TEXTO_GRIS, Palette.MAGENTA));
            container.add(Box.createVerticalStrut(10));
        }
    }

    private void buildAdminView(JPanel container) {
        for (Materia m : DataStore.getInstance().getMaterias()) {
            int inscritos = 0;
            for (Usuario u : DataStore.getInstance().getEstudiantes()) {
                if (u.getMateriasInscritas().contains(m.getId())) inscritos++;
            }
            String docenteTxt = buscarDocentes(m.getId());

            container.add(buildMateriaCard(m, "👨‍🏫 " + docenteTxt, inscritos + " estudiante(s) inscrito(s)",
                Palette.CIAN, Palette.ADVERTENCIA));
            container.add(Box.createVerticalStrut(10));
        }
    }

    private String buscarDocentes(int idMateria) {
        StringBuilder sb = new StringBuilder();
        for (Usuario u : DataStore.getInstance().getDocentes()) {
            if (u.getMateriasImpartidas().contains(idMateria)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(u.getNombre());
            }
        }
        return sb.length() > 0 ? sb.toString() : "Sin docente asignado";
    }

    private RoundedPanel buildMateriaCard(Materia m, String line1, String line2, Color line2Color, Color accent) {
        RoundedPanel card = new RoundedPanel(12, Palette.GUINDO);
        card.setLayout(new BorderLayout(14, 0));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));
        card.setPreferredSize(new Dimension(10, 76));

        JPanel accentBar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(accent);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
            }
        };
        accentBar.setOpaque(false);
        accentBar.setPreferredSize(new Dimension(5, 0));
        card.add(accentBar, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(0, 14, 0, 0));

        JLabel title = new JLabel(m.getSigla() + " — " + m.getNombre());
        title.setFont(Palette.fontBold(15));
        title.setForeground(Palette.TEXTO_CLARO);

        JLabel sub1 = new JLabel(line1);
        sub1.setFont(Palette.fontPlain(12));
        sub1.setForeground(Palette.TEXTO_GRIS);

        info.add(title);
        info.add(Box.createVerticalStrut(2));
        info.add(sub1);
        card.add(info, BorderLayout.CENTER);

        JLabel notaLbl = new JLabel(line2);
        notaLbl.setFont(Palette.fontBold(13));
        notaLbl.setForeground(line2Color);
        card.add(notaLbl, BorderLayout.EAST);

        return card;
    }

    private JLabel emptyMsg(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Palette.fontPlain(13));
        lbl.setForeground(Palette.TEXTO_TENUE);
        lbl.setBorder(new EmptyBorder(20, 0, 0, 0));
        return lbl;
    }
}
