package venta;

import producto.Producto;
/**
 *
 */
public class DetalleFactura implements Comparable {
    // Declaramos los atributos
    private static int contador;
    private int id, lineaFactura, cantidad;
    private Factura factura;
    private Producto producto;
    // Bloque de inicialización estático
    static {
        contador = 0;
    }
    // Bloque de inicialización de instancia
    {
        id = contador++;
    }
    // Constructor
    public DetalleFactura(int lineaFactura, int cantidad,
                          Producto producto) {
        this.lineaFactura = lineaFactura;
        this.cantidad = cantidad;
        this.producto = producto;
    }

    // Métodos y funcionalidades
    //Interfaz comparable
    /**
     * Implementación del método compareTo de la interfaz Comparable.
     * Compara este detalle de la factura con otro basándose en sus identificadores.
     * @param objeto el detalle de factura con la que se va a comparar.
     * @return 0 si los detalles de factura son iguales, -1 si este es menor
     * que el pasado, 1 si este es mayor que el pasado.
     */
    @Override
    public int compareTo(Object objeto) {
        // Evaluamos si el objeto esta a "null"
        if(objeto==null) throw new IllegalArgumentException("El parámetro no puede ser nulo");
        // Evaluamos si el objeto es una instancia de "Factura"
        if(!(objeto instanceof DetalleFactura)) throw new IllegalArgumentException("El parámetro debe ser del tipo Empleado");
        // Comparamos si el DetalleFactura pasado es mayor(1), menor(-1) o son iguales (0)
        if(((DetalleFactura)objeto).getId()==this.getId()) {
            // Si son iguales devolvemos un 0
            return 0;
        } else if(((DetalleFactura)objeto).getId()<this.getId()) {
            // Si el objeto pasado tiene un id menor devolvemos un -1
            return -1;
        } else {
            // Si el objeto pasado tiene un id mayor devolvemos un 1.
            return 1;
        }
    }

    // GETTERS Y SETTERS
    public int getLineaFactura() {
        return lineaFactura;
    }
    public void setLineaFactura(int lineaFactura) {
        this.lineaFactura = lineaFactura;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public Producto getProducto() {
        return producto;
    }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Factura getFactura() {
        return factura;
    }

}