package uniandes.cupi2.cutimizer.mundo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import uniandes.cupi2.cutimizer.interfaz.paneles.PanelDibujoPatron;

/**
 * Modela un Patron que se dibuja usando el framework JavaD
 * 
 * @author jsierrajur
 */
public class Patron2D extends Patron {

	/**
	 * Construye una nueva instancia de la clase con los par�metros dados sin instrucciones de corte
	 * @param items
	 * @param desperdicios
	 * @param precio
	 * @param id
	 */
	public Patron2D(ArrayList<ItemSolucion> items, double precio, int id, int cantidad) {
		super(items, precio, id,cantidad);
	}

	/**
	 * Crea una instancia de la clase con indicaciones de c�mo cortar el patr�n.
	 * @param items
	 * @param precio
	 * @param id
	 * @param cantidad
	 * @param indicaciones
	 */
	public Patron2D(ArrayList<ItemSolucion> items, double precio, int id, int cantidad, String indicaciones) {
		super(items, precio, id,cantidad, indicaciones);
	}

	@Override
	/**
	 * Dibuja el patr�n usando un lienzo: dibuja todos los items
	 */
	public void dibujarse(Graphics2D lienzo, int escala) {
		/**
		 * Dibuja todos los items tendiendo en cuenta la orientaci�n
		 */
		for (ItemSolucion item : getItems()) {
			Item representado = item.getRepresentado();
			Rectangle2D.Double figura;

			
			if (item.darOrientacion() == ItemSolucion.ORIGINAL)
				figura = new Rectangle2D.Double(PanelDibujoPatron.INICIO
						+ PanelDibujoPatron.darPixelCorrespondienteAlMM(item
								.getCoordenadaX(),escala), PanelDibujoPatron.INICIO
						+ PanelDibujoPatron.darPixelCorrespondienteAlMM(item
								.getCoordenadaY(),escala),
						PanelDibujoPatron.darPixelCorrespondienteAlMM(representado
								.getAncho(),escala),
						PanelDibujoPatron.darPixelCorrespondienteAlMM(representado
								.getAlto(),escala));
			
			
			
			else
				figura = new Rectangle2D.Double(PanelDibujoPatron.INICIO
						+ PanelDibujoPatron.darPixelCorrespondienteAlMM(item
								.getCoordenadaX(),escala), PanelDibujoPatron.INICIO
						+ PanelDibujoPatron.darPixelCorrespondienteAlMM(item
								.getCoordenadaY(),escala),
						PanelDibujoPatron.darPixelCorrespondienteAlMM(representado
								.getAlto(),escala),
						PanelDibujoPatron.darPixelCorrespondienteAlMM(representado
								.getAncho(),escala));
			
			lienzo.setColor(representado.getColor());
			lienzo.fill(figura);
			lienzo.draw(figura);
			
			lienzo.setStroke(new BasicStroke(0));
			lienzo.setColor(Color.BLACK);
			lienzo.draw(figura);
		}
		
	}
	
	/**
	 * Crea una imagen del patron
	 * @throws IOException 
	 */
	public void crearImagenPatron(String ruta) throws IOException {
		int ancho = Principal.darContenedoraConEspesor(super.darEspesorContenedora()).getAncho()*3;
		int alto = Principal.darContenedoraConEspesor(darEspesorContenedora()).getAlto()*3;
		
		BufferedImage imagenPatron = new BufferedImage(ancho+PanelDibujoPatron.INICIO_CUADRILLA+30, alto+PanelDibujoPatron.INICIO_CUADRILLA+30, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = imagenPatron.createGraphics();
		
		Rectangle2D.Double fondo = new Rectangle2D.Double(0,0,imagenPatron.getWidth(), imagenPatron.getHeight());
		g2.setColor(Color.WHITE);
		g2.fill(fondo);
		
		PanelDibujoPatron.dibujarCuadrilla(g2, ancho+PanelDibujoPatron.INICIO+10, alto+PanelDibujoPatron.INICIO+10);
		PanelDibujoPatron.dibujarContenedora(g2, ancho, alto);
		
		dibujarse(g2,30);	//Se usa una escala de 30 pixel/cm que es equivalente a 3 pixel/mm
	    File archivoImagen = new File(ruta);
	    ImageIO.write(imagenPatron, "png", archivoImagen);

	}
}
