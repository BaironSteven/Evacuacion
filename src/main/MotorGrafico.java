package main;

import graph.Blackboard;
import graph.Point2DAnimable;
import javax.swing.Timer;

public class MotorGrafico implements Runnable {
	long interval = 666;
	
	boolean parar = false;
	public void parar() { parar = true; }

	boolean pausado = false;
	void pausar() { pausado = !pausado; }
	
	private Blackboard graf;
	public MotorGrafico(Blackboard graf) {
		this.graf=graf;
	}
	
	private volatile Thread blinker;
	public void start() {
		if(blinker!=null){
			blinker=null;
		}
		blinker = new Thread(this);
		blinker.start();
	}
	
	public void stop() {
		blinker = null;
	}
	
	@Override
	public void run() {
		Thread thisThread = Thread.currentThread();
		while (!parar && blinker == thisThread) {
			try {
				thisThread.sleep(interval);
			}  catch (InterruptedException e) {}

			if (graf != null && !pausado) {
				animate();
				graf.repaint();
			}
		}
	}
	
	private void animate() {
		graf.anima();
	}
}
