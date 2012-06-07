package uniandes.cupi2.cutimizer.interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import uniandes.cupi2.cutimizer.interfaz.paneles.PanelDibujoPatron;
import uniandes.cupi2.cutimizer.mundo.ItemSolucion;
import uniandes.cupi2.cutimizer.mundo.Patron2D;
import uniandes.cupi2.cutimizer.mundo.Solucion;
import uniandes.cupi2.cutimizer.mundo.interfaces.IPatron;

public class InterfazSolucionConsola extends JFrame implements
		TreeSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Solucion solucion;
	private PanelDibujoPatron panelDibujoPatron;
	private JTree listaPatrones;
	private JTextArea areaDescripcion;
	private JScrollPane scrollLista;
	private JScrollPane scrollDescripcion;

	private JSplitPane divisionVertical;
	private JSplitPane divisionHorizontal;

	public InterfazSolucionConsola(Solucion nSolucion) {

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(1265,880));
		
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		this.solucion = nSolucion;
		this.setTitle(solucion.getNombre() + " del "
				+ solucion.getFecha().toString());
		this.setLayout(new BorderLayout());
		
		JPanel panelLista = new JPanel();
		panelLista.setBorder(BorderFactory.createTitledBorder("Patrones"));
		panelLista.setLayout(new BorderLayout());

		panelLista.setMinimumSize(new Dimension(470, 800));

		/**
		 * La lista de patrones
		 */
		DefaultMutableTreeNode nodoSolucion;
		DefaultMutableTreeNode nodoPatron;
		DefaultMutableTreeNode nodoItemSolucion;

		nodoSolucion = new DefaultMutableTreeNode(solucion.getNombre()
				+ " del " + solucion.getFecha().toString());
				
		listaPatrones = new JTree(nodoSolucion);
		
		ImageIcon iconoSolucion = new ImageIcon("./data/imagenes/solucion.png");
		DefaultTreeCellRenderer renderer =  new DefaultTreeCellRenderer();
		
		renderer.setOpenIcon(iconoSolucion);
		
		listaPatrones.setCellRenderer(renderer);
		
		scrollLista = new JScrollPane(listaPatrones);

		listaPatrones.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		listaPatrones.addTreeSelectionListener(this);

		for (int i = 0; i < solucion.getPatrones().size(); i++) {
			nodoPatron = new DefaultMutableTreeNode(solucion.getPatrones().get(
					i));
			
			ArrayList<ItemSolucion> itemsSolucion = solucion.getPatrones().get(i).getItems();
			for(ItemSolucion it : itemsSolucion)
			{
				nodoItemSolucion = new DefaultMutableTreeNode(it);
				nodoPatron.add(nodoItemSolucion);
			}
			
			nodoSolucion.add(nodoPatron);
		}

		for (int i = 0; i < listaPatrones.getRowCount(); i++) {
			listaPatrones.expandRow(i);
		}

		
		
		panelLista.add(scrollLista, BorderLayout.CENTER);

		/**
		 * El panel con el dibujo del patron
		 */

		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new BorderLayout());

		panelDibujoPatron = new PanelDibujoPatron(this);
		panelDibujoPatron.setPreferredSize(new Dimension(790, 420));

		JPanel panelDescripcion = new JPanel();
		panelDescripcion.setBorder(BorderFactory
				.createTitledBorder("Descripciones"));
		panelDescripcion.setLayout(new BorderLayout());

		areaDescripcion = new JTextArea("");
		areaDescripcion.setEditable(false);

		scrollDescripcion = new JScrollPane(areaDescripcion);
		panelDescripcion.add(scrollDescripcion, BorderLayout.CENTER);

		divisionHorizontal = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				panelDibujoPatron, panelDescripcion);

		panelCentral.add(divisionHorizontal, BorderLayout.CENTER);

		divisionVertical = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				panelLista, panelCentral);
		this.add(divisionVertical, BorderLayout.CENTER);

		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		
		validate();
	}

	public void refrezcar(boolean borrar, Graphics2D lienzo) {
		DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) listaPatrones
				.getLastSelectedPathComponent();
		if (nodo == null)
			return;

		if (nodo.getUserObject() instanceof IPatron) {
			if (borrar)
				panelDibujoPatron.borrar();
			if (lienzo == null)
				lienzo = panelDibujoPatron.darLienzo();

			panelDibujoPatron.dibujarCuadrilla(lienzo);
			
			IPatron patron = (IPatron) nodo.getUserObject();
			patron.dibujarse(lienzo, PanelDibujoPatron.ESCALA);

			areaDescripcion.setText(patron.darDescripcion() + '\n'
					+ "Instrucciones de cortado: " + '\n' + '\n'
					+ patron.darIndicacionesDeCorte());
			panelDibujoPatron.dibujarContenedora(lienzo);
		}

		else if (nodo.isRoot()) {
			panelDibujoPatron.borrar();
			panelDibujoPatron.dibujarPatronNoSeleccionado();
			areaDescripcion.setText(solucion.toString());
		}
		else
			System.out.println(nodo.getUserObject().getClass().toString());

	}

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		refrezcar(true, null);
	}

}
