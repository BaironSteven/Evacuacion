package graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import auxiliar.Coordenadas;
import material.Animable;

public class Fire2DAnimable extends Fire2D implements Animable{
	private List<ArrayList<Coordenadas>> movimientos = new ArrayList<>();
	private ArrayList<Coordenadas> estado = new ArrayList<>();
	private int x,y;
	private int dimX = 0;
	private int dimY = 0;
	
	public Fire2DAnimable(int x, int y){
		super(x,y);
	}
	public Fire2DAnimable(int x, int y, List<ArrayList<Coordenadas>> movimientos, int dimX, int dimY){
		this.x=x;
		this.y = y;
		this.movimientos = movimientos;
		this.dimX = dimX;
		this.dimY = dimY;
	}
	public void anima() {
		if(movimientos.size()>0) {
			estado = movimientos.remove(0);
		}
	}
	
	public void paint(Graphics g) {
		for(Coordenadas node:estado) {
			g.setColor(Color.red);
			g.fillRect(node.getCoord_x()*dimX-1, node.getCoord_Y()*dimY-1, dimX, dimY);
			g.setColor(Color.red);
			g.drawRect(node.getCoord_x()*dimX-1, node.getCoord_Y()*dimY-1, dimX, dimY);
		}
	}

	public void paint(Graphics g, int width, int height) {
		for(Coordenadas node:estado) {
			g.setColor(Color.red);
			g.fillRect(node.getCoord_x()*dimX-1, node.getCoord_Y()*dimY-1, width, height);
			g.setColor(Color.red);
			g.drawRect(node.getCoord_x()*dimX-1, node.getCoord_Y()*dimY-1, width, height);
		}
	}
	
	public List<ArrayList<Coordenadas>> getMovimientos() {
		return movimientos;
	}
	public void setMovimientos(List<ArrayList<Coordenadas>> movimientos) {
		this.movimientos = movimientos;
	}
}
