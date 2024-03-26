package producto;


import logger.MyLogger;
import venta.DetalleFactura;
import venta.Factura;
import venta.Venta;
import java.sql.*;

import java.sql.Connection;
import java.util.logging.Logger;

/**
 * Esta clase hace referencia a los distintos productos de nuestro sistema de
 * venta. Esta clase implementa la interfaz comparable.
 * @author Mlm96
 * @version 1.0
 */
public class Producto implements Comparable {
    private String referencia, descripcion;
    private float precio;
    private int marca, modelo, categoria;
    // Constantes de Categorias (Reservamos el valor 0 para asignar a las variables cuando
    // no se haya escogido ninguna de las opciones disponibles)
    private final int OTROS=0, BATERIAS=1, FILTROS=2, ACEITES=3;
    // Constantes de Marcas (Baterias)
    private final int VARTA=1, BOSCH=2;
    // Constantes de Marcas (Filtros)
    private final int MANN=1, PURFLUX=2, MILLARD=3;
    // Constantes de Marcas (Aceites)
    private final int REPSOL=1, PETRONAS=2;
    // Constantes de Modelos (Baterias) (Varta)
    private final int BLACK=1, BLUE=2, PLATINUM=3;
    // Constantes de Modelos (Filtros)
    private final int CHAPA=1, CARTUCHO=2;
    // Constantes de Modelos (Aceites) (Repsol)
    private final int PREMIUM=1, ELITE=2;
    private DetalleFactura detalleFactura;
    protected static Logger logger;
    private AtributoCategoria[] atributosCategoria;
    // Bloque estático
    static {
        // Inicializamos el logger estático para que nos sirva para la clase en general
        logger = MyLogger.getLogger("A");
    }
    // Bloque de inicializacion de instancia
    {
        // Le damos al array de Atributos un tamaño de 3
        atributosCategoria = new AtributoCategoria[3];
    }
    // Constructores
    // Constructor
    /**
     * Este constructor inicializa una nueva instancia de la clase Producto con los detalles
     * de la referencia, marca, modelo, categoría y precio.
     * Primero, se asigna el parámetro pasado referencia al atributo correspondiente de la
     * clase, convirtiéndolo a minúsculas y eliminando los espacios. Esto se hace para evitar
     * que se introduzcan más de un String diferente para la misma referencia.
     * Luego, se llama al método comprobarCategoria con el parámetro pasado categoria para
     * verificar la categoría del producto, y se asigna el resultado al atributo
     * correspondiente de la clase.
     * Después, se llama al método comprobarMarca con los parámetros pasados categoria y marca
     * para verificar la marca del producto, y se asigna el resultado al atributo
     * correspondiente de la clase.
     * A continuación, se llama al método comprobarModelo con los parámetros pasados
     * categoria, marca y modelo para verificar el modelo del producto, y se asigna el
     * resultado al atributo correspondiente de la clase.
     * Finalmente, se asigna el parámetro pasado precio al atributo correspondiente de la clase.
     * @param referencia referencia del producto, se convierte a minúsculas y se eliminan los
     * espacios para evitar duplicados.
     * @param marca marca del producto, se verifica con el método comprobarMarca.
     * @param modelo modelo del producto, se verifica con el método comprobarModelo.
     * @param categoria categoria del producto, se verifica con el método comprobarCategoria.
     * @param precio es el precio del producto.
     */
    public Producto(String referencia, int marca, int modelo,
                     int categoria, float precio) {
        // Ponemos el String pasado a referencia como minúscula y le quitamos los espacios
        // que le hayan podido introducir, con esto evitamos que de la misma referencia
        // se introduzcan más de un String diferente
        this.referencia = referencia.toLowerCase().replace(" ", "");
        // Llamamos al método correspondiente para asegurarnos de la categoría
        this.categoria = comprobarCategoria(categoria);
        // Llamamos al método correspondiente para asegurarnos de la marca
        this.marca = comprobarMarca(this.categoria, marca);
        // Llamamos al método correspondiente para asegurarnos del modelo
        this.modelo = comprobarModelo(this.categoria, this.marca, modelo);
        this.precio = precio;
    }
    // Segundo constructor, llamamos al primero, y además le pasamos la decripción
    public Producto( String referencia, int marca, int modelo,
                     int categoria, float precio, String descripcion) {
        // Llamamos al primer constructor
        this(referencia, marca, modelo, categoria, precio);
        // Asignamos la descripción
        this.descripcion = descripcion;
    }

    // Métodos y funcionalidades
    //Interfaz comparable
    /**
     * Implementación del método compareTo de la interfaz Comparable.
     * Compara este producto con otra producto basándose en sus referencias.
     * @param objeto el producto con la que se va a comparar.
     * @return 0 si los productos son iguales, -1 si este producto es menor
     * que el producto pasado, 1 si este producto es mayor que el producto pasado.
     */
    @Override
    public int compareTo(Object objeto) {
        // Evaluamos si el objeto esta a "null"
        if(objeto==null) throw new IllegalArgumentException("El parámetro no puede ser nulo");
        // Evaluamos si el objeto es una instancia de "Producto"
        if(!(objeto instanceof Producto)) throw new IllegalArgumentException("El parámetro debe ser del tipo Empleado");
        // Como en este caso el identificador es de tipo String, he usado el "compareto" de String
        return this.referencia.compareTo(((Producto)objeto).referencia);
    }

