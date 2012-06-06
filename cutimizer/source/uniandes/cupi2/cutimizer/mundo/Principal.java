/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación 
 * Licenciado bajo el esquema Academic Free License version 2.1 
 *
 * Proyecto Cupi2 (http://cupi2.uniandes.edu.co)
 * Ejercicio: n18_cutimizer
 * Autor: Jorge Sierra - 11-feb-2012
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.cupi2.cutimizer.mundo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import uniandes.cupi2.cutimizer.graficas.PoolDeColores;
import uniandes.cupi2.cutimizer.interfaz.InterfazSolucionConsola;
import uniandes.cupi2.cutimizer.mundo.interfaces.IPatron;
import uniandes.cupi2.cutimizer.mundo.interfaces.IProblemaAuxiliar;
import uniandes.cupi2.cutimizer.mundo.interfaces.IProblemaMaestro;
import uniandes.cupi2.cutimizer.mundo.problemaauxiliar.ProblemaAuxiliarDP;
import uniandes.cupi2.cutimizer.mundo.problemamaestro.ProblemaMaestroGLPK;
import uniandes.cupi2.cutimizer.mundo.problemamaestro.ProblemaMaestroGurobi;
import uniandes.cupi2.cutimizer.mundo.problemamaestro.ProblemaMaestroLPSOLVE;

/**
 * @author SIERRRA
 */
public class Principal {
	// -----------------------------------------------------------------
	// Constantes
	// -----------------------------------------------------------------
	/**
	 * La tolerancia para considerar un valor como cero
	 */
	public static final double TOLERANCIA = -1E-6;

	/**
	 * La ruta del archivo de propiedades que definen las contenedoras
	 */
	private static final String RUTA_ARCHIVO_INICIALIZACION = "./data/laminas.properties";
	// -----------------------------------------------------------------
	// Atributos
	// -----------------------------------------------------------------

	/**
	 * Modela las items de sub láminas agregadas
	 */
	private static ArrayList<Item> items;
	/**
	 * Las láminas que debe ser cortadas
	 */
	private static ArrayList<Contenedora> contenedoras;

	/**
	 * Una instancia del problema maestro asociado
	 */
	private IProblemaMaestro problemaMaestro;

	/**
	 * Modela el número de contenedores de los cuales se puede disponer
	 */
	private int numeroContenedoras;

	/**
	 * Maneja las soluciones que han sido cargadas por medio de un archivo o
	 * solucionando un modelo
	 */
	private ArrayList<Solucion> solucionesCargadas;

	/**
	 * La identificación actual del item
	 */
	private int idItem;

	/**
	 * El id actual de los patrones
	 */
	private static int id;

	// -----------------------------------------------------------------
	// Constructores
	// -----------------------------------------------------------------

	/**
	 * Crea una nueva instancia de la clase principal de la aplicación.
	 * 
	 * @throws Exception
	 */
	public Principal() throws Exception {
		id = 0;
		solucionesCargadas = new ArrayList<Solucion>();
		// Crea el arreglo con las items vacíndiceas.
		items = new ArrayList<Item>();
		// Crea el arreglo de láminas a usar vacíndiceo.
		contenedoras = new ArrayList<Contenedora>();
		// Carga las láminas originales
		cargarContenedoras();

	}

	// -----------------------------------------------------------------
	// Métodos
	// -----------------------------------------------------------------

	/**
	 * Crea una instancia del problema maestro
	 * 
	 * @throws Exception
	 *             si existe algún problema con el optimizador
	 */
	@SuppressWarnings("unused")
	private void crearProblemaMaestro() throws Exception {
		// Crea un nuevo problema maestro
		int tipo = IProblemaMaestro.RELAJACION_LINEAL;

		System.out
				.println("------------------------ GLPK ---------------------------------");
		problemaMaestro = ProblemaMaestroGLPK.darInstancia(items, contenedoras);
		problemaMaestro.resolver(tipo);
		System.out.println("FO: " + problemaMaestro.darValorFO());
		System.out.println(problemaMaestro.darValorVariablesDecision()
				.toString());
		if (tipo == IProblemaMaestro.RELAJACION_LINEAL)
			System.out.println(problemaMaestro.darDuales().toString());
		System.out
				.println("------------------------ GUROBI ---------------------------------");
		IProblemaMaestro problemaMaestro2 = ProblemaMaestroGurobi.darInstancia(
				items, contenedoras);
		problemaMaestro2.resolver(tipo);
		System.out.println("FO: " + problemaMaestro2.darValorFO());
		System.out.println(problemaMaestro2.darValorVariablesDecision()
				.toString());
		if (tipo == IProblemaMaestro.RELAJACION_LINEAL)
			System.out.println(problemaMaestro2.darDuales().toString());

		System.out
				.println("------------------------ LPSOLVE ---------------------------------");
		IProblemaMaestro problemaMaestro3 = ProblemaMaestroLPSOLVE
				.darInstancia(items, contenedoras);
		problemaMaestro3.resolver(tipo);
		System.out.println("FO: " + problemaMaestro3.darValorFO());
		System.out.println(problemaMaestro3.darValorVariablesDecision()
				.toString());
		if (tipo == IProblemaMaestro.RELAJACION_LINEAL)
			System.out.println(problemaMaestro3.darDuales().toString());

		ArrayList<Integer> coeficientes = new ArrayList<Integer>();
		for (int i = 0; i < items.size(); i++) {
			coeficientes.add(items.get(i).getCantidad());
		}

		System.out
				.println("------------------------GLPK 2---------------------------------");
		problemaMaestro.agregarNuevaVariable(1, coeficientes);
		problemaMaestro.resolver(tipo);
		System.out.println("FO: " + problemaMaestro.darValorFO());
		System.out.println(problemaMaestro.darValorVariablesDecision()
				.toString());
		if (tipo == IProblemaMaestro.RELAJACION_LINEAL)
			System.out.println(problemaMaestro.darDuales().toString());

		System.out
				.println("------------------------GUROBI 2---------------------------------");
		problemaMaestro2.agregarNuevaVariable(1, coeficientes);
		problemaMaestro2.resolver(tipo);
		System.out.println("FO: " + problemaMaestro2.darValorFO());
		System.out.println(problemaMaestro2.darValorVariablesDecision()
				.toString());
		if (tipo == IProblemaMaestro.RELAJACION_LINEAL)
			System.out.println(problemaMaestro2.darDuales().toString());

		System.out
				.println("------------------------LPSOLVE 2---------------------------------");
		problemaMaestro3.agregarNuevaVariable(1, coeficientes);
		problemaMaestro3.resolver(tipo);
		System.out.println("FO: " + problemaMaestro3.darValorFO());
		System.out.println(problemaMaestro3.darValorVariablesDecision()
				.toString());
		if (tipo == IProblemaMaestro.RELAJACION_LINEAL)
			System.out.println(problemaMaestro3.darDuales().toString());

		ProblemaMaestroLPSOLVE.eliminarInstancia();
		ProblemaMaestroGurobi.eliminarInstancia();
		ProblemaMaestroGLPK.eliminarInstancia();
	}

	// -----------------------------------------------------------------
	// Puntos de Extensión
	// -----------------------------------------------------------------
	/**
	 * Carga las láminas originales que se pueden usar del archivo de
	 * propiedades
	 * 
	 * @throws Exception
	 *             si ocurre algún problema con la inicialización del archivo de
	 *             propiedades
	 */
	private void cargarContenedoras() throws Exception {
		File arch = new File(RUTA_ARCHIVO_INICIALIZACION);

		Properties datos = new Properties();
		FileInputStream in = new FileInputStream(arch);
		try {
			datos.load(in);
			in.close();
		} catch (Exception e) {
			throw new Exception("Formato inválido");
		}

		numeroContenedoras = Integer.parseInt(datos
				.getProperty("cutimizer.laminas"));
		for (int i = 0; i < numeroContenedoras; i++) {
			int alto = Integer.parseInt(datos.getProperty("cutimizer.lamina"
					+ i + ".alto"));
			int ancho = Integer.parseInt(datos.getProperty("cutimizer.lamina"
					+ i + ".ancho"));
			int espesor = Integer.parseInt(datos.getProperty("cutimizer.lamina"
					+ i + ".espesor"));
			Double precio = Double.parseDouble(datos
					.getProperty("cutimizer.lamina" + i + ".precio"));
			Contenedora nueva = new Contenedora(ancho, alto, espesor, precio);
			contenedoras.add(nueva);
		}
	}

	/**
	 * Agrega una item nueva a la lista de items. Si ya existe una item con
	 * alto, ancho y espesor igual entonces actualiza la cantidad de esa item
	 * 
	 * @param ancho
	 *            de el item que se quiere agregar
	 * @param alto
	 *            de el item que se quiere agregar
	 * @param espesor
	 *            de el item que se quiere agregar
	 * @param cantidad
	 *            de el item que se quiere agregar
	 * @throws Exception
	 *             si el item que se quiere agregar ya existe.
	 */
	public void agregarItem(int ancho, int alto, int espesor, int cantidad)
			throws Exception {

		Item duplicada = verificarDuplicados(ancho, alto, espesor);
		Contenedora correspondiente = darContenedoraConEspesor(espesor);
		if (duplicada == null && correspondiente!=null) {
			if(alto<= correspondiente.getAlto() && ancho <= correspondiente.getAncho())
			{
				Item nueva = new Item(ancho, alto, espesor, cantidad, PoolDeColores.darInstancia().darColor(), darIDItem());
				items.add(nueva);
			}
			else if (alto<=correspondiente.getAncho() && ancho <= correspondiente.getAlto())
			{
				Item nueva = new Item(alto, ancho, espesor, cantidad, PoolDeColores.darInstancia().darColor(), darIDItem());
				items.add(nueva);
			}
			else
				throw new Exception ("El item no cabe dentro de ninguna de las contenedoras disponibles");
		} 
		else if(duplicada!=null) {
			duplicada.setCantidad(duplicada.getCantidad() + cantidad);
			// throw new Exception(
			// "Ya existía una item igual. Se ha aumentado el item existente con la cantidad adicional.");
		}
	}
	
	/**
	 * Retorna la contenedora con espesor dado si existe
	 * @param espesor el espesor buscado
	 * @return la contenedora, de lo contrario null
	 */
	public static Contenedora darContenedoraConEspesor(int espesor)
	{
		for(Contenedora contenedora: contenedoras)
		{
			if(contenedora.getEspesor()==espesor)
				return contenedora;
		}
		
		return null;
	}
	
	
	/**
	 * Le asigna un id único a los items
	 * 
	 * @return
	 */
	private int darIDItem() {
		return idItem += 1;
	}

	/**
	 * Elimina una item de la lista de items <br/>
	 * <b>pre:</b> El item que se quiere eliminar existe y el índicen dice es el
	 * adecuado adecuado
	 * 
	 * @param index
	 *            el índice del item que se quiere eliminar
	 */
	public void eliminarItem(int index) {
		items.remove(index);
	}

	/**
	 * Da el costo de la contenedora con espesor indica
	 * 
	 * @param espesor
	 *            el espesor en mm
	 * @return el costo de la contenedora
	 */
	public static double darCostoContenedoraPorEspesor(int espesor) {
		for (Contenedora contenedora : contenedoras) {
			if (contenedora.getEspesor() == espesor)
				return contenedora.getPrecio();
		}
		return 0;
	}

	/**
	 * Retorna una item que ya ha sido agregada
	 * 
	 * @param ancho
	 *            del item que se quiere verificar
	 * @param alto
	 *            del item que se quiere verificar
	 * @param espesor
	 *            del item que se quiere verificar
	 * @return null si no hay una item ya existente, o el item de si ya existe
	 */
	private Item verificarDuplicados(double ancho, double alto, double espesor) {
		Item duplicada = null;
		for (int i = 0; i < items.size(); i++) {
			if ((items.get(i).getAlto() == alto
					&& items.get(i).getAncho() == ancho && items.get(i)
					.getEspesor() == espesor)
					|| (items.get(i).getAlto() == ancho
					&& items.get(i).getAncho() == alto
					&& items.get(i).getEspesor() == espesor)) {
				duplicada = items.get(i);
				break;
			}
		}
		return duplicada;
	}

	public void cargarDeArchivoInstancia(String ruta) {
		File f = new File(ruta);
		FileReader fr;
		try {
			fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			String linea = br.readLine();
			linea = br.readLine();
			while (linea != null && !linea.equals("")) {
				// System.out.println(linea);
				String valores[] = linea.split("	");
				int espesor = Integer.parseInt(valores[0].split("mm")[0]);
				int cantidad = Integer.parseInt(valores[1]);
				int ancho = Integer.parseInt(valores[2]);
				int alto = Integer.parseInt(valores[3]);

				// System.out.println(ancho + " " + alto + " " + espesor + " "
				// + cantidad);

				agregarItem(ancho, alto, espesor, cantidad);

				linea = br.readLine();
			}

			br.close();
			fr.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void main(String[] arg0) {
		try {
			Principal principal = new Principal();
			/*
			 * principal.agregarDemandas(2300, 600, 15, 117*2);
			 * principal.agregarDemandas(600, 600, 15, 117*2);
			 * principal.agregarDemandas(400, 400, 15, 117*4);
			 * principal.agregarDemandas(75, 700, 15, 117*2);
			 * principal.agregarDemandas(75, 550, 15, 117*2);
			 * principal.agregarDemandas(400, 730, 15, 117*2);
			 * principal.agregarDemandas(75, 700, 9, 117*4);
			 * principal.agregarDemandas(730, 2300, 4, 117*1);
			 * 
			 * /* principal.agregarDemandas(800, 350, 15, 3*117);
			 * principal.agregarDemandas(1430, 350, 15, 1*117);
			 * principal.agregarDemandas(700, 350, 15, 2*117);
			 * principal.agregarDemandas(723, 423, 15, 4*117);
			 * principal.agregarDemandas(1400, 800, 4, 1*117);
			 */
			principal.cargarDeArchivoInstancia("./data/instancia10.txt");

			principal.generacionColumnas("Generacion 1");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inicia la generación de columna con los datos de los items
	 * 
	 * @throws Exception
	 *             si ocurre algún problema con la generación de columnas
	 */
	public void generacionColumnas(String nombre) throws Exception {

		ArrayList<IPatron> patrones = new ArrayList<IPatron>();

		// Agrega los patrones iniciales a la lista de patrones
		for (Item i : items) {
			ArrayList<ItemSolucion> itemSolucions = new ArrayList<ItemSolucion>();

			ItemSolucion item = new ItemSolucion(0, 0, i, ItemSolucion.ORIGINAL);
			itemSolucions.add(item);

			IPatron patron = new Patron2D(itemSolucions,
					darCostoContenedoraPorEspesor(item.getRepresentado()
							.getEspesor()), darIDPatron(), -1);
			patrones.add(patron);
		}

		System.out.println("Iniciando la generación de columnas");
		long inicio = System.currentTimeMillis();

		int iter = 1;
		int tipo = IProblemaMaestro.RELAJACION_LINEAL;
		double cq = 1;
		int colGeneradas = 0;

		double foAuxiliares[] = new double[numeroContenedoras];
		IProblemaAuxiliar[] auxiliares = new ProblemaAuxiliarDP[numeroContenedoras];

		for (int i = 0; i < numeroContenedoras; i++) {
			foAuxiliares[i] = Double.MAX_VALUE;
		}

		while (hayCostosReducidosNegativos(cq, foAuxiliares)) {
			System.out.println();
			System.out.println();
			System.out.println("Iteración " + iter);

			/**
			 * ----------------------------------------------------------------
			 * Se resuelve el problema maestro
			 */
			problemaMaestro = ProblemaMaestroLPSOLVE.darInstancia(items,
					contenedoras);

			problemaMaestro.resolver(tipo);

			double fo = problemaMaestro.darValorFO();
			System.out.println("FO: " + fo);
			System.out.println(problemaMaestro.darValorVariablesDecision()
					.toString());

			ArrayList<Double> duales = problemaMaestro.darDuales();

			System.out.println(duales.toString());

			/**
			 * ----------------------------------------------------------------
			 * Se resuelven los problemas auxiliares
			 */

			// Se inicia la ejecución en hilos separados de cada problema
			// auxiliar y se espera que todos acaben
			for (int i = 0; i < numeroContenedoras; i++) {
				int Wr = contenedoras.get(i).getAncho();
				int Hr = contenedoras.get(i).getAlto();
				int Er = contenedoras.get(i).getEspesor();

				ArrayList<Integer> w = darAnchos(i);
				ArrayList<Integer> h = darAltos(i);
				ArrayList<Double> v = darValores(i, duales);

				auxiliares[i] = new ProblemaAuxiliarDP(Wr, Hr, Er, w, h, v,
						false);
				auxiliares[i].iniciar();
			}

			// Una vez terminada la ejecución de todos los problemas auxiliares
			// se procesa la solución
			// Se actualizan los valores de las funciones objetivos y se agregan
			// las columnas convenientes

			for (int i = 0; i < numeroContenedoras; i++) {
				foAuxiliares[i] = auxiliares[i].darValorPatron();
				if (cq - foAuxiliares[i] < TOLERANCIA) {
					ArrayList<Integer> columna = crearNuevaColumna(
							auxiliares[i].darCoeficientesPatron(), i);
					System.out.println();
					System.out.println("Se agrega la columna " + columna);
					problemaMaestro.agregarNuevaVariable(1, columna);
					System.out.println(contenedoras.get(i).getPrecio());
					patrones.add(auxiliares[i].darPatron());

					colGeneradas++;
				}

			}
			iter++;
		}
		double forelajada = problemaMaestro.darValorFO();

		File sol = new File("./data/sol instancia 15.txt");
		if (!sol.exists())
			sol.createNewFile();

		FileWriter fw = new FileWriter(sol);
		PrintWriter pw = new PrintWriter(fw);
		pw.println("Columnas generadas: " + colGeneradas);
		pw.println("FO relajada: " + forelajada);

		System.out.println();
		System.out.println();
		System.out
				.println("Resolviendo problema entero con las columnas generadas");
		/**
		 * Resuelve el problema entero con las columnas generadas
		 */
		tipo = IProblemaMaestro.ENTERO;
		problemaMaestro.resolver(tipo);

		Double fo = problemaMaestro.darValorFO();
		ArrayList<Double> vd = problemaMaestro.darValorVariablesDecision();
		System.out.println("FO: " + fo);
		System.out.println(vd.toString());

		pw.println("FO entera con columnas generadas: " + fo);
		pw.println("GAP: " + (fo - forelajada) * 100 / forelajada + "%");

		ArrayList<IPatron> patronesUsados = new ArrayList<IPatron>();

		for (int i = 0; i < vd.size(); i++) {

			int vdi = (int) Math.round(vd.get(i));
			if (vdi > 0) {
				patrones.get(i).asignarCantidad(vdi);
				patronesUsados.add(patrones.get(i));
			}

		}
		Solucion respuesta = new Solucion(new Date(), nombre, patronesUsados);
		System.out.println(respuesta.toString());

		solucionesCargadas.add(respuesta);

		System.out.println();
		pw.println("Tiempo de ejecución total "
				+ (System.currentTimeMillis() - inicio) / 1000 + " segundos");
		System.out.println("Tiempo de ejecución total "
				+ (System.currentTimeMillis() - inicio) / 1000 + " segundos");
		System.out.println("Fin del modelo");

		pw.close();
		fw.close();

		//respuesta.guardarImagenesSolucion("./data");
		new InterfazSolucionConsola(respuesta);
	}

	/**
	 * Da el id del patrón
	 * 
	 * @return el id del patrón
	 */
	public static int darIDPatron() {
		return id += 1;
	}

	/**
	 * Retorna la nueva columna con los coeficientes dados y ceros en las demás
	 * posiciones
	 * 
	 * @param coeficientes
	 *            el valor de los coeficientes para la columna
	 * @param contenedora
	 *            la contenedora a la que se refiere dichos indice
	 * @return la nueva columna a ser adicionada
	 */
	private ArrayList<Integer> crearNuevaColumna(
			ArrayList<Integer> coeficientes, int contenedora) {

		int indice = 0;
		int espesor = contenedoras.get(contenedora).getEspesor();

		ArrayList<Integer> columna = new ArrayList<Integer>();

		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getEspesor() == espesor) {
				columna.add(coeficientes.get(indice));
				indice++;
			} else
				columna.add(0);
		}

		return columna;
	}

	/**
	 * Retorna un arreglo de los altos de los items asociados a la contenedora
	 * 
	 * @param contenedora
	 *            la contenedora
	 * @return el arreglo de altos de los items
	 */
	private ArrayList<Integer> darAltos(int contenedora) {
		ArrayList<Integer> altos = new ArrayList<Integer>();
		double espesor = contenedoras.get(contenedora).getEspesor();
		for (Item i : items) {
			if (espesor == i.getEspesor())
				altos.add((int) i.getAlto());
		}
		return altos;
	}

	/**
	 * Retorna un arreglo con los valores de los items
	 * 
	 * @param contenedora
	 *            con los items asociados
	 * @param duales
	 *            el valor de las variables duales en la iteración actual
	 * @return arreglo con los valores de los itemes
	 */
	private ArrayList<Double> darValores(int contenedora,
			ArrayList<Double> duales) {
		ArrayList<Double> valores = new ArrayList<Double>();
		double espesor = contenedoras.get(contenedora).getEspesor();
		int ind = 0;

		for (Item i : items) {
			if (espesor == i.getEspesor())
				valores.add(duales.get(ind));

			ind++;
		}
		return valores;
	}

	/**
	 * Retorna un arreglo con los anchos de los items asociados a la contenedora
	 * 
	 * @param contenedora
	 *            la contenedora dada
	 * @return el arreglo con los anchos de los items
	 */
	private ArrayList<Integer> darAnchos(int contenedora) {
		ArrayList<Integer> anchos = new ArrayList<Integer>();
		double espesor = contenedoras.get(contenedora).getEspesor();
		for (Item i : items) {
			if (espesor == i.getEspesor())
				anchos.add((int) i.getAncho());
		}
		return anchos;
	}

	/**
	 * Retorna el item con dimensionadas dadas de lo contrario retorna null
	 * 
	 * @param ancho
	 * @param alto
	 * @param espesor
	 * @return el item buscado o null si no existe
	 */
	public static Item darItem(int ancho, int alto, int espesor) {
		for (Item i : items) {
			if (i.getAlto() == alto && i.getAncho() == ancho
					&& i.getEspesor() == espesor)
				return i;
		}

		return null;
	}

	/**
	 * Retorna verdadero si hay costos reducidos negativos y el método debe
	 * continuar
	 * 
	 * @param cq
	 *            el costo de la función objetivo actual
	 * @param foAuxiliares
	 *            los costos de las diferentes funciones objetivos
	 * @return verdadero si hay, falso de lo contrario
	 */
	private boolean hayCostosReducidosNegativos(double cq, double[] foAuxiliares) {
		for (double fo : foAuxiliares) {
			if (cq - fo < TOLERANCIA)
				return true;
		}
		return false;
	}
}