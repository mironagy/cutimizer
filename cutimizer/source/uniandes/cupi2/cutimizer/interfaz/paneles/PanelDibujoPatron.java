package uniandes.cupi2.cutimizer.interfaz.paneles;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.JPanel;

import uniandes.cupi2.cutimizer.interfaz.InterfazSolucionConsola;

/**
 * Esta clase se encarga de mostrar un item de una solucion
 */
public class PanelDibujoPatron extends JPanel {

	/**
	 * Versi—n para la serializacion
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Posicion donde inicia el rectangulo en coordenadas x
	 */
	public static final int INICIO = 40;

	/**
	 * Posicion donde inicia la cuadrilla
	 */
	public static final int INICIO_CUADRILLA = 20;

	/**
	 * Largo de rectangulo que representa el item en pixeles
	 */
	private int largoContenedora;

	/**
	 * Ancho del rectangulo que representa el item en pixeles
	 */
	private static int anchoContenedora;

	/**
	 * Se define la escala como 3pixel/cm
	 */
	public static final int ESCALA = 3;

	/**
	 * El Graphics2D que se est‡ usando
	 */

	private static int largo;
	private static int ancho;

	private InterfazSolucionConsola interfaz;

	public PanelDibujoPatron(InterfazSolucionConsola interfaz) {
		this.interfaz = interfaz;
		largo = 243;
		ancho = 121;

		largoContenedora = ESCALA * largo + 1;
		anchoContenedora = ESCALA * ancho + 1;

	}

	public void refrezcar() {
		dibujarCuadrilla();
		dibujarContenedora();
	}

	public static int darPixelCorrespondienteAlMM(int mm, int escala) {
		return escala * mm / 10;
	}

	public static int darPixelX(int cm) {
		return ESCALA * cm;
	}

	public static int darPixelY(int cm) {
		return ESCALA * cm;
	}

