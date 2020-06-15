package main;

import graph.Blackboard;

public class MotorGrafico  {

	boolean pausado = false;

	
	private Blackboard graf;
	public MotorGrafico(Blackboard graf) {
		this.graf=graf;
	}

	public void ejecutar() {

		if (graf != null && !pausado) {
			animate();
			graf.repaint();
		}
	}
	
	private void animate() {
		graf.anima();
	}
}
