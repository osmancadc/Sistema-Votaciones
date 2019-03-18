

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

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

		/*Se pide el id hasta que sea un id valido*/
		while (!conectado) {
			System.out.print("Ingrese su id: ");
			id = scanner.nextInt();
			/*El usuario se quiere conectar*/
			conexion(id, "uc",proxyPort,ipAdd);
		}

		/*Menu de opciones para el usuario*/
		while (opcion != 3) { 
			System.out.println("\nMENU DE OPCIONES");
			System.out.println("1. Solicitar consultas");
			System.out.println("2. Votar en una consulta");
			System.out.println("3. Desconectar y salir");
			System.out.print("Ingrese la opcion: ");	
			opcion = scanner.nextInt();
			if (opcion == 1) {
				/*El usuario se quiere ver las consultas*/
				conexion (id, "us", proxyPort, ipAdd);
			}
			else if(opcion == 2) {
				conexion (id,"uv",proxyPort, ipAdd);
			}
			else if (opcion == 3) {
				/*El usuario se quiere desconectar*/
				conexion(id, "ud",proxyPort,ipAdd);
				return;
			}
		}
	}

	/*Conexion con el proxy*/
	private static void conexion(int id, String operacion, int proxyPort, String ipAdd) {
		try {
			String data = null;
			Socket clientsocket = new Socket();
			clientsocket.connect(new InetSocketAddress(ipAdd,proxyPort));
			DataInputStream in = new DataInputStream(clientsocket.getInputStream());
			DataOutputStream out = new DataOutputStream(clientsocket.getOutputStream());
			if(operacion != "uv"){
				out.writeUTF(operacion + Integer.toString(id));
				data = in.readUTF();
			}
			if (operacion.equals("uc")) {
				System.out.println(data);
				if (data.contains("validado")) {
					conectado = true;
				}
			}
			else if (operacion.equals("ud")) {
				conectado = false;
				System.out.println(data);
				disconnect_proxy(proxyPort);
			}
			else if (operacion.equals("us")){
				System.out.println(data);
			}
			else if(operacion.equals("uv")){
				String entradaConsulta = "";
				Scanner consultaEscaner = new Scanner (System.in);
				String entradaVoto = "";
				Scanner votoEscaner = new Scanner (System.in);
				System.out.println("Ingrese la consulta en la que desea votar: "); 
		        entradaConsulta = consultaEscaner.nextLine ();
		        System.out.println("Ingrese la consulta su voto ya sea como alto, medio o bajo: "); 
		        entradaVoto = votoEscaner.nextLine ();
				out.writeUTF(operacion + "," + Integer.toString(id)+ "," + entradaConsulta + "," + entradaVoto);
				data = in.readUTF();
				System.out.println(data);
			}
			clientsocket.close();
		} catch (UnknownHostException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Readline: " + e.getMessage());
		}
	}
	
	
	private static String get_port() {
		try {
			int dirPort = 6666;
			Socket clientsocket = new Socket();
			//clientsocket.bind(new InetSocketAddress("192.168.0.1",9806));
			clientsocket.connect(new InetSocketAddress("127.0.0.1",dirPort));
			DataInputStream in = new DataInputStream(clientsocket.getInputStream());
			DataOutputStream out = new DataOutputStream(clientsocket.getOutputStream());
			out.writeUTF("Obtener");
			String data = in.readUTF();
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
			Socket clientsocket = new Socket();
			clientsocket.connect(new InetSocketAddress("127.0.0.1",dirPort));
			DataInputStream in = new DataInputStream(clientsocket.getInputStream());
			DataOutputStream out = new DataOutputStream(clientsocket.getOutputStream());
			out.writeUTF("Delete,"+proxyPort);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
