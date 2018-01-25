package sttApi.translate.speech.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Azure translator speechの認証設定
 * 今後Java9以降に楽して移行していきたいのでlombok未使用
 * @author ishida.m
 *
 */
public class AuthConfig {
    Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * api呼び出しに使用するsubscriptionKey
     * TODO application.yamlに移す
	 */
	private static final String SUBSCRIPTION_KEY = "Azure Speech-translatorAPIのサブスクリプションキーを設定してください";

    public String getSubscriptionKey() {
        log.info("getSubscriptionKey");
        return SUBSCRIPTION_KEY;
    }
}
