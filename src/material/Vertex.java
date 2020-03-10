package material;

import java.util.ArrayList;

import Algoritmos.Nodo;
import Auxiliar.Coordenadas;


/**
 *
 * @author jvelez
 */
public interface Vertex <V> extends Position <V>, Runnable,Comparable<Vertex<V>>{

	ArrayList<Vertex<V>> getVecinos();

	void setVecinos(ArrayList<Vertex<V>> vecinos);
	void exec();

	ArrayList<Nodo> getNodo(ArrayList<Vertex<Nodo>> vecinos);
    	
}
