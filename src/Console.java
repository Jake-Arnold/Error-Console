import java.awt.Color;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
		final JMenuItem save = new JMenuItem("Save as text file");

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				save();
			}
		});

		final JMenuBar menuBar = new JMenuBar() {
			{
				add(new JMenu("File") {
					{
						add(save);
					}
				});
			}
		};

		frame = new JFrame("Output") {
			{
				getContentPane().setLayout(
						new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

				setJMenuBar(menuBar);

				add(new ScrollPane() {
					{
						add(outputText);
					}
				});

			}
		};

		// SET IT RELATIVE TO THE MAIN APPLICATION.
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
					"  [" + simpleDateFormatter.format(currentTime) + " ERROR]: "
							+ message + "\n", keyWord);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	// Main for testing.
	public static void main(String[] args) {
		Console console = new Console();

		for(int i = 0; i <40; i++){
			if(i%2 == 0)
				console.log(""+i);
			else 
				console.logError(""+i);
		}
	}

	private void save() {
		String text = outputText.getText();
		JFileChooser saveFile = new JFileChooser();
		int option = saveFile.showSaveDialog(null);
		saveFile.setDialogTitle("Save the file...");

		if (option == JFileChooser.APPROVE_OPTION) {

			File file = saveFile.getSelectedFile();
			if (!file.exists()) {

				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(
							file.getAbsolutePath() + ".txt"));
					writer.write(text);
					writer.close();

				} catch (IOException ex) {

				}

			}

			else if (file.exists()) {

				int confirm = JOptionPane.showConfirmDialog(null,
						"File exists do you want to save anyway?");
				if (confirm == 0) {

					try {
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(file.getAbsolutePath() + ".txt"));
						writer.write(text);
						writer.close();

					} catch (IOException ex) {

					}

				}

				else if (confirm == 1) {

					JOptionPane.showMessageDialog(null,
							"The file was not saved.");

				}

			}

		}

		if (option == JFileChooser.CANCEL_OPTION) {

			saveFile.setVisible(false);

		}

	}
}