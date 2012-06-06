package uniandes.cupi2.cutimizer.mundo.problemamaestro;

import java.util.ArrayList;
import gurobi.*;
import gurobi.GRB.DoubleAttr;
import gurobi.GRB.IntParam;
import uniandes.cupi2.cutimizer.mundo.Item;
import uniandes.cupi2.cutimizer.mundo.Contenedora;

/**
 * Esta clase repesenta un modelo maestro resuelto con GUROBI
 */
public class ProblemaMaestroGurobi extends ProblemaMaestro {

	/**
	 * Modela el ambiente en GUROBI
	 */
	private GRBEnv env;
	/**
	 * Modela el modelo de optimización en GUROBI
	 */
	private GRBModel entero;

	/**
	 * Modela el modelo de optimización relajado en GUROBI
	 */
	private GRBModel relajacionLineal;

	/**
	 * Modela las expresiones lineales del problema maestro
	 */
	private ArrayList<GRBLinExpr> expresionesLineales;

	/**
	 * Modela las restricciones del problema maestro
	 */
	private ArrayList<GRBConstr> restricciones;

	/**
	 * Crea una instancia del problema maestro
	 * 
	 * @uml.property name="problemaMaestroGurobi"
	 * @uml.associationEnd
	 */
	private static ProblemaMaestroGurobi problemaMaestroGurobi;

	/**
	 * Constructor una instancia de un ProblemaMaestroGurobi
	 * 
	 * @throws Exception
	 *             en caso de que el optimizador falle creando el modelo.
	 */
	private ProblemaMaestroGurobi(ArrayList<Item> demandas,
			ArrayList<Contenedora> laminas) throws Exception {
		// Lama al constructor de la clase ProblemaMaestro
		super(demandas, laminas);

		// Crea un nuevo ambiente. El archivo de seguimineto se llama
		// "Problema Maestro Gurobi.log"
		env = new GRBEnv("Problema Maestro Gurobi.log");
		// Desactiva el presolver
		env.set(GRB.IntParam.Presolve, 0);
		// Metodo simplex primal
		env.set(IntParam.Method, 0);

		// Se crea el modelo restringido
		entero = new GRBModel(env);

		// Inicializa los arreglos del problema maestro
		expresionesLineales = new ArrayList<GRBLinExpr>();
		restricciones = new ArrayList<GRBConstr>();

		// Carga el problema inicial a resolver
		cargarProblema();
	}

	@Override
	/**
	 * Retorna el valor de las variables duales del problema anteriormente ejecutado
	 */
	public ArrayList<Double> darDuales() throws GRBException {
		GRBConstr[] restr;
		if (isRelajacionLineal())
			restr = relajacionLineal.getConstrs();
		else
			throw new GRBException();

		ArrayList<Double> vd = new ArrayList<Double>();
		for (int i = 0; i < restr.length; i++) {
			vd.add(restr[i].get(DoubleAttr.Pi));
		}

		return vd;
	}

	@Override
	/**
	 * Retorna el valor de la función objetivo del último problema ejecutado
	 */
	public double darValorFO() throws GRBException {
		if (!isRelajacionLineal())
			return entero.get(GRB.DoubleAttr.ObjVal);
		else
			return relajacionLineal.get(GRB.DoubleAttr.ObjVal);
	}

	/**
	 * Retorna el valor de las variables de decisión del último problema corrido
	 */
	@Override
	public ArrayList<Double> darValorVariablesDecision() throws GRBException {
		GRBVar[] vars;
		if (isRelajacionLineal())
			vars = relajacionLineal.getVars();
		else
			vars = entero.getVars();

		ArrayList<Double> vd = new ArrayList<Double>();
		for (int i = 0; i < vars.length; i++) {
			vd.add(vars[i].get(DoubleAttr.X));
		}

		return vd;
	}

