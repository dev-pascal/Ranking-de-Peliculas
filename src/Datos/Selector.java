package Datos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;


public class Selector {
	
	private String [] datos = {"5","10","20","100","1000","TODOS"};
	private JComboBox<String> cb = new JComboBox<String>(datos);
	private int todos;
	private static Selector instancia;
	
	public static void instanciar() {
		if (instancia==null) {
			instancia = new Selector();
		}
	}
	
	private Selector () {
		try {
			this.todos = calcularTodos();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.cb.addActionListener(new Implementa());
	}
	private Selector(String[] datos, JComboBox<String> cb) {
		super();
		this.datos = datos;
		this.cb = cb;
	}
	public String[] getDatos() {
		return datos;
	}
	public void setDatos(String[] datos) {
		this.datos = datos;
	}
	public JComboBox<String> getCb() {
		return cb;
	}
	public void setCb(JComboBox<String> cb) {
		this.cb = cb;
	}
	public int getTodos() {
		return todos;
	}
	public void setTodos(int todos) {
		this.todos = todos;
	}
	public static Selector getInstancia() {
		return instancia;
	}
	
	public int calcularTodos () throws IOException {
		int lineas=0;
		FileInputStream fstream = new FileInputStream("movies.csv");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
			String strLine = br.readLine();
			strLine = br.readLine();
			while (strLine!=null) {
				lineas++;
				strLine = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fstream.close(); 
		return lineas;
	}
	
	class Implementa implements ActionListener {

		private void insertar (int filas) {
		    DefaultTableModel model = new DefaultTableModel(Tabla.getInstancia().getDatos(), Tabla.getInstancia().getHeader()){
				
				public boolean isCellEditable(int row, int column) {
					return false;
				}
				
			};
		    model.setRowCount(filas);
		    
		    //Tabla.getInstancia().setModel(model);
		    Tabla.getInstancia().getTabla().setModel(model);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//usar cb.getSelectedItem() para manipular el
			//item seleccionado en el selector
			if (!cb.getSelectedItem().equals("TODOS")) {
				insertar(Integer.parseInt((String) cb.getSelectedItem()));
			}
			else {
				insertar(todos);
			}
			
		}
		
	}
	
}
