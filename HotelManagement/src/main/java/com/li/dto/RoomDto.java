package com.li.dto;

import com.li.pojo.Room;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author lzw
 * @Date 2024/1/3 16:22
 * @description
 */
@Data
public class RoomDto extends Room {
    private String roomTypeName;
    private BigDecimal price;
}
