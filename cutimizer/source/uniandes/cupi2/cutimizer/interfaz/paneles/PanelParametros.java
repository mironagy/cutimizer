package uniandes.cupi2.cutimizer.interfaz.paneles;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import uniandes.cupi2.cutimizer.interfaz.InterfazCutimizer;
/**
 * Clase que modela el panel de parámetros
 * @author      SIERRRA
 */
public class PanelParametros extends JPanel
{	
	
	private static final long serialVersionUID = -8057638031710123128L;

	/**
	 * Panel para modificar la demanda
	 * @uml.property  name="panelModificarDemanda"
	 * @uml.associationEnd  
	 */
	PanelModificarDemanda panelModificarDemanda;
	
	/**
	 * Panel para mostrar las demandas
	 * @uml.property  name="panelMostrarDemandas"
	 * @uml.associationEnd  
	 */
	
	PanelMostrarDemandas panelMostrarDemandas;
	
	/**
	 * Construye e inicializa el panel de parámetros
	 * @param interfazCutimizer clase que modela la ventana principal
	 */
	public PanelParametros(InterfazCutimizer interfazCutimizer)
	{
		//Configuración de la ventana
		this.setLayout(new BorderLayout());
		
		//Creación de los páneles
		panelModificarDemanda = new PanelModificarDemanda(interfazCutimizer,PanelModificarDemanda.AGREGAR_DEMANDA);
		panelMostrarDemandas = new PanelMostrarDemandas(interfazCutimizer);
		
		//Agregar páneles
		this.add(panelMostrarDemandas,BorderLayout.WEST);
		this.add(panelModificarDemanda,BorderLayout.CENTER);
		
	}
}
