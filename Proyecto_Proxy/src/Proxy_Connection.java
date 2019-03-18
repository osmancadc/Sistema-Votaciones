

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.IIOException;

public class Proxy_Connection extends Thread{

	DataInputStream in;
	DataOutputStream out;
	Socket clientsocket; 

	public Proxy_Connection(Socket aClientSocket) {
		try {
			clientsocket = aClientSocket;
			in = new DataInputStream(clientsocket.getInputStream());
			out = new DataOutputStream(clientsocket.getOutputStream());
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			String data = in.readUTF();
			char tipo_cliente = data.charAt(0);
			char operacion = data.charAt(1);
			if (tipo_cliente == 'u' && operacion == 's') {
				if(Proxy.contador_global<3 && Proxy.reloj_global<60 && Proxy.pedido) {
					System.out.println("Voy a usar el cache");
					Proxy.contador_global+=1;
					out.writeUTF(Proxy.cache);
					return;
				}
			}
			System.out.println("Voy a hacer una solicitud al servidor");
			int serverPort = 1234;
			Socket proxysocket = new Socket("127.0.0.1",serverPort);
			DataInputStream in1 = new DataInputStream(proxysocket.getInputStream());
			DataOutputStream out1 = new DataOutputStream(proxysocket.getOutputStream());
			out1.writeUTF(data);
			String respuesta = in1.readUTF();
			if (tipo_cliente == 'u' && operacion == 's') {
				Proxy.cache=respuesta;
				Proxy.contador_global=0;
				Proxy.reloj_global=0;
				Proxy.pedido=true;
			}
			out.writeUTF(respuesta);


		}catch (EOFException e) {
			System.out.println("EOF: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			try {
				clientsocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
