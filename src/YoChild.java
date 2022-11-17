import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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

    public YoChild(String host, int port) throws IOException {
        initUI();
        if(connect()){
            listen();
        }
    }


    public void howdyButton(){
        howdyButton = new JButton("Howdy");

    };
    @Override
    public void updateStats(char recvCh) throws NullPointerException {
        switch(recvCh){
            case SEND_YO:
                yoCount++;
                if (this.yoCount % 5 == 0) {
                    updateConsoleText(yoLabel.getText());
                }
                break;
            case RECV_YO:
                recvYo++;
                if (this.recvYo % 5 == 0) {
                    updateConsoleText(recvyoLabel.getText());
                }
                break;
            case SEND_HOWDY:
                howdyCount++;
                if (this.howdyCount % 5 == 0) {
                    updateConsoleText(howdyLabel.getText());
                }
                break;
            case RECV_HOWDY:
                recvHowdy++;
                if (this.recvHowdy % 5 == 0) {
                    updateConsoleText(recvhowdyLabel.getText());
                }
                break;
            default:
                throw new NullPointerException("Invalid command");
        };
    }
}

