package turquia.ui;

import turquia.model.*;
import turquia.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Gestión de usuarios. Solo accesible para ADMINISTRADOR.
 * Permite ver lista completa, crear, editar y eliminar usuarios.
 */
public class UsuariosPanel extends JPanel {

    private final Usuario admin;
    private JPanel listContainer;

    public UsuariosPanel(Usuario admin) {
        this.admin = admin;
        setLayout(new BorderLayout(0, 0));
        setBackground(Palette.GUINDO_OSCURO);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        header.add(UIFactory.sectionTitle("🛠 Gestión de Usuarios"), BorderLayout.WEST);

        JButton nuevoBtn = UIFactory.accentButton("➕ Nuevo usuario");
        nuevoBtn.setPreferredSize(new Dimension(160, 38));
        nuevoBtn.addActionListener(e -> abrirFormulario(null));
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(nuevoBtn);
        header.add(right, BorderLayout.EAST);
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
        List<Usuario> usuarios = DataStore.getInstance().getUsuarios();

        for (Usuario u : usuarios) {
            listContainer.add(buildRow(u));
            listContainer.add(Box.createVerticalStrut(10));
        }
        listContainer.revalidate();
        listContainer.repaint();
    }

    private RoundedPanel buildRow(Usuario u) {
        RoundedPanel row = new RoundedPanel(12, Palette.GUINDO);
        row.setLayout(new BorderLayout(14, 0));
        row.setBorder(new EmptyBorder(16, 20, 16, 20));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setPreferredSize(new Dimension(10, 70));

        Color rolColor = switch (u.getRol()) {
            case ESTUDIANTE -> Palette.CIAN;
            case DOCENTE -> Palette.MAGENTA;
            case ADMINISTRADOR -> Palette.ADVERTENCIA;
        };

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        left.setOpaque(false);

        JLabel avatar = new JLabel("●");
        avatar.setFont(new Font("SansSerif", Font.BOLD, 26));
        avatar.setForeground(rolColor);
        left.add(avatar);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);
        JLabel name = new JLabel(u.getNombre());
        name.setFont(Palette.fontBold(14));
        name.setForeground(Palette.TEXTO_CLARO);
        JLabel sub = new JLabel("@" + u.getUsername() + "  ·  " + u.getRolEtiqueta());
        sub.setFont(Palette.fontPlain(12));
        sub.setForeground(Palette.TEXTO_GRIS);
        namePanel.add(name);
        namePanel.add(sub);
        left.add(namePanel);

        row.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        JButton editBtn = UIFactory.primaryButton("Editar");
        editBtn.setPreferredSize(new Dimension(90, 36));
        editBtn.addActionListener(e -> abrirFormulario(u));
        right.add(editBtn);

        JButton delBtn = UIFactory.dangerButton("Eliminar");
        delBtn.setPreferredSize(new Dimension(90, 36));
        if (u.getId() == admin.getId()) {
            delBtn.setEnabled(false);
            delBtn.setToolTipText("No puedes eliminar tu propia cuenta");
        } else {
            delBtn.addActionListener(e -> {
                int res = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar al usuario \"" + u.getNombre() + "\"? Esta acción no se puede deshacer.",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (res == JOptionPane.YES_OPTION) {
                    DataStore.getInstance().eliminarUsuario(u.getId());
                    refresh();
                }
            });
        }
        right.add(delBtn);

