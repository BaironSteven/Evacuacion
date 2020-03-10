package Auxiliar;

import java.util.ArrayList;
import java.util.List;

public class Fuego {
	private static List<ArrayList<Coordenadas>> estado;
	private static List<ArrayList<Coordenadas>> fuego;
	public static List<ArrayList<Coordenadas>> getEstado() {
		return estado;
	}
	public static void setEstado(List<ArrayList<Coordenadas>> estado) {
		Fuego.estado = estado;
	}
	public static List<ArrayList<Coordenadas>> getFuego() {
		return fuego;
	}
	public static void setFuego(List<ArrayList<Coordenadas>> fuego) {
		Fuego.fuego = fuego;
	}
}
