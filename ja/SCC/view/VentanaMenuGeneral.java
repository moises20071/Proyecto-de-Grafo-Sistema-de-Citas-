package SCC.view;
import SCC.Controller.ControladorSistema;
import SCC.model.GCC;
import javax.swing.*;
import java.awt.*;

public class VentanaMenuGeneral extends JFrame {

    private GCC sistemaGCC;
    private ControladorSistema controlador;

    public VentanaMenuGeneral(GCC sistemaGCC,ControladorSistema controlador) {

        this.sistemaGCC = sistemaGCC;
        this.controlador = controlador;
        setTitle("Sistema de Citas a Ciegas \"LinkUp\" - Menú Principal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false);

        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panelPrincipal.setBackground(new Color(245, 247, 250));

        // --- TITULO DE LA APLICACIÓN ---
        JLabel lblTitulo = new JLabel("Bienvenido a LinkUp!");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(44, 62, 80));
        
        JLabel lblSubtitulo = new JLabel("Seleccione una opción para continuar");
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(127, 140, 141));

        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createVerticalStrut(5));
        panelPrincipal.add(lblSubtitulo);
        panelPrincipal.add(Box.createVerticalStrut(30));

        // --- BOTONES DEL MENÚ ---
        Font fuenteBotones = new Font("Segoe UI", Font.BOLD, 14);

        // Botón 1: Redirección directa al Login
        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIngresar.setFont(fuenteBotones);
        btnIngresar.setBackground(new Color(70, 130, 180)); 
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Botón 2: Redirección directa al Registro
        JButton btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setFont(fuenteBotones);
        btnRegistrar.setBackground(new Color(46, 139, 87)); 
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Botón 3: Salir de la Aplicación por completo
        JButton btnSalir = new JButton("Salir del Sistema");
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.setFont(fuenteBotones);
        btnSalir.setBackground(new Color(178, 34, 34)); 
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        //Dev menu
        JButton btnDesarrollador = new JButton("Panel del Desarrollador (Admin)");
        btnDesarrollador.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDesarrollador.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDesarrollador.setBackground(new Color(52, 73, 94)); 
        btnDesarrollador.setForeground(Color.WHITE);
        btnDesarrollador.setFocusPainted(false);
        btnDesarrollador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Agregar componentes al contenedor
        panelPrincipal.add(btnIngresar);
        panelPrincipal.add(Box.createVerticalStrut(15));
        panelPrincipal.add(btnRegistrar);
        panelPrincipal.add(Box.createVerticalStrut(15)); 
        panelPrincipal.add(btnSalir);
        panelPrincipal.add(btnDesarrollador);
        add(panelPrincipal);

        // --- ASIGNACIÓN DE EVENTOS ---

        // Abrir login (pestaña 0 implícita / modo login)
        btnIngresar.addActionListener(e -> controlador.mostrarAutenticacion("LOGIN"));

        // Abrir registro (modo registro)
        btnRegistrar.addActionListener(e -> controlador.mostrarAutenticacion("REGISTRO"));

        //Arbir Dev menu
        btnDesarrollador.addActionListener(e -> controlador.mostrarMenuDesarrollador());

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                controlador.GuardarDatos(sistemaGCC);
                System.exit(0);
            }
        });

        // Salida limpia de la aplicación
        btnSalir.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                    this, 
                    "¿Seguro que deseas salir del LinkUp?", 
                    "Confirmar Salida", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (confirmacion == JOptionPane.YES_OPTION) {
                controlador.GuardarDatos(sistemaGCC);
                System.exit(0);
            }
        });
        
    }
}