package uniandes.cupi2.cutimizer.mundo;
/**
 * Clae que modela una contenedora de materia prima original que se debe cortar.
 * @author        jsierrajur
 */
public class Contenedora {
	/**
	 * Modela el ancho de la contenedora
	 * @uml.property  name="ancho"
	 */
	private int ancho;
	/**
	 * Modela el alto de la contenedora
	 * @uml.property  name="alto"
	 */
	private int alto;
	/**
	 * Modela el espesor de la contenedora
	 * @uml.property  name="espesor"
	 */
	private int espesor;
	
	/**
	 * El precio de la contenedora
	 * @uml.property  name="precio"
	 */
	private double precio;
	/**
	 * Construye una nuevacontenedora
	 * @param ancho	el ancho de la contenedora
	 * @param alto	el alto de la contenedora
	 * @param espesor	el espesor de la contenedora
	 * @param cantidad	la cantidad de la contenedora
	 * @param precio el precio de la contenedora
	 */
	public Contenedora(int ancho, int alto, int espesor, double precio) {
		this.ancho = ancho;
		this.alto = alto;
		this.espesor = espesor;
		this.precio=precio;
	}

	/**
	 * Retorna el ancho de la contenedora
	 * @return        	el ancho de la contenedora
	 * @uml.property  name="ancho"
	 */
	public int getAncho() {
		return ancho;
	}

	/**
	 * Modifica el ancho de la contenedora
	 * @param ancho        el nuevo ancho de la contenedora
	 * @uml.property  name="ancho"
	 */
	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	/**
	 * Retorna el alto de la contenedora
	 * @return        	el alto de la contenedora
	 * @uml.property  name="alto"
	 */
	public int getAlto() {
		return alto;
	}
	
	/**
	 * Modifica el alto de la contenedora
	 * @param ancho        el nuevo alto de la contenedora
	 * @uml.property  name="alto"
	 */
	public void setAlto(int alto) {
		this.alto = alto;
	}

	/**
	 * Retorna el espesor de la contenedora
	 * @return        	el espesor de la contenedora
	 * @uml.property  name="espesor"
	 */
	public int getEspesor() {
		return espesor;
	}

	/**
	 * Modifica el espesor de la contenedora
	 * @param ancho        el nuevo espesor de la contenedora
	 * @uml.property  name="espesor"
	 */
	public void setEspesor(int espesor) {
		this.espesor = espesor;
	}
	/**
	 * Retorna el precio de la contenedora
	 * @return        	el precio de la contenedora
	 * @uml.property  name="precio"
	 */
	public double getPrecio() {
		return precio;
	}

	/**
	 * Modifica el precio de la contenedora
	 * @param ancho        el nuevo precio de la contenedora
	 * @uml.property  name="precio"
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	 
}
