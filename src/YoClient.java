/* Yo App client
* Possibly the most awesome communication app ever created.
* Why text someone when you can send a 'Yo' to everyone?
*/

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

public abstract class YoClient implements ActionListener {

    /** IP address of the Yo server */
    public final String SERVER_ADDRESS = "10.176.149.20";

    /** Response commands.  Uppercase are responses from things we have sent, lowercase are responses from things other
     * people have sent to us.
     */
    public final char SEND_YO = 'Y';
    public final char RECV_YO = 'y';
    public final char SEND_HOWDY = 'H';
    public final char RECV_HOWDY = 'h';

    /**
     * This panel is where the buttons will go.
     */
    public JPanel bPanel;


    /** Additional GUI related fields which are private. */
    private JScrollPane scrollPane;
    private JTextArea recvPane;
    private JButton yoButton;

    /** Networking related stuff */
    private DataOutputStream dataOut;
    private DataInputStream dataIn;

    private Socket socket;

    /**
     * Must implement this in a child class.
     * @param recvCh response command character
     */
    public abstract void updateStats(char recvCh);

    /**
     * Build and display the GUI (user interface).
     */
    public void initUI() throws IOException {
        JFrame mainFrame = new JFrame("Yo App");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setIconImage(ImageIO.read(Objects.requireNonNull(YoClient.class.getResourceAsStream("yo.png"))));
        mainFrame.getContentPane().setLayout(new BorderLayout(10, 10)); //set layout of frame


        //Add the text area for receiving stuff
        recvPane = new JTextArea();
        recvPane.setEditable(false);
        recvPane.setBackground(Color.DARK_GRAY);
        recvPane.setForeground(Color.WHITE);

        scrollPane = new JScrollPane(recvPane);
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.setPreferredSize(new Dimension(500, 350));

        bPanel = new JPanel();
        yoButton = new JButton("Send Yo"); //add buttons to frame
        yoButton.setEnabled(false);
        yoButton.addActionListener(this);
        bPanel.add(yoButton);

        mainFrame.getContentPane().add(scrollPane, BorderLayout.NORTH);
        mainFrame.getContentPane().add(bPanel, BorderLayout.SOUTH);

        //Display the window.
        mainFrame.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation((screenSize.width / 2) - (mainFrame.getWidth() / 2), (screenSize.height / 2) - (mainFrame.getHeight() / 2));

        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    /**
     * This method is called when a button is pressed.
     * @param e the event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.yoButton) {
            this.send(this.SEND_YO);
        }
    }

    /**
     * Updates the app's text display with additional text, adding a newline character at the end.
     * @param text text to display
     */
    public void updateConsoleText(final String text) {
        SwingUtilities.invokeLater(() -> {
            String consoleText = recvPane.getText();
            recvPane.setText(consoleText + text + "\n");

            JScrollBar sb = scrollPane.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());
        });
    }

    /**
     * Connect to the Yo server.
     * @return true if connect attempt was successful
     * @throws IOException
     */
    public boolean connect() throws IOException {
        socket = new Socket();

        updateConsoleText("Connecting to Yo server ...");

        try {
            socket.connect(new InetSocketAddress(this.SERVER_ADDRESS, 2336));
        } catch (ConnectException ex) {
            updateConsoleText("Could not connect to server: " + ex.getMessage());
            return false;
        }

        this.dataOut = new DataOutputStream(socket.getOutputStream());
        this.dataIn = new DataInputStream(socket.getInputStream());

        updateConsoleText("Connected to Yo server\n");
        yoButton.setEnabled(true);

        return true;
    }

    /**
     * Disconnect from the Yo server.
     */
    public void disconnect() {
        try {
            this.socket.close();
        } catch(IOException ex) {
            updateConsoleText("-- On socket close: " + ex.getMessage());
        }
        yoButton.setEnabled(false);
    }

    /**
     * Send the command character corresponding to a word (like Yo).
     * @param sendCh command character to send
     */
    public void send(char sendCh) {
        try {
            this.dataOut.writeByte(sendCh);
            this.dataOut.flush();
        } catch (IOException ex) {
            updateConsoleText("Could not send Yo: " + ex.getMessage());
        }
    }

    /**
     * Listens for incoming data from Yo server.  Once the app has connected to the Yo server, this method
     * needs to be called.
     */
    public void listen() {
        while (true) {
            char recvCh;

            try {
                recvCh = (char) this.dataIn.readByte();
            }
            catch (IOException ex) {
                updateConsoleText("Stopping listener: " + ex.getMessage());
                this.disconnect();
                break;
            }

            if (recvCh == this.SEND_YO) {
                // invalid op received
                updateConsoleText("I said: Yo!");
            }
            else if (recvCh == this.RECV_YO) {
                updateConsoleText("Someone else said: Yo!");
            }
            else if (recvCh == this.SEND_HOWDY) {
                updateConsoleText("I said: Howdy!");
            }
            else if (recvCh == this.RECV_HOWDY) {
                updateConsoleText("Someone else said: Howdy!");
            }
            else {
                // invalid op received
                updateConsoleText("- invalid op received: " + recvCh);
                continue;
            }

            updateStats(recvCh);  // polymorphism in action
        }
    }
}
