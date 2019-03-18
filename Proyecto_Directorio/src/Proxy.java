
public class Proxy implements Comparable<Proxy>{

	private String ip;
	private int puerto, conectados;

	public Proxy(String ip, int puerto) {
		super();
		this.ip = ip;
		this.puerto = puerto;
		this.conectados = 0;
	}
	public Proxy(String ip, int puerto,int conectados) {
		super();
		this.ip = ip;
		this.puerto = puerto;
		this.conectados = conectados;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public int getConectados() {
		return conectados;
	}

	public void setConectados(int conectados) {
		this.conectados = conectados;
	}

	public int compareTo(Proxy o) {
		return Integer.compare(conectados, o.getConectados());
	}
	public void sumarConectados(int i) {
		this.conectados+=i;
	}
	public void reducirConectados() {
		this.conectados-=1;
	}

}
