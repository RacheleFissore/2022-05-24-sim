package it.polito.tdp.itunes.model;

public class Adiacenza implements Comparable<Adiacenza> {
	private Track vertice1;
	private Track vertice2;
	private int peso;
	public Adiacenza(Track vertice1, Track vertice2, int peso) {
		super();
		this.vertice1 = vertice1;
		this.vertice2 = vertice2;
		this.peso = peso;
	}
	public Track getVertice1() {
		return vertice1;
	}
	public void setVertice1(Track vertice1) {
		this.vertice1 = vertice1;
	}
	public Track getVertice2() {
		return vertice2;
	}
	public void setVertice2(Track vertice2) {
		this.vertice2 = vertice2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public int compareTo(Adiacenza o) {
		return -(this.peso - o.peso);
	}
	@Override
	public String toString() {
		return vertice1.getName() + ", " + vertice2.getName() + " --> " + peso;
	}
	
	
	
}
