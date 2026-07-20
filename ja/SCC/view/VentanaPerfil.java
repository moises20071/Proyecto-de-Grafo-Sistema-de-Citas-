package SCC.view;

import SCC.model.GCC;
import SCC.model.Usuario;
import SCC.model.Usuario.Gustos;
import SCC.model.Usuario.Sexo;
import SCC.model.Preferencias;
import SCC.model.Preferencias.*;
import SCC.Controller.ControladorSistema;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class VentanaPerfil extends JFrame {

    private GCC sistemaGCC;
    private ControladorSistema controlador;
    private Usuario usuarioActual;
    private JLabel lblInfo; // Se volvió global para actualizar el nombre dinámicamente

    private Map<Gustos, JSlider> mapaSliders;
    private JComboBox<TipodeRelacion> comboRelacion;
    private JComboBox<AceptacionDeMascotas> comboMascotas;
    private JComboBox<ConsumoDeSustancias> comboSustancias;
    private JComboBox<Disponibilidad> comboHorarios;

    public boolean LogOut(){
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas cerrar sesión?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            sistemaGCC.Logout(); 
            return true; 
        }
        return false; 
    }

    public VentanaPerfil(GCC sistemaGCC,ControladorSistema controlador ,JFrame ventanaLoginPadre) {
        this.sistemaGCC = sistemaGCC; 
        this.controlador = controlador;
        this.usuarioActual = sistemaGCC.getUIngresado(); 

        setTitle("\"LinkUp\" - Perfil"); 
        setSize(420, 480); // Ajustado para dar espacio al nuevo botón cómodamente
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        setLocationRelativeTo(null); 
        setResizable(false); 

        JPanel panelContenedor = new JPanel(); 
        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS)); 
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30)); 
        panelContenedor.setBackground(new Color(245, 247, 250)); 

        // --- SECCIÓN 1: BIENVENIDA ---
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        panelInfo.setOpaque(false); 
        String nombreCompleto = (usuarioActual != null) ? usuarioActual.getNombre() + " " + usuarioActual.getApellido() : "Invitado"; 
        lblInfo = new JLabel("¡Bienvenido, " + nombreCompleto + "!"); 
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        lblInfo.setForeground(new Color(44, 62, 80)); 
        panelInfo.add(lblInfo); 
        panelContenedor.add(panelInfo); 
        panelContenedor.add(Box.createVerticalStrut(25)); 

        // --- SECCIÓN 2: BOTONES DE FUNCIONES PRINCIPALES ---
        // Incrementado a 4 filas para el nuevo botón de modificar datos
        JPanel panelFunciones = new JPanel(new GridLayout(4, 1, 0, 15)); 
        panelFunciones.setOpaque(false); 

        Color colorBotonPrincipal = new Color(70, 130, 180); 
        Font fuenteBotones = new Font("Segoe UI", Font.BOLD, 13); 

        JButton btnAbrirGustos = new JButton("Configurar mis Gustos"); 
        btnAbrirGustos.setFont(fuenteBotones); 
        btnAbrirGustos.setBackground(colorBotonPrincipal); 
        btnAbrirGustos.setForeground(Color.WHITE); 
        btnAbrirGustos.setFocusPainted(false); 
        btnAbrirGustos.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); 

        JButton btnAbrirPref = new JButton("Configurar mis Preferencias"); 
        btnAbrirPref.setFont(fuenteBotones); 
        btnAbrirPref.setBackground(colorBotonPrincipal); 
        btnAbrirPref.setForeground(Color.WHITE); 
        btnAbrirPref.setFocusPainted(false); 
        btnAbrirPref.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); 

        JButton btnVerPareja = new JButton("Ver mi Media Naranja!");
        btnVerPareja.setFont(fuenteBotones);
        btnVerPareja.setBackground(colorBotonPrincipal);
        btnVerPareja.setForeground(Color.WHITE);
        btnVerPareja.setFocusPainted(false);
        btnVerPareja.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // NUEVO BOTÓN: Modificar Datos
        JButton btnModificarDatos = new JButton("Modificar Datos");
        btnModificarDatos.setFont(fuenteBotones);
        btnModificarDatos.setBackground(new Color(100, 110, 120));
        btnModificarDatos.setForeground(Color.WHITE);
        btnModificarDatos.setFocusPainted(false);
        btnModificarDatos.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        panelFunciones.add(btnAbrirGustos); 
        panelFunciones.add(btnAbrirPref); 
        panelFunciones.add(btnVerPareja); 
        panelFunciones.add(btnModificarDatos); // Insertado en el panel
        panelContenedor.add(panelFunciones); 
        panelContenedor.add(Box.createVerticalStrut(35)); 

        // --- SECCIÓN 3: BOTÓN DE LOGOUT ---
        JButton btnLogout = new JButton("Cerrar Sesión (Logout)"); 
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT); 
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12)); 
        btnLogout.setBackground(new Color(178, 34, 34)); 
        btnLogout.setForeground(Color.WHITE); 
        btnLogout.setFocusPainted(false); 
        btnLogout.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20)); 
        panelContenedor.add(btnLogout); 

        add(panelContenedor); 

        // --- GESTIÓN DE EVENTOS ---
        btnAbrirGustos.addActionListener(e -> abrirModalGustos()); 
        btnAbrirPref.addActionListener(e -> abrirModalPreferencias()); 
        btnVerPareja.addActionListener(e -> abrirModalVerPareja());
        btnModificarDatos.addActionListener(e -> abrirModalModificarDatos()); // Acción del nuevo botón

        btnLogout.addActionListener(e -> {
            if(LogOut()){ 
                ventanaLoginPadre.setVisible(true); 
                VentanaPerfil.this.dispose(); 
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if(LogOut()){ 
                    ventanaLoginPadre.setVisible(true); 
                    VentanaPerfil.this.dispose(); 
                }
            }
        });
    }

    // --- NUEVO DIÁLOGO MODAL: MENÚ MODIFICAR DATOS ---
    private void abrirModalModificarDatos() {
        JDialog dialogMenu = new JDialog(this, "Modificar Datos de Cuenta", true);
        dialogMenu.setSize(400, 220);
        dialogMenu.setLocationRelativeTo(this);
        dialogMenu.setResizable(false);

        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelMenu.setBackground(new Color(245, 247, 250));

        JButton btnRehacerDatos = new JButton("Rehacer Datos Personales");
        JButton btnCambiarClave = new JButton("Cambiar Clave de Login");
        JButton btnElimCuenta = new JButton("Eliminar Cuenta");

        Font fuenteBtn = new Font("Segoe UI", Font.BOLD, 12);

        btnRehacerDatos.setFont(fuenteBtn);
        btnRehacerDatos.setBackground(new Color(100, 110, 120));
        btnRehacerDatos.setForeground(Color.WHITE);
        btnRehacerDatos.setFocusPainted(false);
        btnRehacerDatos.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRehacerDatos.setMaximumSize(new Dimension(220, 35));

        btnCambiarClave.setFont(fuenteBtn);
        btnCambiarClave.setBackground(new Color(100, 110, 120));
        btnCambiarClave.setForeground(Color.WHITE);
        btnCambiarClave.setFocusPainted(false);
        btnCambiarClave.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCambiarClave.setMaximumSize(new Dimension(220, 35));

        // Configuración y diseño del botón Eliminar
        btnElimCuenta.setFont(fuenteBtn);
        btnElimCuenta.setBackground(new Color(178, 34, 34)); 
        btnElimCuenta.setForeground(Color.WHITE); 
        btnElimCuenta.setFocusPainted(false);
        btnElimCuenta.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); 
        btnElimCuenta.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnElimCuenta.setMaximumSize(new Dimension(160, 32));

        // Agregamos los dos primeros botones directo a la cuadrícula (ocuparán todo el ancho)
        panelMenu.add(btnRehacerDatos);
        panelMenu.add(btnCambiarClave);

        btnRehacerDatos.addActionListener(e -> {
            dialogMenu.dispose();
            abrirModalRehacerDatos();
        });

        btnCambiarClave.addActionListener(e -> {
            dialogMenu.dispose();
            abrirModalCambiarClave();
        });

        btnElimCuenta.addActionListener(e -> {
            dialogMenu.dispose();
            int respuesta = JOptionPane.showConfirmDialog(this,
                "Desea borrar su Cuenta?",
                "Confirmacion",
                JOptionPane.YES_NO_OPTION
            );

            if(respuesta == JOptionPane.YES_OPTION){
                JOptionPane.showMessageDialog(this,"Gracias por usar LinkUp!");
                sistemaGCC.ElimCuenta(sistemaGCC.getUIngresado());
                controlador.regresarAlMenuGeneral();
            }
        });

        panelMenu.add(btnRehacerDatos);
        panelMenu.add(Box.createVerticalStrut(12)); 
        panelMenu.add(btnCambiarClave);
        panelMenu.add(Box.createVerticalStrut(20)); 
        panelMenu.add(btnElimCuenta);
        dialogMenu.add(panelMenu);
        dialogMenu.setVisible(true);
    }

    // --- MODAL: REHACER DATOS PERSONALES ---
    private void abrirModalRehacerDatos() {
        JDialog dialog = new JDialog(this, "Rehacer Datos Personales", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtNombre = new JTextField(usuarioActual.getNombre());
        JTextField txtApellido = new JTextField(usuarioActual.getApellido());
        JTextField txtEdad = new JTextField(String.valueOf(usuarioActual.toString().replaceAll(".*Edad: ", "").trim()));

        panelForm.add(new JLabel("Nuevo Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Nuevo Apellido:"));
        panelForm.add(txtApellido);
        panelForm.add(new JLabel("Nueva Edad:"));
        panelForm.add(txtEdad);

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBackground(new Color(46, 139, 87));
        btnGuardar.setForeground(Color.WHITE);
        
        btnGuardar.addActionListener(ev -> {
            try {
                String nom = txtNombre.getText().trim();
                String ape = txtApellido.getText().trim();
                int edad = Integer.parseInt(txtEdad.getText().trim());

                if(nom.isEmpty() || ape.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Los campos no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                sistemaGCC.modifCuenta(nom, ape, edad);
                usuarioActual = sistemaGCC.getUIngresado(); // Sincronizamos la referencia local
                lblInfo.setText("¡Bienvenido, " + usuarioActual.getNombre() + " " + usuarioActual.getApellido() + "!");
                
                JOptionPane.showMessageDialog(dialog, "Datos personales actualizados con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Por favor, introduce una edad válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setLayout(new BorderLayout());
        dialog.add(panelForm, BorderLayout.CENTER);
        dialog.add(btnGuardar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // --- MODAL: CAMBIAR CLAVE DE LOGIN ---
    private void abrirModalCambiarClave() {
        JDialog dialog = new JDialog(this, "Cambiar Credencial Login", true);
        dialog.setSize(350, 180);
        dialog.setLocationRelativeTo(this);

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPasswordField txtNuevaClave = new JPasswordField();

        panelForm.add(new JLabel("Nueva Clave / Credencial:"));
        panelForm.add(txtNuevaClave);

        JButton btnGuardar = new JButton("Actualizar Credencial");
        btnGuardar.setBackground(new Color(46, 139, 87));
        btnGuardar.setForeground(Color.WHITE);

        btnGuardar.addActionListener(ev -> {
            String nuevaClave = new String(txtNuevaClave.getPassword()).trim();
            if(nuevaClave.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "La contraseña no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            sistemaGCC.modifCredencial(nuevaClave);
            JOptionPane.showMessageDialog(dialog, "Credencial de acceso actualizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        dialog.setLayout(new BorderLayout());
        dialog.add(panelForm, BorderLayout.CENTER);
        dialog.add(btnGuardar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // --- DIÁLOGO MODAL: MOSTRAR PAREJA ASIGNADA ---
    private void abrirModalVerPareja() {
        JDialog dialog = new JDialog(this, "Tu Cita Perfecta", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panelRaiz = new JPanel(new BorderLayout(15, 15));
        panelRaiz.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelRaiz.setBackground(new Color(250, 250, 250));

        JLabel lblTitulo = new JLabel("Resultado del Emparejamiento", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(new Color(70, 130, 180));
        panelRaiz.add(lblTitulo, BorderLayout.NORTH);

        Usuario pareja = sistemaGCC.getPareja();

        JTextArea txtDetalle = new JTextArea();
        txtDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDetalle.setForeground(new Color(44, 62, 80));
        txtDetalle.setEditable(false);
        txtDetalle.setHighlighter(null); 
        txtDetalle.setOpaque(false);    
        txtDetalle.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (pareja != null) {
            String mensaje = "¡Felicidades! Se ha encontrado tu match ideal:\n\n"
                           + "Nombre: " + pareja.getNombre() + " " + pareja.getApellido() + "\n"
                           + "Género: " + pareja.getSexo() + "\n"
                           + "Edad: " + pareja.toString().replaceAll(".*Edad: ", "");
            txtDetalle.setText(mensaje);
        } else {
            String mensaje = "Por el momento no hemos encontrado tu media naranja.\n\n"
                           + "Inténtalo más tarde.";
            txtDetalle.setText(mensaje);
        }
        
        JPanel panelCentrado = new JPanel(new GridBagLayout());
        panelCentrado.setOpaque(false);
        panelCentrado.add(txtDetalle);
        
        panelRaiz.add(panelCentrado, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Entendido");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrar.setBackground(new Color(46, 139, 87));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        btnCerrar.addActionListener(e -> dialog.dispose());

        panelRaiz.add(btnCerrar, BorderLayout.SOUTH);

        dialog.add(panelRaiz);
        dialog.setVisible(true);
    }

    // --- DIÁLOGO MODAL: GUSTOS ---
    private void abrirModalGustos() {
        JDialog dialog = new JDialog(this, "Mis Gustos", true); 
        dialog.setSize(550, 450); 
        dialog.setLocationRelativeTo(this); 

        JPanel panelRaiz = new JPanel(new BorderLayout(10, 15)); 
        panelRaiz.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

        JTabbedPane pestañasGustos = new JTabbedPane(); 
        pestañasGustos.setFont(new Font("Segoe UI", Font.PLAIN, 12)); 
        
        JPanel panelOpcionCultural = new JPanel(new GridLayout(0, 1, 5, 10)); 
        JPanel panelOpcionActividades = new JPanel(new GridLayout(0, 1, 5, 10)); 

        mapaSliders = new HashMap<>(); 
        Map<Gustos, Integer> gustosActuales = (usuarioActual != null) ? usuarioActual.getGustos() : null; 

        for (Gustos gusto : Gustos.values()) { 
            JPanel panelFilaGusto = new JPanel(new BorderLayout(15, 0)); 
            JLabel lblGusto = new JLabel(gusto.name() + ": ", JLabel.RIGHT); 
            lblGusto.setPreferredSize(new Dimension(100, 20)); 
            lblGusto.setFont(new Font("Segoe UI", Font.PLAIN, 12)); 

            int valorInicial = (gustosActuales != null && gustosActuales.containsKey(gusto)) ? gustosActuales.get(gusto) : 5; 
            if (valorInicial > 10) valorInicial /= 10; 

            JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 10, valorInicial); 
            slider.setMajorTickSpacing(2); 
            slider.setMinorTickSpacing(1); 
            slider.setPaintTicks(true); 
            slider.setPaintLabels(true); 
            slider.setFont(new Font("Segoe UI", Font.PLAIN, 10)); 

            panelFilaGusto.add(lblGusto, BorderLayout.WEST); 
            panelFilaGusto.add(slider, BorderLayout.CENTER); 
            mapaSliders.put(gusto, slider); 

            if (gusto == Gustos.Literatura || gusto == Gustos.Musica || gusto == Gustos.Artes) { 
                panelOpcionCultural.add(panelFilaGusto); 
            } else {
                panelOpcionActividades.add(panelFilaGusto); 
            }
        }

        pestañasGustos.addTab("Arte y Cultura", panelOpcionCultural); 
        pestañasGustos.addTab("Ocio y Deportes", panelOpcionActividades); 
        panelRaiz.add(pestañasGustos, BorderLayout.CENTER); 

        JButton btnGuardarGustos = new JButton("Guardar Gustos"); 
        btnGuardarGustos.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        btnGuardarGustos.setBackground(new Color(46, 139, 87)); 
        btnGuardarGustos.setForeground(Color.WHITE); 
        btnGuardarGustos.setFocusPainted(false); 
        btnGuardarGustos.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); 
        
        btnGuardarGustos.addActionListener(e -> {
            Map<Gustos, Integer> nuevosGustos = new HashMap<>();
            for (Map.Entry<Gustos, JSlider> entrada : mapaSliders.entrySet()) {
                nuevosGustos.put(entrada.getKey(), entrada.getValue().getValue());
            }
            
            sistemaGCC.modifGustos(nuevosGustos);
            this.revalidate();
            this.repaint();
            
            JOptionPane.showMessageDialog(dialog, "Gustos guardados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        panelRaiz.add(btnGuardarGustos, BorderLayout.SOUTH); 
        dialog.add(panelRaiz); 
        dialog.setVisible(true); 
    }

    // --- DIÁLOGO MODAL: PREFERENCIAS ---
    private void abrirModalPreferencias() {
        JDialog dialog = new JDialog(this, "Mis Preferencias", true); 
        dialog.setSize(450, 320); 
        dialog.setLocationRelativeTo(this); 

        JPanel panelRaiz = new JPanel(new BorderLayout(10, 15)); 
        panelRaiz.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

        JPanel panelCampos = new JPanel(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.insets = new Insets(8, 8, 8, 8); 

        comboRelacion = new JComboBox<>(TipodeRelacion.values()); 
        comboMascotas = new JComboBox<>(AceptacionDeMascotas.values()); 
        comboSustancias = new JComboBox<>(ConsumoDeSustancias.values()); 
        comboHorarios = new JComboBox<>(Disponibilidad.values()); 

        if (usuarioActual != null && usuarioActual.getPreferencias() != null) { 
            Preferencias prefPrevias = usuarioActual.getPreferencias(); 
            comboRelacion.setSelectedItem(prefPrevias.getPrefRelacion()); 
            comboMascotas.setSelectedItem(prefPrevias.getPrefMascotas()); 
            comboSustancias.setSelectedItem(prefPrevias.getPrefConsumo()); 
            comboHorarios.setSelectedItem(prefPrevias.getPrefHorarios()); 
        }

        Font fuenteLabels = new Font("Segoe UI", Font.PLAIN, 12); 
        JLabel l1 = new JLabel("Tipo de Relación:"); l1.setFont(fuenteLabels); 
        JLabel l2 = new JLabel("Mascotas:"); l2.setFont(fuenteLabels); 
        JLabel l3 = new JLabel("Habitos de Vida"); l3.setFont(fuenteLabels); 
        JLabel l4 = new JLabel("Disponibilidad:"); l4.setFont(fuenteLabels); 

        gbc.gridx = 0; gbc.gridy = 0; panelCampos.add(l1, gbc); 
        gbc.gridx = 1; panelCampos.add(comboRelacion, gbc); 
        gbc.gridx = 0; gbc.gridy = 1; panelCampos.add(l2, gbc); 
        gbc.gridx = 1; panelCampos.add(comboMascotas, gbc); 
        gbc.gridx = 0; gbc.gridy = 2; panelCampos.add(l3, gbc); 
        gbc.gridx = 1; panelCampos.add(comboSustancias, gbc); 
        gbc.gridx = 0; gbc.gridy = 3; panelCampos.add(l4, gbc); 
        gbc.gridx = 1; panelCampos.add(comboHorarios, gbc); 

        panelRaiz.add(panelCampos, BorderLayout.CENTER); 

        JButton btnGuardarPref = new JButton("Guardar Preferencias"); 
        btnGuardarPref.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        btnGuardarPref.setBackground(new Color(46, 139, 87)); 
        btnGuardarPref.setForeground(Color.WHITE); 
        btnGuardarPref.setFocusPainted(false); 
        btnGuardarPref.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); 

        btnGuardarPref.addActionListener(e -> {
            TipodeRelacion rel = (TipodeRelacion) comboRelacion.getSelectedItem();
            AceptacionDeMascotas mas = (AceptacionDeMascotas) comboMascotas.getSelectedItem();
            ConsumoDeSustancias sus = (ConsumoDeSustancias) comboSustancias.getSelectedItem();
            Disponibilidad disp = (Disponibilidad) comboHorarios.getSelectedItem();

            Preferencias nuevasPref = new Preferencias(rel, mas, sus, disp);
            
            sistemaGCC.modifPref(nuevasPref);
            this.revalidate();
            this.repaint();

            JOptionPane.showMessageDialog(dialog, "Preferencias guardadas correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        panelRaiz.add(btnGuardarPref, BorderLayout.SOUTH); 
        dialog.add(panelRaiz); 
        dialog.setVisible(true); 
    }
}