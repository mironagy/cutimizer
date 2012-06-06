package uniandes.cupi2.cutimizer.interfaz;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import uniandes.cupi2.cutimizer.interfaz.paneles.PanelParametros;
import uniandes.cupi2.cutimizer.interfaz.paneles.PanelSolucion;
import uniandes.cupi2.cutimizer.mundo.Principal;

/**
 * �sta es la venta principal de la aplicaci�n
 */
public class InterfazCutimizer extends JFrame
{
	private static final long serialVersionUID = -3255870012312809474L;
	
    // -----------------------------------------------------------------
    // Atributos de Interfaz
    // -----------------------------------------------------------------

    /**
     * Contenedor de paneles (por tabs)
     */	
	private JTabbedPane contenedor;
	
	/**
	 * Panel de modificación de parámetros
	 * @uml.property  name="panelParametros"
	 * @uml.associationEnd  
	 */
    PanelParametros panelParametros;
    
    /**
     * El panel donde se despliegan las soluciones
     */
    PanelSolucion panelSolucion;
    // -----------------------------------------------------------------
    // Atributos
    // -----------------------------------------------------------------
    
    /**
	 * @uml.property  name="principal"
	 * @uml.associationEnd  
	 */
    @SuppressWarnings("unused")
	private Principal principal;
    
    // -----------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------
	/**
	 * Crea la ventana principal de la aplicaci�n e inicializa el programa
	 */
	public InterfazCutimizer() {
		
		//Inicializaci�n del mundo
		try {
			principal = new Principal();
			dispose();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		//Configuraciones de la ventana principal
		this.setVisible(true);
		this.setSize(600,640);
		//this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		ImageIcon icon = new ImageIcon("./data/imagenes/icono.png");
		this.setIconImage(icon.getImage());
		
		this.setTitle("Principal - Jorge Sierra Construcciones SAS");
		
		//P�neles de informaci�n
		panelParametros = new PanelParametros(this);
		panelSolucion = new PanelSolucion();
		
		//Creaci�n de las pesta�as
		contenedor = new JTabbedPane( );
        contenedor.addTab( "Parámetros", null, panelParametros, null );
        contenedor.addTab( "Soluciones", null, panelSolucion, null );
        add( contenedor, BorderLayout.CENTER );

        
	}

	/**
	 * M�todo que comienza la l�nea de ejecuci�n del programa
	 * 
	 * @param informaci
	 *            �n suministrada por el sistema operativo
	 */
	public static void main(String[] args) {
		// Crea una nueva instancia de la ventana principal
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e) 
		{
	        try{UIManager.setLookAndFeel(
	                UIManager.getCrossPlatformLookAndFeelClassName());
	        }
	        catch (Exception e1)
	        {
	        	e1.printStackTrace();	//En caso de fallar no se hace nada
	        }
	    }
		new InterfazCutimizer();
	}

}
