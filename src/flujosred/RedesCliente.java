package flujosred;

import persona.Cliente;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;

public class RedesCliente {
    public static void main(String[] args) {

        Socket socket = null;
        try {
            // Inicializamos un socket para conectarnos al puerto de nuestro ordenador que hemos abierto en
            // la clase "RedesServidor"
            socket = new Socket("localhost", 3000);
            // Inicializamos una instancia de Cliente para pasarlo al servidor
            Cliente clienteRed = new Cliente("00000000-f", "felipe", LocalDate.of(1990, 12, 12));
            // Creamos un flujo de datos para pasar información al servidor ("main" de la clase "Test")
            // Inicializamos una instancia de "ObjectOutputStream" para enviar un objeto, de tipo
            // Cliente para enviarlo al "main" de la clase "Test"
            // Le pasamos por parámetro "socket.getOutputStream()" que hace referencia al flujo del socket
            // para que sepa por donde tiene que enviarlo
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Le pasamos la instancia de "Cliente" para que la envie por el flujo de red
            objectOutputStream.writeObject(clienteRed);
            // Cerramos las instancias
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
