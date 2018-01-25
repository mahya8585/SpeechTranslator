package sttApi.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import static sttApi.redis.RedisConfig.*;


/**
 * RedisによるPublisher
 */
@Component
public class Publisher {
    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Redisのpublish機能
     * 一度の接続で何回もpublishする場合や、1サーバーで複数接続などをする場合は
     * Redis接続処理のプーリングを検討してください
     * @param channel 対象チャネル名
     * @param message 対象メッセージ
     */
    public void publish(String channel, String message) {
        log.info("publish start.");

        JedisShardInfo settings = new JedisShardInfo(HOSTNAME, PORT_NUMBER, USER_SSL);
        //Azure Redis Cacheでいうプライマリアクセスキー
        settings.setPassword(AUTH);

        Jedis jedis = new Jedis(settings);
        jedis.publish(channel, message);
        jedis.quit();

        log.info("publish end.");
    }
}
