package Algoritmos;

import Auxiliar.Coordenadas;
import Heuristica.DistanciaLineaRecta;
import Heuristica.InterfazHeuristica;
import graph.ELGraph;
import material.Graph;
import material.ListaNodosOrdenados;
import material.Vertex;

import java.util.ArrayList;


public class Algoritmo_A_Estrella {

        private static InterfazHeuristica heuristic = new DistanciaLineaRecta();
      
        private static ArrayList<Nodo> listaCerrada = new ArrayList<>();
        private static ListaNodosOrdenados listaAbierta = new ListaNodosOrdenados();
        private static Recorrido caminoMasCorto;

        
        public static ArrayList<Recorrido> algoritmo(Vertex<Nodo> posicion, Graph elGraph) {
    		int opcion = ((ELGraph)elGraph).getAlgSeleccionado();
    		ArrayList<Recorrido> recorridos = new ArrayList<Recorrido>();
    		switch(opcion){
    			case 2: recorridos = calcularCaminoMasCorto(posicion, (ELGraph<Nodo, Integer>)elGraph);
    				break;
    			case 1: recorridos = calcularCaminoMasCorto(posicion,(ELGraph<Nodo, Integer>) elGraph);
    				break;
    			case 3: recorridos = A_Estrella_Fire(posicion, (ELGraph<Nodo, Integer>)elGraph);
    				break;
    		}
    		return recorridos;
    	}

        public static ArrayList<Recorrido> calcularCaminoMasCorto(Vertex<Nodo> pos, ELGraph<Nodo, Integer> elGraph ) {
        		ArrayList<Recorrido> recorridos = new ArrayList<Recorrido>();
                for(Coordenadas door : elGraph.getDoors()) {
                 	elGraph.vertices().forEach((p)->{p.getElement().setPreviousNode(null);
                 	p.getElement().setDistanceFromStart(Integer.MAX_VALUE);
                     p.getElement().setHeuristicDistanceFromGoal(0);
                 	});
                	listaCerrada = new ArrayList<>();
                    listaAbierta = new ListaNodosOrdenados();
	                if (elGraph.getNodo(door.getCoord_x(), door.getCoord_Y()).isObstacle()) {
	                        return null;
	                }
	
	                pos.getElement().setDistanceFromStart(0);
	                listaCerrada.clear();
	                listaAbierta.clear();
	                listaAbierta.add(pos.getElement());
	                
	                while(listaAbierta.size() != 0) {
	                        Nodo actual = listaAbierta.getFirst();
	
	                        if(actual.getX() == door.getCoord_x() && actual.getY() == door.getCoord_Y()) {
	                        	recorridos.add(reconstruirCamino(actual).addInicial(pos.getElement())); break;
	                        }
	
	                        listaAbierta.remove(actual);
	                        listaCerrada.add(actual);
	
	                        for(Nodo vecino : actual.getNeighborList()) {
	                                boolean esMejorVecino;
	
	                                if (listaCerrada.contains(vecino))
	                                        continue;
	
	                                if (!vecino.isObstacle()) {
	
	                                        float distanciaDesdeElPrincipioAlVecino = (actual.getDistanceFromStart() 
	                                        		+ elGraph.getDistanceBetween(actual, vecino));
	
	                                       
	                                        if(!listaAbierta.contains(vecino)) {
	                                        	listaAbierta.add(vecino);
	                                        	esMejorVecino = true;
	                                                
	                                        } else if(distanciaDesdeElPrincipioAlVecino < actual.getDistanceFromStart()) {
	                                        	esMejorVecino = true;
	                                        } else {
	                                        	esMejorVecino = false;
	                                        }
	                                       
	                                        if (esMejorVecino) {
	                                        	vecino.setPreviousNode(actual);
	                                        	vecino.setDistanceFromStart(distanciaDesdeElPrincipioAlVecino);
	                                        	vecino.setHeuristicDistanceFromGoal(heuristic.calcularDistanciaAMeta(vecino.getX(), vecino.getY(),
	                                        			door.getCoord_x(), door.getCoord_Y()));
	                                        }
	                                }
	                                
	                        }
	                }
	               
                }
        
                return recorridos;
        }
        
        public static ArrayList<Recorrido> A_Estrella_Fire(Vertex<Nodo> pos, ELGraph<Nodo, Integer> elGraph) {
        	ArrayList<Recorrido> recorridos = new ArrayList<Recorrido>();
    		
            for(Coordenadas door : elGraph.getDoors()) {
            	elGraph.vertices().forEach((p)->{p.getElement().setPreviousNode(null);
             	p.getElement().setDistanceFromStart(Integer.MAX_VALUE);
                 p.getElement().setHeuristicDistanceFromGoal(0);
             	});
            	listaCerrada = new ArrayList<>();
                listaAbierta = new ListaNodosOrdenados();
                if (elGraph.getNodo(door.getCoord_x(), door.getCoord_Y()).isObstacle()) {
                        return null;
                }

                pos.getElement().setDistanceFromStart(0);
                listaCerrada.clear();
                listaAbierta.clear();
                listaAbierta.add(pos.getElement());
                
                while(listaAbierta.size() != 0) {
                        Nodo actual = listaAbierta.getFirst();

                        if(actual.getX() == door.getCoord_x() && actual.getY() == door.getCoord_Y()) {
                                recorridos.add(reconstruirCamino(actual).addInicial(pos.getElement())); break;
                        }

                        listaAbierta.remove(actual);
                        listaCerrada.add(actual);

                        for(Nodo vecino : actual.getNeighborList()) {
                                boolean esMejorVecino;

                                if (listaCerrada.contains(vecino))
                                        continue;

                                if (!vecino.isObstacle()&&
                                		!(elGraph.getPosFuego().get(1).contains(new Coordenadas(vecino.getX(),vecino.getY())))) {

                                        float distanciaDesdeElPrincipioAlVecino = (actual.getDistanceFromStart() 
                                        		+ elGraph.getDistanceBetween(actual, vecino));

                                       
                                        if(!listaAbierta.contains(vecino)) {
                                        	listaAbierta.add(vecino);
                                        	esMejorVecino = true;
                                                
                                        } else if(distanciaDesdeElPrincipioAlVecino < actual.getDistanceFromStart()) {
                                        	esMejorVecino = true;
                                        } else {
                                        	esMejorVecino = false;
                                        }
                                       
                                        if (esMejorVecino) {
                                        	vecino.setPreviousNode(actual);
                                        	vecino.setDistanceFromStart(distanciaDesdeElPrincipioAlVecino);
                                        	vecino.setHeuristicDistanceFromGoal(heuristic.calcularDistanciaAMeta(vecino.getX(), vecino.getY(),
                                        			door.getCoord_x(), door.getCoord_Y()));
                                        }
                                }
                                
                        }

                }
             
               
            }
           
            return recorridos;
        	
    	}
        
        
        private static Recorrido reconstruirCamino(Nodo nodo) {
                Recorrido camino = new Recorrido();
                while(!(nodo.getPreviousNode() == null)) {
                	camino.prependWayPoint(nodo);
                    nodo = nodo.getPreviousNode();
                }
                caminoMasCorto = camino;
                return camino;
        }

        
        public Recorrido getCaminoMasCorto(){
        	return caminoMasCorto;
        }
}
