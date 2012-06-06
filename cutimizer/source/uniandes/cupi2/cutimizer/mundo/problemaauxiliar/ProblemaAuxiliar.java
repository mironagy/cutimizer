package uniandes.cupi2.cutimizer.mundo.problemaauxiliar;

import uniandes.cupi2.cutimizer.mundo.interfaces.IProblemaAuxiliar;

/**
 * Implementa un problema auxiliar en nuevo hilo de ejecuci—n 
 * @author jsierrajur
 */
public abstract class ProblemaAuxiliar extends Thread implements IProblemaAuxiliar 
{
	@Override
	public void iniciar() throws Exception
	{
		start();
		join();
	}
}
