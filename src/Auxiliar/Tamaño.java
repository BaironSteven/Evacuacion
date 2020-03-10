package Auxiliar;

public class Tamaño {
	private int width;
	private int height;
	private int x;
	private int y;
	
	private int dimX;
	private int dimY;
	
	public Tamaño(int x2, int y2, int width2, int height2) {
		this.x = x2;
		this.y = y2;
		this.width = width2;
		this.height = height2;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public void calcDims() {
		setDimX(width/x);
		setDimY(height/y);
	}
	public int getDimX() {
		return dimX;
	}
	public void setDimX(int dimX) {
		this.dimX = dimX;
	}
	public int getDimY() {
		return dimY;
	}
	public void setDimY(int dimY) {
		this.dimY = dimY;
	}
}
