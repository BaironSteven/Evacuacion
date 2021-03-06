package auxiliar;

public class Coordenadas {

	private int coord_x;
	private int coord_Y;
	
	public Coordenadas(int coord_x, int coord_Y) {
		super();
		this.coord_x = coord_x;
		this.coord_Y = coord_Y;
	}

	/**
	 * @return the coord_x
	 */
	public int getCoord_x() {
		return coord_x;
	}

	/**
	 * @param coord_x the coord_x to set
	 */
	public void setCoord_x(int coord_x) {
		this.coord_x = coord_x;
	}

	/**
	 * @return the coord_Y
	 */
	public int getCoord_Y() {
		return coord_Y;
	}

	/**
	 * @param coord_Y the coord_Y to set
	 */
	public void setCoord_Y(int coord_Y) {
		this.coord_Y = coord_Y;
	}
	@Override
	public int hashCode() {
	return ("(" + String.valueOf(coord_x) + "," + String.valueOf(coord_Y) + ")").hashCode();
	}

	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
	    Coordenadas coord = (Coordenadas) o;
		return (coord.getCoord_x() == this.coord_x && coord.getCoord_Y() == this.coord_Y);
	}
	
}
