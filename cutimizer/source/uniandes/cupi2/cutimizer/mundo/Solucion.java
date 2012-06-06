package uniandes.cupi2.cutimizer.mundo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import uniandes.cupi2.cutimizer.mundo.interfaces.IPatron;

/**
 * Modela una soluci�n a un problema de optimizac�on
 * 
 * @author SIERRRA
 */
public class Solucion {
	/**
	 * La fecha de cuando fue corrido el modelo
	 * 
	 * @uml.property name="fecha"
	 */
	private Date fecha;

	/**
	 * El nombre de la solucion
	 * 
	 * @uml.property name="nombre"
	 */
	private String nombre;

	/**
	 * La lista de patrones y sus usos
	 * 
	 * @uml.property name="patrones"
	 */
	private ArrayList<IPatron> patrones;

	/**
	 * Crea una nueva soluci�n
	 * 
	 * @param fecha
	 *            la fecha de cuando fue ejecutada la soluci�n
	 * @param nombre
	 *            el nombre de la soluci�n
	 * @param patrones
	 *            la tupla patrones cantidad usados en la soluci�n objetima
	 */
	public Solucion(Date fecha, String nombre, ArrayList<IPatron> patrones) {
		this.fecha = fecha;
		this.nombre = nombre;
		this.patrones = patrones;
	}

	/**
	 * @return the fecha
	 * @uml.property name="fecha"
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha
	 *            the fecha to set
	 * @uml.property name="fecha"
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the nombre
	 * @uml.property name="nombre"
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 * @uml.property name="nombre"
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the patrones
	 * @uml.property name="patrones"
	 */
	public ArrayList<IPatron> getPatrones() {
		return patrones;
	}

	/**
	 * @param patrones
	 *            the patrones to set
	 * @uml.property name="patrones"
	 */
	public void setPatrones(ArrayList<IPatron> patrones) {
		this.patrones = patrones;
	}

	@Override
	public String toString() {
		String desc = "Solución " + nombre + " del " + fecha.toString() + '\n';
		desc+= "Costo total de la solución " + darCosto()+ " de cortar "+ darNumeroLaminas()+ " lámina(s)"+ '\n'+ '\n';
		int contador = 1;
		for (IPatron patron : patrones) {
				desc += contador + ". Cortar " + patron.darCantidad()
						+ " lamina(s) con el patrón " + patron.toString() + '\n';
				contador++;
		}

		return desc;
	}

	/**
	 * Guarda las imagenes correspondientes a la soluci�n en la ruta especificada
	 * @param ruta la ruta donde se guardaran las im�genes
	 * @throws IOException si existe un problema guardado las imagenes
	 */
	public void guardarImagenesSolucion(String ruta) throws IOException
	{
		int i = 0;
		for(IPatron patron: patrones)
		{
			i++;
			patron.crearImagenPatron(ruta+"/"+"patron"+i+".png");
			
		}
	}
	
	
	/**
	 * Da el n�mero de l�minas que se deben cortar en la soluci�n original
	 * 
	 * @return el n�mero de l�minas a cortar
	 */
	public int darNumeroLaminas() {
		int laminas = 0;
		for (IPatron patron : patrones) {
			laminas += patron.darCantidad();
		}

		return laminas;
	}

	/**
	 * Da el costo de la solucion
	 * 
	 * @return el costo
	 */
	public double darCosto() {
		double precio = 0;
		for (IPatron patron : patrones) {
			precio += patron.darPrecio();
		}

		return precio;
	}
}
