import java.util.ArrayList;
import javax.swing.JLabel;
import java.rmi.Naming;
import java.rmi.*;
import java.net.MalformedURLException;

/*--------------------------------------*
 * Classe principale dell'applicazione  *
 *--------------------------------------*/
public class AppEmail {
	private static final String serverName = "rmi://127.0.0.1:2000/server";
	private Server server;

	/**-------------------------------------*
	 * Costruttore della classe AppEmail    *
	 * @return Oggetto AppEmail istanziato  *
	 *--------------------------------------*/
	public AppEmail(){
		connectTo(serverName);

		Client model = new Client(server);
		Controller controller = new Controller(model);
		ClientGUI view = new ClientGUI(controller);
		model.addObserver(view);
	}

	/**-------------------------------------*
	 * Ottiene lo stub del server e recupera*
	 * la lista dei messaggi associati al   *
	 * client                               *
	 * @param s Nome del server             *
	 *--------------------------------------*/
	public void connectTo(String s) {
		try {
			this.server = (Server)Naming.lookup(s);
		} catch (MalformedURLException | NotBoundException | RemoteException e) {
			this.server = null;
			System.out.println("Impossibile connettersi.");
		}
	}
/**-------------------------------------*
 * Metodo main della classe             *
 * @{inheritDorcs}                      *
 *--------------------------------------*/
	public static void main(String args[]) {
		AppEmail avvia = new AppEmail();
	}
}
