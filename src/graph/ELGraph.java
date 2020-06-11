package graph;

import Algoritmos.Algoritmo_A_Estrella;
import Algoritmos.Nodo;
import Algoritmos.Recorrido;
import Auxiliar.Coordenadas;
import Auxiliar.FicheroEdificio;
import material.Edge;
import material.Graph;
import material.Vertex;

import java.util.*;

public class ELGraph <V,E> implements Graph <V,E> {

	private final Set <ELVertex<V>> vertexList = new HashSet<>();
    private final Set <ELEdge<E,V>> edgeList = new HashSet<>();
    private ArrayList<ArrayList<Coordenadas>> estado = new ArrayList<>();
    public void setEstado(ArrayList<ArrayList<Coordenadas>> estado) {
		this.estado = estado;
	}

	private ArrayList<ArrayList<Coordenadas>> estadoAux = new ArrayList<>();
    private ArrayList<ArrayList<Nodo>> estadoFuego = new ArrayList<>();
    private ArrayList<ArrayList<Coordenadas>> posFuego = new ArrayList<>();
    private int algSeleccionado = 0;
    private ArrayList<Coordenadas> doors = new ArrayList<>();
    private ArrayList<Coordenadas> obstacles = new ArrayList<>();
	private int anchura;
	private int altura;
	private ArrayList<ArrayList<Integer>> estadoAuxInt = new ArrayList<>();

	private ArrayList<ArrayList<Integer>> estadoInt = new ArrayList<>();

	@Override
    public Collection<? extends Vertex<V>> vertices() {
        return Collections.unmodifiableCollection(vertexList);
    }

    @Override
    public Collection <? extends Edge<E> > edges() {
        return Collections.unmodifiableCollection(edgeList);
    }

    @Override
    public Collection<? extends Edge<E>> incidentEdges(Vertex<V> v) {
        HashSet <Edge <E> > incidentEdges = new HashSet<>();
        for (ELEdge <E,V> e : edgeList)
        {
            if (e.getStartVertex() == v)
                incidentEdges.add(e);
            if (e.getEndVertex() == v)
                incidentEdges.add(e);            
        }
        return incidentEdges;
    }
    
    @Override
    public Vertex<V> opposite(Vertex<V> v, Edge<E> e) {
        ELEdge<E,V> elv = checkEdge(e);
        
        if (elv.getStartVertex() == v)
            return elv.getEndVertex();
        else if (elv.getEndVertex() == v)
            return elv.getStartVertex();
        else
            throw new RuntimeException("The vertex is not in the edge");
    }

    @Override
    public List<Vertex<V>> endVertices(Edge<E> edge) {
        ELEdge<E,V> elv = checkEdge(edge);
        ArrayList <Vertex<V> > output = new ArrayList<>();
        output.add(elv.getStartVertex());
        output.add(elv.getEndVertex());
        return output;
    }

    @Override
    public Edge <E> areAdjacent(Vertex<V> v1, Vertex<V> v2) {
        for (ELEdge<E,V> edge : edgeList)
        {
            if ((edge.getStartVertex() == v1) && (edge.getEndVertex() == v2))
                return edge;
            else if ((edge.getStartVertex() == v2) && (edge.getEndVertex() == v1))
                return edge;
        }
        return null;
    }

    @Override
    public V replace(Vertex<V> vertex, V vertexValue) {
        ELVertex<V> v = checkVertex(vertex);
        V aux = vertex.getElement();
        v.setElement(vertexValue);
        return aux;
    }

    @Override
    public E replace(Edge<E> edge, E edgeValue) {
        ELEdge<E,V> e = checkEdge(edge);
        E aux = edge.getElement();
        e.setElement(edgeValue);
        return aux;
    }

    @Override
    public Vertex <V> insertVertex(V value) {
        ELVertex<V> v = new ELVertex<>(value,this);
        vertexList.add(v);
        return v;
    }

    @Override
    public Edge <E> insertEdge(Vertex<V> v1, Vertex<V> v2, E edgeValue) {
        if (!vertexList.contains(v1))
            throw new RuntimeException("The vertex v1 doesn't belong to this graph");
        if (!vertexList.contains(v2))
            throw new RuntimeException("The vertex v2 doesn't belong to this graph");

        ELEdge<E,V> e = new ELEdge<>(edgeValue,checkVertex(v1),checkVertex(v2),this);

		edgeList.remove(e);
        edgeList.add(e);
        return e;
    }

