import java.util.*;
import java.rmi.*;
import java.io.*;
import javax.naming.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.*;
import java.util.concurrent.*;

/*
 * TODO:
 * La richiesta di messaggi spawna un Thread che viene messo in wait finchè la
 * risorsa non è disponibile AKA la lista messaggi non è stata modificata
 */

/*--------------------------------------*
 * Classe Server, comunica con i Clients*
 * si basa su RMI.                      *
 *--------------------------------------*/
@SuppressWarnings("serial")
public class ServerImpl extends Observable implements Server {
	private static final String[] accounts = {"A@mail.it", "B@mail.it", "C@mail.it"};
	private static final String idFile = "idFile.txt";
	private static final boolean TESTING = true;
	private static final String DIR = "./JSON/";

	private HashMap<String, ArrayList<Email>> inboxes;
	private HashMap<String, Boolean> changed;
	private long idCounter = 0;
	private Logger log;

	/**-------------------------------------*
	 * Costruttore dell'oggetto ServerImpl  *
	 * @param l Riferimento al Logger       *
	 * @see Logger                          *
	 * @return L'oggetto ServerImpl         *
	 *--------------------------------------*/
	public ServerImpl(Logger l) {
		this.log = l;
		initServer();
		if (TESTING) { // Per quetioni accademiche
			popolaJSON();
			writeContToFile(idCounter);
		}
		// Popola HashMaps
		for (String account : accounts) {
			readFromJson(account);
			changed.put(account, false);
		}
		idCounter = readContFromFile();
	}

	/**-------------------------------------*
	 * Inizializza alcune variabili del     *
	 * server                               *
	 *--------------------------------------*/
	private void initServer() {
		inboxes = new HashMap<>();
		changed = new HashMap<>();
		for (String account : accounts) {
			ArrayList<Email> tmp = new ArrayList<>();
			inboxes.put(account, tmp);
			changed.put(account, false);
		}
	}

