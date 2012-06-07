package uniandes.cupi2.cutimizer.mundo.problemaauxiliar;

import java.util.ArrayList;

import uniandes.cupi2.cutimizer.mundo.Item;
import uniandes.cupi2.cutimizer.mundo.ItemSolucion;
import uniandes.cupi2.cutimizer.mundo.Patron2D;
import uniandes.cupi2.cutimizer.mundo.Principal;
import uniandes.cupi2.cutimizer.mundo.interfaces.IPatron;

/**
 * Modela un problema auxiliar con el algoritmo DP usando threads
 * 
 * @author jsierrajur
 */
public class ProblemaAuxiliarDP extends ProblemaAuxiliar {

	/**
	 * el conjunto de diferentes anchos de items
	 */
	private ArrayList<Integer> w;
	/**
	 * El conjunto de diferentes alturas de los items
	 */
	private ArrayList<Integer> h;
	/**
	 * El valor de los diferentes items
	 */
	private ArrayList<Double> v;

	/**
	 * El conjunto original de puntos discretizados para el ancho
	 */
	private ArrayList<Integer> P;
	/**
	 * El conjunto original de puntos discretizados para la altura
	 */
	private ArrayList<Integer> Q;
	/**
	 * El conjunto reducido de puntos discretizados para el ancho
	 */
	private ArrayList<Integer> P1;
	/**
	 * El conjunto reducido de puntos discretizados para el alto
	 */
	private ArrayList<Integer> Q1;

	/**
	 * El n�mero de piezas
	 */
	private int m;
	/**
	 * El ancho del compartimiento
	 */
	private int W;
	/**
	 * El alto del compartimiento
	 */
	private int H;
	/**
	 * Los itemes de la solución
	 */
	private int[][] item;
	/**
	 * La posici�n de los cortes guillotina en la solución
	 */
	private int[][] position;
	/**
	 * El tipo de corte guillotina en la solución
	 */
	private String[][] gullotine;
	/**
	 * El valor del item más grande para cada punto de discretización en la
	 * solución
	 */
	private double[][] V;

	/**
	 * Define si se debe imprimir o no el procedimiento del algoritmo
	 */
	private boolean print;

	/**
	 * Cantidad de items en la solución
	 */
	private ArrayList<Integer> amountOfItems;

	/**
	 * Los items encontrados en la columna
	 */
	private ArrayList<ItemSolucion> items = new ArrayList<ItemSolucion>();

	/**
	 * El espesor del item
	 */
	private int espesor;

	/**
	 * Las indicaciones de cómo cortar la contenedora
	 */
	private String instructions;

	/**
	 * Contruye una nueva instancia del problema auxiliar con los parametros
	 * dados
	 * 
	 * @param W
	 *            el ancho de la contenedora
	 * @param H
	 *            el alto de la contenedora
	 * @param E
	 *            el espesor de la contenedora
	 * @param w
	 *            los anchos de los items
	 * @param h
	 *            los altos de los items
	 * @param v
	 *            los valores de los items correspondientes a las variables
	 *            daules
	 * @param print
	 *            si se debe imprimir el proceso o no (útil para hacer debug)
	 */
	public ProblemaAuxiliarDP(int W, int H, int E, ArrayList<Integer> w,
			ArrayList<Integer> h, ArrayList<Double> v, boolean print) {
		this.instructions = "";
		this.espesor = E;
		this.items = new ArrayList<ItemSolucion>();
		this.W = W;
		this.H = H;
		this.h = h;
		this.w = w;
		this.v = v;
		this.m = w.size();
		this.print = print;

		addRotations();

		if (print) {
			System.out.println("Instance: B = (" + W + "," + H + ")");
			printItems();
		}
	}

