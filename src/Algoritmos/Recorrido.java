package Algoritmos;



import java.util.ArrayList;

public class Recorrido {
	   
        private ArrayList<Nodo> coordenadasCamino = new ArrayList<>();
        
        public Recorrido() {
        }
        //devuelve el tamano del camino
        public int getLength() {
                return coordenadasCamino.size();
        }

        public Nodo getCoordenadasCamino(int index) {
                return coordenadasCamino.get(index);
        }

     
        public int getX(int index) {
                return getCoordenadasCamino(index).getX();
        }

      
        public int getY(int index) {
                return getCoordenadasCamino(index).getY();
        }

       // este a�ade el nodo al final
        public void addCordenadas(Nodo n) {
                coordenadasCamino.add(n);
        }
        
        public Recorrido addInicial(Nodo n) {
        	coordenadasCamino.add(0, n);
            return this;
         }

       public ArrayList<Nodo> getCoordenadasCamino() {
			return coordenadasCamino;
		}
		public void setCoordenadasCamino(ArrayList<Nodo> coordenadasCamino) {
			this.coordenadasCamino = coordenadasCamino;
		}
		// este a�ade el nodo al principio
        public void prependWayPoint(Nodo n) {
                coordenadasCamino.add(0, n);
        }

       //comprueba si una posicion pertenece al camino
        public boolean contains(int x, int y) {
                for(Nodo node : coordenadasCamino) {
                        if (node.getX() == x && node.getY() == y)
                                return true;
                }
                return false;
        }

}

