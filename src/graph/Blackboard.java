package graph;

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
		for(Object node:bd) {
			if(node instanceof Point2DAnimable)  {
				if(!((Point2DAnimable) node).getMovimientos().isEmpty()){
					return false;
				}
			}
		}
		return true;
	}
	
	public Blackboard() {
		// TODO Auto-generated constructor stub
		
	}

	/*public void paintInicial() {
		Object ob;
		for(Object node:bd) {
			ob = node;
			if(ob instanceof SimpleDrawable) 
				if(ob instanceof Square2D) 
					((SimpleDrawable) ob).paint();
		}
	}*/

}
