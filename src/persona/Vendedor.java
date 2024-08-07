package persona;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;

import venta.Factura;
import venta.Venta;

/**
 * La clase Vendedor hereda de  "Persona" y representa a un vendedor en el sistema.
 * @author Mlm96
 * @version 1.0
 */
public final class Vendedor extends Persona{
    // Declaramos los atributos
    private static int contador;
    private int numeroVendedor;
    // Bloque de inicialización estático
    static {
        contador = 0;
    }
    // Bloque de inicialización de instancia
    {
        numeroVendedor = contador++;
    }
    // Constructores
    public Vendedor(@NonNull String nif, @NonNull String nombre, @NonNull LocalDate fechaNac) {
        super(nif, nombre, fechaNac);
    }
    public Vendedor(@NonNull String nif, @NonNull String nombre, @NonNull LocalDate fechaNac,
                    String direccion, String correo) {
        super(nif, nombre, fechaNac, direccion, correo);
    }
    public Vendedor(@NonNull String nif, @NonNull String nombre, int telefono, LocalDate fechaNac) {
        super(nif, nombre, telefono, fechaNac);
    }
    public Vendedor(@NonNull String nif, @NonNull String nombre, int telefono, LocalDate fechaNac,
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
        System.out.println("Vendedor [numeroVendedor=" + numeroVendedor + ", nif=" + nif +
                ", nombre=" + nombre + ", direccion=" + direccion + ", telefono=" +
                telefono + ", edad=" + edad + ", fechaNac=" + fechaNac + "]");
    }

    // GETTERS Y SETTERS
    // Codigo Cliente
    public int getNumeroVendedor() {
        return numeroVendedor;
    }

}