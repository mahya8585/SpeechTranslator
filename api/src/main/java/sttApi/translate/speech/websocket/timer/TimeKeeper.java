package sttApi.translate.speech.websocket.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.TimerTask;

/**
 * 一定時間毎にtimeoutのisTimeOutをtrueにする<br>
 * isTimeOutの値がtrueの時、webSocketのsessionを切断する<br>
 *
 * @author shiraki.s
 */
public class TimeKeeper extends TimerTask {
    Logger log = LoggerFactory.getLogger(this.getClass());
    private WebSocketSession session;
    private Boolean isTimeOut;

    public TimeKeeper(WebSocketSession session) {
        this.session = session;
        this.isTimeOut = false;
    }

    @Override
    public void run() {
        if (!isTimeOut) {
            log.info("Translate....");
            isTimeOut = true;
            return;
        }

        try {
            this.session.close();
        } catch (IOException e) {
            log.error("web socket session close ERROR!");
        }

        log.info("session closed.");
    }

}
