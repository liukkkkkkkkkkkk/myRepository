package com.msb.spring.redis.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import javax.naming.Name;
import java.util.Map;

@Component
public class RedisTest {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
   @Qualifier("getMyTemplate")
    StringRedisTemplate myStringRedisTemplate;

    public void testRedis(){
        redisTemplate.opsForValue().set("hello","china");
        System.out.println(redisTemplate.opsForValue().get("hello"));

        RedisConnection con = redisTemplate.getConnectionFactory().getConnection();
        con.set("hello3".getBytes(),"china3".getBytes());
        System.out.println(new String(con.get("hello3".getBytes())));
    }

    public void testStringRedis(){
        stringRedisTemplate.opsForValue().set("hello2","china2");
        System.out.println(stringRedisTemplate.opsForValue().get("hello2"));

        ///测试hash
        HashOperations<String, Object, Object> hashOpr = stringRedisTemplate.opsForHash();
        hashOpr.put("king","name","张三");
        hashOpr.put("king","age","20");
        System.out.println(hashOpr.entries("king"));
    }

    public void testStringRedis02(){
        System.out.println("_____________testStringRedis02________________");
        Person p =new Person();
        p.setAge(20);
        p.setName("zhangsan");
        Jackson2HashMapper jackMapper = new Jackson2HashMapper(objectMapper,false);

        redisTemplate.opsForHash().putAll("sean01",jackMapper.toHash(p));
        Map map = redisTemplate.opsForHash().entries("sean01");
        Person person = objectMapper.convertValue(map, Person.class);
        System.out.println("person:"+person);

        System.out.println("_____________222222222222222222222________________");
    /*    stringRedisTemplate.opsForHash().putAll("sean01",jackMapper.toHash(p));
         map = stringRedisTemplate.opsForHash().entries("sean01");
         person = objectMapper.convertValue(map, Person.class);   // java.lang.Integer cannot be cast to java.lang.String
        System.out.println("person:"+person);*/

    //设置对hash的value值进行序列化的序列化器，解决类型报错问题
       stringRedisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        stringRedisTemplate.opsForHash().putAll("sean01",jackMapper.toHash(p));
        map = stringRedisTemplate.opsForHash().entries("sean01");
        person = objectMapper.convertValue(map, Person.class);   // java.lang.Integer cannot be cast to java.lang.String
        System.out.println("person:"+person);

        System.out.println("~~~~~~~~~~~~~~~~~~03~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        myStringRedisTemplate.opsForHash().putAll("sean02",jackMapper.toHash(p));
        map = stringRedisTemplate.opsForHash().entries("sean02");
        person = objectMapper.convertValue(map, Person.class);
        System.out.println("person:"+person);


        System.out.println("~~~~~~~~~~~~~~~~~~04发布订阅~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        stringRedisTemplate.convertAndSend("ooxx","hello");


        RedisConnection con = stringRedisTemplate.getConnectionFactory().getConnection();
        con.subscribe(new MessageListener() {   //消息来了，异外一个线程里异步回调
            @Override
            public void onMessage(Message message, byte[] bytes) {
                byte[] body =message.getBody();
                System.out.println(new String(body));
            }
        },"ooxx".getBytes());

        while (true){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stringRedisTemplate.convertAndSend("ooxx","hello from me ");

        }
    }

}
