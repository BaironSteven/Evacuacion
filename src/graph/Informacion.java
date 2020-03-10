package graph;

import Auxiliar.Coordenadas;

public class Informacion{

	private int pos;
	private boolean disponible = true;
	private Coordenadas coord;
	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public Informacion(int pos, Coordenadas coord) {
		super();
		this.pos = pos;
		disponible = true;
		this.coord = coord;
	}

	public Informacion() {
		// TODO Auto-generated constructor stub
	}

	public Coordenadas getCoord() {
		return coord;
	}

	public void setCoord(Coordenadas coord) {
		this.coord = coord;
	}

}