    // Método para comprobar la categoría
    /**
     * Este método comprueba la categoría del producto.
     * Si la categoría es BATERIAS, FILTROS o ACEITES, en cuyo caso devuelve
     * la categoría, en caso contrario, devuelve OTROS.
     * @param categoria categoría del producto.
     * @return la categoría del producto o OTROS si la categoría no es válida.
     */
    private int comprobarCategoria(int categoria) {
        if(categoria==BATERIAS || categoria==FILTROS || categoria==ACEITES) {
            return categoria;
        } else {
            return OTROS;
        }
    }
    // Método para comprobar la marca
    /**
     * Este método comprueba la marca del producto.
     * Comprueba si la marca es válida para la categoría dada, en cuyo caso
     * devuelve la marca, en caso contrario, devuelve OTROS.
     * @param categoria categoría del producto.
     * @param marca la marca del producto.
     * @return la marca del producto, u OTROS si no es válida.
     */
    private int comprobarMarca(int categoria, int marca) {
        if(categoria == BATERIAS && (marca==VARTA || marca==BOSCH)) {
            return marca;
        } else if(categoria == FILTROS && (marca==MANN || marca==PURFLUX || marca==MILLARD)) {
            return marca;
        } else if(categoria == ACEITES && (marca==REPSOL || marca==PETRONAS)) {
            return marca;
        } else {
            return OTROS;
        }
    }
    // Método para comprobar el modelo
    /**
     * Este método comprueba el modelo del producto.
     * Comprueba si el modelo es válido para la categoría y la marca dadas,
     * en cuyo caso, devuelve el modelo, en caso contrario, devuelve OTROS.
     * @param categoria categoría del producto.
     * @param marca marca la marca del producto.
     * @param modelo modelo El modelo del producto.
     * @return modelo del producto o OTROS si el modelo no es válido para la
     * categoría y la marca.
     */
    private int comprobarModelo(int categoria, int marca, int modelo) {
        if(categoria == BATERIAS && marca == VARTA && (modelo==BLACK || modelo==BLUE || modelo==PLATINUM)) {
            return modelo;
        } else if(categoria == FILTROS && (modelo==CHAPA || modelo==CARTUCHO)) {
            return modelo;
        } else if(categoria == ACEITES && marca == REPSOL && (modelo==PREMIUM || modelo==ELITE)) {
            return modelo;
        } else {
            return OTROS;
        }
    }