    @Override
    public V removeVertex(Vertex<V> vertex) {
        ELVertex<V> v = checkVertex(vertex);
        V aux = vertex.getElement();
        vertexList.remove(v);

        //You need an aux set, because you can't remove from a set while you iterate it
        List <ELEdge<E,V>> removeEdgeList = new ArrayList<>();
        for (ELEdge<E,V> edge : edgeList) {
            if ((edge.getStartVertex() == vertex) || (edge.getEndVertex() == vertex)) {
                removeEdgeList.add(edge);
            }
        }
        
        for (ELEdge<E,V> edge : removeEdgeList) {
            edgeList.remove(edge);
        }
        
        return aux;        
    }

    @Override
    public E removeEdge(Edge<E> edge) {
        ELEdge<E, V> e = checkEdge(edge);
        E aux = edge.getElement();
        edgeList.remove(e);
        return aux;
    }    
    
    private ELEdge<E,V> checkEdge(Edge<E> edge) {
        if (edge instanceof ELEdge){
            ELEdge<E, V> e = (ELEdge<E,V>)edge;
            if (e.getGraph() == this)
                return e;
        }
        throw new RuntimeException("The edge is not in the graph");
    }

    private ELVertex<V> checkVertex(Vertex<V> vertex) {
        if (vertex instanceof ELVertex){
            ELVertex<V> v = (ELVertex<V>)vertex;
            if (v.getGraph() == this)
                return v;
        }
        throw new RuntimeException("The vertex is not in the graph");        
    }

    public ArrayList<Coordenadas> getRecorridoMasCorto(Vertex<V> v) {
    	ELVertex<V> vertex = checkVertex(v);
    	int tam = 0;
    	ArrayList<Coordenadas> small = new ArrayList<>();
    	for(int i = 0; i<vertex.getRecorridoC().size();i++) {
    		if(i==0) {
    			tam = vertex.getRecorridoC().get(i).size();
    			small= (ArrayList<Coordenadas>) vertex.getRecorridoC().get(i).clone();
    		}else
    			if(vertex.getRecorridoC().get(i).size()<tam) {
    				tam = vertex.getRecorridoC().get(i).size();
    				small=(ArrayList<Coordenadas>) vertex.getRecorridoC().get(i).clone();
    			}
    	}
    	return small;
    }
    
    public ArrayList<Coordenadas> getRecorrido(Vertex<V> v, Coordenadas fin) {
    	ELVertex<V> vertex = checkVertex(v);
    	ArrayList<Coordenadas> recSel = new ArrayList<>();
		for(ArrayList<Coordenadas> node : vertex.getRecorridoC()) 
    		if(node.get(node.size()-1).equals(fin))
    			recSel =  node;
    	return recSel;
    }


