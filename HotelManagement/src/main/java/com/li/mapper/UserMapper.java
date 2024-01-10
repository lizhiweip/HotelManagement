package com.li.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.li.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author lzw
 * @Date 2024/1/2 18:39
 * @description
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
