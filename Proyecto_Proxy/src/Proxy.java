import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class Proxy {
	public static int contador_global=0;
	public static int reloj_global=0;
	public static String cache="";
	public static boolean pedido=false;

	public static void main(String[] args) {
		try {
			int serverPort = Integer.parseInt(args[0]);
			ServerSocket proxy = new ServerSocket(serverPort);
			registrar_directorio(serverPort);
			new Reloj_global();
			while (true) {
				Socket clientsocket = proxy.accept();
				Proxy_Connection c = new Proxy_Connection(clientsocket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void registrar_directorio(int serverPort){
		String thisIp;
		try {
			thisIp = InetAddress.getLocalHost().getHostAddress();
			String registration_message= thisIp+","+serverPort;
			int dirPort = 6666;
			Socket clientsocket = new Socket();
			clientsocket.connect(new InetSocketAddress("127.0.0.1",dirPort));
			DataInputStream in = new DataInputStream(clientsocket.getInputStream());
			DataOutputStream out = new DataOutputStream(clientsocket.getOutputStream());
			out.writeUTF(registration_message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