	public void actualizarEstado(List<ArrayList<Coordenadas>> recorridos) {

		this.estado.addAll(recorridos);
    	ArrayList<Coordenadas> col;
    	Coordenadas coordAux = new Coordenadas(-1,-1);
		ArrayList<Coordenadas> colPrev;
		ArrayList<Coordenadas> duplicadosInterc,prevDuplicadosInterc;
		List<Coordenadas> duplicateList;
		ArrayList<Coordenadas> duplicadosSp,prevDuplicadosSp;
		ArrayList<Coordenadas> dupMenosPrioritarios,prevDupMenosPrioritarios;
		int mayorSize = calcularMayorSize(this.estado);
		ArrayList<Coordenadas> rep;
		ArrayList<Coordenadas> prevDuplic;
		
		for(int y = 1;y<mayorSize;y++) {
			col = (ArrayList<Coordenadas>) obtenerColumna(this.estado,y).clone();
			colPrev = (ArrayList<Coordenadas>) obtenerColumna(this.estado,y-1).clone();
			duplicateList = findDuplicates(col);
			prevDuplic = new ArrayList<>();
			duplicadosInterc = new ArrayList<>();
			duplicadosSp = new ArrayList<>();
			dupMenosPrioritarios = new ArrayList<>();
			prevDuplicadosInterc = new ArrayList<>();
			prevDuplicadosSp = new ArrayList<>();
			prevDupMenosPrioritarios = new ArrayList<>();
			rep = new ArrayList<>();
			
			for(int i = 0;i<col.size();i++) 
				if(duplicateList.contains(col.get(i)))
					prevDuplic.add(colPrev.get(i));			
			
			for(int l = 0;l<col.size();l++)
				if(!col.get(l).equals(coordAux))
					if(colPrev.get(l).equals(col.get(l))){
						rep.add(col.get(l));
					}
						
			for(int j = 0;j<col.size();j++)
				if(!col.get(j).equals(coordAux))
					if(colPrev.contains(col.get(j))&&col.get(colPrev.indexOf(col.get(j))).equals(colPrev.get(j)))
						if(!rep.contains(col.get(j))&&!duplicadosInterc.contains(col.get(colPrev.indexOf(col.get(j))))) {
							duplicadosInterc.add(col.get(colPrev.indexOf(col.get(j))));
							prevDuplicadosInterc.add(colPrev.get(j));
						}
			
			for(int k = 0;k<prevDuplic.size();k++) 
				if(colPrev.indexOf(col.get(k))!=-1)
					if (duplicateList.contains(prevDuplic.get(k)) && !duplicadosInterc.contains(prevDuplic.get(k))&&!rep.contains(col.get(colPrev.indexOf(col.get(k)))))
						if(!duplicadosInterc.contains(col.get(colPrev.indexOf(prevDuplic.get(k))))&&
								!rep.contains(col.get(colPrev.indexOf(prevDuplic.get(k))))&&
								!duplicadosSp.contains(col.get(colPrev.indexOf(prevDuplic.get(k))))) {
							duplicadosSp.add(col.get(colPrev.indexOf(prevDuplic.get(k))));
							prevDuplicadosSp.add(prevDuplic.get(k));
						}
			
			for(int m = 0;m<prevDuplic.size();m++)
				if(colPrev.indexOf(prevDuplic.get(m))!=-1&&colPrev.indexOf(col.get(m))!=-1)
					if (col.contains(prevDuplic.get(m))&&!duplicateList.contains(prevDuplic.get(m)) && !duplicadosInterc.contains(prevDuplic.get(m)))
						if(!duplicadosInterc.contains(col.get(colPrev.indexOf(prevDuplic.get(m))))&&!duplicadosSp.contains(col.get(colPrev.indexOf(prevDuplic.get(m))))
							&&!rep.contains(col.get(colPrev.indexOf(col.get(m))))&&!rep.contains(col.get(colPrev.indexOf(prevDuplic.get(m))))&&!dupMenosPrioritarios.contains(col.get(colPrev.indexOf(prevDuplic.get(m))))) {
							dupMenosPrioritarios.add(col.get(colPrev.indexOf(prevDuplic.get(m))));
							prevDupMenosPrioritarios.add(prevDuplic.get(m));
						}
				
			if(!duplicateList.isEmpty()) {

				for(int x= 0;x<this.estado.size();x++) {
					if(this.estado.get(x).size()>y-1&&rep.contains(col.get(x))) {
						if (!rep.contains(colPrev.get(x))) {
							this.estado.get(x).add(y, this.estado.get(x).get(y - 1));
						}
					} else if(this.estado.get(x).size()>y-1&&duplicadosInterc.contains(col.get(x))) {
						if (!prevDuplicadosInterc.contains(colPrev.get(x))) {
							this.estado.get(x).add(y, this.estado.get(x).get(y - 1));
						}
					} else if(this.estado.get(x).size()>y-1&&duplicadosSp.contains(col.get(x))) {
						if (!prevDuplicadosSp.contains(colPrev.get(x))) {
							this.estado.get(x).add(y, this.estado.get(x).get(y - 1));
						}
					} else if(this.estado.get(x).size()>y-1&&dupMenosPrioritarios.contains(col.get(x))) {
						if (!prevDupMenosPrioritarios.contains(colPrev.get(x))) {
							this.estado.get(x).add(y, colPrev.get(x));
						}
					} else {
						if (col.indexOf(col.get(x)) != x && !col.get(x).equals(coordAux) && duplicateList.contains(col.get(x))) {
							this.estado.get(x).add(y, colPrev.get(x));
						}
					}
				}

				y--;
			}
			mayorSize = calcularMayorSize(this.estado);

		}
		
    }
    
    public List<Coordenadas> findDuplicates(List<Coordenadas> listContainingDuplicates){ 
	    final Set<Coordenadas> setToReturn = new HashSet<>(); 
	    final Set<Coordenadas> set1 = new HashSet<>();
	    List<Coordenadas> duplicates = new ArrayList<>();
	    Coordenadas auxCoord = new Coordenadas(-1,-1);
	    for (Coordenadas yourInt : listContainingDuplicates)
	       if (!set1.add(yourInt))
	    	   setToReturn.add(yourInt);
	      for(Coordenadas aux:setToReturn) {
	    	  if(!aux.equals(auxCoord))
	    		  duplicates.add(aux);
	      }
	    return duplicates;
    }

