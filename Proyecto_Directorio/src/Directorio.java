import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Directorio {

	public static void main(String[] args) {

		try {
			List<Proxy> lista_proxys = new ArrayList<Proxy>();
			int puerto = 6666;
			ServerSocket directorio = new ServerSocket(puerto);
			new Control_errores(lista_proxys);
			while (true) {
				Socket socket = directorio.accept();
				Directorio_Connection c = new Directorio_Connection(socket,lista_proxys);
				System.out.println("-------------------------------------------------------");
				for(Proxy p:lista_proxys){
					System.out.println(p.getIp()+","+p.getPuerto()+"->"+p.getConectados());
				}
				System.out.println("-------------------------------------------------------");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
