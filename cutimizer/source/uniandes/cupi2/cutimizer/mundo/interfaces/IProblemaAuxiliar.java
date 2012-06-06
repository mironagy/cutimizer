package uniandes.cupi2.cutimizer.mundo.interfaces;

import java.util.ArrayList;

/**
 * Interface que indica los servicios b‡sicos que debe ofrecer un problema
 * auxiliar
 */
public interface IProblemaAuxiliar {
	/**
	 * Retorna los coeficientes del patrón generado
	 * 
	 * @return los coeficientes del patrón generado
	 */
	public ArrayList<Integer> darCoeficientesPatron();

	/**
	 * Retorna el costo del patrón generado
	 * 
	 * @return el costo del patrón generado
	 */
	public double darValorPatron();
	
	/**
	 * Inicia la ejecucuci—n del problema auxiliar y espera a que termine
	 * @throws Exception si el hilo es detenido antes de dar la soluci—n
	 */
	public void iniciar() throws Exception;
	
	/**
	 * Retrona el patr—n originado
	 * @return el patr—n correspondiente a la columna que ha sido generada
	 */
	public IPatron darPatron();
	
}
