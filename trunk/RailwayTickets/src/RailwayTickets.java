import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class RailwayTickets {

	private int l1, l2, l3, c1, c2, c3;
	private int[] distancias;
	private int inicio;
	private int fin;

	public RailwayTickets(String rutaArchivoParametros, boolean cargarDeConsola) {
		try {
			if(!cargarDeConsola)
				cargarParametros(rutaArchivoParametros);
			else
				cargarDesdeConsola();
			resolver();
		} catch (Exception e) {
			System.out.println("Error leyendo el archivo de parámteros");
			e.printStackTrace();
		}
		
	}

	private int darPosicion(int i) {
		return i - inicio;
	}

	private void resolver() {
		int n = Math.abs(inicio - fin);
		int[][] c = new int[n + 1][n + 1];

		for (int i = 0; i < n + 1; i++) {
			for (int j = 0; j < n + 1; j++) {
				if (i != j)
					c[i][j] = Integer.MAX_VALUE;
				else
					c[i][j] = 0;
			}
		}

		for (int salto = 1; salto <= n; salto++) {
			for (int i = inicio; i < fin; i++) {
				if (i + salto <= fin) {
					int posicionInicio = darPosicion(i);
					int posicionFin = darPosicion(i + salto);

					c[posicionInicio][posicionFin] = darMejorCosto(i,
							i + salto, c);
				} else
					break;
			}
		}

		System.out.println(c[0][fin - inicio]);
	}

	/**
	 * Da el mejor costo
	 * 
	 * @param inicio
	 * @param fin
	 * @param c
	 * @return
	 */
	private int darMejorCosto(int inicio, int fin, int[][] c) {
		int minimo = darCostoTrayectoDirecto(inicio, fin);
		int posicionInicio = darPosicion(inicio);
		int posicionFin = darPosicion(fin);

		for (int i = posicionInicio + 1; i < posicionFin; i++) {
			if (c[posicionInicio][i] + c[i][posicionFin] < minimo)
				minimo = c[posicionInicio][i] + c[i][posicionFin];
		}

		return minimo;
	}

	private int darDistancia(int inicio, int fin) {
		return Math.abs(distancias[fin] - distancias[inicio]);
	}

	private void cargarDesdeConsola() {
		Scanner in = new Scanner(System.in);
		l1 = in.nextInt();
		l2 = in.nextInt();
		l3 = in.nextInt();

		c1 = in.nextInt();
		c2 = in.nextInt();
		c3 = in.nextInt();
		
		in.nextLine();
		int n = in.nextInt();
		
		in.nextLine();
		inicio = in.nextInt()-1;
		fin = in.nextInt()-1;
		
		if (fin < inicio) {
			int aux = fin;
			fin = inicio;
			inicio = aux;
		}
		
		distancias = new int[n];
		distancias[0] = 0;
		
		for (int i = 1; i < n; i++) {
			in.nextLine();
			distancias[i] = in.nextInt();
		}
		
	}

	/**
	 * Da el costo del del trayecto directo desde el inicio al fin
	 * 
	 * @param inicio
	 *            0<= inicio < n
	 * @param fin
	 *            0<= fin < n
	 * @return el costo del trayecto directo, si no es posible entonces retorna
	 *         infinito
	 */
	private int darCostoTrayectoDirecto(int inicio, int fin) {
		int distancia = darDistancia(inicio, fin);
		if (0 < distancia && distancia <= l1)
			return c1;
		else if (l1 < distancia && distancia <= l2)
			return c2;
		else if (l2 < distancia && distancia <= l3)
			return c3;
		else
			return Integer.MAX_VALUE;
	}

	private void cargarParametros(String rutaArchivoParametros)
			throws IOException {

		File archivoParametros = new File(rutaArchivoParametros);
		FileReader in = new FileReader(archivoParametros);
		BufferedReader br = new BufferedReader(in);

		String linea = br.readLine();
		String[] partes = linea.split(" ");

		l1 = Integer.parseInt(partes[0]);
		l2 = Integer.parseInt(partes[1]);
		l3 = Integer.parseInt(partes[2]);

		c1 = Integer.parseInt(partes[3]);
		c2 = Integer.parseInt(partes[4]);
		c3 = Integer.parseInt(partes[5]);

		linea = br.readLine();
		int n = Integer.parseInt(linea);
		distancias = new int[n];
		distancias[0] = 0;

		linea = br.readLine();
		inicio = Integer.parseInt(linea.split(" ")[0]) - 1;
		fin = Integer.parseInt(linea.split(" ")[1]) - 1;

		// Si la estación de inicio está antes de la de fin, entonces se hace un
		// swap
		if (fin < inicio) {
			int aux = fin;
			fin = inicio;
			inicio = aux;
		}

		for (int i = 1; i < n; i++) {
			linea = br.readLine();
			distancias[i] = Integer.parseInt(linea);
		}

	}

	public static void main(String[] arg0s) {
		new RailwayTickets("./data/instancia1.txt",true);
	}
}