	public void actualizarEstadoAux(List<ArrayList<Coordenadas>> recorridos) {

		this.estadoAux.addAll(recorridos);
		ArrayList<Coordenadas> col;
		Coordenadas coordAux = new Coordenadas(-1,-1);
		ArrayList<Coordenadas> colPrev;
		ArrayList<Coordenadas> duplicadosInterc,prevDuplicadosInterc;
		List<Coordenadas> duplicateList;
		ArrayList<Coordenadas> duplicadosSp,prevDuplicadosSp;
		ArrayList<Coordenadas> dupMenosPrioritarios,prevDupMenosPrioritarios;
		int mayorSize = calcularMayorSize(this.estadoAux);
		ArrayList<Coordenadas> rep;
		ArrayList<Coordenadas> prevDuplic;

		for(int y = 1;y<mayorSize;y++) {
			col = (ArrayList<Coordenadas>) obtenerColumna(this.estadoAux,y).clone();
			colPrev = (ArrayList<Coordenadas>) obtenerColumna(this.estadoAux,y-1).clone();
			duplicateList = findDuplicates(col);
			prevDuplic = new ArrayList<>();
			duplicadosInterc = new ArrayList<>();
			duplicadosSp = new ArrayList<>();
			dupMenosPrioritarios = new ArrayList<>();
			prevDuplicadosInterc = new ArrayList<>();
			prevDuplicadosSp = new ArrayList<>();
			prevDupMenosPrioritarios = new ArrayList<>();
			rep = new ArrayList<>();

			for(int i = 0;i<col.size();i++)
				if(duplicateList.contains(col.get(i)))
					prevDuplic.add(colPrev.get(i));

			for(int l = 0;l<col.size();l++)
				if(!col.get(l).equals(coordAux))
					if(colPrev.get(l).equals(col.get(l))){
						rep.add(col.get(l));
					}

			for(int j = 0;j<col.size();j++)
				if(!col.get(j).equals(coordAux))
					if(colPrev.contains(col.get(j))&&col.get(colPrev.indexOf(col.get(j))).equals(colPrev.get(j)))
						if(!rep.contains(col.get(j))&&!duplicadosInterc.contains(col.get(colPrev.indexOf(col.get(j))))) {
							duplicadosInterc.add(col.get(colPrev.indexOf(col.get(j))));
							prevDuplicadosInterc.add(colPrev.get(j));
						}

			for(int k = 0;k<prevDuplic.size();k++)
				if(colPrev.indexOf(col.get(k))!=-1)
					if (duplicateList.contains(prevDuplic.get(k)) && !duplicadosInterc.contains(prevDuplic.get(k))&&!rep.contains(col.get(colPrev.indexOf(col.get(k)))))
						if(!duplicadosInterc.contains(col.get(colPrev.indexOf(prevDuplic.get(k))))&&!rep.contains(col.get(colPrev.indexOf(prevDuplic.get(k))))&&!duplicadosSp.contains(col.get(colPrev.indexOf(prevDuplic.get(k))))) {
							duplicadosSp.add(col.get(colPrev.indexOf(prevDuplic.get(k))));
							prevDuplicadosSp.add(prevDuplic.get(k));
						}

			for(int m = 0;m<prevDuplic.size();m++)
				if(colPrev.indexOf(prevDuplic.get(m))!=-1&&colPrev.indexOf(col.get(m))!=-1)
					if (col.contains(prevDuplic.get(m))&&!duplicateList.contains(prevDuplic.get(m)) && !duplicadosInterc.contains(prevDuplic.get(m)))
						if(!duplicadosInterc.contains(col.get(colPrev.indexOf(prevDuplic.get(m))))&&!duplicadosSp.contains(col.get(colPrev.indexOf(prevDuplic.get(m))))
								&&!rep.contains(col.get(colPrev.indexOf(col.get(m))))&&!rep.contains(col.get(colPrev.indexOf(prevDuplic.get(m))))&&!dupMenosPrioritarios.contains(col.get(colPrev.indexOf(prevDuplic.get(m))))) {
							dupMenosPrioritarios.add(col.get(colPrev.indexOf(prevDuplic.get(m))));
							prevDupMenosPrioritarios.add(prevDuplic.get(m));
						}

			if(!duplicateList.isEmpty()) {
				for(int x= 0;x<this.estadoAux.size();x++) {
					if(this.estadoAux.get(x).size()>y-1&&rep.contains(col.get(x))) {
						if (!rep.contains(colPrev.get(x))) {
							this.estadoAux.get(x).add(y, this.estadoAux.get(x).get(y - 1));
						}
					} else if(this.estadoAux.get(x).size()>y-1&&duplicadosInterc.contains(col.get(x))) {
						if (!prevDuplicadosInterc.contains(colPrev.get(x))) {
							this.estadoAux.get(x).add(y, this.estadoAux.get(x).get(y - 1));
						}
					} else if(this.estadoAux.get(x).size()>y-1&&duplicadosSp.contains(col.get(x))) {
						if (!prevDuplicadosSp.contains(colPrev.get(x))) {
							this.estadoAux.get(x).add(y, this.estadoAux.get(x).get(y - 1));
						}
					} else if(this.estadoAux.get(x).size()>y-1&&dupMenosPrioritarios.contains(col.get(x))) {
						if (!prevDupMenosPrioritarios.contains(colPrev.get(x))) {
							this.estadoAux.get(x).add(y, colPrev.get(x));
						}
					} else {
						if (col.indexOf(col.get(x)) != x && !col.get(x).equals(coordAux) && duplicateList.contains(col.get(x))) {
							this.estadoAux.get(x).add(y, colPrev.get(x));
						}
					}
				}
				y--;
			}
			mayorSize = calcularMayorSize(this.estadoAux);

		}

	}

