package uniandes.cupi2.cutimizer.mundo.problemamaestro;

import java.util.ArrayList;

import lpsolve.LpSolve;

import uniandes.cupi2.cutimizer.mundo.Item;
import uniandes.cupi2.cutimizer.mundo.Contenedora;

/**
 * @author SIERRRA
 */
public class ProblemaMaestroLPSOLVE extends ProblemaMaestro {

	/**
	 * La instancia de la clase
	 * 
	 * @uml.property name="problemaMaestroLPSOLVE"
	 * @uml.associationEnd
	 */
	private static ProblemaMaestroLPSOLVE problemaMaestroLPSOLVE;

	/**
	 * El problema entero
	 */
	private LpSolve entero;

	/**
	 * El problema relajado
	 */
	private LpSolve relajacionLineal;

	/**
	 * Maneja las restricciones en LPSOLVE
	 */
	private ArrayList<String> restricciones;

	/**
	 * Maneja la función objetivo en LPSOLVE
	 */

	private String funcionObjetivo;

	/**
	 * Construye un nuevo Problema maestro que usa el optimizador LPSOLVE
	 * 
	 * @param demandas
	 *            las demandas de las láminas
	 * @param laminas
	 *            las láminas que son disponibles
	 * @throws Exception
	 */
	private ProblemaMaestroLPSOLVE(ArrayList<Item> demandas,
			ArrayList<Contenedora> laminas) throws Exception {
		super(demandas, laminas);
		restricciones = new ArrayList<String>();

		cargarProblema();
	}

	@Override
	public ArrayList<Double> darDuales() throws Exception {

		if (isRelajacionLineal()) {
			ArrayList<Double> vd = new ArrayList<Double>();
			double[] duals = relajacionLineal.getPtrDualSolution();

			for (int i = 1; i < duals.length; i++) {
				if (i <= restricciones.size())
					vd.add(duals[i]);
				else
					break;
			}

			return vd;
		} else
			throw new Exception(
					"No se pueden sacar variables duales del problema entero");
	}

	@Override
	public double darValorFO() throws Exception {
		if (!isRelajacionLineal())
			return entero.getObjective();
		else
			return relajacionLineal.getObjective();
	}

	@Override
	public ArrayList<Double> darValorVariablesDecision() throws Exception {
		double[] vars;
		if (isRelajacionLineal())
			vars = relajacionLineal.getPtrVariables();
		else
			vars = entero.getPtrVariables();

		ArrayList<Double> vd = new ArrayList<Double>();
		for (int i = 0; i < vars.length; i++) {
			vd.add(vars[i]);
		}

		return vd;
	}

	@Override
	public void resolver(int tipoProblema) throws Exception {

		// Si el problema a resolver es el restringido
		if (tipoProblema == ENTERO) {
			setRelajacionLineal(false);
			crearProblemaEntero();
			entero.solve();
		}
		// Si el problema que se quiere resolver es la relajación lineal
		else {
			setRelajacionLineal(true);
			relajacionLineal.solve();
		}

	}

	@Override
	public void agregarNuevaVariable(int coeficienteFO,
			ArrayList<Integer> coeficienteRestricciones) throws Exception {
		setNumCol(getNumCol() + 1);

		if (relajacionLineal != null)
			relajacionLineal.deleteLp();
		if (entero != null)
			entero.deleteLp();

		relajacionLineal = LpSolve.makeLp(0, getNumCol());
		// Se crea y agrega la nueva función objetivo
		funcionObjetivo += " " + coeficienteFO;
		relajacionLineal.strSetObjFn(funcionObjetivo);

		for (int i = 0; i < coeficienteRestricciones.size(); i++) {
			String restriccion = restricciones.get(i);
			String nRestriccion = restriccion + " "
					+ coeficienteRestricciones.get(i);
			restricciones.remove(i);
			restricciones.add(i, nRestriccion);

			relajacionLineal.strAddConstraint(nRestriccion, LpSolve.GE,
					getItems().get(i).getCantidad());
		}

		relajacionLineal.setMinim();

	}

	/**
	 * Crea el problema entero con las columnas ya existentes
	 * 
	 * @throws Exception
	 *             si existe algún problema creando el problema entero
	 */
	private void crearProblemaEntero() throws Exception {
		if (relajacionLineal != null)
			relajacionLineal.deleteLp();
		if (entero != null)
			entero.deleteLp();

		entero = LpSolve.makeLp(0, getNumCol());

		// Poner función objetivo
		entero.strSetObjFn(funcionObjetivo);

		// Poner restricciones
		for (int i = 0; i < getItems().size(); i++) {
			entero.strAddConstraint(restricciones.get(i), LpSolve.GE,
					getItems().get(i).getCantidad());
		}

		// Establecer variables enteras
		for (int i = 1; i <= getNumCol(); i++) {
			entero.setInt(i, true);
		}

		entero.setMinim();
	}

	/**
	 * Carga el problema con un patron inicial falso factible con costo muy
	 * grande (método de la gran M)
	 */
	private void cargarProblema() throws Exception {
		setNumCol(getItems().size());
		relajacionLineal = LpSolve.makeLp(0, getNumCol());

		// Se establece la función objetivo
		funcionObjetivo = "";
		for (int i = 0; i < getItems().size(); i++) {
			funcionObjetivo += " " + 1;
		}

		funcionObjetivo = funcionObjetivo.trim();
		relajacionLineal.strSetObjFn(funcionObjetivo);

		for (int i = 0; i < getItems().size(); i++) {
			String restriccion = "";
			for (int j = 0; j < getItems().size(); j++) {
				if(i==j)
					restriccion += " " + 1;
				else
					restriccion += " " + 0;
			}

			restricciones.add(restriccion.trim());

			relajacionLineal.strAddConstraint(restriccion.trim(), LpSolve.GE,
					getItems().get(i).getCantidad());
		}

		relajacionLineal.setMinim();

	}

	/**
	 * Retorna una instancia de la clase
	 * 
	 * @param demandas
	 *            las demandas de sub láminas
	 * @param laminas
	 *            las láminas necesarias
	 * @return la instancia de la clase ProblemaMaestroGurobi
	 * @throws Exception
	 *             Si ocurre algún problema con el optimizador
	 */
	public static ProblemaMaestroLPSOLVE darInstancia(
			ArrayList<Item> demandas, ArrayList<Contenedora> laminas)
			throws Exception {
		if (problemaMaestroLPSOLVE == null)
			problemaMaestroLPSOLVE = new ProblemaMaestroLPSOLVE(demandas,
					laminas);
		return problemaMaestroLPSOLVE;
	}

	/**
	 * Elimina la instancia del ProblemaMaestroGurobi
	 */
	public static void eliminarInstancia() {
		problemaMaestroLPSOLVE = null;
	}

}
