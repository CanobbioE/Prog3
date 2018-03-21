import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.*;
import java.io.*;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

/*--------------------------------------*
 * Classe Client funge da model comunica*
 * con il server è osservato dalla vista*
 * e la notifica in caso di modifiche   *
 *--------------------------------------*/
public class Client extends Observable{
	private ArrayList<Email> inbox;
	private String nickname;
	private Server server;
	private int cont = 0;
	private final static String DIR = "./data/";

	/**-------------------------------------*
	 * Costruttore dell'oggetto Client      *
	 * @param a Nickname del proprietario   *
	 * @return L'oggetto Client istanziato  *
	 *--------------------------------------*/
	public Client(Server s){
		this.server = s;
	}

	/**-------------------------------------*
	 * @return Il nickname associato        *
	 *--------------------------------------*/
	public String getNickname(){
		return this.nickname;
	}

	/**-------------------------------------*
	 * @param i Intero, indice della mail   *
	 * @return L'i-esima mail nella inbox   *
	 *--------------------------------------*/
	public Email getEmail(int i){
		return this.inbox.get(i);
	}

	/**-------------------------------------*
	 * Cancella un messaggio dalla lista di *
	 * messaggi associata all'account       *
	 * @param i Indice email da cancellare  *
	 *--------------------------------------*/
	public boolean deleteMessage(int i) {
		try {
			inbox = server.deleteMessage(inbox.get(i), nickname);
			writeToJson();
			setChanged();
			notifyObservers(new Pair<UpdateType, ArrayList<Email>>(
						UpdateType.REFRESH, inbox));
			return true;
		} catch (Exception e) {
			System.out.println("Errore cancellazione: " + e.getMessage());
			return false;
		}
	}

	/**-------------------------------------*
	 * Inizializza il client impostando il  *
	 * nickname, connettendosi al server e  *
	 * avviando il MessageChecker           *
	 * @param nickname Nome account client  *
	 *--------------------------------------*/
	public void initClient(String nickname) {
		this.nickname = nickname;
		setChanged();
		notifyObservers(new Pair<UpdateType, String>(UpdateType.NAME_SET, nickname));
		if (server != null) { // Connessione riuscita
			try {
				this.inbox = server.connect(nickname);
				writeToJson();
				setChanged();
				notifyObservers(new Pair<UpdateType, ArrayList<Email>>(
							UpdateType.REFRESH, inbox));
				MessageChecker mc = new MessageChecker();
				mc.start();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else { // Server non raggiungibile
			readFromJson();
			setChanged();
			notifyObservers(new Pair<UpdateType, ArrayList<Email>>(
						UpdateType.OFFLINE, inbox));
		}
		cont = this.inbox.size();
	}

	/**-------------------------------------*
	 * Notifica la vista dopo che è stato   *
	 * richiesto di leggere una mail.       *
	 * @param index Indice della mail che si*
	 * vuole leggere                        *
	 *--------------------------------------*/
	public void viewEmail(int index){
		setChanged();
		notifyObservers(new Pair<UpdateType, Email>(UpdateType.READ_MAIL,
					this.inbox.get(index)));
	}

	/**-------------------------------------*
	 * Aggiorna l'elenco dei messaggi       *
	 * @return La inbox aggiornata          *
	 *--------------------------------------*/
	public ArrayList<Email> getMessages() {
		try {
			inbox = server.getMails(nickname);
			writeToJson();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		setChanged();
		notifyObservers(new Pair<UpdateType, ArrayList<Email>>(
					UpdateType.REFRESH, inbox));
		return inbox;
	}

	/**-------------------------------------*
	 * Invia al server una mail creata      *
	 * @param m Un Email creata sul client  *
	 * @see Email                           *
	 * @return true o false                 *
	 *--------------------------------------*/
	public boolean sendMessage(Email m) {
		try {
			server.sendMessage(m);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**-------------------------------------*
	 * Reads from a JSON file and update the*
	 * Inboxes                              *
	 *--------------------------------------*/
	@SuppressWarnings("unchecked")
	private void readFromJson() {
		ObjectMapper mapper = new ObjectMapper();
		File file = new File(DIR + nickname + ".json");
		try {
			TypeReference<ArrayList<Email>> tf
				= new TypeReference<ArrayList<Email>>() {};
			inbox = mapper.readValue(file, tf);
		} catch (MismatchedInputException e) {
			System.out.println("File vuoto, non ho letto nulla.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**-------------------------------------*
	 * Writes to a JSON file                *
	 * @param account Nome utente del quale *
	 * verranno salvati i messaggi          *
	 *--------------------------------------*/
	private synchronized void writeToJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(DIR + nickname + ".json"),
					inbox);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*--------------------------------------*
	 * Custom Thread per il controllo di    *
	 * nuovi messaggi.                      *
	 * WARNING: Soluzione poco efficiente e *
	 * in uno scenario reale pericolosa.    *
	 *--------------------------------------*/
	class MessageChecker extends Thread {
		private static final long interval = 2000;

		/**-------------------------------------*
		 * {@inheritDoc}                        *
		 *--------------------------------------*/
		public void run() {
			while (true) {
				try {
					Thread.sleep(interval);
					if (server.hasNewMail(nickname)) {
						getMessages();
						setChanged();
						notifyObservers(new Pair<UpdateType, Email>(
									UpdateType.NEW_MAIL, inbox.get(inbox.size()-1)));
					}
				} catch (InterruptedException | RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
