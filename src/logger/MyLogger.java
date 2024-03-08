package logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Clase de utilidad para construir un logger a partir
 * del nombre de la clase que lo requiere
 * @author manuellaordenmartin
 * @version 1.0
 */
public class MyLogger {
    // Método estático de logger
    /**
     * Este método estático devuelve un objeto Logger que esta configurado leyendo
     * las propiedades de logging desde el archivo 'myloggin.properties' ubicado
     * en el directorio 'src'.
     * @param clase es el nombre de la clase para la cual se desea obtener el Logger.
     * @return Logger Un objeto Logger configurado según las propiedades
     * especificadas en 'myloggin.properties'.
     * @throws IOException Si ocurre un error al leer el archivo de propiedades
     * 'myloggin.properties'.
     */
    public static Logger getLogger(String clase) {
        // Lo envolvemos en un try,catch ya que nos da un error en tiempo de
        // compilación (comprobado), al detectar que vamos a leer de un fichero,
        // ya que puede darse que dicho fichero no exista
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("src/mylogging.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Logger.getLogger(clase);
    }
}