package uniandes.cupi2.cutimizer.mundo;

import java.util.ArrayList;

import uniandes.cupi2.cutimizer.mundo.interfaces.IPatron;

/**
 * Modela un patrón de corte
 * 
 * @author SIERRRA
 */
public abstract class Patron implements IPatron {

	/**
	 * La cantidad del patron que se requiere en la solución
	 */
	private int cantidad;

	/**
	 * La identificación del patrón
	 */
	private int id;

	/**
	 * El conjunto de items en el patrón
	 */
	private ArrayList<ItemSolucion> items;

	/**
	 * El precio del patrón
	 */
	private double precio;

	/**
	 * Indicaciones de c�mo cortar esta patrón
	 */
	private String indicacionesCorte;

	/**
	 * Construye un nuevo Patron con los parámetros dados y sin indicaciones de
	 * corte
	 * 
	 * @param items
	 * @param precio
	 * @param id
	 * @param cantidad
	 */
	public Patron(ArrayList<ItemSolucion> items, double precio, int id,
			int cantidad) {
		this.cantidad = cantidad;
		this.asignarPrecio(precio);
		this.items = items;
		this.id = id;
		indicacionesCorte = "No existen indicaciones de corte para este patrón";
	}

	/**
	 * Construye una nueva instancia con las indicaciones de corte dadas
	 * 
	 * @param items
	 * @param precio
	 * @param id
	 * @param cantidad
	 * @param indicaciones
	 */
	public Patron(ArrayList<ItemSolucion> items, double precio, int id,
			int cantidad, String indicaciones) {
		this.cantidad = cantidad;
		this.asignarPrecio(precio);
		this.items = items;
		this.id = id;
		this.indicacionesCorte = indicaciones;
	}

	/**
	 * Da la cantidad del item con medidas especificadas hay en este patron
	 * 
	 * @param alto
	 *            el alto del item
	 * @param ancho
	 *            el ancho del item
	 * @param espesor
	 *            el espesor del item
	 * @return la cantidad del item en este patron
	 */
	@Override
	public int darCantidadItem(int alto, int ancho, int espesor) {
		int cantidad = 0;
		for (ItemSolucion is : items) {
			Item i = is.getRepresentado();
			if (i.getAlto() == alto && i.getAncho() == ancho
					&& i.getEspesor() == espesor)
				cantidad++;
		}
		return cantidad;
	}

	/**
	 * Devuelve los items del patrón
	 * 
	 * @return la estructura de datos con los patrones
	 */
	@Override
	public ArrayList<ItemSolucion> getItems() {
		return items;
	}

	@Override
	public String toString() {
		return "Patrón #" + id + ": " + cantidad + " lámina(s) de "
				+ darEspesorContenedora() + " mm";
	}

	@Override
	/**
	 * Da indicaciones de cómo cortar la lámina para obtener el patrón
	 */
	public String darIndicacionesDeCorte() {
		return indicacionesCorte;
	}

	@Override
	/**
	 * Da una descripción del patrón
	 */
	public String darDescripcion() {
		String patron = "Patrón " + id + " - lámina de "
				+ darEspesorContenedora() + " mm de espesor" + '\n';
		patron += "Costo unitaro " + precio + '\n';
		patron += "Costo total " + darPrecio() + '\n' + '\n';

		patron += "Descripción de los items" + '\n' + '\n';
		int contador = 1;
		for (ItemSolucion item : items) {
			patron += contador + ". " + item.toString();
			contador++;
		}

		return patron;
	}

	@Override
	/**
	 * Da la cantidad necesidad del patrón
	 */
	public int darCantidad() {
		return cantidad;
	}

	/**
	 * Da el precio
	 * 
	 * @return the precio
	 */
	@Override
	public double darPrecio() {
		return precio * cantidad;
	}

	/**
	 * Asignar un nuevo precio al patrón
	 * 
	 * @param precio
	 *            the precio to set
	 */
	public void asignarPrecio(Double precio) {
		this.precio = precio;
	}

	/**
	 * Modifica la cantidad necesitada del patrón
	 */
	@Override
	public void asignarCantidad(int nCantidad) {
		cantidad = nCantidad;
	}

	/**
	 * Retorna el espesor de la contenedora de donde sale el patrón
	 */
	public int darEspesorContenedora() {
		return items.get(0).getRepresentado().getEspesor();
	}



}
