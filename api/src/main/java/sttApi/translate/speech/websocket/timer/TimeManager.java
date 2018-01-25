package sttApi.translate.speech.websocket.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.util.Timer;

/**
 * WebSocket接続自動制御用
 * タイマー処理
 */
public class TimeManager {
    Logger log = LoggerFactory.getLogger(this.getClass());

    private Timer timer;
    private Boolean isTimeOut;

    /**
     * msec秒毎にtimeOutの値をチェックし、タイムアウトが発生した場合<br>
     * WebSocketのsessionを切断するTaskを実行する
     * @param session webSocketのsession
     * @param msec タイムアウト発生までの時間(ミリ秒)
     */
    public void startTask(WebSocketSession session, int msec) {
        timer = new Timer();
        timer.schedule(new TimeKeeper(session), 0, msec);
    }

    /**
     * タイマータスクを終了する
     */
    public void cancelTask() {
        if(timer == null) {
            log.info("Timer is NULL");
        } else {
            timer.cancel();
        }
    }

    /**
     * タイムアウトを延長する
     */
    public void extendTask() {
        log.info("extend time task");
        isTimeOut = false;
    }
}
