import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.io.*;
@SuppressWarnings("serial")
public class FrameRispondi extends JFrame {

	private JLabel label_destinatario;
	private JLabel label_titolo;
	private JLabel label_testo;
	private JLabel label_mittente;
	private JTextField destinatario;
	private JTextField titolo;
	private JTextField mittente;
	private JScrollPane scroll_testo;
	private JTextArea testo;
	private JButton rispondi;
	private JButton chiudi;

	public FrameRispondi(Controller controllo, Email mail, String nickname, FrameType frame){
		label_destinatario = new javax.swing.JLabel();
		label_titolo = new javax.swing.JLabel();
		label_testo = new javax.swing.JLabel();
		label_mittente = new javax.swing.JLabel();
		destinatario = new javax.swing.JTextField();
		titolo = new javax.swing.JTextField();
		mittente = new javax.swing.JTextField();
		chiudi = new javax.swing.JButton();
		scroll_testo = new javax.swing.JScrollPane();
		testo = new javax.swing.JTextArea();
		rispondi = new javax.swing.JButton();

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		rispondi.addActionListener(controllo);
		chiudi.addActionListener(controllo);

		switch(frame){
			case RISPONDI:
				destinatario.setText(mail.getMittente()+";");
				destinatario.setEditable(false);
				titolo.setText("RE:"+mail.getArgomento());
				titolo.setEditable(false);
				rispondi.setText("Invia");
				break;

			case RISPONDI_ALL:
				String var = ";";
				String[] tmpArray = mail.getDestinatario().split(";");
				for (String tmpString : tmpArray) {
					if (!tmpString.equals(nickname)) {
						var += tmpString+";";
					}
				}
				destinatario.setText(mail.getMittente()+var);
				destinatario.setEditable(false);
				titolo.setText("RE:"+mail.getArgomento());
				titolo.setEditable(false);
				rispondi.setText("Invia");
				break;

			case INOLTRA:
				destinatario.setText(mail.getMittente());
				testo.setText(mail.getTesto());
				testo.setEditable(false);
				titolo.setText("FW:"+mail.getArgomento());
				titolo.setEditable(false);
				rispondi.setText("Invia");
				break;

			case INVIA:
				rispondi.setText("Invia");
				break;

			default:
				break;
		}


		mittente.setText(nickname);
		mittente.setEditable(false);
		label_destinatario.setText("Destinatario:");
		label_titolo.setText("Titolo:");
		label_testo.setText("Testo:");
		label_mittente.setText("Mittente:");
		testo.setColumns(20);
		testo.setRows(5);
		scroll_testo.setViewportView(testo);
		chiudi.setText("Chiudi");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
							.addGap(50, 50, 50)
							.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
								.addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
									.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(label_testo, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
										.addComponent(label_titolo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
									.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(scroll_testo, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
										.addComponent(titolo)))
								.addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
									.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
										.addComponent(label_mittente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(label_destinatario, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
									.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
									.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(destinatario)
										.addComponent(mittente)))))
						.addGroup(layout.createSequentialGroup()
							.addGap(100, 100, 100)
							.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
								.addComponent(rispondi, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
							.addGap(75,75,75)
							.addComponent(chiudi, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
							.addGap(0, 100, Short.MAX_VALUE))
							);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addGap(25, 25, 25)
					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(label_mittente, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(mittente, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
					.addGap(25, 25, 25)
					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(label_destinatario, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(destinatario, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
					.addGap(25, 25, 25)
					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(label_titolo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(titolo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
					.addGap(25, 25, 25)
					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(label_testo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(scroll_testo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
					.addGap(25, 25, 25)
					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(rispondi, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(chiudi, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
					.addContainerGap(25, Short.MAX_VALUE))
					);

		pack();
	}

	public Email getEmail() {
		Email dati = new Email(destinatario.getText(), titolo.getText(), testo.getText(), mittente.getText());
		return dati;
	}

	public String getDestinatario(){
		return destinatario.getText();
	}

	public String getTitolo() {
		return titolo.getText();
	}


}