	/**
	 * Inicia la ejecución del problema auxiliar
	 */
	@Override
	public void run() {

		long start = System.currentTimeMillis();
		generateDP();

		if (print) {
			System.out.println();
			System.out.println("Loading model");
		}

		P1 = new ArrayList<Integer>();
		Q1 = new ArrayList<Integer>();

		int wmin = getMinimumInteger(w);
		int hmin = getMinimumInteger(h);

		for (int i : P) {
			if (i <= W - wmin)
				P1.add(i);
		}
		P1.add(W);

		for (int i : Q) {
			if (i <= H - hmin)
				Q1.add(i);
		}
		Q1.add(H);

		if (print)
			printDP(P1, Q1);
		if (print)
			System.out.println();
		if (print)
			System.out.println("Running model");

		int r = P1.size();
		int s = Q1.size();
		V = new double[r][s];
		item = new int[r][s];
		position = new int[r][s];
		gullotine = new String[r][s];

		for (int i = 0; i < r; i++) {
			for (int j = 0; j < s; j++) {
				V[i][j] = getMaximumValueFor(P1.get(i), Q1.get(j));
				item[i][j] = getItemFor(P1.get(i), Q1.get(j), V[i][j]);
				gullotine[i][j] = "";
			}
		}

		for (int i = 1; i < r; i++) {
			for (int j = 1; j < s; j++) {
				int n = nValue(P1, i);
				for (int x = 0; x <= n; x++) {
					int t = tValue(P1, i, x);
					if (V[i][j] < V[x][j] + V[t][j]) {
						V[i][j] = V[x][j] + V[t][j];
						position[i][j] = P1.get(x);
						gullotine[i][j] = "Vertical";
					}
				}
				n = nValue(Q1, j);
				for (int y = 0; y <= n; y++) {
					int t = tValue(Q1, j, y);
					if (V[i][j] < V[i][y] + V[i][t]) {
						V[i][j] = V[i][y] + V[i][t];
						position[i][j] = Q1.get(y);
						gullotine[i][j] = "Horizontal";
					}
				}
			}
		}

		if (print) {
			System.out.println();
			System.out.println("Solution found");
			System.out.println();
			System.out.println("Optimal solution value: " + V[r - 1][s - 1]);
		}

		amountOfItems = new ArrayList<Integer>();

		for (@SuppressWarnings("unused")
		int i : w) {
			amountOfItems.add(0);
		}

		// Llena el arreglo con la solución
		getItemsInSolution(amountOfItems, r - 1, s - 1, 0, 0);

		if (print) {
			for (int i = 0; i < amountOfItems.size(); i++) {
				System.out.println("Item " + i + ": " + amountOfItems.get(i));
			}
		}

		long end = System.currentTimeMillis();
		double totalTimeSeconds = (end - start) / (1000.0);
		if (print)
			System.out.println("Total time elapsed: " + totalTimeSeconds
					+ " seconds");
	}

	@Override
	public ArrayList<Integer> darCoeficientesPatron() {
		ArrayList<Integer> items = new ArrayList<Integer>();
		for (int i = 0; i < m / 2; i++) {
			items.add(amountOfItems.get(i) + amountOfItems.get(i + m / 2));
		}
		return items;
	}

	@Override
	public double darValorPatron() {
		int r = P1.size();
		int s = Q1.size();
		return V[r - 1][s - 1];
	}

	/**
	 * Retorna el valor de t que se usa a lo largo del algoritmo DP
	 * 
	 * @param D1
	 *            el arreglo de donde se quiere sacar encontrar t
	 * @param i
	 *            el valor actual de i o j en el algoritmo
	 * @param x
	 *            el valor actual de x o y en el algoritmo
	 * @return el valor de t
	 */
	private int tValue(ArrayList<Integer> D1, int i, int x) {
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (int k = 0; k <= D1.size() - 1; k++) {
			if (D1.get(k) <= D1.get(i) - D1.get(x))
				values.add(k);
		}
		return getMaximumInteger(values);
	}

	/**
	 * Retorna el valor de n que se pregunta constantemente en el algoritmo
	 * 
	 * @param D1
	 *            el arreglo de donde se quiere encontrar n
	 * @param i
	 *            el valor actual de i
	 * @return el valor de n
	 */
	private int nValue(ArrayList<Integer> D1, int i) {
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (int k = 0; k <= i; k++) {
			if (D1.get(k) <= Math.floor(D1.get(i) / 2))
				values.add(k);
		}
		return getMaximumInteger(values);
	}

