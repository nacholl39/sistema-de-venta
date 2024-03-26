package persona;

import java.time.LocalDate;

/**
 * La clase Cliente hereda de "Persona" y representa a un cliente en el sistema.
 * @author Mlm96
 * @version 1.0
 */
public final class Cliente extends Persona {
    // Declaramos los atributos
    private static int contador;
    private int codigoCliente;
    // Bloque de inicialización estático
    static {
        contador = 0;
    }
    // Bloque de inicialización de instancia
    {
        codigoCliente = contador++;
    }
    // Constructores
    // Constructores
    public Cliente(String nif, String nombre, LocalDate fechaNac) {
        super(nif, nombre, fechaNac);
    }
    public Cliente( String nif, String nombre, LocalDate fechaNac,
                   String direccion, String correo) {
        super(nif, nombre, fechaNac, direccion, correo);
    }
    public Cliente(String nif, String nombre, int telefono, LocalDate fechaNac) {
        super(nif, nombre, telefono, fechaNac);
    }
    public Cliente( String nif, String nombre, int telefono, LocalDate fechaNac,
                   String direccion, String correo) {
        super(nif, nombre, telefono, fechaNac, direccion, correo);
    }


    // Métodos y funcionalidades
    // Sobrescribimos el método abstracto de la clase "Persona"
    /**
     * Sobrescribe el método abstracto de la clase "Persona" para mostrar la información del cliente.
     */
    @Override
    public void mostrarInformacion() {
        System.out.println("Cliente [codigoCliente=" + codigoCliente + ", nif=" + nif +
                ", nombre=" + nombre + ", direccion=" + direccion + ", telefono=" +
                telefono + ", edad=" + edad + ", fechaNac=" + fechaNac + "]");
    }


    // GETTERS Y SETTERS
    // Codigo Cliente
    public int getId() {
        return codigoCliente;
    }

}