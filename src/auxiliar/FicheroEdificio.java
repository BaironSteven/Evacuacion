package auxiliar;

import graph.Informacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FicheroEdificio {
	
	private static ArrayList<Informacion> baldosas;
	private static String[] dimensiones = new String[2];
	
	public ArrayList<Informacion> getBaldosas() {
		return baldosas;
	}

	public static void setBaldosas(ArrayList<Informacion> baldosas) {
		FicheroEdificio.baldosas = baldosas;
	}
	
	public FicheroEdificio(String arg) throws IOException {
		baldosas = new ArrayList<>();
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		String[] aux = null;

			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			archivo = new File (arg);
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			Coordenadas coord;
			Informacion inf;
			// Lectura del fichero
			String linea;
			int cont = 0;
			int coordX=0, coordY = 0;
			while((linea=br.readLine())!=null) {
				if(cont==0)
					setDimensiones(linea.split(" "));
				else
					if(cont>1) {
						aux = linea.split("");
						for(String n:aux) {
							coord= new Coordenadas(coordX,coordY);
							inf = new Informacion(Integer.parseInt(n),coord);
							baldosas.add(inf);
							coordX++;
						}
					coordX = 0;
					coordY++;
					}
				cont++;
			}

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

	public FicheroEdificio() {
		// TODO Auto-generated constructor stub
	}

	public String[] getDimensiones() {
		return dimensiones;
	}

	public static void setDimensiones(String[] dimensiones) {
		FicheroEdificio.dimensiones = dimensiones;
	}
}
