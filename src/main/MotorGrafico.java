package main;

import graph.Blackboard;

public class MotorGrafico  {
	long interval = 666;
	
	//boolean parar = false;
	//public void parar() { parar = true; }

	boolean pausado = false;
	//void pausar() { pausado = !pausado; }
	
	private Blackboard graf;
	public MotorGrafico(Blackboard graf) {
		this.graf=graf;
	}
	
	//private volatile Thread blinker;
	/*public void start() {
		if(blinker!=null){
			blinker=null;
		}
		blinker = new Thread(this);
		blinker.start();
	}*/
	
	/*public void stop() {
		pausado = true;
	}*/
	
	//@Override
	public void ejecutar() {
		/*Thread thisThread = Thread.currentThread();
		while (!parar && blinker == thisThread) {
			try {
				thisThread.sleep(interval);
			}  catch (InterruptedException e) {}*/
		//pausado = false;
		if (graf != null && !pausado) {
			animate();
			graf.repaint();
		}
		//}
	}
	
	private void animate() {
		graf.anima();
	}
}