    // Añadir AtributoCategoria
    public boolean addAtributoCategoria(Connection conexion, AtributoCategoria atributoCategoria) {
        // Comprobamos la nulidad
        if(atributoCategoria == null) {
            logger.info("El objeto Atributo Producto es nulo.");
            return false;
        }
        // Creamos la sentencia sql
        String sql = "INSERT INTO atributoCategoria (idAtributo, atributo, valor, unidad" +
                ", referenciaProducto) VALUES ?, ?, ?, ?, ?";
        try {
            // Creamos un objeto de PreparedStatement y le pasamos la consulta creada
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Ahora le pasamos los valores de los atributos
            statement.setInt(1, atributoCategoria.getId());
            statement.setString(2, atributoCategoria.getAtributo());
            statement.setString(3, atributoCategoria.getValor());
            statement.setString(4, atributoCategoria.getUnidad());
            statement.setString(5, atributoCategoria.getProducto().getReferencia());
            // Ejecutamos la sentencia
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        logger.info("Atributo categoría añadido correctamente");
        return true;
    }

    // Buscar AtributoCategoria
    public String buscarAtributoCategoria(Connection conexion, AtributoCategoria atributoCategoria) {
        // Primero comprobamos la nulidad
        if(atributoCategoria == null) {
            logger.info("Objeto atributo categoría nulo");
            return null;
        }
        // Preparamos la consulta
        String sql = "SELECT * FROM atributoCategoria WHERE idAtributo = ?";
        try {
            // Creamos un objeto de PreparedStatement y le pasamos la consulta
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Ahora le asignamos el valor al campo "?"
            statement.setInt(1, atributoCategoria.getId());
            // Ejecutamos la sentencia y guardamos el resultado en un objeto de ResultSet
            // Como es una consulta SELECT, lo ejecutamos con executeQuery, ya que no vamos
            // a modificar ningún registro
            ResultSet rs = statement.executeQuery();
            // Utilizamos el objeto de ResultSet, para recorrer los diferentes resultados de
            // la consulta con un bucle while, aunque como estamos filtrando por el ID, solo
            // nos va a devolver un único registro, y por tanto se ejecutará una sola vez
            while (rs.next()) {
                String resultadoString = "AtributoCategoria encontrado: \nID: " + rs.getInt("idAtributo")
                        + "\natributo: " + rs.getString("atributo")
                        + "\nvalor: " + rs.getString("valor")
                        + "\nunidad: " + rs.getString("unidad")
                        + "\nreferenciaProducto: " + rs.getString("referenciaProducto");
                logger.info("Atributo Producto encontrado.");
                return resultadoString;
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        // Si no se ha encontrado se devolvera null
        return null;
    }
    // Eliminar AtributoCategoria
    public boolean eliminarAtributoCategoria(Connection conexion, AtributoCategoria atributoCategoria) {
        // Comprobamos la nulidad
        if(atributoCategoria == null) {
            logger.info("El objeto atributoCategoría es nulo.");
            return false;
        }
        // Creamos la sentencia SQL
        String sql = "DELETE FROM atributoCategoria WHERE idAtributo = ?";
        try {
            // Creamos un objeto de PreparedStatement
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Le pasamos el valor
            statement.setInt(1, atributoCategoria.getId());
            // Ejecutamos la sentencia
            statement.executeUpdate();
            logger.info("AtributoCategoria eliminada");
            return true;
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return false;
    }


    // Editar AtributoCategoria
    public boolean modificarAtributoCategoria(Connection conexion, AtributoCategoria atributoCategoria) {
        // Comprobamos la nulidad
        if(atributoCategoria == null) {
            logger.info("El objeto atributoCategoria es nulo.");
            return false;
        }
        // Creamos la sentencia
        String sql = "UPDATE atributoCategoria SET atributo = ?, valor = ?, unidad = ?," +
                "referenciaProducto = ? WHERE idAtributo = ?";
        try {
            // Creamos un objeto de PreparedStatement y le pasamos la sentencia anterior
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Le pasamos los valores de los campos
            statement.setString(1, atributoCategoria.getAtributo());
            statement.setString(2, atributoCategoria.getValor());
            statement.setString(3, atributoCategoria.getUnidad());
            statement.setString(4, atributoCategoria.getProducto().getReferencia());
            statement.setInt(5, atributoCategoria.getId());
            // Ejecutamos la consulta con "executeUpdate" ya que vamos a modificar valores
            statement.executeUpdate();
            logger.info("Atributo Producto Modificado");
            return true;
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return false;
    }


    // GETTERS Y SETTERS
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public int getMarca() {
        return marca;
    }
    /**
     * Este método set, comprueba si la marca es válida para la categoría.
     * Cuando cambia la marca, también se asegura de que el modelo sea válido
     * para esta nueva marca.
     * @param marca nueva marca que sustituye a la anterior.
     */
    public void setMarca(int marca) {
        // Solo cambiamos la marca si es válida para la categoría actual asi que
        // llamaremos al método "comprobarMarca" para comprobarlo.
        int nuevaMarca = comprobarMarca(this.categoria, marca);
        if (nuevaMarca != OTROS) {
            this.marca = nuevaMarca;
            // Cuando cambiamos la marca, también debemos asegurarnos de que el modelo
            // sea válido para esa marca, asi que llamamos a "comprobarModelo"
            this.modelo = comprobarModelo(this.categoria, this.marca, this.modelo);
        }
    }
    public int getModelo() {
        return modelo;
    }
    /**
     * Este método set comprueba si el modelo es correcto para la marca y categoría
     * @param modelo nuevo modelo del producto.
     */
    public void setModelo(int modelo) {
        // Solo cambiaremos el modelo si es válido para la marca y categoría actuales
        // asi que llamamos al método "comprobarModelo".
        int nuevoModelo = comprobarModelo(this.categoria, this.marca, modelo);
        if (nuevoModelo != OTROS) {
            this.modelo = nuevoModelo;
        }
    }
    public int getCategoria() {
        return categoria;
    }
    /**
     * Este método set comprueba que la marca y el modelo sean válidos para la
     * nueva categoría.
     * @param categoria nueva del producto
     */
    public void setCategoria(int categoria) {
        // Llamamos a comprobarCategoria para asegurarnos que sea válida
        this.categoria = comprobarCategoria(categoria);
        // Cuando cambiamos la categoría, también tenemos que asegurarnos de que la
        // marca y el modelo sean válidos para esa categoría.
        this.marca = comprobarMarca(this.categoria, this.marca);
        this.modelo = comprobarModelo(this.categoria, this.marca, this.modelo);
    }
    public float getPrecio() {
        return precio;
    }
    public void setPrecio(float precio) {
        this.precio = precio;
    }
    public DetalleFactura getDetalleFactura() {
        return detalleFactura;
    }
    public void setDetalleFactura(DetalleFactura detalleFactura) {
        this.detalleFactura = detalleFactura;
    }
    public AtributoCategoria[] getAtributosCategoria() {
        return atributosCategoria;
    }
    public void setAtributosCategoria(AtributoCategoria[] atributosCategoria) {
        this.atributosCategoria = atributosCategoria;
    }
    public String getReferencia() {
        return referencia;
    }

}