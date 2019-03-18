import java.util.concurrent.TimeUnit;

public class Reloj_global extends Thread{
	public Reloj_global() {
			this.start();
	}

	public void run() {
		while(true) {
			Proxy.reloj_global+=1;
			//System.out.println(Proxy.reloj_global);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
