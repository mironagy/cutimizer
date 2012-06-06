package uniandes.cupi2.cutimizer.interfaz.paneles;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class PanelSolucion extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * El panel donde se dibuja la soluci�n
	 */
	private PanelDibujoPatron panelItem;
	
	public PanelSolucion () 
	{
		this.setLayout(new GridLayout(1,1));
		//panelItem = new PanelDibujoPatron();
		
		//add(panelItem);
		//panelItem.refrezcar();
	}
}
