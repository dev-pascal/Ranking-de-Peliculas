package Datos;

public class Pelicula implements Comparable<Pelicula>{
	private int id;
	private String title;
	private int [] ratings= new int [5];
	private int usuarios;
	private int promedio;
	
	public Pelicula() {
	}
	public Pelicula(int id) {
		this.id=id;
	}
	public Pelicula (String title) {
		this.title=title;
	}
	public Pelicula(int id, String title) {
		this.id=id;
		this.title=title;
	}
	public Pelicula(int id, String title, int [] ratings) {
		this.id=id;
		this.title=title;
		this.ratings=ratings;
	}
	public int calculaVotosPromedio () {
		int suma=0;
		for (int i=0;i<ratings.length;i++) {
			suma+=(i+1)*ratings[i];
		}
		this.promedio=suma/ratings.length;
		return this.promedio;
	}
	public int calculaUsuarios () {
		for (int i=0;i<ratings.length;i++) {
			this.usuarios+=ratings[i];
		}
		return this.usuarios;
	}
	public int getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(int usuarios) {
		this.usuarios = usuarios;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int [] getRatings() {
		return ratings;
	}
	public int getPromedio() {
		return promedio;
	}
	public void setPromedio(int promedio) {
		this.promedio = promedio;
	}
	public void setRatings(int [] ratings) {
		this.ratings = ratings;
	}
	public boolean equals (Pelicula pelicula) {
		return (this.getTitle().equals(pelicula.getTitle()));
	}
	public int compareTo (Pelicula o) {
		return this.getUsuarios()-o.getUsuarios();
	}
	
	
}
