import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Entidad {

	private static boolean conectado;

	public static void main(String[] args) {
		conectado = false;
		int opcion = 0;
		Scanner scanner = new Scanner(System.in);
		int id = 0;

		/*Busca el puerto del proxy mas libre*/
		String message = get_port();
		if(message==null) {
			System.out.println("Error obteniendo el puerto del directorio.");
			return;
		}
		String msg[]=message.split(",");
		int proxyPort = Integer.parseInt(msg[1]); 
		String ipAdd=msg[0];

		while (!conectado) {
			System.out.print("Ingrese su id: ");
			id = scanner.nextInt();
			conexion(id, "ec", proxyPort, ipAdd);
		}
		System.out.println("Se procede a leer el archivo de proyectos");
		//leerArchivo();
		while (opcion != 2)
		{ 
			System.out.println("\nMENU DE OPCIONES");
			System.out.println("1.Consultar votos");
			System.out.println("2. Desconectar y salir");
			System.out.print("Ingrese la opcion: ");	
			opcion = scanner.nextInt();
			//System.out.println(opcion);

			if (opcion == 1) {
				conexion(id, "ev", proxyPort, ipAdd);
			}
			else if (opcion == 2) {

				conexion(id, "ed", proxyPort, ipAdd);
			}
		}
	}

	private static void conexion(int id, String operacion, int proxyPort, String ipAdd) {
		try {
			Socket entidadsocket = new Socket(ipAdd,proxyPort);
			DataInputStream in = new DataInputStream(entidadsocket.getInputStream());
			DataOutputStream out = new DataOutputStream(entidadsocket.getOutputStream());

			out.writeUTF(operacion + Integer.toString(id));
			String data = in.readUTF();
			//System.out.println(data);

			if (operacion.equals("ec")) {
				System.out.println(data);
				if (data.contains("validado"))
				{
					conectado = true;
				}
			}else if (operacion.equals("ev")) {
				System.out.println(data);
			}
			else if (operacion.equals("ed")) {
				conectado = false;
				System.out.println(data);
				disconnect_proxy(proxyPort);
			}

			entidadsocket.close();
		} catch (UnknownHostException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Readline: " + e.getMessage());
		}
	}

	private static String get_port() {
		try {
			int dirPort = 6666;
			Socket entidadsocket = new Socket("127.0.0.1",dirPort);
			//clientsocket.bind(new InetSocketAddress("192.168.0.1",9806));
			DataInputStream in = new DataInputStream(entidadsocket.getInputStream());
			DataOutputStream out = new DataOutputStream(entidadsocket.getOutputStream());
			out.writeUTF("Obtener");
			String data = in.readUTF();
			entidadsocket.close();
			return data;
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	private static void disconnect_proxy(int proxyPort) {
		try {
			int dirPort = 6666;
			Socket entidadsocket = new Socket();
			entidadsocket.connect(new InetSocketAddress("127.0.0.1",dirPort));
			DataInputStream in = new DataInputStream(entidadsocket.getInputStream());
			DataOutputStream out = new DataOutputStream(entidadsocket.getOutputStream());
			out.writeUTF("Delete,"+proxyPort);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
