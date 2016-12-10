package CipherGUI;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.File;
import java.util.Arrays;
import java.util.Stack;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;

public class GUI {

    // global variables
    public static int CIPHER_FONT = 1;
    public static int PLAIN_FONT = 2;

    public static int CIPHER_FILE = 10;
    public static int FREQ_FILE = 11;
    public static int BOTH_FILES = 12;
    public static int DECRYPTER_FILE = 13;

    // parent panels
    private JFrame frame;
    private Timer resizeTimer;
    private JPanel contentPane;

    private ActionListener windowResized;

    // menu bar variables
    private JMenuBar menu;
    private JFileChooser fileChooser;
    private JDialog fileDialog;
    private JColorChooser colorChooser;
    private JDialog colorDialog;

    private JMenu fileMenu;
    private JMenu optionsMenu;

    private JMenuItem newDecryptItem;
    private JMenuItem loadCipherItem;
    private JMenuItem loadFrequencyItem;

    private JMenuItem choosePlainColorItem;
    private JMenuItem chooseCipherColorItem;
    private JMenuItem chooseFontSizeItem;

    // panel variables
    private JPanel textPanel;
    private JPanel freqPanel;
    private JPanel optionsPanel;
    private JPanel mappingPanel;
    private JPanel outputPanel;

    // text panel variables
    private JScrollPane textScroll;
    private JTextPane textArea;
    private String highlightedString;

    // output panel variables
    private JScrollPane outputScroll;
    private JTextArea outputText;

    // mapping panel variables
    private JButton[] mapButtons;
    private JButton undoButton;

    // frequency panel variables
    private JScrollPane freqScroll;
    private JTable freqTable;

    // options panel variables
    private JButton clearHighlightButton;
    private JButton clearChangesButton;
	private JButton searchButton;
	private JTextField searchTextField;

    // history variables
    private Stack<AlphabetMapping> history;

    // constraint variables for the holding panels
    private GridBagConstraints freqBag;
    private GridBagConstraints optionsBag;
    private GridBagConstraints outputBag;
    private GridBagConstraints textBag;
    private GridBagConstraints mappingBag;
    private GridBagConstraints menuBag;

    // font formatting variables
    private Font cipherFont;
    private Font plainFont;

    private Color cipherColor;
    private Color plainColor;

    private int charsPerLine;

    // decrypter associated with the GUI
    private DeCrypter decrypter;
    private File cipherFile;
    private File freqFile;

// ******* CONSTRUCTOS AND CONSTRUCTOR HELPERS *******
    public GUI() {

        frame = new JFrame("Cipher Solver");
        resizeTimer = new Timer(0, null);
        contentPane = new JPanel(new GridBagLayout());
        history = new Stack<>();
        decrypter = new DeCrypter();

        highlightedString = null;
        frame.setContentPane(contentPane);
        addInternalPanels();

        initMenuBar();
        initTextPanel();
        initMapPanel();
        initFreqPanel();
        initOptionsPanel();
        initOutputPanel();

        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();

        frame.setBounds((int) (s.width * .125), (int) (s.height * .125), (int) (s.width * .75), (int) (s.height * .75));
        frame.setResizable(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        windowResized =  new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshGUI();
            }
        };

