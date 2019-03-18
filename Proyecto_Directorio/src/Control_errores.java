import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Control_errores extends Thread {
	List<Proxy> lproxys;
	public Control_errores(List<Proxy> lista_proxys) {
		lproxys=lista_proxys;
		this.start();
	}

	public void run() {
		while(true) {
			System.out.println("---------------------------------------------------");
			System.out.println("Voy a revisar que los proxys esten vivos");
			for(int j=0; j<lproxys.size();j++) {
				int serverPort = lproxys.get(j).getPuerto();
				try {
					Socket proxysocket = new Socket("127.0.0.1",serverPort);
					proxysocket.close();
				} catch (IOException e1) {
					System.out.println("El proxy "+lproxys.get(j).getIp()+","+lproxys.get(j).getPuerto()+" no respondio, lo voy a eliminar");
					lproxys.remove(j);
				}
			}
			/*System.out.println("------------------------Proxys sobrevivientes-------------------------------");
			for(Proxy p:lproxys){
				System.out.println(p.getIp()+","+p.getPuerto()+"->"+p.getConectados());
			}
			System.out.println("----------------------------------------------------------------------------");*/
			System.out.println("Revision terminada, proxima revision en 60 segundos");
			System.out.println("---------------------------------------------------");
			try {
				TimeUnit.SECONDS.sleep(60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
