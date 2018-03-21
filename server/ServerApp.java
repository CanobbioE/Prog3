import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

/*--------------------------------------*
 * Classe principale lato server        *
 *--------------------------------------*/
@SuppressWarnings("serial")
public class ServerApp extends JFrame {
	private ServerImpl server;

	/**-------------------------------------*
	 * Costruttore della classe ServerApp   *
	 * @return Oggetto ServerApp istanziato *
	 *--------------------------------------*/
	public ServerApp() {
		super("Server");
		setLayout(new BorderLayout());
		setSize(400, 400);

		// Crea JComponents
		JPanel panelWest = new JPanel();
		panelWest.setLayout(new FlowLayout());
		JButton start = new JButton("Start");
		panelWest.add(start);

		Logger log = new Logger();
		JScrollPane scroll = new JScrollPane(log);

		start.addActionListener(new StartAndStop(log));

		add(panelWest, BorderLayout.WEST);
		add(scroll);


		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**-------------------------------------*
	 * Metodo principale dell'applicazione  *
	 * @{inheritDoc}                        *
	 *--------------------------------------*/
	public static void main(String args[]) {
		ServerApp sa = new ServerApp();
	}
}


/*--------------------------------------*
 * Action listener per il pulsante di   *
 * start and stop                       *
 *--------------------------------------*/
class StartAndStop implements ActionListener {
	private Logger log;
	private Server server,stub;
	private Registry registry;
	private static final int port = 2000;
	private static final String serverName = "server";

	/**-------------------------------------*
	 * Costruttore dell'oggetto StartAndStop*
	 * @param l Logger per il log di eventi *
	 * @return L'oggetto istanziato         *
	 *--------------------------------------*/
	public StartAndStop(Logger l) {
		this.log = l;
	}

	/**-------------------------------------*
	 * Azione effettuata alla pressione del *
	 * pulsante di Start o Stop             *
	 * @{inheritDoc}                        *
	 *--------------------------------------*/
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton)e.getSource();
		if (source.getText() == "Start") {
			log.log("Avvio server...");
			checkSecurity();
			try {
				registry = LocateRegistry.createRegistry(port);
				log.success("registry creato con porta " + port);

				server = (Server)UnicastRemoteObject.exportObject(new ServerImpl(log), port);
				log.success("oggetto server creato");

				registry.bind(serverName, server);
				log.success("server legato al registry");
				log.success("server avviato");
			}
			catch (Exception ex) {
				log.error("START SERVER", ex.getMessage());
				ex.printStackTrace();
			}
			source.setText("Stop");
		} else {
			log.log("Fermo server...");
			try {
				registry.unbind(serverName);
				log.success("unbind avvenuto");
				UnicastRemoteObject.unexportObject(registry, true);
				log.success("registry rimosso");

				log.success("server fermato");
				source.setText("Start");
			} catch (Exception ex) {
				log.error("STOP SERVER", ex.getMessage());
			}
		}
	}

	/**-------------------------------------*
	 * Controllo delle impostazioni di      *
	 * sicurezza del server                 *
	 *--------------------------------------*/
	public void checkSecurity() {
		System.setProperty("java.security.policy", "file:server.policy");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
			log.success("creato security manager");
		}
	}
}