	/**-------------------------------------*
	 * Comunica al server che il client si è*
	 * connesso e richiede la lista messaggi*
	 * @param account nome client connesso  *
	 * @throws RemoteException              *
	 * @return L'inbox dell'account         *
	 *--------------------------------------*/
	public ArrayList<Email> connect(String account){
		log.log(account + " si è collegato al server.");
		try {
			return getMails(account);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**-------------------------------------*
	 * Invia l'Email al destinatario        *
	 * @param m Email da inviare            *
	 * @throws RemoteException              *
	 * @see Email                           *
	 *--------------------------------------*/
	// se volessi controllare lo stato dell'invio: usare Executor, Callable
	// e FutureTask. Controllare FutureTask con .isDone, una volta true usare
	// .get che in caso di errore lancia un'eccezione
	public void sendMessage(Email m) throws RemoteException {
		log.success("Richiesta invio\nda:\n  " + m.getMittente()
				+ "\na:\n  " + m.getDestinatario());
		Thread t = new Thread(new SendMessage(m));
		t.start();
	}

	/**-------------------------------------*
	 * Elimina l'Email                      *
	 * @param m Email da eliminare          *
	 * @param a Account da cui eliminare    *
	 * @throws RemoteException              *
	 * @return La lista messaggi aggiornata *
	 *--------------------------------------*/
	public ArrayList<Email> deleteMessage(Email m, String a) throws RemoteException {
		log.log("Richiesta di cancellazione di " + m.getID() +
				" da parte di " + m.getDestinatario());
		Thread t = new Thread(new DeleteMessage(m, a));
		t.start();
		try {
			t.join();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		return inboxes.get(a);
	}

	/**-------------------------------------*
	 * Restituisce la lista messaggi        *
	 * associata all'account                *
	 * @throws RemoteException              *
	 * @param account Nome account a cui è  *
	 * associata la lista dei messaggi      *
	 *--------------------------------------*/
	public ArrayList<Email> getMails(String account) throws RemoteException {
		return inboxes.get(account);
	}

	/**-------------------------------------*
	 * Controlla che non ci siano nuove mail*
	 * @param a Account da controllare      *
	 * @return true o false                 *
	 *--------------------------------------*/
	public boolean hasNewMail(String account) throws RemoteException {
		boolean ret = changed.get(account);
		changed.replace(account, false);
		return ret;
	}

	/**-------------------------------------*
	 * Reads from a JSON file and update the*
	 * Inboxes                              *
	 *--------------------------------------*/
	@SuppressWarnings("unchecked")
	private synchronized void readFromJson(String account) {
		ObjectMapper mapper = new ObjectMapper();
		File file = new File(DIR + account + ".json");
		try {
			TypeReference<ArrayList<Email>> tf
				= new TypeReference<ArrayList<Email>>() {};
			inboxes.replace(account, mapper.readValue(file, tf));
		} catch (NullPointerException | IOException e) {
			log.error("lettura da JSON", "inbox di " + account + " vuota");
		}
	}

	/**-------------------------------------*
	 * Writes to a JSON file                *
	 * @param account Nome utente del quale *
	 * verranno salvati i messaggi          *
	 *--------------------------------------*/
	private synchronized void writeToJson(String account) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(DIR + account + ".json"), inboxes.get(account));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**-------------------------------------*
	 * Legge il da file l'ultimo valore     *
	 * visto del contatore ID               *
	 * @return il valore letto              *
	 *--------------------------------------*/
	private synchronized long readContFromFile() {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(idFile));
			return in.readLong();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**-------------------------------------*
	 * Scrive su file l'attuale valore del  *
	 * contatore                            *
	 * @param cont valore da scrivere       *
	 *--------------------------------------*/
	private synchronized void writeContToFile(long cont) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(idFile));
			out.writeLong(cont);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// TASKS:

	/*--------------------------------------*
	 * Custom Runnable per l'invio dei      *
	 * messaggi                             *
	 *--------------------------------------*/
	class SendMessage implements Runnable {
		private Email msg;
		private static final String fakeMailSbj = "Errore invio del messaggio";
		private static final String fakeMailTxt =
			"Abbiamo riscontrato un problema durante l'invio del messaggio.\n" +
			"Controllare l'indirizzo di destinazione e riprovare.";
		/**-------------------------------------*
		 * Costruttore dell'oggetto SendMessage *
		 * @param msg messaggio da inviare      *
		 * @return L'oggetto SendMessage        *
		 *--------------------------------------*/
		public SendMessage(Email msg) {
			this.msg = msg;
		}

		/**-------------------------------------*
		 * Metodo eseguito quando il thread che *
		 * esegue SendMessage viene avviato     *
		 *--------------------------------------*/
		public void run() {
			String[] destinatari = msg.getDestinatario().split(";");

			for (String destinatario : destinatari) {
				try {
					ArrayList<Email> tmp = inboxes.get(destinatario);
					synchronized(this) {
						msg.setID(idCounter++);
						writeContToFile(idCounter);
						tmp.add(msg);
						inboxes.replace(destinatario, tmp);
						// TODO check this
						for (String key : msg.getDestinatario().split(";")) {
							changed.replace(key, true);
						}
					}
					log.success("Messaggio " + msg.getID() +
							" inviato a " + destinatario);
					writeToJson(destinatario);
				} catch (NullPointerException e) {
					errorHandler(msg.getMittente());
				}
			}
		}

		/**-------------------------------------*
		 * Metodo per la gestione di errori     *
		 * durante l'invio di una mail          *
		 * @param reciver Account da notificare *
		 *--------------------------------------*/
		private synchronized void errorHandler(String reciver) {
			log.error("invio messaggio", "destinatario inesistente");
			Email fakeMail = new Email(reciver, fakeMailSbj,
					fakeMailTxt, "info@service.it");
			fakeMail.setID(idCounter++);
			writeContToFile(idCounter);
			fakeMail.setPriorita(999);
			ArrayList<Email> tmp = inboxes.get(reciver);
			tmp.add(fakeMail);
			inboxes.replace(msg.getMittente(), tmp);
			changed.replace(reciver, true);
		}
	}


	/*--------------------------------------*
	 * Custom Runnable per la cancellazione *
	 * dei messaggi                         *
	 *--------------------------------------*/
	class DeleteMessage implements Runnable {
		private Email msg;
		private String account;

		/**-------------------------------------*
		 * Costruttore delloggetto DeleteMessage*
		 * @param msg messaggio da elminare     *
		 * @param account da cui eliminare      *
		 * @return L'oggetto DeleteMessage      *
		 *--------------------------------------*/
		public DeleteMessage(Email msg, String account) {
			this.msg = msg;
			this.account = account;
		}

		/**-------------------------------------*
		 * @{inheritDoc}                        *
		 *--------------------------------------*/
		public void run() {
			ArrayList<Email> tmpArray = new ArrayList<>();
			for (Email tmpMail : inboxes.get(account)) {
				if (tmpMail.getID() != msg.getID()) {
					tmpArray.add(tmpMail);
				}
			}
			synchronized (this) {
				inboxes.replace(account, tmpArray);
			}
			writeToJson(account);
			log.success("Messaggio " + msg.getID() + " eliminato.");
		}
	}

	/**-------------------------------------*
	 * Metodo di testing                    *
	 *--------------------------------------*/
	private void popolaJSON() {
		log.log(">>>TESTING<<<\nPopolo le inbox.");
		for (String account : accounts) {
			ArrayList<Email> tmp = new ArrayList<>();
			for (int j = 0; j < 10; j++) {
				Email dummyMail = new Email(account, "oggetto "+j, "Testo "+j,"service@mail.it");
				dummyMail.setID(idCounter++);
				tmp.add(dummyMail);
			}
			inboxes.put(account, tmp);
			writeToJson(account);
		}
	}
}