        frame.addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent e) {
                if(cipherFile != null && freqFile != null){
                    resizeTimer.stop();
                    resizeTimer = new Timer(250, windowResized);
                    resizeTimer.setRepeats(false);
                    resizeTimer.start();
                }
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
            }

            public void componentHidden(ComponentEvent e) {
            }

        });
        
        textArea.setText("Use the FILE menu to open a cipher file and frequency file. Enjoy!\n"
                + "- LOAD A NEW DECRYPTION FILE to let you input the CIPHERTEXT and FREQUENCY at once.\n"
                + "- LOAD INDIVIDUAL FILES if you would like to change the current CIPHERTEXT or FREQUENCY.\n\n\n"
                + "Use the OPTIONS menu to format the text to your liking.\n"
                + "- SET CIPHERTEXT COLOR to change the color of the output font.\n"
                + "- SET PLAINTEXT COLOR to change the color of the output font.\n"
                + "- CHANGE THE FONT SIZE to make readability easier.\n\n\n"
                + "The buttons below tell you which PLAINTEXT character results from which CIPHERTEXT letter.\n"
                + "Push the button you would like the change the mapping for.\n"
                + "The DeCrypter will automatically update the pane and swap the character mappings.\n"
                + "The program will automatically COPY THE PLAINTEXT GENERATED to the clipboard.");
        outputText.setText("This panel will display the plaintext for easier reading and exporting.");

        
        
    }

    /**
     * Constructor helper to format the upper level panels that will house the
     * different parts of the GUI.
     */
    private void addInternalPanels() {
        freqPanel = new JPanel();
        optionsPanel = new JPanel();
        outputPanel = new JPanel();
        textPanel = new JPanel();
        mappingPanel = new JPanel();
        menu = new JMenuBar();

        freqBag = new GridBagConstraints();
        optionsBag = new GridBagConstraints();
        outputBag = new GridBagConstraints();
        textBag = new GridBagConstraints();
        mappingBag = new GridBagConstraints();
        menuBag = new GridBagConstraints();

        // assign the column
        freqBag.gridx = 1;
        optionsBag.gridx = 1;
        outputBag.gridx = 1;
        textBag.gridx = 0;
        mappingBag.gridx = 0;

        // assign the row
        freqBag.gridy = 3;
        optionsBag.gridy = 2;
        outputBag.gridy = 1;
        textBag.gridy = 1;
        mappingBag.gridy = 3;
        menuBag.gridy = 0;

        // give the column widths
        freqBag.weightx = 0.25;
        textBag.weightx = 0.75;
        outputBag.weightx = 0;

        // give the row heights
        textBag.weighty = 0.65;
        mappingBag.weighty = 0.35;
        freqBag.weighty = 0.4;
        outputBag.weighty = 0.5;
        optionsBag.weighty = 0.3;

        textBag.gridheight = 2;
        menuBag.gridwidth = 2;

        // make it fill the area
        freqBag.fill = GridBagConstraints.BOTH;
        optionsBag.fill = GridBagConstraints.BOTH;
        outputBag.fill = GridBagConstraints.BOTH;
        textBag.fill = GridBagConstraints.BOTH;
        mappingBag.fill = GridBagConstraints.BOTH;
        menuBag.fill = GridBagConstraints.BOTH;

        menuBag.anchor = GridBagConstraints.NORTH;
        menu.setPreferredSize(new Dimension(0, 20));

        // add borders
        Border raised = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY, Color.WHITE);
        Border lowered = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.WHITE);
        Border b = BorderFactory.createCompoundBorder(raised, lowered);

        freqPanel.setBorder(b);
        optionsPanel.setBorder(b);
        outputPanel.setBorder(b);
        textPanel.setBorder(b);
        mappingPanel.setBorder(b);

        contentPane.add(mappingPanel, mappingBag);
        contentPane.add(menu, menuBag);
        contentPane.add(freqPanel, freqBag);
        contentPane.add(optionsPanel, optionsBag);
        contentPane.add(outputPanel, outputBag);
        contentPane.add(textPanel, textBag);
    }

    /**
     * Constructor helper method that will take care of formatting the menu bar
     * of GUI
     */
    private void initMenuBar() {

        fileMenu = new JMenu("File");
        optionsMenu = new JMenu("Options");

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\Users\\timothyglensmith\\Documents\\NetBeansProjects\\CipherGUI"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Decrypter File *.dcypt", "dcypt"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text File *.txt", "txt"));

        colorChooser = new JColorChooser();
        colorChooser.setColor(Color.blue);

        fileDialog = new JDialog();
        colorDialog = new JDialog();

        newDecryptItem = new JMenuItem("New...");
        newDecryptItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (openFileChooser(BOTH_FILES)) {
                    decrypter = new DeCrypter(cipherFile, freqFile);
                    highlightedString = null;
                    refreshGUI();
                }
            }
        });

        loadCipherItem = new JMenuItem("Change cipher text...");
		loadCipherItem.setForeground(Color.lightGray);
        loadCipherItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cipherFile != null && freqFile != null) {
                    if (openFileChooser(CIPHER_FILE)) {
                        decrypter = new DeCrypter(cipherFile, freqFile);
                        highlightedString = null;
                        refreshGUI();
                    };
                }
            }
        });

        loadFrequencyItem = new JMenuItem("Change frequency...");
		loadFrequencyItem.setForeground(Color.lightGray);
        loadFrequencyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cipherFile != null && freqFile != null) {
                    if (openFileChooser(FREQ_FILE)) {
                        decrypter = new DeCrypter(cipherFile, freqFile);
                        highlightedString = null;
                        refreshGUI();
                    };
                }
            }
        });

        choosePlainColorItem = new JMenuItem("Plaintext color...");
        choosePlainColorItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openColorChooser(PLAIN_FONT);
            }
        });

        chooseCipherColorItem = new JMenuItem("Ciphertext color...");
        chooseCipherColorItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openColorChooser(CIPHER_FONT);
            }
        });

        chooseFontSizeItem = new JMenuItem("Font size...");
        chooseFontSizeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFontSizeChooser();
            }
        });
        newDecryptItem.setToolTipText("Opens a dialog box that will allow you to choose a Ciphertext file and a Frequency file to create a new DeCrypter.");

        loadCipherItem.setToolTipText("Allows you to change the current cipher text and generate a new plaintext. (Cannot be used until you have already started).");
        loadFrequencyItem.setToolTipText("Allows you to change the current frequency text and generate a new plaintext. (Cannot be used until you have already started).");

        fileMenu.add(newDecryptItem);
        fileMenu.add(loadCipherItem);
        fileMenu.add(loadFrequencyItem);

        optionsMenu.add(choosePlainColorItem);
        optionsMenu.add(chooseCipherColorItem);
        optionsMenu.add(chooseFontSizeItem);

        menu.add(fileMenu);
        menu.add(optionsMenu);
    }

    /**
     * Constructor helper method that will take care of formatting the text area
     * of GUI
     */
    private void initTextPanel() {

        // make the text panel have a GridBagLayous
        textPanel.setLayout(new GridBagLayout());

        // the text is going to go in a text pane in a scroll pane
        textArea = new JTextPane();
        textScroll = new JScrollPane(textArea);
        textScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        textScroll.setPreferredSize(new Dimension(0, 0));

        // format the text area
        cipherFont = new Font("Courier New", Font.PLAIN, 14);
        plainFont = new Font("Courier New", Font.PLAIN, 14);

        changeColor(Color.red, CIPHER_FONT);
        changeColor(Color.blue, PLAIN_FONT);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        textArea.addCaretListener(new CaretListener() {

            public void caretUpdate(CaretEvent e) {
                String str = textArea.getSelectedText();

                if (str != null && str.toLowerCase().equals(str)) {
                    highlightOccurrences(str);
                }
            }
        });

        textArea.setEditable(false);
        textPanel.add(textScroll, c);
    }

    /**
     * Constructor helper method that will take care of formatting the mapping
     * area of GUI
     */
    private void initMapPanel() {

        mappingPanel.setLayout(new GridLayout(3, 12));

        mappingPanel.setPreferredSize(new Dimension(0, 0));

        mapButtons = new JButton[26];

        char c = 'A';
        // Initializes the button mappings
        for (int i = 0; i < 26; i++) {

            String str = "<html><center><font size=16 color=blue>" + Character.toLowerCase(decrypter.getKey().get(c)) + "</font> " + "<br><font color=red>" + c + "</center></font></html>";
            mapButtons[i] = new JButton(str);

            mappingPanel.add(mapButtons[i]);
            c++;
        }

        undoButton = new JButton("UNDO");

        undoButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!history.isEmpty()) {
                    undoChange(false);
                }
            }
        });

        mappingPanel.add(undoButton);
    }

    /**
     * Constructor helper method that will take care of formatting the frequency
     * area of GUI
     */
    private void initFreqPanel() {
        freqPanel.setBackground(Color.white);

        freqPanel.setLayout(new BorderLayout(1, 1));
        freqPanel.setPreferredSize(new Dimension(0, 0));

        freqTable = new JTable(14, 2);
        freqTable.setShowGrid(false);
        freqTable.setTableHeader(null);
        freqTable.setRowHeight(22);

        freqTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
        freqTable.setValueAt("<html><b>FREQUENCIES:</b></html>", 0, 0);
        freqTable.setEnabled(false);

        freqScroll = new JScrollPane(freqTable);
        freqPanel.add(freqScroll);

    }

    /**
     * Constructor helper method that will take care of formatting the options
     * area of GUI
     */
    private void initOptionsPanel() {
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setLayout(new GridBagLayout());
        optionsPanel.setPreferredSize(new Dimension(0, 0));
		
		JPanel optionsPanel_01 = new JPanel();
		JPanel optionsPanel_02 = new JPanel();
		
		optionsPanel_02.setLayout(new GridLayout(1,2));
		optionsPanel_01.setLayout(new GridLayout(1,2));
		
        clearHighlightButton = new JButton("<html><center>Clear<br>Highlighting</html>");
        clearChangesButton = new JButton("<html><center>Clear<br>Changes</html>");

		clearHighlightButton.setBackground(new Color(220,220,220));
		clearChangesButton.setBackground(new Color(220,220,220));
		
		
        clearHighlightButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                highlightedString = null;
				searchTextField.setText("");
                removeHighlights();
            }

        });

        clearChangesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!history.isEmpty()) {
                    undoChange(true);
                }
            }

        });
		
		searchTextField = new JTextField();		
		
		searchTextField.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				highlightOccurrences(searchTextField.getText().toLowerCase());
			}
			
		});
		
		searchTextField.setFont(plainFont);		
		searchButton = new JButton("Find");
		
		searchButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				highlightOccurrences(searchTextField.getText().toLowerCase());
			}
			
		});
		
		
		
		
		optionsPanel_02.add(searchTextField);
		optionsPanel_02.add(searchButton);
        optionsPanel_01.add(clearHighlightButton);
        optionsPanel_01.add(clearChangesButton);
		
		GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = .85;
		c.gridx = 0;
		c.gridy = 0;
		
		optionsPanel.add(optionsPanel_01, c);
		
		c.weighty = 0.15;
		c.gridy = 1;
		
		optionsPanel.add(optionsPanel_02, c);
    }

    /**
     * Constructor helper method that will take care of formatting the history
     * panel of GUI
     */
    private void initOutputPanel() {

        outputText = new JTextArea();
        outputScroll = new JScrollPane(outputText);
        outputPanel.setLayout(new GridBagLayout());

        outputScroll.setPreferredSize(new Dimension(0, 0));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        outputPanel.add(outputScroll, c);

        outputText.setLineWrap(true);
        outputText.setWrapStyleWord(true);
        outputText.addCaretListener(new CaretListener() {

            @Override
            public void caretUpdate(CaretEvent e) {
                String str = outputText.getSelectedText();

                if (str != null && str.toLowerCase().equals(str)) {
                    highlightOccurrences(str);
                    try {
                        textArea.setCaretPosition(textArea.getText().indexOf(str));
                    } catch (IllegalArgumentException ex) {
                        // just ignore this error
                        int x = 0;
                    }
                }
            }

        });

        outputText.setFont(new Font("Tahoma", Font.PLAIN, 12));
    }

    /**
     * Method to draw the text on the screen, generate the frequencies, and
     * change the button mappings.
     */
    private void refreshGUI() {
        frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        int pos = textArea.getCaretPosition();
        // regenerate text
        displayText();
        // map the buttons
        changeButtonText();
        // regenerate frequencies
        setFrequencies();

        textArea.setCaretPosition(pos);
        frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Function to change the text on the buttons
     */
    private void changeButtonText() {
        char c = 'A';
        String plainHexColor = String.format("#%02x%02x%02x", plainColor.getRed(), plainColor.getGreen(), plainColor.getBlue());
        String cipherHexColor = String.format("#%02x%02x%02x", cipherColor.getRed(), cipherColor.getGreen(), cipherColor.getBlue());

        for (int i = 0; i < 26; i++) {

            String str = "<html><center><font size=16 color=" + plainHexColor + ">" + Character.toLowerCase(decrypter.getKey().get(c)) + "</font> " + "<br><font color=" + cipherHexColor + ">" + c + "</center></font></html>";
            mapButtons[i].setText(str);

            if (mapButtons[i].getActionListeners().length == 0) {
                mapButtons[i].addActionListener((ActionEvent e) -> {
                    swapCharacter((JButton) e.getSource());
                });
            }
            c++;
        }
    }

    /**
     * Method to recreate the frequencies chart on the freqPanel
     */
    private void setFrequencies() {
        //freqPanel.removeAll();
        freqPanel.setEnabled(true);

        FrequencyString[] cipherFreq = decrypter.getCipherFreq();
        FrequencyString[] givenFreq = decrypter.getGivenFreq();

        Arrays.sort(cipherFreq, 0, 26, new FrequencyComparator(true));

        for (int i = 0; i < 26; i++) {

            // get the plaintext letter associated with this key
            char c = decrypter.getKey().get(cipherFreq[i].string().charAt(0));

            int j;
            for (j = 0; j < 26; j++) {
                if (givenFreq[j].string().charAt(0) == Character.toUpperCase(c)) {
                    break;
                }
            }

            FrequencyString temp = givenFreq[i];
            givenFreq[i] = givenFreq[j];
            givenFreq[j] = temp;
        }

        for (int i = 0; i < 26; i++) {

            String colorMod = "black>";

            if (i == 0 && Math.abs(cipherFreq[i + 1].percentageUsed() - cipherFreq[i].percentageUsed()) > 0.01) {
                colorMod = "green>";
            } else if (i == 0 && Math.abs(cipherFreq[i + 1].percentageUsed() - cipherFreq[i].percentageUsed()) > 0.001) {
                colorMod = "orange>";
            } else if (i == 0) {
                colorMod = "red>";
            }

            if (i > 0 && Math.abs(cipherFreq[i - 1].percentageUsed() - cipherFreq[i].percentageUsed()) > 0.01
                    && i < 25 && Math.abs(cipherFreq[i + 1].percentageUsed() - cipherFreq[i].percentageUsed()) > 0.01) {
                colorMod = "green>";
            } else if (i > 0 && Math.abs(cipherFreq[i - 1].percentageUsed() - cipherFreq[i].percentageUsed()) > 0.001
                    && i < 25 && Math.abs(cipherFreq[i + 1].percentageUsed() - cipherFreq[i].percentageUsed()) > 0.001) {
                colorMod = "orange>";
            } else if (i > 0) {
                colorMod = "red>";
            }

            FrequencyString cipher = cipherFreq[i];
            FrequencyString gen = givenFreq[i];

            int cipherPer = (int) (cipher.percentageUsed() * 10000);
            double cipherPerD = (double) (cipherPer) / 100;

            int genPer = (int) (gen.percentageUsed() * 10000);
            double genPerD = (double) (genPer) / 100;

            String count = String.valueOf(i + 1);

            String str = ("<html>"/* + count + ") */ + "<font color=" + colorMod + cipher.string().charAt(0)
                    + ": " + String.valueOf(cipherPerD) + "% -> " + String.valueOf(gen.string().charAt(0)).toLowerCase()
                    + ": " + String.valueOf(genPerD) + "%</font></html>");

            freqTable.setValueAt(str, (i) % 13 + 1, (i + 1) / 14);
        }
        freqTable.setEnabled(false);
    }

    /**
     * Method to change the color of the font of the text
     *
     * @param c the Color to change to
     * @param type int value that will decide to assign to the cipher or plain
     * text
     */
    private void changeColor(Color c, int type) {
        if (type == CIPHER_FONT) {
            cipherColor = c;
        } else {
            plainColor = c;
        }
    }

    /**
     * Method to display the cipher and plain text on the screen, as well as an
     * additional output text box
     */
    private void displayText() {

        charsPerLine = textArea.getWidth() / textArea.getGraphics().getFontMetrics(cipherFont).charWidth('X') - 3;

        textArea.setEditable(true);
        textArea.setText("");

        decrypter.generatePlainText();
        String cipherText = decrypter.getCipherText();
        String plainText = decrypter.getPlainText();

        // TURN THESE LINES ON TO REMOVE OTHER CHARACTERS
        //cipherText = cipherText.replaceAll("[^A-Za-z0-9 ]", "");
        //plainText = plainText.replaceAll("[^A-Za-z0-9 ]", "");
        // LOOP THROUGH THE TWO STRINGS PRINTING THEM OUT LINE PER LINE (increment i at the bottom)
        int i;
        for (i = 0; i < cipherText.length() - charsPerLine; i += 0) {

            // remove spaces at the beginning of lines
            while (plainText.charAt(i) == ' ') {
                i++;
            }

            // make the words wrap non-mid word
            int mod = 0;
            int count = 0;
            while (count < 9 && plainText.charAt(i + charsPerLine - mod) != ' ') {
                count++;
                mod++;

                if (count == 9) {
                    mod = 0;
                }
            }

            // format the text to work with line breaks
            int skip = 0;
            for (int j = i; j < i + charsPerLine - mod; j++) {
                if (plainText.charAt(j) == '\n') {
                    skip++;

                    if (skip == 1) {
                        mod = i + charsPerLine - j;
                    }
                }
            }

            // make sure symbols make it to the previous line
            while (plainText.charAt(i + charsPerLine - mod) == '.'
                    || plainText.charAt(i + charsPerLine - mod) == ','
                    || plainText.charAt(i + charsPerLine - mod) == '!'
                    || plainText.charAt(i + charsPerLine - mod) == '?') {

                mod--;
                skip++;
            }

            addToTextArea(plainText.substring(i, i + charsPerLine - mod) + "\n", PLAIN_FONT);
            addToTextArea(cipherText.substring(i, i + charsPerLine - mod) + "\n", CIPHER_FONT);
            addToTextArea("\n", PLAIN_FONT);

            i += charsPerLine - mod + skip;
        }

        // remove spaces at the beginning of lines
        while (plainText.charAt(i) == ' ') {
            i++;
        }

        // add the final line of text
        addToTextArea(plainText.substring(i) + "\n", PLAIN_FONT);
        addToTextArea(cipherText.substring(i) + "\n", CIPHER_FONT);
        addToTextArea("\n", PLAIN_FONT);

        // make the text region highlightable but not editable
        textArea.setEditable(false);

        // display text in the output box
        outputText.setText(plainText);

        removeHighlights();
        highlightOccurrences(highlightedString);

        // copy the text to the clipboard
        StringSelection select = new StringSelection(decrypter.getPlainText());
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();

        clpbrd.setContents(select, null);
    }

    /**
     * Method to add text to the end of the text pane
     *
     * @param msg the String to append
     * @param type int to decide how to format the text
     */
    private void addToTextArea(String msg, int type) {
        Font f;
        Color c;
        if (type == CIPHER_FONT) {
            f = cipherFont;
            c = cipherColor;
        } else {
            f = plainFont;
            c = plainColor;
        }

        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, f.getFontName());
        aset = sc.addAttribute(aset, StyleConstants.FontSize, f.getSize());
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);
        textArea.setCharacterAttributes(aset, false);
        textArea.replaceSelection(msg);
    }

    /**
     * Method to set the key of the decrypter based on the historyList
     *
     * @param a the AlphabetMapping to revert to
     */
    private void undoChange(boolean reset) {
        if (reset) {
            decrypter.setKey(history.firstElement());
            history.clear();
        } else {
            decrypter.setKey(history.pop());
        }

        refreshGUI();
    }

    /**
     * Method to repaint the historyList and add eventListeners for double
     * clicking
     */
    private void addToHistory() {
        history.add(new AlphabetMapping(decrypter.getKey()));
    }

    /**
     * Method to open a dialog box to allow the user to choose a file to load.
     * If the type parameter is BOTH, the file chooser will open twice, allowing
     * the user to load in both a cipher file and a frequency file.
     *
     * @param type int parameter that specifies what kind of file(s) should be
     * loaded and decides where to store that file
     */
    private boolean openFileChooser(int type) {

        if (type == BOTH_FILES) {

            fileChooser.setDialogTitle("CHOOSE CIPHER FILE");

            if (fileChooser.showOpenDialog(fileDialog) != JFileChooser.APPROVE_OPTION) {
                return false;
            }

            cipherFile = fileChooser.getSelectedFile();
            fileChooser.setSelectedFile(null);

            fileChooser.setDialogTitle("CHOOSE FREQUENCY FILE");
            if (fileChooser.showOpenDialog(fileDialog) != JFileChooser.APPROVE_OPTION) {
                cipherFile = null;
                return false;
            }

            freqFile = fileChooser.getSelectedFile();
			
			loadCipherItem.setForeground(Color.BLACK);
			loadFrequencyItem.setForeground(Color.BLACK);

        } else {

            fileChooser.setDialogTitle("Choose file...");
            int result = fileChooser.showOpenDialog(fileDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                if (type == CIPHER_FILE) {
                    cipherFile = fileChooser.getSelectedFile();
                } else {
                    freqFile = fileChooser.getSelectedFile();
                }
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * Method to open a dialog box to allow the user to choose a color
     *
     * @param type int parameter that specifies which font to change
     */
    private void openColorChooser(int type) {

        Color c = JColorChooser.showDialog(colorDialog, "Choose a color...", plainColor);

        if (c != null && cipherFile != null && freqFile != null) {
            changeColor(c, type);
            refreshGUI();
        } else if (c != null) {
            changeColor(c, type);
            changeButtonText();
        }
    }

    private void openFontSizeChooser() {

    }

    /**
     * Method to get user input and swap two characters. A dialog box takes over
     * the GUI and swaps using the DeCrypter swap method
     *
     * @param button The JButton pressed to assign a mapping to
     */
    private void swapCharacter(JButton button) {

        JDialog dialog = new JDialog(frame);
        JLabel label = new JLabel();
        
        String b = String.valueOf(button.getText().charAt(42));
        
        label.setText("<HTML><CENTER>Press a character to map to <br><font size=20>\"" + b + "\"</font></CENTER></HTML>");
        dialog.setLayout(new GridLayout(1,1));

        Rectangle r = frame.getBounds();
        dialog.setBounds(r.x + r.width / 2 - 100, r.y + r.height / 2 - 100, 100, 120);
        dialog.add(label);
        dialog.setModal(true);
        dialog.setUndecorated(true);
        dialog.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {

                if (Character.isLetter(e.getKeyChar())) {
                    addToHistory();
                    decrypter.swapChar(e.getKeyChar(), button.getText().charAt(42));

                    char c = button.getText().charAt(42);

                    if (highlightedString != null && highlightedString.indexOf(String.valueOf(e.getKeyChar())) >= 0) {

                        highlightedString = highlightedString.replaceAll(String.valueOf(e.getKeyChar()), String.valueOf(button.getText().charAt(42)));
                    } else if (highlightedString != null) {
                        highlightedString = highlightedString.replaceAll(String.valueOf(button.getText().charAt(42)), String.valueOf(e.getKeyChar()));
                    }

                    refreshGUI();
                    dialog.dispose();
                } else {
                    dialog.dispose();
                }
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        });

        dialog.setVisible(true);
    }

    /**
     * Method to remove the highlighting done by the user on certain words
     */
    private void removeHighlights() {

        SimpleAttributeSet highlighted = new SimpleAttributeSet();
        StyledDocument doc = textArea.getStyledDocument();

        StyleConstants.setBackground(highlighted, Color.WHITE);

        doc.setCharacterAttributes(0, doc.getLength(), highlighted, false);
    }

    /**
     * Method to hightlight all user input that the current highlighted text
     * matches.
     *
     * @param str
     */
    private void highlightOccurrences(String str) {

        if (str == null || str.length() < 1) {
            return;
        }
        try {
            removeHighlights();
            highlightedString = str;
            SimpleAttributeSet highlighted = new SimpleAttributeSet();
            StyledDocument doc = textArea.getStyledDocument();

            String text = doc.getText(0, doc.getLength());
            StyleConstants.setBackground(highlighted, Color.YELLOW);
            StyleConstants.setFontFamily(highlighted, plainFont.getFontName());

            int pos = 0;
            while ((pos = text.indexOf(str, pos)) >= 0) {
                doc.setCharacterAttributes(pos, str.length(), highlighted, false);
                pos += str.length();
            }
        } catch (BadLocationException e) {
            System.exit(1);
        }
    }

    public static void main(String[] args) {

        GUI g = new GUI();

    }
}
