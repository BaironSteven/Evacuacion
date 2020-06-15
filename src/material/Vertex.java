package material;

import algoritmos.Nodo;

import java.util.ArrayList;


/**
 *
 * @author bairon
 */
public interface Vertex <V> extends Position <V>, Runnable,Comparable<Vertex<V>>{

	ArrayList<Vertex<V>> getVecinos();

	void setVecinos(ArrayList<Vertex<V>> vecinos);
	void exec();

	ArrayList<Nodo> getNodo(ArrayList<Vertex<Nodo>> vecinos);
    	
}
