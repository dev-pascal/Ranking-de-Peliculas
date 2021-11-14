package Datos;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import Ventanas.Ventana;


public class Tabla {
	
	private final int columnas = 3;
	private int filas = 5;
	private String [] header = {"Nombre de película","Usuarios","Votos"};
	private String [][] datos = new String [Selector.getInstancia().getTodos()][columnas];
	private DefaultTableModel model = new DefaultTableModel(datos, header) {
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private JTable tabla = new JTable(model);
	private JScrollPane scroll = new JScrollPane(tabla);
	private static Tabla instancia;
	private LinkedHashMap<Integer, Pelicula> peliculas = new LinkedHashMap<Integer, Pelicula>();
	private List<Pelicula> lista = new ArrayList<Pelicula>();
	private static int [] montos = new int [5];
	
	public static void instanciar () {
		if (instancia==null) {
			instancia = new Tabla();
		}
	}
	
	private Tabla () {
		scroll.setPreferredSize(new Dimension(450,103));
		model.setRowCount(filas);
    	model.setColumnCount(columnas);
    	//tabla.setEnabled(false);
	}
	
	public int calcularTotal () {
		int suma=0;
		for (int i=0;i<montos.length;i++) {
			suma+=(i+1)*montos[i];
		}
		return suma;
	}
	public void cargarDatos () throws IOException {
		cargarPeliculas();
		cargarRankings();
		
		for (Map.Entry<Integer, Pelicula> entry : peliculas.entrySet()) {
		   
			Pelicula value = entry.getValue();
		    value.calculaUsuarios();
			value.calculaVotosPromedio();
			lista.add(value);
		}
		
		lista.sort((o2, o1)->o1.compareTo(o2));
		
		int I=0;
		for (Pelicula i: lista) {
			for (int j=0;j<columnas;j++) {
				 if (j==0) {
					  datos[I][j]=i.getTitle();
				 }
				 else {
					 if (j==1) {
						 datos[I][j]=i.getUsuarios()+"";
					 }
					 else {
						 if (j==2) {
							 datos[I][j]=i.getPromedio()+"";
						 }
					 }
				 }
			}
			I++;
		}
		
		model = new DefaultTableModel(datos, header) {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
			
		};
		model.setRowCount(filas);
		tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			
			private boolean buscarPelicula (Pelicula pelicula, Pelicula resultado) {
				for (Pelicula i: lista) {
					if (i.equals(pelicula)) {
						resultado.setId(i.getId());
						resultado.setPromedio(i.getPromedio());
						resultado.setRatings(i.getRatings());
						resultado.setTitle(i.getTitle());
						resultado.setUsuarios(i.getUsuarios());
						return true;
					}
				}
				return false;
			}
			
			public void valueChanged(ListSelectionEvent arg0) {
				int fila = tabla.getSelectedRow();
				
				if (!arg0.getValueIsAdjusting() && fila>=0) {
					
					String title = (String)tabla.getModel().getValueAt(fila, 0);
					
					Pelicula aux = new Pelicula(title);
					Pelicula resultado = new Pelicula();
					
					if (buscarPelicula(aux,resultado)) {
						Tabla.setMontos(resultado.getRatings());
						//aca justifica que Ventana sea un singleton
						Ventana.getInstancia().paint(Ventana.getInstancia().getGraphics());
					}
					
				}
			}
			
		});
		tabla.setModel(model);
	} 
	
	private void cargarPeliculas () throws IOException {
		FileInputStream fstream = new FileInputStream("movies.csv");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
			String strLine = br.readLine();
			strLine = br.readLine();
			while (strLine!=null) {
				String ID="", Title="";
				int cant=0;
				boolean cerrado = false;
				for (int i=0;cant!=2;i++) {
					char car = strLine.charAt(i);
					if (car==',' && !cerrado) {
						cant++;
					}
					if (car=='"') {
						if (!cerrado) {
							cerrado=true;
						}
						else {
							cerrado=false;
						}
					}
					if (cant==0) {
						ID+=car;
					}
					else {
						if (!cerrado) {
							if (cant==1 && car!=',' && car!='"') {
								Title+=car;
							}
						}
						else {
							if (cant==1 && car!='"') {
								Title+=car;
							}
						}
					}
				}
				peliculas.put(Integer.parseInt(ID), new Pelicula(Integer.parseInt(ID), Title));
				strLine = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fstream.close();
	}
	private void cargarRankings () throws IOException {
		FileInputStream fstream = new FileInputStream("ratings.csv");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
			int lineas=0;
			String strLine = br.readLine();
			strLine = br.readLine();
			while (strLine!=null) {
				String ID="", rating = "";
				int cant=0;
				String decimal = "";
				boolean bandera = false;
				for (int i=0; cant!=3; i++) {
					if (strLine.charAt(i)==',') {
						cant++;
					}
					if (cant==0) {
						//something
					}
					else {
						//lectura de ID
						if (cant==1 && strLine.charAt(i)!=',') {
							ID+=strLine.charAt(i);
						}
						else {
							if (cant==2) {
								if (strLine.charAt(i)=='.') {
									bandera=true;
								}
								else {
									if (bandera) {
										if (strLine.charAt(i)!=',') {
											decimal+=strLine.charAt(i);
										}
									}
									else {
										if (strLine.charAt(i)!=',') {
											rating+=strLine.charAt(i);
										}
									}
								}
							}
						}
					}
				}
				if (Integer.parseInt(decimal)>=5) {
					int aux = Integer.parseInt(rating);
					aux++;
					rating = aux + "";
				}
				Pelicula p = peliculas.get(Integer.parseInt(ID));
				p.getRatings()[(Integer.parseInt(rating)-1)]++;
				montos[(Integer.parseInt(rating)-1)]++;
				lineas++;
				strLine = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fstream.close(); 
	}
	
	public static void setMontos(int [] otrosMontos) {
		montos=otrosMontos;
	}
	
	public int [] getMontos () {
		return this.montos;
	}
	
	public String[] getHeader() {
		return header;
	}

	public void setHeader(String[] header) {
		this.header = header;
	}

	public static void setInstancia(Tabla instancia) {
		Tabla.instancia = instancia;
	}

	public int getFilas() {
		return filas;
	}

	public void setFilas(int filas) {
		this.filas = filas;
	}

	public DefaultTableModel getModel() {
		return model;
	}

	public JTable getTabla() {
		return tabla;
	}

	public void setTabla(JTable tabla) {
		this.tabla = tabla;
	}

	public int getColumnas() {
		return columnas;
	}

	public void setScroll(JScrollPane scroll) {
		this.scroll=scroll;
	}

	public JScrollPane getScroll() {
		return scroll;
	}

	public static Tabla getInstancia() {
		return instancia;
	}
	
	public String[][] getDatos() {
		return datos;
	}

	public void setDatos(String[][] datos) {
		this.datos = datos;
	}

	public LinkedHashMap<Integer, Pelicula> getPeliculas() {
		return peliculas;
	}
	
}