	/**
	 * Carga el problema con un patron inicial falso factible con costo muy
	 * grande (mŽtodo de la gran M)
	 */
	private void cargarProblema() throws Exception {
		// Crea la nueva función objetivo
		GRBLinExpr funcionObjetivo = new GRBLinExpr();

		for (int i = 0; i < getItems().size(); i++) {
			// Crea una nueva variable de decisión
			GRBVar nv = entero.addVar(0.0, getCotaSuperior(), 0.0, GRB.INTEGER,
					"Patrón " + getNumCol());

			entero.update();

			// Agrega el tŽrmino del patrón falso a la nueva función objetivo
			funcionObjetivo.addTerm(1, nv);

			// Se construye el patrón básico falso que puede proporcionar todas
			// las
			// cantidades demandas
			GRBLinExpr expr = new GRBLinExpr();
			expr.addTerm(1, nv);

			expresionesLineales.add(expr);

			GRBConstr constr = entero.addConstr(expr, GRB.GREATER_EQUAL,
					getItems().get(i).getCantidad(), "r" + i);
			restricciones.add(constr);

			setNumCol(getNumCol() + 1);
		}
		entero.setObjective(funcionObjetivo, GRB.MINIMIZE);
		entero.update();
	}

	@Override
	/**
	 * Resuelve el problema de optimización de SET COVERING
	 * @param tipoProblema el tipo de problema que se quiere resolver
	 */
	public void resolver(int tipoProblema) throws GRBException {
		entero.reset();
		// Si el problema a resolver es el restringido
		if (tipoProblema == ENTERO) {
			setRelajacionLineal(false);
			entero.optimize();
		}
		// Si el problema que se quiere resolver es la relajación lineal
		else {
			setRelajacionLineal(true);
			relajacionLineal = entero.relax();
			relajacionLineal.optimize();

		}

	}

	@Override
	/**
	 * Agrega una nueva columna obtenida por el problema auxiliar al problema maestro
	 * @param coeficienteFO	El coeficiente de la nueva variable en la función objetivo
	 * @param coeficientesRestricciones	Los coeficientes en las demás expresionesLineales
	 */
	public void agregarNuevaVariable(int coeficienteFO,
			ArrayList<Integer> coeficienteRestricciones) throws Exception {
		// Aumenta el número de patrones a uno más
		setNumCol(getNumCol() + 1);
		// Crea la nueva variable
		GRBVar nv = entero.addVar(0.0, getCotaSuperior(), coeficienteFO,
				GRB.INTEGER, "Patrón " + getNumCol());
		// Actualiza el modelo
		entero.update();

		// Agrega la variable a las expresiones lineales
		for (int i = 0; i < expresionesLineales.size(); i++) {
			// Modifico la expresión lineal con el nuevo tŽrmino
			GRBLinExpr expr = expresionesLineales.get(i);
			expr.addTerm(coeficienteRestricciones.get(i), nv);

			// Elimino la restricción anterior asociado con ese tŽrmino del
			// modelo
			entero.remove(restricciones.get(i));
			// Elimino la restricción del arreglo de restricciones
			restricciones.remove(i);
			// Agrego la nueva restricción asociado con el tŽrmino al modelo
			GRBConstr constr = entero.addConstr(expr, GRB.GREATER_EQUAL,
					getItems().get(i).getCantidad(), "r" + i);

			// Agrego la nueva restricción al arreglo de restricciones en la
			// misma posición.
			restricciones.add(i, constr);
		}
		entero.update();
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
	public static ProblemaMaestroGurobi darInstancia(
			ArrayList<Item> demandas, ArrayList<Contenedora> laminas)
			throws Exception {
		if (problemaMaestroGurobi == null)
			problemaMaestroGurobi = new ProblemaMaestroGurobi(demandas, laminas);
		return problemaMaestroGurobi;
	}

	/**
	 * Elimina la instancia del ProblemaMaestroGurobi
	 */
	public static void eliminarInstancia() {
		problemaMaestroGurobi = null;
	}

}
