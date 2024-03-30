package interfazgrafica;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
/**
 * En esta clase se crea una ventana gráfica propia y se le añade un "controlador de
 * eventos", así cumplo con los criterios: "f", "g" y "h"; que corresponden al uso
 * de la interfaz gráfica y de los controladores de eventos.
 * CRITERIOS: "f", "g" y "h".
 */
public class VentanaFechaNacVendedor extends JFrame{
    // "fechaNacimiento" lo declaramos como un atributo de la clase, fuera del constructor, ya que si
    // lo hicieramos dentro, no sería accesible desde su método "get" y no podríamos utilizar su valor
    // en el "main"
    private LocalDate fechaNacimiento;  // Fecha de nacimiento introducida
    // Constructor de la clase
    public VentanaFechaNacVendedor() {
        // Creamos un nuevo marco de la ventana y le damos un título
        JFrame frame = new JFrame("Fecha de Nacimiento del Vendedor");
        // Establecemos el tamaño de la ventana
        frame.setSize(300, 200);
        // A continuación, le indicamos que cuando cierre la ventana termine la aplicación,
        // de lo contrario, cuando la cerremos seguira funcionando. En este caso no es
        // necesario, ya que más abajo le indicamos que termine cuando pulsemos el botón
        // de "enviar" y el formato de la fecha sea correcto, pero lo dejaremos de todas formas.
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Inicializamos el panel de la ventana
        JPanel panel = new JPanel();
        // Añadimos el panel al marco de la ventana que hemos creado antes
        frame.add(panel);

        // Establece el diseño del panel a null para que podamos colocar los componentes manualmente
        panel.setLayout(null);

        // Creamos la etiqueta con el texto "Fecha de Nacimiento (yyyy-mm-dd)" para indicar el formato
        JLabel fechaNacLabel = new JLabel("Fecha de Nacimiento (yyyy-mm-dd)");
        // Indicamos las coordenadas y el tamaño de la etiqueta con el texto, dentro de la ventana
        fechaNacLabel.setBounds(10, 20, 200, 25);
        // Añade la etiqueta al panel
        panel.add(fechaNacLabel);

        // Creamos un nuevo campo de texto donde el usuario puede introducir la fecha
        JTextField campoFecha = new JTextField(20);
        // Establecemos la posición y el tamaño del campo de texto en el panel
        campoFecha.setBounds(10, 50, 160, 25);
        // Añade el campo de texto al panel
        panel.add(campoFecha);

        // Creamos el botón de enviar y le indicamos el texto "Enviar"
        JButton botonEnviar = new JButton("Enviar");
        // Establecemos la posición y el tamaño del botón en el panel
        botonEnviar.setBounds(10, 80, 80, 25);
        // Añade el botón al panel
        panel.add(botonEnviar);

        /*
        CRITERIO "h": Se han programado controladores de eventos (15%)
         ya que hemos hecho que como respuesta a que se pulse el botón de "enviar"
         se ejecute el método "actionPerformed" que coge el texto del campo
         "
         */
        // Añadimos un "listenner"" de acción al botón. Esto significa que cuando se haga clic en el botón,
        // se ejecutará el código en este "listenner"
        botonEnviar.addActionListener(new ActionListener() {
            // Método de la interfaz "ActionListener
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cuando se hace clic en el botón, recoge la fecha del campo de texto en el que se
                // encontrará la fecha introducida
                String fecha = campoFecha.getText();
                // Crea un nuevo formateador de fecha para convertir una cadena en un LocalDate
                DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                try {
                    // Inicializamos el atributo referente a la fecha de nacimiento, llamando al
                    // método ".parse", y pasandole por parámetro la fecha recogida, y el formato creado
                    fechaNacimiento = LocalDate.parse(fecha, formatoFecha);
                    // Si la fecha es válida y cumple con el formato, mostramos un mensajr con dicha fecha
                    // en otra ventana emergente usando JOptionPane, pero en este caso con su método "showMessageDialog"
                    JOptionPane.showMessageDialog(null, "Fecha recibida: " + fechaNacimiento);
                    // Evaluamos si la fecha no es nula, es decir, si es válida, y en cuyo caso, cerramos
                    // la ventana
                    if (fechaNacimiento != null) {
                        frame.dispose(); // Cerramos la ventana con ".dispose" desde el "frame" creado
                    }
                } catch (DateTimeParseException ex) {
                    // Si la fecha no está en el formato correcto, mostramos un mensaje de error
                    JOptionPane.showMessageDialog(null, "Formato de fecha incorrecto. Por favor, use el formato yyyy-MM-dd.");
                }
            }
        });

        // Hacemos visible la ventana
        frame.setVisible(true);
    }

    public LocalDate getFechaNacimiento() {
        // Devolvemos la "fechaNacimiento" para que sea accesible desde otras clases, en este caso
        // desde el "main" de la clase "Prueba" para poder instanciar al "Vendedor"
        return fechaNacimiento;
    }
}

