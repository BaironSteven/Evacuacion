package main;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Canvas;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import Auxiliar.Coordenadas;
import Auxiliar.Fuego;
import Auxiliar.FicheroEdificio;
import Auxiliar.Tamaño;
import graph.Blackboard;
import graph.Door2D;
import graph.ELGraph;
import graph.Edificio;
import graph.Fire2DAnimable;
import graph.Point2DAnimable;
import graph.Square2D;

import java.awt.Choice;
import java.awt.Color;

import javax.swing.JSlider;
import java.awt.Button;
import java.awt.Frame;

public class Simulador extends javax.swing.JFrame{

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	protected String ficheroPersonas;
	protected String ficheroEdificio;
	Blackboard pizarra = new Blackboard();
	MotorGrafico motor = null;
	Menu m;
	ArrayList<Coordenadas> col = new ArrayList<>();
	private static List<Integer> opciones = Arrays.asList(1,2,3,4);
	int ds = 0; int ds1 = 0;
	int seg = 0; int seg1 = 0;
	int min = 0; int min1 = 0;
	int hor = 0; int hor1 = 0;
	boolean estadoCronometro = false;
	private String porcentajeDeEvacuacion;
	int x,y, w, h;
	Tamaño tam;
	FicheroEdificio lec;
	private static Simulador window;
	
	Fuego f;
	List<ArrayList<Coordenadas>> elems;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Simulador();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public Simulador() throws HeadlessException {
		// TODO Auto-generated constructor stub
		initialize();
		
		//tEvac = new Timer(100,accionesEvac);
	}
	
	public void pararTimer() {
		// TODO Auto-generated constructor stub
		t.stop();
		
		//tEvac = new Timer(100,accionesEvac);
	}
	
