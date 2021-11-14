package Ventanas;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;

import Datos.Tabla;

public class Interfaz {
	private JPanel panel = new JPanel();
	private JButton boton = new JButton("Procesar Datos");
	private JButton cantUsuarios_btn = new JButton("<html>"+"???"+"<br/>Usuarios</html>");
	private JButton cantPeliculas_btn = new JButton("<html>"+"???"+"<br/>Películas</html>");
	private JButton cantVotos_btn = new JButton("<html>"+"???"+"<br/>Cant. de votos</html>");
	private static Interfaz instancia;
	
	private Interfaz () {
		
		boton.addActionListener(new Implementa());
		
		panel.setLayout(new GridLayout(1,3));

		cantUsuarios_btn.setEnabled(false);
		cantPeliculas_btn.setEnabled(false);
		cantVotos_btn.setEnabled(false);
		
		panel.add(cantUsuarios_btn);
		panel.add(cantPeliculas_btn);
		panel.add(cantVotos_btn);
		
	}
	
	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public JButton getBoton() {
		return boton;
	}

	public void setBoton(JButton boton) {
		this.boton = boton;
	}

	public static void instanciar () {
		if (instancia==null) {
			instancia = new Interfaz();
		}
	}
	
	public static Interfaz getInstancia () {
		return instancia;
	}
	
	class Implementa implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			boton.setEnabled(false);
			new Thread(new Concurrencia()).start();
		}
		
		class Concurrencia implements Runnable {

			@Override
			public void run() {
				try {
					Tabla.getInstancia().cargarDatos();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Ventana.getInstancia().setBandera(true);
				Ventana.getInstancia().paint(Ventana.getInstancia().getGraphics());
				cantUsuarios_btn.setText("<html>"+(Tabla.getInstancia().calcularTotal()+"")+"<br/>Usuarios</html>");
				cantVotos_btn.setText("<html>"+(Tabla.getInstancia().calcularTotal()+"")+"<br/>Cant. de votos</html>");
				cantPeliculas_btn.setText("<html>"+Tabla.getInstancia().getPeliculas().size()+"<br/>Películas</html>");
			}
			
		}
	}

}
