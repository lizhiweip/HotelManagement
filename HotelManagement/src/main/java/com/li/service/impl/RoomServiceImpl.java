package com.li.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.mapper.RoomMapper;
import com.li.pojo.Room;
import com.li.service.RoomService;
import org.springframework.stereotype.Service;

/**
 * @Author lzw
 * @Date 2024/1/2 18:48
 * @description
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {
}
