package uniandes.cupi2.cutimizer.mundo;

/**
 * Modela un ítem en una solución dada
 * 
 * @author jsierrajur
 */
public class ItemSolucion {

	/**
	 * Si la orientación es invertida en el ítem representado
	 */
	public static final int INVERTIDA = 1;
	/**
	 * Si la orientación es igual a la del ítem representado
	 */
	public static final int ORIGINAL = 0;

	/**
	 * La posición x del ítem
	 */
	private int x;

	/**
	 * La posición y del ítem
	 */
	private int y;
	/**
	 * El ítem que representa la instancia
	 */
	private Item representado;

	/**
	 * La orientación que se encuentra el ítem en el patrón
	 */
	private int orientacion;

	/**
	 * Construye una nueva instancia con los parámetros dados
	 * 
	 * @param x
	 * @param y
	 * @param representado
	 */
	public ItemSolucion(int x, int y, Item representado, int orientacion) {
		this.x = x;
		this.y = y;
		this.orientacion = orientacion;
		this.representado = representado;
	}

	/**
	 * Da la coordenada x del ítem
	 * 
	 * @return la coordenada x del ítem
	 */
	public int getCoordenadaX() {
		return x;
	}

	/**
	 * Da el ítem que representa esta isntacia
	 * 
	 * @return el ítem representado
	 */
	public Item getRepresentado() {
		return representado;
	}

	/**
	 * Asigna el ítem representado por esta instancia
	 * 
	 * @param representado
	 *            el ítem representado
	 */
	public void setRepresentado(Item representado) {
		this.representado = representado;
	}

	/**
	 * Da la coordenada y del ítem
	 * 
	 * @return la coordenada y
	 */
	public int getCoordenadaY() {
		return y;
	}

	/**
	 * Da la orientación del ítem dado
	 * 
	 * @return la orientación del ítem
	 */
	public int darOrientacion() {
		return orientacion;
	}

	@Override
	public String toString() {
		String resumen;
		if (orientacion == ORIGINAL)
			resumen = "ítem #" + representado.darID() + " de tamaño " + representado.getAncho() + "x"
					+ representado.getAlto() + "x"
					+ representado.getEspesor() + " mm ubicado en la posición ("
					+ getCoordenadaX() + "," + getCoordenadaY() + ") mm de la lámina"
					+ '\n';

		else
			resumen = "ítem #" + representado.darID() + " de tamaño " + representado.getAlto() + "x"
					+ representado.getAncho() + "x"
					+ representado.getEspesor() + " mm ubicado en la posición ("
					+ getCoordenadaX() + "," + getCoordenadaY() + ") mm de la lámina"
					+ '\n';

		return resumen;
	}
}
