package persona;

import java.time.LocalDate;

import logger.MyLogger;

import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;

import venta.Factura;
import venta.Venta;

import javax.swing.*;

/**
 * Esta clase abstracta hace referencia a una persona dentro del sistema de ventas.
 * Tiene una seria de atributos que representan información sobre la persona, además
 * también lleva un registro de las ventas y facturas asociadas a la propia persona.
 * @author Mlm96
 * @version 1.0
 */
public abstract class Persona {
    // Declaramos los atributos
    protected String nif, nombre, direccion, correo;
    protected int telefono, edad;
    protected LocalDate fechaNac;
    protected static Logger logger;
    // Bloque estático
    static {
        // Inicializamos el logger estático para que nos sirva para la clase en general
        logger = MyLogger.getLogger("A");
    }
    /**
     * Este constructor inicializa una instancia de la clase "Persona" con los datos
     * imprescindibles para ello. Además a partir de la fecha de nacimiento, calcula la
     * edad mediante la operación de resta entre dicha fecha de nacimiento, y la actual.
     * @param nif Se trata del número de identificación fiscal.
     * @param nombre Es el nombre de la persona en cuestión.
     * @param fechaNac Es la fecha de nacimiento de la persona.
     */
    // Constructores
    // Primer constructor, pedimos lo imprescindible: nif y nombre
    public Persona(String nif, String nombre, LocalDate fechaNac) {
        this.nif = nif;
        this.nombre = nombre;
        this.fechaNac = fechaNac;
        // Calculamos la edad con el "ChronoUnit" y usamos la fechaNac y el LocalDate.now()
        edad = (int) ChronoUnit.YEARS.between(fechaNac, LocalDate.now());
    }
    // Segundo constructor, llamamos al primer constructor y además
    // pedimos la direccion y el correo
    /**
     * En este constructor además de llamar al primero, le pedimos la dirección y el correo
     * en cuanto al correo nos aseguramos del formato realizando algunas validaciones
     * mediante el uso de StringBuilder
     * @param nif Se trata del número de identificación fiscal.
     * @param nombre Es el nombre de la persona en cuestión.
     * @param fechaNac Es la fecha de nacimiento de la persona.
     * @param direccion Es la direccion de la persona
     * @param correo Se trata del correo de la persona
     */
    public Persona( String nif, String nombre,  LocalDate fechaNac,
                   String direccion, String correo) {
        this(nif,nombre, fechaNac);
        this.direccion = direccion;
        // Validamos el formato del correo llamando al método set que tiene las comprobaciones
        this.correo = setCorreo(correo);
    }
    // Tercer constructor, llamamos al primer constructor y además
    // pedimos la telefono
    public Persona( String nif, String nombre, int telefono, LocalDate fechaNac) {
        this(nif,nombre, fechaNac);
        this.telefono = telefono;
    }
    // Cuarto constructor, llamamos al segundo y además pedimos el teléfono
    public Persona( String nif, String nombre, int telefono, LocalDate fechaNac,
                   String direccion, String correo) {
        this(nif, nombre, fechaNac, direccion, correo);
        this.telefono = telefono;
    }

    // Métodos y funcionalidades
    // Método genérico, que implementamos de la interfaz Listable

    // Mostrar informacion (método abstracto)
    /**
     * Método abstracto para mostrar la información relacionada con la persona.
     * Al ser abstracta será sobreescrita por las clases hijas.
     */
    public abstract void mostrarInformacion();

