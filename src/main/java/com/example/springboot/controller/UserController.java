package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/sendRegisterCode")
    public Result sendRegisterCode(@RequestBody User user){
        Result result = userService.sendRegisterCode(user);
        if (result.getCode() != "200")
            return Result.error(result.getMsg());
        return Result.success("发送成功");
    }
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        Result result = userService.register(user);
        if (result.getCode() != "200")
            return Result.error(result.getMsg());
        return Result.success("注册成功");
    }
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody User user){
        Result result = userService.updatePassword(user);
        if (result.getCode() != "200")
            return Result.error(result.getMsg());
        return Result.success("修改密码成功");
    }

    @PostMapping("/sendUpdateCode")
    public Result sendUpdateCode(@RequestBody User user){
        Result result = userService.sendUpdateCode(user);
        if (result.getCode() != "200")
            return Result.error(result.getMsg());
        return Result.success("发送成功");
    }
}
