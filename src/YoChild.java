import java.awt.*;
import java.io.IOException;

public class YoChild extends YoClient {
    private int yoCount;
    private Label yoLabel;
    private int howdyCount;
    private Label howdyLabel;

    public YoChild(String host, int port) throws IOException {
        initUI();
        connect();
        listen();
    }

    public void sendYo() {
        this.send(this.SEND_YO);
    }

    public void sendHowdy() {
        this.send(this.SEND_HOWDY);
    }

    @Override
    public void updateStats(char recvCh) {
        if (recvCh == this.SEND_YO || recvCh == this.RECV_YO) {
            this.yoCount++;
            this.yoLabel.setText("Yo count: " + this.yoCount);
        }
        else if (recvCh == this.SEND_HOWDY || recvCh == this.RECV_HOWDY) {
            this.howdyCount++;
            this.howdyLabel.setText("Howdy count: " + this.howdyCount);
        }
        if (this.yoCount % 5 == 0){
            updateConsoleText(yoLabel.getText());
        }
    }
}

