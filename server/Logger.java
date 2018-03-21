import javax.swing.*;

@SuppressWarnings("serial")
public class Logger extends JTextArea{

	public Logger() {
		setEditable(false);
	}

	public void log(String s) {
		this.append("LOG: " + s + "\n");
	}

	public void error(String where, String s) {
		this.append("ERRORE IN " + where + ": " + s + "\n");
		System.out.println("Errore, dettagli nel log del server");
	}
	public void success(String s) {
		this.append("OK: " + s + "\n");
	}
}