	private ActionListener acciones = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent ae) {
			ds++;
			if(ds==10) {
				ds=0;
				++seg;
			}
			if(seg==60) {
				seg=0;
				++min;
			}
			if(min==60) {
				min=0;
				++hor;
			}
			actualizarLabel();
		}
	};
	private ActionListener accionesEvac = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent ae) {
			ds1++;
			if(ds1==10) {
				ds1=0;
				++seg1;
			}
			if(seg1==60) {
				seg1=0;
				++min1;
			}
			if(min1==60) {
				min1=0;
				++hor1;
			}
			actualizarLabelEvac();
		}
	};
	private void actualizarLabel() {
		String tiempo = (hor <= 9 ? "0" : "") + hor + " : " + (min <= 9 ? "0" : "") + min + " : " +(seg <= 9 ? "0" :"") + seg + " : " + ds;
		((JLabel)this.frame.getContentPane().getComponent(12)).setText(tiempo);
	}
	private void actualizarLabelEvac() {
		String tiempoAux = (hor1 <= 9 ? "0" : "") + hor1 + " : " + (min1 <= 9 ? "0" : "") + min1 + " : " +(seg1 <= 9 ? "0" :"") + seg1 + " : " + ds1;
		((JLabel)this.frame.getContentPane().getComponent(15)).setText(tiempoAux);
	}
	

	private Timer t = new Timer(100,acciones);
	private synchronized void iniciar(String edificio, String personas, int j) {
		//t= new Timer(100, acciones);
		//t.start();
		ArrayList<Integer>squares = new ArrayList<>();
		ArrayList<Integer>doors = new ArrayList<>();
		String [] s = new String[2];
		m = new Menu();
		s[0] = edificio;
		if(!personas.equals(""))
			s[1] = personas;
		else 
			s[1]=porcentajeDeEvacuacion;
			
		lec = new FicheroEdificio(s[0]);
		for(int i = 0;i<lec.getBaldosas().size();i++) {
			if(lec.getBaldosas().get(i).getPos()==0)
				squares.add(i);	
			if(lec.getBaldosas().get(i).getPos()==2)
				doors.add(i);		
		}
		tam = new Tamaño(Integer.parseInt(lec.getDimensiones()[0]),Integer.parseInt(lec.getDimensiones()[1]),Integer.parseInt(lec.getDimensiones()[0])*20,Integer.parseInt(lec.getDimensiones()[1])*20);
		tam.calcDims();
		elems = new ArrayList<>();
		//elemsI = new ArrayList<>();
		//t.start();

		t = new Timer(100,acciones);
		t.start();
		f = new Fuego();
		if (j==3) {
			
			f = m.menuF(s,lec,j);
			col = obtenerColumna(f.getEstado(),0);
			iniciarElementos(f,squares,doors,lec);
		} else if (j==1){

			elems = m.menu1(s, lec, j);
			col =obtenerColumna(elems,0);
			iniciarElementos(elems,squares,doors,lec);
		} else {
			
			elems = m.menu2(s, lec, j);
			
			//ArrayList<Coordenadas> elemsAux = new ArrayList<>();
			/*for(ArrayList<Integer> node:elemsI) {
				for(Integer node1:node) {
					if(node1 == -1) {
						elemsAux.add(new Coordenadas(-1,-1));
					} else {
						elemsAux.add(new Coordenadas(node1%Integer.parseInt(lec.getDimensiones()[0]),node1/Integer.parseInt(lec.getDimensiones()[0])));
					}
				}
				elems.add(elemsAux);
				elemsAux = new ArrayList<>();
			}*/
			col =obtenerColumna(elems,0);
			iniciarElementos(elems,squares,doors,lec);
		}
		t.stop();
		t = new Timer(100,accionesEvac);
		//t= new Timer(100,acciones);
		//t.stop();
		motor = new MotorGrafico(pizarra);
		//tEvac.start();
		t.start();
		motor.start();
		t.stop();
	}

	private void iniciarElementos(Fuego f, ArrayList<Integer> squares, ArrayList<Integer> doors, FicheroEdificio lec) {
		int cont = 0;
		ArrayList<Coordenadas> col = f.getEstado().get(0);
		for(Coordenadas node:col) {
			pizarra.addElement((new Point2DAnimable(node.getCoord_x()*tam.getDimX(),node.getCoord_Y()*tam.getDimY(),
					obtenerColumna(f.getEstado(),cont),tam.getDimX(),tam.getDimY())));
			cont++;
		}
		for(Integer node1:squares) {
			pizarra.addElement((new Square2D((node1%tam.getX())*tam.getDimX(),(node1/tam.getX())*tam.getDimY(),tam.getDimX(),tam.getDimY())));
		}
		for(Integer node2:doors) {
			pizarra.addElement((new Door2D((node2%tam.getX())*tam.getDimX(),(node2/tam.getX())*tam.getDimY(),tam.getDimX(),tam.getDimY())));
		}
		pizarra.addElement(new Fire2DAnimable(f.getFuego().get(0).get(0).getCoord_x()*tam.getDimX(),f.getFuego().get(0).get(0).getCoord_Y()*tam.getDimY(),
				f.getFuego(),tam.getDimX(),tam.getDimY()));
		
	}

	private void iniciarElementos(List<ArrayList<Coordenadas>> elems, ArrayList<Integer> squares, ArrayList<Integer> doors, FicheroEdificio lec) {
		int cont = 0;
		ArrayList<Coordenadas> col = obtenerColumna(elems,0);
		for(Coordenadas node:col) {
			pizarra.addElement((new Point2DAnimable(node.getCoord_x()*tam.getDimX(),node.getCoord_Y()*tam.getDimY(), elems.get(cont),tam.getDimX(),tam.getDimY())));
			cont++;
		}
		for(Integer node1:squares) {
			pizarra.addElement((new Square2D((node1%Integer.parseInt(lec.getDimensiones()[0]))*tam.getDimX(),(node1/Integer.parseInt(lec.getDimensiones()[0]))*tam.getDimY(),tam.getDimX(),tam.getDimY())));
		}
		for(Integer node2:doors) {
			pizarra.addElement((new Door2D((node2%Integer.parseInt(lec.getDimensiones()[0]))*tam.getDimX(),(node2/Integer.parseInt(lec.getDimensiones()[0]))*tam.getDimY(),tam.getDimX(),tam.getDimY())));
		}
	}
	

	public static ArrayList<Coordenadas> obtenerColumna(List<ArrayList<Coordenadas>> matriz, int col) {
    	ArrayList<Coordenadas> values = new ArrayList<>();
    	for(int i=0; i<matriz.size(); i++)
    		if(matriz.get(i).size()>col)
    			values.add(matriz.get(i).get(col));
    		else 
    			values.add(new Coordenadas(-1,-1));
		return values;
    }
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		//tEvac= new Timer(100, accionesEvac);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setBounds(100, 100, 3879, 2082);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnSeleccionar = new JButton("Seleccionar...");
		btnSeleccionar.setBounds(3579, 201, 233, 47);
		frame.getContentPane().add(btnSeleccionar);
		
		JLabel lblFicheroEdificio = new JLabel("Fichero Edificio:");
		lblFicheroEdificio.setBounds(3108, 109, 281, 39);
		frame.getContentPane().add(lblFicheroEdificio);
		
		textField = new JTextField();
		textField.setBounds(3113, 202, 466, 45);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel label = new JLabel("Fichero Personas:");
		label.setBounds(3108, 316, 281, 39);
		frame.getContentPane().add(label);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(3113, 403, 466, 45);
		frame.getContentPane().add(textField_1);
		
		JButton button = new JButton("Seleccionar...");
		button.setBounds(3579, 402, 233, 47);
		frame.getContentPane().add(button);
		
		JLabel lblAlgoritmo = new JLabel("Algoritmo:");
		lblAlgoritmo.setBounds(3113, 500, 189, 39);
		frame.getContentPane().add(lblAlgoritmo);
		
		Choice choice = new Choice();
		choice.setBounds(3361, 509, 447, 45);
		choice.add("Algoritmo 1");
		choice.add("Algoritmo 2");
		choice.add("Algoritmo 3");
		frame.getContentPane().add(choice);
		
		JSlider slider = new JSlider();
		slider.setBounds(3091, 1666, 447, 26);
		frame.getContentPane().add(slider);
		porcentajeDeEvacuacion = Integer.toString(slider.getValue());
		
		
		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.setBounds(3091, 1735, 718, 47);
		frame.getContentPane().add(btnIniciar);
		
		JButton btnReiniciar = new JButton("Reiniciar");
		btnReiniciar.setBounds(3090, 1842, 718, 47);
		frame.getContentPane().add(btnReiniciar);
		
		
		JButton btnParar = new JButton("Parar");
		btnParar.setBounds(3091, 1789, 718, 47);
		frame.getContentPane().add(btnParar);
		
		JLabel etiquetaTiempo = new JLabel("00 : 00: 00 : 0");
		etiquetaTiempo.setFont(new Font("Tahoma", Font.PLAIN, 48));
		etiquetaTiempo.setBounds(481, 33, 353, 39);
		frame.getContentPane().add(etiquetaTiempo);
		
		
		JLabel lblTiempoClculoDe = new JLabel("Tiempo de evacuaci\u00F3n:");
		lblTiempoClculoDe.setBounds(41, 33, 399, 39);
		frame.getContentPane().add(lblTiempoClculoDe);
		
		JLabel label_2 = new JLabel("Tiempo c\u00E1lculo de ruta");
		label_2.setBounds(1027, 30, 399, 39);
		frame.getContentPane().add(label_2);
		
		JLabel tiempoCalculo = new JLabel("00 : 00: 00 : 0");
		tiempoCalculo.setFont(new Font("Tahoma", Font.PLAIN, 48));
		tiempoCalculo.setBounds(1432, 33, 389, 39);
		frame.getContentPane().add(tiempoCalculo);
		
		JLabel lblPorcentajeDeOcupacin = new JLabel("Porcentaje de ocupaci\u00F3n: 50%");
		lblPorcentajeDeOcupacin.setBounds(3090, 1594, 579, 39);
		frame.getContentPane().add(lblPorcentajeDeOcupacin);
		
		
		JButton btnExportar = new JButton("Exportar");
		btnExportar.setBounds(3571, 1655, 197, 47);
		frame.getContentPane().add(btnExportar);
		btnExportar.setEnabled(false);
		btnParar.setEnabled(false);
		btnReiniciar.setEnabled(false);
		
		btnIniciar.addActionListener(new ActionListener() {
			 
			  public void actionPerformed(ActionEvent e) {
			    //iniciar simulador
				  btnSeleccionar.setEnabled(false);
				  choice.setEnabled(false);
				  button.setEnabled(false);
				  slider.setEnabled(false);
				  btnIniciar.setEnabled(false);
				  btnParar.setEnabled(true);
				  btnExportar.setEnabled(true);
				  if(motor==null) {
					  iniciar(textField.getText(),textField_1.getText(), choice.getSelectedIndex()+1);
					  pizarra.setBounds(0, 98, tam.getX()*20, tam.getY()*20);
					  pizarra.setBackground(Color.WHITE);
					  frame.getContentPane().add(pizarra);
				  } else {
					  motor.pausar();
				  }
			  }	
			});
		
		btnReiniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnIniciar.setText("Iniciar");
				etiquetaTiempo.setText("00 : 00 : 00 : 0");
				btnReiniciar.setEnabled(false);
				btnSeleccionar.setEnabled(true);
				button.setEnabled(true);
				choice.setEnabled(true);
				slider.setEnabled(true);
				btnExportar.setEnabled(false);
				pizarra.setVisible(false);
				pizarra.removeAll();
				pizarra.revalidate();
				pizarra.repaint();
				frame.getContentPane().remove(pizarra);
				choice.removeAll();
				choice.add("Algoritmo 1");
				choice.add("Algoritmo 2");
				choice.add("Algoritmo 3");
				textField.setText("");
				textField_1.setText("");
				pizarra = new Blackboard();
				motor = null;
				m = null;
				col = new ArrayList<>();
				ds = 0; ds1 = 0;
				seg = 0; seg1 = 0;
				min = 0; min1 = 0;
				hor = 0; hor1 = 0;
				slider.setValue(50);
				porcentajeDeEvacuacion = "50";
				t= new Timer(100, acciones);
				//tEvac = new Timer(100,accionesEvac);
				lec = new FicheroEdificio();
			}
		});
		
		btnParar.addActionListener(new ActionListener() {
			 
			  public void actionPerformed(ActionEvent e) {
			    //iniciar simulador	
				  btnIniciar.setText("Reanudar");
				  t.stop();
				  btnIniciar.setEnabled(true);
				  btnParar.setEnabled(false);
				  btnReiniciar.setEnabled(true);
				  motor.pausar();
			  }	
			});
		
		button.addActionListener(new ActionListener() {
			 
			  public void actionPerformed(ActionEvent e) {
				  ficheroPersonas = seleccionarFichero();
				  textField_1.setText(ficheroPersonas);}	
			});
		
		
		btnSeleccionar.addActionListener(new ActionListener() {
			 
			  public void actionPerformed(ActionEvent e) {
				  ficheroEdificio = seleccionarFichero();
				  textField.setText(ficheroEdificio);	
				  }	
			});
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				lblPorcentajeDeOcupacin.setText("Porcentaje de ocupaci\u00F3n: " + slider.getValue() + "%");
				porcentajeDeEvacuacion = Integer.toString(slider.getValue());
			}
		});
		
		btnExportar.addActionListener(new ActionListener() {
			 
			  public void actionPerformed(ActionEvent e) {
			    //iniciar simulador
				  String ruta = guardarFichero();
				  String cadenaE = "";
				  FileWriter fichero = null;
			        PrintWriter pw = null;
			        try
			        {
			            fichero = new FileWriter(ruta);
			            pw = new PrintWriter(fichero);
			            pw.println(col.size() + "\n");
			          for(Coordenadas node0:col) {
			        	 cadenaE = cadenaE + "(" + node0.getCoord_x() + "." + node0.getCoord_Y() + "),";
			          }
			          pw.print(cadenaE.substring(0, cadenaE.length()-1));

			        } catch (IOException e1) {
			            e1.printStackTrace();
			        } finally {
			           try {
			           // Nuevamente aprovechamos el finally para 
			           // asegurarnos que se cierra el fichero.
			           if (null != fichero)
			              fichero.close();
			           } catch (Exception e2) {
			              e2.printStackTrace();
			           }}
			  }	
			});

		
	}
	

	protected void restaurarVentana() {
		dispose();
		new Simulador();
	}


	private static String guardarFichero() {
		JFileChooser jF1= new JFileChooser(); 
		String ruta = ""; 
		try{ 
		if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION){ 
		ruta = jF1.getSelectedFile().getAbsolutePath(); 
		//Aqui ya tiens la ruta,,,ahora puedes crear un fichero n esa ruta y escribir lo k kieras... 
		} 
		}catch (Exception ex){ 
		ex.printStackTrace(); 
		} 
		return ruta;
	} 
	
	private static String seleccionarFichero() {
		Scanner entrada = null;
        //Se crea el JFileChooser. Se le indica que la ventana se abra en el directorio actual
        JFileChooser fileChooser = new JFileChooser(".");      
        //Se crea el filtro. El primer parámetro es el mensaje que se muestra,
        //el segundo es la extensión de los ficheros que se van a mostrar      
        FileFilter filtro = new FileNameExtensionFilter("Archivos java (.java)", "java"); 
        //Se le asigna al JFileChooser el filtro
        fileChooser.setFileFilter(filtro);
        //se muestra la ventana
        String ruta = "";
        int valor = fileChooser.showOpenDialog(fileChooser);
        if (valor == JFileChooser.APPROVE_OPTION) {
            ruta = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            System.out.println("No se ha seleccionado ningún fichero");
        }
		return ruta;
	}
}

