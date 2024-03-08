package pruebas;

import org.w3c.dom.Document;
import persona.Cliente;
import persona.Persona;
import persona.Vendedor;
import producto.Producto;
import venta.DetalleFactura;
import venta.Factura;
import venta.Venta;
import xml.Xml;

import java.time.LocalDate;
/**
 * Esta clase nos servirá para probar las diferentes clases y métodos de dichas clases de nuestro
 * proyecto, para verificar su correcto funcionamiento, o por el contrario, para detectar los
 * posibles errores existentes.
 * @author manuellaordenmartin
 * @version 1.0
 */
public class Test {
    public static void main(String[] args) {
        // Para empezar crearemos al menos una instancia de cada clase, teniendo en cuenta la
        // dependencia entre ellas, es decir, no puedo crear un objeto de "DetalleFactura", sin
        // crear un objeto de "Factura"

        // Primero crearemos un objeto de cliente, y para ello vamos a inicializar sus atributos
        String nifCliente = "22233343-s", nombreCliente = "antonio", direccion = "Avenida Genil",
                correo = "antonio@gmail.com"; // Le pasamos un correo para verificar que funcione la expresión regular
        LocalDate fechaNacCliente = LocalDate.of(1998, 10, 27);
        Cliente cliente = new Cliente(nifCliente, nombreCliente, fechaNacCliente, direccion, correo);
        // A continuación haremos lo mismo para Vendedor
        String nifVendedor = "324432432-j", nombreVendedor = "federico";
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


        // ------ XML -----------

        // Creamos dos Documentos, uno para los clientes y otro para vendedor
        // Primero creamos una instancia de la clase  para usar los métodos, con cada tipo de dato
        // que admite en este caso todos los que hereden de "Persona" (Cliente y Vendedor)
        // Esto en si se podría hacer de otra forma, pero es la manera que he encontrado
        // para implementar genéricos
        Xml<Cliente> xmlCliente = new Xml<>();
        Xml<Vendedor> xmlVendedor = new Xml<>();
        // Creamos los documentos correspondientes
        Document docCliente = xmlCliente.crearDocumento(); // Creamos un documento de CLiente
        Document docVendedor = xmlVendedor.crearDocumento(); // Creamos un documento de Vendedor

        // Añadimos el cliente a su xml y vendedor al suyo
        xmlCliente.addPersonaXml(cliente, docCliente); // Cliente
        xmlVendedor.addPersonaXml(vendedor, docVendedor); // Vendedor

        // Listamos los clientes de su xml y los vendedores del suyo
        System.out.println();
        System.out.println("Vamos a listar los Clientes del XML dedicado a los clientes.");
        xmlCliente.listar(docCliente);
        System.out.println();
        System.out.println("Vamos a listar los Vendedores del XML dedicado a los vendedores.");
        xmlVendedor.listar(docVendedor);

        // PRUEBA DE MÉTODOS (add, eliminar, buscar y editar)
        // A continuación vamos a agregar los objetos a los arrays o arrayList correspondientes,
        // mediante los métodos de relación entre clases
        // Vamos a empezar directamente agregando la venta y factura, ya que el detalle factura ya
        // lo hemos agregado a Factura en su  constructor

        // -------------------------------------------
        // AÑADIR
        // CLIENTE
        cliente.addFactura(factura); // No comprobaremos, ya que es un Array y no es de este RA 6
        cliente.addVenta(venta);
        // VENDEDOR
        vendedor.addFactura(factura); // No comprobaremos, ya que es un Array y no es de este RA 6
        vendedor.addVenta(venta);

        // --------------------------------
        // BUSCAR (VENTAS) (con este método además de verificar su funcionamiento, comprobamos que se haya añadido
        // CLIENTE
        // Llamamos al método y le pasamos el objeto de Venta en caso de encontrarlo nos lo devuelve y lo mostramos
        System.out.println("Vamos a buscar la venta en los arrayList (listados) de CLiente y Vendedor.");
        System.out.println("Se ha encontrado la venta: " + cliente.buscarVenta(venta)
                + " en el arrayList de Cliente."); // En caso de no añadirse nos devuelve "null"
        // VENDEDOR
        // Llamamos al método y le pasamos el objeto de Venta en caso de encontrarlo nos lo devuelve y lo mostramos
        // debe aparecer el mismo objeto de Venta que en cliente, ya que le hemos pasado el mismo
        System.out.println("Se ha encontrado la venta: " + vendedor.buscarVenta(venta)
                + " en el arrayList de Vendedor."); // En caso de no añadirse nos devuelve "null"


        // --------------------------------
        // EDITAR (creamos un nuevo objeto y se lo pasamos para que sustituya el anterior por este)
        // Creamos el nuevo objeto de Venta, por el que vamos a sustituir el existente
        // Vamos a sustituir el cliente de la venta, por lo que vamos a crear una instancia de cliente también
        /*
         NOTA IMPORTANTE: al modificar el Cliente de la venta, también deveriamos eliminar dicha venta al
         listado del cliente original, y añadirselo al nuevo, pero como es solo para probar los métodos, no lo vamos
         ha hacer y seguiremos usando al listado de ventas del cliente original, aunque dicha venta aparezca con los
         datos del nuevo Cliente.
         */
        Cliente nuevoCliente = new Cliente("00000000-f", "felipe", LocalDate.of(1990, 12, 12));
        Venta nuevaVenta = new Venta(nuevoCliente, vendedor); // Dejamos el mismo vendedor
        // CLIENTE
        // Llamamos al método y le pasamos la "venta" anterior, ya que tiene que buscarla, y la nueva, para que la sustituye
        cliente.modificarVenta(venta,nuevaVenta);
        // Buscamos el nuevo objeto en el ArrayList para ver si ha sustituido al anterior
        // Mostramos la nueva venta dentro del listado para verificar que se haya añadido
        System.out.println();
        System.out.println("Ventas con nuevo Cliente (modificada):");
        System.out.println("Se ha encontrado la venta: " + cliente.buscarVenta(nuevaVenta)
                + " en el arrayList de Cliente.");

        // VENDEDOR
        // Llamamos al método y le pasamos la "venta" anterior, ya que tiene que buscarla, y la nueva, para que la sustituye
        vendedor.modificarVenta(venta,nuevaVenta);
        // Buscamos el nuevo objeto en el ArrayList para ver si ha sustituido al anterior
        // Mostramos la nueva venta dentro del listado para verificar que se haya añadido
        System.out.println("Se ha encontrado la venta: " + vendedor.buscarVenta(nuevaVenta)
                + " en el arrayList de Vendedor.");
        System.out.println();
        // --------------------------------
        System.out.println("Ahora vamos a eliminar.");
        // ELIMINAR (eliminamos los objetos de Venta existentes en los listados, y volvemos a llamar a "buscar" para
        // verificar que no se encuentre)
        // CLIENTE
        // Llamamos al método eliminar, y le pasamos la venta que queremos eliminar (en este caso la nueva, que es la
        // única que se encuentra en este momento)
        cliente.eliminarVenta(nuevaVenta); // Eliminamos la nueva venta
        // Al haber eliminado dicha venta, esto debe devolvernos null, así que lo evaluamos con un condicional
        if(cliente.buscarVenta(nuevaVenta) == null) {
            System.out.println("La venta se ha eliminado correctamente del arrayList de Cliente.");
        } else System.out.println("La venta no se ha eliminado.");

        // VENDEDOR
        // Llamamos al método eliminar, y le pasamos la venta que queremos eliminar (en este caso la nueva, que es la
        // única que se encuentra en este momento)
        vendedor.eliminarVenta(nuevaVenta); // Eliminamos la nueva venta
        // Al haber eliminado dicha venta, esto debe devolvernos null, así que lo evaluamos con un condicional
        if(vendedor.buscarVenta(nuevaVenta) == null) {
            System.out.println("La venta se ha eliminado correctamente del arrayList de Vendedor.");
        } else System.out.println("La venta no se ha eliminado.");
    }
}