    // FACTURA
    // Añadir Factura
    public boolean addFactura(Connection conexion, Factura factura) {
        if(factura != null) {
            // Preparamos la sentencia SQL para insertar una venta en la base de datos
            String sql = "INSERT INTO factura (idFactura, fechaHora, idCliente, idVendedor, idVenta, " +
                    "importeTotalSinImpuestos, importeTotalConImpuestos, impuestos) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                // Creamos un PreparedStatement a partir de la sentencia SQL
                PreparedStatement statement = conexion.prepareStatement(sql);
                // Asignamos los valores de la venta a los parámetros de la sentencia SQL
                // Insertamos los valores de los atributos del objeto en la posición correspondiente
                // en el mismo orden de los campos a los que hace rerferencia
                statement.setInt(1, factura.getId()); // Insertamos ID
                // Para el campo fechaHora, al ser de tipo LocalDateTime, tenemos que pasarlo a
                // tipo "Timestamp" y emplear el método correspondiente "setTimestamp"
                statement.setTimestamp(2, Timestamp.valueOf(factura.getFechaHora())); // Insertamos fechaHora
                statement.setInt(3, factura.getCliente().getId());
                statement.setInt(4, factura.getVendedor().getId());
                statement.setInt(5, factura.getVenta().getId());
                statement.setDouble(6, factura.getImporteTotalSinImpuestos());
                statement.setDouble(7, factura.getImporteTotalConImpuestos());
                statement.setDouble(8, factura.getImpuestos());
                // Ejecutamos la sentencia SQL
                statement.executeUpdate();
                logger.info("Factura añadida");
                return true;
            } catch (SQLException e) {
                // Usamos el logger para trazar el mesaje de la excepción
                logger.info(e.getMessage());
            }
        }
        logger.info("Factura no añadida");
        return false;
    }
    // Buscar Factura
    public String buscarFactura(Connection conexion, Factura facturaBuscar) {
        // Preparamos la sentencia SQL para buscar una venta en la base de datos
        String sql = "SELECT * FROM factura WHERE idFactura = ?";
        try {
            // Creamos un PreparedStatement a partir de la sentencia SQL
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Asignamos el valor del id a los parámetros de la sentencia SQL
            statement.setInt(1, facturaBuscar.getId());
            // Ejecutamos la sentencia SQL y obtenemos los resultados
            ResultSet rs = statement.executeQuery();
            // Si encontramos una venta, la devolvemos, solo lo realizamos una vez, porque solo
            // debe haber una sola factura con ese ID
            while(rs.next()) {
                // Mostramos por consola el resulado de la consulta
                // Hacemos alusión a los diferentes campos de la consulta mediante su nombre, y nos
                // traemos su valor mediante el "get" correspondiente
                String resultadoString = "Factura encontrada: \nID: " + rs.getInt("idFactura")
                        + "\nfechaHora: " + rs.getTimestamp("fechaHora")
                        + "\nidCliente: " + rs.getInt("idCliente")
                        + "\nidVendedor: " + rs.getInt("idVendedor")
                        + "\nidVenta: " + rs.getInt("idVenta")
                        + "\nimporteTotalSinImpuestos: " + rs.getDouble("importeTotalSinImpuestos")
                        + "\nimporteTotalConImpuestos: " + rs.getDouble("importeTotalConImpuestos")
                        + "\nimpuestos: " + rs.getDouble("impuestos");
                logger.info("Factura encontrada");
                return resultadoString;
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        // Si no hemos encontrado la factura devolvemos Null
        return null;
    }
    // Eliminar Factura
    public boolean eliminarFactura(Connection conn, Factura factura) {
        // Preparamos la sentencia SQL para eliminar una venta de la base de datos
        String sql = "DELETE FROM factura WHERE idFactura = ?";
        try {
            // Creamos un PreparedStatement a partir de la sentencia SQL
            PreparedStatement statement = conn.prepareStatement(sql);
            // Asignamos el valor del id a los parámetros de la sentencia SQL
            statement.setInt(1, factura.getId());
            // Ejecutamos la sentencia SQL
            statement.executeUpdate();
            logger.info("Factura eliminada");
            return true;
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return false;
    }
    // Editar Factura
    public boolean modificarFactura(Connection conexion, Factura facturaNueva, Factura facturaBuscar) {
        if(facturaNueva == null || facturaBuscar == null) {
            logger.info("Uno de los dos objetos de Factura es nulo");
            return false;
        }
        // Preparamos la sentencia SQL para modificar una venta en la base de datos
        String sql = "UPDATE factura SET fechaHora = ?, idCliente = ?, idVendedor = ?, " +
                "idVenta = ?, importeTotalSinImpuestos = ?, importeTotalConImpuestos = ?," +
                "impuestos = ? WHERE idFactura = ?";
        try {
            // Creamos un PreparedStatement a partir de la sentencia SQL
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Asignamos los valores de la venta a los parámetros de la sentencia SQL
            statement.setTimestamp(1, Timestamp.valueOf(facturaNueva.getFechaHora()));
            statement.setInt(2, facturaNueva.getCliente().getId());
            statement.setInt(3, facturaNueva.getVendedor().getId());
            statement.setInt(4, facturaNueva.getVenta().getId());
            statement.setDouble(5, facturaNueva.getImporteTotalSinImpuestos());
            statement.setDouble(6, facturaNueva.getImporteTotalConImpuestos());
            statement.setDouble(7, facturaNueva.getImpuestos());
            // Como el id que es el campo por el cual buscamos, lo vamos a dejar igual,
            // recurrimos a extraer el id del objeto factura antiguo
            statement.setInt(8, facturaBuscar.getId());
            // Ejecutamos la sentencia SQL
            statement.executeUpdate();
            logger.info("Factura modificada");
            return true;
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        // Si hemos llegado hasta aquí significa que ha saltado la execpción y por tanto no se ha
        // modificado el registro. Devolvemos false para indicarlo
        return false;
    }


    // VENTA
    // Añadir Venta
    public boolean addVenta(Connection conexion, Venta venta) {
        if(venta == null) {
            logger.info("Venta no añadida");
            return false;
        }
        // Preparamos la sentencia SQL para insertar una venta en la base de datos
        String sql = "INSERT INTO venta (idVenta, fechaHora, idCliente, idVendedor) " +
                "VALUES (?, ?, ?, ?)";
        try {
            // Creamos un PreparedStatement a partir de la sentencia SQL
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Asignamos los valores de la venta a los parámetros de la sentencia SQL
            statement.setInt(1, venta.getId());
            statement.setTimestamp(2, Timestamp.valueOf(venta.getFechaHora()));
            statement.setInt(3, venta.getCliente().getId());
            statement.setInt(4, venta.getVendedor().getId());
            // Ejecutamos la sentencia SQL mediante "executeUpdate" ya que estamos añadiendo
            // un nuevo registro, si modificamos o eliminamos usaremos el mismo
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        logger.info("Venta añadida");
        return true;
    }
    // Buscar Venta
    public String buscarVenta(Connection conexion, Venta ventaBuscar) {
        // Preparamos la sentencia SQL para buscar una venta en la base de datos
        String sql = "SELECT * FROM venta WHERE idVenta = ?";
        try {
            // Creamos un PreparedStatement a partir de la sentencia SQL
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Asignamos el valor del id a los parámetros de la sentencia SQL
            statement.setInt(1, ventaBuscar.getId());
            // Ejecutamos la sentencia y guardamos el resultado en un objeto de ResultSet
            // Como es una consulta SELECT, lo ejecutamos con executeQuery, ya que no vamos
            // a modificar ningún registro
            ResultSet rs = statement.executeQuery();
            // Si encontramos una venta, la devolvemos
            // Recorremos los registros de la consulta, pero como estamos filtrando por el
            // ID, solo aparecerá un unico registro, por lo que el bucle solo se ejecuta
            // 1 vez. Hacemos uno del método "next" el cual actúa como un Iterador,
            // y nos devuelve true siempre que haya un elemento
            while(rs.next()) {
                String resultadoString = "Venta encontrada: \nID: " + rs.getInt("idVenta")
                        + "\nfechaHora: " + rs.getTimestamp("fechaHora")
                        + "\nidCliente: " + rs.getInt("idCliente")
                        + "\nidVendedor: " + rs.getInt("idVendedor");
                logger.info("Venta encontrada");
                return resultadoString;
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        // Si no hemos encontrado la factura devolvemos Null
        return null;
    }
    // Eliminar Venta
    public boolean eliminarVenta(Connection conexion, Venta venta) {
        // Preparamos la sentencia SQL para eliminar una venta de la base de datos
        String sql = "DELETE FROM venta WHERE idVenta = ?";
        try {
            // Creamos un PreparedStatement a partir de la sentencia SQL
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Asignamos el valor del id a los parámetros de la sentencia SQL
            statement.setInt(1, venta.getId());
            // Ejecutamos la sentencia SQL
            statement.executeUpdate();
            logger.info("Venta eliminada");
            return true;
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return false;
    }
    // Editar venta
    public boolean modificarVenta(Connection conexion, Venta ventaNueva, Venta ventaBuscar) {
        if(ventaNueva == null || ventaBuscar == null) {
            logger.info("Uno de los objetos de venta es nulo.");
            return false;
        }
        // Preparamos la sentencia SQL para modificar una venta en la base de datos
        String sql = "UPDATE venta SET fechaHora = ?, idCliente = ?, idVendedor = ? WHERE idVenta = ?";
        try {
            // Creamos un PreparedStatement a partir de la sentencia SQL
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Asignamos los valores de la venta a los parámetros de la sentencia SQL
            statement.setTimestamp(1, Timestamp.valueOf(ventaNueva.getFechaHora()));
            statement.setInt(2, ventaNueva.getCliente().getId());
            statement.setInt(3, ventaNueva.getVendedor().getId());
            // Como el id que es el campo por el cual buscamos, lo vamos a dejar igual,
            // recurrimos a extraer el id del objeto venta antiguo
            statement.setInt(4, ventaBuscar.getId());
            // Ejecutamos la sentencia SQL
            statement.executeUpdate();
            logger.info("Venta modificada");
            return true;
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        // Si ha llegado aquí significa que ha saltado el error, por lo tanto no se ha
        // modificado la venta, así que devolvemos "false"
        return false;
    }


    // GETTERS Y SETTERS
    // Nombre
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    // Dirección
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    // Teléfono
    public int getTelefono() {
        return telefono;
    }
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
    // Nif
    public String getNif() {
        return nif;
    }

    // Edad
    public int getEdad() {
        return edad;
    }
    // Fecha nacimiento
    public LocalDate getFechaNac() {
        return fechaNac;
    }
    /**
     * Con este método damos una nueva fecha de nacimiento, y volvemos a restablecer la edad en
     * consecuencia a esta.
     * En esta clase no he puesto setEdad, para evitar que se modifique la edad sin cambiar la
     * fecha de nacimiento, ya que ambas están relacionadas.
     * @param fechaNac es la nueva fecha de nacimiento
     */
    public void setFechaNac(LocalDate fechaNac) {
        this.fechaNac = fechaNac;
        // Al cambiar la fecha de nacimiento debemos cambiar la edad en consecuencia
        // Calculamos la edad con el "ChronoUnit" y usamos la fechaNac y el LocalDate.now()
        edad = (int) ChronoUnit.YEARS.between(fechaNac, LocalDate.now());
    }
    public String getCorreo() {
        return correo;
    }
    /**
     * Este método set comprueba el formato del correo y en caso de no cumplirlo no se le asigna
     * y se devuelve un correo por defecto.
     * @param correo es el nuevo correo
     * @return retorna el nuevo correo en caso de que cumpla con el formato, o devolvemos un
     * valor por defecto en caso contrario.
     */
    public String setCorreo(String correo) {
        // CRITERIO: Se han utilizado expresiones regulares en la búsqueda de patrones en cadenas de texto (10%).
        // Creamos un objeto de Pattern pasandole el formato que queremos para el correo
        // mediante una expresión regular
        /*
         Explicación de la expresión:
            - ( \\w|\\-){1,60}: La \\w indica que permite caracteres alfanuméricos y "_", pero como
                algunos correos pueden incluir "-" lo incluimos como opción dentro del paréntesis
                y usando el operador lógico or "|". En cuanto al tamaño he puesto que sea de 1 caracter
                como mínimo, para asegurarme que tenga algun nombre delante del "@", y le he puesto un
                límite de 60, para evitar que se introduzcan infinitos caracteres.
            - @: En este caso este caracter lo incamos directamente y sin un tamaño, porque no existen
                más opciones después del nombre que no sea la @ y solo debe haber una.
            - [a-zA-Z]{1,50}: Esta parte hace referencia al dominio del correo, como solo he visto dominios
                que contengan letras, he puesto el rango solamente de caracteres del alfabeto, tanto mayuscula
                como minuscula. Además un tamaño entre 1 y 50.
            - \\.: al igual que en la @, queremos indicar que en esta posición va un punto, y solo uno, pero
                al tratarse el "." de un caracter reservado, debemos utilizar los caracteres de escape \\,
                al igual que en la "w" y "-".
            - [a-z]{1,6}: Por último, en esta parte que hace referencia a la extensión del correo (Por ejemplo:
                "com","es",...) he puesto que acepte caracteres alfabéticos en minusculas, porque no he visto
                ningún correo que utilice otro tipo de caracteres, además en este caso el tamaño que he puesto
                también es mucho menor, de 1 a 6 caracteres.
         */
        Pattern patron = Pattern.compile("(\\w|\\-){1,60}@[a-zA-Z]{1,50}\\.[a-z]{1,6}");
        // Creamos un objeto de Matcher que nos servirá para validar si se ha cumplido con
        // el patron definido
        Matcher matcher = patron.matcher(correo);
        // Utilizamos el objeto de "matcher" para verificar mediante un condicional si se ha cumplido
        // en caso afirmativo, asignamos el correo pasado por parámetro y lo devovemos
        if(matcher.matches()) {
            this.correo = correo;
            return correo;
        } else {
            // Si el formato no es correcto le asignamos un correo por defecto para indicar
            // que no existe
            return "correo@pordefecto.com";
        }
    }


}