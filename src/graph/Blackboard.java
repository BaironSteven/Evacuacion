package graph;

import auxiliar.Coordenadas;
import material.Animable;
import material.SimpleDrawable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Blackboard  extends JPanel{

	ArrayList bd = new ArrayList();

	public ArrayList getBd() {
		return bd;
	}

	@Override
	public void removeAll() {
		bd.clear();
	}

	public void borrarPuntos()
	{
		int lim = bd.size();
		for (int i = 0; i < lim; i++) {
			if (bd.get(i) instanceof Point2DAnimable) {
				bd.remove(i);
				i--;
				lim = bd.size();
			}
		}
	}

	public void addElement(Object elem) {bd.add(elem);}
	
	@Override
	public void paint (Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Object ob;
		for(Object node:bd) {
			ob = node;
			if(ob instanceof SimpleDrawable) {
				if(ob instanceof Square2D) {
					((SimpleDrawable) ob).paint(g);
				} else {
					((SimpleDrawable) ob).paint(g);
				} 
				
			}
		}
	}
	
	public void anima(){
		Object ob;
		for(Object node:bd) {
			ob=node;
			if(ob instanceof Animable){
				((Animable) ob).anima();
			}
		}

	}


	public boolean isEmpty() {
		//int cont = 0;
		for(Object node:bd) {
			if(node instanceof Point2DAnimable)  {
				if(!((Point2DAnimable) node).getMovimientos().isEmpty()){
					/*if(cont > 0){*/ return false; //}
					//cont++;
				}
			}
		}
		return true;
	}
	
	public Blackboard() {
		// TODO Auto-generated constructor stub
		
	}

	public int personasRestantes() {
		int numPers = 0;
		for(Object node:bd) {
			if (node instanceof Point2DAnimable) {
				try {
					if (!((Point2DAnimable) node).getMovimientos().isEmpty() && !((Point2DAnimable) node).getMovimientos().get(0).equals(new Coordenadas(-1, -1))) {
						numPers++;
					}
				} catch (IndexOutOfBoundsException e) {
					numPers = 0;
				}
			}
		}

		return  numPers;
	}


}
