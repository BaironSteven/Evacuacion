package main;

import auxiliar.*;
import graph.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Simulador{

	private static boolean stateParada;
	private JButton acelerarButton;
	private JButton cancelarButton;
	private JTextField txtFieldFicheroEdificio;
	private JButton seleccionarButton;
	private JTextField txtFieldFicheroPersonas;
	private JButton seleccionarButton1;
	private JSlider slider;
	private JButton exportarButton;
	private JButton genEdificioButton;
	private JButton iniciarButton;
	private JButton pararButton;
	private JButton reiniciarButton;
	private JButton genPersButton;
	private JComboBox comboBox1;
	private JLabel lblAlgoritmo;
	private JLabel lblPersIni;
	private JLabel lblPersRest;
	private JLabel lblPersEvac;
	private JLabel lblporcentajeOcupacion;
	private JLabel lblTimeEvac;
	private JLabel lblEvac;
	private JLabel lblTimeCalc;
	private JLabel lblCalc;
	private JLabel lblCalculando;
	private JPanel blackboard;
	private JPanel panel;


	protected String ficheroPersonas;
	protected String ficheroEdificio;
	MotorGrafico motor = null;
	Menu m;
	ArrayList<Coordenadas> col = new ArrayList<>();
	int ds = 0; int ds1 = 0;
	int seg = 0; int seg1 = 0;
	int min = 0; int min1 = 0;
	int hor = 0; int hor1 = 0;
	long interval = 666;
	long velocidad = 1;
	private String porcentajeOcupacion;
	static Size tam;
	static FicheroEdificio lec;
	FicheroPosiciones pos;

	Thread motorGrafic;
	Thread alg;

	Fuego f;
	List<ArrayList<Coordenadas>> elems;
	private static Simulador window;


	private boolean state;
	private boolean motorState;
	private boolean state1;
	private int numStart;
	private int contPers;
	private boolean finSim;
	private Thread tCalc;
	private boolean stateCalc = true;
	private static JFrame frame;
	private Thread etiquetaPers;
	private long sleepInicial = 1000;
	Thread tParada;

	public static void main(String[] args) {
		frame = new JFrame("Simulador");
		frame.setContentPane(new Simulador().panel);

		stateParada = true;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private void resize() {
		if(lec!=null) {
			Size tamAux = new Size(tam.getX(),tam.getY(),tam.getWidth(),tam.getHeight());
			tamAux.calcDims();
			tam = new Size(Integer.parseInt(lec.getDimensiones()[0]), Integer.parseInt(lec.getDimensiones()[1]),
					blackboard.getWidth(), blackboard.getHeight());
			tam.calcDims();
			float x = ((float)tam.getDimX()) / tamAux.getDimX();
			float y = ((float)tam.getDimY()) / tamAux.getDimY();
			for (Object component: ((Blackboard)blackboard).getBd()) {
				if (component instanceof Point2DAnimable) {
					((Point2DAnimable) component).resize(Math.round(((Point2DAnimable) component).getX() * x),
							Math.round(((Point2DAnimable) component).getY() * y), (tam.getDimX()), (tam.getDimY()));
				} else if (component instanceof Square2D) {
					((Square2D) component).resize(Math.round(((Square2D)component).getX()*x),
							Math.round((((Square2D)component).getY())*y),
							(tam.getDimX()), (tam.getDimY()));
				} else if (component instanceof Door2D) {
					((Door2D) component).resize(Math.round(((Door2D)component).getX()*x),
							Math.round((((Door2D)component).getY())*y),
							(tam.getDimX()), (tam.getDimY()));
				}
			}
			blackboard.revalidate();
			blackboard.repaint();
		}
	}

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
		if(pos==null)
			s[1]=porcentajeOcupacion;
		else
			s[1] = "Vacio";

		elems = new ArrayList<>();
		//elemsI = new ArrayList<>();
		//t.start();

		//t = new Timer(100,acciones);
		//t.start();
		f = new Fuego();

		state = true;

		Thread t = new Thread(() -> {
			while (state){
				try {

					Thread.sleep(1000);
					seg++;

					if(seg==60) {
						seg=0;
						++min;
					}
					if(min==60) {
						min=0;
						++hor;
					}
					if(state) {
						String tiempo = (hor <= 9 ? "0" : "") + hor + " : " + (min <= 9 ? "0" : "") + min + " : " + (seg <= 9 ? "0" : "") + seg;
						lblTimeCalc.setText(tiempo);
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		Thread t1 = new Thread(() -> {
			int cont = 0;
			for (;;) {

				if (cont==0) {
					try {
						motorGrafic.sleep(sleepInicial);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				cont++;
				if (state1) {

						++seg1;
						if(seg1==60) {
							seg1=0;
							++min1;
						}
						if(min1==60) {
							min1=0;
							++hor1;
						}
						String tiempo = (hor1 <= 9 ? "0" : "") + hor1 + " : " + (min1 <= 9 ? "0" : "") + min1 + " : " +(seg1 <= 9 ? "0" :"") + seg1;
						lblTimeEvac.setText(tiempo);


				}

			try {

				Thread.sleep(1000/velocidad);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			}
		});

		tParada = new Thread(() -> {
			int cont = 0;
			while(stateParada) {
				if (cont==0) {
					try {
						motorGrafic.sleep(90);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				cont++;
				try {
					if ((((Blackboard) blackboard).isEmpty())) {
						stateParada = false;
						//motorState = false;
						state1 = false;
						pararButton.setEnabled(false);
						reiniciarButton.setEnabled(true);
						finSim = true;
						genEdificioButton.setEnabled(false);
						genPersButton.setEnabled(false);
						acelerarButton.setEnabled(false);
					}
				} catch (ConcurrentModificationException e) {

				}
				try {
					tParada.sleep(interval	/velocidad);
				}  catch (InterruptedException e) {}
			}
		});

		tCalc = new Thread(() -> {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long milCalc=1000;
			while(stateCalc){

				if(stateCalc){
					lblCalculando.setText("                                    CALCULANDO   ");
					try {
						Thread.sleep(milCalc);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if(stateCalc){
					lblCalculando.setText("                                    CALCULANDO.  ");
					try {
						Thread.sleep(milCalc);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if(stateCalc) {
					lblCalculando.setText("                                    CALCULANDO.. ");
					try {
						Thread.sleep(milCalc);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if(stateCalc) {
					lblCalculando.setText("                                    CALCULANDO...");
					try {
						Thread.sleep(milCalc);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		motor = new MotorGrafico((Blackboard)blackboard);
		motorGrafic = new Thread(() -> {
			int cont = 0;
			while (!finSim) {
				if (cont==0) {
					try {
						motorGrafic.sleep(sleepInicial);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				cont++;
				if (motorState) {
					motor.ejecutar();

				}
				try {
					motorGrafic.sleep(interval
							/velocidad);
				}  catch (InterruptedException e) {}
			/* else if (numStart%2==1){
					motor.stop();
					numStart++;
				}*/
				/*try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		});

		etiquetaPers = new Thread(() -> {
			while (!finSim) {

				if (motorState && !finSim) {
					int numPers = (((Blackboard) blackboard).personasRestantes());
					lblPersRest.setText("Personas restantes en el edificio: " + numPers);
					lblPersEvac.setText("Personas evacuadas: " + (pos.getPosiciones().size() - numPers));
				}

				try {
					etiquetaPers.sleep(interval
							/ velocidad);
				} catch (InterruptedException e) {
				}
			}
		});

		t.start();
		alg = new Thread(() -> {
			if (j==3) {
				if(s[1].equals("Vacio"))
					f = m.menuF(s,lec,j, pos);
				else
					f = m.menuF(s,lec,j);
				col = obtenerColumna(f.getEstado(),0);
				((Blackboard)blackboard).borrarPuntos();
				iniciarElementos(f,squares,doors);
			} else if (j==1){
				if(s[1].equals("Vacio"))
					elems = m.menu1(s,lec,j, pos);
				else
					elems = m.menu1(s, lec, j);
				col =obtenerColumna(elems,0);
				((Blackboard)blackboard).borrarPuntos();
				iniciarElementos(elems);
			} else {
				if(s[1].equals("Vacio"))
					elems = m.menu2(s,lec,j, pos);
				else
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
				((Blackboard)blackboard).borrarPuntos();
				iniciarElementos(elems);

			}
			stateCalc = false;
			motorState = true;
			state = false;
			state1 = true;
			motorGrafic.start();
			etiquetaPers.start();
			t1.start();
			tParada.start();
			acelerarButton.setEnabled(true);
			pararButton.setEnabled(true);
			cancelarButton.setEnabled(false);
			lblCalculando.setText("");
		});
		alg.start();

	}

	private void iniciarElementos(Fuego f, ArrayList<Integer> squares, ArrayList<Integer> doors) {
		int cont = 0;
		ArrayList<Coordenadas> col = f.getEstado().get(0);
		for(Coordenadas node:col) {
			((Blackboard)blackboard).addElement((new Point2DAnimable((node.getCoord_x()*tam.getDimX()),
					(node.getCoord_Y()*tam.getDimY()),obtenerColumna(f.getEstado(),cont),
					(tam.getDimX()),(tam.getDimY()), (Blackboard) blackboard)));
			cont++;
		}
		for(Integer node1:squares) {
			((Blackboard)blackboard).addElement((new Square2D(((node1%tam.getX())*tam.getDimX()),
					((node1/tam.getX())*tam.getDimY()),
					(tam.getDimX()), (tam.getDimY()))));
		}
		for(Integer node2:doors) {
			((Blackboard)blackboard).addElement((new Door2D(((node2%tam.getX())*tam.getDimX()),
					((node2/tam.getX())*tam.getDimY()),
					(tam.getDimX()), (tam.getDimY()))));
		}
		((Blackboard)blackboard).addElement(new Fire2DAnimable((f.getFuego().get(0).get(0).getCoord_x()*tam.getDimX()),
				(f.getFuego().get(0).get(0).getCoord_Y()*tam.getDimY()),
				f.getFuego(),(tam.getDimX()),(tam.getDimY())));

	}

	private void iniciarElementos(List<ArrayList<Coordenadas>> elems){
		int cont = 0;
		ArrayList<Coordenadas> col = obtenerColumna(elems,0);
		for(Coordenadas node:col) {
			((Blackboard)blackboard).addElement((new Point2DAnimable((node.getCoord_x()*tam.getDimX()),
					(node.getCoord_Y()*tam.getDimY()), elems.get(cont),
					(tam.getDimX()),(tam.getDimY()), (Blackboard) blackboard)));
			cont++;
		}

	}


	public static ArrayList<Coordenadas> obtenerColumna(List<ArrayList<Coordenadas>> matriz, int col) {
		ArrayList<Coordenadas> values = new ArrayList<>();
		for (ArrayList<Coordenadas> coordenadas : matriz)
			if (coordenadas.size() > col)
				values.add(coordenadas.get(col));
			else
				values.add(new Coordenadas(-1, -1));
		return values;
	}

	public Simulador() {

		//comboBox1 = new JComboBox();
		comboBox1.addItem("Algoritmo 1");
		comboBox1.addItem("Algoritmo 2");
		comboBox1.addItem("Algoritmo 3");
		genPersButton.setEnabled(false);
		genEdificioButton.setEnabled(false);
		cancelarButton.setEnabled(false);
		acelerarButton.setEnabled(false);
		iniciarButton.setEnabled(false);
		pararButton.setEnabled(false);
		reiniciarButton.setEnabled(false);
		slider.setEnabled(false);
		seleccionarButton1.setEnabled(false);
		exportarButton.setEnabled(false);
		//Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		//double escala = ss.width / 1920d;

		//slider = new JSlider();
		slider.setValue(5);
		porcentajeOcupacion = Integer.toString(slider.getValue());

		//int blackboardHeight = blackboard.getX();
		//int blackboardWidth = blackboard.getY();

		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				resize();
			}
		});

		iniciarButton.addActionListener(e -> {
			//iniciar simulador
			seleccionarButton.setEnabled(false);
			reiniciarButton.setEnabled(false);
			genEdificioButton.setEnabled(false);
			genPersButton.setEnabled(false);
			comboBox1.setEnabled(false);
			seleccionarButton1.setEnabled(false);
			slider.setEnabled(false);
			iniciarButton.setEnabled(false);
			//btnParar.setEnabled(true);
			//exportarButton.setEnabled(true);

			if(motor==null) {
				iniciar(txtFieldFicheroEdificio.getText(),txtFieldFicheroPersonas.getText(), comboBox1.getSelectedIndex()+1);
				tCalc.start();
				lblPersIni.setText("Personas al comienzo de la simulación: " + pos.getPosiciones().size());
				lblPersRest.setText("Personas restantes en el edificio: " + pos.getPosiciones().size());
				lblPersEvac.setText("Personas evacuadas: 0");
				cancelarButton.setEnabled(true);
				stateCalc = true;
			} else {
				state1 = true;
				motorState = true;
				pararButton.setEnabled(true);
			}
		});

		reiniciarButton.addActionListener(e -> {
			iniciarButton.setText("Iniciar");
			lblTimeCalc.setText("00 : 00 : 00");
			lblTimeEvac.setText("00 : 00 : 00");
			acelerarButton.setText("Acelerar");
			lblPersIni.setText("Personas al comienzo de la simulación: ");
			lblPersRest.setText("Personas restantes en el edificio: ");
			lblPersEvac.setText("Personas evacuadas: ");
			lblCalculando.setText("");
			seleccionarButton.setEnabled(true);
			seleccionarButton1.setEnabled(true);
			comboBox1.setEnabled(true);
			acelerarButton.setEnabled(false);
			slider.setEnabled(false);
			iniciarButton.setEnabled(false);
			exportarButton.setEnabled(false);
			pararButton.setEnabled(false);
			//blackboard.setVisible(true);
			((Blackboard)blackboard).removeAll();
			//blackboard = new Blackboard();
			blackboard.revalidate();
			blackboard.repaint();
			//frame.getContentPane().remove(panel);
			comboBox1.removeAllItems();
			comboBox1.addItem("Algoritmo 1");
			comboBox1.addItem("Algoritmo 2");
			comboBox1.addItem("Algoritmo 3");
			txtFieldFicheroPersonas.setText("");
			txtFieldFicheroEdificio.setText("");
			motor = null;
			m = null;
			contPers=0;
			col = new ArrayList<>();
			state = false;
			state1 = false;
			motorState = false;
			stateCalc=false;
			finSim = false;
			stateParada = true;
			numStart=0;
			velocidad=1;
			ds = 0; ds1 = 0;
			seg = 0; seg1 = 0;
			min = 0; min1 = 0;
			hor = 0; hor1 = 0;
			slider.setValue(5);
			porcentajeOcupacion = "5";
			//Timer t = new Timer(100, acciones);
			//tEvac = new Timer(100,accionesEvac);
			lec = new FicheroEdificio();
			reiniciarButton.setEnabled(false);
		});

		pararButton.addActionListener(e -> {
			iniciarButton.setText("Reanudar");
			state = false;
			state1 = false;
			//t.stop();
			iniciarButton.setEnabled(true);
			pararButton.setEnabled(false);
			reiniciarButton.setEnabled(true);
			motorState = false;
		});

		seleccionarButton1.addActionListener(e -> {
			ficheroPersonas = seleccionarFichero();
			if (!ficheroPersonas.equals("")){
				slider.setEnabled(false);
			}
			txtFieldFicheroPersonas.setText(ficheroPersonas);});


		seleccionarButton.addActionListener(e -> {

			ficheroEdificio = seleccionarFichero();
			genEdificioButton.setEnabled(true);
			txtFieldFicheroEdificio.setText(ficheroEdificio);

		});

		slider.addChangeListener(ce -> {
			lblporcentajeOcupacion.setText("Porcentaje de ocupaci\u00F3n: " + slider.getValue() + "%");
			porcentajeOcupacion = Integer.toString(slider.getValue());
		});

		exportarButton.addActionListener(e -> {
			//iniciar simulador
			String ruta = guardarFichero();
			StringBuilder cadenaE = new StringBuilder();
			FileWriter fichero = null;
			PrintWriter pw;
			try
			{
				fichero = new FileWriter(ruta);
				pw = new PrintWriter(fichero);
				pw.println(col.size() + "\n");
				for(Coordenadas node0:col) {
					cadenaE.append("(").append(node0.getCoord_x()).append(".").append(node0.getCoord_Y()).append("),");
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
		});

		genEdificioButton.addActionListener(e -> {

			if (!ficheroEdificio.equals("")){
				ArrayList<Integer>squares = new ArrayList<>();
				ArrayList<Integer>doors = new ArrayList<>();

				if(((Blackboard)blackboard).getBd().size()>0) {
					((Blackboard)blackboard).removeAll();
					iniciarButton.setEnabled(false);
				}

				try {
					lec = new FicheroEdificio(ficheroEdificio);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Fichero inválido");
					txtFieldFicheroEdificio.setText("");
					return;
				}

				tam = new Size(Integer.parseInt(lec.getDimensiones()[0]),Integer.parseInt(lec.getDimensiones()[1]),
						blackboard.getWidth(), blackboard.getHeight());

				tam.calcDims();
				for(int i = 0;i<lec.getBaldosas().size();i++) {
					if(lec.getBaldosas().get(i).getPos()==0)
						squares.add(i);
					if(lec.getBaldosas().get(i).getPos()==2)
						doors.add(i);
				}

				//btnIniciar.setEnabled(true);
				for(Integer node1:squares) {
					((Blackboard)blackboard).addElement((new Square2D(((node1%Integer.parseInt(lec.getDimensiones()[0]))*tam.getDimX()),
							((node1/Integer.parseInt(lec.getDimensiones()[0]))*tam.getDimY()),
							(tam.getDimX()),(tam.getDimY()))));
				}
				for(Integer node2:doors) {
					((Blackboard)blackboard).addElement((new Door2D(((node2%Integer.parseInt(lec.getDimensiones()[0]))*tam.getDimX()),
							((node2/Integer.parseInt(lec.getDimensiones()[0]))*tam.getDimY()),
							(tam.getDimX()),(tam.getDimY()))));
				}
				//((Blackboard)blackboard).setBounds(blackboard.getX(), blackboard.getY(), blackboard.getWidth(), blackboard.getHeight());
				((Blackboard)blackboard).setBackground(Color.WHITE);
				//frame.add(panel);

				((Blackboard)blackboard).repaint();
				genPersButton.setEnabled(true);
				seleccionarButton1.setEnabled(true);
				slider.setEnabled(true);
			}

		});

		genPersButton.addActionListener(e -> {

			if(contPers>0)
				((Blackboard)blackboard).borrarPuntos();
			try {
				if (!slider.isEnabled())
					pos = new FicheroPosiciones(ficheroPersonas, lec.getDimensiones());
				else
					pos = new FicheroPosiciones(porcentajeOcupacion, lec);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Fichero inválido");
				txtFieldFicheroPersonas.setText("");
				return;
			}

			ArrayList<Coordenadas> col = pos.getPosiciones();
			for (Coordenadas node : col) {
				((Blackboard)blackboard).addElement((new Point2DAnimable((node.getCoord_x() * tam.getDimX()),
						(node.getCoord_Y() * tam.getDimY()),
						(tam.getDimX()), (tam.getDimY()))));
			}
			iniciarButton.setEnabled(true);
			exportarButton.setEnabled(true);
			lblPersIni.setText("Personas al comienzo de la simulación: "
					+ pos.getPosiciones().size());
			contPers++;
			((Blackboard)blackboard).anima();
			blackboard.repaint();
		});

		cancelarButton.addActionListener(e -> {
			alg.stop();
			iniciarButton.setText("Iniciar");
			lblTimeEvac.setText("00 : 00 : 00");
			acelerarButton.setText("Acelerar");
			lblPersIni.setText("Personas al comienzo de la simulación: ");
			lblPersRest.setText("Personas restantes en el edificio: ");
			lblPersEvac.setText("Personas evacuadas: ");
			reiniciarButton.setEnabled(false);
			seleccionarButton.setEnabled(true);
			pararButton.setEnabled(false);
			cancelarButton.setEnabled(false);
			seleccionarButton1.setEnabled(true);
			acelerarButton.setEnabled(false);
			genPersButton.setEnabled(true);
			genEdificioButton.setEnabled(true);
			comboBox1.setEnabled(true);
			state = false;
			slider.setEnabled(true);
			slider.setEnabled(true);
			iniciarButton.setEnabled(false);
			exportarButton.setEnabled(false);
			comboBox1.removeAllItems();
			comboBox1.addItem("Algoritmo 1");
			comboBox1.addItem("Algoritmo 2");
			comboBox1.addItem("Algoritmo 3");
			//txtFieldFicheroEdificio.setText("");
			txtFieldFicheroPersonas.setText("");
			//panel = new Blackboard();
			motor = null;
			m = null;
			contPers=0;
			//col = new ArrayList<>();
			state1 = false;
			motorState = false;
			stateCalc=false;
			stateParada = true;
			finSim = false;
			numStart=0;
			velocidad=1;
			ds = 0; ds1 = 0;
			seg = 0; seg1 = 0;
			min = 0; min1 = 0;
			hor = 0; hor1 = 0;
			slider.setValue(5);
			porcentajeOcupacion = "5";
			//for(int i = 0;i<1000000;i++){continue;}
			lblTimeCalc.setText("00 : 00 : 00");
			lblCalculando.setText("");
			//blackboard.setVisible(true);
			((Blackboard)blackboard).borrarPuntos();
			blackboard.revalidate();
			blackboard.repaint();
			//((JFrame)frame).getContentPane().remove(blackboard);

			lec = new FicheroEdificio();
		});

		acelerarButton.addActionListener(e -> {
			if(velocidad==8) {
				velocidad = 1;
				acelerarButton.setText("Acelerar");
			} else {
				velocidad*=2;
				acelerarButton.setText("Acelerar x" + velocidad);
			}
		});


	}

	private static String guardarFichero() {
		JFileChooser jF1= new JFileChooser();
		String ruta = "";
		try{
			if(jF1.showSaveDialog(null)== JFileChooser.APPROVE_OPTION){
				ruta = jF1.getSelectedFile().getAbsolutePath();
				//Aqui ya tiens la ruta,,,ahora puedes crear un fichero n esa ruta y escribir lo que kieras...
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return ruta;
	}

	private static String seleccionarFichero() {
		//Se crea el JFileChooser. Se le indica que la ventana se abra en el directorio actual
		JFileChooser fileChooser = new JFileChooser(".");
		//Se crea el filtro. El primer par�metro es el mensaje que se muestra,
		//el segundo es la extensi�n de los ficheros que se van a mostrar
		FileFilter filtro = new FileNameExtensionFilter("Archivos txt (.txt)", "txt");
		//Se le asigna al JFileChooser el filtro
		fileChooser.setFileFilter(filtro);
		//se muestra la ventana
		String ruta = "";
		int valor = fileChooser.showOpenDialog(fileChooser);
		if (valor == JFileChooser.APPROVE_OPTION) {
			ruta = fileChooser.getSelectedFile().getAbsolutePath();
		} else {
			System.out.println("No se ha seleccionado ningun fichero");
		}
		return ruta;
	}


	private void createUIComponents() {
		// TODO: place custom component creation code here

		blackboard = new Blackboard();
	}
}
