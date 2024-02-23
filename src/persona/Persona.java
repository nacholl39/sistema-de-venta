package persona;

import java.time.LocalDate;

import interfaz.Listable;
import org.eclipse.jdt.annotation.NonNull;

import logger.MyLogger;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import venta.Factura;
import venta.Venta;

/**
 * Esta clase abstracta hace referencia a una persona dentro del sistema de ventas.
 * Tiene una seria de atributos que representan información sobre la persona, además
 * también lleva un registro de las ventas y facturas asociadas a la propia persona.
 * @author Mlm96
 * @version 1.0
 */
public abstract class Persona implements Listable {
    // Declaramos los atributos
    protected String nif, nombre, direccion, correo;
    protected int telefono, edad;
    protected LocalDate fechaNac;
    // CRITERIO: Se han reconocido las librerías de clases relacionadas con tipos de datos avanzados (5%).
    // CRITERIO: Se han utilizado listas para almacenar y procesar información (15%).
    // He cambiado el array de ventas, por un arrayList, y he modificado
    // los métodos relacionados con dicho ArrayList (buscar, add, eliminar y modificar)
    // por lo que en dichos métodos relacionados con el listado de ventas
    // también podrás ver el uso de los métodos de los arrayList
    // para cubrir los criterios
    protected ArrayList<Venta> listadoVentas;
    // En el caso del listado de Facturas, lo he dejado como un array
    // normal, para poder hacer uso de los métodos de la clase Arrays
    // y cumplir con el criterio "a"
    // Puede verse en el método "buscarFactura" en el hacemos uso de "sort"
    // para ordenar el array antes de recorrerlo, aprovechando que la
    // clase "Factura" tiene implementada la interfaz "comparable"
    protected Factura[] listadoFacturas;
    protected static Logger logger;
    // Bloque estático
    static {
        // Inicializamos el logger estático para que nos sirva para la clase en general
        logger = MyLogger.getLogger("A");
    }
    // Bloque inicialización
    {
        listadoVentas = new ArrayList<>();
        listadoFacturas = new Factura[10];
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
    public Persona(@NonNull String nif, @NonNull String nombre, @NonNull LocalDate fechaNac) {
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
    public Persona(@NonNull String nif, @NonNull String nombre, @NonNull LocalDate fechaNac,
                   String direccion, String correo) {
        this(nif,nombre, fechaNac);
        this.direccion = direccion;
        // Validamos el formato del correo llamando al método set que tiene las comprobaciones
        this.correo = setCorreo(correo);
    }
    // Tercer constructor, llamamos al primer constructor y además
    // pedimos la telefono
    public Persona(@NonNull String nif, @NonNull String nombre, int telefono, LocalDate fechaNac) {
        this(nif,nombre, fechaNac);
        this.telefono = telefono;
    }
    // Cuarto constructor, llamamos al segundo y además pedimos el teléfono
    public Persona(@NonNull String nif, @NonNull String nombre, int telefono, LocalDate fechaNac,
                   String direccion, String correo) {
        this(nif, nombre, fechaNac, direccion, correo);
        this.telefono = telefono;
    }

    // Métodos y funcionalidades
    // Método genérico, que implementamos de la interfaz Listable
    // CRITERIO: Se han utilizado iteradores para recorrer los elementos de las listas (10%)
    // CRITERIO: Se han creado clases y métodos genéricos (20%).
    @Override
    public void listarElemento(ArrayList listadoVentas) {
        // Creamos un iterador
        Iterator<Venta> it = listadoVentas.iterator();
        // Recorremos con el objeto de iterator, verificando que haya un siguiente elemento
        while (it.hasNext()) {
            // Y lo mostramos
            System.out.println(it.next());
        }
    }

    // Mostrar informacion (método abstracto)
    /**
     * Método abstracto para mostrar la información relacionada con la persona.
     * Al ser abstracta será sobreescrita por las clases hijas.
     */
    public abstract void mostrarInformacion();
    // FACTURA
    // Añadir Factura
    /**
     * Método para añadir una factura al listado de facturas de la persona.
     * @param factura factura que va a ser añadida.
     * @return true si la factura se ha añadido correctamente, false en caso contrario.
     */
    public boolean addFactura(Factura factura) {
        // Inicializamos un constador para recorrer el listado de Facturas
        int contador=0;
        // Evaluamos que sea una instancia de "Factura" y que no sea un valor nulo
        if(factura!=null) {
            // Recorremos el listado de facturas, y añadimos la factura donde el valor sea Null
            // es decir, donde este vacío. Como no sabemos cuando será, usamos do,while
            do {
                if(listadoFacturas[contador] == null) {
                    // Añadimos la factura al array
                    listadoFacturas[contador] = factura;
                    // Trazamos que se ha añadido
                    logger.info("Factura añadida correctamente");
                    // Si se ha agregado devolvemos un true y así salimos del bucle también
                    return true;
                }
                // Incrementamos el contador
                contador++;
                // Evaluamos en la condición que contador no supere al tamaño del array
            } while((contador<listadoFacturas.length));
        }
        // Si no se ha añadido correctamente devolvemos "false" y trazamos el mensaje
        logger.info("Factura no añadida correctamente");
        return false;
    }
    // Buscar Factura
    /**
     * Método para buscar una factura en el listado de facturas de la persona.
     * @param factura factura que se va a buscar en el listado.
     * @return El índice de la factura en el listado si se encuentra, -1 si no se encuentra,
     * -2 si el parámetro no es válido.
     */
    public int buscarFactura(Factura factura) {
        // CRITERIO: Se han escrito programas que utilicen arrays (15%)
        // Ordenamos el array con el método "sort"
        Arrays.sort(listadoFacturas);
        // Inicializamos un contador para recorrer el array
        int contador=0;
        // Evaluamos que sea una instancia de "Factura" y que no sea un valor nulo
        if(factura!=null) {
            // Recorremos el array de Facturas, como no sabemos cuando encontraremos la factura
            // emplearemos un bucle do,while
            do {
                // Usamos la interfaz comparable de "Factura" para evaluar si son iguales
                if(listadoFacturas[contador].compareTo(factura) == 0) {
                    // Salimos del bucle si se ha cumplido la condición
                    // Con eso conseguimos que contador se quede con el valor de la posición
                    logger.info("Factura encontrada en posición: " + contador);
                    return contador;
                }
                // Incrementamos el contador
                contador++;
                // La condición es que el contador siga siendo menor a la longitud, para evitar un fuera
                // de índice; además de que "facturaBuscada" debe seguir a null, ya que si se ha encontrado
                // no debemos seguir buscando
            } while((contador<listadoFacturas.length));
            // Retornamos el valor -1 si no se ha encontrado ninguna
            logger.info("Factura no encontrada");
            return -1;
        } else {
            // Devolvemos un -2 en caso de que el parámetro no sea válido (nulo o de otro tipo)
            logger.info("Factura no válida");
            return -2;
        }
    }
    // Eliminar Factura
    /**
     * Método para eliminar una factura del listado de facturas de la persona.
     * @param factura factura que se va a eliminar.
     * @return true si la factura se ha eliminado correctamente, false en caso contrario.
     */
    public boolean eliminarFactura(Factura factura) {
        // Llamamos al método "buscarFactura" para encontrar la posición del elemento a
        // eliminar y le asignamos el valor a una variable
        int posicion = buscarFactura(factura);
        // Inicializamos un nuevo array de facturas
        Factura[] nuevoListadoFacturas = new Factura[10];
        // Evaluamos que la posición devuelta sea mayor o igual a 0, ya que en caso contrario
        // significa que el objeto factura es nulo, de otro tipo o no se ha encontrado
        if(posicion>=0) {
            // Inicializamos un contador para recorrer el array nuevo
            int contador=0;
            // Recorremos el array de Facturas y la guardamos mientras no coincida, en el nuevo array
            // asi obtenemos un nuevo array usamos un bucle for porque debemos recorrerlo entero
            for(int i=0; i<listadoFacturas.length; i++) {
                // Si no coincide la variable iteradora con la posición que deseamos eliminar,
                // la almacenamos e incrementamos el contador de posiciones del nuevo
                // array, en caso contrario no, así conseguimos que no se quede el hueco
                // de la posición que hemos eliminado en el nuevo listado
                if(i!=posicion) {
                    nuevoListadoFacturas[contador] = listadoFacturas[i];
                    // Incrementamos el contador
                    contador++;
                }
            }
            // Reasignamos al listado de facturas el nuevo listado
            setListadoFacturas(nuevoListadoFacturas);
            // Devolvemos un true para verificar que se ha hecho correctamente
            logger.info("Factura eliminada");
            return true;
        }
        // Devolvemos false, en caso de que no se haya eliminado ningun elemento y siga igual
        logger.info("Factura no eliminada");
        return false;
    }
    // Editar factura
    /**
     * Método para modificar una factura en el listado de facturas de la persona.
     * @param facturaBuscar factura que se va a buscar y modificar.
     * @param facturaNueva la factura nueva que va reemplazará a la factura antigua.
     * @return true si la factura se ha modificado correctamente, false en caso contrario.
     */
    public boolean modificarFactura(Factura facturaBuscar, Factura facturaNueva) {
        // Llamamos al método buscar factura para localizar la factura a modificar
        // y guardamos la posición en una variable
        int posicion = buscarFactura(facturaBuscar);
        // Evaluamos que la posición devuelta sea mayor o igual a 0, ya que en caso contrario
        // significa que el objeto factura es nulo, de otro tipo o no se ha encontrado
        if(posicion>=0) {
            // Se lo asignamos a la factura nueva el identificador de la antigua
            facturaNueva.setId(listadoFacturas[posicion].getId());
            // Le asignamos al objeto de factura en esa posición, los nuevos valores, que
            // son los del objeto "facturaNueva"
            listadoFacturas[posicion] = facturaNueva;
            // Devolvemos un true para verificar que se ha hecho correctamente
            logger.info("Factura editada");
            return true;
        }
        // Devolvemos false, en caso de que no se haya modficado ningun elemento y siga igual
        logger.info("Factura no editada");
        return false;
    }


    // VENTA
    // Añadir Venta
    /**
     * Método para añadir una venta al listado de ventas de la persona.
     * @param venta venta que va a ser añadida.
     * @return true si la factura se ha añadido correctamente, false en caso contrario.
     */
    public boolean addVenta(Venta venta) {
        // Evaluamos que sea una instancia de "Venta" y que no sea un valor nulo
        if(venta!=null) {
            // En caso de no ser nulo, añadimos la venta al arrayList con "add"
            listadoVentas.add(venta);
            // Si se ha agregado devolvemos un true y así salimos del bucle también
            logger.info("Venta añadida");
            return true;
        } else {
            // Si no se ha añadido correctamente devolvemos "false"
            logger.info("Venta no añadida");
            return false;
        }
    }
    // Buscar Venta
    /**
     * Método para buscar una venta en el listado de ventas de la persona.
     * @param venta venta que se va a buscar en el listado.
     * @return El índice de la venta en el listado si se encuentra, -1 si no se encuentra,
     * -2 si el parámetro no es válido.
     */
    public Venta buscarVenta(Venta venta) {
        // En primer lugar ordenamos el arrayList haciendo uso del método "sort", pero de la interfaz
        // colection en este caso, ya que no es un array normal
        Collections.sort(listadoVentas);
        // Evaluamos que sea una instancia de "Factura" y que no sea un valor nulo
        if(venta!=null) {
            // Creamos un iterador para recorrer el arrayList
            // CRITERIO: Se han utilizado iteradores para recorrer los elementos de las listas (10%).
            Iterator<Venta> it = listadoVentas.iterator();
            // Utilizamos el objeto de iterator que hemos creado, para hacer uso de su método
            // "hasnext" y comprobar si existe un elemento siguiente
            // Como dicho método nos devulve un valor boolean, lo usaremos como condición del bucle
            while (it.hasNext()) {
                // En caso de existir, nos devuelve true, y acontinuación haciendo uso del método "next()"
                // accedemos a dicho elemento y aprovechando que tenemos implementado en "Venta" la interfaz
                // comparable, evaluamos si el valor devuelto por dicho mé
                if (it.next().compareTo(venta) == 0) {
                    logger.info("Venta encontrada");
                    return venta;
                }
            }
            // En caso de recorrer el arrayList y no encontrar coincidencia
            // devolvemos "null"
            return null;
        } else {
            // En caso de que el objeto pasado sea nulo, devolvemos "null"
            return null;
        }

    }
    // Eliminar Venta
    /**
     * Método para eliminar una venta del listado de ventas de la persona.
     * @param venta venta que se va a eliminar.
     * @return true si la venta se ha eliminado correctamente, false en caso contrario.
     */
    public boolean eliminarVenta(Venta venta) {
        // Evaluamos la nulidad del objeto pasado
        if(venta!=null) {
            // Si no es nulo, llamamos al método "remove", que verifica si dicho objeto existe en el arrayLIst,
            // y en caso afirmativo lo elimina
            // Como el método nos devuelve un booleano, lo aprovechamos y lo devolvemos
            return listadoVentas.remove(venta);
        } else {
            // En caso de ser nulo, devolvemos un valor "false"
            return false;
        }
    }


    // Editar venta
    /**
     * Método para modificar una venta en el listado de ventas de la persona.
     * @param ventaBuscar venta que se va a buscar y modificar.
     * @param ventaNueva la venta nueva que va reemplazará a la venta antigua.
     * @return true si la venta se ha modificado correctamente, false en caso contrario.
     */
    public boolean modificarVenta(Venta ventaBuscar, Venta ventaNueva) {
        // Evaluamos que los dos objetos no sean nulos
        if((ventaBuscar != null) && (ventaNueva != null)) {
            // Llamamos al método "indexOf" para averiguar la posición del elemento que buscamos en el ArrayList
            int posicion = listadoVentas.indexOf(ventaBuscar);
            // Aprovechando que "indexOf", nos devulve un -1 en caso de no encontrar el objeto en el ArrayList
            // y evaluamos que la posición sea mayor o igual a 0
            if(posicion>=0) {
                // En primer lugar, le asignamos al nuevo objeto, el id del viejo
                ventaNueva.setId(listadoVentas.get(posicion).getId());
                // Asignamos el nuevo Objeto, en la posición del antiguo
                listadoVentas.set(posicion, ventaNueva);
                // Si se ha modificado el objeto correctamente, devolvemos un true
                return true;
            } else {
                // Si no se encuentra devolvemos "false"
                return false;
            }
        } else {
            // En caso de haber alguno nulo, devolvemos "false
            return false;
        }

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
    public Venta[] getListadoVentas() {
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
    public Factura[] getListadoFacturas() {
        return listadoFacturas;
    }
    /**
     * Este método setter nos sirve para cambiar el listado de las facturas de la persona,
     * además se asegura que el listado pasado sea del mismo tamaño que el anterior para
     * evitar errores.
     * @param listadoFacturas el nuevo listado de facturas.
     * @return true si el listado de facturas se ha establecido correctamente, false en caso contrario.
     */
    public boolean setListadoFacturas(Factura[] listadoFacturas) {
        // Nos aseguramos de que ambos arrays tengan el mismo tamaño
        if(listadoFacturas.length==getListadoFacturas().length) {
            this.listadoFacturas = listadoFacturas;
            // Devolvemos true si se a reasignado correctamente
            return true;
        } else return false;
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