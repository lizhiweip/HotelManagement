package com.li.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.common.R;
import com.li.pojo.Reservation;

import java.time.LocalDateTime;

/**
 * @Author lzw
 * @Date 2024/1/2 18:44
 * @description
 */

public interface ReservationService extends IService<Reservation> {
    R<String> bookRoom(String roomNum, LocalDateTime checkInDate, LocalDateTime checkOutDate, String idCardNo);
}
