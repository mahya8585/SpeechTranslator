package sttApi.redis;

/**
 * 接続設定値
 * TODO あとでapplication.yamlに移す
 */
public class RedisConfig {
    static final String HOSTNAME = "DDG-CogDev-edge.redis.cache.windows.net";
    static final String AUTH = "HuPV2mJu04q7/5SoNDKrjtUkaVrvJUF2iPIEVENXjNg=";
    static final Integer PORT_NUMBER = 6380;

    /**
     * SSLの利用情報
     * PORT_NUMBERが6380になってるなど、セキュリティ通信を行う場合はture,その他通常通信の場合はfalseの設定を行う
     */
    static final boolean USER_SSL =true;

    /**
     * パブリッシュするチャネル名
     * フロントのsubscriberと統一すること
     */
    public static final String CHANNEL = "meeting";

}
