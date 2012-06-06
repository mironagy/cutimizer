package uniandes.cupi2.cutimizer.interfaz.paneles;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import uniandes.cupi2.cutimizer.interfaz.InterfazCutimizer;

public class PanelModificarDemanda extends JPanel implements ActionListener {

	private static final long serialVersionUID = -7698797301486261010L;

	/**
	 * Indica que se usar� el panel para agregar una nueva demanda
	 */
	public static final int AGREGAR_DEMANDA = -1;

	/**
	 * Constante que modela el comando del bot�n eliminar
	 */
	public static final String ELIMINAR = "ELIMINAR";

	/**
	 * Constante que modela el comando del bot�n agregar
	 */
	public static final String AGREGAR_LAMINA = "AGREGAR_LAMINA";

	/**
	 * Etiqueta que muestra el ancho de la l�mina
	 */
	private JLabel lblAncho;

	/**
	 * �rea de texto con el ancho de la l�mina
	 */
	private JTextField txtAncho;
	/**
	 * Etiqueta que muestra el alto de la l�mina
	 */
	private JLabel lblAlto;
	/**
	 * �rea de texto con el alto de la l�mina
	 */
	private JTextField txtAlto;
	/**
	 * Etiqueta que muestra el alto de la l�mina
	 */
	private JLabel lblCantidad;
	/**
	 * �rea de texto con el alto de la l�mina
	 */
	private JTextField txtCantidad;
	/**
	 * Etiqueta que muestra el espesor de la l�mina
	 */
	private JLabel lblEspesor;
	/**
	 * Combo box con los espesores disponibles
	 */
	private JComboBox cmbEspesor;

	/**
	 * Bot�n para eliminar la demanda seleccionada
	 */
	@SuppressWarnings("unused")
	private JButton btnEliminar;

	/**
	 * Bot�n para agregar una nueva l�mina
	 */
	private JButton btnAgregar;

	/**
	 * Bot�n para guardar los cambios realizados
	 */
	@SuppressWarnings("unused")
	private JButton btnModificar;

	/**
	 * Panel usado para ubicar de manera correcta los componentes gr�ficos
	 */
	private JPanel panelSuperior;

	/**
	 * Crea el panel sea vacio para agregar una nueva demanda o lleno para
	 * modificar una demanda ya existente
	 * 
	 * @param interfazCutimizer
	 *            objeto que modela la ventan principal de la aplicaci�n
	 * @param indiceDemanda
	 *            indice que indica cu�l demanda se quiere modificar. Si
	 *            inidiceDemanda == AGERGAR_DEMANDA, se quiere agregar una nueva
	 *            demanda
	 */
	public PanelModificarDemanda(InterfazCutimizer interfazCutimizer,
			int indiceDemanda) {

		// Configuraci�n del panel
		setLayout(new BorderLayout());

		// Creaci�n de los componentes gr�ficos
		lblAlto = new JLabel("Alto de la lámina            (cm)");
		lblAncho = new JLabel("Ancho de la lámina        (cm)");
		lblEspesor = new JLabel("Espesor de la lámina      (mm)");
		lblCantidad = new JLabel("Cantidad necesaria        (unidades)");

		// Inicializar el panel dependiendo de c�mo se quiera crear
		if (indiceDemanda == AGREGAR_DEMANDA)
			inicializarVacio();
		else
			inicializarConInformacion(indiceDemanda);

	}

	/**
	 * Este m�todo inicializa el panel con la informaci�n de una demanda ya
	 * existente <br/>
	 * <b>pre:</b> La demanda ya existe
	 * 
	 * @param indiceDemanda
	 *            de la demanda que se quiere modificar
	 */
	private void inicializarConInformacion(int indiceDemanda) {
		// TODO
	}

	/**
	 * Este m�todo inicializa el panel vac�o para que una nueva demanda se
	 * creada
	 */
	private void inicializarVacio() {
		panelSuperior = new JPanel();

		panelSuperior.setLayout(new GridLayout(5, 2));

		Border borde = new TitledBorder("Agregar nueva lámina");
		this.setBorder(borde);
		panelSuperior.add(lblAncho);
		txtAncho = new JTextField();
		panelSuperior.add(txtAncho);
		panelSuperior.add(lblAlto);
		txtAlto = new JTextField();
		panelSuperior.add(txtAlto);
		panelSuperior.add(lblEspesor);
		cmbEspesor = new JComboBox();
		panelSuperior.add(cmbEspesor);
		panelSuperior.add(lblCantidad);
		txtCantidad = new JTextField();
		panelSuperior.add(txtCantidad);

		panelSuperior.add(new JLabel());

		btnAgregar = new JButton("  Agregar la lámina");
		btnAgregar.setActionCommand(AGREGAR_LAMINA);
		btnAgregar.addActionListener(this);
		btnAgregar.setIcon(new ImageIcon("./data/imagenes/accept.png"));

		this.add(panelSuperior, BorderLayout.NORTH);
		this.add(btnAgregar, BorderLayout.SOUTH);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
