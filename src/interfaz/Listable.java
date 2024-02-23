package interfaz;

import persona.Persona;

import java.util.ArrayList;

/**
 * Esta interfaz nos sirve para cumplir el criterio "f",
 * Se han creado clases y métodos genéricos (20%).
 * Contiene un método que nos servirá para mostrar los elementos
 * de los distintos arrayList
 * @param <T> ponemos la T para aprovechar la programación genérica
 * y que nos sirva para los arrayList de caulquier tipo.
 */
public interface Listable<T> {
    void listarElemento(ArrayList<T> listado);
}
