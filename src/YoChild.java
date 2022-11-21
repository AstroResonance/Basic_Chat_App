import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class YoChild extends YoClient {
    private int yoCount;
    private final Label yoLabel = new Label("Yo Count: 0");
    private int howdyCount;
    private final Label howdyLabel = new Label("Howdy Count: 0");
    private int recvYo;
    private int recvHowdy;
    private final Label recvhowdyLabel = new Label("Received Howdy Count: 0");
    private final Label recvyoLabel = new Label("Received Yo Count: 0");

    private JButton howdyButton;
    private JTextArea recvPane;
    private JScrollPane scrollPane;
    private JButton yoButton;
    private JButton connectionButton;

    public YoChild(String host, int port) throws IOException {
        initUI();

        howdyButton = new JButton("Send Howdy");
        howdyButton.addActionListener(e -> send(SEND_HOWDY));
        bPanel.add(howdyButton);

        connectionButton = new JButton("Disconnect");
        connectionButton.addActionListener(e -> disconnect());
        bPanel.add(connectionButton);
        if(connect()) {
            listen();
        }

    };






    @Override
    public void updateStats(char recvCh) throws NullPointerException {
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

