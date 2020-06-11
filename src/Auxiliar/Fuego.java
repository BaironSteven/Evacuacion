package Auxiliar;

import java.util.ArrayList;
import java.util.List;

public class Fuego {
	private List<ArrayList<Coordenadas>> estado;
	private  List<ArrayList<Coordenadas>> fuego;

	public List<ArrayList<Coordenadas>> getEstado() {
		return estado;
	}
	public void setEstado(List<ArrayList<Coordenadas>> estado) {
		this.estado = estado;
	}
	public  List<ArrayList<Coordenadas>> getFuego() {
		return fuego;
	}
	public void setFuego(List<ArrayList<Coordenadas>> fuego) {
		this.fuego = fuego;
	}
}
