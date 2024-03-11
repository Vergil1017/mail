package com.example.springboot.listener;

import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
@Component
public class MqListener {
    @Value("${mail.fromMail.fromAddress}")
    private String fromAddress;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    RedisTemplate redisTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "queue",declare = "true"),
            exchange = @Exchange(value = "direct",type = ExchangeTypes.DIRECT),
            key = {"send"}
    ))
    public void Listener(User user){
        System.err.println("接收到"+user.getMail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("这是您的注册码");
        message.setFrom(fromAddress);
        message.setTo(user.getMail());
        message.setSentDate(new Date());
        Random r = new Random();
        int i = r.nextInt(1000000);
        message.setText("这是您的注册码：" + i + "\n" + "有效期1分钟");
        try{
            javaMailSender.send(message);
            redisTemplate.opsForValue().set(user.getMail(),i,3, TimeUnit.MINUTES);
        }catch (Exception e){
            System.err.println(e.toString());
        }
    }

}