	/**
	 * Encuentra el indice del item más grande que (p,q) con valor v
	 * 
	 * @param p
	 *            el ancho máximo
	 * @param q
	 *            el alto máximo
	 * @param V
	 *            el valor v dado
	 * @return el índice más grande
	 */
	private int getItemFor(int p, int q, double V) {
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (int k = 0; k < m; k++) {
			if (w.get(k) <= p && h.get(k) <= q && v.get(k) == V)
				values.add(k);
		}
		values.add(-1);
		return getMaximumInteger(values);
	}

	/**
	 * Encuentra valor del item más grande menor a p y q
	 * 
	 * @param p
	 *            el alto máximo
	 * @param q
	 *            el ancho máximo
	 * @return el valor del item más grande
	 */
	private double getMaximumValueFor(int p, int q) {
		ArrayList<Double> values = new ArrayList<Double>();
		for (int k = 0; k < m; k++) {
			if (w.get(k) <= p && h.get(k) <= q)
				values.add(v.get(k));
		}
		values.add(0.0);
		return getMaximum(values);
	}

	/**
	 * Imprime los puntos de discretización reducidos
	 * 
	 * @param P
	 *            el arreglo de puntos de discretización para el ancho
	 * @param Q
	 *            el arreglo de puntos de discretización para el ancho
	 */
	private void printDP(ArrayList<Integer> P, ArrayList<Integer> Q) {
		System.out.println();
		System.out.println("REDUCED DP");
		System.out.println();
		String v = "P1={";
		for (int r : P) {
			v += r + ",";
		}
		v += ("}");
		System.out.println(v.replace(",}", "}"));

		v = "Q1={";
		for (int r : Q) {
			v += r + ",";
		}
		v += "}";
		System.out.println(v.replace(",}", "}"));

	}

	/**
	 * Imprime los puntos de discretización
	 */
	private void printDP() {
		System.out.println();
		System.out.println("DP");
		System.out.println();
		String v = "P={";
		for (int r : P) {
			v += r + ",";
		}
		v += ("}");
		System.out.println(v.replace(",}", "}"));

		v = "Q={";
		for (int r : Q) {
			v += r + ",";
		}
		v += "}";
		System.out.println(v.replace(",}", "}"));

	}

	/**
	 * Genera los puntos de discretización dado los items
	 */
	private void generateDP() {

		ArrayList<Integer> P = new ArrayList<Integer>();
		ArrayList<Integer> d = h;
		int D = H;
		int c[] = new int[D + 1];

		// GENERA W
		P.add(0);
		for (int j = 0; j <= D; j++) {
			c[j] = 0;
		}

		for (int i = 0; i < m; i++) {
			for (int j = d.get(i); j <= D; j++) {
				if (c[j] < c[j - d.get(i)] + d.get(i))
					c[j] = c[j - d.get(i)] + d.get(i);
			}
		}

		for (int j = 1; j <= D; j++) {
			if (c[j] == j)
				P.add(j);
		}

		// ASIGNA W
		this.Q = P;

		// ----------------------------------------------------

		P = new ArrayList<Integer>();
		d = w;
		D = W;
		c = new int[D + 1];

		// GENERA L
		P.add(0);
		for (int j = 0; j <= D; j++) {
			c[j] = 0;
		}

		for (int i = 0; i < m; i++) {
			for (int j = d.get(i); j <= D; j++) {
				if (c[j] < c[j - d.get(i)] + d.get(i))
					c[j] = c[j - d.get(i)] + d.get(i);
			}
		}

		for (int j = 1; j <= D; j++) {
			if (c[j] == j)
				P.add(j);
		}
		this.P = P;

		if (print)
			printDP();
	}

	/**
	 * Imprime los items
	 */
	private void printItems() {

		System.out.println();
		System.out.println("Random generated items");
		System.out.println();
		for (int i = 0; i < w.size(); i++) {
			System.out.println(i + ".(" + w.get(i) + "," + h.get(i) + "): "
					+ v.get(i));
		}

	}

