package sttApi.translate.speech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import sttApi.translate.speech.auth.Authenticator;
import sttApi.translate.speech.websocket.Handler;
import sttApi.translate.speech.websocket.WebSocketConnector;

import java.util.UUID;

/**
 * Azure FileSender Speech API利用のためのクラス。
 * 前処理・後処理など
 */
@Component
public class RequestService {
    Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * Azure FileSender Speechの固定URL.
     * Security WebSocketで接続していることに注意
     * とりあえずwavに固定
     * TODO application.yamlに移す
     */
    private static final String SPEECH_TO_TEXT_URL_PREFIX = "wss://dev.microsofttranslator.com/speech/translate?from=%1s&to=%2s&api-version=1.0&format=audio/wav";

    /**
     * スターター
     *
     * @param voice 音声wavファイル
     * @return
     */
    public void translate(byte[] voice) {
        String translateSttUrl = createSpeechToTextUrl();
        HttpHeaders headers = createHeaders(new Authenticator().createToken());
        Handler handler = new Handler(voice);

        //翻訳API WSS 接続
        new WebSocketConnector().connect(handler, translateSttUrl, headers).start();
    }


    /**
     * Azure FileSender Speech API呼び出しのためのHttp headerを作成する
     *
     * @param token authトークン
     * @return
     */
    HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();

        //翻訳処理トレースIDとサービスサブスクリプションキーの設定
        String traceId = UUID.randomUUID().toString();
        headers.set("X-ClientTraceId", traceId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        return headers;
    }

    /**
     * Azure FileSender Speech URL getter
     *
     * @return
     */
    String createSpeechToTextUrl() {
        // 今回は日本語のSTTを実施したいため日本語->日本語とする
        //ここをフロント側で設定できるようにすればさまざまな国の言葉の変換を伴ったSTTが可能
        return String.format(SPEECH_TO_TEXT_URL_PREFIX, "ja-JP", "ja-JP");
    }

}
