package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import Algoritmos.Recorrido;
import Algoritmos.Nodo;
import Auxiliar.Coordenadas;
import Auxiliar.FicheroPosiciones;
import Auxiliar.Fuego;
import Auxiliar.FicheroEdificio;
import graph.ELGraph;
import graph.Edificio;
import graph.Informacion;
import material.Vertex;

public class Menu {
	
	private static  Edificio e;
	private static List<Coordenadas> fin;
	private static List<Integer> opciones;
	private static List<ArrayList<Coordenadas>> a;
	private static Fuego fAUX;
	private static List<ArrayList<Coordenadas>> b;
	private static FicheroEdificio lec;
	private static FicheroPosiciones pos;
	private static ArrayList<Coordenadas> asignacion;
	
	public Menu() {
		e = new Edificio();
		fin = new ArrayList<>();
		opciones = Arrays.asList(1,2,3,4);
		a = new ArrayList<>();
		fAUX = new Fuego();
		b =new ArrayList<>();
		lec = null;
		pos = null;
		ArrayList<Coordenadas> asignacion = new ArrayList<>();
	}

	
	public static Edificio getE() {
		return e;
	}

	public static void setE(Edificio e) {
		Menu.e = e;
	}

	
	private static Scanner sc;
	
	
	
	static <T> List<List<Integer>> transpose(List<ArrayList<Integer>> list) {
        List<List<Integer>> ret = new ArrayList<List<Integer>>();
        final int N = list.get(0).size();
        for (int i = 0; i < N; i++) {
            List<Integer> col = new ArrayList<>();
            for (List<Integer> row : list) {
                col.add(row.get(i));
            }
            ret.add(col);
        }
        return ret;
    }
	
	private static void completarEstado(int mayor) {
		for(int y=0;y<e.getPlanta().getEstado().size();y++) 
			for(int k = e.getPlanta().getEstado().get(y).size();k<mayor;k++)
				e.getPlanta().getEstado().get(y).add(new Coordenadas(-1,-1));
	}

	private static void completarEstadoInt(int mayor) {
		for(int y=0;y<e.getPlanta().getEstadoInt().size();y++) 
			for(int k = e.getPlanta().getEstadoInt().get(y).size();k<mayor;k++)
				e.getPlanta().getEstadoInt().get(y).add(-1);
	}
	
	private static int mayorDim(ArrayList<Integer> dims) {
		int ant =-1;
		for(int i = 0; i<dims.size();i++) 
			if(dims.get(i)>ant) 
				ant = dims.get(i);
		return ant;
	}
		
	private static int menorDim(ArrayList<Integer> dims, int tam) {
		int ant = tam;
		for(int i = 0; i<dims.size();i++) 
			if(dims.get(i)<ant) 
				ant = dims.get(i);
		return ant;
	}
	
	private static void calcularDim(ArrayList<Integer> dims){
		for(int i = 0;i<e.getPlanta().getDoors().size();i++)
			for(int size = 0;size<e.getPlanta().getEstado().size();size++) 
				if((dims.get(i)<e.getPlanta().getEstado().get(size).size()-1)&&(e.getPlanta().getEstado().get(size).get(e.getPlanta().getEstado().get(size).size()-1).equals(e.getPlanta().getDoors().get(i))))
					dims.set(i, e.getPlanta().getEstado().get(size).size()-1);
	}
	
	private static void calcularDimAux(ArrayList<Integer> dims){
		for(int i = 0;i<e.getPlanta().getDoors().size();i++)
			for(int size = 0;size<e.getPlanta().getEstadoAux().size();size++) 
				if((dims.get(i)<e.getPlanta().getEstadoAux().get(size).size()-1)&&(e.getPlanta().getEstadoAux().get(size).get(e.getPlanta().getEstadoAux().get(size).size()-1).equals(e.getPlanta().getDoors().get(i))))
					dims.set(i, e.getPlanta().getEstadoAux().get(size).size()-1);
	}
	
