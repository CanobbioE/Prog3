import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;


/*--------------------------------------*
 * Classe ClientGUI funge da view per il*
 * client                               *
 *--------------------------------------*/
public class ClientGUI implements Observer {

	private JFrame frame;
	private JPanel panel_login;
	private JButton luca_button;
	private JButton francesco_button;
	private JButton pietro_button;

	private JPanel panel_mail;
	private JList <String> lista_messaggi;
	public int index;
	private JTextArea testo_messaggio_area;
	private JButton crea_button;
	private JButton rispondi_button;
	private JButton rispondi_tutti_button;
	private JButton inoltra_button;
	private JButton cancella_button;
	private JScrollPane jscroll_list;
	private JScrollPane jscroll_area;
	private javax.swing.GroupLayout jPanel1Layout;
	private javax.swing.GroupLayout jPanel2Layout;

	public ClientGUI(Controller controllo){

		frame = new javax.swing.JFrame();
		panel_login = new javax.swing.JPanel();
		francesco_button = new javax.swing.JButton();
		pietro_button = new javax.swing.JButton();
		luca_button = new javax.swing.JButton();
		panel_mail = new javax.swing.JPanel();
		lista_messaggi = new javax.swing.JList<>();
		testo_messaggio_area = new javax.swing.JTextArea();
		crea_button = new javax.swing.JButton();
		rispondi_button = new javax.swing.JButton();
		rispondi_tutti_button = new javax.swing.JButton();
		inoltra_button = new javax.swing.JButton();
		cancella_button = new javax.swing.JButton();
		jscroll_list = new javax.swing.JScrollPane();
		jscroll_area = new javax.swing.JScrollPane();
		jPanel1Layout = new javax.swing.GroupLayout(panel_login);
		jPanel2Layout = new javax.swing.GroupLayout(panel_mail);

		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new java.awt.CardLayout());

		//Listener
		francesco_button.addActionListener(controllo);
		pietro_button.addActionListener(controllo);
		luca_button.addActionListener(controllo);
		crea_button.addActionListener(controllo);
		rispondi_button.addActionListener(controllo);
		rispondi_tutti_button.addActionListener(controllo);
		inoltra_button.addActionListener(controllo);
		cancella_button.addActionListener(controllo);

		lista_messaggi.addListSelectionListener(controllo);

		//Panel login
		luca_button.setText("A@mail.it");
		francesco_button.setText("B@mail.it");
		pietro_button.setText("C@mail.it");

		// Layouts:
		panel_login.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
					.addContainerGap(75, Short.MAX_VALUE)
					.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
						.addComponent(pietro_button, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(francesco_button, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(luca_button, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
					.addGap(75, 75, 75))
				);
		jPanel1Layout.setVerticalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
					.addContainerGap(50, Short.MAX_VALUE)
					.addComponent(luca_button, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addGap(50, 50, 50)
					.addComponent(francesco_button, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addGap(50, 50, 50)
					.addComponent(pietro_button, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addGap(50, 50, 50))
				);

		frame.getContentPane().add(panel_login, "login");
		frame.pack();


		//Panel mail
		jscroll_list.setViewportView(lista_messaggi);
		testo_messaggio_area.setEditable(false);
		testo_messaggio_area.setColumns(20);
		testo_messaggio_area.setRows(5);
		jscroll_area.setViewportView(testo_messaggio_area);

		rispondi_button.setText("Rispondi");
		rispondi_tutti_button.setText("Rispondi a tutti");
		cancella_button.setText("Cancella");
		crea_button.setText("Crea messaggio");
		inoltra_button.setText("Inoltra");

		// Layouts:
		panel_mail.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup()
					.addGap(20, 20, 20)
					.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jscroll_list, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
							.addComponent(crea_button, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addGap(50, 50, 50)))
					.addGap(20, 20, 20)
					.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jscroll_area)
						.addGroup(jPanel2Layout.createSequentialGroup()
							.addComponent(rispondi_button, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addGap(20, 20, 20)
							.addComponent(rispondi_tutti_button, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addGap(20, 20, 20)
							.addComponent(inoltra_button, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
							.addComponent(cancella_button, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
					.addGap(15, 15, 15))
				);
		jPanel2Layout.setVerticalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
					.addGap(50, 50, 50)
					.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(crea_button, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(rispondi_button, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(rispondi_tutti_button, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(cancella_button, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(inoltra_button, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
					.addGap(20, 20, 20)
					.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
						.addComponent(jscroll_area)
						.addComponent(jscroll_list, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
					.addGap(15, 15, 15))
				);

		frame.getContentPane().add(panel_mail, "mail");
		frame.setVisible(true);
	}

	/**-------------------------------------*
	 * @return Il frame associato           *
	 *--------------------------------------*/
	public JFrame getFrame(){
		return this.frame;
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked", "fallthrough"})
	public void update(Observable o, Object arg) {
		UpdateType type = (UpdateType) ((Pair)arg).getFirst();
		switch (type) {
			case NAME_SET: // Nickname impostato
				((CardLayout) frame.getContentPane().getLayout())
					.show(frame.getContentPane(), "mail");
				frame.setTitle((String)((Pair)arg).getSecond());
				frame.pack();
				break;

			case NEW_MAIL: // Nuovo messaggio
				Email m = (Email)((Pair)arg).getSecond();
				JOptionPane.showMessageDialog(new JDialog(),
						"Ricevuto nuovo messaggio: " + m.getArgomento() + " da: " +
						m.getMittente());
				break;

			case READ_MAIL: // Selezionata mail da leggere
				Email aux = (Email)((Pair)arg).getSecond();
				String text = "Titolo: " + aux.getArgomento() + "\n\nMittente: "
					+ aux.getMittente() + "\n\nDestinatari: "
					+ aux.getDestinatario() + "\n\n\n" + aux.getTesto();
				testo_messaggio_area.setText(text);
				break;

			case OFFLINE: // Server non raggiungibile
				JOptionPane.showMessageDialog(new JDialog(),
						"Server non raggiungibile, verificare la connessione.");
			case REFRESH: // Refresh messaggi
				DefaultListModel modello = new DefaultListModel();
				ArrayList <Email> mail = (ArrayList<Email>) ((Pair)arg).getSecond();
				try {
					for (int i = mail.size()-1; i >= 0; i--) {
						modello.addElement(mail.get(i).getArgomento());
					}
				} catch (NullPointerException e) {
					JOptionPane.showMessageDialog(new JDialog(),
						"Nessuna mail salvata in locale.");
				}
				lista_messaggi.setModel(modello);
				break;

			default:
				break;
		}
	}
}
