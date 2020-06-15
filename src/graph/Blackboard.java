package graph;

import Auxiliar.Coordenadas;
import material.Animable;
import material.SimpleDrawable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Blackboard  extends JPanel{

	ArrayList bd = new ArrayList();

	ArrayList<Coordenadas> prevs = new ArrayList<>();
	ArrayList<Coordenadas> movs = new ArrayList<>();
	ArrayList<Coordenadas> duplicadosInterc= new ArrayList<>();
	ArrayList<Coordenadas> prevDuplicadosInterc= new ArrayList<>();

	public Set<Coordenadas> getDups() {
		return dups;
	}

	Set<Coordenadas> dups= new HashSet<>();

	public ArrayList<Coordenadas> getArrayDups() {
		return arrayDups;
	}

	ArrayList<Coordenadas> arrayDups= new ArrayList<>();
	ArrayList<Boolean> visitedDups= new ArrayList<>();
	ArrayList<Coordenadas> duplicadosSp = new ArrayList<>();
	ArrayList<Coordenadas>prevDuplicadosSp= new ArrayList<>();
	ArrayList<Coordenadas> dupMenosPrioritarios= new ArrayList<>();
	ArrayList<Coordenadas> prevDupMenosPrioritarios= new ArrayList<>();
	ArrayList<Coordenadas> rep= new ArrayList<>();
	ArrayList<Coordenadas> prevDups= new ArrayList<>();

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
		/*movs.clear();
		dups.clear();
		rep.clear();
		duplicadosSp.clear();
		prevDuplicadosSp.clear();
		prevs.clear();
		duplicadosInterc.clear();
		prevDuplicadosInterc.clear();
		dupMenosPrioritarios.clear();
		prevDupMenosPrioritarios.clear();
		prevDups.clear();
		visitedDups.clear();
		arrayDups.clear();*/
	}

	public void animaI(){
		Object ob;
		for(Object node:bd) {
			ob=node;
			if(ob instanceof Animable){
				((Point2DAnimable) ob).animaI();
			}
		}
		/*movs.clear();
		dups.clear();
		rep.clear();*/
	}

	public ArrayList<Boolean> getVisitedDups() {
		return visitedDups;
	}

	/*private void addDuplicates() {
		for (Object node:bd) {
			if(node instanceof Animable) {
				if (movs.contains(((Point2DAnimable) node).getMovimientos().get(0)) && (((Point2DAnimable) node).getMovimientos().size() > 0) &&
				!((Point2DAnimable) node).getMovimientos().get(0).equals(new Coordenadas(-1, -1))) {
					dups.add(((Point2DAnimable) node).getMovimientos().get(0));
				}

				movs.add(((Point2DAnimable) node).getMovimientos().get(0));
				prevs.add(((Point2DAnimable) node).getMovimientosD().get(0));
				if (!((Point2DAnimable) node).getMovimientos().get(0).equals(new Coordenadas(-1, -1)))
					if (((Point2DAnimable) node).getMovimientosD().get(0).equals(((Point2DAnimable) node).getMovimientos().get(0))) {
						rep.add(((Point2DAnimable) node).getMovimientos().get(0));
					}

			}
		}

		for(Coordenadas node:dups) {
			visitedDups.add(false);
			arrayDups.add(node);
		}

		for (Object node:bd)
			if (node instanceof Animable && (((Point2DAnimable) node).getMovimientos().size() > 0) &&
			!((Point2DAnimable) node).getMovimientos().get(0).equals(new Coordenadas(-1, -1))) {
				if (dups.contains(((Point2DAnimable) node).getMovimientos().get(0)))
					prevDups.add(((Point2DAnimable) node).getMovimientosD().get(0));
				if (prevs.contains(((Point2DAnimable) node).getMovimientos().get(0)) &&
						movs.get(prevs.indexOf(((Point2DAnimable) node).getMovimientos().get(0))).equals(((Point2DAnimable) node).getMovimientosD().get(0)))
					if (!rep.contains(((Point2DAnimable) node).getMovimientos().get(0)) &&
							!duplicadosInterc.contains(movs.get(prevs.indexOf(((Point2DAnimable) node).getMovimientos().get(0))))) {
						duplicadosInterc.add(movs.get(prevs.indexOf(((Point2DAnimable) node).getMovimientos().get(0))));
						prevDuplicadosInterc.add(((Point2DAnimable) node).getMovimientosD().get(0));
					}
			}

		for (int k = 0; k < prevDups.size(); k++)
			if (prevs.indexOf(movs.get(k)) != -1 && !movs.get(prevs.indexOf(prevDups.get(k))).equals(new Coordenadas(-1,-1)))
				if (dups.contains(prevDups.get(k)) && !duplicadosInterc.contains(prevDups.get(k)) &&
						!rep.contains(movs.get(prevs.indexOf(movs.get(k)))))
					if (!duplicadosInterc.contains(movs.get(prevs.indexOf(prevDups.get(k)))) &&
							!rep.contains(movs.get(prevs.indexOf(prevDups.get(k)))) &&
							!duplicadosSp.contains(movs.get(prevs.indexOf(prevDups.get(k))))) {
						duplicadosSp.add(movs.get(prevs.indexOf(prevDups.get(k))));
						prevDuplicadosSp.add(prevDups.get(k));
					}

		for (int m = 0; m < prevDups.size(); m++)
			if (prevs.indexOf(prevDups.get(m)) != -1 && prevs.indexOf(movs.get(m)) != -1 && !movs.get(prevs.indexOf(prevDups.get(m))).equals(new Coordenadas(-1,-1)))
				if (movs.contains(prevDups.get(m)) && !dups.contains(prevDups.get(m)) && !duplicadosInterc.contains(prevDups.get(m)))
					if (!duplicadosInterc.contains(movs.get(prevs.indexOf(prevDups.get(m)))) &&
							!duplicadosSp.contains(movs.get(prevs.indexOf(prevDups.get(m))))
							&& !rep.contains(movs.get(prevs.indexOf(movs.get(m)))) &&
							!rep.contains(movs.get(prevs.indexOf(prevDups.get(m)))) &&
							!dupMenosPrioritarios.contains(movs.get(prevs.indexOf(prevDups.get(m))))) {
						dupMenosPrioritarios.add(movs.get(prevs.indexOf(prevDups.get(m))));
						prevDupMenosPrioritarios.add(prevDups.get(m));
					}
	}*/

	public boolean isEmpty() {
		int cont = 0;
		for(Object node:bd) {
			if(node instanceof Point2DAnimable)  {
				if(!((Point2DAnimable) node).getMovimientos().isEmpty()){
					if(cont > 0){ return false; }
					cont++;
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
				if(!((Point2DAnimable) node).getMovimientos().isEmpty() && !((Point2DAnimable) node).getMovimientos().get(0).equals(new Coordenadas(-1,-1))){
					numPers++;
				}
			}
		}

		return  numPers;
	}


	/*public void paintInicial() {
		Object ob;
		for(Object node:bd) {
			ob = node;
			if(ob instanceof SimpleDrawable) 
				if(ob instanceof Point2DAnimable)
					((Point2DAnimable) ob).paint();
		}
	}*/

}
