import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {



	public static void main(String[] args) {

		List<Integer> lista_ids = new ArrayList<Integer>();
		List<Integer> lista_entidades = new ArrayList<Integer>();
		List<Consulta> lista_consultas = new ArrayList<Consulta>();
		//llenar_listas(lista_ids,lista_entidades);
		Server_Lectura lec = new Server_Lectura(lista_consultas);
		
		try {
			int serverPort = 1234;
			ServerSocket servidor = new ServerSocket(serverPort);
			while (true) {
				System.out.println("\n\nEsperando conexion...");
				Socket proxysocket = servidor.accept();
				System.out.println("\n\nConexion aceptada...");
				Server_Connection c = new Server_Connection(proxysocket, lista_ids, lista_entidades, lista_consultas);

			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//devolverConsultas(lista_consultas);
	}	

	public static void registrarVoto(String ID_Persona, String ID_Consulta,String Voto) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			String data = ID_Persona+","+ID_Consulta+","+Voto+"\n";
			File file = new File ("./votos.txt");
			if(!file.exists()) file.createNewFile();
			fw = new FileWriter(file.getAbsoluteFile(),true);
			bw = new BufferedWriter(fw);
			bw.write(data);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			try{
				if( null != bw ){
					bw.close();
				}
				if(null != fw) {
					fw.close();
				}
			}
			catch (IOException e2){
				e2.printStackTrace();
			}
		}
	}
	
}