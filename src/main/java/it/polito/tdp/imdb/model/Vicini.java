package it.polito.tdp.imdb.model;

public class Vicini {

	private Director d;
	private double peso;
	public Vicini(Director d, double peso) {
		super();
		this.d = d;
		this.peso = peso;
	}
	public Director getD() {
		return d;
	}
	public void setD(Director d) {
		this.d = d;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return d.toString() + ", attori condivisi: " + peso;
	}
	
	
}
