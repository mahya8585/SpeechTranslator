package sttApi.translate.speech.websocket;

import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * WebSocket処理
 */
public class WebSocketConnector {

    /**
     * webSocketとのコネクションの確立を行う
     * @param handler websocketハンドラ
     * @param url 接続先URL
     * @return
     */
    public WebSocketConnectionManager connect(TextWebSocketHandler handler, String url, HttpHeaders headers) {

        WebSocketConnectionManager manager = new WebSocketConnectionManager(new StandardWebSocketClient(), handler, url);
        manager.setHeaders(headers);
        manager.setAutoStartup(true);

        return manager;
    }
}
