package producto;


import venta.Factura;

/**
 * Esta clase hace referencia a los atributos relacionados con la categoria
 * de los productos. Estan representadas por su valor y unidad de medida.
 * La clase implementa la interfaz comparable.
 * @author Mlm96
 * @version 1.0
 */
public class AtributoCategoria implements Comparable {
    // Declaramos los atributos
    private static int contador;
    private int id;
    private String atributo, valor, unidad;
    private Producto producto;
    // Bloque de inicialización estático
    static {
        contador = 0;
    }
    // Bloque de inicialización de instancia
    {
        id = contador++;
    }
    // Constructor
    public AtributoCategoria( String atributo, String valor,
                              String unidad) {
        this.atributo = atributo;
        this.valor = valor;
        this.unidad = unidad;
    }


    // Métodos y funcionalidades
    //Interfaz comparable
    /**
     * Implementación del método compareTo de la interfaz Comparable.
     * Compara este atributo con otro atributo basándose en sus identificadores.
     * @param objeto El atributo con la que se va a comparar.
     * @return 0 si las atributos son iguales, -1 si esta atributo es menor
     * que la atributo pasada, 1 si esta atributo es mayor que la atributo pasada.
     */
    @Override
    public int compareTo(Object objeto) {
        // Evaluamos si el objeto esta a "null"
        if(objeto==null) throw new IllegalArgumentException("El parámetro no puede ser nulo");
        // Evaluamos si el objeto es una instancia de "Factura"
        if(!(objeto instanceof AtributoCategoria)) throw new IllegalArgumentException("El parámetro debe ser del tipo Empleado");
        // Comparamos si el AtributoCategoria pasada es mayor(1), menor(-1) o son iguales (0)
        if(((AtributoCategoria)objeto).getId()==this.getId()) {
            // Si son iguales devolvemos un 0
            return 0;
        } else if(((AtributoCategoria)objeto).getId()<this.getId()) {
            // Si el objeto pasado tiene un id menor devolvemos un -1
            return -1;
        } else {
            // Si el objeto pasado tiene un id mayor devolvemos un 1.
            return 1;
        }
    }


    // GETTERS Y SETTERS
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getAtributo() {
        return atributo;
    }
    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }
    public String getValor() {
        return valor;
    }
    public void setValor(String valor) {
        this.valor = valor;
    }
    public String getUnidad() {
        return unidad;
    }
    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Producto getProducto() {
        return producto;
    }
}