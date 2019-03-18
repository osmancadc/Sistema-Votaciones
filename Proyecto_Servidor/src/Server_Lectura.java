import java.io.*;
import java.util.*;

public class Server_Lectura extends Thread{


	List<Consulta> l_consultas;
	File archivo;
	FileReader fr;
	BufferedReader br;
	
	public Server_Lectura(List<Consulta> lista_consultas) {
		// TODO Auto-generated constructor stub
		l_consultas = lista_consultas;
		archivo= null;
		fr = null;
		br = null;
		this.start();
	}
	
	public void run() {
		try {
			archivo = new File("./proyectos.txt");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			String linea;
			int t_actual = 0;
			while((linea=br.readLine())!=null) {
				//System.out.println(linea);
				String[] tokens = linea.split(",");
				String ID = tokens[0];
				String ID_Consulta = tokens[1];
				String Nombre = tokens[2];
				int tiempo = Integer.parseInt(tokens[3]);
				//System.out.println(tiempo);
				Thread.sleep((tiempo - t_actual)*100);
				Consulta cons = new Consulta(ID, ID_Consulta, Nombre);
				l_consultas.add(cons);
				System.out.println("Nueva consulta agregada");
				t_actual = tiempo;
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
	}	
	
}