	public void eliminarRepetidos(int col) {
    	Coordenadas aux;
    	Coordenadas coordAux = new Coordenadas(-1,-1);
    	ArrayList<Coordenadas> columna = this.obtenerColumna(this.estado, col);
    	List<Coordenadas> duplicateList;
    	duplicateList = this.findDuplicates(columna);
    	
    	while(!duplicateList.isEmpty()) {

			for(int n = 0;n <columna.size();n++)
				if(duplicateList.contains(columna.get(n))&&!columna.get(n).equals(coordAux)&&!this.estado.get(n).get(col-1).equals(this.estado.get(n).get(col))) {
					aux = new Coordenadas(this.estado.get(n).get(col-1).getCoord_x(),this.estado.get(n).get(col-1).getCoord_Y());
					this.estado.get(n).add(col,aux);
				}
	    	columna = this.obtenerColumna(this.estado, col);
	    	duplicateList = this.findDuplicates(columna);
	    	if(duplicateList.size()==1&&duplicateList.contains(new Coordenadas(-1,-1))) {
	    		duplicateList.clear();
	    	}
    	}
    }

	public List<ArrayList<Coordenadas>> getEstado() {
		return estado;
	}
    
    public ArrayList<Coordenadas> obtenerColumna(List<ArrayList<Coordenadas>> estado2, int col) {
    	ArrayList<Coordenadas> values = new ArrayList<>();
		for (ArrayList<Coordenadas> coordenadas : estado2)
			if (coordenadas.size() > col)
				values.add(coordenadas.get(col));
			else {
				values.add(new Coordenadas(-1, -1));
			}
		return values;
    }
    
    private int calcularMayorSize(List<ArrayList<Coordenadas>> estado2) {
    	int dim = 0;
		for (ArrayList<Coordenadas> coordenadas : estado2)
			if (coordenadas.size() > dim)
				dim = coordenadas.size();
    	return dim;
    }
    
    public ArrayList<ArrayList<Nodo>> comportamientoFuego(Vertex<Nodo> vertexInicial){
		Set<Nodo> estado = new HashSet<>();
		Collection<? extends Vertex<Nodo>> aux = (Collection<? extends Vertex<Nodo>>) this.vertices();
		Set<Nodo> estAux1 = new HashSet<>();
		ArrayList<Nodo> estAux2 = new ArrayList<>();
		ArrayList<ArrayList<Nodo>> comportamiento = new ArrayList<ArrayList<Nodo>>();
		estado.add(vertexInicial.getElement());
		estAux1.add(vertexInicial.getElement());
		Nodo nodoP = new Nodo(-1,-1,(ELGraph<Nodo, Integer>) this);
		int cont = 0;
		int size = estado.size();
		while(size!=aux.size()-obstacles.size()) {
			if(cont % 3 == 0 && cont!=0) { 
				for(Nodo node : estado) {
					estAux1.addAll(node.getNeighborList());	
				}
				estAux1.removeIf((Nodo n) -> obstacles.contains(new Coordenadas(n.getX(),n.getY())));

				estado.addAll(estAux1);
				size = estado.size();
				if(estAux1.size()<aux.size()) {
					for(Nodo nodeAux:estAux1)
						if(!obstacles.contains(new Coordenadas(nodeAux.getX(),nodeAux.getY())))
							estAux2.add(nodeAux);
					for(int j=0;j<aux.size()-estAux1.size();j++) {
						estAux2.add(nodoP);
					}
				} else {
					for(Nodo nodeAux:estAux1)
						if(!obstacles.contains(new Coordenadas(nodeAux.getX(),nodeAux.getY())))
							estAux2.add(nodeAux);
				}
			}
			else {
				if(estAux1.size()<aux.size()) {
					for(Nodo nodeAux:estAux1)
						if(!obstacles.contains(new Coordenadas(nodeAux.getX(),nodeAux.getY())))
							estAux2.add(nodeAux);
					for(int j=0;j<aux.size()-estAux1.size();j++) {
						estAux2.add(nodoP);
					}
				} else {
					for(Nodo nodeAux:estAux1)
						if(!obstacles.contains(new Coordenadas(nodeAux.getX(),nodeAux.getY())))
							estAux2.add(nodeAux);
				}
			}
			this.estadoFuego.add(estAux2);
			comportamiento.add(estAux2);
			estAux2 = new ArrayList<>();
			cont ++;
		}
		return comportamiento;
	}