	/**
	 * Retorna la cantidad de items dado un tama�o de contenedora y la posici�n
	 * de los mismos
	 * 
	 * @param amountOfItems
	 *            el arreglo donde se almacenará la solución
	 * @param x
	 *            el ancho del compartimiento
	 * @param y
	 *            el alto del compartimiento
	 */
	private void getItemsInSolution(ArrayList<Integer> amountOfItems, int x,
			int y, int posx, int posy) {
		if (print)
			System.out.print("Evaluating a " + P1.get(x) + "x" + Q1.get(y)
					+ " bin:");
		/**
		 * Finds an item of the exact size
		 */
		if (gullotine[x][y] == "") {
			if (item[x][y] > -1) {
				int index = (item[x][y]);
				if (print)
					System.out.println("Item " + item[x][y]
							+ " found at position (" + posx + "," + posy + ")");

				int amount = amountOfItems.get(index);
				amountOfItems.remove(index);
				amountOfItems.add(index, amount + 1);

				Item representado = Principal.darItem(w.get(item[x][y]),
						h.get(item[x][y]), espesor);

				if (representado != null)
					items.add(new ItemSolucion(posx, posy, representado,
							ItemSolucion.ORIGINAL));
				else
					items.add(new ItemSolucion(posx, posy, Principal.darItem(
							h.get(item[x][y]), w.get(item[x][y]), espesor),
							ItemSolucion.INVERTIDA));
			}
		}
		/**
		 * Considers a cut made on the stock either horizontal or vertical
		 */
		else {
			String direction = gullotine[x][y];
			int cutPosition = position[x][y];
			instructions += "instr Corte " + direction.toLowerCase() +  " en " + cutPosition + " mm de la lámina " + P1.get(x) + "x"
					+ Q1.get(y) + " mm" + '\n';

			/**
			 * If a horizontal cut is made
			 */
			if (direction == "Horizontal") {
				if (print)
					System.out.print(" a horizontal cut must be made on "
							+ cutPosition);
				if (print)
					System.out.println(": " + P1.get(x) + "x" + cutPosition
							+ " , " + P1.get(x) + "x"
							+ (Q1.get(y) - cutPosition));

				if (Q1.indexOf(Q1.get(y) - cutPosition) == -1) {
					if (print)
						System.out
								.println('\t'
										+ "> Waste of "
										+ P1.get(x)
										+ "x"
										+ ((Q1.get(y) - cutPosition) - Q1
												.get(getIndexOfBiggestDPSmallerThan(
														(Q1.get(y) - cutPosition),
														Q1)))
										+ " at position (" + posx + ","
										+ (posy + cutPosition) + ")");

				}

				/**
				 * The first sheet cut
				 */
				getItemsInSolution(amountOfItems, x, Q1.indexOf(cutPosition),
						posx, posy);

				/**
				 * The second sheet cut
				 */
				if (Q1.indexOf(Q1.get(y) - cutPosition) != -1)
					getItemsInSolution(amountOfItems, x,
							Q1.indexOf(Q1.get(y) - cutPosition), posx, posy
									+ cutPosition);
				else {
					int indexOfBigger = getIndexOfBiggestDPSmallerThan(
							Q1.get(y) - cutPosition, Q1);
					getItemsInSolution(
							amountOfItems,
							x,
							indexOfBigger,
							posx,
							posy
									+ cutPosition
									+ (Q1.get(y) - cutPosition - Q1
											.get(indexOfBigger)));
				}

			}
			/**
			 * Considers a vertical cut
			 */
			else if (direction == "Vertical") {
				if (print)
					System.out.print(" a vertical cut must be made on "
							+ cutPosition);
				if (print)
					System.out.println(": " + cutPosition + "x" + Q1.get(y)
							+ " , " + (P1.get(x) - cutPosition) + "x"
							+ Q1.get(y));

				if (P1.indexOf(P1.get(x) - cutPosition) == -1) {
					if (print)
						System.out
								.println('\t'
										+ "> Waste of "
										+ ((P1.get(x) - cutPosition) - P1
												.get(getIndexOfBiggestDPSmallerThan(
														(P1.get(x) - cutPosition),
														P1))) + "x" + Q1.get(y)
										+ " at position ("
										+ (cutPosition + posx) + "," + (posy)
										+ ")");

				}

				/**
				 * The first sheet is cut
				 */
				getItemsInSolution(amountOfItems, P1.indexOf(cutPosition), y,
						posx, posy);

				/**
				 * The second sheet
				 */
				if (P1.indexOf(P1.get(x) - cutPosition) != -1)
					getItemsInSolution(amountOfItems,
							P1.indexOf(P1.get(x) - cutPosition), y, posx
									+ cutPosition, posy);
				else {
					int indexOfBigger = getIndexOfBiggestDPSmallerThan(
							P1.get(x) - cutPosition, P1);
					getItemsInSolution(
							amountOfItems,
							indexOfBigger,
							y,
							posx
									+ cutPosition
									+ (P1.get(x) - cutPosition - P1
											.get(indexOfBigger)), posy);
				}
			}
		}
	}

