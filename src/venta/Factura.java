package venta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import persona.Cliente;
import persona.Vendedor;

/**
 * Esta clase representa una factura realizada por un vendedor a un cliente. También
 * implementa la interfaz comparable.
 * Guarda una relación de 1 a 1 con la clase Venta, ya que en la aplicación cada venta
 * lleva una factura asociada.
 * @author Mlm96
 * @version 1.0
 */
public class Factura implements Comparable {
    // Declaramos los atributos
    private static int contador; // Contador estático para generar identifircadores únicos
    private int id; // Identificador único de cada factura
    private LocalDateTime fechaHora; // Fecha y hora en la que se realiza la factura
    private String fechaHoraFormateada;
    private Cliente cliente; // Cliente al que se realizó la factura
    private Vendedor vendedor; // Vendedor que realizó la factura
    private Venta venta; // Venta relacionada con la propia factura
    private DetalleFactura[] detallesFactura; // Array de objetos de la clase detalle factura
    private float importeTotalSinImpuestos, importeTotalConImpuestos,
            impuestos; // Importes de nuestra factura
    // Bloque de inicialización estático
    static {
        contador = 0;
    }
    // Bloque de inicialización de instancia
    {
        // Incrementamos el contador y lo asignamos al id
        id = contador++;
        // Recogemos la fecha y hora actual de la factura
        fechaHora = LocalDateTime.now();
        // Formateamos la fecha y la hora y la recogemos en una variable String
        // para mostrarla de forma más legible
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        fechaHoraFormateada = fechaHora.format(formatter);
        detallesFactura = new DetalleFactura[10];
    }
    /**
     * Este constructor inicializa una nueva instancia de la clase Factura con los
     * detalles del cliente, vendedor, venta y los detalles de la factura.
     * Primero, se asignan los parámetros pasados (cliente, vendedor, venta y
     * detallesFactura) a los atributos correspondientes de la clase.
     * Luego, se inicializa el atributo importeTotalSinImpuestos a 0. Este atributo
     * representa el total de la factura sin incluir los impuestos.
     * Después, se recorre el array detallesFactura con un bucle for. Para cada
     * objeto DetalleFactura en el array, se multiplica la cantidad del producto por
     * el precio del producto, y se suma al importeTotalSinImpuestos.
     * A continuación, se calcula el importe de los impuestos. Se asume un porcentaje
     * de impuestos del 21%, por lo que se multiplica el importeTotalSinImpuestos
     * por 0.21.
     * Finalmente, se calcula el importe total con impuestos, sumando el
     * importeTotalSinImpuestos y los impuestos calculados.
     * @param cliente Cliente al que se realizó la factura
     * @param vendedor Vendedor que realizó la factura
     * @param venta Venta relacionada con la propia factura
     * @param detallesFactura Array de objetos de la clase detalle factura
     */
    // Constructor
    public Factura( Cliente cliente, Vendedor vendedor, Venta venta,
                    DetalleFactura[] detallesFactura) {
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.venta = venta;
        this.detallesFactura = detallesFactura;
        // Inicializamos el importe a 0
        this.importeTotalSinImpuestos =0;
        // Recorremos con un bucle for el array "detallesFactura"
        // y vamos sumando el resultado del precio de cada Producto
        // por su cantidad
        for(int i=0; i<detallesFactura.length; i++) {
            importeTotalSinImpuestos = detallesFactura[i].getCantidad()
                    * detallesFactura[i].getProducto().getPrecio();
        }
        // Calculamos el importe de los impuestos (21%)
        this.impuestos = (importeTotalSinImpuestos*21)/100;
        // Calculamos el total con impuestos
        this.importeTotalConImpuestos = importeTotalSinImpuestos + impuestos;
    }

