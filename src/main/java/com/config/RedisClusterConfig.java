package com.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis集群
 @author ZHUHAO
 @date 2022-10-28 17:55
 */
@Configuration
@Slf4j
public class RedisClusterConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Autowired
    public Environment environment;

    /**
     * 使用jedis操作redis
     * @return
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        RedisProperties.Pool props = this.redisProperties.getJedis().getPool();
        config.setMaxTotal(props.getMaxActive());
        config.setMaxIdle(props.getMaxIdle());
        config.setMinIdle(props.getMinIdle());
        config.setMaxWaitMillis(props.getMaxWait().toMillis());
        return config;
    }

    /**
     * 创建redis集群连接
     * @return
     */
    @Bean
    public RedisClusterConfiguration redisClusterConfiguration() {
        if (this.redisProperties.getCluster() == null) {
            return null;
        }
        RedisProperties.Cluster clusterProperties = redisProperties.getCluster();
        RedisClusterConfiguration redisClusterConfig = new RedisClusterConfiguration(clusterProperties.getNodes());
        //该集群没有设置密码，一般集群reids设置的密码都一样
        //redisClusterConfig.setPassword(RedisPassword.of("zhuhao"));
        if (clusterProperties.getMaxRedirects() != null) {
            redisClusterConfig.setMaxRedirects(clusterProperties.getMaxRedirects());
        }
        return redisClusterConfig;
    }

    /**
     * 创建工厂
     * @return
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(redisClusterConfiguration(), jedisPoolConfig());
    }

    /**
     * 使用自定义RedisTemplate操作redis
     * https://www.cnblogs.com/csjoz/p/16337145.html
     * https://blog.csdn.net/qq_46848085/article/details/126680946
     * 使用JSON 序列化方式：客户端查看数据结构更清晰,
     * 绝大多数情况下，不推荐使用 JdkSerializationRedisSerializer 进行序列化。
     * 主要是不方便人工排查数据。
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 使用StringRedisSerializer来序列化和反序列化Redis的key值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 使用GenericJackson2JsonRedisSerializer来序列化和反序列化Redis的value值
        GenericJackson2JsonRedisSerializer jscksonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // 配置key，value，hashKey，hashValue的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jscksonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jscksonRedisSerializer);
        //是否开启reis事务
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