	/**
	 * Dibuja la contenedora en el lienzo dado
	 * 
	 * @param g
	 *            el lienzo
	 * @param ancho
	 *            el ancho de la contenedora a dibujar
	 * @param alto
	 *            el alto de la contenedora a dibujar
	 */
	public static void dibujarContenedora(Graphics2D g, int ancho, int alto) {
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(3));
		Rectangle2D.Double rectangle = new Rectangle2D.Double(INICIO, INICIO,
				ancho, alto);
		g.draw(rectangle);
	}

	/**
	 * Dibuja la contenedora original
	 */
	public void dibujarContenedora() {
		Graphics2D lienzo = (Graphics2D) getGraphics();
		lienzo.setColor(Color.BLACK);
		lienzo.setStroke(new BasicStroke(3));
		Rectangle2D.Double rectangle = new Rectangle2D.Double(INICIO, INICIO,
				largoContenedora, anchoContenedora);
		lienzo.draw(rectangle);
	}

	public static void dibujarCuadrilla(Graphics2D lienzo, int ancho, int alto) {
		Line2D.Double linea = null;
		int cm10 = darPixelX(10);
		int cm = 0;
		lienzo.setColor(new Color(212, 212, 212));
		lienzo.setFont(new Font("Arial", Font.PLAIN, 10));
		// Dibujar la l’nea completa cada 10 cm
		for (int i = INICIO; i <= ancho - 10; i += cm10) {
			linea = new Line2D.Double(i, 9, i, alto - 10);
			lienzo.draw(linea);
		}

		for (int i = INICIO; i <= alto - 10; i += cm10) {

			linea = new Line2D.Double(9, i, ancho - 10, i);
			lienzo.draw(linea);
		}

		lienzo.setColor(Color.BLACK);

		linea = new Line2D.Double(INICIO_CUADRILLA, INICIO_CUADRILLA,
				ancho - 10, INICIO_CUADRILLA);
		lienzo.draw(linea);
		linea = new Line2D.Double(INICIO_CUADRILLA, INICIO_CUADRILLA,
				INICIO_CUADRILLA, alto - 10);
		lienzo.draw(linea);

		// Dibuja la l’nea chica cada 10 cm
		for (int i = INICIO; i <= ancho - 10; i += cm10) {
			linea = new Line2D.Double(i, 9, i, INICIO_CUADRILLA);
			lienzo.draw(linea);
			lienzo.drawString("" + cm, i + 2, 10);
			cm += 10;
		}

		cm = 0;
		for (int i = INICIO; i <= alto - 10; i += cm10) {
			linea = new Line2D.Double(9, i, INICIO_CUADRILLA, i);
			lienzo.draw(linea);
			dibujarTextoConAngulo(lienzo, 10, i - 1, Math.toRadians(0),
					Integer.toString(cm));
			cm += 10;
		}

		int cm1 = darPixelX(1);
		for (int i = INICIO; i <= ancho - 10; i += cm1) {
			linea = new Line2D.Double(i, 16, i, INICIO_CUADRILLA);
			lienzo.draw(linea);
		}

		for (int i = INICIO; i <= alto - 10; i += cm1) {
			linea = new Line2D.Double(16, i, INICIO_CUADRILLA, i);
			lienzo.draw(linea);
		}

		int cm5 = darPixelX(5);
		for (int i = INICIO; i <= ancho - 10; i += cm5) {
			linea = new Line2D.Double(i, 12, i, INICIO_CUADRILLA);
			lienzo.draw(linea);
		}

		for (int i = INICIO; i <= alto - 10; i += cm5) {
			linea = new Line2D.Double(12, i, INICIO_CUADRILLA, i);
			lienzo.draw(linea);
		}
	}

	/**
	 * Dibuja la cuadrilla del plano
	 */
	public void dibujarCuadrilla() {
		Graphics2D lienzo = (Graphics2D) getGraphics();

		Line2D.Double linea = null;
		int cm10 = darPixelX(10);
		int cm = 0;
		lienzo.setColor(new Color(212, 212, 212));
		lienzo.setFont(new Font("Arial", Font.PLAIN, 10));
		// Dibujar la l’nea completa cada 10 cm
		for (int i = INICIO; i <= getWidth() - 10; i += cm10) {
			linea = new Line2D.Double(i, 9, i, getHeight() - 10);
			lienzo.draw(linea);
		}

		for (int i = INICIO; i <= getHeight() - 10; i += cm10) {

			linea = new Line2D.Double(9, i, getWidth() - 10, i);
			lienzo.draw(linea);
		}

		lienzo.setColor(Color.BLACK);

		linea = new Line2D.Double(INICIO_CUADRILLA, INICIO_CUADRILLA,
				getWidth() - 10, INICIO_CUADRILLA);
		lienzo.draw(linea);
		linea = new Line2D.Double(INICIO_CUADRILLA, INICIO_CUADRILLA,
				INICIO_CUADRILLA, getHeight() - 10);
		lienzo.draw(linea);

		// Dibuja la l’nea chica cada 10 cm
		for (int i = INICIO; i <= getWidth() - 10; i += cm10) {
			linea = new Line2D.Double(i, 9, i, INICIO_CUADRILLA);
			lienzo.draw(linea);
			lienzo.drawString("" + cm, i + 2, 10);
			cm += 10;
		}

		cm = 0;
		for (int i = INICIO; i <= getHeight() - 10; i += cm10) {
			linea = new Line2D.Double(9, i, INICIO_CUADRILLA, i);
			lienzo.draw(linea);
			dibujarTextoConAngulo(lienzo, 10, i - 1, Math.toRadians(-90), ""
					+ cm);
			cm += 10;
		}

		int cm1 = darPixelX(1);
		for (int i = INICIO; i <= getWidth() - 10; i += cm1) {
			linea = new Line2D.Double(i, 16, i, INICIO_CUADRILLA);
			lienzo.draw(linea);
		}

		for (int i = INICIO; i <= getHeight() - 10; i += cm1) {
			linea = new Line2D.Double(16, i, INICIO_CUADRILLA, i);
			lienzo.draw(linea);
		}

		int cm5 = darPixelX(5);
		for (int i = INICIO; i <= getWidth() - 10; i += cm5) {
			linea = new Line2D.Double(i, 12, i, INICIO_CUADRILLA);
			lienzo.draw(linea);
		}

		for (int i = INICIO; i <= getHeight() - 10; i += cm5) {
			linea = new Line2D.Double(12, i, INICIO_CUADRILLA, i);
			lienzo.draw(linea);
		}

	}

	private static void dibujarTextoConAngulo(Graphics2D g2D, double x,
			double y, double theta, String label) {

		// Create a rotation transformation for the font.
		AffineTransform fontAT = new AffineTransform();

		// get the current font
		Font theFont = g2D.getFont();

		// Derive a new font using a rotatation transform
		fontAT.rotate(theta);
		Font theDerivedFont = theFont.deriveFont(fontAT);

		// set the derived font in the Graphics2D context
		g2D.setFont(theDerivedFont);

		// Render a string using the derived font
		g2D.drawString(label, (int) x, (int) y);
		// put the original font back
		g2D.setFont(theFont);

	}

	/**
	 * Da el lienzo que se est‡ usando para dibujar
	 * 
	 * @return el lienzo
	 */
	public Graphics2D darLienzo() {
		return (Graphics2D) getGraphics();
	}

	/**
	 * Borra todo el lienzo incluyendo la cuadrilla y la contenedora
	 */
	public void borrar() {

		((Graphics2D) getGraphics()).clearRect(0, 0, getWidth(), getHeight());
	}

	public void dibujarSolucion() {
		Graphics2D g = (Graphics2D) getGraphics();

		g.setFont(new Font("Arial", Font.PLAIN, 60));
		g.drawString("Seleccione un patrón", getWidth() / 6, getHeight() / 2);

	}

	@Override
	public void repaint()
	{
		super.repaint();
		if(getGraphics()!=null)
			this.paintComponent(getGraphics());
	}
	
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		this.paintComponent(g);
	}
	@Override
	/**
	 * Pinta el panel
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D lienzo = (Graphics2D) g;
		lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		lienzo.setColor(Color.WHITE);
		lienzo.fillRect(0, 0, getWidth(), getHeight());

		interfaz.refrezcar();

	}

}