    // Métodos y funcionalidades
    //Interfaz comparable
    /**
     * Implementación del método compareTo de la interfaz Comparable.
     * Compara esta factura con otra factura basándose en sus identificadores.
     * @param objeto La factura con la que se va a comparar.
     * @return 0 si las facturas son iguales, -1 si esta factura es menor
     * que la factura pasada, 1 si esta factura es mayor que la factura pasada.
     */
    @Override
    public int compareTo(Object objeto) {
        // Evaluamos si el objeto esta a "null"
        if(objeto==null) throw new IllegalArgumentException("El parámetro no puede ser nulo");
        // Evaluamos si el objeto es una instancia de "Factura"
        if(!(objeto instanceof Factura)) throw new IllegalArgumentException("El parámetro debe ser del tipo Empleado");
        // Comparamos si la factura pasada es mayor(1), menor(-1) o son iguales (0)
        if(((Factura)objeto).getId()==this.getId()) {
            // Si son iguales devolvemos un 0
            return 0;
        } else if(((Factura)objeto).getId()<this.getId()) {
            // Si el objeto pasado tiene un id menor devolvemos un -1
            return -1;
        } else {
            // Si el objeto pasado tiene un id mayor devolvemos un 1.
            return 1;
        }
    }

    // Añadir DetalleFactura
    /**
     * Método para añadir un detalle de factura al listado de estos de la factura.
     * @param detalleFactura deltalle de factura que va a ser añadida.
     * @return true si la factura se ha añadido correctamente, false en caso contrario.
     */
    public boolean addDetalleFactura(DetalleFactura detalleFactura) {
        // Inicializamos un constador para recorrer el listado de DetalleFactura
        int contador=0;
        // Evaluamos que sea una instancia de "DetalleFactura" y que no sea un valor nulo
        if((detalleFactura instanceof DetalleFactura) && (detalleFactura!=null)) {
            // Recorremos el listado de DetallesFactura, y añadimos el DetalleFactura donde el valor sea Null
            // es decir, donde este vacío. Como no sabemos cuando será, usamos do,while
            do {
                if(detallesFactura[contador] == null) {
                    // Añadimos la factura al array
                    detallesFactura[contador] = detalleFactura;
                    // Si se ha agregado devolvemos un true y así salimos del bucle también
                    return true;
                }
                // Incrementamos el contador
                contador++;
                // Evaluamos en la condición que contador no supere al tamaño del array
            } while((contador<detallesFactura.length));
        }
        // Si no se ha añadido correctamente devolvemos "false"
        return false;
    }
    // Buscar DetalleFactura
    /**
     * Método para buscar un detalle factura en el listado de detalles facturas de
     * la factura.
     * @param detalleFactura detalleFactura que se va a buscar en el listado
     * @return El índice de la factura en el listado si se encuentra, -1 si no se encuentra,
     * -2 si el parámetro no es válido.
     */
    public int buscarDetalleFactura(DetalleFactura detalleFactura) {
        // Inicializamos un contador para recorrer el array
        int contador=0;
        // Evaluamos que sea una instancia de "Factura" y que no sea un valor nulo
        if((detalleFactura instanceof DetalleFactura) && (detalleFactura!=null)) {
            // Recorremos el array de Facturas, como no sabemos cuando encontraremos la factura
            // emplearemos un bucle do,while
            do {
                // Usamos la interfaz comparable de "Factura" para evaluar si son iguales
                if(detallesFactura[contador].compareTo(detalleFactura) == 0) {
                    // Salimos del bucle si se ha cumplido la condición
                    // Con eso conseguimos que contador se quede con el valor de la posición
                    return contador;
                }
                // Incrementamos el contador
                contador++;
                // Evaluamos que contador no supere el tamaño del array
            } while((contador<detallesFactura.length));
            // Retornamos el valor -1 si no se ha encontrado ninguna
            return -1;
        }
        // Devolvemos un -2 en caso de que el parámetro no sea válido (nulo o de otro tipo)
        else return -2;
    }
    // Eliminar DetalleFactura
    /**
     * Método para eliminar un detalle del listado de detalles de la factura.
     * @param detalleFactura detalleFactura que se va a eliminar
     * @return true si la factura se ha eliminado correctamente, false en caso contrario.
     */
    public boolean eliminarDetalleFactura(DetalleFactura detalleFactura) {
        // Llamamos al método "buscarDetalleFactura" para encontrar la posición del elemento a
        // eliminar y le asignamos el valor a una variable
        int posicion = buscarDetalleFactura(detalleFactura);
        // Inicializamos un nuevo array de Ventas
        DetalleFactura[] nuevoDetallesFactura = new DetalleFactura[10];
        // Evaluamos que la posición devuelta sea mayor o igual a 0, ya que en caso contrario
        // significa que el objeto venta es nulo, de otro tipo o no se ha encontrado
        if(posicion>=0) {
            // Inicializamos un contador para recorrer el array nuevo
            int contador=0;
            // Recorremos el array de Ventas y la guardamos mientras no coincida, en el nuevo array
            // asi obtenemos un nuevo array usamos un bucle for porque debemos recorrerlo entero
            for(int i=0; i<detallesFactura.length; i++) {
                // Si no coincide la variable iteradora con la posición que deseamos eliminar,
                // la almacenamos e incrementamos el contador de posiciones del nuevo
                // array, en caso contrario no, así conseguimos que no se quede el hueco
                // de la posición que hemos eliminado en el nuevo listado
                if(i!=posicion) {
                    nuevoDetallesFactura[contador] = detallesFactura[i];
                    // Incrementamos el contador
                    contador++;
                }
            }
            // Reasignamos al listado de facturas el nuevo listado
            setDetallesFactura(nuevoDetallesFactura);
            // Devolvemos un true para verificar que se ha hecho correctamente
            return true;
        }
        // Devolvemos false, en caso de que no se haya eliminado ningun elemento y siga igual
        return false;
    }
    // Editar DetalleFactura
    /**
     * Método para modificar un DetalleFactura en el listado de DetalleFactura de la factura.
     * @param detalleFacturaBuscar DetalleFactura que se va a buscar y modificar.
     * @param detalleFacturaNueva el DetalleFactura nuevo que va reemplazará a el DetalleFactura antiguo.
     * @return true si la factura se ha modificado correctamente, false en caso contrario.
     */
    public boolean modificarDetalleFactura(DetalleFactura detalleFacturaBuscar,
                                           DetalleFactura detalleFacturaNueva) {
        // Llamamos al método buscar DetalleFactura para localizar la venta a modificar
        // y guardamos la posición en una variable
        int posicion = buscarDetalleFactura(detalleFacturaBuscar);
        // Evaluamos que la posición devuelta sea mayor o igual a 0, ya que en caso contrario
        // significa que el objeto DetalleFacturaNueva es nulo, de otro tipo o no se ha encontrado
        if(posicion>=0) {
            // Se lo asignamos a la factura nueva el identificador de la antigua
            detalleFacturaNueva.setId(detallesFactura[posicion].getId());
            // Le asignamos al objeto de factura en esa posición, los nuevos valores, que
            // son los del objeto "facturaNueva"
            detallesFactura[posicion] = detalleFacturaNueva;
            // Devolvemos un true para verificar que se ha hecho correctamente
            return true;
        }
        // Devolvemos false, en caso de que no se haya modficado ningun elemento y siga igual
        return false;
    }

    // GETTERS Y SETTERS
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public DetalleFactura[] getDetallesFactura() {
        return detallesFactura;
    }
    public void setDetallesFactura(DetalleFactura[] detallesFactura) {
        this.detallesFactura = detallesFactura;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public Vendedor getVendedor() {
        return vendedor;
    }
    public Venta getVenta() {
        return venta;
    }
    public float getImporteTotalSinImpuestos() {
        return importeTotalSinImpuestos;
    }
    public float getImporteTotalConImpuestos() {
        return importeTotalConImpuestos;
    }
    public float getImpuestos() {
        return impuestos;
    }
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

}