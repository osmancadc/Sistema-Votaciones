import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class Directorio_Connection{
	DataInputStream in;
	DataOutputStream out;
	Socket socket; 
	List<Proxy> aux;
	public Directorio_Connection(Socket asocket, List<Proxy> lista_proxys) {
		try {
			socket = asocket;
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			aux=lista_proxys;
			String role = in.readUTF();
			if(role.compareTo("Obtener")==0)
				out.writeUTF(enviar_puerto(aux));
			else{
				String comp[]=role.split(",");
				if(comp[0].compareTo("Delete")==0){
					reduce_connection(comp[1],lista_proxys);
				}
				else {
					registrar_proxy(aux,role);
				}
					
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void reduce_connection(String comp, List<Proxy> lproxys){
		int port = Integer.parseInt(comp);
		for(int i=0;i<lproxys.size();i++){
			if(lproxys.get(i).getPuerto()==port)
				lproxys.get(i).reducirConectados();
		}
	}
	private void registrar_proxy(List<Proxy> lproxys, String role) {
		String[] tokens=role.split(",");
		Proxy a= new Proxy (tokens[0],Integer.parseInt(tokens[1]));
		lproxys.add(a);
	}
	private String enviar_puerto(List<Proxy> lproxys) {
		Collections.sort(lproxys);
		String r=lproxys.get(0).getIp()+","+lproxys.get(0).getPuerto();
		lproxys.get(0).sumarConectados(1);
		return r;
	}
}
