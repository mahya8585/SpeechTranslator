package sttApi.translate.speech.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.Collections;


/**
 * azure TranslatorSpeechAPIの認証に使用するTokenを生成する
 * @author ishida.m
 *
 */
@Service
public class Authenticator {
    Logger log = LoggerFactory.getLogger(this.getClass());
	private final RestOperations restOperations;
	//TODO application.yamlに移行する
	private static final String ISSUE_TOKEN_URL = "https://api.cognitive.microsoft.com/sts/v1.0/issueToken";

	/**
	 * コンストラクタ
	 */
	public Authenticator(){
		this.restOperations = new RestTemplateBuilder().build();
	}

	/**
	 * azure TranslatorSpeechAPIの認証に使用するtokenを生成する
	 * @return token
	 */
	public String createToken() {
		log.info("start createToken");
        //httpEntityにheaderを設定
        HttpEntity<String> entity = new HttpEntity<>("parameters", createHeaders());

		return restOperations.postForObject(ISSUE_TOKEN_URL, entity, String.class);
	}

    /**
     * Http header の作成
     * @return
     */
	HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        AuthConfig config = new AuthConfig();
        headers.set("Ocp-Apim-Subscription-Key", config.getSubscriptionKey());
        headers.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));
        return headers;
    }
}
