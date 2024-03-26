package venta;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Logger;

import logger.MyLogger;


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
    protected static Logger logger;
    // Bloque de inicialización estático
    static {
        // Inicializamos el logger estático para que nos sirva para la clase en general
        logger = MyLogger.getLogger("A");
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
        if(!(objeto instanceof Factura)) throw new IllegalArgumentException("El parámetro debe ser del tipo Factura");
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
    public boolean addDetalleFactura(Connection conexion, DetalleFactura detalleFactura) {
        if(detalleFactura == null) {
            logger.info("Detalle Factura no añadido");
            return false;
        }
        // Preparamos la sentencia SQL para insertar una venta en la base de datos
        String sql = "INSERT INTO detalleFactura (idDetalle, lineaFactura, cantidad, idFactura, referenciaProducto) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            // Creamos un PreparedStatement a partir de la sentencia SQL
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Asignamos los valores de la venta a los parámetros de la sentencia SQL
            statement.setInt(1, detalleFactura.getId());
            statement.setInt(2, detalleFactura.getLineaFactura());
            statement.setInt(3, detalleFactura.getCantidad());
            statement.setInt(4, detalleFactura.getFactura().getId());
            statement.setString(5, detalleFactura.getProducto().getReferencia());
            // Ejecutamos la sentencia SQL
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        logger.info("Detalle Factura no añadido");
        return true;
    }
    // Buscar DetalleFactura
    public String buscarDetalleFactura(Connection conexion, DetalleFactura detalleFactura) {
        // Comprobamos la nulidad
        if(detalleFactura == null) {
            logger.info("Objeto detalleFactura nulo");
            return null;
        }
        // Preparamos la sentencia SQL para buscar una venta en la base de datos
        String sql = "SELECT * FROM detalleFactura WHERE idDetalle = ?";
        try {
            // Creamos un PreparedStatement a partir de la sentencia SQL
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Asignamos el valor del id a los parámetros de la sentencia SQL
            statement.setInt(1, detalleFactura.getId());
            // Ejecutamos la sentencia SQL y obtenemos los resultados
            ResultSet rs = statement.executeQuery();
            // Si encontramos una venta, la devolvemos
            // Recorremos los registros de la consulta, pero como estamos filtrando
            while(rs.next()) {
                String resultadoString = "DetalleFactura encontrado: \nID: " + rs.getInt("idDetalle")
                        + "\nlineaFactura: " + rs.getInt("lineaFactura")
                        + "\ncantidad: " + rs.getInt("cantidad")
                        + "\nidFactura: " + rs.getInt("idFactura"
                        + "\nreferenciaProducto: " + rs.getString("referenciaProducto"));
                logger.info("DetalleFactura encontrado");
                return resultadoString;
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        // Si no hemos encontrado la factura devolvemos Null
        return null;
    }
    // Eliminar DetalleFactura
    public boolean eliminarDetalleFactura(Connection conexion, DetalleFactura detalleFactura) {
        // Comprobamos la nulidad
        if(detalleFactura == null) {
            logger.info("Objeto detalleFactura nulo");
            return false;
        }
        // Preparamos la sentencia para eliminarlo
        String sql = "DELETE FROM detalleFactura WHERE idDetalle = ?";
        try {
            // Creamos el PreparedStatement a partir de la sentencia creada
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Asignamos el valor del id al campo de la consulta "?"
            statement.setInt(1, detalleFactura.getId());
            // Ejecutamos la sentencia
            statement.executeUpdate();
            logger.info("Detalle Factura eliminada");
            return true;
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return false;
    }


    // Editar DetalleFactura
    public boolean modificarDetalleFactura(Connection conexion, DetalleFactura detalleFactura) {
        // Primero comprobamos la nulidad del objeto
        if(detalleFactura == null) {
            logger.info("Objeto detalleFactura nulo");
            return false;
        }
        // Preparamos la sentencia SQL para realizar un UPDATE
        String sql = "UPDATE detalleFactura SET lineaFactura = ?, cantidad = ?, " +
                "idFactura = ?, referenciaProducto = ? WHERE idDetalle = ?";
        try {
            // Creamos un objeto de PreparedStatement y le pasamos la consulta
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Asignamos los valores a los campos de la consulta
            statement.setInt(1, detalleFactura.getLineaFactura());
            statement.setInt(2, detalleFactura.getCantidad());
            statement.setInt(3, detalleFactura.getFactura().getId());
            statement.setString(4, detalleFactura.getProducto().getReferencia());
            statement.setInt(5, detalleFactura.getId());
            // Ejecutamos la consulta
            statement.executeUpdate();
            logger.info("DetalleFactura Modificado");
            return true;
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        // Si hemos llegado aquí significa que ha ocurrido una exepción y no se ha modificado
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

    @Override
    public String toString() {
        return "Factura{" +
                "id=" + id +
                ", fechaHora=" + fechaHora +
                ", fechaHoraFormateada='" + fechaHoraFormateada + '\'' +
                ", cliente=" + cliente +
                ", vendedor=" + vendedor +
                ", venta=" + venta +
                ", detallesFactura=" + Arrays.toString(detallesFactura) +
                ", importeTotalSinImpuestos=" + importeTotalSinImpuestos +
                ", importeTotalConImpuestos=" + importeTotalConImpuestos +
                ", impuestos=" + impuestos +
                '}';
    }
}