package material;

import algoritmos.Nodo;

import java.util.ArrayList;
import java.util.Collections;

public class ListaNodosOrdenados {
        //Lista de nodos.
      private ArrayList<Nodo> list = new ArrayList<Nodo>();

      public Nodo getFirst(){
              return list.get(0);
      }

      public void clear(){
              list.clear();
      }

      public void add(Nodo node) {
              list.add(node);
              Collections.sort(list);
      }

      public void remove(Nodo n) {
    	  list.remove(n);
      }

      public int size() {
              return list.size();
      }
      // devuelve si esta ese nodo en la lista.
      public boolean contains(Nodo n) {
              return list.contains(n);
      }

}
