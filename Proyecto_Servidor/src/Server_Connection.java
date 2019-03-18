import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Server_Connection extends Thread{

	DataInputStream in;
	DataOutputStream out;
	Socket proxysocket; 
	List<Integer> lista_u;
	List<Integer> lista_e;
	List<Consulta> lista_c;

	public Server_Connection(Socket aProxySocket, List<Integer> lista_ids, List<Integer> lista_entidades, List<Consulta> lista_consultas) {
		try {
			proxysocket = aProxySocket;
			lista_u = lista_ids;
			lista_e = lista_entidades;
			lista_c = lista_consultas;
			in = new DataInputStream(proxysocket.getInputStream());
			out = new DataOutputStream(proxysocket.getOutputStream());
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			String data = in.readUTF();
			//System.out.println(data);

			//System.out.println(data + "llego!");
			char tipo_cliente = data.charAt(0);
			char operacion = data.charAt(1);
			//System.out.println(tipo_cliente);
			System.out.println(operacion);

			if (tipo_cliente == 'u') {
				if (operacion == 'c') {
					String id = "";
					for (int i = 2; i < data.length(); i++) {
						id = id + data.charAt(i);
					}
					boolean valido = validarId_u(Integer.parseInt(id));
					if (valido) {
						out.writeUTF("Id validado exitosamente");
						System.out.println("Se ha conectado el usuario con id " + id);
					}
					else {
						out.writeUTF("Id invalido");
					}
				}
				else if(operacion == 'd') {
					String id = "";
					for (int i = 2; i < data.length(); i++) {
						id = id + data.charAt(i);
					}
					desconectar_u(Integer.parseInt(id));
					out.writeUTF("Desconeccion exitosa");
					System.out.println("Se ha desconectado el usuario con id " + id);
				}
				else if (operacion == 's'){
					String consultas = devolverConsultas(lista_c);
					if (consultas.equals("")) {
						out.writeUTF("No hay consultas disponibles actualmente");
					}
					else {
						out.writeUTF("Las consultas disponibles son:\n" + consultas);
					}
				}
				else if (operacion == 'v'){
					String[] tokens = data.split(",");
					if(validarId_u(Integer.parseInt(tokens[1]))){
						out.writeUTF("El voto es invalido, usuario incorrecto");
					}
					else if (!validarConsulta_e(tokens[2])){
						out.writeUTF("El voto es invalido, la consulta no existe");
					}
					else if(!validarVoto_e(tokens[3])){
						out.writeUTF("El voto es invalido, el voto debe ser UNICAMENTE: alto,medio o bajo");
					}
					else if(!validarVoto(tokens[1],tokens[2])){
						out.writeUTF("El voto es invalido, ya se voto en la misma consulta");
					}
					else{
						registrarVoto(tokens[1], tokens[2],tokens[3]);
						out.writeUTF("El voto fue registrado");
					}

				}
			} else if (tipo_cliente == 'e') {
				if (operacion == 'c') {
					String id = "";
					for (int i = 2; i < data.length(); i++) {
						id = id + data.charAt(i);
					}
					boolean valido = validarId_e(Integer.parseInt(id));
					if (valido) {
						out.writeUTF("Id validado exitosamente");
						System.out.println("Se ha conectado la entidad con id " + id);
					}
					else {
						out.writeUTF("Id invalido");
					}
				}
				else if(operacion == 'd') {
					String id = "";
					for (int i = 2; i < data.length(); i++) {
						id = id + data.charAt(i);
					}
					desconectar_e(Integer.parseInt(id));
					out.writeUTF("Desconeccion exitosa");
					System.out.println("Se ha desconectado la entidad con id " + id);
				}else if (operacion == 'v') {
					String id = "";
					for (int i = 2; i < data.length(); i++) {
						id = id + data.charAt(i);
					}
					String votos = devolverVotos(id);
					if (votos.equals("")) {
						out.writeUTF("No hay votos registrados actualmente");
					}
					else {
						out.writeUTF("Los votos de sus consultas son:\n" + votos);
					}
				}
			}
			else {
				out.writeUTF("Ya recibi!!!!!!!");
			}


		}catch (EOFException e) {
			System.out.println("EOF: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				proxysocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean validarId_u(int id) {
		boolean valido = true;
		for (int i = 0; i < lista_u.size(); i++) {
			//System.out.println(id+"->"+lista_u.get(i));
			if (lista_u.get(i)==id) {
				valido = false;
			}
		}
		if (valido) {
			lista_u.add(id);
		}
		return valido;
	}


	private void desconectar_u(int id) {
		for (int i = 0; i < lista_u.size(); i++) {
			if (lista_u.get(i)==id) {
				lista_u.remove(i);
			}
		}
	}

	private boolean validarId_e(int id) {
		boolean valido = true;
		for (int i = 0; i < lista_e.size(); i++) {
			if (lista_e.get(i)==id) {
				valido = false;
			}
		}
		if (valido) {
			lista_e.add(id);
		}
		return valido;
	}

	private boolean validarConsulta_e(String idCons) {
		for(Consulta c:lista_c){
			//			System.out.println(c.getID_Consulta()+"="+idCons);
			if(idCons.compareTo(c.getID_Consulta()) ==0)
				return true;
		}
		return false;
	}

	private boolean validarVoto_e(String voto) {
		if(voto.compareTo("alto")==0 || voto.compareTo("medio")==0 || voto.compareTo("bajo")==0){
			return true;
		}
		else{
			return  false;
		}
	}

	private void desconectar_e(int id) {
		for (int i = 0; i < lista_e.size(); i++) {
			if (lista_e.get(i)==id) {
				lista_e.remove(i);
			}
		}
	}

	public String devolverConsultas(List<Consulta> lista_consultas) {
		String resultado = "";
		for (int i = 0; i < lista_consultas.size(); i++) {
			Consulta c = lista_consultas.get(i);
			resultado = resultado + c.getID() + "," + c.getID_Consulta() + "," + c.getNombre() + "\n";
		}
		return resultado;
	}

	public boolean validarVoto(String ID_Persona, String ID_Consulta){
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			archivo = new File("./votos.txt");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			String linea;
			while((linea=br.readLine())!=null) {
				//System.out.println(linea);
				String[] tokens = linea.split(",");
				String ID_a = tokens[0];
				String IDConsulta = tokens[1];
				System.out.println(ID_Persona+"->"+ID_a);
				System.out.println(ID_Consulta+"->"+IDConsulta);
				if(ID_a.compareTo(ID_Persona) == 0 && IDConsulta.compareTo(ID_Consulta)==0){
					return false;
				}
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
		return true;
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
	private String get_entidad(String id){
		for(Consulta c:lista_c){
			if(id.compareTo(c.getID_Consulta())==0)
				return c.getID();
		}
		return null;
	}
	private String devolverVotos(String id) {
		String resultado = "";
		File archivo = null;
		FileReader fr = null;
		ArrayList<Votos> votos = new ArrayList<Votos>();
		boolean tienen_v=false;
		BufferedReader br = null;
		try {
			archivo = new File("./votos.txt");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			String linea;
			while((linea=br.readLine())!=null) {
				System.out.println(linea);
				String[] tokens = linea.split(",");
				String idvoto = tokens[0];
				String IDConsulta = tokens[1];
				String voto = tokens[2];

				votos.add(new Votos(idvoto,IDConsulta,voto));
				/*if(id.equals(entidad_c)){
					resultado = resultado + idvoto + "," + IDConsulta + "," + voto + "\n";

				}*/
			}
			Collections.sort(votos);
			int p_actual=-1;
			int contA=-1,contB=-1,contM=-1;
			for(Votos v:votos) {	
				String entidad_c=get_entidad(v.getId_proyecto());
				if(Integer.parseInt(v.getId_proyecto())>p_actual) {
					p_actual=Integer.parseInt(v.getId_proyecto());
					if(contA>-1 && contB>-1 && contM>-1) {
						resultado=resultado+"\nTotal de votos Altos: "+contA;
						resultado=resultado+"\nTotal de votos Medios: "+contM;
						resultado=resultado+"\nTotal de votos bajos: "+contB;
					}
					contA=0;
					contB=0;
					contM=0;
					if(entidad_c.compareTo(id)==0) {
						resultado=resultado+"\n\n"+get_nombre_proyecto(v.getId_proyecto());
					}
					else {contA--; contB--; contM--;}

				}
				if(entidad_c.compareTo(id)==0) {
					tienen_v=true;
					if(v.getVoto().compareTo("alto")==0)
						contA++;
					else if(v.getVoto().compareTo("medio")==0)
						contM++;
					else
						contB++;
				}
			}
			if(contA>-1 && contB>-1 && contM>-1) {
			resultado=resultado+"\nTotal de votos Altos: "+contA;
			resultado=resultado+"\nTotal de votos Medios: "+contM;
			resultado=resultado+"\nTotal de votos bajos: "+contB;
			}
			if(!tienen_v) {
				resultado+="\n\nTus proyectos aun no tienen votaciones!";
			}
			resultado+="\n\n\nNOTA: los proyectos que aun no tienen votos no apareceran aqui.\n\n";

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
		return resultado;
	}

	private String get_nombre_proyecto(String id_proyecto) {
		for(Consulta c:lista_c) {
			if(id_proyecto.compareTo(c.getID_Consulta())==0)
				return c.getNombre();
		}
		return null;
	}
}