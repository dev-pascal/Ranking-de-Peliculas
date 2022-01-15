package Ventanas;

import java.awt.*;

import javax.swing.JFrame;

import Datos.Selector;
import Datos.Tabla;
//prueba
public class Ventana extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Ventana instancia;
	private static boolean bandera = false; 
	
	private static void instanciar () {
		if (instancia == null) {
			instancia = new Ventana();
		}
	}
	
	public Ventana () {
		
		this.setLayout(new FlowLayout());
		this.setTitle("Tablero (SIN DATOS)");
		this.setSize(490,750);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		Interfaz.instanciar();
		
		this.add(Interfaz.getInstancia().getBoton());
		
		Selector.instanciar();
		
		this.add(Selector.getInstancia().getCb());
		this.add(Interfaz.getInstancia().getPanel());
		
		Tabla.instanciar();
		
		this.add(Tabla.getInstancia().getScroll());
		this.setVisible(true);
		
		repaint();
		
	}
	
	public static Ventana getInstancia () {
		return instancia;
	}
	
	/*Dibujo de Histograma*/
	public void paint(Graphics g) {
		
		super.paint(g);
		
		g.setColor(Color.WHITE);
		g.fillRect(50, 235, 380, 485);
		g.setColor(Color.BLACK);
		g.drawString("1", 125, 715);
		g.drawString("2", 175, 715);
		g.drawString("3", 225, 715);
		g.drawString("4", 275, 715);
		g.drawString("5", 325, 715);

		g.drawLine(70, 700, 70, 250);
		g.drawLine(70, 700, 400, 700);
		
		if (bandera) {
			
			int [] montos = Tabla.getInstancia().getMontos();
			
			int max = getMax(montos);
			
			int num= max/25;
			int cont = num;
			
			//esto evita que ocurra un error al seleccionar una película 
			//que no tiene ningun voto o usuario
			if (num==0) {
				cont=15;
				num=15;
			}

			for (int i=1;i<28;i++) {
				g.drawString(num+"", 25, 700-16*i); //mide la escala entre cada linea del eje y
				g.drawLine(65, 700-16*i, 75, 700-16*i); //traza las lineas del eje y
				num+=cont;
			}
			//g.drawLine(65, 420-16, 1565, 420-16);
			
			num = max/15;
			
			if (num==0) {
				num=15;
			}
			
			this.setTitle("Tablero (DATOS CARGADOS)");
			
			//se usó regla de tres para calcular de cuanto sería la escala
			//dependiendo de la pelicula que se elija. 
			//por ejemplo, la primera vez que se ejecute el programa la escala
			//del eje y va a ser de 3995 y esos 3995 representan 16 px.
			//Entonces, ¿Cuánto va a valer en pixeles si (por ej) montos[0]=4181?
			//3995 --- 16 px
			//4181 --- X = ...
			
			g.setColor(Color.BLACK);
			for (int i=0, j=120; i<montos.length; i++, j+=50) {
				g.fillRect(j, 700, 20, -16*montos[i]/num);
			}
			
		}
	
	}
	
	public int getMax (int [] v) {
		int max=Integer.MIN_VALUE;
		for (int i=0;i<v.length;i++) {
			if (v[i]>max) {
				max=v[i];
			}
		}
		return max;
	}
	
	public static void setBandera (boolean otraBandera) {
		bandera=otraBandera;
	}
	
	public static void main (String args[]) {
		Ventana.instanciar();
	}
	
}
