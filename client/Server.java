import java.rmi.*;
import java.util.*;

public interface Server extends Remote {

	/**-------------------------------------*
	 * Comunica al server che il client si è*
	 * connesso e richiede la lista messaggi*
	 * @param account nome client connesso  *
	 * @throws RemoteException              *
	 * @return Lista messaggi aggiornata    *
	 *--------------------------------------*/
	public ArrayList<Email> connect(String account) throws RemoteException;

	/**-------------------------------------*
	 * Invia l'Email al destinatario        *
	 * @param m Email da inviare            *
	 * @throws RemoteException              *
	 *--------------------------------------*/
	void sendMessage(Email e)throws RemoteException;

	/**-------------------------------------*
	 * Elimina l'Email                      *
	 * @param m Email da eliminare          *
	 * @param a Account da cui eliminare    *
	 * @throws RemoteException              *
	 * @return Lista messaggi aggiornata    *
	 *--------------------------------------*/
	ArrayList<Email> deleteMessage(Email e, String a)throws RemoteException;

	/**-------------------------------------*
	 * Restituisce la lista messaggi        *
	 * associata all'account                *
	 * @throws RemoteException              *
	 * @param a Nome account a cui è        *
	 * associata la lista dei messaggi      *
	 * @return ArrayList<Email> inbox client*
	 *--------------------------------------*/
	ArrayList<Email> getMails(String a)throws RemoteException;

	/**-------------------------------------*
	 * Controlla che non ci siano nuove mail*
	 * @param a Account da controllare      *
	 * @return true o false                 *
	 *--------------------------------------*/
	boolean hasNewMail(String a) throws RemoteException;
}
