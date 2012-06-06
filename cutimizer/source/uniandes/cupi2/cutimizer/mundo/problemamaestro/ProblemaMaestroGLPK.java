package uniandes.cupi2.cutimizer.mundo.problemamaestro;

import java.util.ArrayList;

import uniandes.cupi2.cutimizer.mundo.Item;
import uniandes.cupi2.cutimizer.mundo.Contenedora;
import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_iocp;
import org.gnu.glpk.glp_prob;

/**
 * @author SIERRRA
 */
public class ProblemaMaestroGLPK extends ProblemaMaestro {
	/**
	 * La instancia de la clase
	 * 
	 * @uml.property name="problemaMaestroGLPK"
	 * @uml.associationEnd
	 */
	private static ProblemaMaestroGLPK problemaMaestroGLPK;

	/**
	 * La relajación lineal de problema
	 */
	glp_prob relajacionLineal;

	/**
	 * El problema entero restringido
	 */
	glp_prob entero;

	/**
	 * Guarda los coeficientes de la funci—n objetivo
	 */
	private String funcionObjetivo;

	/**
	 * Guarda los coeficientes de las restricciones
	 */
	private ArrayList<String> restricciones;

	/**
	 * Construye un nuevo Problema maestro que usa el optimizador GLPK
	 * 
	 * @param nDemandas
	 *            las demandas de láminas
	 * @param nLami
	 *            nas las configuraciones de sub láminas posibles
	 * @throws Exception
	 */
	private ProblemaMaestroGLPK(ArrayList<Item> nDemandas,
			ArrayList<Contenedora> nLaminas) throws Exception {
		super(nDemandas, nLaminas);
		restricciones = new ArrayList<String>();

		// Se crea el problema relajado
		relajacionLineal = GLPK.glp_create_prob();
		GLPK.glp_set_prob_name(relajacionLineal, "Relajación lineal");

		cargarProblema();
	}

	/**
	 * Carga el problema con un patron inicial que saca s—lo un item de cada
	 * l‡mina.
	 */
	private void cargarProblema() throws Exception {

		SWIGTYPE_p_int ind;
		SWIGTYPE_p_double val;

		/**
		 * Variables
		 */
		// Crea el problema con nœmero de columnas igual a demandas
		GLPK.glp_add_cols(relajacionLineal, getItems().size());
		funcionObjetivo = "";
		for (int i = 1; i <= getItems().size(); i++) {

			// La variable es de tipo continuo
			GLPK.glp_set_col_kind(relajacionLineal, i, GLPK.GLP_CV);

			// Le asigna un nombre a la columna
			GLPK.glp_set_col_name(relajacionLineal, i, "Patrón " + getNumCol());
			// Le asigna un límite inferior a la columna
			GLPK.glp_set_col_bnds(relajacionLineal, i, GLPK.GLP_LO, 0.0, 0.0);

			/**
			 * Función objetivo
			 */
			// Asignar coeficiente en la FO
			GLPK.glp_set_obj_coef(relajacionLineal, i, 1);

			funcionObjetivo += " " + 1;
			// Aumenta el nœmero de columnas en uno excepto para el primer
			// patrón
			if (i != 1)
				setNumCol(getNumCol() + 1);
		}
		funcionObjetivo = funcionObjetivo.trim();
		/**
		 * Restricciones
		 */
		// Crea el problema con demandas.size() filas
		GLPK.glp_add_rows(relajacionLineal, getItems().size());

		for (int i = 0; i < getItems().size(); i++) {
			// Le asigna el nombre r_i a las restricciones
			GLPK.glp_set_row_name(relajacionLineal, i + 1, "r" + (i + 1));
			GLPK.glp_set_row_bnds(relajacionLineal, i + 1, GLPK.GLP_LO,
					getItems().get(i).getCantidad(), 0);

			String restriccion;
			if (i + 1 == 1)
				restriccion = "" + 1;
			else
				restriccion = "" + 0;

			ind = GLPK.new_intArray(getNumCol() + 1);
			val = GLPK.new_doubleArray(getNumCol() + 1);

			for (int j = 1; j <= getNumCol(); j++) {

				if (j != 1) {
					if (i + 1 == j)
						restriccion += " " + 1;
					else
						restriccion += " " + 0;
				}

				GLPK.intArray_setitem(ind, j, j);
				if (i + 1 == j)
					GLPK.doubleArray_setitem(val, j, 1);
				else
					GLPK.doubleArray_setitem(val, j, 0);

			}
			restricciones.add(restriccion);
			GLPK.glp_set_mat_row(relajacionLineal, i + 1, getNumCol(), ind, val);
		}

		// Establecer el objetivo como minimización
		GLPK.glp_set_obj_dir(relajacionLineal, GLPK.GLP_MIN);
	}

