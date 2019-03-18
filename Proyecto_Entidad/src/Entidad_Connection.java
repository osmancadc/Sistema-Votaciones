
import java.io.*;
import java.net.*;
import java.util.*;

public class Entidad_Connection extends Thread{

	DataInputStream in;
	DataOutputStream out;
	Socket entidadsocket; 


	public Entidad_Connection(Socket aEntidadSocket) {
		try {
			entidadsocket = aEntidadSocket;
			
			in = new DataInputStream(entidadsocket.getInputStream());
			out = new DataOutputStream(entidadsocket.getOutputStream());
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println (new File (".").getAbsolutePath ());
		File archivo_consultas = null;
		FileReader fr = null;
		BufferedReader br = null;
		try{
			archivo_consultas = new File ("./proyectos1");
			fr = new FileReader (archivo_consultas);
			br = new BufferedReader (fr);
			String linea;
			int t_actual = 0;
			while ((linea = br.readLine())!=null){
				System.out.println(linea);
				String[] tokens = linea.split(",");
				int tiempo = Integer.parseInt(tokens[1]);
				System.out.println(tiempo);
				System.out.println("se procede a sleep");
				Thread.sleep((tiempo - t_actual)*1000);
				
				
				out.writeUTF("puta");
				
				System.out.println(in.readUTF());
				t_actual = tiempo;
				System.out.println("sigue");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if( null != fr ){
					fr.close();
				}
			}
			catch (Exception e2){
				e2.printStackTrace();
			}
		}
		try {
			entidadsocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





}
