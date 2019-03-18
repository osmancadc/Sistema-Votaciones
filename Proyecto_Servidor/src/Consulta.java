
public class Consulta {
	String ID;
	String ID_Consulta;
	String nombre;
	
	public Consulta(String iD, String iD_Consulta, String nombre) {
		super();
		ID = iD;
		ID_Consulta = iD_Consulta;
		this.nombre = nombre;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getID_Consulta() {
		return ID_Consulta;
	}

	public void setID_Consulta(String iD_Consulta) {
		ID_Consulta = iD_Consulta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
