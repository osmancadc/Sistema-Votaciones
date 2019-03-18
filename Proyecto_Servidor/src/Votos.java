
public class Votos implements Comparable<Votos>{
		String cedula;
		String id_proyecto;
		String voto;
		public Votos(String cedula, String id_proyecto, String voto) {
			super();
			this.cedula = cedula;
			this.id_proyecto = id_proyecto;
			this.voto = voto;
		}
		public String getCedula() {
			return cedula;
		}
		public void setCedula(String cedula) {
			this.cedula = cedula;
		}
		public String getId_proyecto() {
			return id_proyecto;
		}
		public void setId_proyecto(String id_proyecto) {
			this.id_proyecto = id_proyecto;
		}
		public String getVoto() {
			return voto;
		}
		
		public void setVoto(String voto) {
			this.voto = voto;
		}
		@Override
		public int compareTo(Votos o) {
			return id_proyecto.compareTo(o.getId_proyecto());
		}
}
