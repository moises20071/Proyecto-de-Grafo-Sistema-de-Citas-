package SCC.view;

import SCC.model.GCC;
import SCC.model.Usuario.Sexo;
import SCC.Controller.ControladorSistema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaAutenticacion extends JFrame {

    private GCC sistemaGCC;
    private ControladorSistema controlador;

    // Componentes de Login
    private JTextField txtLoginNombre;
    private JTextField txtLoginApellido;
    private JPasswordField txtLoginClave;

    // Componentes de Registro
    private JTextField txtRegNombre;
    private JTextField txtRegApellido;
    private JSpinner spinnerEdad;
    private JComboBox<Sexo> comboSexo;
    private JPasswordField txtRegClave;

    // Recibe el sistema y un String ("LOGIN" o "REGISTRO") para determinar qué panel montar
    public VentanaAutenticacion(GCC sistemaGCC,ControladorSistema Controller, String vistaInicial) {

        this.sistemaGCC = sistemaGCC;
        this.controlador = Controller;

        setTitle("Sistema de Citas a Ciegas \"LinkUp\" - " + (vistaInicial.equals("LOGIN") ? "Iniciar Sesión" : "Registro"));
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false);

        if (vistaInicial.equalsIgnoreCase("LOGIN")) {
            add(crearPanelLogin());
        } else {
            add(crearPanelRegistro());
        }
    }

    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Componentes
        JLabel lblNombre = new JLabel("Nombre:");
        txtLoginNombre = new JTextField(15);

        JLabel lblApellido = new JLabel("Apellido:");
        txtLoginApellido = new JTextField(15);

        JLabel lblClave = new JLabel("Clave / ID:");
        txtLoginClave = new JPasswordField(15);

        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(new Color(70, 130, 180));
        btnIngresar.setForeground(Color.WHITE);

        JButton btnAtras = new JButton("Atras");
        btnAtras.setBackground(new Color(178, 34, 34));
        btnAtras.setForeground(Color.WHITE);

        // Posicionamiento en el Grid
        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblNombre, gbc);
        gbc.gridx = 1; panel.add(txtLoginNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblApellido, gbc);
        gbc.gridx = 1; panel.add(txtLoginApellido, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblClave, gbc);
        gbc.gridx = 1; panel.add(txtLoginClave, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnIngresar, gbc);
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnAtras,gbc);

        // Evento de Login
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionLogin();
            }
        });

        btnAtras.addActionListener(e -> {controlador.regresarAlMenuGeneral();});

        return panel;
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Componentes
        JLabel lblNombre = new JLabel("Nombre:");
        txtRegNombre = new JTextField(15);

        JLabel lblApellido = new JLabel("Apellido:");
        txtRegApellido = new JTextField(15);

        JLabel lblSexo = new JLabel("Género:");
        comboSexo = new JComboBox<>(Sexo.values());

        JLabel lblEdad = new JLabel("Edad:");
        spinnerEdad = new JSpinner(new SpinnerNumberModel(18, 18, 100, 1));

        JLabel lblClave = new JLabel("Clave / ID único:");
        txtRegClave = new JPasswordField(15);

        JButton btnRegistrar = new JButton("Crear Cuenta");
        btnRegistrar.setBackground(new Color(46, 139, 87));
        btnRegistrar.setForeground(Color.WHITE);

        JButton btnAtras = new JButton("Atras");
        btnAtras.setBackground(new Color(178, 34, 34));
        btnAtras.setForeground(Color.WHITE);

        // Posicionamiento
        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblNombre, gbc);
        gbc.gridx = 1; panel.add(txtRegNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblApellido, gbc);
        gbc.gridx = 1; panel.add(txtRegApellido, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblSexo, gbc);
        gbc.gridx = 1; panel.add(comboSexo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(lblEdad, gbc);
        gbc.gridx = 1; panel.add(spinnerEdad, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(lblClave, gbc);
        gbc.gridx = 1; panel.add(txtRegClave, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnRegistrar, gbc);
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnAtras,gbc);
        // Evento de Registro
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionRegistro();
            }
        });
        
        btnAtras.addActionListener(e -> {controlador.regresarAlMenuGeneral();});

        return panel;

    }

    private void accionLogin() {
        String nombre = txtLoginNombre.getText().trim();
        String apellido = txtLoginApellido.getText().trim();
        String clave = new String(txtLoginClave.getPassword()).trim();

        if (nombre.isEmpty() || apellido.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, llene todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        sistemaGCC.Login(clave, nombre, apellido);

        // Verificación de éxito de sesión
        if (sistemaGCC.getUIngresado() != null) {
            JOptionPane.showMessageDialog(this, "¡Inicio de sesión exitoso!", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
            
            controlador.procesarLoginExitoso(); 

        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas. Verifique Clave, Nombre y Apellido.", "Error de Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionRegistro() {
        String nombre = txtRegNombre.getText().trim();
        String apellido = txtRegApellido.getText().trim();
        Sexo genero = (Sexo) comboSexo.getSelectedItem();
        int edad = (int) spinnerEdad.getValue();
        String clave = new String(txtRegClave.getPassword()).trim();

        if (nombre.isEmpty() || apellido.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos de registro.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(!sistemaGCC.CrearCuenta(nombre, apellido, genero, edad, clave)){
            JOptionPane.showMessageDialog(this, "Contraseña ya existente, por Favor ingrese Otra", "Registro Fallido", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(this, "¡Cuenta creada exitosamente!\nYa puedes regresar e iniciar sesión.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
        }

        
        // Limpiar campos de registro
        txtRegNombre.setText("");
        txtRegApellido.setText("");
        txtRegClave.setText("");
    }
}