package com.li.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.mapper.RoomTypeMapper;
import com.li.pojo.RoomType;
import com.li.service.RoomTypeService;
import org.springframework.stereotype.Service;

/**
 * @Author lzw
 * @Date 2024/1/2 18:45
 * @description
 */
@Service
public class RoomTypeServiceImpl extends ServiceImpl<RoomTypeMapper, RoomType> implements RoomTypeService {
}
