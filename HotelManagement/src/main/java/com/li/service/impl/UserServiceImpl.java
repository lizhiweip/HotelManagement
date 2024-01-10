package com.li.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.mapper.UserMapper;
import com.li.pojo.User;
import com.li.service.UserService;
import org.springframework.stereotype.Service;


/**
 * @Author lzw
 * @Date 2024/1/2 18:52
 * @description
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
