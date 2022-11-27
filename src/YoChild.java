import javax.swing.*;
import java.io.IOException;

/**
 * This is the main class for the Yo app.
 */

public class YoChild extends YoClient {
    private int yoCount;
    private int howdyCount;
    private int recvYo;
    private int recvHowdy;

    private JButton howdyButton;
    private JButton connectionButton;

    /**
     * Constructor for the YoChild class that calls the GUI and makes the buttons.
     * This Constructor also listens.
     * @throws IOException
     */

    public YoChild(String host, int port) throws IOException {
        // GUI has to be called first
        initUI();
        // Make buttons
        howdyButton = new JButton("Send Howdy");
        howdyButton.addActionListener(e -> send(SEND_HOWDY));
        bPanel.add(howdyButton);

        connectionButton = new JButton("Disconnect");
        connectionButton.addActionListener(e -> disconnect());
        bPanel.add(connectionButton);
        // Listen in...I wish I could've done this differently
        if(connect()) {
            listen();
        }

    };





    /**
     * This method is called when the user clicks the "Send Yo" button.
     * It keeps count of the number of times the user has sent things on the buttons.
     * @param recvCh response command character
     * @throws NullPointerException
     */

    @Override
    public void updateStats(char recvCh) throws NullPointerException {

        // I used a switch statement because it is easier to read and understand.
        // I also love what I can do with the different cases.
        // Each case just increments or displays the number of times the user has sent a message.

        switch(recvCh){

            case SEND_YO:
                yoCount++;
                if (this.yoCount % 5 == 0) {
                    updateConsoleText(yoCount + " Yos sent.");
                }
                break;
            case RECV_YO:
                recvYo++;
                if (this.recvYo % 5 == 0) {
                    updateConsoleText(recvYo + " Yos received.");
                }
                break;
            case SEND_HOWDY:
                howdyCount++;
                if (this.howdyCount % 5 == 0) {
                    updateConsoleText(howdyCount + " Howdys sent.");
                }
                break;
            case RECV_HOWDY:
                recvHowdy++;
                if (this.recvHowdy % 5 == 0) {
                    updateConsoleText(recvHowdy + " Howdys received.");
                }
                break;
            default:
                throw new NullPointerException("Invalid command");
        };
    }
}

