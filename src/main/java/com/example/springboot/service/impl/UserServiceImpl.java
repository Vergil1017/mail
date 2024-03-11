package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Value("${mail.fromMail.fromAddress}")
    private String fromAddress;
    @Override
    public Result sendRegisterCode(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mail",user.getMail());
        User target = userMapper.selectOne(wrapper);
        if (target != null)
            return Result.error("该邮箱已被使用");
        rabbitTemplate.convertAndSend("direct","send",user);
        return Result.success();
    }

    @Override
    public Result register(User user) {
        Integer code = (Integer) redisTemplate.opsForValue().get(user.getMail());
        if (!user.getCode().equals(code))
            return Result.error("验证码错误");
        userMapper.insert(user);
        return Result.success();
    }

    @Override
    public Result updatePassword(User user) {
        Integer code = (Integer) redisTemplate.opsForValue().get(user.getMail());
        if (!user.getCode().equals(code))
            return Result.error("验证码错误");
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mail",user.getMail());
        userMapper.update(user,wrapper);
        return Result.success();
    }

    @Override
    public Result sendUpdateCode(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mail",user.getMail());
        User target = userMapper.selectOne(wrapper);
        if (target == null)
            return Result.error("该邮箱还未注册");
        rabbitTemplate.convertAndSend("direct","send",user);
        return Result.success();
    }
}
