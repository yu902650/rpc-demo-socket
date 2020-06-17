package com.demo.service;

import org.example.service.UserService;

/**
 * @author bo bo
 * @Package com.demo.service
 * @date 2020/6/17 22:18
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @Description:
 */
public class UserServiceImpl implements UserService {

    @Override
    public String addUserName(String name) {
        return "添加名字为：" + name;
    }
}
