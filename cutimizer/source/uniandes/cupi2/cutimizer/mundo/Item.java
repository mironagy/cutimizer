package uniandes.cupi2.cutimizer.mundo;

import java.awt.Color;

/**
 * Esta clase modela una demanda del sub lamninas que el problema maestro utiliza como par�metro
 * @author        SIERRRA
 */
public class Item {
	
	/**
	 * La identificación del item
	 */
	private int id;
	/**
	 * Modela el ancho del item
	 * @uml.property  name="ancho"
	 */
	private int ancho;
	/**
	 * Modela el alto del item
	 * @uml.property  name="alto"
	 */
	private int alto;
	/**
	 * Modela el espesor del item
	 * @uml.property  name="espesor"
	 */
	private int espesor;
	/**
	 * Modela la cantidad demandada del item
	 * @uml.property  name="cantidad"
	 */
	private int cantidad;
	
	
	private Color color;
	/**
	 * Construye una nueva demanda del item
	 * @param ancho	el ancho del la sub lámina
	 * @param alto	el alto del la sub lámina
	 * @param espesor	el espesor del la sub lámina
	 * @param cantidad	la cantidad del la item
	 * @param id la identificación del item
	 */
	public Item(int ancho, int alto, int espesor, int cantidad, Color color, int id) {
		this.ancho = ancho;
		this.alto = alto;
		this.espesor = espesor;
		this.cantidad = cantidad;
		this.color = color;
		this.id = id;
	}

	/**
	 * Retorna el ancho del la  demanda del item
	 * @return        	el ancho del la  demanda del item
	 * @uml.property  name="ancho"
	 */
	public int getAncho() {
		return ancho;
	}

	/**
	 * Modifica el ancho del la  demanda del item
	 * @param ancho        el nuevo ancho del la  demanda del item
	 * @uml.property  name="ancho"
	 */
	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	/**
	 * Retorna el alto del la  demanda del item
	 * @return        	el alto del la  demanda del subb láminas
	 * @uml.property  name="alto"
	 */
	public int getAlto() {
		return alto;
	}
	
	/**
	 * Modifica el alto del la  demanda del item
	 * @param ancho        el nuevo alto del la  demanda del item
	 * @uml.property  name="alto"
	 */
	public void setAlto(int alto) {
		this.alto = alto;
	}

	/**
	 * Retorna el espesor del la item
	 * @return        	el espesor del la  demanda del item
	 * @uml.property  name="espesor"
	 */
	public int getEspesor() {
		return espesor;
	}

	/**
	 * Modifica el espesor del la  demanda del item
	 * @param ancho        el nuevo espesor del la  demanda del item
	 * @uml.property  name="espesor"
	 */
	public void setEspesor(int espesor) {
		this.espesor = espesor;
	}

	/**
	 * Retorna la cantidad del la item
	 * @return        	la cantidad del la item
	 * @uml.property  name="cantidad"
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * Modifica la cantidad del las demandas del item
	 * @param la        nueva cantidad demandada del item
	 * @uml.property  name="cantidad"
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * Da el color del que se dibujará el item
	 * @return el color del item
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Cambia el color del que se dibujará el item
	 * @param color el nuevo color del item
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	public int darID()
	{
		return id;
	}
	
	
}