	@Override
	public ArrayList<Double> darDuales() throws Exception {
		if (isRelajacionLineal()) {
			ArrayList<Double> duales = new ArrayList<Double>();
			for (int i = 1; i <= getItems().size(); i++) {
				duales.add(GLPK.glp_get_row_dual(relajacionLineal, i));
			}

			return duales;
		} else
			throw new Exception(
					"No se puede sacar variables duales del problema entero");
	}

	@Override
	public double darValorFO() throws Exception {
		if (isRelajacionLineal())
			return GLPK.glp_get_obj_val(relajacionLineal);
		else
			return GLPK.glp_mip_obj_val(entero);
	}

	@Override
	public ArrayList<Double> darValorVariablesDecision() throws Exception {
		ArrayList<Double> variables = new ArrayList<Double>();
		for (int i = 1; i <= getNumCol(); i++) {
			if (isRelajacionLineal())
				variables.add(GLPK.glp_get_col_prim(relajacionLineal, i));
			else
				variables.add(GLPK.glp_mip_col_val(entero, i));

		}
		return variables;
	}

	@Override
	public void resolver(int tipoProblema) throws Exception {
		if (tipoProblema == RELAJACION_LINEAL) {
			setRelajacionLineal(true);
			GLPK.glp_simplex(relajacionLineal, null);
		} else {
			setRelajacionLineal(false);
			crearProblemaEntero();
			glp_iocp iocp = new glp_iocp();
			GLPK.glp_init_iocp(iocp);
			iocp.setPresolve(GLPKConstants.GLP_ON);
			GLPK.glp_intopt(entero, iocp);
		}

	}

	private void crearProblemaEntero() {
		SWIGTYPE_p_int ind;
		SWIGTYPE_p_double val;

		// Parte los coefiencientes por su separador
		String[] coeficientes = funcionObjetivo.split(" ");

		if (relajacionLineal != null)
			GLPK.glp_delete_prob(relajacionLineal);
		if (entero != null)
			GLPK.glp_delete_prob(entero);

		entero = GLPK.glp_create_prob();

		/**
		 * Variables
		 */
		// Crea el problema con numCol variables
		GLPK.glp_add_cols(entero, getNumCol());

		for (int i = 1; i <= getNumCol(); i++) {
			// Las variables deben ser de tipo entero
			GLPK.glp_set_col_kind(entero, i, GLPK.GLP_IV);
			// Le asigna un nombre a la columna
			GLPK.glp_set_col_name(entero, i, "Patrón " + i);
			// Le asigna un límite inferior a la columna
			GLPK.glp_set_col_bnds(entero, i, GLPK.GLP_LO, 0.0, 0.0);

			/**
			 * Función objetivo
			 */
			// Asignar coeficientes en la FO
			GLPK.glp_set_obj_coef(entero, i,
					Double.parseDouble(coeficientes[i - 1]));

		}

		/**
		 * Restricciones
		 */
		// Crea el problema con demandas.size() filas
		GLPK.glp_add_rows(entero, getItems().size());
		for (int i = 0; i < getItems().size(); i++) {
			// Le asigna el nombre r_i a las restricciones
			GLPK.glp_set_row_name(entero, i + 1, "r" + i);
			GLPK.glp_set_row_bnds(entero, i + 1, GLPK.GLP_LO, getItems()
					.get(i).getCantidad(), 0);

			String restriccion = restricciones.get(i);

			ind = GLPK.new_intArray(getNumCol() + 1);
			val = GLPK.new_doubleArray(getNumCol() + 1);
			String[] valores = restriccion.split(" ");

			for (int j = 1; j <= getNumCol(); j++) {
				GLPK.intArray_setitem(ind, j, j);
				GLPK.doubleArray_setitem(val, j,
						Double.parseDouble(valores[j - 1]));
			}

			GLPK.glp_set_mat_row(entero, i + 1, getNumCol(), ind, val);
		}
		// Establecer el objetivo como minimización
		GLPK.glp_set_obj_dir(entero, GLPK.GLP_MIN);
	}

