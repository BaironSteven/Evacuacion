package graph;

import auxiliar.Coordenadas;
import material.Animable;

import java.awt.*;
import java.util.ArrayList;

public class Point2DAnimable extends Point2D implements Animable{


	private ArrayList<Coordenadas> movimientos = new ArrayList<>();

	private Blackboard blackboard;

	private int dimX = 0;
	private int dimY = 0;
	
	public Point2DAnimable(int x, int y, Blackboard b){
		super(x,y);
		this.blackboard = b;
	}

	public Point2DAnimable(int x, int y, ArrayList<Coordenadas> movimientos, int dimX, int dimY, Blackboard b){
		this.x=x;
		this.y = y;
		this.movimientos = movimientos;
		//this.movimientosD= (ArrayList<Coordenadas>) this.movimientos.clone();
		//this.movimientosD.add(0, new Coordenadas(-1,-1));
		this.dimX = dimX;
		this.dimY = dimY;
		this.blackboard = b;
		int cont = 0;
	}

	public Point2DAnimable(int x, int y, int dimX, int dimY) {
		this.x=x;
		this.y = y;
		this.dimX = dimX;
		this.dimY = dimY;
	}

	public void resize(int x, int y, int dimX, int dimY) {
		this.x=x;
		this.y = y;
		this.dimX = dimX;
		this.dimY = dimY;
	}

	public void anima() {

		/*if(!blackboard.getDups().isEmpty()) {

			if(movimientos.size()>0&&blackboard.getRep().contains(movimientos.get(0))) {
				if (!blackboard.getRep().contains(movimientosD.get(0))) {
					movimientos.add(0,movimientosD.get(0));
					movimientosD.add(0,movimientosD.get(0));
				}
			} else if(movimientos.size()>0&&blackboard.getDuplicadosInterc().contains(movimientos.get(0))) {
				if (!blackboard.prevDuplicadosInterc.contains(movimientosD.get(0))) {
					movimientos.add(0,movimientosD.get(0));
					movimientosD.add(0,movimientosD.get(0));
				}
			} else if(movimientos.size()>0&&blackboard.getDuplicadosSp().contains(movimientos.get(0))) {
				if (!blackboard.prevDuplicadosSp.contains(movimientosD.get(0))) {
					movimientos.add(0,movimientosD.get(0));
					movimientosD.add(0,movimientosD.get(0));
				}
			} else if(movimientos.size()>0&&blackboard.dupMenosPrioritarios.contains(movimientos.get(0))) {
				if (!blackboard.prevDupMenosPrioritarios.contains(movimientosD.get(0))) {
					movimientos.add(0,movimientosD.get(0));
					movimientosD.add(0,movimientosD.get(0));
				}
			} else if (!movimientos.get(0).equals(new Coordenadas(-1,-1)) && blackboard.getDups().contains(movimientos.get(0))){
					if( blackboard.getVisitedDups().get(blackboard.getArrayDups().indexOf(movimientos.get(0))) == false){
						blackboard.getVisitedDups().set(blackboard.getArrayDups().indexOf(movimientos.get(0)),true);
					} else {
						movimientos.add(0,movimientosD.get(0));
						movimientosD.add(0,movimientosD.get(0));
					}
			}
		}*/

		if(movimientos.size()>0) {
			Coordenadas mov = movimientos.remove(0);
			//movimientosD.remove(0);
			x=mov.getCoord_x()*dimX;
			y=mov.getCoord_Y()*dimY;
		}
	}

	public void animaI() {

		if(movimientos.size()>0) {
			Coordenadas mov = movimientos.remove(0);
			x=mov.getCoord_x()*dimX;
			y=mov.getCoord_Y()*dimY;
		}
	}

	/*private boolean collision() {
		return b.racquet.getBounds().intersects(getBounds());
	}*/
	
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
		//this.width = width;
		//this.height = height;
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
