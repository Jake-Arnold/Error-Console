package com.rakeyjakey.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
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
	private final JMenuItem about = new JMenuItem("About this program");
	private final JMenuItem close = new JMenuItem("Close console");
	private JScrollBar scrollBar;

	private final StyledDocument doc = outputText.getStyledDocument();
	private final SimpleAttributeSet keyWord = new SimpleAttributeSet();
	private final SimpleDateFormat simpleDateFormatter = new SimpleDateFormat(
			"HH:mm");
	private Date currentTime;
	private Color errorColor = Color.RED, normalColor = Color.BLACK,
			warningColor = Color.ORANGE, infoColor = Color.GREEN;

	/**
	 * This constructor simply calls the method to initialize the Swing
	 * components.
	 */
	public Console() {
		initialize();
		setVisible(true);
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
						addSeparator();
						add(close);
						add(terminate);
					}
				});

				add(new JMenu("Console") {
					{
						add(clear);

					}
				});

				add(new JMenu("About") {
					{
						add(about);
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
				clear();
			}
		});

		/*
		 * Action listener for optional console-close instead of clicking x.
		 */
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				close();
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

		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				new JFrame("About this Program") {
					{
						getContentPane().add(new JPanel() {
							{
								setBorder(BorderFactory.createEmptyBorder(10,
										10, 10, 10));
								setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
								add(Box.createRigidArea(new Dimension(0, 10)));
								add(new JLabel(
										"This program is an open source program"));
								add(new JLabel(
										"created by Jake Arnold (RakeyJakey)."));

								add(Box.createRigidArea(new Dimension(0, 20)));
								add(new JLabel(
										"Find the source at https://github.com/RakeyJakey"));
							}
						}, BorderLayout.NORTH);

						getContentPane().add(new JPanel() {
							{
								add(new JButton("OK") {
									{
										addActionListener(new ActionListener() {
											@Override
											public void actionPerformed(
													ActionEvent arg0) {
												dispose();
											}
										});
										setAlignmentX(Component.RIGHT_ALIGNMENT);
									}
								});
							}
						}, BorderLayout.SOUTH);
						setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
						pack();
						setResizable(false);
						setLocationRelativeTo(Console.this.frame);
						setVisible(true);
					}
				};
				;
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

		/*
		 * This logs that the console is started.
		 */
		log("Console started...\n");

	}

	/**
	 * Closes the console and disposes the frame.
	 */
	public void close() {
		frame.dispose();

	}

	/**
	 * Clears the console.
	 */
	public void clear() {
		try {
			doc.remove(0, doc.getLength());
			log("Console Cleared...");
			log("New Console Started...");

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This saves the content of the console as a text file to the desired
	 * location.
	 */

	public void save() {
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
					logInfo("File saved as : " + file.getAbsolutePath()
							+ ".txt");

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
								new FileWriter(file.getAbsolutePath()));
						writer.write(text);
						writer.close();
						logInfo("File saved as : " + file.getAbsolutePath());

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

	/**
	 * Logs the given message in the console window.
	 * 
	 * @param message
	 *            the message to be logged.
	 * 
	 * @return true if message successfully logged.
	 */
	public boolean log(String message) {
		StyleConstants.setForeground(keyWord, normalColor);

		currentTime = new Date(System.currentTimeMillis());
		try {
			doc.insertString(doc.getLength(),
					"  [" + simpleDateFormatter.format(currentTime) + "]: "
							+ message + "\n", null);
			scrollBar.setValue(scrollBar.getMaximum());
			System.out.println("  [" + simpleDateFormatter.format(currentTime)
					+ "]: " + message);
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	/**
	 * Logs the given message in the console window as an info message.
	 * 
	 * @param message
	 *            the info message to be logged.
	 * 
	 * @return true if info message successfully logged.
	 */
	public boolean logInfo(String message) {
		StyleConstants.setForeground(keyWord, infoColor);

		currentTime = new Date(System.currentTimeMillis());

		try {
			doc.insertString(doc.getLength(),
					"  [" + simpleDateFormatter.format(currentTime)
							+ "] [INFO]: " + message + "\n", keyWord);

			scrollBar.setValue(scrollBar.getMaximum());

			System.out.println("  [" + simpleDateFormatter.format(currentTime)
					+ "] [INFO]: " + message);
			return true;

		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	/**
	 * Logs the given message in the console window as a warning.
	 * 
	 * @param message
	 *            the warning message to be logged.
	 * 
	 * @return true if warning message successfully logged.
	 */
	public boolean logWarning(String message) {

		StyleConstants.setForeground(keyWord, warningColor);

		currentTime = new Date(System.currentTimeMillis());

		try {
			doc.insertString(doc.getLength(),
					"  [" + simpleDateFormatter.format(currentTime)
							+ "] [WARNING]: " + message + "\n", keyWord);

			scrollBar.setValue(scrollBar.getMaximum());

			System.out.println("  [" + simpleDateFormatter.format(currentTime)
					+ "] [WARNING]: " + message);
			return true;

		} catch (Exception e) {
			System.out.println(e);
			return false;
		}

	}

	/**
	 * Logs the given message in the console window as an error.
	 * 
	 * @param message
	 *            the error message to be logged.
	 * 
	 * @return true if error message successfully logged.
	 */
	public boolean logError(String message) {
		StyleConstants.setForeground(keyWord, errorColor);

		currentTime = new Date(System.currentTimeMillis());

		try {
			doc.insertString(doc.getLength(),
					"  [" + simpleDateFormatter.format(currentTime)
							+ "] [ERROR]: " + message + "\n", keyWord);

			scrollBar.setValue(scrollBar.getMaximum());

			System.out.println("  [" + simpleDateFormatter.format(currentTime)
					+ "] [ERROR]: " + message);
			return true;

		} catch (Exception e) {
			System.out.println(e);
			return false;
		}

	}

	/**
	 * Sets the visibilty of the console to either true or false.
	 * 
	 * @param visible
	 *            true = visible.
	 */
	public void setVisible(boolean visible) {
		frame.setVisible(visible);

		logInfo("Visibility set to: " + visible);
	}

	/**
	 * Checks if the console is visible.
	 * 
	 * @return true if the console is visible.
	 */
	public boolean isVisible() {
		return frame.isVisible();
	}

	/**
	 * Sets whether the console will always be on top.
	 * 
	 * @param alwaysOnTop
	 *            true = always on top.
	 */
	public void setAlwaysOnTop(boolean alwaysOnTop) {
		frame.setAlwaysOnTop(alwaysOnTop);
		logInfo("Always on top set to: " + alwaysOnTop);
	}

	/**
	 * Checks if the console is set to always be on top.
	 * 
	 * @return true if frame is always on top.
	 */
	public boolean isAlwaysOnTop() {
		return frame.isAlwaysOnTop();
	}

	/**
	 * Sets the title of the frame.
	 * 
	 * @param title
	 *            the title to be set.
	 */
	public void setTitle(String title) {
		frame.setTitle(title);
		logInfo("Frame title set to: " + title);
	}

	/**
	 * Returns the title of the console as a String.
	 * 
	 * @return the title of the console.
	 */
	public String getTitle() {
		return frame.getTitle();
	}

	/**
	 * Sets the normal text color to the color specified.
	 * 
	 * @param color
	 *            the color to change to.
	 */
	public void setTextColor(Color color) {
		normalColor = color;
		logInfo("Normal text color set to: " + color.toString());
	}

	/**
	 * Sets the error text color to the color specified.
	 * 
	 * @param color
	 *            the color to change to.
	 * 
	 */
	public void setErrorTextColor(Color color) {
		errorColor = color;
		logInfo("Error text color set to: " + color.toString());
	}

	/**
	 * Sets the warning text color to the color specified.
	 * 
	 * @param color
	 *            the color to change to.
	 * 
	 */
	public void setWarningTextColor(Color color) {
		warningColor = color;
		logInfo("Warning text color set to: " + color.toString());
	}

	/**
	 * Sets the info text color to the color specified.
	 * 
	 * @param color
	 *            the color to change to.
	 * 
	 */
	public void setInfoTextColor(Color color) {
		infoColor = color;
		logInfo("Info text color set to: " + color.toString());
	}

	/**
	 * Sets the LookAndFeel of the frame.
	 * 
	 * @param laf
	 *            - The LookAndFeel class to set as.
	 */
	public void setLookAndFeel(LookAndFeel laf) {

		try {
			UIManager.setLookAndFeel(laf);
			SwingUtilities.updateComponentTreeUI(frame);
			logInfo("LookAndFeel set to: " + laf.getName());

		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the LookAndFeel of the frame.
	 * 
	 * @param laf
	 *            - The LookAndFeel as a String to set as.
	 */

	public void setLookAndFeel(String laf) {
		try {
			UIManager.setLookAndFeel(laf);
			SwingUtilities.updateComponentTreeUI(frame);
			logInfo("LookAndFeel set to: " + laf);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the font to the specified Font.
	 * 
	 * @param font
	 *            the Font desired to be set.
	 */
	public void setFont(Font font) {
		outputText.setFont(font);
		logInfo("Font set to: " + font.getName());
	}

	/**
	 * Gets the font the console is using.
	 * 
	 * @return the font being used.
	 */
	public Font getFont() {
		return outputText.getFont();
	}

}
