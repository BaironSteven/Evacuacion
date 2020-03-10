package graph;

import Algoritmos.Nodo;

public class Edificio{
	
	private ELGraph<Nodo,Integer> planta = new ELGraph<>();
	
	public ELGraph<Nodo, Integer> getPlanta() {
		return planta;
	}

	public void setPlanta(ELGraph<Nodo, Integer> planta) {
		this.planta = planta;
	}	
}
