package Auxiliar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import graph.Informacion;

public class FicheroPosiciones {
	int num;
	ArrayList<Coordenadas> list;
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public ArrayList<Coordenadas> getPosiciones() {
		return posiciones;
	}

	public static void setPosiciones(ArrayList<Coordenadas> posiciones) {
		FicheroPosiciones.posiciones = posiciones;
	}

	private static ArrayList<Coordenadas> posiciones;
	
	public FicheroPosiciones(String path, String [] dimensiones) {
		posiciones = new ArrayList<>();
		
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		String[] aux = null;
		
		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			archivo = new File (path);
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
			String linea;
			int cont = 0;
			while((linea=br.readLine())!=null) {
				if(cont==0)
					num = Integer.parseInt(linea);
				else
					if(cont>1) {
						aux = linea.split(",");
						for(String n:aux) {
							n = n.replaceAll("\\(", "");
							n = n.replaceAll("\\)", "");	
							Coordenadas c = new Coordenadas(Integer.parseInt(n.substring(0, n.indexOf("."))),Integer.parseInt(n.substring(n.indexOf(".")+1,n.length())));
							posiciones.add(c);
						}
					}
				cont++;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta 
			// una excepcion.
			try{                    
				if( null != fr ){   
					fr.close();     
	            }                  
			}catch (Exception e2){ 
				e2.printStackTrace();
	        }
	    }
	}

	public FicheroPosiciones(String string, FicheroEdificio lec) {
		// TODO Auto-generated constructor stub
		posiciones = new ArrayList<>();
		int per = Integer.parseInt(string);
		int cont = 0;
		// Conjunto de números ya usados
		int disp = 0;
		for(Informacion baldosas:lec.getBaldosas()) {
			if(baldosas.getPos()==1)
				disp++;
		}
		
		// Instanciamos la clase Random
		Random random = new Random();
		//System.out.println(lec.getBaldosas().size());
		num = disp*per/100;
		list = new ArrayList<>();
		for(Informacion node:lec.getBaldosas()) {
			if(node.getPos()==0 || node.getPos()==2)
				list.add(node.getCoord());
		}

		while (cont < num){
			// Elegimos un índice al azar
			int rN2 = random.nextInt(Integer.parseInt(lec.getDimensiones()[0]));
			int rN1 = random.nextInt(Integer.parseInt(lec.getDimensiones()[1]));
			if (!list.contains(new Coordenadas(rN2,rN1))&&!posiciones.contains(new Coordenadas(rN2,rN1))) {
			      posiciones.add(new Coordenadas(rN2,rN1));
			      cont ++;
			}
		}
	}
	
}
