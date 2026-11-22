import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.imageio.ImageIO;

/**
 * EA3 - Interfaz Swing para comprobar conexiones JDBC.
 * Solo usa bibliotecas estandar de Java. El controlador JDBC de cada
 * base de datos (por ejemplo, SQLite) debe instalarse por separado.
 */
public class ConexionTester extends JFrame {
    private final JTextField campoUrl;
    private final JTextField campoUsuario;
    private final JPasswordField campoClave;
    private final JTextArea areaResultado;
    private final JButton botonProbar;

    public ConexionTester() {
        super("ConexionTester - Prueba de conexion JDBC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(660, 430);
        setMinimumSize(new Dimension(570, 390));
        setLocationRelativeTo(null);

        JPanel principal = new JPanel(new BorderLayout(12, 12));
        principal.setBorder(new EmptyBorder(22, 25, 22, 25));
        principal.setBackground(new Color(247, 249, 252));

        JLabel titulo = new JLabel("Prueba de conexion a bases de datos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 21));
        titulo.setForeground(new Color(25, 47, 72));
        principal.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(10, 14));
        centro.setOpaque(false);
        JPanel formulario = new JPanel(new GridLayout(3, 2, 12, 12));
        formulario.setOpaque(false);

        campoUrl = new JTextField("jdbc:sqlite:inventario.db");
        campoUsuario = new JTextField();
        campoClave = new JPasswordField();
        formulario.add(new JLabel("URL de conexion JDBC:"));
        formulario.add(campoUrl);
        formulario.add(new JLabel("Usuario (opcional en SQLite):"));
        formulario.add(campoUsuario);
        formulario.add(new JLabel("Contrasena (opcional en SQLite):"));
        formulario.add(campoClave);
        centro.add(formulario, BorderLayout.NORTH);

        JPanel resultados = new JPanel(new BorderLayout(8, 8));
        resultados.setOpaque(false);
        JLabel rotulo = new JLabel("Resultado de la prueba:");
        rotulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        resultados.add(rotulo, BorderLayout.NORTH);
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultado.setBorder(new EmptyBorder(10, 10, 10, 10));
        areaResultado.setText("Ingrese los datos y pulse 'Probar conexion'.");
        resultados.add(new JScrollPane(areaResultado), BorderLayout.CENTER);
        centro.add(resultados, BorderLayout.CENTER);
        principal.add(centro, BorderLayout.CENTER);

        botonProbar = new JButton("Probar conexion");
        botonProbar.setFont(new Font("SansSerif", Font.BOLD, 14));
        botonProbar.setBackground(new Color(23, 91, 147));
        botonProbar.setForeground(Color.WHITE);
        botonProbar.setOpaque(true);
        botonProbar.addActionListener(this::probarConexion);
        JPanel abajo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        abajo.setOpaque(false);
        abajo.add(botonProbar);
        principal.add(abajo, BorderLayout.SOUTH);
        setContentPane(principal);
    }

    private void mostrarResultado(String mensaje, Color color) {
        areaResultado.setForeground(color);
        areaResultado.setText(mensaje);
        areaResultado.setCaretPosition(0);
    }

    private String detectarControlador(String url) {
        if (url.startsWith("jdbc:sqlite:")) return "org.sqlite.JDBC";
        if (url.startsWith("jdbc:mysql:")) return "com.mysql.cj.jdbc.Driver";
        if (url.startsWith("jdbc:postgresql:")) return "org.postgresql.Driver";
        return null;
    }

    private void probarConexion(ActionEvent evento) {
        final String url = campoUrl.getText().trim();
        final String usuario = campoUsuario.getText().trim();
        final char[] claveChars = campoClave.getPassword();
        final String clave = new String(claveChars);
        java.util.Arrays.fill(claveChars, '\0');

        if (url.isEmpty()) {
            mostrarResultado("FALLO DE CONEXION: la URL JDBC no puede estar vacia.", Color.RED);
            return;
        }

        botonProbar.setEnabled(false);
        mostrarResultado("Conectando...", new Color(65, 65, 65));
        new SwingWorker<String, Void>() {
            private boolean exito;

            @Override
            protected String doInBackground() {
                try {
                    String controlador = detectarControlador(url);
                    if (controlador != null) Class.forName(controlador);
                    // El try-with-resources cierra Connection incluso si hay excepciones.
                    try (Connection conexion = usuario.isEmpty() && clave.isEmpty()
                            ? DriverManager.getConnection(url)
                            : DriverManager.getConnection(url, usuario, clave)) {
                        exito = conexion.isValid(3);
                        return exito
                                ? "CONEXION EXITOSA: se establecio y verifico la conexion.\n"
                                  + "La conexion fue cerrada de forma segura."
                                : "FALLO DE CONEXION: el servidor no valido la conexion.";
                    }
                } catch (ClassNotFoundException e) {
                    return "FALLO DE CONEXION: no se encontro el controlador JDBC.\n"
                            + "Detalle tecnico: " + e.getMessage();
                } catch (SQLException e) {
                    return "FALLO DE CONEXION: error al conectar con la base de datos.\n"
                            + "Detalle tecnico: " + e.getMessage();
                }
            }

            @Override
            protected void done() {
                try {
                    mostrarResultado(get(), exito ? new Color(0, 120, 52) : Color.RED);
                } catch (Exception e) {
                    mostrarResultado("FALLO DE CONEXION: " + e.getMessage(), Color.RED);
                } finally {
                    botonProbar.setEnabled(true);
                }
            }
        }.execute();
    }

    /** Capturas reales de la interfaz para evidenciar el comportamiento. */
    private static void capturarEvidencia(String modo, String destino) throws Exception {
        ConexionTester ventana = new ConexionTester();
        SwingUtilities.invokeAndWait(() -> {
            ventana.setVisible(true);
            if ("vacia".equals(modo)) ventana.campoUrl.setText("");
            if ("sin-driver".equals(modo)) ventana.campoUrl.setText("jdbc:sqlite:inventario.db");
            ventana.botonProbar.doClick();
        });
        Thread.sleep(1800);
        Rectangle bounds = ventana.getBounds();
        Point origen = ventana.getLocationOnScreen();
        BufferedImage captura = new Robot().createScreenCapture(
                new Rectangle(origen.x, origen.y, bounds.width, bounds.height));
        ImageIO.write(captura, "png", new File(destino));
        SwingUtilities.invokeAndWait(ventana::dispose);
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 3 && "--evidencia".equals(args[0])) {
            capturarEvidencia(args[1], args[2]);
            System.exit(0);
        }
        SwingUtilities.invokeLater(() -> new ConexionTester().setVisible(true));
    }
}
