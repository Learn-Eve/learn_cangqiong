package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 调用微信接口服务，获得当前微信用户的openid
        // 这里直接写死...不想整wx相关的东西
        String openId = "123";

        // 判断openid是否为空，如果为空表示登陆失败，抛出业务异常
        if(openId == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断当前用户是否为新用户
        User user = userMapper.getByOpenId(openId);

        // 如果是新用户，自动完成注册
        if(user == null) {
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();

            userMapper.insert(user);
        }

        // 返回这个用户对象
        return user;
    }
}
