

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
			//System.out.println(data);
			int serverPort = 1234;
			Socket proxysocket = new Socket("127.0.0.1",serverPort);
			DataInputStream in1 = new DataInputStream(proxysocket.getInputStream());
			DataOutputStream out1 = new DataOutputStream(proxysocket.getOutputStream());
			out1.writeUTF(data);
			String respuesta = in1.readUTF();
			//System.out.println(respuesta);
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
