package venta;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;

import persona.Cliente;
import persona.Vendedor;
/**
 * Esta clase representa una venta realizada por un vendedor a un cliente. También
 * implementa la interfaz comparable.
 * @author Mlm96
 * @version 1.0
 */
public class Venta implements Comparable{
    // Declaramos los atributos
    private static int contador; // Contador estático para generar identifircadores únicos
    private int id; // Identificador único de cada venta
    private LocalDate fecha; // Fecha en la que se realiza la venta
    private Cliente cliente; // Cliente al que se realizó la venta
    private Vendedor vendedor; // Vendedor que realizó la venta
    private Factura factura; // Factura relacionada con la propia venta
    // Bloque de inicialización estático
    static {
        contador = 0;
    }
    // Bloque de inicialización de instancia. En él, asignamos el identificador y la fecha actual.
    {
        id = contador++;
        fecha = LocalDate.now();
    }
    // Constructor
    public Venta(@NonNull Cliente cliente, @NonNull Vendedor vendedor) {
        this.cliente = cliente;
        this.vendedor = vendedor;
    }


    // Métodos y funcionalidades
    //Interfaz comparable
    /**
     * Implementación del método compareTo de la interfaz Comparable.
     * Compara esta venta con otra venta basándose en sus identificadores.
     * @param objeto La venta con la que se va a comparar.
     * @return 0 si las ventas son iguales, -1 si esta venta es menor
     * que la venta pasada, 1 si esta venta es mayor que la venta pasada.
     */
    @Override
    public int compareTo(Object objeto) {
        // Evaluamos si el objeto esta a "null"
        if(objeto!=null) throw new IllegalArgumentException("El parámetro no puede ser nulo");
        // Evaluamos si el objeto es una instancia de "Venta"
        if(objeto instanceof Venta) throw new IllegalArgumentException("El parámetro debe ser del tipo Empleado");
        // Comparamos si la factura pasada es mayor(1), menor(-1) o son iguales (0)
        if(((Venta)objeto).getId()==this.getId()) {
            // Si son iguales devolvemos un 0
            return 0;
        } else if(((Venta)objeto).getId()<this.getId()) {
            // Si el objeto pasado tiene un id menor devolvemos un -1
            return -1;
        } else {
            // Si el objeto pasado tiene un id mayor devolvemos un 1.
            return 1;
        }
    }

    // Getters Y Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Factura getFactura() {
        return factura;
    }
    public void setFactura(Factura factura) {
        this.factura = factura;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public Vendedor getVendedor() {
        return vendedor;
    }

}