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
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * 
 * @author RakeyJakey
 * 
 */
public class Console {

	private JFrame frame;
	private final JTextPane outputText = new JTextPane();
	private final JMenuItem save = new JMenuItem("Save as text file");
	private final JMenuItem clear = new JMenuItem("Clear console");
	private final JMenuItem close = new JMenuItem("Close");
	private StyledDocument doc = outputText.getStyledDocument();
	private SimpleAttributeSet keyWord = new SimpleAttributeSet();
	private SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("HH:mm");
	private Date currentTime;

	public Console() {
		initialize();
	}

	@SuppressWarnings("serial")
	public void initialize() {

		final JMenuBar menuBar = new JMenuBar() {
			{
				add(new JMenu("File") {
					{
						add(save);
						add(clear);
						addSeparator();
						add(close);
					}
				});
			}
		};

		frame = new JFrame("Console") {
			{
				getContentPane().setLayout(
						new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
				setJMenuBar(menuBar);

				add(new ScrollPane() {
					{
						add(outputText);
						outputText.setEditable(false);
					}
				});

			}
		};

		// ActionListener added to save file.
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				save();
			}
		});

		// Action listener to clear console
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					doc.remove(0, doc.getLength());
					log("Console Cleared...");
					log("New Console Started...");

				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});

		// Action listener for optional file-close instead of clicking x.
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				frame.dispose();
			}
		});

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
		currentTime = new Date(System.currentTimeMillis());
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
		StyleConstants.setForeground(keyWord, Color.RED);
		currentTime = new Date(System.currentTimeMillis());

		try {
			doc.insertString(doc.getLength(),
					"  [" + simpleDateFormatter.format(currentTime)
							+ "] [ERROR]: " + message + "\n", keyWord);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	// Main for testing.
	public static void main(String[] args) {
		Console console = new Console();

		for (int i = 0; i < 1000; i++) {
			if (i % 2 == 0)
				console.log("" + i);
			else
				console.logError("" + i);
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
					ex.printStackTrace();
				}

			}

			else if (file.exists()) {

				int confirm = JOptionPane.showConfirmDialog(null,
						"File exists do you want to save anyway?");

				// If confirm YES.
				if (confirm == 0) {

					try {
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(file.getAbsolutePath() + ".txt"));
						writer.write(text);
						writer.close();

					} catch (IOException ex) {
						ex.printStackTrace();

					}

				}

				// If confirm NO
				else if (confirm == 1) {
					JOptionPane.showMessageDialog(null,
							"The file was not saved.");

					// If confirm CANCEL
				} else {
					saveFile.setVisible(false);
				}

			}

		}

		if (option == JFileChooser.CANCEL_OPTION) {
			saveFile.setVisible(false);

		}

	}
}