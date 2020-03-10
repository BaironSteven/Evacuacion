package graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import Auxiliar.Coordenadas;
import material.Animable;

public class Point2DAnimable extends Point2D implements Animable{
	
	private ArrayList<Coordenadas> movimientos = new ArrayList<>();
	private int dimX = 0;
	private int dimY = 0;
	
	public Point2DAnimable(int x, int y){
		super(x,y);
	}
	public Point2DAnimable(int x, int y, ArrayList<Coordenadas> movimientos, int dimX, int dimY){
		this.x=x;
		this.y = y;
		this.movimientos = movimientos;
		this.dimX = dimX;
		this.dimY = dimY;
	}
	public void anima() {
		if(movimientos.size()>0) {
			Coordenadas mov = movimientos.remove(0);
			x=mov.getCoord_x()*dimX;
			y=mov.getCoord_Y()*dimY;
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.blue);
		g.fillOval(x-1, y-1, dimX, dimY);
		g.setColor(Color.black);
		g.drawOval(x-1, y-1, dimX, dimY);
	}

	@Override
	public void paint(Graphics g, int width, int height) {
		g.setColor(Color.blue);
		g.fillOval(x-1, y-1, width, height);
		g.setColor(Color.black);
		g.drawOval(x-1, y-1, width, height);
	}
	
	public ArrayList<Coordenadas> getMovimientos() {
		return movimientos;
	}
	public void setMovimientos(ArrayList<Coordenadas> movimientos) {
		this.movimientos = movimientos;
	}
}
