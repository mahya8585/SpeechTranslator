package sttApi.translate.speech.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sttApi.redis.Publisher;
import sttApi.redis.RedisConfig;
import sttApi.translate.speech.ResponseHelper;
import sttApi.translate.speech.websocket.timer.TimeManager;

import java.io.IOException;


public class Handler extends TextWebSocketHandler {
    Logger log = LoggerFactory.getLogger(this.getClass());

    private int BINARY_MESSAGE_SIZE = 32000;
    private int TEXT_MESSAGE_SIZE = 32000;

    private byte[] voiceFile;
    private TimeManager timeManager;


    /**
     * ハンドラをWebSocketコネクタに受け渡すときに必要な情報を設定
     *
     * @param sendObject 翻訳対象の音声ファイル
     */
    public Handler(byte[] sendObject) {
        this.voiceFile = sendObject;
        this.timeManager = new TimeManager();
    }


    /**
     * WebSocketコネクション確立後の動作を指定するメソッド
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        session.setBinaryMessageSizeLimit(BINARY_MESSAGE_SIZE);
        session.setTextMessageSizeLimit(TEXT_MESSAGE_SIZE);

        //音声ファイルの送信
        log.info(String.valueOf(voiceFile.length));
        new FileSender().execute(session, voiceFile);
    }

    /**
     * WebSocket session切断後に行う処理
     *
     * @param session
     * @param status
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        log.info("[translator] after connection closed");
        timeManager.cancelTask();

        session.close();
        log.info("[translator] translate end. close connection.");
    }

    /**
     * WebSocketから翻訳結果を受け取る
     * メッセージを取得した場合はタイムアウトまでの時間を延長する
     *
     * @param session WebSocketセッション
     * @param message ハンドルしたメッセージ
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("translator websocket : handleMessage start");

        if (message instanceof TextMessage) {
            log.info("translator return TEXT MESSAGE");
            TextMessage testMessage = (TextMessage) message;
            String response = new ResponseHelper().createResponse(testMessage.getPayload());

            //Redis Publish
            new Publisher().publish(RedisConfig.CHANNEL, response);

            timeManager.extendTask();
        }
        log.info("translator handleMessage end");
    }

}