	/**
	 * Agrega las rotaciones a los items originales
	 */
	private void addRotations() {
		int initialAmount = w.size();
		for (int i = 0; i < initialAmount; i++) {
			h.add(w.get(i));
			w.add(h.get(i));
			v.add(v.get(i));
			this.m++;
		}
	}

	/**
	 * Retorna el índice del punto de discretización más pequeño que la
	 * dimensión dada
	 * 
	 * @param size
	 *            la dimensión máxima
	 * @param d
	 *            la colección de puntos de discretización apropiada para la
	 *            dimensión
	 * @return el índice del punto de discretización más grande
	 */
	private int getIndexOfBiggestDPSmallerThan(int size, ArrayList<Integer> d) {
		int max = -1;
		for (int i = 0; i < d.size() && d.get(i) <= size; i++) {
			if (d.get(i) > max) {
				max = i;
			}
		}

		return max;
	}

	/**
	 * Retorna el máximo valor de una colección de doubles
	 * 
	 * @param valores
	 *            la colección de doubles
	 * @return el máximo valor, si valores es vacía entonces retorna -10
	 */
	private double getMaximum(ArrayList<Double> valores) {
		double max = -10;

		for (int i = 0; i < valores.size(); i++) {
			if (valores.get(i) > max)
				max = valores.get(i);
		}

		return max;
	}

	/**
	 * Retorna el minimo entero de la colección de enteros
	 * 
	 * @param valores
	 *            la colección de enteros
	 * @return el minimo entero, si es vacía. Entonces retorna infinito
	 */
	private int getMinimumInteger(ArrayList<Integer> valores) {
		int min = Integer.MAX_VALUE;

		for (int i = 0; i < valores.size(); i++) {
			if (valores.get(i) < min)
				min = valores.get(i);
		}

		return min;
	}

	private void modifyInstructions ()
	{
		int  instruccion = 1;
		while(instructions.contains("instr"))
		{			
			instructions = instructions.replaceFirst("instr", instruccion+".");
			instruccion++;
		}
	}
	
	/**
	 * Retorna el entero de mayor valor
	 * 
	 * @param valores
	 *            la colección de enteros donde se busca el máximo
	 * @return el máximo valor, si valores es vacío retorna -10
	 */
	private int getMaximumInteger(ArrayList<Integer> valores) {
		int max = -10;

		for (int i = 0; i < valores.size(); i++) {
			if (valores.get(i) > max)
				max = valores.get(i);
		}

		return max;
	}

	/**
	 * Da el patron generado por el problema auxiliar
	 */
	@Override
	public IPatron darPatron() {
		modifyInstructions();
		IPatron respuesta = new Patron2D(items,
				Principal.darCostoContenedoraPorEspesor(espesor),
				Principal.darIDPatron(), -1,instructions);
		return respuesta;
	}

}
