package SCC.Controller;

import SCC.model.GCC;
import SCC.model.Saves;
import SCC.view.*;
import javax.swing.JOptionPane;

public class ControladorSistema {

    private final GCC modeloFacade;
    private VentanaMenuGeneral menuGeneral;
    private VentanaAutenticacion ventanaAuth;
    private VentanaPerfil ventanaPerfil;

    public ControladorSistema() {
        // Inicializa la fachada del modelo
        GCC modeloCargado = Saves.LoadGCC();
        if(modeloCargado != null){
            this.modeloFacade = modeloCargado;
            if(modeloFacade.grafo != null){
                modeloFacade.grafo.RearmarGrafoTrasCarga();
            }
        }else{
            this.modeloFacade = new GCC(); 
        }
    }

    public void GuardarDatos(GCC modelo){
        Saves.SaveGCC(modelo);
    }

    /**
     * Arranca la aplicación mostrando el menú principal.
     */
    public void iniciarAplicacion() {
        this.menuGeneral = new VentanaMenuGeneral(modeloFacade,this);
        this.menuGeneral.setVisible(true);
    }

    /**
     * Muestra la ventana de Autenticación (Login o Registro).
     */
    public void mostrarAutenticacion(String tipoVista) {
        if (menuGeneral != null) {
            menuGeneral.setVisible(false);
        }
        
        ventanaAuth = new VentanaAutenticacion(modeloFacade,this,tipoVista);
        
        // Controlamos el cierre de la ventana de autenticación para regresar de forma segura
        ventanaAuth.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        ventanaAuth.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                regresarAlMenuGeneral();
            }
        });
        
        ventanaAuth.setVisible(true);
    }

    /**
     * Transición controlada tras un inicio de sesión exitoso.
     * Verifica que el estado de la fachada sea consistente.
     */
    public void procesarLoginExitoso() {
        // Validación del estado de la Fachada (Control de correcto funcionamiento)
        if (modeloFacade.getUIngresado() == null) {
            JOptionPane.showMessageDialog(null, 
                "Error de Consistencia: La fachada indica que no hay un usuario logueado.", 
                "Error de Verificación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ventanaAuth != null) {
            ventanaAuth.dispose();
        }

        // Se construye la ventana de perfil inyectando el estado verificado
        ventanaPerfil = new VentanaPerfil(modeloFacade,this,menuGeneral);
        ventanaPerfil.setVisible(true);
    }

    /**
     * Ejecuta el proceso de Logout limpiando el estado de la fachada y de la interfaz.
     */
    public void procesarLogout() {
        modeloFacade.Logout(); //
        
        if (ventanaPerfil != null) {
            ventanaPerfil.dispose();
        }
        
        // Verificación de sanidad del modelo
        if (modeloFacade.getUIngresado() == null) {
            if (menuGeneral != null) {
                menuGeneral.setVisible(true);
            }
        } else {
            System.err.println("Alerta: Falló la limpieza de sesión en el modelo.");
        }
    }

    public void regresarAlMenuGeneral() {
        if (ventanaAuth != null) {
            ventanaAuth.dispose();
        }
        if(ventanaPerfil != null){
            ventanaPerfil.dispose();
        }
        if (menuGeneral != null) {
            menuGeneral.setVisible(true);
        }
    }

    // Agrega esto dentro de ControladorSistema.java
    public void mostrarMenuDesarrollador() {
        if(!VentanaMenuDesarrollador.solicitarAccesoDesarrollador()){
            return;
        }
        VentanaMenuDesarrollador ventanaDev = new VentanaMenuDesarrollador(modeloFacade);
        ventanaDev.setVisible(true);
    }
}