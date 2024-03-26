package pruebas;

import database.BaseDatos;
import logger.MyLogger;
import org.w3c.dom.Document;
import persona.Cliente;
import persona.Persona;
import persona.Vendedor;
import producto.Producto;
import venta.DetalleFactura;
import venta.Factura;
import venta.Venta;
import xml.Xml;
import xmlsqlconversor.XmlSqlConversor;

import javax.print.Doc;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Logger;

/**
 * Esta clase nos servirá para probar las diferentes clases y métodos de dichas clases de nuestro
 * proyecto, para verificar su correcto funcionamiento, o por el contrario, para detectar los
 * posibles errores existentes.
 * @author manuellaordenmartin
 * @version 1.0
 */
public class Test {
    protected static Logger logger;
    // Bloque de inicialización estático
    static {
        // Inicializamos el logger estático para que nos sirva para la clase en general
        logger = MyLogger.getLogger("A");
    }
    public static void main(String[] args) throws SQLException {
        // CONEXIÓN DB
        // Primero creamos la conexión con la base de datos y lo envolvemos en un try/catch
        // ya que el IDE nos indica una posible excepción de tipo comprobada
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto", "root", "7611");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // SCRIPT ESTRUCTURA DB
        // Instanciamos un String que va a contener la ruta del archivo con el Script
        String rutaScript = "C:\\Users\\Mlm96\\IdeaProjects\\sistema-de-venta\\src\\database\\Proyecto.sql";
        // Llamamos al método estático de la clase "BaseDatos" para poder utilizar el método
        // que ejecuta el Script SQL para generar la estructura de la base de datos
        BaseDatos.ejecutarScript(rutaScript, conexion);

        // INSTANCIAS DE LAS CLASES
        // Para empezar crearemos al menos una instancia de cada clase, teniendo en cuenta la
        // dependencia entre ellas, es decir, no puedo crear un objeto de "DetalleFactura", sin
        // crear un objeto de "Factura"

        // Primero crearemos un objeto de cliente, y para ello vamos a inicializar sus atributos
        String nifCliente = "22233343-s", nombreCliente = "antonio", direccion = "Avenida Genil",
                correo = "antonio@gmail.com"; // Le pasamos un correo para verificar que funcione la expresión regular
        LocalDate fechaNacCliente = LocalDate.of(1998, 10, 27);
        Cliente cliente = new Cliente(nifCliente, nombreCliente, fechaNacCliente, direccion, correo);
        // A continuación haremos lo mismo para Vendedor
        String nifVendedor = "324432-j", nombreVendedor = "federico";
        LocalDate fechaNacVendedor = LocalDate.of(1993, 11, 15);
        Vendedor vendedor = new Vendedor(nifVendedor, nombreVendedor, fechaNacVendedor);

        // Ahora que ya tenemos a una instancia de las personas que intervienen en la aplicación,
        // vamos a crear una instancia de producto para poder después crear una de de Venta y de
        // Factura, que son las que nos interesan para las pruebas de este RA. Para que el ejemplo
        // no sea demasiado largo, a este producto no le voy a asignar ningún objeto de la clase
        // AtributoProducto, ya que no todos los productos tienen porque incluir dicho atributo
        // en nuestro programa, bastará con los atributos de la clase "Producto"
        // Vamos a guardar los valores de los parámetros del constructor en variables, como antes
        String referencia = "hu716/2x";
        // Las siguientes son de tipo "int" porque hacen referencia a las constantes creadas en la clase
        // Producto, donde marca = 1 (marca "Mann-filter"), modelo = 1 (modelo "metalico") y
        // categoria = 2 (categoría "Filros)
        int marca = 1, modelo = 1, categoria = 2;
        float precio = 5;
        Producto producto = new Producto(referencia, marca, modelo, categoria, precio);

        // Creamos la instancia de venta, factura y detalle factura
        Venta  venta = new Venta(cliente, vendedor);
        // Creamos antes el detalle de factura, ya que necesitamos un array de
        // ellos, para calcular los impuestos, importe y demás en Factura
        // Inicializamos primero el array de DetalleFactura
        /*
        Parámetros: líneaFactura (1, "porque vamos a empezar en 1 en vez de 0"),
        cantidad (5) y producto
         */
        DetalleFactura detalleFactura = new DetalleFactura(1,
                5, producto);
        // Inicializamos un array, aunque en este caso con un solo elemento
        // para hacer la prueba
        DetalleFactura [] listaDetallesFacturas = {detalleFactura};

        Factura factura = new Factura(cliente, vendedor, venta, listaDetallesFacturas);

        System.out.println("Vamos a ello");
        // ----------------- MÉTODOS BASE DE DATOS -------------------------
        // AÑADIR REGISTROS DB
        // OJO: La llamada a los métodos y el proceso que se va a seguir es solo para verificar
        // que los métodos y la conexión con la base de datos funciones correctamente
        // Teniendo en cuenta que en un futuro el programa contará con una interfaz gráfica
        // desde la cual realizaremos todos estos procesos
        // Ahora vamos a añadir los objetos a la base de datos, para ello también tenemos que tener
        // en cuenta la dependencia entre ellos, es decir, en este caso hablamos de las claves
        // primarias y las claves foráneas entre las tablas
        // Debemos seguir el orden en el cual hemos creado las tablas en el Script SQL
        // Cliente y vendedor
        // Llamamos al método creado en esta misma clase para realizarlo
        System.out.println("Añadimos al cliente:");
        Test.addPersona(conexion, cliente); // Añadimos el cliente a la BBDD
        System.out.println("Añadimos al vendedor:");
        Test.addPersona(conexion, vendedor); // Añadimos el vendedor a la BBDD
        // Venta
        // Ahora que en la base de datos ya se encuentran el cliente y el vendedor, ya podemos
        // introducir la venta que depende de ellos (ya que Venta tiene sus claves primarias como foránea)
        // Para ello llamamos al método "addVenta" que esta en Persona, por lo que podemos usar para
        // llamarlo, tanto la instancia de Cliente, como la de Vendedor, esto no lo cambiará,
        // ya que aunque se pase a la base de datos desde una sola de estas instancias, ya posee
        // los id de ambas, desde que la hemos creado
        System.out.println("Añadimos a la venta:");
        cliente.addVenta(conexion, venta);
        // Factura
        // Realizamos lo mismo con el objeto de Factura
        System.out.println("Añadimos a la Factura:");
        cliente.addFactura(conexion, factura);
        // Producto

        // AtributoCategoria

        // DetalleFactura


        // BUSCAR REGISTROS  (buscaremos la factura y la venta, para verificar que se hayan
        // añadido correctamente
        // De nuevo utilizaremos el objeto de Cliente, pero realmente serviría cualquiera,
        // ya que lo único que necesitamos es una instancia de Persona
        System.out.println("Vamos a buscar la Factura anterior:");
        // Como devuelve un String, aprovechamos y lo enseñamos por consola
        System.out.println(cliente.buscarFactura(conexion, factura));
        System.out.println();
        System.out.println("Vamos a buscar la Venta anterior:");
        System.out.println(cliente.buscarVenta(conexion, venta));
        System.out.println("--------------------------"); // Separador
        System.out.println();

        // EDITAR REGISTROS
        // Para editar a ambos, crearemos un nuevo Cliente, y se lo pasaremos para crear
        // una nueva instancia de cada clase que sustituya al registro anterior
        Cliente clienteNuevo = new Cliente("000000-t", "Manolo",
                LocalDate.of(1998, 10, 27),
                "Calle Ancha", "manolo@gmail.com");
        // Antes de modificar la factura y la venta, debo insertar este nuevo cliente, ya que
        // sino el campo idCliente, hará referencia a un cliente que no se encuentra en la base
        Test.addPersona(conexion, clienteNuevo);
        Factura facturaNueva = new Factura(clienteNuevo, vendedor,
                venta, listaDetallesFacturas); // Creamos la nueva Factura
        // Todos los campos seguiran igual, menos el idCliente, incluso la hora será la misma
        // ya que se ejecuta el código casí a la vez
        System.out.println("Vamos a modificar la Factura modificada (solo veremos el cambio en el " +
                "idCliente, ya que lo demás es igual):");
        cliente.modificarFactura(conexion, facturaNueva, factura); // Modificamos la factura con los nuevos datos
        System.out.println("Vamos a modificar la Venta modificada (solo veremos el cambio en el " +
                "idCliente, ya que lo demás es igual):");
        Venta ventaNueva = new Venta(clienteNuevo, vendedor); // Creamos la nueva Venta
        cliente.modificarVenta(conexion, ventaNueva, venta); // Modificamos la venta con los nuevos datos

        // Volvemos a llamar a Buscar para verificar que se haya modificado
        // Como vamos a buscarlo por el ID y el método modificar cambia todos los campos
        // menos el del ID para evitar posibles problemas, para localizar al registro
        // debemos de seguir haciendo uso del objeto anterior de Factura y Venta, ya que
        // tendrán los mismos ID que el registro
        // Esto es así porque el método de buscar buscar el registro a traves de su ID
        System.out.println("\nVamos a buscar la Factura modificada (como el :");
        System.out.println(cliente.buscarFactura(conexion, factura));
        System.out.println("\nVamos a buscar la Venta modificada:");
        System.out.println(cliente.buscarVenta(conexion, venta));
        System.out.println("--------------------------"); // Separador

        // ELIMINAR REGISTROS (eliminaremos los registros anteriores)
        System.out.println("\nVamos a eliminar la Factura:");
        cliente.eliminarFactura(conexion, factura);
        System.out.println("\nVamos a eliminar la Venta:");
        cliente.eliminarVenta(conexion, venta);
        System.out.println("--------------------------"); // Separador

        // Volvemos a llamar a Buscar para verificar que se haya eliminado
        System.out.println("\nVamos a buscar la Factura eliminada, debe devolvernos null:");
        System.out.println(cliente.buscarFactura(conexion, factura));
        System.out.println("Vamos a buscar la Venta eliminada, debe devolvernos null:");
        System.out.println(cliente.buscarVenta(conexion, venta));
        System.out.println("--------------------------"); // Separador


        // CONVERSOR SQL-XML SENTENCIA
        System.out.println("Vamos a transformar un resultado en un XML.");
        // Vamos a crear un XML con los clientes de nuestra base de datos
        // Para ello llamaremos al método que hemos creado con dicho fin de la clase que hemos creado
        // XmlSqlConversor, y le pasaremos por parámetro una llamada al método "listarClientes" el
        // cual nos devolverá un objeto de ResultSet
        // Como esto nos devuelve un objeto document, vamos a instanciarlo para recogerlo
        Document documento = null;
        documento = XmlSqlConversor.sqlToXml(Test.listarClientes(conexion));
        try {
            // Ahora procederemos a guardar dicho Documento, ya que ahora mismo solo está en memoria
            // Creamos un objeto de TransformerFactory, que nos permitirá crear objetos de tipo
            // Transformer
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            // Ahora creamos la instancia de Transformer
            Transformer transformer = null;
            transformer = transformerFactory.newTransformer();
            // Creamos un objeto de DOMSource, que hará de contenedor para nuestro xml que tenemos en memoria
            // por eso se lo pasamos por parámetro
            DOMSource source = new DOMSource(documento);
            // Creamos una instancia de StreamResult que será el contenedor para el resultado de la transformación
            StreamResult resultado = new StreamResult(new File("empleados.xml"));
            // Por último, con el objeto de transformer realizamos la transformación final, le pasamos el
            // contenedor del resultado, y el de nuestro document
            transformer.transform(source, resultado);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        // CONVERSOR XML-SQL SENTENCIA
        System.out.println("\nVamos a transformar un XML en registros de la base de datos.");
        // Llamamos al método y le pasamos un xml con un par de clientes, para que lo añada
        // a la base de datos
        // Como el método requiere de un Document con los nuevos clientes, llamaremos al método
        // Crear documento y luego a addPersona, de la clase XML para crear el Document
        Xml<Cliente> xmlClientes = new Xml<>(); // Creamos la instancia para los métodos
        Document documentoClientes = xmlClientes.crearDocumento(); // Creamos el documento
        // Ahora le asignamos los 2 objetos de Cliente al documento
        Cliente clienteDos = new Cliente("11122233", "felipe", LocalDate.of(2000, 3, 3));
        Cliente clienteTres = new Cliente("55566677", "alfredo", LocalDate.of(2000, 3, 3));
        xmlClientes.addPersonaXml(clienteDos , documentoClientes); // Add ClienteDos
        xmlClientes.addPersonaXml(clienteTres , documentoClientes); // Add ClienteTres

        // Evaluamos con un if si se realiza correctamente
        if(XmlSqlConversor.xmlToSql(documentoClientes, conexion)){
            System.out.println("Se han insertado los registros correctamente.");
        } else System.out.println("No se han insertado los registros correctamente.");

        // Por último, para verificar que se han insertado dichos registros, vamos a listar
        // los clientes de la tabla de la base de datos
        // Como nos devuelve un ResultSet conj el resultado del SELECT, voy a crear un objeto de este
        ResultSet rs = Test.listarClientes(conexion);
        // Ahora lo recorremos para mostrar los diferentes clientes de la lista
        System.out.println("\nVamos a listar los clientes de la base de datos:");
        while(rs.next()) {
            System.out.println("ID: " + rs.getInt("idCliente")
                    + "\nnif: " + rs.getString("nif")
                    + "\nnombre: " + rs.getString("nombre"));
            System.out.println("----------------------------"); // Separador
        }

    }

    // Como no tenemos métodos para añadir a Personas a nuestra base de datos,
    // los vamos a crear aqui en nuestra clase principal que sea estático
    public static boolean addPersona(Connection conexion, Persona persona) {
        // Comprobamos la nulidad
        if(persona == null) {
            logger.info("Persona no añadida, el objeto Persona es nulo.");
            return false;
        }
        // Declaramos un String, que contendra la sentencia
        String sql;
        // Creamos un PreparedStatement y le pasamos después la sentencia SQL
        PreparedStatement statement = null;
        // Evaluamos si se trata de una instancia de "Cliente" o de "Vendedor", para
        // modificar la sentencia en consecuencia
        if(persona instanceof Cliente) {
            // Creamos la instancia de Cliente a partir de la persona
            Cliente cliente = (Cliente) persona;
            // Instancia de Cliente
            sql = "INSERT INTO cliente (idCliente, nif, nombre, direccion, correo, " +
                    "telefono, edad, fechaNac) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                statement = conexion.prepareStatement(sql);
                // Asignamos los valores de la venta a los parámetros de la sentencia SQL
                // Insertamos los valores de los atributos del objeto en la posición correspondiente
                // en el mismo orden de los campos a los que hace rerferencia
                statement.setInt(1, cliente.getId()); // Insertamos ID
            } catch (SQLException e) {
                System.out.println(e.getMessage());;
            }
        } else {
            Vendedor vendedor = (Vendedor) persona;
            // Instancia de Vendedor
            sql = "INSERT INTO vendedor (idVendedor, nif, nombre, direccion, correo, " +
                    "telefono, edad, fechaNac) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                statement = conexion.prepareStatement(sql);
                // Asignamos los valores de la venta a los parámetros de la sentencia SQL
                // Insertamos los valores de los atributos del objeto en la posición correspondiente
                // en el mismo orden de los campos a los que hace rerferencia
                statement.setInt(1,  vendedor.getId()); // Insertamos ID
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        // Volvemos a envolver el "statement" con un try/catch", por si ocurre una excepción
        try {
            // Para el campo fechaHora, al ser de tipo LocalDateTime, tenemos que pasarlo a
            // tipo "Timestamp" y emplear el método correspondiente "setTimestamp"
            statement.setString(2, persona.getNif()); // Insertamos fechaHora
            statement.setString(3, persona.getNombre());
            statement.setString(4, persona.getDireccion());
            statement.setString(5, persona.getCorreo());
            statement.setInt(6, persona.getTelefono());
            statement.setInt(7, persona.getEdad());
            statement.setDate(8, Date.valueOf(persona.getFechaNac()));
            // Ejecutamos la sentencia SQL
            statement.executeUpdate();
            logger.info("Persona añadida");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            logger.info("Persona no añadida");
            return false;
        }

    }

    /**
     * Este método es similar al método de buscar, pero en este caso vamos a devolver el listado de
     * todos sus registros de la tabla, ya que en este método no filtramos por el "ID"
     * @param conexion
     * @return
     */
    public static ResultSet listarClientes(Connection conexion) {
        // Preparamos la sentencia SQL para buscar una venta en la base de datos
        String sql = "SELECT * FROM cliente";
        try {
            // Creamos un PreparedStatement a partir de la sentencia SQL
            PreparedStatement statement = conexion.prepareStatement(sql);
            // Ejecutamos la sentencia y guardamos el resultado en un objeto de ResultSet
            // Como es una consulta SELECT, lo ejecutamos con executeQuery, ya que no vamos
            // a modificar ningún registro
            ResultSet rs = statement.executeQuery();
            return rs;
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        // Si no hemos encontrado la factura devolvemos Null
        return null;
    }



}
