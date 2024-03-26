package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;
public class BaseDatos {
    public static boolean ejecutarScript(String rutaScript, Connection conexion) {
        try {
            // Creamos un objeto de BufferedReader, para leer el Script del fichero
            BufferedReader databaseFile = new BufferedReader(new FileReader(rutaScript));
            // Creamos un statement, para cargar los comandos del Script y ejecutarlos
            Statement statement = conexion.createStatement();
            // Inicializamos un String, el cual contendrá cada comando SQL que vaya leyendo
            String comando = databaseFile.readLine();
            // Como en la inicialización se ha leido la primera línea, comprobamos si contenia algo
            // metiendolo en la condición del bucle, así mientras la línea que lea tenga
            // contenido, entrara dentro del bucle y se cargará en el "executeBach"
            while (comando != null) {
                // Como habrá consultas que tengan más de una línea, vamos a comprobar si la
                // linea actual contiene ";", lo cual indicaría el final del comando, si la contiene
                // no entraríamos en el siguiente bucle, pero en caso de no hayarla en la línea,
                // volvemos a leer la siguiente línea y la concatenamos con la anterior, así
                // sucesivamente hasta que se encuentre con el ";" que indicará el fin del comando
                while (!comando.contains(";")) {
                    // Leemos la siguiente línea y la concatenamos
                    comando += databaseFile.readLine();
                }
                // Cargamos la línea que contiene el comando
                statement.addBatch(comando);
                comando = databaseFile.readLine(); // Leemos la siguiente línea
            }
            // Una vez fuera del bucle, como ya se habrá cargado el Script completo, pasamos a ejecutarlo
            statement.executeBatch();
            // Cerramos el objeto de statement
            statement.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