	public void vaciarEstadoAux() {
		this.estadoAux = new ArrayList<>();
	}

	public void asignarEstado() {
		this.setEstado((ArrayList<ArrayList<Coordenadas>>) this.estadoAux.clone());
	}

	public ArrayList<ArrayList<Coordenadas>> getPosFuego() {
		return posFuego;
	}

	public void setPosFuego(ArrayList<ArrayList<Coordenadas>> posFuego) {
		this.posFuego = posFuego;
	}

	public int getAlgSeleccionado() {
		return algSeleccionado;
	}

	public void setAlgSeleccionado(int algSeleccionado) {
		this.algSeleccionado = algSeleccionado;
	}

	public ArrayList<Coordenadas> getDoors() {
		return doors;
	}

	public void setDoors(ArrayList<Coordenadas> doors) {
		this.doors = doors;
	}

	public ArrayList<Coordenadas> getObstacles() {
		return obstacles;
	}

	public void setObstacles(ArrayList<Coordenadas> obstacles) {
		this.obstacles = obstacles;
	}
	
	public Nodo getNodo(int x, int y) {
		Nodo n = null;
		for(ELVertex<V> aux:this.vertexList) {
        	n = (Nodo) aux.getElement();
        	if( n.getX()==x && n.getY()==y)
        		return n;
		}
		return n;
	}
	
	 public float getDistanceBetween(Nodo node1, Nodo node2) {
         //if the nodes are on top or next to each other, return 1
         if (node1.getX() ==  node2.getX() || 
        		 node1.getY() ==  node2.getY()){
                 return (this.altura + this.anchura);
         } else { //if they are diagonal to each other return diagonal distance: sqrt(1^2+1^2)
                 return (float) 1.7*(this.altura+this.anchura);
         }
	 }

	public void setDims(FicheroEdificio lec) {
		this.anchura = Integer.parseInt(lec.getDimensiones()[0]);
		this.altura = Integer.parseInt(lec.getDimensiones()[1]);
		
	}

	 
	 public ArrayList<Integer> obtenerColumnaAux(List<ArrayList<Integer>> estado2, int col) {
	    	ArrayList<Integer> values = new ArrayList<>();
		 for (ArrayList<Integer> integers : estado2)
			 if (integers.size() > col)
				 values.add(integers.get(col));
			 else {
				 values.add(-1);
			 }
			return values;
			
	 }
	
	 public List<Integer> findDuplicatesAux(List<Integer> listContainingDuplicates){ 
		final Set<Integer> setToReturn = new HashSet<>();
		final Set<Integer> set1 = new HashSet<>();
		for (Integer yourInt : listContainingDuplicates)
		       if (!set1.add(yourInt))
		    	   setToReturn.add(yourInt);
		List<Integer> duplicates = new ArrayList<>(setToReturn);

		return duplicates;
	}

	public ArrayList<ArrayList<Integer>> getEstadoInt() {
		return estadoInt;
	}

