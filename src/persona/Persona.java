package persona;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import logger.MyLogger;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pruebas.Test;
import venta.Factura;
import venta.Venta;

/**
 * Esta clase abstracta hace referencia a una persona dentro del sistema de ventas.
 * Tiene una seria de atributos que representan información sobre la persona, además
 * también lleva un registro de las ventas y facturas asociadas a la propia persona.
 * Criterios:
 * Cambios RA5: se ha mantenido el objeto de "logger" para trazar en formato "xml".
 * También he añadido un objeto de "fileWriter" para trazar en un formato normal los mismos
 * mensajes, y así usar otra librería interna de trazado. Se ha reconocido la posibilidad de
 * usar para trazar una instancia de la clase "BufferWriter", en vez de FileWriter, pero
 * como no lo he visto necesario, he escogido esta última, además de usar la librería del
 * logger, como en la entrega pasada.
 * @author Mlm96
 * @version 1.2
 */
public abstract class Persona {
    // Declaramos los atributos
    protected String nif, nombre, direccion, correo;
    protected int telefono, edad;
    protected LocalDate fechaNac;
    protected ArrayList<Venta> listadoVentas;
    protected ArrayList<Factura> listadoFacturas;
    // En esta entrega dejare el "logger" para trazar tanto por consola, como en un fichero con
    // formato "xml" y cumplir con varios criterios
    /*
     CRITERIO "a": Se ha utilizado la consola para realizar operaciones de entrada y salida de
     información (15%).
     Ya que "logger" trazamos también por consola.
     */
    /*
    CRITERIO "b": Se han aplicado formatos en la visualización de la información (10%).
    El "logger" traza en el fichero en formato "xml". Se puede ver en el archivo "java.log"
     */
    /*
    CRITERIO "c": Se han reconocido las posibilidades de entrada/salida del lenguaje y las
    librerías asociadas (10%).
    Ya que trazamos con "logger" aquí, y con "FileWriter"
     */
    /*
    CRITERIO "d": Se han utilizado ficheros para almacenar y recuperar información (10%).
    Ya que trazamos en el fichero "java.log"
     */
    protected static Logger logger;
    /*
    CRITERIO "c": Se han reconocido las posibilidades de entrada/salida del lenguaje y las librerías asociadas (10%).
     */
    /*
    CRITERIO "d": Se han utilizado ficheros para almacenar y recuperar información (10%).
    Ya que trazamos en el fichero "mensajes_metodos.txt"
     */
    // Le voy a añadir otro atributo que nos servirá para trazar mensajes en otro fichero, será
    // una instancia de la clase "FileWriter", pense en hacerlo de "BufferWriter", como en el caso
    // de los métodos de la clase "Copy" del proyecto, pero en este caso no lo veía necesario.
    //protected static FileWriter fileWriter;
    // Bloque estático
    static {
        // Inicializamos el logger estático para que nos sirva para la clase en general
        logger = MyLogger.getLogger("A");
        // Inicializamos el objeto de "FileWriter" y lo envolvemos en un "try/catch" ya que el
        // IDE nos obliga al ser una posible exepción de tipo comprobada.
        /*try {
            // Le pasamos a la instancia el nombre del fichero en el que queremos que escriba.
            fileWriter = new FileWriter("mensajes_metodos.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }
    // Bloque inicialización
    {
        listadoVentas = new ArrayList<>();
        listadoFacturas = new ArrayList<>();
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

    /**
     * Método para validar la nulidad de la Factura
     * @param factura
     * @return
     */
    public static boolean facturaIsNull(Factura factura) {
        // Validamos si la factura es "nula", en cuyo caso, trazamos el mensaje, y devolvemos "true"
        if(factura == null) {
            try {
                // Creamos un objeto de "fileWriter" para trazar el mensaje, y le pasamos como segundo parámetro
                // "true" para que no sobreescriba el archivo, sino que lo añada al final
                FileWriter fileWriter = new FileWriter(Test.NOMBRE_ARCHIVO, true);
                // Trazamos con el objeto de "fileWriter" de la clase en el archivo "mensaje_metodos.txt"
                // TRAZAMOS CON "FILEWRITER"
                fileWriter.write("Objeto de Factura Nulo");
                // Cerramos "fileWriter"
                fileWriter.close();
                // TRAZAMOS CON "LOGGER"
                // Trazamos por consola y en el archivo "java.log" con el "logger.info"
                logger.info("Objeto de Factura Nulo");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            // Devolvemos "true" para indicar que la factura es "nula"
            return true;
        }

        // Devolvemos "false" para indicar que el objeto no es "nulo"
        return false;
    }
    // FACTURA
    // Añadir Factura
    /**
     * Método para añadir una factura al listado de facturas de la persona.
     * @param factura factura que va a ser añadida.
     * @return true si la factura se ha añadido correctamente, false en caso contrario.
     */
    public boolean addFactura(Factura factura) {
        try {
            // Evaluamos la nulidad
            // Llamamos al método "facturaIsNull" para comprobar si la factura es nula
            if(Persona.facturaIsNull(factura)) {
                // Devolvemos "false" en caso de que sea "nula" para indicar al programamdor que no se ha
                // añadido la factura al "listadoFactura"
                return false;
            }

            // Creamos un objeto de "fileWriter" para trazar el mensaje, y le pasamos como segundo parámetro
            // "true" para que no sobreescriba el archivo, sino que lo añada al final
            FileWriter fileWriter = new FileWriter(Test.NOMBRE_ARCHIVO, true);

            // En caso de no ser nulo, añadimos la factura al arrayList con "add"
            listadoFacturas.add(factura);
            // TRAZAMOS CON "FILEWRITER"
            fileWriter.write("Factura añadida.");
            // Cerramos "fileWriter"
            fileWriter.close();
            // TRAZAMOS CON "LOGGER"
            // Si se ha agregado devolvemos un true y así salimos del bucle también
            logger.info("Factura añadida");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // Devolvemos "true" para indicar que la Factura se ha añadido al listado
        return true;
    }
    // Buscar Factura
    /**
     * Método para buscar una factura en el listado de facturas de la persona.
     * @param factura factura que se va a buscar en el listado.
     * @return El índice de la factura en el listado si se encuentra, -1 si no se encuentra,
     * -2 si el parámetro no es válido.
     */
    public Factura buscarFactura(Factura factura) {
        // Validamos si la factura es nula
        if(Persona.facturaIsNull(factura)) {
            // Devolvemos "null" en caso de que sea "nula" para indicar al programamdor que no se ha
            // añadido la factura al "listadoFactura"
            return null;
        }

        // Creamos una instancia de FileWriter para trazar el mensaje en el archivo
        try {
            // Creamos un objeto de "fileWriter" para trazar el mensaje, y le pasamos como segundo parámetro
            // "true" para que no sobreescriba el archivo, sino que lo añada al final
            FileWriter fileWriter = new FileWriter(Test.NOMBRE_ARCHIVO, true);
            // Creamos un iterador para recorrer el arrayList
            // CRITERIO: Se han utilizado iteradores para recorrer los elementos de las listas (10%).
            Iterator<Factura> it = listadoFacturas.iterator();
            // Utilizamos el objeto de iterator que hemos creado, para hacer uso de su método
            // "hasnext" y comprobar si existe un elemento siguiente
            // Como dicho método nos devulve un valor boolean, lo usaremos como condición del bucle
            while (it.hasNext()) {
                // En caso de existir, nos devuelve true, y acontinuación haciendo uso del método "next()"
                // accedemos a dicho elemento y aprovechando que tenemos implementado en "Venta" la interfaz
                // comparable, evaluamos si el valor devuelto por dicho mé
                if (it.next().compareTo(factura) == 0) {
                    fileWriter.write("Factura encontrada.");
                    // Cerramos "fileWriter"
                    fileWriter.close();
                    logger.info("Factura encontrada");
                    return factura;
                }
            }
            // TRAZAMOS
            fileWriter.write("Factura no encontrada.");
            // Cerramos "fileWriter"
            fileWriter.close();
            logger.info("Factura no encontrada");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // En caso de recorrer el arrayList y no encontrar coincidencia
        // devolvemos "null"
        return null;
    }
    // Eliminar Factura
    /**
     * Método para eliminar una factura del listado de facturas de la persona.
     * @param factura factura que se va a eliminar.
     * @return true si la factura se ha eliminado correctamente, false en caso contrario.
     */
    public boolean eliminarFactura(Factura factura) {
        // Validamos si la factura es nula
        if(Persona.facturaIsNull(factura)) {
            // Devolvemos "false" en caso de que sea "nula" para indicar al programamdor que no se ha
            // añadido la factura al "listadoFactura"
            return false;
        }
        // Si no es nulo, llamamos al método "remove", que verifica si dicho objeto existe en el arrayLIst,
        // y en caso afirmativo lo elimina
        // Como el método nos devuelve un booleano, lo aprovechamos y lo devolvemos
        return listadoFacturas.remove(factura);
    }
    // Editar factura
    /**
     * Método para modificar una venta en el listado de ventas de la persona.
     * @param facturaBuscar venta que se va a buscar y modificar.
     * @param facturaNueva la venta nueva que va reemplazará a la venta antigua.
     * @return true si la venta se ha modificado correctamente, false en caso contrario.
     */
    public boolean modificarFactura(Factura facturaBuscar, Factura facturaNueva) {
        // Validamos si la factura es nulo uno de los dos objetos
        if(Persona.facturaIsNull(facturaBuscar) || Persona.facturaIsNull(facturaNueva)) {
            // Devolvemos "false" en caso de que una de las dos
            return false;
        }

        // Creamos el objeto FileWriter
        try {
            FileWriter fileWriter = new FileWriter(Test.NOMBRE_ARCHIVO);
            // Llamamos al método "indexOf" para averiguar la posición del elemento que buscamos en el ArrayList
            int posicion = listadoFacturas.indexOf(facturaBuscar);
            // Aprovechando que "indexOf", nos devuelve un -1 en caso de no encontrar el objeto en el ArrayList
            // por lo que evaluamos que la posición es menor a 0
            if(posicion < 0) {
                // TRAZAMOS
                fileWriter.write("Factura no encontrada.");
                // Cerramos "fileWriter"
                fileWriter.close();
                logger.info("Factura no encontrada.");
                // Si no se encuentra devolvemos "false"
                return false;
            }
            // En primer lugar, le asignamos al nuevo objeto, el id del viejo
            facturaNueva.setId(listadoFacturas.get(posicion).getId());
            // Asignamos el nuevo Objeto, en la posición del antiguo
            listadoFacturas.set(posicion, facturaNueva);
            fileWriter.write("Factura no modificada");
            // Cerramos "fileWriter"
            fileWriter.close();
            logger.info("Factura no modificada.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Si se ha modificado el objeto correctamente, devolvemos un true
        return true;
    }


    // VENTA
    // Verificar nulidad
    /**
     * Método para validar la nulidad de la Venta
     * @param venta
     * @return
     */
    public static boolean ventaIsNull(Venta venta) {
        // Validamos si la venta es "nula", en cuyo caso, trazamos el mensaje, y devolvemos "true"
        if(venta == null) {
            try {
                // Creamos un objeto de "fileWriter" para trazar el mensaje, y le pasamos como segundo parámetro
                // "true" para que no sobreescriba el archivo, sino que lo añada al final
                FileWriter fileWriter = new FileWriter(Test.NOMBRE_ARCHIVO, true);
                // Trazamos con el objeto de "fileWriter" de la clase en el archivo "mensaje_metodos.txt"
                // TRAZAMOS CON "FILEWRITER"
                fileWriter.write("Objeto de Venta Nulo");
                // Cerramos "fileWriter"
                fileWriter.close();
                // TRAZAMOS CON "LOGGER"
                // Trazamos por consola y en el archivo "java.log" con el "logger.info"
                logger.info("Objeto de Venta Nulo");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            // Devolvemos "true" para indicar que la venta es "nula"
            return true;
        }
        // Devolvemos "false" para indicar que el objeto no es "nulo"
        return false;
    }
    // Añadir Venta
    /**
     * Método para añadir una venta al listado de ventas de la persona.
     * @param venta venta que va a ser añadida.
     * @return true si la factura se ha añadido correctamente, false en caso contrario.
     */
    public boolean addVenta(Venta venta) {
        try {
            // Evaluamos la nulidad
            // Llamamos al método "facturaIsNull" para comprobar si la venta es nula
            if(Persona.ventaIsNull(venta)) {
                // Devolvemos "false" en caso de que sea "nula" para indicar al programamdor que no se ha
                // añadido la venta al "listadoVentas"
                return false;
            }
            // Creamos un objeto de "fileWriter" para trazar el mensaje, y le pasamos como segundo parámetro
            // "true" para que no sobreescriba el archivo, sino que lo añada al final
            FileWriter fileWriter = new FileWriter(Test.NOMBRE_ARCHIVO, true);
            // En caso de no ser nulo, añadimos la venta al arrayList con "add"
            listadoVentas.add(venta);
            // TRAZAMOS CON "FILEWRITER"
            fileWriter.write("Venta añadida.");
            // Cerramos "fileWriter"
            fileWriter.close();
            // TRAZAMOS CON "LOGGER"
            // Si se ha agregado devolvemos un true y así salimos del bucle también
            logger.info("Venta añadida");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // Devolvemos "true" para indicar que la Venta se ha añadido al listado
        return true;
    }

    // Buscar Venta
    /**
     * Método para buscar una venta en el listado de ventas de la persona.
     * @param venta venta que se va a buscar en el listado.
     * @return El índice de la venta en el listado si se encuentra, -1 si no se encuentra,
     * -2 si el parámetro no es válido.
     */
    public Venta buscarVenta(Venta venta) {
        // Validamos si la venta es nula
        if(Persona.ventaIsNull(venta)) {
            // Devolvemos "null" en caso de que sea "nula" para indicar al programamdor que no se ha
            // añadido la venta al "listadoVenta"
            return null;
        }

        // Creamos una instancia de FileWriter para trazar el mensaje en el archivo
        try {
            // Creamos un objeto de "fileWriter" para trazar el mensaje, y le pasamos como segundo parámetro
            // "true" para que no sobreescriba el archivo, sino que lo añada al final
            FileWriter fileWriter = new FileWriter(Test.NOMBRE_ARCHIVO, true);
            // Creamos un iterador para recorrer el arrayList
            // CRITERIO: Se han utilizado iteradores para recorrer los elementos de las listas (10%).
            Iterator<Venta> it = listadoVentas.iterator();
            // Utilizamos el objeto de iterator que hemos creado, para hacer uso de su método
            // "hasnext" y comprobar si existe un elemento siguiente
            // Como dicho método nos devulve un valor boolean, lo usaremos como condición del bucle
            while (it.hasNext()) {
                // En caso de existir, nos devuelve true, y acontinuación haciendo uso del método "next()"
                // accedemos a dicho elemento y aprovechando que tenemos implementado en "Venta" la interfaz
                // comparable, evaluamos si el valor devuelto por dicho método es 0
                if (it.next().compareTo(venta) == 0) {
                    fileWriter.write("Venta encontrada.");
                    // Cerramos "fileWriter"
                    fileWriter.close();
                    logger.info("Venta encontrada");
                    return venta;
                }
            }
            // TRAZAMOS
            fileWriter.write("Venta no encontrada.");
            // Cerramos "fileWriter"
            fileWriter.close();
            logger.info("Venta no encontrada");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // En caso de recorrer el arrayList y no encontrar coincidencia
        // devolvemos "null"
        return null;

    }
    // Eliminar Venta
    /**
     * Método para eliminar una venta del listado de ventas de la persona.
     * @param venta venta que se va a eliminar.
     * @return true si la venta se ha eliminado correctamente, false en caso contrario.
     */
    public boolean eliminarVenta(Venta venta) {
        // Validamos si la venta es nula
        if(Persona.ventaIsNull(venta)) {
            // Devolvemos "false" en caso de que sea "nula" para indicar al programamdor que no se ha
            // añadido la venta al "listadoVenta"
            return false;
        }
        // Si no es nulo, llamamos al método "remove", que verifica si dicho objeto existe en el arrayLIst,
        // y en caso afirmativo lo elimina
        // Como el método nos devuelve un booleano, lo aprovechamos y lo devolvemos
        return listadoVentas.remove(venta);
    }

    // Editar venta
    /**
     * Método para modificar una venta en el listado de ventas de la persona.
     * @param ventaBuscar venta que se va a buscar y modificar.
     * @param ventaNueva la venta nueva que va reemplazará a la venta antigua.
     * @return true si la venta se ha modificado correctamente, false en caso contrario.
     */
    public boolean modificarVenta(Venta ventaBuscar, Venta ventaNueva) {
        // Validamos si la factura es nulo uno de los dos objetos
        if(Persona.ventaIsNull(ventaBuscar) || Persona.ventaIsNull(ventaNueva)) {
            // Devolvemos "false" en caso de que una de las dos
            return false;
        }

        // Creamos el objeto FileWriter
        try {
            FileWriter fileWriter = new FileWriter(Test.NOMBRE_ARCHIVO, true);
            // Llamamos al método "indexOf" para averiguar la posición del elemento que buscamos en el ArrayList
            int posicion = listadoVentas.indexOf(ventaBuscar);
            // Aprovechando que "indexOf", nos devuelve un -1 en caso de no encontrar el objeto en el ArrayList
            // por lo que evaluamos que la posición es menor a 0
            if(posicion < 0) {
                // TRAZAMOS
                fileWriter.write("Venta no encontrada.");
                // Cerramos "fileWriter"
                fileWriter.close();
                logger.info("Venta no encontrada.");
                // Si no se encuentra devolvemos "false"
                return false;
            }
            // En primer lugar, le asignamos al nuevo objeto, el id del viejo
            ventaNueva.setId(listadoVentas.get(posicion).getId());
            // Asignamos el nuevo Objeto, en la posición del antiguo
            listadoVentas.set(posicion, ventaNueva);
            fileWriter.write("Venta modificada");
            // Cerramos "fileWriter"
            fileWriter.close();
            logger.info("Venta modificada.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Si se ha modificado el objeto correctamente, devolvemos un true
        return true;
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
    // Listado Ventas
    public ArrayList<Venta> getListadoVentas() {
        return listadoVentas;
    }
    /**
     * Este método setter nos sirve para cambiar el listado de las ventas de la persona,
     * además se asegura que el listado pasado sea del mismo tamaño que el anterior para
     * evitar errores.
     * @param listadoVentas el nuevo listado de ventas.
     * @return true si el listado de ventas se ha establecido correctamente, false en caso contrario.
     */
    public void setListadoVentas(ArrayList<Venta> listadoVentas) {
        this.listadoVentas = listadoVentas;
    }
    // Listado Facturas

    public ArrayList<Factura> getListadoFacturas() {
        return listadoFacturas;
    }

    public void setListadoFacturas(ArrayList<Factura> listadoFacturas) {
        this.listadoFacturas = listadoFacturas;
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