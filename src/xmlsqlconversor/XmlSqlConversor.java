package xmlsqlconversor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Esta clase guardará los métodos que nos convertirán el resultado de una consulta en un xml,
 * o un xml en un registro de la base de datos
 * @author manuellaordenmartin
 * @version 1.0
 */
public class XmlSqlConversor {
    /**
     * Este método nos recorrerá a los diferentes "clientes" de un xml, y los insertará como
     * registros en nuestra base de datos.
     * Es similar al método listar de la clase XML, pero insertando registros en cada iteración.
     * @param documento es un objeto de Document que hace referencia al xml en cuestión
     * @return true en caso de que se haya ejecutado correctamente y false en caso contrario.
     */
    public static boolean xmlToSql(Document documento, Connection conexion) {
        // Comprobamos la nulidad de ambos objetos
        if(documento == null || conexion == null) {
            // Si uno es nulo no se puede realizar la operación por lo que devolvemos "null"
            return false;
        }
        // Sacamos una lista de Nodos que representa a la etiqueta raiz, como solo hay una
        // etiqueta raíz solo habrá un único elemento
        NodeList raiz = documento.getChildNodes();
        // En la siguiente línea obtendremos una lista de nodos de la etiqueta raiz, como el
        // xml que le vamos a pasar en este caso es de Clientes, será una lista de nodos de
        // elementos que representan a cada cliente en el XML
        // Como la "raiz" la hemos sacado en forma de lista de nodos, para acceder a su única
        // posición, ya que solo hay una raíz, usaremos "item(0)" y luego de este sacaremos su
        // lista de nodos hijos, que serán los clientes
        NodeList cliente = raiz.item(0).getChildNodes();
        // Recorremos la lista de nodos de cliente
        for(int i = 0; i < cliente.getLength(); i++) {
            // Evaluamos que el tipo de Nodo de esta iteración de la lista sea ELEMENT (una etiqueta)
            if (cliente.item(i).getNodeType() == Node.ELEMENT_NODE) {
                // Ahora vamos a extraer a su vez, la lista de nodos hijos de cada elemento Cliente
                // que representarán a sus atributos (en este caso en el Xml solo le he puesto 3
                // etiquetas de 3 de sus atributos, así que serán los que extraigamos)
                NodeList atributosCliente = cliente.item(i).getChildNodes();
                // Creamos la sentencia SQL para insertar los registros
                String sql = "INSERT INTO cliente (idCliente, nif, nombre) VALUES (?, ?, ?)";
                // Lo envolvemos en un try/catch
                try {
                    // Ahora creamos un objeto de PreparedStatement para ejecutar la sentencia
                    // y le pasamos la sentencia creada
                    PreparedStatement statement = conexion.prepareStatement(sql);
                    // Asignamos los valores de las etiquetas a los campos de la sentencia
                    // En primer lugar obtenemos el ID del cliente que esta en la primera (0) posición
                    // del listado de nodos (atributosCliente) y lo casteamos a "int"
                    statement.setInt(1, Integer.parseInt(atributosCliente.item(0).getTextContent()));
                    statement.setString(2, atributosCliente.item(1).getTextContent());
                    statement.setString(3, atributosCliente.item(2).getTextContent());
                    // Le hemos dado el valor a los 3 campos de nuestra sentencia y ahora la ejecutamos
                    // Al tratarse de una sentencia que cambia o inserta nuevos registros, lo hacemos con
                    // "executeUpdate()"
                    statement.executeUpdate();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    // Retornamos un false en caso de excepción de nuevo
                    return false;
                }
            }
        }
        // Si hemos llegado aquí significa que se ha ejecutado sin problemas, asi que devolvemos "true"
        return true;
    }

    /**
     * Este método recibe por parámetro un Resultado de una consulta, el cual aprovecharemos
     * para extraer el valor de sus campos, y crear etiquetas que contengan dichos valores.
     * @param rs recogemos por parámetro un objeto de "resultSet" el cual contendrá los
     * resultados de la consulta
     * @return devolvemos un objeto Document que hará referencia al XML que hemos creado
     */
    public static Document sqlToXml(ResultSet rs) {
        // Evaluamos que el objeto de ResultSet no sea nulo
        if(rs == null){
            // En caso de ser nulo devolvemos null
            return null;
        }
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
            // Creamos la etiqueta raiz, le vamos a dar el nombre de "personas" pero
            Element raiz = document.createElement("personas");
            // Añadimos la etiqueta raiz al documento
            document.appendChild(raiz);

            // Ahora recorremos con un bucle while los registros que nos devolvera la consulta y creamos
            // un elemento de "cliente" por cada iteración
            // Para ello usaremos el método ".next()" el cual actuará como un iterador
            while(rs.next()) {
                // Creamos el elemento de cliente de esta iteración
                Element cliente = document.createElement("cliente");
                // Ahora creamos los elementos de los atributos con los mismos nombre que los campos
                // correspondientes
                Element idCliente = document.createElement("idCliente");
                Element nif = document.createElement("nif");
                Element nombre = document.createElement("nombre");

                // Una vez creados, vamos a darles el valor que van a contener, en formato String
                // para ello haremos uso de los valores que se encuentran en los campos de "rs"
                // en esta iteración
                // Para el ID, al ser de tipo "int" tenemos que pasarlo a String
                idCliente.setTextContent(String.valueOf(rs.getInt("idCliente")));
                nif.setTextContent(rs.getString("nif"));
                nombre.setTextContent(rs.getString("nombre"));

                // Añadimos los elementos de los atributos al elemento "cliente"
                cliente.appendChild(idCliente);
                cliente.appendChild(nif);
                cliente.appendChild(nombre);

                // Añadimos el cliente a la etiqueta raiz
                raiz.appendChild(cliente);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Devolvemos el objeto de document, el cual en caso de saltar una excepción será "null"
        return document;
    }

}