	public void actualizarEstadoParcial(List<ArrayList<Coordenadas>> recorridos) {
		this.estado.addAll(recorridos);
		ArrayList<Coordenadas> col;
		Coordenadas coordAux = new Coordenadas(-1,-1);
		ArrayList<Coordenadas> colPrev;
		ArrayList<Coordenadas> duplicadosInterc,prevDuplicadosInterc;
		List<Coordenadas> duplicateList;
		ArrayList<Coordenadas> duplicadosSp,prevDuplicadosSp;
		ArrayList<Coordenadas> dupMenosPrioritarios,prevDupMenosPrioritarios;
		int mayorSize = calcularMayorSize(this.estado);
		ArrayList<Coordenadas> rep;
		ArrayList<Coordenadas> prevDuplic;

		for(int y = 1;y<mayorSize;y++) {
			col = (ArrayList<Coordenadas>) obtenerColumna(this.estado,y).clone();
			colPrev = (ArrayList<Coordenadas>) obtenerColumna(this.estado,y-1).clone();
			duplicateList = findDuplicates(col);
			prevDuplic = new ArrayList<>();
			duplicadosInterc = new ArrayList<>();
			duplicadosSp = new ArrayList<>();
			dupMenosPrioritarios = new ArrayList<>();
			prevDuplicadosInterc = new ArrayList<>();
			prevDuplicadosSp = new ArrayList<>();
			prevDupMenosPrioritarios = new ArrayList<>();
			rep = new ArrayList<>();

			for(int i = 0;i<col.size();i++)
				if(duplicateList.contains(col.get(i)))
					prevDuplic.add(colPrev.get(i));

			for(int l = 0;l<col.size();l++)
				if(!col.get(l).equals(coordAux))
					if(colPrev.get(l).equals(col.get(l))){
						rep.add(col.get(l));
					}

			for(int j = 0;j<col.size();j++)
				if(!col.get(j).equals(coordAux))
					if(colPrev.contains(col.get(j))&&col.get(colPrev.indexOf(col.get(j))).equals(colPrev.get(j)))
						if(!rep.contains(col.get(j))&&!duplicadosInterc.contains(col.get(colPrev.indexOf(col.get(j))))) {
							duplicadosInterc.add(col.get(colPrev.indexOf(col.get(j))));
							prevDuplicadosInterc.add(colPrev.get(j));
						}

			for(int k = 0;k<prevDuplic.size();k++)
				if(colPrev.indexOf(col.get(k))!=-1)
					if (duplicateList.contains(prevDuplic.get(k)) && !duplicadosInterc.contains(prevDuplic.get(k))&&!rep.contains(col.get(colPrev.indexOf(col.get(k)))))
						if(!duplicadosInterc.contains(col.get(colPrev.indexOf(prevDuplic.get(k))))&&!rep.contains(col.get(colPrev.indexOf(prevDuplic.get(k))))&&!duplicadosSp.contains(col.get(colPrev.indexOf(prevDuplic.get(k))))) {
							duplicadosSp.add(col.get(colPrev.indexOf(prevDuplic.get(k))));
							prevDuplicadosSp.add(prevDuplic.get(k));
						}

			for(int m = 0;m<prevDuplic.size();m++)
				if(colPrev.indexOf(prevDuplic.get(m))!=-1&&colPrev.indexOf(col.get(m))!=-1)
					if (col.contains(prevDuplic.get(m))&&!duplicateList.contains(prevDuplic.get(m)) && !duplicadosInterc.contains(prevDuplic.get(m)))
						if(!duplicadosInterc.contains(col.get(colPrev.indexOf(prevDuplic.get(m))))&&!duplicadosSp.contains(col.get(colPrev.indexOf(prevDuplic.get(m))))
								&&!rep.contains(col.get(colPrev.indexOf(col.get(m))))&&!rep.contains(col.get(colPrev.indexOf(prevDuplic.get(m))))&&!dupMenosPrioritarios.contains(col.get(colPrev.indexOf(prevDuplic.get(m))))) {
							dupMenosPrioritarios.add(col.get(colPrev.indexOf(prevDuplic.get(m))));
							prevDupMenosPrioritarios.add(prevDuplic.get(m));
						}

			if(!duplicateList.isEmpty()) {

				for(int x= 0;x<this.estado.size();x++) {
					if(this.estado.get(x).size()>y-1&&rep.contains(col.get(x))) {
						if (!rep.contains(colPrev.get(x))) {
							this.estado.get(x).add(y, this.estado.get(x).get(y - 1));
						}
					} else if(this.estado.get(x).size()>y-1&&duplicadosInterc.contains(col.get(x))) {
						if (!prevDuplicadosInterc.contains(colPrev.get(x))) {
							this.estado.get(x).add(y, this.estado.get(x).get(y - 1));
						}
					} else if(this.estado.get(x).size()>y-1&&duplicadosSp.contains(col.get(x))) {
						if (!prevDuplicadosSp.contains(colPrev.get(x))) {
							this.estado.get(x).add(y, this.estado.get(x).get(y - 1));
						}
					} else if(this.estado.get(x).size()>y-1&&dupMenosPrioritarios.contains(col.get(x))) {
						if (!prevDupMenosPrioritarios.contains(colPrev.get(x))) {
							this.estado.get(x).add(y, colPrev.get(x));
						}
					} else {
						if (col.indexOf(col.get(x)) != x && !col.get(x).equals(coordAux) && duplicateList.contains(col.get(x))) {
							this.estado.get(x).add(y, colPrev.get(x));
						}
					}
				}

				y--;
			} else {
				break;
			}
			mayorSize = calcularMayorSize(this.estado);

		}
	}
}

