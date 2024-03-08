package xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import persona.Cliente;
import persona.Persona;
import persona.Vendedor;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Esta clase hace referencia a los distintos métodos, en este caso estáticos, que utilizan xml.
 * He dejado lso métodos como instanciables, en vez de estáticos, ya que si son estáticos, aunque
 * no necesitaría crear una instancia de esta clase para utilizarlos, tampoco le afectaría el
 * parámetro de tipo de la clase "T" y no podría cumplir con ese criterio de evaluación (f).
 * @param <T> con este parámetro indicamos que puede utilizar cualquier tipo de dato, pero lo
 * hemos limitado a las clases que heredan de "Persona", ya que son con las que queremos trabajar
 * con nuestro archivo xml.
 * @author manuellaordenmartin
 * @version 1.0
 */
public class Xml <T extends Persona> {
    // Declaramos el elemento raiz de cada xml, ya que siempre sera el mismo "personas"
    Element raiz;

    public Document crearDocumento() {
        Document document = null;
        // Como el IDE nos indica una excepción de tipo comprobada por el método "newDocumentBuilder()"
        // ya que puede devolver una excepción de tipo "ParserConfigurationException", lo controlaremos
        // con un "try/catch", voy a usar en el catch "Exception", ya que al ser más genérico, no se
        // crearan instancias de las excepciones que estén abajo en la gerarquía
        try {
            // A continuación, creamos nuestro documento xml y se lo asignamos al objeto de "Document"
            /*
             Con "DocumentBuilderFactory.newDefaultInstance()", creamos una instancia de la "factory"DocumentBuilderFactory,
             con la cual podremos después crear un objeto de "newDocumentBuilder",
             con dicho objeto de "newDocumentBuilder" nos permitirá crear el documento XML vacio.
             */
            document = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder().newDocument();
            // Creamos la etiqueta raiz justo en la creación del documento, ya que debe ser única,
            // porque si la añadimos cuando creemos nuevos elementos, nos creará una nueva raiz por
            // nuevo elemento
            this.raiz = document.createElement("personas");
            // Añadimos la etiqueta raiz al documento
            document.appendChild(this.raiz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Devolvemos el objeto de document, el cual en caso de saltar una excepción será "null"
        return document;
    }

    // Método Genérico
    public void addPersonaXml(T persona, Document document) {
        // Evaluamos si la persona es de tipo "CLiente" o "Vendedor" y asignamos sus etiquetas específicas
        if (persona instanceof Cliente) {
            // Creamos la etiqueta raiz (cada elemento "cliente")
            Element cliente = document.createElement("cliente");
            // Le añadimos el elemento "cliente" a la etiqueta raiz
            // Antes de esto podríamos envolver la siguiente línea en un "try/catch", ya que en caso
            // de haber algún error y de no existir ninguna etiqueta "personas" item(0) nos devuelve
            // null y por tanto nos devolvería una excepción "NullPointerException"
            document.getElementsByTagName("personas").item(0).appendChild(cliente);
            // Creamos la etiqueta que contenga el id del cliente
            Element idCliente = document.createElement("idCliente");
            // Aqui le pasamos el valor del id de cliente, sacando su valor con el método get
            // pero como el id es de tipo "int", hacemos uso del método "valueof" de la clase
            // String, para convertir dicho id en String
            idCliente.setTextContent(String.valueOf(((Cliente) persona).getCodigoCliente()));
            Element nombre = document.createElement("nombre"); // Creamos la etiqueta "nombre"
            nombre.setTextContent(persona.getNombre()); // Le asignamos su valor
            Element nif = document.createElement("nif"); // Creamos la etiqueta "nif"
            nif.setTextContent(persona.getNif()); // Le asignamos su valor
            // Ahora le asignamos a "cliente" sus atributos
            cliente.appendChild(idCliente);
            cliente.appendChild(nif);
            cliente.appendChild(nombre);
            // Adjuntamos el cliente creado, a la etiqueta raiz del documento
            this.raiz.appendChild(cliente);
        } else {
            // Creamos la etiqueta raiz (cada elemento "vendedor")
            Element vendedor = document.createElement("vendedor");
            // Le agregamos a la etiqueta raiz, el elemento "vendedor"
            // Antes de esto podríamos envolver la siguiente línea en un "try/catch", ya que en caso
            // de haber algún error y de no existir ninguna etiqueta "personas" item(0) nos devuelve
            // null y por tanto nos devolvería una excepción "NullPointerException"
            document.getElementsByTagName("personas").item(0).appendChild(vendedor);
            // Creamos la etiqueta que contenga el id del vendedor
            Element numeroVendedor = document.createElement("numeroVendedor");
            // Aqui le pasamos el valor del id del vendedor, sacando su valor con el método get
            // pero como el id es de tipo "int", hacemos uso del método "valueof" de la clase
            // String, para convertir dicho id en String
            numeroVendedor.setTextContent(String.valueOf(((Vendedor) persona).getNumeroVendedor()));
            Element nombre = document.createElement("nombre"); // Creamos la etiqueta "nombre"
            nombre.setTextContent(persona.getNombre()); // Le asignamos su valor
            Element nif = document.createElement("nif"); // Creamos la etiqueta "nif"
            nif.setTextContent(persona.getNif()); // Le asignamos su valor
            // Ahora le asignamos a "vendedor" sus atributos
            vendedor.appendChild(numeroVendedor);
            vendedor.appendChild(nif);
            vendedor.appendChild(nombre);
            // Adjuntamos el vendedor creado, a la etiqueta raiz del documento
            this.raiz.appendChild(vendedor);
        }

    }

    // Método listar genérico (para vendedor y cliente)
    public void listar(Document document) {
        // El método "getElementsByTagName()" nos devuelve una lista de nodos (etiquetas) con este
        // nombre, se encuentre donde se encuentre en la gerarquía del xml, por lo que no tenemos
        // que buscar antes la etiqueta raiz "personas", para acceder a su hija "vendedor"
        // Primero creamos una lista de nodos con las etiquetas de vendedor para obtener su tamaño
        // Verificar, si me leerá las etiquetas con nombre "cliente", o si antes necesito "clientes"
        NodeList hijosRaiz = this.raiz.getChildNodes();
        NodeList atributosDeHijo;
        for (int i = 0; i < hijosRaiz.getLength(); i++) {
            // Obtenemos el nodo de esa posición y lo convertimos a "Element" con casting
            if (hijosRaiz.item(i).getNodeType() == Node.ELEMENT_NODE) {
                atributosDeHijo = hijosRaiz.item(i).getChildNodes();
                for (int j = 0; j < atributosDeHijo.getLength(); j++) {
                    System.out.println(atributosDeHijo.item(j).getTextContent());
                }
                System.out.println("---------------------------------------");
            }
        }
    }


    // Los siguientes métodos no los vamos a utilizar, ya que hemos creado el de arriba que nos
    // sirve para los dos tipos de documentos (de Clientes y de Vendedores)
    public void listarCliente(Document document) {
        // El método "getElementsByTagName()" nos devuelve una lista de nodos (etiquetas) con este
        // nombre, se encuentre donde se encuentre en la gerarquía del xml, por lo que no tenemos
        // que buscar antes la etiqueta raiz "personas", para acceder a su hija "cliente"
        // Primero creamos una lista de nodos con las etiquetas de clientes para obtener su tamaño
        NodeList listaClientes = document.getElementsByTagName("cliente");
        // Verificar, si me leerá las etiquetas con nombre "cliente", o si antes necesito "clientes"
        for (int i = 0; i < listaClientes.getLength(); i++) {
            // Obtenemos el nodo de esa posición y lo convertimos a "Element" con casting
            Element cliente = (Element) listaClientes.item(i);
            // Ahora con dicho cliente encontrado, podemos mostrar los valores de sus etiquetas hijas
            // que en este caso hacen referencia a sus atributos (nombre, nif, id)
            // Buscamos como antes con el método "getElementsByTagName", y como nos devuelve una lista
            // usamos "item(0)" para acceder la primera y unica posición de esta lista que nos devuelve
            System.out.println(cliente.getElementsByTagName("idCliente").item(0).getTextContent());
            System.out.println(cliente.getElementsByTagName("nif").item(0).getTextContent());
            System.out.println(cliente.getElementsByTagName("nombre").item(0).getTextContent());
            System.out.println("----------------------"); // Separador
        }
    }

    public void listarVendedores(Document document) {
        // El método "getElementsByTagName()" nos devuelve una lista de nodos (etiquetas) con este
        // nombre, se encuentre donde se encuentre en la gerarquía del xml, por lo que no tenemos
        // que buscar antes la etiqueta raiz "personas", para acceder a su hija "vendedor"
        // Primero creamos una lista de nodos con las etiquetas de vendedor para obtener su tamaño
        NodeList listaVendedores = document.getElementsByTagName("vendedor");
        // Verificar, si me leerá las etiquetas con nombre "cliente", o si antes necesito "clientes"
        for (int i = 0; i < listaVendedores.getLength(); i++) {
            // Obtenemos el nodo de esa posición y lo convertimos a "Element" con casting
            Element vendedor = (Element) listaVendedores.item(i);
            // Ahora con dicho cliente encontrado, podemos mostrar los valores de sus etiquetas hijas
            // que en este caso hacen referencia a sus atributos (nombre, nif, id)
            // Buscamos como antes con el método "getElementsByTagName", y como nos devuelve una lista
            // usamos "item(0)" para acceder la primera y unica posición de esta lista que nos devuelve
            System.out.println(vendedor.getElementsByTagName("numeroVendedor").item(0).getTextContent());
            System.out.println(vendedor.getElementsByTagName("nif").item(0).getTextContent());
            System.out.println(vendedor.getElementsByTagName("nombre").item(0).getTextContent());
            System.out.println("----------------------"); // Separador
            // Lo anterior debería estar en un "try/catch" para evitar una excepción de tipo
            // NullPointerException, en caso de que no encuentre ninguna etiqueta con ese nombre
        }
    }


}