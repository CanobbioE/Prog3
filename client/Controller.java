import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;

@SuppressWarnings("rawtypes")
public class Controller implements java.awt.event.ActionListener, javax.swing.event.ListSelectionListener{

	private final Client modello;
	private JList lista;
	private FrameRispondi emailFrame;
	private JDialog errore;
	private int index = -1;
	private int cont = 0;
	private final String input = "Errore durante l'inserimento dei dati.\n"
		+ "Riprovare, tenendo conto che l'indirizzo"
		+ " email deve essere scritto secondo le seguenti regole: \n"
		+ "\t- Non si può utilizzare lo spazio in nessun caso; \n"
		+ "\t- Va usato il carattere ; dopo ogni indirizzo;\n"
		+ "\t- Il destinatario è formato da caratteri alfanumerici "
		+ "seguiti da '@mail.it';\n";

	public Controller(Client modello){
		this.modello = modello;
	}

	/**-------------------------------------*
	 * {@inheritDoc}                        *
	 *--------------------------------------*/
	@Override
	public void actionPerformed(java.awt.event.ActionEvent e) {
		JButton button = (JButton)e.getSource();
		String text = button.getText();

		switch (text) {
			case "C@mail.it":
			case "B@mail.it":
			case "A@mail.it":
				modello.initClient(text);
				break;

			case "Rispondi":
				if(cont == 0){
					if(index != -1){
						emailFrame = new FrameRispondi(this,
								modello.getEmail(index), modello.getNickname(),
								FrameType.RISPONDI);
						emailFrame.setVisible(true);
						cont+=1;
					}
				}
				break;

			case "Rispondi a tutti":
				if(cont == 0){
					if(index != -1){
						emailFrame = new FrameRispondi(
								this, modello.getEmail(index), modello.getNickname(),
								FrameType.RISPONDI_ALL);
						emailFrame.setVisible(true);
						cont+=1;
					}
				}
				break;

			case "Inoltra":
				if(cont == 0){
					if(index != -1){
						emailFrame = new FrameRispondi(
								this, modello.getEmail(index),modello.getNickname(),
								FrameType.INOLTRA);
						emailFrame.setVisible(true);
						cont+=1;
					}
				}
				break;

			case "Crea messaggio":
				if(cont == 0){
					emailFrame = new FrameRispondi(
							this, null, modello.getNickname(), FrameType.INVIA);
					emailFrame.setVisible(true);
					cont+=1;
				}
				break;

			case "Cancella":
				if(cont == 0){
					if(index != -1){
						if (modello.deleteMessage(index)) {
							index = -1;
						} else {
							JOptionPane.showMessageDialog(errore,
									"Impossibile cancellare");
						}
					}
				}
				break;

			case "Invia":
				if(check(emailFrame.getDestinatario())){
					if(emailFrame.getTitolo().length() != 0){
						if (modello.sendMessage(emailFrame.getEmail())) {
							JOptionPane.showMessageDialog(errore,
									"Messaggio inviato");
						} else {
							JOptionPane.showMessageDialog(errore,
									"Errore invio messaggio");
						}
					} else {
						errore = new JDialog();
						JOptionPane.showMessageDialog(errore,
								"Completare il form con il titolo");
					}
				} else {
					errore = new JDialog();
					JOptionPane.showMessageDialog(errore, input);
				}
				emailFrame.dispose();
				cont-=1;
				break;

			case "Chiudi":
				emailFrame.dispose();
				cont-=1;
				break;

			default:
				break;
		}
	}

	/**-------------------------------------*
	 * Verifica che l'indirizzo inserito    *
	 * rispetti determinati standard        *
	 * @param prova Stringa da controllare  *
	 * @return True o False                 *
	 *--------------------------------------*/
	public boolean check(String prova){
		return prova.matches("^([a-zA-Z0-9]+(@mail.it;))+$");
	}

	/**-------------------------------------*
	 * {@inheritDoc}                        *
	 *--------------------------------------*/
	@Override
	public void valueChanged(ListSelectionEvent e) {
		lista = (JList)e.getSource();
		index = lista.getSelectedIndex();
		if(index != -1){
			index = lista.getModel().getSize()-1 - index;
			modello.viewEmail(index);
		}
	}
}
