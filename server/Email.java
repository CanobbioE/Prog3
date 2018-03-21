import java.time.LocalDate;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("serial")
public class Email implements java.io.Serializable {
	private String mittente;
	private String destinatario;
	private String argomento;
	private String testo;
	private int priorita;
	private String data;
	private long id;


	/**-------------------------------------*
	 * Costruttore per jackson.fasterxml    *
	 *--------------------------------------*/
	public Email() {}

	/**-------------------------------------*
	 * Costruttore di Email                 *
	 * @param d Destinatario del messaggio  *
	 * @param a Argomento del messaggio     *
	 * @param t Testo del messaggio         *
	 * @param p Priorità del messaggio      *
	 *--------------------------------------*/
	public Email(String d, String a, String t, String m){
		destinatario = d;
		mittente = m;
		argomento = a;
		testo = t;
		priorita = 0;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate localDate = LocalDate.now();
		data = dtf.format(localDate);
		id = -1;
	}
	/**-------------------------------------*
	 * @return Mittente del messaggio*      *
	 *--------------------------------------*/
	public String getMittente(){
		return this.mittente;
	}
	/**-------------------------------------*
	 * @return Destinatario del messaggio   *
	 *--------------------------------------*/
	public String getDestinatario(){
		return this.destinatario;
	}
	/**-------------------------------------*
	 * @return Argomento del messaggio*     *
	 *--------------------------------------*/
	public String getArgomento(){
		return this.argomento;
	}
	/**-------------------------------------*
	 * @return Testo del messaggio*         *
	 *--------------------------------------*/
	public String getTesto(){
		return this.testo;
	}
	/**-------------------------------------*
	 * @return Data del messaggio*          *
	 *--------------------------------------*/
	public String getData(){
		return this.data;
	}
	/**-------------------------------------*
	 * Imposta la priorità del messaggio    *
	 * @param p Valore della priorità       *
	 *--------------------------------------*/
	public void setPriorita(int p) {
		this.priorita = p;
	}
	/**-------------------------------------*
	 * Imposta l'ID del messaggio           *
	 * @param id Valore da impostare        *
	 *--------------------------------------*/
	public void setID(long id) {
		this.id = id;
	}

	/**-------------------------------------*
	 * @return Data del messaggio*          *
	 *--------------------------------------*/
	public long getID() {
		return this.id;
	}

	@Override
	public String toString() {
		return ("From: " + this.mittente +
				"\nTo: " + this.destinatario +
				"\nObj: " + this.argomento +
				"\nID: " + this.id);
	}

	public boolean equals(Email m) {
		return this.id == m.id;
	}
}
