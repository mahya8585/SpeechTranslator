package sttApi.translate.speech.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import sttApi.translate.speech.websocket.timer.TimeManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;

/**
 * WebSocket ファイル送信処理
 */
public class FileSender {
    Logger log = LoggerFactory.getLogger(this.getClass());

    private final static int BYTE_SIZE = 32000;
    private int TIME_OUT_MSEC = 3000;

    /**
     * 音声ファイルの送信を行う
     *
     * @param session
     */
    public void execute(WebSocketSession session, byte[] target) {
        try {
            log.info("file sender : execute");
            sendVoice(session, new ByteArrayInputStream(target));

            //タイマーを利用してファイルの送信終了を操作する
            new TimeManager().startTask(session, TIME_OUT_MSEC);

        } catch (Exception e) {
            log.info("ERROR! file not send.");
            e.printStackTrace();

            try {
                session.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                log.info("ERROR! WebSocket session close error");
            }
        }
    }

    /**
     * WebSocketへdataの送信を行う
     *
     * @param session     webSocket session
     * @param voiceStream 音声ファイルのInputStream
     */
    void sendVoice(WebSocketSession session, InputStream voiceStream) throws IOException, InterruptedException {
        byte[] buffer = new byte[BYTE_SIZE];
        log.info("voice file send start");

        //音声ファイルの送付
        while (voiceStream.read(buffer, 0, buffer.length) > 0) {
            if (session.isOpen()) {
                log.info("sending...:" + buffer.length);
                session.sendMessage(new BinaryMessage(buffer));

                Thread.sleep(100);
            }
        }
        log.info("end:voice file");

        //無音ファイル(空バイト)を送付することでここで翻訳対象ファイルの送信が完了したことを通知
        log.info("blank file send start");
        IntStream.range(1, 10).forEach(i -> sendBlank(session));

        session.close();
        log.info("end:blank file");
    }

    /**
     * 空ファイルの送信
     *
     * @param session
     */
    void sendBlank(WebSocketSession session) {
        byte[] silenceBytes = new byte[BYTE_SIZE];

        try {
            if (session.isOpen()) {
                log.info("sending...");
                BinaryMessage silence = new BinaryMessage(silenceBytes);
                session.sendMessage(silence);

                Thread.sleep(100);
                log.info("send end");
            }
        } catch (IOException e) {
            log.error("blank file send ERROR!");
        } catch (InterruptedException e) {
            log.error("black file thread sleep ERROR!");
        }
    }
}
