package uniandes.cupi2.cutimizer.mundo;

/**
 * Modela un item en una solución dada
 * 
 * @author jsierrajur
 */
public class ItemSolucion {

	/**
	 * Si la orientación es invertida en el item representado
	 */
	public static final int INVERTIDA = 1;
	/**
	 * Si la orientación es igual a la del item representado
	 */
	public static final int ORIGINAL = 0;

	/**
	 * La posición x del item
	 */
	private int x;

	/**
	 * La posición y del item
	 */
	private int y;
	/**
	 * El item que representa la instancia
	 */
	private Item representado;

	/**
	 * La orientación que se encuentra el item en el patrón
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
	 * Da la coordenada x del item
	 * 
	 * @return la coordenada x del item
	 */
	public int getCoordenadaX() {
		return x;
	}

	/**
	 * Da el Item que representa esta isntacia
	 * 
	 * @return el item representado
	 */
	public Item getRepresentado() {
		return representado;
	}

	/**
	 * Asigna el item representado por esta instancia
	 * 
	 * @param representado
	 *            el item representado
	 */
	public void setRepresentado(Item representado) {
		this.representado = representado;
	}

	/**
	 * Da la coordenada y del item
	 * 
	 * @return la coordenada y
	 */
	public int getCoordenadaY() {
		return y;
	}

	/**
	 * Da la orientación del item dado
	 * 
	 * @return la orientación del item
	 */
	public int darOrientacion() {
		return orientacion;
	}

	@Override
	public String toString() {
		String resumen;
		if (orientacion == ORIGINAL)
			resumen = "Item #" + representado.darID() + " de tamaño " + representado.getAncho() + "x"
					+ representado.getAlto() + "x"
					+ representado.getEspesor() + " mm ubicado en la posición ("
					+ getCoordenadaX() + "," + getCoordenadaY() + ") mm de la lámina"
					+ '\n';

		else
			resumen = "Item #" + representado.darID() + " de tamaño " + representado.getAlto() + "x"
					+ representado.getAncho() + "x"
					+ representado.getEspesor() + " mm ubicado en la posición ("
					+ getCoordenadaX() + "," + getCoordenadaY() + ") mm de la lámina"
					+ '\n';

		return resumen;
	}
}
