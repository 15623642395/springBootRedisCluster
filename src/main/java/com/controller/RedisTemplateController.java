package com.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 测试redis集群,开启虚拟机clusterServer
 @author ZHUHAO
 @date 2022-10-29 17:08
 */
@RestController
@Slf4j
public class RedisTemplateController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/setValue")
    public void setValue(@RequestParam String key, @RequestBody Map<String, Object> map) {
        log.info("value:{}",map);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, map);
    }

    @GetMapping("/setPo")
    public Object setPo(@RequestParam String key) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        TestPo test = new TestPo();
        test.setName("祝浩");
        test.setAge("18");
        valueOperations.set(key,test);
        log.info("getValue:{}", JSONObject.toJSONString(test));
        return test;
    }

    @GetMapping("/getValue")
    public Object getValue(@RequestParam String key) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object test = valueOperations.get(key);
        log.info("getValue:{}", JSONObject.toJSONString(test));
        return test;
    }


}

@Data
class TestPo{
    private String name;
    private String age;
}
