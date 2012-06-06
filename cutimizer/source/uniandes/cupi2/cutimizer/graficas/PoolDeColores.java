package uniandes.cupi2.cutimizer.graficas;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Esta clase se encarga de asignar colores diferentes diferenciables a los
 * diferentes items
 * 
 * @author jsierrajur
 * 
 */
public class PoolDeColores {

	/**
	 * Todos los colores que se pueden usar
	 */
	private static ArrayList<Color> colores;

	private static PoolDeColores instancia;

	/**
	 * Crea un nuevo pool de colores
	 */
	private PoolDeColores() {
		rellenarListaColores();
	}

	/**
	 * Rellena la lista de colores en caso de que esta se acabe
	 */
	private void rellenarListaColores() {
		colores = new ArrayList<Color>();
		for (int r = 0; r <= 255; r += 40) {
			for (int g = 0; g <= 255; g += 40) {
				for (int b = 0; b <= 255; b += 40) {
					if (r + g + b > 0) {
						if (r < 240)
							colores.add(new Color(r, g, b));
						else if (r == 240 && (g > 40 || r > 40))
							colores.add(new Color(r, g, b));
					}
				}
			}
		}
	}

	/**
	 * Retorna el Color correspondiente a un desperdicio
	 * 
	 * @return el color correspondiente a un desperdicio
	 */
	public Color darColorDesperdicio() {
		return new Color(255, 0, 0);
	}

	/**
	 * Retorna un color para ser usado
	 * 
	 * @return el color para ser usado
	 */
	public Color darColor() {
		int aleatorio = (int) (Math.random() * colores.size());
		Color color = colores.get(aleatorio);
		colores.remove(color);

		if (colores.size() == 0)
			rellenarListaColores();

		return color;
	}

	/**
	 * Da una instancia de la clase
	 * 
	 * @return la instancia de la clase PoolDeColores
	 */
	public static PoolDeColores darInstancia() {
		return (instancia == null) ? instancia = new PoolDeColores()
				: instancia;
	}

}