	@Override
	public void agregarNuevaVariable(int coeficienteFO,
			ArrayList<Integer> coeficienteRestricciones) throws Exception {
		SWIGTYPE_p_int ind;
		SWIGTYPE_p_double val;

		// Cambia la funci—n objetivo por la nueva funci—n objetivo
		funcionObjetivo += " " + coeficienteFO;
		// Parte los coefiencientes por su separador
		String[] coeficientes = funcionObjetivo.split(" ");

		setNumCol(getNumCol() + 1);

		if (relajacionLineal != null)
			GLPK.glp_delete_prob(relajacionLineal);
		if (entero != null)
			GLPK.glp_delete_prob(entero);

		relajacionLineal = GLPK.glp_create_prob();

		/**
		 * Variables
		 */
		// Crea el problema con numCol variables
		GLPK.glp_add_cols(relajacionLineal, getNumCol());

		for (int i = 1; i <= getNumCol(); i++) {
			// Las variables deben ser de tipo cont’nuo
			GLPK.glp_set_col_kind(relajacionLineal, i, GLPK.GLP_CV);
			// Le asigna un nombre a la columna
			GLPK.glp_set_col_name(relajacionLineal, i, "Patrón " + i);
			// Le asigna un límite inferior a la columna
			GLPK.glp_set_col_bnds(relajacionLineal, i, GLPK.GLP_LO, 0.0, 0.0);

			/**
			 * Función objetivo
			 */
			// Asignar coeficientes en la FO
			GLPK.glp_set_obj_coef(relajacionLineal, i,
					Double.parseDouble(coeficientes[i - 1]));

		}

		/**
		 * Restricciones
		 */
		// Crea el problema con demandas.size() filas
		GLPK.glp_add_rows(relajacionLineal, getItems().size());
		for (int i = 0; i < getItems().size(); i++) {
			// Le asigna el nombre r_i a las restricciones
			GLPK.glp_set_row_name(relajacionLineal, i + 1, "r" + i);
			GLPK.glp_set_row_bnds(relajacionLineal, i + 1, GLPK.GLP_LO,
					getItems().get(i).getCantidad(), 0);

			String restriccion = restricciones.get(i) + " "
					+ coeficienteRestricciones.get(i);
			restricciones.remove(i);
			restricciones.add(i, restriccion);

			ind = GLPK.new_intArray(getNumCol() + 1);
			val = GLPK.new_doubleArray(getNumCol() + 1);
			String[] valores = restriccion.split(" ");

			for (int j = 1; j <= getNumCol(); j++) {
				GLPK.intArray_setitem(ind, j, j);
				GLPK.doubleArray_setitem(val, j,
						Double.parseDouble(valores[j - 1]));
			}

			GLPK.glp_set_mat_row(relajacionLineal, i + 1, getNumCol(), ind, val);
		}
		// Establecer el objetivo como minimización
		GLPK.glp_set_obj_dir(relajacionLineal, GLPK.GLP_MIN);

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
	public static ProblemaMaestroGLPK darInstancia(ArrayList<Item> demandas,
			ArrayList<Contenedora> laminas) throws Exception {
		if (problemaMaestroGLPK == null)
			problemaMaestroGLPK = new ProblemaMaestroGLPK(demandas, laminas);
		return problemaMaestroGLPK;
	}

	/**
	 * Elimina la instancia del ProblemaMaestroGurobi
	 */
	public static void eliminarInstancia() {
		problemaMaestroGLPK = null;
	}

}
