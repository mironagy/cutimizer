package uniandes.cupi2.cutimizer.interfaz.paneles;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import uniandes.cupi2.cutimizer.interfaz.InterfazCutimizer;

/**
 * Clase encargada de mostrar la lista de demandas ya agregadas
 * @author      SIERRRA
 */
public class PanelMostrarDemandas extends JPanel implements ActionListener {

	private static final long serialVersionUID = -46416814547644507L;

	/**
	 * Constante que modela el comando del bot�n agregar
	 */
	public static final String AGREGAR = "AGREGAR";

	/**
	 * La lista que muestra las demandas ya agregadas
	 */

	private JList listaDemandas;

	/**
	 * El bot�n agregar
	 */

	private JButton btnAgregar;

	/**
	 * La ventana principal de la interfaz
	 * @uml.property  name="interfazCutimizer"
	 * @uml.associationEnd  
	 */

	@SuppressWarnings("unused")
	private InterfazCutimizer interfazCutimizer;

	/**
	 * M�todo encargado de inicializar el panel demandas
	 * @param interfazCutimizer la ventana principal de la interfaz
	 */
	public PanelMostrarDemandas (InterfazCutimizer interfazCutimizer)
		{
			//Asignaci�n de la clase principal
			this.interfazCutimizer = interfazCutimizer;
			
			//Modificaci�n del panel
			TitledBorder borde = new TitledBorder("Demandas agregadas");
			this.setBorder(borde);
			this.setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(200,640));
			
			//Inicializaci�n de los componentes gr�ficos
			listaDemandas = new JList();
			
			btnAgregar = new JButton("Nueva lámina");
			btnAgregar.setIcon(new ImageIcon("./data/imagenes/mas.png"));
			btnAgregar.setActionCommand(AGREGAR);
			btnAgregar.addActionListener(this);

			//Agregar componentes al panel
			this.add(btnAgregar,BorderLayout.SOUTH);
			this.add(listaDemandas,BorderLayout.NORTH);
		}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
