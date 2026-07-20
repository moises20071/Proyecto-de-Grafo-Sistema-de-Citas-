package SCC;

import SCC.Controller.ControladorSistema;

public class Root {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            ControladorSistema appController = new ControladorSistema();
            appController.iniciarAplicacion();
        });
    }
}