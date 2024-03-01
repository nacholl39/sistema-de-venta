package producto;


import venta.DetalleFactura;
import venta.Factura;
import venta.Venta;

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
    private AtributoCategoria[] atributosCategoria;
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
    public Producto( String referencia, int marca, int modelo,
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
    /**
     * Método para añadir un atributos de categoria del producto al listado de estos
     * del propio producto.
     * @param atributoCategoria atributos de categoria que va a ser añadido.
     * @return true si el atributo se ha añadido correctamente, false en caso contrario.
     */
    public boolean addAtributoCategoria(AtributoCategoria atributoCategoria) {
        // Inicializamos un constador para recorrer el listado de Facturas
        int contador=0;
        // Evaluamos que sea una instancia de "Venta" y que no sea un valor nulo
        if(atributoCategoria!=null) {
            // Recorremos el listado de AtributoCategoria, y añadimos la AtributoCategoria donde
            // el valor sea Null es decir, donde este vacío. Como no sabemos cuando será, usamos do,while
            do {
                if(atributosCategoria[contador] == null) {
                    // Añadimos el atributoCategoria al array
                    atributosCategoria[contador] = atributoCategoria;
                    // Si se ha agregado devolvemos un true y así salimos del bucle también
                    return true;
                }
                // Incrementamos el contador
                contador++;
                // Evaluamos en la condición que contador no supere al tamaño del array
            } while((contador<atributosCategoria.length));
        }
        // Si no se ha añadido correctamente devolvemos "false"
        return false;
    }

    // Buscar AtributoCategoria
    /**
     * Método para buscar un atributo en el listado de atributos del producto.
     * @param atributoCategoria es el atributo que se va a buscar.
     * @return El índice de la factura en el listado si se encuentra, -1 si no se encuentra,
     * -2 si el parámetro no es válido.
     */
    public int buscarAtributoCategoria(AtributoCategoria atributoCategoria) {
        // Inicializamos un contador para recorrer el array
        int contador=0;
        // Evaluamos que sea una instancia de "Factura" y que no sea un valor nulo
        if(atributoCategoria!=null) {
            // Recorremos el array de Facturas, como no sabemos cuando encontraremos la factura
            // emplearemos un bucle do,while
            do {
                // Usamos la interfaz comparable de "Factura" para evaluar si son iguales
                if(atributosCategoria[contador].compareTo(atributoCategoria) == 0) {
                    // Salimos del bucle si se ha cumplido la condición
                    // Con eso conseguimos que contador se quede con el valor de la posición
                    return contador;
                }
                // Incrementamos el contador
                contador++;
                // Evaluamos que contador no supere el tamaño del array
            } while((contador<atributosCategoria.length));
            // Retornamos el valor -1 si no se ha encontrado ninguna
            return -1;
        }
        // Devolvemos un -2 en caso de que el parámetro no sea válido (nulo o de otro tipo)
        else return -2;
    }
    // Eliminar AtributoCategoria
    /**
     * Método para eliminar un atributo del listado de atributos del producto.
     * @param atributoCategoria es el atributo que se va a eliminar.
     * @return true si el atributo se ha eliminado correctamente, false en caso contrario.
     */
    public boolean eliminarAtributoCategoria(AtributoCategoria atributoCategoria) {
        // Llamamos al método "buscarVenta" para encontrar la posición del elemento a
        // eliminar y le asignamos el valor a una variable
        int posicion = buscarAtributoCategoria(atributoCategoria);
        // Inicializamos un nuevo array de Ventas
        AtributoCategoria[] nuevoAtributosCategoria = new AtributoCategoria[10];
        // Evaluamos que la posición devuelta sea mayor o igual a 0, ya que en caso contrario
        // significa que el objeto venta es nulo, de otro tipo o no se ha encontrado
        if(posicion>=0) {
            // Inicializamos un contador para recorrer el array nuevo
            int contador=0;
            // Recorremos el array de Ventas y la guardamos mientras no coincida, en el nuevo array
            // asi obtenemos un nuevo array usamos un bucle for porque debemos recorrerlo entero
            for(int i=0; i<atributosCategoria.length; i++) {
                // Si no coincide la variable iteradora con la posición que deseamos eliminar,
                // la almacenamos e incrementamos el contador de posiciones del nuevo
                // array, en caso contrario no, así conseguimos que no se quede el hueco
                // de la posición que hemos eliminado en el nuevo listado
                if(i!=posicion) {
                    nuevoAtributosCategoria[contador] = atributosCategoria[i];
                    // Incrementamos el contador
                    contador++;
                }
            }
            // Reasignamos al listado de facturas el nuevo listado
            setAtributosCategoria(nuevoAtributosCategoria);
            // Devolvemos un true para verificar que se ha hecho correctamente
            return true;
        }
        // Devolvemos false, en caso de que no se haya eliminado ningun elemento y siga igual
        return false;
    }
    // Editar AtributoCategoria
    /**
     * Método para modificar un atributo en el listado de atributos del producto.
     * @param atributoCategoriaBuscar atributo que se va a buscar y modificar.
     * @param atributoCategoriaNueva atributo nuevo que va reemplazará al atributo antiguo.
     * @return true si el atributo se ha modificado correctamente, false en caso contrario.
     */
    public boolean modificarAtributoCategoria(AtributoCategoria atributoCategoriaBuscar,
                                              AtributoCategoria atributoCategoriaNueva) {
        // Llamamos al método buscar venta para localizar la venta a modificar
        // y guardamos la posición en una variable
        int posicion = buscarAtributoCategoria(atributoCategoriaBuscar);
        // Evaluamos que la posición devuelta sea mayor o igual a 0, ya que en caso contrario
        // significa que el objeto factura es nulo, de otro tipo o no se ha encontrado
        if(posicion>=0) {
            // Se lo asignamos a la factura nueva el identificador de la antigua
            atributoCategoriaNueva.setId(atributosCategoria[posicion].getId());
            // Le asignamos al objeto de factura en esa posición, los nuevos valores, que
            // son los del objeto "facturaNueva"
            atributosCategoria[posicion] = atributoCategoriaNueva;
            // Devolvemos un true para verificar que se ha hecho correctamente
            return true;
        }
        // Devolvemos false, en caso de que no se haya modficado ningun elemento y siga igual
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