	private static Set<Coordenadas> generarNumsAleatorios(FicheroEdificio lec, int pers) {
		int cont = 0;
	
		Set<Coordenadas> nums = new HashSet<>();
		int disp = 0;
		for(Informacion baldosas:lec.getBaldosas()) {
			if(baldosas.getPos()==1)
				disp++;
		}
		
		// Instanciamos la clase Random
		Random random = new Random();

		int personas = disp*pers/100;
		while (cont < personas){
			// Elegimos un índice al azar
			int rN2 = random.nextInt(Integer.parseInt(lec.getDimensiones()[0]));
			int rN1 = random.nextInt(Integer.parseInt(lec.getDimensiones()[1]));
			if (!e.getPlanta().getObstacles().contains(new Coordenadas(rN2,rN1))&&!e.getPlanta().getDoors().contains(new Coordenadas(rN2,rN1))&&!nums.contains(new Coordenadas(rN2,rN1))) {
			      nums.add(new Coordenadas(rN2,rN1));
			      cont ++;
			}
		}
		return nums;
	}
	
		
	private static Vertex<Nodo> generarGraphAndPeople(ArrayList<Vertex<Nodo>> people, Set<Coordenadas> numbers, String[] dimensiones, Coordenadas posIniFuego,
			ArrayList<Informacion> edificio) {

		int x= Integer.parseInt(dimensiones[0]);
		int y = Integer.parseInt(dimensiones[1]);
		List<Vertex<Nodo>> l = new ArrayList<Vertex<Nodo>>();
		Vertex<Nodo> iniFuego = null;
		Nodo nodoP;
		Coordenadas coord;
		for( int i = 0 ; i < x; i++ ){
			for(int j = 0; j <y; j++) {
				nodoP = new Nodo(i,j,e.getPlanta());
				Vertex<Nodo> vertex =   e.getPlanta().insertVertex(nodoP);
				l.add(vertex);
				coord = new Coordenadas(i,j);
				if(numbers.contains(coord)) {
					people.add(vertex);	
				}
				if(edificio.get(i+j*x).getPos()==0)
					e.getPlanta().getObstacles().add(coord);
				if(edificio.get(i+j*x).getPos()==2)
					e.getPlanta().getDoors().add(coord);
				if(vertex.getElement().getX()==posIniFuego.getCoord_x() && vertex.getElement().getY() == posIniFuego.getCoord_Y())
					iniFuego = vertex;
			}
		}
		
		for(Vertex<Nodo> aux:e.getPlanta().vertices()) {
			aux.getElement().setObstacle( e.getPlanta().getObstacles().contains(new Coordenadas(aux.getElement().getX(),aux.getElement().getY())));
		}
		
		for( int x0 = 0 ; x0 < x; x0++ ){
			for(int y0 = 0; y0 <y; y0++) {
				if(x0+1 < x || y0+1 < y) {
					if((y0+1)==y) {
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0), 1); 
					}
					else if((x0+1)==x) {
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get(x0*y+y0+1), 1);
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0-1)*y+y0+1), 1);					
					} else if(x0==0){
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get(x0*y+y0+1), 1);
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0), 1); 
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0+1), 1);
					} else {
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get(x0*y+y0+1), 1);
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0), 1); 
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0+1), 1);
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0-1)*y+y0+1), 1);
					}
					Nodo node = e.getPlanta().getNodo(x0,y0);
			        if (!(y0==0))
			                node.setNorth(e.getPlanta().getNodo(x0,y0-1));
			        if (!(y0==0) && !(x0==x-1))
			                node.setNorthEast(e.getPlanta().getNodo(x0+1,y0-1));
			        if (!(x0==x-1))
			                node.setEast(e.getPlanta().getNodo(x0+1,y0));
			        if (!(x0==x-1) && !(y0==y-1))
			                node.setSouthEast(e.getPlanta().getNodo(x0+1,y0+1));
			        if (!(y0==y-1))
			                node.setSouth(e.getPlanta().getNodo(x0,y0+1));
			        if (!(x0==0) && !(y0==y-1))
			                node.setSouthWest(e.getPlanta().getNodo(x0-1,y0+1));
			        if (!(x0==0))
			                node.setWest(e.getPlanta().getNodo(x0-1,y0));
			        if (!(x0==0) && !(y0==0))
			                node.setNorthWest(e.getPlanta().getNodo(x0-1,y0-1));
				}
			}
		}
		
		return iniFuego;
	}	
	private static ArrayList<Coordenadas> generarGraphAndPeopleAuxFuego(List<Coordenadas> peopleAux,List<Vertex<Nodo>> people, String[] dimensiones,
			ArrayList<Informacion> edificio) {
		
		int x= Integer.parseInt(dimensiones[0]);
		int y = Integer.parseInt(dimensiones[1]);
		List<Vertex<Nodo>> l = new ArrayList<Vertex<Nodo>>();
		ArrayList<Vertex<Nodo>> auxiliar = new ArrayList<>();
		ArrayList<Coordenadas> auxiliarNum = new ArrayList<>();
		Nodo nodoP;
		Coordenadas coord;
		for( int i = 0 ; i < x; i++ ){
			for(int j = 0; j <y; j++) {
				nodoP = new Nodo(i,j,e.getPlanta());
				Vertex<Nodo> vertex =   e.getPlanta().insertVertex(nodoP);
				l.add(vertex);
				coord = new Coordenadas(i,j);
				if(edificio.get(i+j*x).getPos()==0)
					e.getPlanta().getObstacles().add(coord);
				if(edificio.get(i+j*x).getPos()==2)
					e.getPlanta().getDoors().add(coord);
				if(peopleAux.contains(new Coordenadas(vertex.getElement().getX(),vertex.getElement().getY()))) 
					auxiliar.add(vertex);	
		
			}
		}
		
		for(Vertex<Nodo> aux:e.getPlanta().vertices()) {
			aux.getElement().setObstacle( e.getPlanta().getObstacles().contains(new Coordenadas(aux.getElement().getX(),aux.getElement().getY())));
		}
		
		for(Coordenadas node : peopleAux) {
			if(node.equals(new Coordenadas(-1,-1))) 
				auxiliarNum.add(new Coordenadas(-1,-1));
			else 
				for(int k = 0; k< auxiliar.size();k++) 
					if(auxiliar.get(k).getElement().getX()==node.getCoord_x() && auxiliar.get(k).getElement().getY()==node.getCoord_Y()) {
						people.add(auxiliar.get(k));
						auxiliarNum.add(new Coordenadas(auxiliar.get(k).getElement().getX(),auxiliar.get(k).getElement().getY()));
					} 	
		}
		
		for( int x0 = 0 ; x0 < x; x0++ ){
			for(int y0 = 0; y0 <y; y0++) {
				if(x0+1 < x || y0+1 < y) {
					if((y0+1)==y) {
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0), 1); 
					}
					else if((x0+1)==x) {
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get(x0*y+y0+1), 1);
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0-1)*y+y0+1), 1);					
					} else if(x0==0){
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get(x0*y+y0+1), 1);
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0), 1); 
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0+1), 1);
					} else {
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get(x0*y+y0+1), 1);
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0), 1); 
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0+1), 1);
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0-1)*y+y0+1), 1);
					}
					Nodo node = e.getPlanta().getNodo(x0,y0);
			        if (!(y0==0))
			                node.setNorth(e.getPlanta().getNodo(x0,y0-1));
			        if (!(y0==0) && !(x0==x-1))
			                node.setNorthEast(e.getPlanta().getNodo(x0+1,y0-1));
			        if (!(x0==x-1))
			                node.setEast(e.getPlanta().getNodo(x0+1,y0));
			        if (!(x0==x-1) && !(y0==y-1))
			                node.setSouthEast(e.getPlanta().getNodo(x0+1,y0+1));
			        if (!(y0==y-1))
			                node.setSouth(e.getPlanta().getNodo(x0,y0+1));
			        if (!(x0==0) && !(y0==y-1))
			                node.setSouthWest(e.getPlanta().getNodo(x0-1,y0+1));
			        if (!(x0==0))
			                node.setWest(e.getPlanta().getNodo(x0-1,y0));
			        if (!(x0==0) && !(y0==0))
			                node.setNorthWest(e.getPlanta().getNodo(x0-1,y0-1));
				}
			}
		}
		return auxiliarNum;
	}	
	
	
	private static void generarEstadoParcial(List<Vertex<Nodo>> people) {
		List<ArrayList<Coordenadas>> recorridos = new ArrayList<>();
		for(int i = 0;i< people.size();i++) 
			recorridos.add(e.getPlanta().getRecorridoMasCorto(people.get(i)));
	
		e.getPlanta().actualizarEstado(recorridos);
	}
	
	private static void inicializarDims(ArrayList<Integer> dims) {
		for(int comp = 0;comp<e.getPlanta().getDoors().size();comp++)
			dims.add(-1);
	}

	public static ArrayList<Coordenadas> obtenerColumna(List<ArrayList<Coordenadas>> matriz, int col) {
    	ArrayList<Coordenadas> values = new ArrayList<Coordenadas>();
    	for(int i=0; i<matriz.size(); i++)
    		if(matriz.get(i).size()>col)
    			values.add(matriz.get(i).get(col));
    		else 
    			values.add(new Coordenadas(-1,-1));
		return values;
    }
	

	private static void generarEstadoParcialAux(List<Vertex<Nodo>> people, ArrayList<Coordenadas> asignaciones,FicheroEdificio lec) {
		ArrayList<ArrayList<Coordenadas>> recorridosN = new ArrayList<ArrayList<Coordenadas>>();
		for(int i = 0;i< people.size();i++) {
			if (!asignaciones.get(people.get(i).getElement().getX()+people.get(i).getElement().getY()*Integer.parseInt(lec.getDimensiones()[0]))
				.equals(new Coordenadas(-1,-1))) 
				if (asignaciones.get(people.get(i).getElement().getX()+people.get(i).getElement().getY()*Integer.parseInt(lec.getDimensiones()[0]))
						.equals(new Coordenadas(0,0))) 
					recorridosN.add(e.getPlanta().getRecorridoMasCorto(people.get(i)));
				else 
					recorridosN.add(e.getPlanta().getRecorrido(people.get(i),
							asignaciones.get(people.get(i).getElement().getX()+people.get(i).getElement().getY()*Integer.parseInt(lec.getDimensiones()[0]))));
		}
		ArrayList<ArrayList<Coordenadas>> recAux = (ArrayList<ArrayList<Coordenadas>>) eliminarRepetidos(recorridosN).clone();

		e.getPlanta().actualizarEstadoAux(recAux);
		recorridosN = new ArrayList<>();
		recAux = new ArrayList<>();
	}

	private static ArrayList<ArrayList<Integer>> eliminarRepetidosInteger(ArrayList<ArrayList<Integer>> recorridosN) {
		ArrayList<Integer> recorridosAux = new ArrayList<>();
		ArrayList<ArrayList<Integer>> recorridosNAux = new ArrayList<>();
		for(int i = 0; i<recorridosN.size(); i++) {
			recorridosAux = new ArrayList<>();;
			for(int j = 0; j<recorridosN.get(i).size();j++){
				if(!recorridosAux.contains(recorridosN.get(i).get(j))) 
					recorridosAux.add(recorridosN.get(i).get(j));
			}

			recorridosNAux.add(recorridosAux);
		}

		return recorridosNAux;
	}

	public static ArrayList<ArrayList<Coordenadas>> eliminarRepetidos(ArrayList<ArrayList<Coordenadas>> recorridosN) {
		
		ArrayList<Coordenadas> recorridosAux = new ArrayList<>();
		ArrayList<ArrayList<Coordenadas>> recorridosNAux = new ArrayList<>();
		for(int i = 0; i<recorridosN.size(); i++) {
			recorridosAux = new ArrayList<>();;
			for(int j = 0; j<recorridosN.get(i).size();j++){
				if(!recorridosAux.contains(recorridosN.get(i).get(j))) 
					recorridosAux.add(recorridosN.get(i).get(j));
			}

			recorridosNAux.add(recorridosAux);
		}

		return recorridosNAux;
	}
		

	private static Coordenadas trasladar(ArrayList<Integer> dims, ArrayList<Coordenadas> asignacion,
			List<Vertex<Nodo>> people, FicheroEdificio lec) {
		
		Coordenadas masPoblado = new Coordenadas(-1,-1);
		int tamaño = 0;
		for(int recDim = 0; recDim<dims.size();recDim++) {
			if (dims.get(recDim)>tamaño) {
				tamaño = dims.get(recDim);
				masPoblado = e.getPlanta().getDoors().get(recDim);
			}
		}
		
		Coordenadas coord0 = new Coordenadas(0,0);
		int aTrasladar = 0;
		Coordenadas destino = new Coordenadas(0,0);;
		int altMasCorto = Integer.MAX_VALUE;
		int nec;
		int tiempoPuertaMasC=0;
		
		for (ArrayList<Coordenadas> p : e.getPlanta().getEstado()) {
			if(p.get(p.size()-1).equals(masPoblado)) {
				for(Vertex<Nodo> position : people) {
					for(int k = 0;k < e.getPlanta().getDoors().size();k++) {
						if(!e.getPlanta().getDoors().get(k).equals(masPoblado)
								&&(p.get(0).equals(new Coordenadas(position.getElement().getX(),position.getElement().getY()))))
							if(e.getPlanta().getRecorrido(position,e.getPlanta().getDoors().get(k)).size()<altMasCorto) {
								
								nec =position.getElement().getX() +	Integer.parseInt(lec.getDimensiones()[0]) * position.getElement().getY();
								if(asignacion.get(nec).equals(coord0)) {
									
									aTrasladar = nec;
									destino = e.getPlanta().getRecorrido(position,e.getPlanta().getDoors().get(k))
											.get(e.getPlanta().getRecorrido(position,e.getPlanta().getDoors().get(k)).size()-1);
									altMasCorto = e.getPlanta().getRecorrido(position,e.getPlanta().getDoors().get(k)).size();
									tiempoPuertaMasC = e.getPlanta().getRecorridoMasCorto(position).size();
								}
							}
					}
				}
			}
		}
		ArrayList<Coordenadas> col = new ArrayList<>();
		int cont = 0;
		int newTamMasPoblado = 0;
		for(Coordenadas node:e.getPlanta().getDoors()) {
			if(node.equals(destino))
				break;
			cont++;
		}
		int contAux = 0;
		for(Coordenadas node:e.getPlanta().getDoors()) {
			if(node.equals(masPoblado))
				break;
			contAux++;
		}
		if(!destino.equals(masPoblado)) {	
			if(dims.get(cont)<altMasCorto)
				dims.set(cont, altMasCorto);
			else
				dims.set(cont, dims.get(cont)+1);
			
			
			if(dims.get(contAux)>tiempoPuertaMasC)
				dims.set(contAux, dims.get(contAux)-1);
			else {
				for(int i = dims.get(contAux)-2; i>0;i--) {
					col = obtenerColumna(e.getPlanta().getEstado(),i);
					if(col.contains(masPoblado)) {
						newTamMasPoblado = i;
						break;
					}
				}
				dims.set(contAux, newTamMasPoblado+1);
			}
		}
		if(!destino.equals(masPoblado))	
		asignacion.set(aTrasladar, destino);
		return destino;
	}
	
	private static boolean esMasPoblado(Coordenadas coordenadas, Coordenadas masP, ArrayList<Integer> dims) {
		
		int idx0 = e.getPlanta().getDoors().indexOf(masP);
		int idx1 = e.getPlanta().getDoors().indexOf(coordenadas);
		int poblacion0 = dims.get(idx0);
		int poblacion1 = dims.get(idx1);
		return poblacion1>=poblacion0;
	}

	private static Coordenadas trasladar(ArrayList<Integer> dims, ArrayList<Coordenadas> asignacion,
			List<Vertex<Nodo>> people, FicheroEdificio lec,Coordenadas masP) {
				
		Coordenadas coord0 = new Coordenadas(0,0);
		int aTrasladar = -1;
		Coordenadas destino = new Coordenadas(0,0);;
		int altMasCorto = Integer.MAX_VALUE;
		int nec;
		int tiempoPuertaMasC=0;
		
		for (ArrayList<Coordenadas> p : e.getPlanta().getEstado()) {
			if(p.get(p.size()-1).equals(masP)) {
				for(Vertex<Nodo> position : people) {
					for(int k = 0;k < e.getPlanta().getDoors().size();k++) {
						if(!e.getPlanta().getDoors().get(k).equals(masP)
								&&(p.get(0).equals(new Coordenadas(position.getElement().getX(),position.getElement().getY())))&&!esMasPoblado(e.getPlanta().getDoors().get(k),masP,dims))
							if(e.getPlanta().getRecorrido(position,e.getPlanta().getDoors().get(k)).size()<altMasCorto) {
								
								nec =position.getElement().getX() +	Integer.parseInt(lec.getDimensiones()[0]) * position.getElement().getY();
								if(asignacion.get(nec).equals(coord0)) {
									
									aTrasladar = nec;
									destino = e.getPlanta().getRecorrido(position,e.getPlanta().getDoors().get(k))
											.get(e.getPlanta().getRecorrido(position,e.getPlanta().getDoors().get(k)).size()-1);
									altMasCorto = e.getPlanta().getRecorrido(position,e.getPlanta().getDoors().get(k)).size();
									tiempoPuertaMasC = e.getPlanta().getRecorridoMasCorto(position).size();
								}
							}
					}
				}
			}
		}
		ArrayList<Coordenadas> col = new ArrayList<>();
		
		if(aTrasladar == -1) return null;
		
		int cont = 0;
		int newTamMasPoblado = 0;
		for(Coordenadas node:e.getPlanta().getDoors()) {
			if(node.equals(destino))
				break;
			cont++;
		}
		int contAux = 0;
		for(Coordenadas node:e.getPlanta().getDoors()) {
			if(node.equals(masP))
				break;
			contAux++;
		}
		if(!destino.equals(masP)) {	
			if(dims.get(cont)<altMasCorto)
				dims.set(cont, altMasCorto);
			else
				dims.set(cont, dims.get(cont)+1);
			
			
			if(dims.get(contAux)>tiempoPuertaMasC)
				dims.set(contAux, dims.get(contAux)-1);
			else {
				for(int i = dims.get(contAux)-2; i>0;i--) {
					col = obtenerColumna(e.getPlanta().getEstado(),i);
					if(col.contains(masP)) {
						newTamMasPoblado = i;
						break;
					}
				}
				dims.set(contAux, newTamMasPoblado+1);
			}
		}
		if(!destino.equals(masP))			
		asignacion.set(aTrasladar, destino);
		return destino;
	}

	
	public static List<ArrayList<Coordenadas>> menu1(String[] args, FicheroEdificio lec2,int opcion) {
		
		
		lec = lec2;
		for(int i = 0;i<lec.getBaldosas().size();i++)
			if(lec.getBaldosas().get(i).getPos()==2)
				fin.add(lec.getBaldosas().get(i).getCoord());
			
		 a = algoritmoRutaMasCorta(args, opcion, lec);
				

		return a;
		
	}
	public static List<ArrayList<Coordenadas>> menu2(String[] args, FicheroEdificio lec2,int opcion) {
		
		lec = lec2;
		for(int i = 0;i<lec.getBaldosas().size();i++)
			if(lec.getBaldosas().get(i).getPos()==2)
				fin.add(lec.getBaldosas().get(i).getCoord());
		 b = algoritmoEficiente(args, opcion, lec);
		 return b;
		 
	}
		

	private static List<ArrayList<Coordenadas>> algoritmoRutaMasCorta(String [] args, int opcion, FicheroEdificio lec) {
		
		Set<Coordenadas> numbers = new HashSet<>();
		
		if(args[1].contains("."))
			pos = new FicheroPosiciones(args[1],lec.getDimensiones());	
		else 
			pos = new FicheroPosiciones(args[1],lec);
		
		for (int num = 0; num<pos.getNum(); num++)
			numbers.add(pos.getPosiciones().get(num));

		int mayor = 0;
	
		List<Vertex<Nodo>> people = new ArrayList<>();
		people = generarGraphAndPeople(people, lec.getDimensiones(),numbers, lec.getBaldosas());
		e.getPlanta().setDims(lec);
		e.getPlanta().setAlgSeleccionado(opcion);			
		
		for(Vertex<Nodo>aux : people) {
			aux.exec();
		}
		
		generarEstadoParcial(people);
		
		ArrayList<Integer> dims = new ArrayList<>();
		inicializarDims(dims);
		calcularDim(dims);
		mayor = mayorDim(dims);		
		completarEstado(mayor+1);
		return e.getPlanta().getEstado();

	}

	private static List<ArrayList<Coordenadas>> algoritmoEficiente(String args[], int opcion, FicheroEdificio lec) {
		
		Set<Coordenadas> numbers = new HashSet<>();

		if(args[1].contains("."))
			pos = new FicheroPosiciones(args[1],lec.getDimensiones());	
		else
			pos = new FicheroPosiciones(args[1],lec);
		
		for (int num = 0; num<pos.getNum(); num++)
			numbers.add(pos.getPosiciones().get(num));
		int mayor = 0, menor = 0, mayor1 = 0, menor1 = 0;

		List<Vertex<Nodo>> people = new ArrayList<>();
		List<Coordenadas> posiciones = new ArrayList<>();
		people = generarGraphAndPeople(people, lec.getDimensiones(),numbers, lec.getBaldosas());
		e.getPlanta().setDims(lec);
		e.getPlanta().setAlgSeleccionado(opcion);
		
		for(Vertex<Nodo> posit : people) 
			posiciones.add(new Coordenadas(posit.getElement().getX(),posit.getElement().getY()));	
		
		for(Vertex<Nodo>aux : people) {
			aux.exec();
		}
		
		generarEstadoParcial(people);
		
		asignacion = new ArrayList<>();
		
		for(int t0 = 0; t0< Integer.parseInt(lec.getDimensiones()[1]);t0++)
			for(int t1 = 0; t1< Integer.parseInt(lec.getDimensiones()[0]);t1++)
				if(posiciones.contains(new Coordenadas(t1,t0)))
					asignacion.add(new Coordenadas(0,0));
				else
					asignacion.add(new Coordenadas(-1,-1));

		ArrayList<Integer> dims = new ArrayList<>();
		inicializarDims(dims);
		calcularDim(dims);
		mayor = mayorDim(dims);
		menor = menorDim(dims, Integer.parseInt(lec.getDimensiones()[1])*Integer.parseInt(lec.getDimensiones()[0]));
		
		Coordenadas masPoblado = new Coordenadas(-1,-1);
		int tamaño = 0;
		for(int recDim = 0; recDim<dims.size();recDim++) {
			if (dims.get(recDim)>tamaño) {
				tamaño = dims.get(recDim);
				masPoblado = e.getPlanta().getDoors().get(recDim);
			}
		}

		
		Coordenadas dest = trasladar(dims,asignacion,people,lec);
		mayor1 = mayorDim(dims);
		menor1 = menorDim(dims, Integer.parseInt(lec.getDimensiones()[1])*Integer.parseInt(lec.getDimensiones()[0]));

		if(menor1<mayor1)
		while (menor<mayor-1) {

			dest =  trasladar(dims,asignacion,people,lec);

			if(dest.equals(masPoblado)) trasladarSigMasPoblado(masPoblado,dims,people,asignacion); 
			
			int diff1 = mayor1-mayor;
			mayor = mayor1;
			menor = menor1;
			mayor1 = mayorDim(dims);
			menor1 = menorDim(dims,Integer.parseInt(lec.getDimensiones()[1])*Integer.parseInt(lec.getDimensiones()[0]));
			int diff2 = mayor1-mayor;
			if(diff1-diff2<0&&mayor1>mayor&&menor1>menor) break;
			
		}
		
		generarEstadoParcialAux(people, asignacion,lec);
		e.getPlanta().asignarEstado(); 
		e.getPlanta().vaciarEstadoAux();
		completarEstadoInt(mayor+1);
		
		return e.getPlanta().getEstado();
		
	}

	private static void trasladarSigMasPoblado(Coordenadas masP, ArrayList<Integer> dims, List<Vertex<Nodo>> people, ArrayList<Coordenadas> asignacion) {
		
		int mayor = mayorDim(dims);
		int menor = menorDim(dims, Integer.parseInt(lec.getDimensiones()[1])*Integer.parseInt(lec.getDimensiones()[0]));
		Coordenadas masPoblado = new Coordenadas(-1,-1);
		int tamaño = 0;
		for(int recDim = 0; recDim<dims.size();recDim++) {
			if (dims.get(recDim)>tamaño&&!e.getPlanta().getDoors().get(recDim).equals(masP)) {
				tamaño = dims.get(recDim);
				masPoblado = e.getPlanta().getDoors().get(recDim);
			}
		}
		
		Coordenadas asignacion0 = trasladar(dims,asignacion,people,lec,masPoblado);
		int mayor1 = mayorDim(dims);
		int menor1 = menorDim(dims, Integer.parseInt(lec.getDimensiones()[1])*Integer.parseInt(lec.getDimensiones()[0]));

		if(menor1<mayor1)
		while (menor<mayor-1) {
		
			asignacion0 = trasladar(dims,asignacion,people,lec,masPoblado);
			
			if( asignacion0 == null || asignacion0.equals(masPoblado)) {
				break;} 
	
			int diff1 = mayor1-mayor;
			mayor = mayor1;
			menor = menor1;
			mayor1 = mayorDim(dims);
			menor1 = menorDim(dims,Integer.parseInt(lec.getDimensiones()[1])*Integer.parseInt(lec.getDimensiones()[0]));
			int diff2 = mayor1-mayor;
			if(diff1-diff2<0&&mayor1>mayor&&menor1>menor) break;
			
		}
	}


	private static List<Vertex<Nodo>> generarGraphAndPeople(List<Vertex<Nodo>> people, String[] dimensiones,
			Set<Coordenadas> numbers, ArrayList<Informacion>edificio) {

		int x = Integer.parseInt(dimensiones[0]);
		int y = Integer.parseInt(dimensiones[1]);
		Nodo nodoP;
		List<Vertex<Nodo>> l = new ArrayList<Vertex<Nodo>>();
		Coordenadas coord;
		for( int i = 0 ; i < x; i++ ){
			for(int j = 0; j <y; j++) {
				nodoP = new Nodo(i,j,e.getPlanta());
				Vertex<Nodo> vertex =   e.getPlanta().insertVertex(nodoP);
				l.add(vertex);
				coord = new Coordenadas(i,j);
				if(numbers.contains(coord)) {
					people.add(vertex);	
				}
				if(edificio.get(i+j*x).getPos()==0)
					e.getPlanta().getObstacles().add(coord);
				if(edificio.get(i+j*x).getPos()==2)
					e.getPlanta().getDoors().add(coord);
			}
		}

		for(Vertex<Nodo> aux:e.getPlanta().vertices()) {
			aux.getElement().setObstacle( e.getPlanta().getObstacles().contains(new Coordenadas(aux.getElement().getX(),aux.getElement().getY())));
		}
		
				
		
		for( int x0 = 0 ; x0 < x; x0++ ){
			for(int y0 = 0; y0 <y; y0++) {
				if(x0+1 < x || y0+1 < y) {
					if((y0+1)==y) {
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0), 1); 
					}
					else if((x0+1)==x) {
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get(x0*y+y0+1), 1);
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0-1)*y+y0+1), 1);					
					} else if(x0==0){
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get(x0*y+y0+1), 1);
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0), 1); 
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0+1), 1);
					} else {
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get(x0*y+y0+1), 1);
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0), 1); 
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0+1)*y+y0+1), 1);
						e.getPlanta().insertEdge(l.get(x0*y+y0), l.get((x0-1)*y+y0+1), 1);
					}
					Nodo node = e.getPlanta().getNodo(x0,y0);
			        if (!(y0==0))
			                node.setNorth(e.getPlanta().getNodo(x0,y0-1));
			        if (!(y0==0) && !(x0==x-1))
			                node.setNorthEast(e.getPlanta().getNodo(x0+1,y0-1));
			        if (!(x0==x-1))
			                node.setEast(e.getPlanta().getNodo(x0+1,y0));
			        if (!(x0==x-1) && !(y0==y-1))
			                node.setSouthEast(e.getPlanta().getNodo(x0+1,y0+1));
			        if (!(y0==y-1))
			                node.setSouth(e.getPlanta().getNodo(x0,y0+1));
			        if (!(x0==0) && !(y0==y-1))
			                node.setSouthWest(e.getPlanta().getNodo(x0-1,y0+1));
			        if (!(x0==0))
			                node.setWest(e.getPlanta().getNodo(x0-1,y0));
			        if (!(x0==0) && !(y0==0))
			                node.setNorthWest(e.getPlanta().getNodo(x0-1,y0-1));
				}
			}
		}
		
		return people;
	}

	private static Fuego algoritmoFuego(String [] args, int opcion, FicheroEdificio lec) {
		
		Set<Coordenadas> auxFuego = new HashSet<>();
		Set<Coordenadas> numbers = new HashSet<>();
		int cont = 0; 
		
		if(args[1].contains("."))
			pos = new FicheroPosiciones(args[1],lec.getDimensiones());	
		else
			pos = new FicheroPosiciones(args[1],lec);

		for (int num = 0; num<pos.getNum(); num++)
			numbers.add(pos.getPosiciones().get(num));
		
		int posAux = 0;

		posAux = pos.getNum();
		int mayor0 = 0;

		ArrayList<Vertex<Nodo>> people = new ArrayList<>();
		ArrayList<Coordenadas> peopleAux = new ArrayList<>();
		List<Coordenadas> posiciones = new ArrayList<>();
		
		Random random = new Random();
		Coordenadas posIniFuego = new Coordenadas(-1,-1);
		while (posIniFuego.equals(new Coordenadas(-1,-1))){
			int rN2 = random.nextInt(Integer.parseInt(lec.getDimensiones()[0]));
			int rN1 = random.nextInt(Integer.parseInt(lec.getDimensiones()[1]));
			if (!numbers.contains(new Coordenadas(rN2,rN1))&&!e.getPlanta().getDoors().contains(new Coordenadas(rN2,rN1))&&!e.getPlanta().getObstacles().contains(new Coordenadas(rN2,rN1)))
			      posIniFuego = new Coordenadas(rN2,rN1);
		}
		
		Vertex<Nodo> vertexFuego = generarGraphAndPeople(people,numbers,lec.getDimensiones(), posIniFuego, lec.getBaldosas());
		e.getPlanta().setAlgSeleccionado(opcion);
		e.getPlanta().setDims(lec);
		for(Vertex<Nodo> ins : people) 
			auxFuego.add(new Coordenadas(ins.getElement().getX(),ins.getElement().getY()));
		
		for(Vertex<Nodo> posit : people) 
			posiciones.add(new Coordenadas(posit.getElement().getX(),posit.getElement().getY()));		
		
		ArrayList<ArrayList<Nodo>> fuego = e.getPlanta().comportamientoFuego(vertexFuego);
		
		ArrayList<ArrayList<Coordenadas>> posFuego = new ArrayList<>();
		ArrayList<Coordenadas> posFuegoAux = new ArrayList<>();
		ArrayList<ArrayList<Coordenadas>> posFuegoCS = new ArrayList<>();

		for(ArrayList<Nodo> momento:fuego) {
			for(Nodo posF:momento) {
				posFuegoAux.add(new Coordenadas(posF.getX(),posF.getY()));
			}
			posFuego.add(posFuegoAux);
			posFuegoCS.add(posFuegoAux);
			posFuegoAux = new ArrayList<>();
		}

		for(Vertex<Nodo> bup : people) {
			peopleAux.add(new Coordenadas(bup.getElement().getX(),bup.getElement().getY()));
		}
		ArrayList<ArrayList<Coordenadas>> estadoAux = new ArrayList<>();
		
		ArrayList<Coordenadas> estA = new ArrayList<>();
		int contF = 0;
		ArrayList<Coordenadas> lista = new ArrayList<>();
		ArrayList<Coordenadas> listaAux = new ArrayList<>();
		e.getPlanta().setPosFuego(posFuego);

		while(auxFuego.size()!=1&&posFuegoCS.get(contF).contains(new Coordenadas(-1,-1))) {
			
			if(contF!=0)if(posFuegoCS.get(contF).contains(e.getPlanta().getDoors().get(0))
					&&posFuegoCS.get(contF).contains(e.getPlanta().getDoors().get(1)))break;
			e.getPlanta().setAlgSeleccionado(opcion);
			e.getPlanta().setDims(lec);
			if(cont%3==0) {
				for(Vertex<Nodo> node : people) {
						node.exec(); 
				}	
			}
			generarEstadoParcial(people);
			if(contF == 0) {
				ArrayList<Vertex<Nodo>> estructura = (ArrayList<Vertex<Nodo>>) people.clone();
				for(Vertex<Nodo>first : estructura)
					estA.add(new Coordenadas(first.getElement().getX(),first.getElement().getY()));
				estadoAux.add(estA);
			}
			ArrayList<Integer> dims = new ArrayList<>();
			inicializarDims(dims);
			calcularDim(dims);
			mayor0 = mayorDim(dims);
			auxFuego = new HashSet<>();
	
			completarEstado(mayor0+1);	
			for(Coordenadas pers : peopleAux) {
				auxFuego.add(pers);
			}
			peopleAux = new ArrayList<>();
		
			for(Coordenadas f1 : obtenerColumna(e.getPlanta().getEstado(),cont%3+1)) {
				peopleAux.add(f1);	
			}

			for(int k0 = 0;k0<listaAux.size();k0++){
				if( listaAux.get(k0).equals(new Coordenadas(-1,-1))) {
					peopleAux.add(k0, new Coordenadas(-1,-1));
				}
			}
			for(int aux = 0; aux<peopleAux.size();aux++)
				if(posFuego.get(0).contains(peopleAux.get(aux)))	
					peopleAux.set(aux, new Coordenadas(-1,-1));
			
			people = new ArrayList<>();
			if(cont%3==0) {
				e.setPlanta(new ELGraph<Nodo,Integer>());
			}
			lista = generarGraphAndPeopleAuxFuego(peopleAux, people, lec.getDimensiones(),lec.getBaldosas());
			listaAux = (ArrayList<Coordenadas>) lista.clone();
			estadoAux.add(lista);
			posFuego.remove(0);
			
			for(int l=0; l<listaAux.size(); l++)
				if(posFuego.get(0).contains(listaAux.get(l))) 
					listaAux.set(l, new Coordenadas(-1,-1));
			
			if(auxFuego.size()!=1&&posFuegoCS.get(contF+1).contains(-1))
				for(int p = 0; p<people.size();p++)
					if(posFuego.get(0).contains(new Coordenadas(people.get(p).getElement().getX(),people.get(p).getElement().getY())))
						people.remove(p);
			e.getPlanta().setPosFuego(posFuego);
			contF++;
			cont++;
		}	
		ArrayList<Integer> hlp = new ArrayList<>();
		for(int hp = 0;hp < posAux;hp++)
			hlp.add(-1);
		if(estadoAux.get(estadoAux.size()-2).equals(hlp))
			estadoAux.remove(estadoAux.size()-1);
		Fuego f = new Fuego();
		posFuegoCS.add(0,posFuegoCS.get(0));
		f.setEstado(estadoAux);
		f.setFuego(posFuegoCS);
		return f;

	}

	public static Fuego menuF(String[] s, FicheroEdificio lec, int opcion) {
		fAUX = algoritmoFuego(s, opcion, lec);
		return fAUX;
	}
	
}
