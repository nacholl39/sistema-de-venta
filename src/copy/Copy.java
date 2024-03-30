package copy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Esta clase nos ayudará a crear dos métodos muy parecidos, pero que aunque tienen la misma
 * función que es pasarle un archivo, leerlo, y generar una copia de dicho archivo, varían
 * un poco en la instancia de la clase que creamos para leer el archivo original, y por tanto
 * también es diferente el método que usaremos para leerlo, ya que en el primer método usamos
 * una instancia de BufferReader y leemos usando "readLine" el cual nos permite leer la línea
 * completa, y en el segundo método usamos directamente la instancia de "FileReader", por lo
 * que usamos su método "read", el cual lee el archivo origina, pero lo hará carácter a carácter.
 */
public class Copy {

    /*
    CRITERIO "e": Se han creado programas que utilicen diversos métodos de acceso al contenido de
    los ficheros (15%).
    Ya que estamos leyendo en este caso el fichero original, mediante una instancia de "BufferReader"
     */
    public static void copiar(String fichero, String ficheroCopia) {
        // En primer lugar, usamos una instancia de "File", a la que le pasaremos el archivo
        // original, y llamaremos a su método "exists" para verificar si el archivo existe.
        // Como esto nos devolvera "true" en caso de existir, lo negaremos con "!"
        if (!new File(fichero).exists()) {
            System.out.println("El fichero de origen no existe.");
            return;
        }
        // Creamos una instancia de "FileReader" para poder pasarsela a la instancia que queremos
        // crear de "BufferReader". Esta instancia nos ayudará a leer el archivo original.
        try {
            /*
            CRITERIO "c": Se han reconocido las posibilidades de entrada/salida del lenguaje y las
            librerías asociadas (10%).
            Ya que utilizamos una instancia de "BufferReader", a partir de una instancia de "FileReader"
            y en el siguiente método "copiarDos" lo hacemos con "FileReader", cambiando también los
            métodos con los que leemos el fichero de origen.
            */
            /*
            CRITERIO "d": Se han utilizado ficheros para almacenar y recuperar información (10%).
            Ya que estamos pasandole el "fichero" original para que lo lea.
             */
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fichero));
            // Creamos una instancia de "FileWriter", que nos servirá para escribir sobre el nuevo
            // archivo. Le pasamos el nombre del archivo, el cual lo creará en caso de que no exista,
            // o lo sobreescribirá en caso de que exista.
            // Si existe y no queremos que lo sobreescriba, tenemos que poner como segundo parámetro "true"
            // así conseguiremos que en vez de sobreescribir, lo añada seguidamente.
            FileWriter fileWriter = new FileWriter(ficheroCopia);
            // Leemos la primera línea del archivo original, llamando al método "readLine", y como nos
            // devolverá un String que corresponde a la línea leida, lo asignamos a una variable de tipo
            // String también.
            String linea = bufferedReader.readLine();
            // Verificamos que la línea leida no sea "null", es decir, que haya leido una línea.
            while (linea != null) {
                // Una vez hemos verificado que la línea no es nula, llamamos al método "write"
                // de nuestra instancia de "fileWriter" para escribir dicha línea en el archivo
                // copia
                /*
                CRITERIO "d": Se han utilizado ficheros para almacenar y recuperar información (10%).
                 Ya que estamos trazando en el fichero de copia "ficheroCopia"
                 */
                fileWriter.write(linea);
                // Leemos la siguiente línea. Cuando nos devuelva un "null" significará que ha llegado
                // al final y no hay más líneas, por lo que en la siguiente iteración no entra al bucle
                linea = bufferedReader.readLine();
            }
            // Cerramos el "bufferReader"
            bufferedReader.close();
            // Cerramos también la instancia de "fileWriter"
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    CRITERIO "e": Se han creado programas que utilicen diversos métodos de acceso al contenido de
    los ficheros (15%).
    Ya que estamos leyendo en este caso el fichero original, mediante una instancia de "FileReader"
    */
    public static void copiarDos(String fichero, String ficheroCopia) {
        // En primer lugar, usamos una instancia de "File", a la que le pasaremos el archivo
        // original, y llamaremos a su método "exists" para verificar si el archivo existe.
        // Como esto nos devolvera "true" en caso de existir, lo negaremos con "!"
        if (!new File(fichero).exists()) {
            System.out.println("El fichero de origen no existe");
            return;
        }
        // Creamos una instancia de "fileReader" para leer el archivo original
        try {
            FileReader fileReader = new FileReader(fichero);
            // Creamos una instancia de "fileWriter" para escribir en el archivo de copia
            FileWriter fileWriter = new FileWriter(ficheroCopia);
            // En este caso, como vamos a leer con una instancia de "FileReader", tendremos que
            // llamar a su método "read" que leerá caracter a caracter, en vez de la línea completa
            // Además dicho método no nos devuelve un String como el método "readLine" de BufferReader
            // sino que en este caso nos devuelve un "int" que será el equivalente al valor de la tabla
            // ASCII del carácter que ha leido.
            int caracter = fileReader.read();
            // El método anterior (read) nos devuelve "-1" en caso de no leer ningún caracter, con
            // eso podemos saber cuando hemos llegado al final del archivo. Por ello evaluamos,
            // y miestras el valor devuelto sea distinto a "-1" seguimos leyendo
            while (caracter != -1) {
                // A continuación, es igual que el otro método "copiar", llamamos al método "write"
                // de la instancia "fileWriter", para escribir el caracter leido, en el archivo de
                // copia
                fileWriter.write(caracter);
                // Leemos el siguiente caracter
                caracter = fileReader.read();
            }
            // Cerramos las dos instancias anteriores
            fileReader.close();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
