package pruebas;

import copy.Copy;
import interfazgrafica.VentanaFechaNacVendedor;
import org.w3c.dom.Document;
import persona.Cliente;
import persona.Persona;
import persona.Vendedor;
import producto.Producto;
import venta.DetalleFactura;
import venta.Factura;
import venta.Venta;
import xml.Xml;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DateTimeException;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Esta clase nos servirá para probar las diferentes clases y métodos de dichas clases de nuestro
 * proyecto, para verificar su correcto funcionamiento, o por el contrario, para detectar los
 * posibles errores existentes.
 * @author manuellaordenmartin
 * @version 1.0
 */
public class Test {
    // Inicializamos dos constantes que harán referencia a los nombres de los archivos sobre los que vamos
    // a trazar con "FileWriter" en los diferentes métodos.
    public final static String NOMBRE_ARCHIVO = "mensajes_metodos.txt", NOMBRE_ARCHIVO_COPIA = "archivoCopia.txt";
    public static void main(String[] args) {



        // Para empezar crearemos al menos una instancia de cada clase, teniendo en cuenta la
        // dependencia entre ellas, es decir, no puedo crear un objeto de "DetalleFactura", sin
        // crear un objeto de "Factura"

        /*
        CRITERIO "f", "g": ya que utilizamos los cuadros de dialogo para pedirle mediante su mensaje, los
        valores de los atributos del Vendedor, y dicha ventana también recoge la entrada en valor de
        tipo String, lo que nos hará "castearlo" en algunos casos.
         */
        /*
        CRITERIO "h": ya que llamamos a la interfáz gráfica creada, que incluye controlador de
        eventos, ya que al pulsar el botón enviar se ejecuta el método de la interfaz "actionPerformed"
        y este recogerá el valor introducido en el campo de la ventana.
         */
        // CREAMOS UN VENDEDOR
        // Los atributos de "nif" y "nombreVendedor", los vamos a pedir y recoger con el método
        // "showInputDialog" de JOptionPane, que nos mostrará una insterfáz gráfica a la que le
        // pondremos el texto que queremos que muestre al usuario, y además recoge el valor en
        // tipo "String"
        String nifVendedor = JOptionPane.showInputDialog("Introduce el NIF del Vendedor: ");
        String nombreVendedor = JOptionPane.showInputDialog("Introduce el nombre del Vendedor: ");
        // A continuación, para la fecha de nacimiento, vamos a llamar a la ventana que hemos creado
        // y luego llamaremos al método "getFechaNacimiento" de la ventana para obtener el valor
        // de la fecha introducida, y así inicializar nuestro objeto LocalDate que hace referencia
        // a la fecha de nacimiento del Vendedor
        // Crea una instancia de la clase VentanaFecha para pedir
        VentanaFechaNacVendedor ventana = new VentanaFechaNacVendedor();
        // Creamos un bucle "while" que llamará al método "getFechaNacimiento" hasta que este
        // no devuelva "null", o lo que es lo mismo, que la fecha introducida tenga el formato
        // correcto y así se llegue a inicializar el atributo "FechaNacimiento" en la clase
        // que hace referencia a la ventana gráfica
        while (ventana.getFechaNacimiento() == null) {
            // Como esto seguirá ejecutandose mientras no introduzcamos la fecha de forma correcta,
            // debemos darle un tiempo de espera hasta la siguiente iteración, de lo contrario
            // se saturará la pila y se quedará pillado o nos dará una excepción
            try {
                // Llamamos al método "sleep" de la clase "Thread" para que pare el programa el
                // tiempo que le pasamos por parámetro, en este caso le hemos pasado 10000, que
                // al estar en milisegundos, son 10 segundos. Le he dado 10 segundos, porque con menos
                // me llegaba a trazar el logger los avisos por consola, ya que tardaba más de lo que
                // dura una iteración, y con más se hace muy lento.
                // Se puede probar introduciendo más milisegundos, o menos y viendo el resultado.
                // Si el tiempo es muy bajo, seguramente se quede pillado o de error como he comentado.
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Inicializamos la fecha de nacimiento del vendedor, al valor recogido por la ventana
        // en su atributo "FechaNacimiento", a traves de su método "get"
        LocalDate fechaNacVendedor = ventana.getFechaNacimiento();
        System.out.println("La fecha es: " + fechaNacVendedor);
        Vendedor vendedor = new Vendedor(nifVendedor, nombreVendedor, fechaNacVendedor); // Instanciamos el Vendedor
        // -------------------------------------------
        /*
         CRITERIO "a": Se ha utilizado la consola para realizar operaciones de entrada y salida de información (15%).
         Ya que vamos a mostrar mensajes por consola para pedirle al usuario los valores de los atributos,
         además de recoger por consola también dichos valores del usuario, a través de un objeto de Scanner.
         */
        // -------------------------------------
        // CREAMOS UN CLIENTE, y para ello vamos a declarar sus atributos
        String nifCliente, nombreCliente, direccion,
                correo = "antonio@gmail.com"; // Le pasamos un correo para verificar que funcione la expresión regular
        // Le damos un valor por defecto a la fecha de nacimiento, para que el IDE no nos la marque en
        // rojo en el constructor de "Cliente", aunque realmente no es necesario, ya que siempre va a
        // tener una fecha válida cuando llegue a dicho constructor
        LocalDate fechaNacCliente = LocalDate.of(1990, 4, 4);
        // Creamos un objeto de "Scanner" para recoger la entrada por consola del usuario
        Scanner sc = new Scanner(System.in);
        // Ahora le pedimos al usuario por consola los valores de los atributos para crear el Cliente
        // También realizaremos las validaciones necesarias para evitar errores, esto lo he copiado
        // directamente de las validaciones que hice del ejercicio 6.14, y lo he modificado  este caso
        System.out.println("Introduce el nombre del Cliente:");
        nombreCliente = sc.nextLine(); // nombre
        System.out.println("Introduce el nif del Cliente:");
        nifCliente = sc.nextLine(); // nif
        System.out.println("Introduce la dirección del Cliente: ");
        direccion = sc.nextLine();
        // fecha nacimiento
        // Lo envolvemos en un bucle "do/while" para que se repita hasta que la fecha sea válida
        // Declaramos una variable boolean para validar la fecha, por defecto estará a "true"
        // para que se siga repitiendo mientras no sea válida la fecha.
        boolean fechaValidaCliente = true;
        // También vamos a declarar una variable "boolean" que nos servirá para validar los nº enteros
        boolean esInt;
        do {
            System.out.println("A continuación, introduce la fecha de Nacimiento del Cliente:");
            int year = 0;
            do {
                System.out.println("Introduce el año de nacimiento:");
                esInt = sc.hasNextInt();
                if(esInt) {
                    year = sc.nextInt();
                } else System.out.println("El año debe ser un valor entero positivo. Intentalo de nuevo.");
                sc.nextLine(); // Limpiamos el buffer
                // Si es "int", esta variable será "true", por lo que la negamos para salir del bucle en ese
                // caso
            } while (!esInt);
            int mes = 0;
            do {
                System.out.println("Introduce el mes de nacimiento:");
                esInt = sc.hasNextInt();
                if(esInt) {
                    mes = sc.nextInt();
                } else System.out.println("El mes debe ser un valor entero positivo. Intentalo de nuevo.");
                sc.nextLine(); // Limpiamos el buffer
                // Si es "int", esta variable será "true", por lo que la negamos para salir del bucle en ese
                // caso
            } while (!esInt);
            int dia = 0;
            do {
                System.out.println("Introduce el día de nacimiento (entre 1 y 30):");
                esInt = sc.hasNextInt();
                if(esInt) {
                    dia = sc.nextInt();
                } else System.out.println("El dia debe ser un valor entero positivo. Intentalo de nuevo.");
                sc.nextLine(); // Limpiamos el buffer
                // Si es "int", esta variable será "true", por lo que la negamos para salir del bucle en ese
                // caso
            } while (!esInt);
            // Controlamos la posible excepción con un try catch, ya que puede que los valores que introduzca
            // el usuario no correspondan a una fecha correcta
            try {
                fechaNacCliente = LocalDate.of(year, mes, dia);
                // En caso de que no salte la excepción, vamos a poner la variable "fechaValida" a "false"
                // para que no vuelva a repetirse el bucle
                // Es decir, cuando "fechaValida" valga "false" en este caso significará que la fecha es válida
                // y saldrá del bucle, en cambio si la fecha no es válida, valdrá "true" y seguirá repitiendose
                // Explico esto porque puede interpretarse al revés, ya que lo estoy haciendo al contrario.
                fechaValidaCliente = false;
            } catch (DateTimeException e) {
                // En caso de haberse dado la excepción mostramos un mensaje
                System.out.println("La fecha introducida no es correcta, prueba de nuevo.");
            }
            // La condición de salida será que la fecha sea válida, ya que es lo que puede dar problemas
            // de todos los atributos, en caso de no ser válida, volveremos a repetir el proceso de la fecha
        } while(fechaValidaCliente);
        // Creamos la instancia de Cliente con los atributos recogidos por consola
        // (Criterio "a")
        Cliente cliente = new Cliente(nifCliente, nombreCliente, fechaNacCliente, direccion, correo);



        // ------------------------------

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


        // Formateamos el archivo de los mensajes
        Test.formatearArchivo();


        // PRUEBA DE MÉTODOS (add, eliminar, buscar y editar)
        // A continuación vamos a agregar los objetos a los arrays o arrayList correspondientes,
        // mediante los métodos de relación entre clases
        // Vamos a empezar directamente agregando la venta y factura, ya que el detalle factura ya
        // lo hemos agregado a Factura en su  constructor

        // -------------------------------------------

        // AÑADIR
        // CLIENTE
        System.out.println("Vamos a añadir la factura al cliente.");
        // Aprovehcamos que nos devuleve un "boolean" para ver si se ha añadido correctamente.
        if(cliente.addFactura(factura)) {
            System.out.println("La Factura se ha añadido correctamente al Cliente.");
        } else System.out.println("La Factura no se ha añadido correctamente al Cliente.");
        System.out.println("Vamos a añadir la Venta al cliente.");
        // Evaluamos como antes que se haya añadido correctamente
        if(cliente.addVenta(venta)) {
            System.out.println("La Venta se ha añadido correctamente al Cliente.");
        } else System.out.println("La Venta no se ha añadido correctamente al Cliente.");

        // VENDEDOR
        System.out.println("Vamos a añadir la Factura al Vendedor");
        if(vendedor.addFactura(factura)) {
            System.out.println("La Factura se ha añadido correctamente al Vendedor");
        } else System.out.println("La Factura no se ha añadido correctamente al Vendedor");
        System.out.println("Vamos a añadir la Venta al Vendedor");
        if(vendedor.addVenta(venta)) {
            System.out.println("La Venta se ha añadido correctamente al Vendedor");
        } else System.out.println("La Venta no se ha añadido correctamente al Vendedor");


        // --------------------------------
        // BUSCAR (VENTAS) (con este método además de verificar su funcionamiento, comprobamos que se haya añadido
        // CLIENTE
        // Llamamos al método y le pasamos el objeto de Venta en caso de encontrarlo nos lo devuelve y lo mostramos
        System.out.println("\nVamos a buscar la venta en los arrayList (listados) de CLiente y Vendedor.");
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

        System.out.println("Ahora vamos a eliminar la venta.");
        // ELIMINAR (eliminamos los objetos de Venta existentes en los listados, y volvemos a llamar a "buscar" para
        // verificar que no se encuentre)

        // CLIENTE
        // Llamamos al método eliminar, y le pasamos la venta que queremos eliminar (en este caso la nueva, que es la
        // única que se encuentra en este momento)
        // Aprovechamos que nos devuelve el método un "booleano" para verificar que se haya eliminado o no.
        if(cliente.eliminarVenta(nuevaVenta)) {
            System.out.println("La venta se ha eliminado correctamente del arrayList de Cliente.");
        } else System.out.println("La venta no se ha eliminado.");
        // Al haber eliminado dicha venta, al buscarla debe devolvernos "null"
        if(cliente.buscarVenta(nuevaVenta) == null) {
            // Si el método nos devulve "null" significa que no se ha encontrado, y así debe ser, ya que
            // la hemos eliminado en las líneas de justo arriba
            System.out.println("Venta no encontrada. Efectivamente se ha eliminado antes.");
        } else {
            // En caso de no devolver "null" mostramos el mensaje que se si se ha encontrado
            System.out.println("Venta encontrada. No se ha llegado a eliminar antes.");
        }

        // VENDEDOR
        // Llamamos al método eliminar, y le pasamos la venta que queremos eliminar (en este caso la nueva, que es la
        // única que se encuentra en este momento)
        // Aprovechamos que nos devuelve el método un "booleano" para verificar que se haya eliminado o no.
        if(vendedor.eliminarVenta(nuevaVenta)) {
            System.out.println("La venta se ha eliminado correctamente del arrayList de Vendedor.");
        } else System.out.println("La venta no se ha eliminado.");
        // Al haber eliminado dicha venta, al buscarla debe devolvernos "null"
        if(vendedor.buscarVenta(nuevaVenta) == null) {
            // Si el método nos devulve "null" significa que no se ha encontrado, y así debe ser, ya que
            // la hemos eliminado en las líneas de justo arriba
            System.out.println("Venta no encontrada. Efectivamente se ha eliminado antes.");
        } else {
            // En caso de no devolver "null" mostramos el mensaje que se si se ha encontrado
            System.out.println("Venta encontrada. No se ha llegado a eliminar antes.");
        }



        // RA5:
        // Vamos a crear una copia del archivo que hemos creado en los métodos de la clase "Persona"
        // que hemos trazado loss mensajes con el objeto de "FileWriter"
        // Para ello vamos a llamar al método "copiar" de la clase que hemos creado "Copy", y le
        // pasamos el nombre del archivo en el que hemos ido trazando los mensajes
        // "mensajes_metodos.txt" y el nombre del archivo que será la copia "archivoCopia.txt"
        Copy.copiar(NOMBRE_ARCHIVO, NOMBRE_ARCHIVO_COPIA);
    }

    /**
     * Este método tendrá un objeto de "FileWrtiter" con el objetivo de sobreescribir el archivo
     * ya que de la ejecución anterior, tendrá mensajes guardados.
     */
    public static void formatearArchivo() {
        // Creamos la instancia de FileWriter, sin pasarle como segundo parámetro "true", ya que en este caso
        // queremos que sobreescriba el archivo cuando llamemos a este método, con el fin de eliminar los
        // mensajes de ejecuciones pasadas, ya que sino cada vez que ejecutemos el código se añadiran los
        // mismos mensajes.
        try {
            FileWriter fileWriter = new FileWriter(NOMBRE_ARCHIVO);
            // Llamamos al método "write", y le pasamos unas comillas dobles vacias, para que sobreescriba
            // el archivo con eso y se vacie
            fileWriter.write("");
            // Cerramos el objeto de flieWriter
            fileWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}