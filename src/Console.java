import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * This class creates a JFrame in Java Swing with the capabilities to log
 * messages and errors in it when instantiated.
 * 
 * @author RakeyJakey
 * 
 */
public class Console {

	/*
	 * Swing Components
	 */
	private JFrame frame;
	private final JTextPane outputText = new JTextPane();
	private final JMenuItem save = new JMenuItem("Save as text file");
	private final JMenuItem clear = new JMenuItem("Clear console");
	private final JMenuItem terminate = new JMenuItem(
			"Terminate Main Application");
	private final JMenuItem close = new JMenuItem("Close console");
	private JScrollBar scrollBar;

	private final StyledDocument doc = outputText.getStyledDocument();
	private final SimpleAttributeSet keyWord = new SimpleAttributeSet();
	private final SimpleDateFormat simpleDateFormatter = new SimpleDateFormat(
			"HH:mm");
	private Date currentTime;

	public Console() {
		initialize();
	}

	/**
	 * Initializes the JFrame and its content. Makes use of double {{
	 * initialization.
	 */
	private void initialize() {

		/*
		 * Creates a JMenuBar and adds its content to it.
		 */
		final JMenuBar menuBar = new JMenuBar() {
			{
				add(new JMenu("File") {
					{
						add(save);
						add(clear);
						addSeparator();
						add(close);
						add(terminate);
					}
				});
			}
		};

		/*
		 * Creates and populates the frame with a JMenu Bar and JScrollPane.
		 */
		frame = new JFrame("Console") {
			{
				getContentPane().setLayout(
						new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
				setJMenuBar(menuBar);

				/*
				 * Adds the JScrollPane and populates it with the JTextPane and
				 * also initializes the JScrollBar variable.
				 */

				add(new JScrollPane() {
					{
						scrollBar = getVerticalScrollBar();
						getViewport().add(outputText);
						outputText.setEditable(false);
					}
				});

			}
		};

		/*
		 * ActionListener added to save file.
		 */
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				save();
			}
		});

		/*
		 * Action listener to clear console
		 */

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

		/*
		 * Action listener for optional file-close instead of clicking x.
		 */
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				frame.dispose();
			}
		});

		/*
		 * Action listener for the option to terminate the main application
		 * alonside the console.
		 */
		terminate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});

		/*
		 * This sets the bounds as pack is not ideal for this. It will be needed
		 * to be set relative to the main application that is using this
		 * console.
		 */
		frame.setBounds(200, 200, 600, 200);

		/*
		 * Sets as visible and sets the default close operation as dispose on
		 * close so that it does not terminate the main application when the
		 * console is closed.
		 */
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);

		/*
		 * This logs that the console is started.
		 */
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
			scrollBar.setValue(scrollBar.getMaximum());
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
			scrollBar.setValue(scrollBar.getMaximum());
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * This saves the content of the console as a text file to the desired
	 * location when "File - Save as text file" is chosen.
	 */

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

	/*
	 * Main for testing.
	 */
	public static void main(String[] args) {

		final Console console = new Console();
		final Random random = new Random();

		Runnable worker = new Runnable() {

			@Override
			public void run() {
				for (int i = 1; i < 500; i++) {
					console.log("" + i);

					try {
						Thread.sleep(random.nextInt(200));
					} catch (InterruptedException e) {
					}
				}
			}

		};

		worker.run();
	}
}