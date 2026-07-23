package SCC.view;

import SCC.model.GCC;
import SCC.model.Usuario;
import SCC.model.Usuario.Sexo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VentanaMenuDesarrollador extends JFrame {

    private final GCC sistemaGCC;
    private DefaultTableModel modeloTabla;
    private JTable tablaCuentas;
    private JLabel lblInfoCuentas;
    private PanelDibujoGrafo panelDibujo;

    public VentanaMenuDesarrollador(GCC sistemaGCC) {
        this.sistemaGCC = sistemaGCC;


        setTitle("Panel de Administración y Desarrollo - LinkUp");
        setSize(900, 600);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);

        JTabbedPane pestañas = new JTabbedPane();

        // --- PESTAÑA 1: LISTADO DE CUENTAS ---
        JPanel panelCuentas = new JPanel(new BorderLayout());
        panelCuentas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configuración de la Tabla
        String[] columnas = {"Clave/ID", "Nombre", "Apellido", "Sexo", "Edad"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla de solo lectura
            }
        };

        tablaCuentas = new JTable(modeloTabla);
        tablaCuentas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaCuentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Seleccionar de a una fila
        JScrollPane scrollTabla = new JScrollPane(tablaCuentas);
        
        lblInfoCuentas = new JLabel();
        lblInfoCuentas.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        
        // Cargar los datos iniciales en la tabla
        actualizarDatosComponentes();

        // --- PANEL DE ACCIONES (ELIMINAR CUENTA) ---
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEliminar = new JButton("Eliminar Cuenta Seleccionada");
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEliminar.setBackground(new Color(192, 57, 43)); // Rojo oscuro institucional
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaCuentas.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, seleccione una cuenta de la tabla para eliminar.", 
                    "Ninguna fila seleccionada", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Obtener la clave de acceso (Columna 0) de la fila seleccionada
            String claveAEliminar = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
            String nombreUser = (String) modeloTabla.getValueAt(filaSeleccionada, 1) + " " + modeloTabla.getValueAt(filaSeleccionada, 2);

            int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de que desea eliminar permanentemente la cuenta de \"" + nombreUser + "\"?\nEsta acción modificará las conexiones del grafo.", 
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                // Invoca la eliminación lógica en tu backend (GCC saca al usuario del mapa y del grafo)
                sistemaGCC.ElimCuenta(claveAEliminar);
                
                // Forzar el recálculo del matching para actualizar las aristas visuales
                sistemaGCC.grafo.ConstruirMatching();

                // Sincronizar y repintar los componentes visuales de ambas pestañas
                actualizarDatosComponentes();
                panelDibujo.repaint();

                JOptionPane.showMessageDialog(this, "La cuenta ha sido removida con éxito.", "Eliminación Completada", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        panelAcciones.add(btnEliminar);

        // Contenedor inferior para agrupar info y botones de acción
        JPanel panelInferiorCuentas = new JPanel(new BorderLayout());
        panelInferiorCuentas.add(lblInfoCuentas, BorderLayout.WEST);
        panelInferiorCuentas.add(panelAcciones, BorderLayout.EAST);

        panelCuentas.add(scrollTabla, BorderLayout.CENTER);
        panelCuentas.add(panelInferiorCuentas, BorderLayout.SOUTH);

        // --- PESTAÑA 2: VISUALIZADOR DEL GRAFO ---
        JPanel panelGrafoContenedor = new JPanel(new BorderLayout());
        JLabel lblInfoGrafo = new JLabel(" Mapa de Conexiones (Matching del Grafo Bipartito en Tiempo Real)");
        lblInfoGrafo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInfoGrafo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panelDibujo = new PanelDibujoGrafo(sistemaGCC);
        
        panelGrafoContenedor.add(lblInfoGrafo, BorderLayout.NORTH);
        panelGrafoContenedor.add(panelDibujo, BorderLayout.CENTER);

        pestañas.addTab("Cuentas y Datos", panelCuentas);
        pestañas.addTab("Mapa de Grafos", panelGrafoContenedor);

        add(pestañas);
    }

    /**
     * Limpia la tabla y vuelve a leer el mapa de usuarios de GCC para mantener la vista sincronizada.
     */
    private void actualizarDatosComponentes() {
        modeloTabla.setRowCount(0); // Limpiar filas anteriores
        Map<String, Usuario> mapaUsuarios = sistemaGCC.getUsuarios();
        
        if (mapaUsuarios != null) {
            for (Map.Entry<String, Usuario> entrada : mapaUsuarios.entrySet()) {
                String clave = entrada.getKey();
                Usuario user = entrada.getValue();
                Object[] fila = {
                    clave,
                    user.getNombre(),
                    user.getApellido(),
                    user.getSexo(),
                    user.getEdad()
                };
                modeloTabla.addRow(fila);
            }
            lblInfoCuentas.setText("Total de usuarios registrados: " + mapaUsuarios.size());
        } else {
            lblInfoCuentas.setText("Total de usuarios registrados: 0");
        }
    }

    public static boolean solicitarAccesoDesarrollador() {
        JPanel panelClave = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel("Ingrese la contraseña de Desarrollador / Administrador:");
        JPasswordField txtPassword = new JPasswordField(15);
        
        panelClave.add(label, BorderLayout.NORTH);
        panelClave.add(txtPassword, BorderLayout.CENTER);
        
        txtPassword.requestFocusInWindow();

        int opcion = JOptionPane.showConfirmDialog(null, panelClave, 
        "Área Restringida - Autenticación requerida", 
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            String passwordIntroducida = new String(txtPassword.getPassword());
            // Puedes cambiar "admin123" por la contraseña que consideres pertinente
            if (passwordIntroducida.equals("admin123")) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Contraseña incorrecta. Acceso denegado.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    /**
     * Panel interno encargado de renderizar visualmente los nodos y las aristas.
     */
    private static class PanelDibujoGrafo extends JPanel {
        private final GCC sistemaGCC;

        public PanelDibujoGrafo(GCC sistemaGCC) {
            this.sistemaGCC = sistemaGCC;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            // Antialiasing para suavizar el renderizado de líneas y círculos
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Map<String, Usuario> usuarios = sistemaGCC.getUsuarios();
            if (usuarios == null || usuarios.isEmpty()) {
                g2d.setColor(Color.GRAY);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
                g2d.drawString("No hay usuarios registrados en el sistema para graficar.", 40, 50);
                return;
            }

            // Separar usuarios por género para formar las dos columnas del grafo bipartito
            ArrayList<Usuario> columnaIzquierda = new ArrayList<>();
            ArrayList<Usuario> columnaDerecha = new ArrayList<>();

            for (Usuario u : usuarios.values()) {
                if (u.getSexo() != null && u.getSexo().equals(Sexo.Mujer)) {
                    columnaDerecha.add(u); // Mujeres a la derecha
                } else {
                    columnaIzquierda.add(u); // Hombres a la izquierda
                }
            }

            Map<Usuario, Point> posiciones = new HashMap<>();
            int radioNodo = 22;
            int altoPanel = getHeight();
            int anchoPanel = getWidth();

            // Definir márgenes horizontales de las columnas (X)
            int margenHorizontal = 180; 
            int xIzquierda = margenHorizontal;
            int xDerecha = anchoPanel - margenHorizontal;

            // 1. Calcular coordenadas columna izquierda (Hombres)
            int totalIzquierda = columnaIzquierda.size();
            for (int i = 0; i < totalIzquierda; i++) {
                int y = (totalIzquierda == 1) ? altoPanel / 2 : 80 + i * (altoPanel - 160) / (totalIzquierda - 1);
                posiciones.put(columnaIzquierda.get(i), new Point(xIzquierda, y));
            }

            // 2. Calcular coordenadas columna derecha (Mujeres)
            int totalDerecha = columnaDerecha.size();
            for (int i = 0; i < totalDerecha; i++) {
                int y = (totalDerecha == 1) ? altoPanel / 2 : 80 + i * (altoPanel - 160) / (totalDerecha - 1);
                posiciones.put(columnaDerecha.get(i), new Point(xDerecha, y));
            }

            // 3. Dibujar las Aristas de COMPATIBILIDAD (Líneas entre columnas)
            Map<Usuario, java.util.List<SCC.model.Aristas>> todosLosVertices = sistemaGCC.grafo.getVertices();
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));

            if (todosLosVertices != null) {
                for (Map.Entry<Usuario, java.util.List<SCC.model.Aristas>> entrada : todosLosVertices.entrySet()) {
                    Usuario origen = entrada.getKey();
                    java.util.List<SCC.model.Aristas> adyacentes = entrada.getValue();

                    if (adyacentes != null && posiciones.containsKey(origen)) {
                        Point p1 = posiciones.get(origen);

                        for (SCC.model.Aristas arista : adyacentes) {
                            Usuario destino = arista.getUsuario();
                            
                            if (destino != null && posiciones.containsKey(destino)) {
                                Point p2 = posiciones.get(destino);
                                double valorCompatibilidad = arista.getValor();
                                
                                // Dibujar línea de conexión
                                g2d.setColor(new Color(127, 140, 141, 150));
                                g2d.setStroke(new BasicStroke(1.5f));
                                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                                
                                // Calcular punto medio exacto para colocar el porcentaje de afinidad
                                int medioX = (p1.x + p2.x) / 2;
                                int medioY = (p1.y + p2.y) / 2;
                                
                                String strValor = String.format("%.0f%%", valorCompatibilidad);
                                FontMetrics fm = g2d.getFontMetrics();
                                int anchoTexto = fm.stringWidth(strValor);
                                
                                // Fondo blanco higiénico detrás del texto
                                g2d.setColor(Color.WHITE);
                                g2d.fillRect(medioX - (anchoTexto / 2) - 3, medioY - 6, anchoTexto + 6, 12);
                                
                                g2d.setColor(Color.BLACK);
                                g2d.drawString(strValor, medioX - (anchoTexto / 2), medioY + 4);
                            }
                        }
                    }
                }
            }

            // 4. Dibujar los Vértices Nodos (Círculos) sobre las columnas
            ArrayList<Usuario> todosLosUsuarios = new ArrayList<>(usuarios.values());
            for (Usuario u : todosLosUsuarios) {
                if (!posiciones.containsKey(u)) continue;
                Point p = posiciones.get(u);

                // Coloreado por género
                boolean esFemenino = u.getSexo() != null && u.getSexo().equals(Sexo.Mujer);
                if (esFemenino) {
                    g2d.setColor(new Color(245, 183, 177)); // Rosado Pastel
                } else {
                    g2d.setColor(new Color(174, 214, 241)); // Azul Pastel
                }

                g2d.fillOval(p.x - radioNodo, p.y - radioNodo, radioNodo * 2, radioNodo * 2);
                
                g2d.setColor(new Color(52, 73, 94));
                g2d.setStroke(new BasicStroke(2.0f));
                g2d.drawOval(p.x - radioNodo, p.y - radioNodo, radioNodo * 2, radioNodo * 2);

                // Texto descriptivo dinámico según su posición en las columnas
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String infoTexto = u.getNombre() + " (" + u.getEdad() + " - " + (u.getSexo() != null ? u.getSexo().toString().substring(0,1) : "?") + ")";
                FontMetrics fm = g2d.getFontMetrics();
                
                // Si está a la izquierda el texto se dibuja hacia afuera (izquierda), si está a la derecha, hacia la derecha.
                int textoX = !esFemenino ? p.x - radioNodo - 12 - fm.stringWidth(infoTexto) : p.x + radioNodo + 12;
                int textoY = p.y + 5;

                // Pequeño recuadro de fondo
                g2d.setColor(new Color(255, 255, 255, 230));
                g2d.fillRect(textoX - 4, textoY - 12, fm.stringWidth(infoTexto) + 8, 16);

                g2d.setColor(Color.BLACK);
                g2d.drawString(infoTexto, textoX, textoY);
            }
        }
    }
}