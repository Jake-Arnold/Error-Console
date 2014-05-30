import java.awt.Color;
import java.awt.ScrollPane;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * 
 * @author RakeyJakey
 * 
 */
public class Console {

	private JFrame frame;
	private final JTextPane outputText = new JTextPane();
	private StyledDocument doc = outputText.getStyledDocument();
	private SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("HH:mm");

	public Console() {
		initialize();
	}

	public void initialize() {

		outputText.setEditable(false);

		frame = new JFrame("Output") {
			{
				getContentPane().setLayout(
						new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

				add(new ScrollPane() {
					{
						add(outputText);
					}
				});

			}
		};

		//SET IT RELATIVE TO THE MAIN APPLICATION.
		frame.setBounds(200, 200, 600, 200);
		
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		log("Console started...\n");
	}

	/**
	 * Logs the given message in the console window.
	 * 
	 * @param message
	 */
	public void log(String message) {

		Date currentTime = new Date(System.currentTimeMillis());

		try {
			doc.insertString(doc.getLength(),
					"  [" + simpleDateFormatter.format(currentTime) + "]: "
							+ message + "\n", null);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * Logs the given message in the console window in bold red.
	 * 
	 * @param message
	 */
	public void logError(String message) {
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.RED);
		StyleConstants.setBold(keyWord, true);

		Date currentTime = new Date(System.currentTimeMillis());

		try {
			doc.insertString(doc.getLength(),
					"  [" + simpleDateFormatter.format(currentTime) + "]: "
							+ message + "\n", keyWord);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	// Main for testing.
	public static void main(String[] args) {
		Console console = new Console();

		for (int i = 0; i < 1000; i++) {
			console.log("" + i);
		}
	}
}