class ELVertex <V> extends Thread implements Vertex <V>,Comparable<Vertex <V>> {
    private V node;
    private final Graph graph;
	private ArrayList<Recorrido> recorrido;
	private ArrayList<Vertex<V>> vecinos;
	@Override
    public ArrayList<Vertex<V>> getVecinos() {
		return vecinos;
	}
	@Override
	public void setVecinos(ArrayList<Vertex<V>> vecinos) {
		this.vecinos = vecinos;
	}


	public ArrayList<ArrayList<Coordenadas>> getRecorridoC() {
    	ArrayList<ArrayList<Coordenadas>> rec = new ArrayList<>();
    	ArrayList<Coordenadas> recAux = new ArrayList<>();
    	Coordenadas coordAux = new Coordenadas(-1,-1);
		for(Recorrido node:recorrido) {
			for(int nodeAux = 0;nodeAux<node.getCoordenadasCamino().size();nodeAux++) {
				coordAux = new Coordenadas(node.getCoordenadasCamino(nodeAux).getX(),node.getCoordenadasCamino(nodeAux).getY());
				recAux.add(coordAux);
			}
			rec.add(recAux);
			recAux = new ArrayList<>();
		}
    	return rec;
	}

	@Override
    public V getElement() {
        return node;
    }
    
    public void setElement(V value) {
        node = value;
    }
    
    public ELVertex(V value, Graph graph) {
        node = value;
        this.graph = graph;
    }

    /**
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }

    @Override
	public void exec() {
		
		recorrido = Algoritmo_A_Estrella.algoritmo((Vertex) this,graph);
	
	}

	@Override
	 public int compareTo(Vertex<V> otherNode) {
        float thisTotalDistanceFromGoal = ((Nodo)this.node).getHeuristicDistanceFromGoal() + ((Nodo)this.node).getDistanceFromStart();
        float otherTotalDistanceFromGoal = ((Nodo)otherNode.getElement()).getHeuristicDistanceFromGoal() + ((Nodo)otherNode.getElement()).getDistanceFromStart();

		return Float.compare(thisTotalDistanceFromGoal, otherTotalDistanceFromGoal);
}
	@Override
	public ArrayList<Nodo> getNodo(ArrayList<Vertex<Nodo>> vecinos) {
		ArrayList<Nodo> vec = new ArrayList<>();
		for(Vertex<Nodo> node:vecinos)
			vec.add(node.getElement());
		return vec;
	}

}

class ELEdge <E,V> implements Edge <E> {
    private E edgeValue;
    private final Graph<V,E> graph;
    
    private final Vertex <V> startVertex;
    private final Vertex <V> endVertex;

    @Override
    public int hashCode() {
        int hash = 3;
        
        final int min = Math.min(Objects.hashCode(this.startVertex), Objects.hashCode(this.endVertex));
        final int max = Math.max(Objects.hashCode(this.startVertex), Objects.hashCode(this.endVertex));
        
        hash = 67 * hash + Objects.hashCode(this.getGraph());
        hash = 67 * hash + min;
        hash = 67 * hash + max;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ELEdge<E, V> other = (ELEdge<E, V>) obj;
        if (!Objects.equals(this.graph, other.graph)) {
            return false;
        }
        
        final int min1 = Math.min(Objects.hashCode(this.startVertex), Objects.hashCode(this.endVertex));
        final int max1 = Math.max(Objects.hashCode(this.startVertex), Objects.hashCode(this.endVertex));

        final int min2 = Math.min(Objects.hashCode(other.startVertex), Objects.hashCode(other.endVertex));
        final int max2 = Math.max(Objects.hashCode(other.startVertex), Objects.hashCode(other.endVertex));

        if (!Objects.equals(min1, min2)) {
            return false;
        }
        if (!Objects.equals(max1, max2)) {
            return false;
        }
        return true;
    }
    
    @Override
    public E getElement() {
        return edgeValue;
    }

    public ELEdge(E value,Vertex<V> startVertex, Vertex<V> endVertex, Graph graph) {
        edgeValue = value;
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.graph = graph;
    }

    public void setElement(E value) {
        edgeValue = value;
    }
    
    /**
     * @return the startVertex
     */
    public Vertex <V> getStartVertex() {
        return startVertex;
    }

    /**
     * @return the endVertex
     */
    public Vertex <V> getEndVertex() {
        return endVertex;
    }

    /**
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }
}
