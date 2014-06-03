package com.rakeyjakey.console;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class creates a JFrame in Java Swing with the capabilities to log
 * messages and errors.
 *
 * @author RakeyJakey
 */
public class Console implements ActionListener {

    private final JTextPane outputText = new JTextPane();
    private final StyledDocument doc = outputText.getStyledDocument();
    private final SimpleAttributeSet keyWord = new SimpleAttributeSet();
    private final SimpleDateFormat simpleDateFormatter = new SimpleDateFormat(
            "HH:mm");
    /*
     * Swing Components
     */
    private JFrame frame;
    private JScrollBar scrollBar;
    private Date currentTime;
    private Color errorColor = Color.RED, normalColor = Color.BLACK,
            warningColor = Color.ORANGE, infoColor = Color.GREEN;


    /**
     * This constructor simply calls the method to set the default LookAndFeel, initialize the Swing
     * components and show the frame.
     */
    public Console() {

        if (System.getProperty("os.name").startsWith("Windows")) {
            try {
                UIManager.setLookAndFeel("com.alee.laf.WebLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        }

        initialize();
        setVisible(true);
    }

    /**
     * Initializes the JFrame and its content. Makes use of double {{
     * initialization.
     */
    private void initialize() {

        frame = new JFrame("Console");

        /*
         * Creates a JMenuBar and adds its content to it.
		 */

        final JMenuBar menuBar = new JMenuBar() {
            {
                add(new JMenu("File") {

                    {

                        String[] fileMenuItems = {"Save as Text Document", "-", "Close Console", "Terminate Main Application"};

                        for (String str : fileMenuItems) {

                            if (str.equals("-"))
                                addSeparator();

                            else {
                                JMenuItem jmi = new JMenuItem(str);
                                add(jmi);
                                jmi.addActionListener(Console.this);
                            }

                        }

                    }
                });

                add(new JMenu("Console") {
                    {
                        String[] fileMenuItems = {"Clear Console"};


                        for (String str : fileMenuItems) {

                            if (str.equals("-"))
                                addSeparator();

                            else {
                                JMenuItem jmi = new JMenuItem(str);
                                add(jmi);
                                jmi.addActionListener(Console.this);
                            }

                        }
                    }
                });

                add(new JMenu("About") {
                    {
                        String[] fileMenuItems = {"About this Program", "License Information"};


                        for (String str : fileMenuItems) {

                            if (str.equals("-"))
                                addSeparator();

                            else {
                                JMenuItem jmi = new JMenuItem(str);
                                add(jmi);
                                jmi.addActionListener(Console.this);
                            }
                        }
                    }
                });
            }
        };

		/*
         * Populates the console with a JMenu Bar and JScrollPane.
		 */

        frame.add(menuBar);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
        frame.setJMenuBar(menuBar);

				/*
                 * Adds the JScrollPane and populates it with the JTextPane and
				 * also initializes the JScrollBar variable.
				 */

        frame.add(new JScrollPane() {
            {
                scrollBar = getVerticalScrollBar();
                getViewport().add(outputText);
                outputText.setEditable(false);
            }
        });


		/*
         * This sets the bounds as pack is not ideal for this. It will be needed
		 * to be set relative to the main application that is using this
		 * console.
		 */
        frame.setBounds(200, 200, 600, 200);

		/*
         * Sets the default close operation as dispose on
		 * close so that it does not terminate the main application when the
		 * console is closed.
		 */
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		/*
         * This logs that the console is started.
		 */
        logInfo("Console started...\n");

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

            } else if (file.exists()) {

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
     * @param message the message to be logged.
     */
    public void log(String message) {
        StyleConstants.setForeground(keyWord, normalColor);

        currentTime = new Date(System.currentTimeMillis());

        try {
            doc.insertString(doc.getLength(),
                    "  [" + simpleDateFormatter.format(currentTime) + "]: "
                            + message + "\n", null);
            scrollBar.setValue(scrollBar.getMaximum());
            System.out.println("  [" + simpleDateFormatter.format(currentTime)
                    + "]: " + message);

        } catch (BadLocationException e) {
            e.printStackTrace();

        }
    }

    /**
     * Logs the given message in the console window as an info message.
     *
     * @param message the info message to be logged.
     */
    public void logInfo(String message) {
        StyleConstants.setForeground(keyWord, infoColor);

        currentTime = new Date(System.currentTimeMillis());

        try {
            doc.insertString(doc.getLength(),
                    "  [" + simpleDateFormatter.format(currentTime)
                            + "] [INFO]: " + message + "\n", keyWord);

            scrollBar.setValue(scrollBar.getMaximum());

            System.out.println("  [" + simpleDateFormatter.format(currentTime)
                    + "] [INFO]: " + message);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs the given message in the console window as a warning.
     *
     * @param message the warning message to be logged.
     */
    public void logWarning(String message) {

        StyleConstants.setForeground(keyWord, warningColor);

        currentTime = new Date(System.currentTimeMillis());

        try {
            doc.insertString(doc.getLength(),
                    "  [" + simpleDateFormatter.format(currentTime)
                            + "] [WARNING]: " + message + "\n", keyWord);

            scrollBar.setValue(scrollBar.getMaximum());

            System.out.println("  [" + simpleDateFormatter.format(currentTime)
                    + "] [WARNING]: " + message);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

    /**
     * Logs the given message in the console window as an error.
     *
     * @param message the error message to be logged.
     */
    public void logError(String message) {
        StyleConstants.setForeground(keyWord, errorColor);

        currentTime = new Date(System.currentTimeMillis());

        try {
            doc.insertString(doc.getLength(),
                    "  [" + simpleDateFormatter.format(currentTime)
                            + "] [ERROR]: " + message + "\n", keyWord);

            scrollBar.setValue(scrollBar.getMaximum());

            System.out.println("  [" + simpleDateFormatter.format(currentTime)
                    + "] [ERROR]: " + message);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

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
     * Sets the visibilty of the console to either true or false.
     *
     * @param visible true = visible.
     */
    public void setVisible(boolean visible) {
        frame.setVisible(visible);

        logInfo("Visibility set to: " + visible);
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
     * Sets whether the console will always be on top.
     *
     * @param alwaysOnTop true = always on top.
     */
    public void setAlwaysOnTop(boolean alwaysOnTop) {
        frame.setAlwaysOnTop(alwaysOnTop);
        logInfo("Always on top set to: " + alwaysOnTop);
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
     * Sets the title of the frame.
     *
     * @param title the title to be set.
     */

    public void setTitle(String title) {
        frame.setTitle(title);
        logInfo("Frame title set to: " + title);
    }

    /**
     * Sets the normal text color to the color specified.
     *
     * @param color the color to change to.
     */
    public void setTextColor(Color color) {
        normalColor = color;
        logInfo("Normal text color set to: " + color.toString());
    }

    /**
     * Sets the error text color to the color specified.
     *
     * @param color the color to change to.
     */
    public void setErrorTextColor(Color color) {
        errorColor = color;
        logInfo("Error text color set to: " + color.toString());
    }

    /**
     * Sets the warning text color to the color specified.
     *
     * @param color the color to change to.
     */
    public void setWarningTextColor(Color color) {
        warningColor = color;
        logInfo("Warning text color set to: " + color.toString());
    }

    /**
     * Sets the info text color to the color specified.
     *
     * @param color the color to change to.
     */
    public void setInfoTextColor(Color color) {
        infoColor = color;
        logInfo("Info text color set to: " + color.toString());
    }

    /**
     * Sets the LookAndFeel of the frame.
     *
     * @param laf - The LookAndFeel class to set as.
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
     * @param laf - The LookAndFeel as a String to set as.
     */

    public void setLookAndFeel(String laf) {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(frame);
            logInfo("LookAndFeel set to: " + laf);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the font the console is using.
     *
     * @return the font being used.
     */
    public Font getFont() {
        return outputText.getFont();
    }

    /**
     * Sets the font to the specified Font.
     *
     * @param font the Font desired to be set.
     */
    public void setFont(Font font) {
        outputText.setFont(font);
        logInfo("Font set to: " + font.getName());
    }

    /**
     * Gets the background color of the main JTextPane.
     *
     * @param color the color to change to.
     */
    public void setBackgroundColor(Color color) {
        outputText.setBackground(color);
        logInfo("Background color set to: " + color.toString());
    }

    /**
     * Gets the background color of the main JTextPane.
     *
     * @return the background color of the main JTextPane.
     */
    public Color getBackggroundColor() {
        return outputText.getBackground();
    }

    /**
     * Creates the about frame.
     */
    protected void createAboutFrame() {
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
                setLocationRelativeTo(frame);
                setVisible(true);
            }
        };
        ;
    }

    /**
     * Opens a webpage to the page of the license agreement.
     */
    protected void viewLicenseAgreement() {

        try {
            Desktop.getDesktop().browse(new URI("http://rakeyjakey.com/license/opensource"));

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void actionPerformed(ActionEvent evt) {

        switch (evt.getActionCommand().toLowerCase()) {
            case "save as text document":
                save();
                break;

            case "about this program":
                createAboutFrame();
                break;

            case "clear console":
                clear();
                break;

            case "close console":
                close();
                break;

            case "terminate main application":
                System.exit(0);
                break;

            case "license information":
                viewLicenseAgreement();
                break;

            default:
                logInfo("This feature has not been implemented yet.");
                break;

        }

    }


}
