package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;

public interface UserService extends IService<User> {
    Result sendRegisterCode(User user);

    Result register(User user);

    Result updatePassword(User user);

    Result sendUpdateCode(User user);
}