        row.add(right, BorderLayout.EAST);
        return row;
    }

    /** Formulario para crear (usuario == null) o editar usuario existente. */
    private void abrirFormulario(Usuario usuarioEditar) {
        boolean esEdicion = usuarioEditar != null;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            esEdicion ? "Editar usuario" : "Nuevo usuario", true);
        dialog.setSize(420, 420);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Palette.GUINDO_OSCURO);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Palette.GUINDO_OSCURO);
        content.setBorder(new EmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.insets = new Insets(4, 0, 4, 0);

        JLabel title = new JLabel(esEdicion ? "✏️ Editar usuario" : "➕ Crear nuevo usuario");
        title.setFont(Palette.fontBold(17));
        title.setForeground(Palette.CIAN);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 16, 0);
        content.add(title, gbc);

        JLabel nombreLbl = label("Nombre completo:");
        gbc.gridy = 1; gbc.insets = new Insets(4, 0, 2, 0);
        content.add(nombreLbl, gbc);

        JTextField nombreField = UIFactory.textField();
        if (esEdicion) nombreField.setText(usuarioEditar.getNombre());
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 10, 0);
        content.add(nombreField, gbc);

        JLabel userLbl = label("Usuario:");
        gbc.gridy = 3; gbc.insets = new Insets(4, 0, 2, 0);
        content.add(userLbl, gbc);

        JTextField userField = UIFactory.textField();
        if (esEdicion) userField.setText(usuarioEditar.getUsername());
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 10, 0);
        content.add(userField, gbc);

        JLabel passLbl = label("Contraseña:" + (esEdicion ? " (dejar igual si no cambia)" : ""));
        gbc.gridy = 5; gbc.insets = new Insets(4, 0, 2, 0);
        content.add(passLbl, gbc);

        JTextField passField = UIFactory.textField();
        if (esEdicion) passField.setText(usuarioEditar.getPassword());
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 10, 0);
        content.add(passField, gbc);

        JLabel rolLbl = label("Rol:");
        gbc.gridy = 7; gbc.insets = new Insets(4, 0, 2, 0);
        content.add(rolLbl, gbc);

        String[] roles = {"Estudiante", "Docente", "Administrador"};
        JComboBox<String> rolCombo = UIFactory.comboBox(roles);
        if (esEdicion) {
            rolCombo.setSelectedIndex(switch (usuarioEditar.getRol()) {
                case ESTUDIANTE -> 0;
                case DOCENTE -> 1;
                case ADMINISTRADOR -> 2;
            });
        }
        gbc.gridy = 8; gbc.insets = new Insets(0, 0, 20, 0);
        content.add(rolCombo, gbc);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);

        JButton cancelBtn = UIFactory.ghostButton("Cancelar");
        cancelBtn.setPreferredSize(new Dimension(100, 38));
        cancelBtn.addActionListener(e -> dialog.dispose());
        btnRow.add(cancelBtn);

        JButton saveBtn = UIFactory.accentButton(esEdicion ? "Guardar cambios" : "Crear usuario");
        saveBtn.setPreferredSize(new Dimension(150, 38));
        saveBtn.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String username = userField.getText().trim();
            String pass = passField.getText().trim();

            if (nombre.isEmpty() || username.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Rol rolSeleccionado = switch (rolCombo.getSelectedIndex()) {
                case 0 -> Rol.ESTUDIANTE;
                case 1 -> Rol.DOCENTE;
                default -> Rol.ADMINISTRADOR;
            };

            if (esEdicion) {
                // Validar username único si cambió
                if (!username.equalsIgnoreCase(usuarioEditar.getUsername()) && DataStore.getInstance().existeUsername(username)) {
                    JOptionPane.showMessageDialog(dialog, "Ese nombre de usuario ya existe.", "Usuario duplicado", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                usuarioEditar.setNombre(nombre);
                usuarioEditar.setUsername(username);
                usuarioEditar.setPassword(pass);
                usuarioEditar.setRol(rolSeleccionado);
            } else {
                if (DataStore.getInstance().existeUsername(username)) {
                    JOptionPane.showMessageDialog(dialog, "Ese nombre de usuario ya existe.", "Usuario duplicado", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                DataStore.getInstance().crearUsuario(nombre, username, pass, rolSeleccionado);
            }

            dialog.dispose();
            refresh();
        });
        btnRow.add(saveBtn);

        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 0, 0);
        content.add(btnRow, gbc);

        dialog.setContentPane(content);
        dialog.setVisible(true);
    }

    private JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Palette.fontPlain(12));
        lbl.setForeground(Palette.TEXTO_GRIS);
        return lbl;
    }
}
