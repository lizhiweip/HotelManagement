package com.li.dto;

import com.li.pojo.Reservation;
import lombok.Data;

/**
 * @Author lzw
 * @Date 2024/1/5 21:51
 * @description
 */
@Data
public class ReservationDto extends Reservation {
    private String roomNum;
    private String userName;
}
